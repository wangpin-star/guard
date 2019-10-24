package com.jinglun.guard.attendance.controller;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jinglun.guard.employeeManage.domain.TreeDomain;
import com.jinglun.guard.employeeManage.domain.TreeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.jinglun.guard.attendance.domain.AttendanceInfo;
import com.jinglun.guard.attendance.domain.AttendanceQueryInfo;
import com.jinglun.guard.attendance.domain.FaceRecordInfo;
import com.jinglun.guard.common.utils.ExportXlsx;
import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.PageResult;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.devicemanage.domain.Device.DeviceFlag;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.visitorManage.domain.FaceVisitInfo;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/attendance")
@Slf4j
public class AttendanceController {
	private static int FLAG=0;

	/**
	 * 跳转页面考勤列表页面
	 * 
	 * @return
	 */
	@RequestMapping("/toAttendanceManage")
	public String toAttendanceManage(HttpSession httpSession,Model model) {
		User user=(User) httpSession.getAttribute("user");
		model.addAttribute("user", user);
		return "attendanceManage/attendanceList";
	}

	/***
	 * 考勤记录列表显示
	 * 
	 * @param page          页面数
	 * @param limit         查询记录数
	 * @param searchContent 查询条件
	 * @param departId      部门id
	 * @param starttime     开始时间
	 * @param endtime       结束时间
	 * @param mode          考勤记录状态
	 * @param httpSession
	 * @return pageResult
	 */
	@RequestMapping("/selectAttendanceList")
	@ResponseBody
	public String selectAttendanceList(@RequestParam(value = "page", defaultValue = "1") String page,
			@RequestParam(value = "limit", defaultValue = "10") String limit,
			@RequestParam(value = "searchContent", defaultValue = "") String searchContent,
			@RequestParam(value = "departId", defaultValue = "0") Integer departId,
			@RequestParam(value = "starttime", defaultValue = "") String starttime,
			@RequestParam(value = "endtime", defaultValue = "") String endtime,
			@RequestParam(value = "mode", defaultValue = "0") String mode, HttpSession httpSession) {
		log.info("page={},limit={},searchContent={},departId={}", page, limit, searchContent,departId);
		User user = (User) httpSession.getAttribute("user");
		List<Integer> authList=user.getDepartList();
		Integer start = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
		ResultDomain<List<AttendanceInfo>> faceInfoResultDomain = null;
		AttendanceQueryInfo queryInfo = new AttendanceQueryInfo();
		String starttime_array[] = starttime.split(",");
		String endtime_array[] = endtime.split(",");
		String startdate = starttime_array[starttime_array.length - 1] + " 00:00:00";
		String enddate = endtime_array[endtime_array.length - 1] + " 23:59:59";

		List<Integer> departs = new ArrayList<>();
		if (departId==0){
			queryInfo.setDeparts(authList);
		}else {
			departs.add(departId);
			queryInfo.setDeparts(departs);
		}
		queryInfo.setMode(Integer.parseInt(mode));
		queryInfo.setAttribute(0);
		queryInfo.setStart_time(startdate);
		queryInfo.setEnd_time(enddate);
		queryInfo.setDepart_num(departs.size());

		queryInfo.setQuery_info(searchContent);
		faceInfoResultDomain = DataService.AttendanceQuery(user, queryInfo, start, Integer.parseInt(limit));
		List<AttendanceInfo> list = new ArrayList<>();
		if (faceInfoResultDomain.getResultData() != null && faceInfoResultDomain.getResultData().size() > 0
				&& faceInfoResultDomain.getResultCode() > 0) {
			list = faceInfoResultDomain.getResultData();

			list.forEach(attendanceInfo -> {
				FaceInfo faceInfo = new FaceInfo();
				faceInfo.setFace_id(attendanceInfo.getFace_id());
				faceInfo.setStatus(0);
				faceInfo.setAttribute(0);
				ResultDomain<List<FaceInfo>> listResultDomain = DataService.FaceInfoQuery(user, faceInfo, "", 0, 100);
				if (null != listResultDomain.getResultData() && listResultDomain.getResultData().size() > 0) {
					faceInfo = listResultDomain.getResultData().get(0);
					attendanceInfo.setFaceinfoname(faceInfo.getName());
					attendanceInfo.setEmpno(faceInfo.getEmployee_id());
				} else {
					log.error("该考勤记录反查员工信息错误,员工ID为:{}",attendanceInfo.getFace_id());
				}
			});
		} else if (faceInfoResultDomain.getResultData() == null) {
			log.info("AttendanceQuery查询结果为空");
		}

		PageResult pageResult = new PageResult(faceInfoResultDomain.getResultCode(), list);
		return JSON.toJSONString(pageResult);
	}

	/**
	 * 跳转刷脸流水
	 * 
	 * @return
	 */
	@RequestMapping("/toAttendanceFaceList")
	public String toAttendanceFaceList(Integer face_id,String date,String starttime,String endtime,Model model) {
		model.addAttribute("face_id", face_id);
		model.addAttribute("date", date);
		model.addAttribute("starttime", starttime);
		model.addAttribute("endtime", endtime);
		return "attendanceManage/attendanceFaceList";
	}

	/**
	 * 查询刷脸流水
	 * 
	 * @param httpSession
	 * @param face_id     人脸id
	 * @param page        页面数
	 * @param starttime   开始时间
	 * @param endtime     结束时间
	 * @param limit       查询数量
	 * @return pageResult
	 */
	@RequestMapping("/selectAttendanceFaceList")
	@ResponseBody
	public String selectAttendanceFaceList(HttpSession httpSession, Integer face_id,
			@RequestParam(value = "page", defaultValue = "1") String page,
			@RequestParam(value = "date", defaultValue = "") String date,
			@RequestParam(value = "starttime", defaultValue = "") String starttime,
			@RequestParam(value = "endtime", defaultValue = "") String endtime,
			@RequestParam(value = "limit", defaultValue = "10") String limit) {

		User user = (User) httpSession.getAttribute("user");
		FaceVisitInfo info = new FaceVisitInfo();
		List<FaceRecordInfo> faceInfoList = new ArrayList<>();
		info.setAttribute(0);
		info.setFace_id(face_id);
		info.setVisit_time(date+" "+starttime);
		if (endtime.equals("")) {
			endtime = "23:59:59";
		}
		info.setExpire_time(date+" "+endtime);
		ResultDomain<List<FaceRecordInfo>> faceInfoResultDomain;
		Integer start = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);

		faceInfoResultDomain = DataService.FaceRecordQuery(user, info, start, Integer.parseInt(limit));
		if (null != faceInfoResultDomain.getResultData() && faceInfoResultDomain.getResultData().size() > 0) {
			faceInfoList = faceInfoResultDomain.getResultData();
		} else {
			log.info("查询考勤流水错误");
		}

		List<FaceRecordInfo> list = faceInfoResultDomain.getResultData();
		if (faceInfoResultDomain.getResultData() != null && faceInfoResultDomain.getResultData().size() > 0
				&& faceInfoResultDomain.getResultCode() > 0) {
			Device device = new Device();
			device.setTerm_id(0);
			device.setDel_flag(DeviceFlag.ALL);
			ResultDomain<List<Device>> listResultDomain = DataService.TermInfoQuery(user, device, "");
			List<Device> devices = listResultDomain.getResultData();

			Map<Integer, String> devicesMap = devices.stream()
					.collect(Collectors.toMap(Device::getTerm_id, Device::getTerm_name));
			list.forEach(e -> e.setTerm_name(devicesMap.get(e.getTerm_id())));
		} else if (faceInfoResultDomain.getResultData() == null) {
			log.info("查询访客来访信息为空值");
		}

		PageResult pageResult = new PageResult(faceInfoResultDomain.getResultCode(), faceInfoList);
		return JSON.toJSONString(pageResult);
	}

	/**
	 * 跳转导出页面
	 * 
	 * @return
	 */
	@RequestMapping("/toAttendanceExport")
	public String toExportAttendance(Model model, HttpSession session) {
		FaceInfo faceInfo = new FaceInfo();
		User user = (User) session.getAttribute("user");
		List<Integer> authlist = user.getDepartList();
		faceInfo.setAttribute(0);
		faceInfo.setStatus(0);
		List<FaceInfo> resultData;
		ResultDomain<List<FaceInfo>> listResultDomain = DataService.FaceListGet(user, faceInfo,authlist);
		if (null != listResultDomain.getResultData()) {
			resultData = listResultDomain.getResultData();
			model.addAttribute("employeeList", resultData);
		} else {
			log.info("搜索全部faceInfo出错");
		}
		ResultDomain<List<Depart>> list = DataService.DepartInfoQueryByAuth(user, -1, 0);
		List<Depart> departs = list.getResultData();
		List<Integer> integers = new ArrayList<>();
		for (Depart depart : departs) {
			int i = depart.getDepart_id();
			integers.add(i);
		}
		model.addAttribute("ids", integers);
		session.setAttribute("departids", integers);
		FLAG=0;
		return "attendanceManage/attendanceExport";
	}

	/**
	 * 显示权限配置部门树
	 * @param httpSession
	 * @return
	 */
	@RequestMapping("/selectDepartTree")
	@ResponseBody
	public String selectDepartTree(HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");

		TreeDomain treeDomain = getDepartTree(user);

		return JSON.toJSONString(treeDomain);
	}

	public TreeDomain getDepartTree(User user) {
		ResultDomain<List<Depart>> listResultDomain = DataService.DepartInfoQueryByAuth(user, -1, 0);
		TreeDomain treeDomain = new TreeDomain(0, user.getCompany_name(),user.getCompany_name(),0,null);
		if (null != listResultDomain.getResultData() && listResultDomain.getResultCode() == 0
				&& listResultDomain.getResultData().size() > 0) {
			List<Depart> resultData = listResultDomain.getResultData();
			ArrayList<TreeDomain> treeDomainList = new ArrayList<>();
			for (Depart resultDatum : resultData) {
				TreeDomain domain = new TreeDomain();
				domain.setData(resultDatum);
				domain.setDepart_id(resultDatum.getDepart_id());
				domain.setParentId(resultDatum.getParent_depart_id());
				domain.setName(resultDatum.getDepart_name());
				treeDomainList.add(domain);
			}
			treeDomain = TreeUtil.getTree(treeDomain, treeDomainList);
		} else {
			log.info("查询部门树错误");
		}
		return treeDomain;
	}

	public static Map<String, Object> getParameterMap(HttpServletRequest request) {
		// 参数Map
		Map properties = request.getParameterMap();
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		StringBuilder paramsb = new StringBuilder();
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			paramsb.append(name).append(":").append(value).append(" ");
			returnMap.put(name, value);
		}
		return returnMap;
	}

	/**
	 * 导出
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/exportAttendanceList")
	public void exportPlayExcel(HttpServletRequest request, HttpServletResponse response, Model model,
			HttpSession session) throws Exception {
		User user = (User) session.getAttribute("user");
		//获取所有部门id
		List<Integer> allDepartIdList = (List<Integer>) session.getAttribute("departids");
		List<Integer> departIdList = new ArrayList<>();
		//导出方式,1是按部门导出,2是按人员导出
		String exportMode = request.getParameter("exportMode");
		//导出的人员id,为空字符串可能是按部门导出也可能是按全部人员导出
		String employeeId = request.getParameter("employee");
		//导出的部门id拼接的字符串
		String departIds = request.getParameter("tree");
		//导出的开始日期
		String startDate = request.getParameter("startdate") + " 00:00:00";
		//导出的结束日期
		String endDate = request.getParameter("enddate") + " 23:59:59";
		//是否导出详细考勤,为null就是非详细考勤,不为null就是详细考勤
		String detail = request.getParameter("detail");
		//存在考勤记录,为null就是导出有考勤数据的记录
		String exist = request.getParameter("exist");
		//是否导出部门
		String table3 = request.getParameter("table3");
		//是否导出首次登记地点
		String table6 = request.getParameter("table6");
		//是否导出工号
		String table7 = request.getParameter("table7");
		//是否导出末次登记地点
		String table8 = request.getParameter("table8");
		AttendanceQueryInfo queryInfo = new AttendanceQueryInfo();
		queryInfo.setAttribute(0);
		queryInfo.setStart_time(startDate);
		queryInfo.setEnd_time(endDate);
		if (null != exist) {
			//0：所有,1：返回有考勤数据的记录
			queryInfo.setMode(1);
		} else {
			queryInfo.setMode(0);
		}
		ResultDomain<List<AttendanceInfo>> attendanceInfoResultDomain = new ResultDomain<>();
		ResultDomain<List<AttendanceInfo>> attendanceInfoResultDomainAdd;
		List<AttendanceInfo> attendanceInfoList = new ArrayList<>();
		List<Map<String, Object>> lMaps = new ArrayList<>();
		if (null == detail) {//非详细
			if (exportMode.equals("2") && !employeeId.equals("")) {//按人员导出,且有人员id
				queryInfo.setFace_id(Integer.parseInt(employeeId));
				queryInfo.setDeparts(new ArrayList<>());
				attendanceInfoResultDomain = DataService.AttendanceQuery(user, queryInfo, 0, -1);
				if (attendanceInfoResultDomain.getResultCode() > 200) {
					for (int i = 1; i <= attendanceInfoResultDomain.getResultCode() / 200; i++) {
						attendanceInfoResultDomainAdd = DataService.AttendanceQuery(user, queryInfo, 200 * i, -1);
						attendanceInfoResultDomain.getResultData().addAll(attendanceInfoResultDomainAdd.getResultData());
					}
				}
			} else if (exportMode.equals("2") && employeeId.equals("")) {//按人员导出,且是全部人员
				queryInfo.setDeparts(allDepartIdList);
				queryInfo.setDepart_num(allDepartIdList.size());
				queryInfo.setQuery_info("");
				attendanceInfoResultDomain = DataService.AttendanceQuery(user, queryInfo, 0, -1);
				if (attendanceInfoResultDomain.getResultCode() > 200) {
					for (int i = 1; i <= attendanceInfoResultDomain.getResultCode() / 200; i++) {
						attendanceInfoResultDomainAdd = DataService.AttendanceQuery(user, queryInfo, 200 * i, -1);
						attendanceInfoResultDomain.getResultData().addAll(attendanceInfoResultDomainAdd.getResultData());
					}
				}
			} else if (exportMode.equals("1")) {//按部门导出
				if (departIds.equals("")) {//选择的部门id为空,就是全部部门
					departIdList = allDepartIdList;
				} else {
					String[] departIdArr = departIds.split(",");
					for (String departId : departIdArr) {
						departIdList.add(Integer.parseInt(departId));
					}
				}
				queryInfo.setDeparts(departIdList);
				queryInfo.setDepart_num(departIdList.size());
				queryInfo.setQuery_info("");
				attendanceInfoResultDomain = DataService.AttendanceQuery(user, queryInfo, 0, -1);
				if (attendanceInfoResultDomain.getResultCode() > 200) {
					for (int i = 1; i <= attendanceInfoResultDomain.getResultCode() / 200; i++) {
						attendanceInfoResultDomainAdd = DataService.AttendanceQuery(user, queryInfo, 200 * i, -1);
						attendanceInfoResultDomain.getResultData().addAll(attendanceInfoResultDomainAdd.getResultData());
					}
				}
			} else {
				log.error("导出模式错误");
			}
			if (attendanceInfoResultDomain != null && attendanceInfoResultDomain.getResultData() != null) {
				attendanceInfoList = attendanceInfoResultDomain.getResultData();
			}
			if (attendanceInfoList != null) {
				for (AttendanceInfo attendanceInfo : attendanceInfoList) {
					LinkedHashMap<String, Object> map = new LinkedHashMap<>();
					// 姓名
					map.put("name", attendanceInfo.getName());
					// 开启工号
					if (null != table7 && table7.equals("on")) {
						map.put("empno", attendanceInfo.getEmpno());
					}
					// 开启部门
					if (null != table3 && table3.equals("on")) {
						map.put("depart", attendanceInfo.getDepart());
					}
					// 日期
					map.put("date", attendanceInfo.getDate());
					// 首次登记时间
					map.put("first_time", attendanceInfo.getFirst_time());
					// 末次登记时间
						map.put("last_time", attendanceInfo.getLast_time());
					// 开启首次登记地点
					if (null != table6 && table6.equals("on")) {
						map.put("first_term_id", attendanceInfo.getFirst_term_name());
					}
					// 开启末次登记地点
					if (null != table8 && table8.equals("on")) {
						map.put("last_term_id", attendanceInfo.getLast_term_name());
					}
					lMaps.add(map);
				}
			} else {
				log.info("查询考勤信息为空值");
			}
		}
		ResultDomain<List<FaceRecordInfo>> faceRecordResultDomain;
		ResultDomain<List<FaceRecordInfo>> faceRecordResultDomainAdd;
		List<FaceRecordInfo> faceRecordInfos = new ArrayList<>();
		if (null != detail) {//导出详细考勤
			if (exportMode.equals("2") && !employeeId.equals("")) {//按人员导出
				FaceVisitInfo info = new FaceVisitInfo();
				info.setAttribute(0);
				info.setFace_id(Integer.parseInt(employeeId));
				info.setVisit_time(startDate);
				info.setExpire_time(endDate);
				faceRecordResultDomain = DataService.FaceRecordQuery(user, info, 0, -1);
				if (faceRecordResultDomain.getResultCode() > 200) {
					for (int i = 1; i <= faceRecordResultDomain.getResultCode() / 200; i++) {
						faceRecordResultDomainAdd = DataService.FaceRecordQuery(user, info, 200*i, -1);
						faceRecordResultDomain.getResultData().addAll(faceRecordResultDomainAdd.getResultData());
					}
				}
				if (null != faceRecordResultDomain && null != faceRecordResultDomain.getResultData()
						&& faceRecordResultDomain.getResultData().size() > 0
						&& faceRecordResultDomain.getResultCode() > 0) {
					faceRecordInfos = faceRecordResultDomain.getResultData();
					Device device = new Device();
					device.setTerm_id(0);
					device.setDel_flag(DeviceFlag.ALL);
					ResultDomain<List<Device>> listResultDomain = DataService.TermInfoQuery(user, device, "");
					List<Device> devices = listResultDomain.getResultData();

					Map<Integer, String> devicesMap = devices.stream()
							.collect(Collectors.toMap(Device::getTerm_id, Device::getTerm_name));
					faceRecordInfos.forEach(e -> e.setTerm_name(devicesMap.get(e.getTerm_id())));
				} else {
					log.info("查询访客来访信息为空值");
				}
			} else if (exportMode.equals("2") && employeeId.equals("")) {
				for (Integer depart : allDepartIdList) {
					FaceInfo faceInfo = new FaceInfo();
					faceInfo.setAttribute(0);
					faceInfo.setStatus(0);
					faceInfo.setDepart_id(depart);
					ResultDomain<List<FaceInfo>> faceinfoResutDomainList = DataService.FaceInfoQuery(user, faceInfo, "", 0, -1);
					List<FaceInfo> faceInfoList = faceinfoResutDomainList.getResultData();
					if (faceInfoList != null) {
						for (FaceInfo faceInfo1 : faceInfoList) {
							FaceVisitInfo info = new FaceVisitInfo();
							info.setAttribute(0);
							info.setFace_id(faceInfo1.getFace_id());
							info.setVisit_time(startDate);
							info.setExpire_time(endDate);
							faceRecordResultDomain = DataService.FaceRecordQuery(user, info, 0, -1);
							if (faceRecordResultDomain.getResultCode() > 200) {
								for (int i = 1; i <= faceRecordResultDomain.getResultCode() / 200; i++) {
									faceRecordResultDomainAdd = DataService.FaceRecordQuery(user, info, 200*i, -1);
									faceRecordResultDomain.getResultData().addAll(faceRecordResultDomainAdd.getResultData());
								}
							}
							if (null != faceRecordResultDomain.getResultData()
									&& faceRecordResultDomain.getResultData().size() > 0
									&& faceRecordResultDomain.getResultCode() > 0) {
								faceRecordInfos.addAll(faceRecordResultDomain.getResultData());
								Device device = new Device();
								device.setTerm_id(0);
								device.setDel_flag(DeviceFlag.ALL);
								ResultDomain<List<Device>> listResultDomain = DataService.TermInfoQuery(user, device, "");
								List<Device> devices = listResultDomain.getResultData();
								Map<Integer, String> devicesMap = devices.stream()
										.collect(Collectors.toMap(Device::getTerm_id, Device::getTerm_name));
								faceRecordInfos.forEach(e -> e.setTerm_name(devicesMap.get(e.getTerm_id())));
							}
						}
					}
				}
			} else if (exportMode.equals("1")){
				if (departIds.equals("")) {
					departIdList = allDepartIdList;
				} else {
					String[] departIdArr = departIds.split(",");
					for (String departId : departIdArr) {
						departIdList.add(Integer.parseInt(departId));
					}
				}
				for (Integer depart : departIdList) {
					FaceInfo faceInfo = new FaceInfo();
					faceInfo.setAttribute(0);
					faceInfo.setStatus(0);
					faceInfo.setDepart_id(depart);
					ResultDomain<List<FaceInfo>> faceinfoResutDomainList = DataService.FaceInfoQuery(user, faceInfo, "", 0, -1);
					List<FaceInfo> faceInfoList = faceinfoResutDomainList.getResultData();
					if (faceInfoList != null) {
						for (FaceInfo faceInfo1 : faceInfoList) {
							FaceVisitInfo info = new FaceVisitInfo();
							info.setAttribute(0);
							info.setFace_id(faceInfo1.getFace_id());
							info.setVisit_time(startDate);
							info.setExpire_time(endDate);
							faceRecordResultDomain = DataService.FaceRecordQuery(user, info, 0, -1);
							if (faceRecordResultDomain.getResultCode() > 200) {
								for (int i = 1; i <= faceRecordResultDomain.getResultCode() / 200; i++) {
									faceRecordResultDomainAdd = DataService.FaceRecordQuery(user, info, 200*i, -1);
									faceRecordResultDomain.getResultData().addAll(faceRecordResultDomainAdd.getResultData());
								}
							}
							if (null != faceRecordResultDomain.getResultData()
									&& faceRecordResultDomain.getResultData().size() > 0
									&& faceRecordResultDomain.getResultCode() > 0) {
								faceRecordInfos.addAll(faceRecordResultDomain.getResultData());
								Device device = new Device();
								device.setTerm_id(0);
								device.setDel_flag(DeviceFlag.ALL);
								ResultDomain<List<Device>> listResultDomain = DataService.TermInfoQuery(user, device, "");
								List<Device> devices = listResultDomain.getResultData();
								Map<Integer, String> devicesMap = devices.stream()
										.collect(Collectors.toMap(Device::getTerm_id, Device::getTerm_name));
								faceRecordInfos.forEach(e -> e.setTerm_name(devicesMap.get(e.getTerm_id())));
							}
						}
					}
				}
			}
			for (FaceRecordInfo faceRecordInfo1 : faceRecordInfos) {
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				// 姓名
				map.put("name", faceRecordInfo1.getName());
				// 身份证号
				map.put("idcard", faceRecordInfo1.getIdcard());
				// 设备名称
				map.put("term_name", faceRecordInfo1.getTerm_name());
				// 登记时间
				map.put("time", faceRecordInfo1.getTime());
				lMaps.add(map);
			}
		}
		if (!lMaps.isEmpty()) {
			ByteArrayInputStream inputStream1 = customExcel(lMaps);
			ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
			int inRead1;
			while ((inRead1 = inputStream1.read()) != -1) {
				outputStream1.write(inRead1);
			}
			Map<String, Object> retMap1 = new HashMap<String, Object>();
			retMap1.put("fileName", "考勤信息_" + System.currentTimeMillis() + ".xlsx");
			retMap1.put("data", outputStream1.toByteArray());
			ServletOutputStream outStream1 = null;
			String agent="User-Agent";
			String utf="UTF-8";
			try {
				byte[] exportData = (byte[]) retMap1.get("data");
				String fileName = (String) retMap1.get("fileName");
				if (request.getHeader(agent).toLowerCase().indexOf("firefox") >= 0) {
					// firefox浏览器
					fileName = new String(fileName.getBytes(utf), "ISO8859-1");
				} else if (request.getHeader(agent).toUpperCase().indexOf("MSIE") >= 0) {
					// IE浏览器
					fileName = URLEncoder.encode(fileName, utf);
				} else if (request.getHeader(agent).toUpperCase().indexOf("CHROME") >= 0) {
					// 谷歌
					fileName = new String(fileName.getBytes(utf), "ISO8859-1");
				} else if (request.getHeader(agent).indexOf("like Gecko") >= 0) {
					fileName = URLEncoder.encode(fileName, utf);
				}
				response.setContentType("application/xlsx;charset=UTF-8");
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
				outStream1 = response.getOutputStream();
				outStream1.write(exportData);
				outStream1.flush();
				outStream1.close();
				FLAG=1;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("查询考勤信息错误");
			}
		} else {
			response.setContentType("text/html; charset=UTF-8"); //转码
			PrintWriter out = response.getWriter();
			out.flush();
			out.println("<script type='text/javascript' src='/js/jquery.min.js'></script>" +
					"	<script type='text/javascript' src='/layui/layui.js'></script>");
			out.println("<script>");
			out.println("layui.use(['layer'], function() {"
					+ "var layer = layui.layer;"
					+ "layer.alert('未查询到符合条件的考勤记录',{icon: 0,title:'提示'}, function(index){"
					+ "parent.layer.closeAll();"
					+ "}); "
					+ "});");
			out.println("</script>");
			out.flush();
			out.close();
			log.info("查询考勤信息为空");
		}
	}

	public static Map<String, Object> convertBeanToMap(Object object) {
		if (object == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				if (!key.equals("class")) {
					Method getter = property.getReadMethod();
					Object value = getter.invoke(object);
					map.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	
	// 返回全局变量，显示进度
		@RequestMapping("/progress")
		@ResponseBody
		public int excelProgress() throws IOException {
			return FLAG;
		}

	/**
	 * @param list
	 * @return
	 * @throws Exception
	 */
	private ByteArrayInputStream customExcel(List<Map<String, Object>> list) throws Exception {

		List<Integer> integers = new ArrayList<Integer>();
		List<String> list2 = new ArrayList<String>();
		int i = 0;
		Map<String, Object> map = list.get(0);

		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		i = map.size();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			list2.add(entry.getKey());

		}
		for (int j = 0; j < i; j++) {
			integers.add(5000);
		}

		Integer[] colWidths = integers.toArray(new Integer[integers.size()]);

		String[] headernum = list2.toArray(new String[list2.size()]);
		String[] headernum2 = list2.toArray(new String[list2.size()]);
		String[] headers = headernum;
		for (int j = 0; j < headernum.length; j++) {
			if (headernum[j].equals("date")) {
				headers[j] = "日期";
			}
			if (headernum[j].equals("faceinfoname")) {
				headers[j] = "姓名";
			}
			if (headernum[j].equals("empno")) {
				headers[j] = "员工号";
			}
			if (headernum[j].equals("depart")) {
				headers[j] = "部门";
			}
			if (headernum[j].equals("first_term_id")) {
				headers[j] = "首次登记地点";
			}
			if (headernum[j].equals("first_time")) {
				headers[j] = "首次登记时间";
			}
			if (headernum[j].equals("last_time")) {
				headers[j] = "末次登记时间";
			}
			if (headernum[j].equals("last_term_id")) {
				headers[j] = "末次登记地点";
			}

			if (headernum[j].equals("name")) {
				headers[j] = "姓名";
			}
			if (headernum[j].equals("idcard")) {
				headers[j] = "身份证号";
			}
			if (headernum[j].equals("term_name")) {
				headers[j] = "设备名称";
			}
			if (headernum[j].equals("time")) {
				headers[j] = "时间";
			}

		}

		String title = "导出";
		ExportXlsx<Map<String, Object>> exportXlsx = new ExportXlsx<Map<String, Object>>();
		return exportXlsx.exportXlsx(title, headers, list, "YYYY-MM-DD HH24:MI:SS", headernum2, colWidths);
	}

}
