package com.jinglun.guard.visitorManage.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jinglun.guard.attendance.domain.FaceRecordInfo;
import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.PageResult;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.employeeManage.service.impl.EmployeeManageServiceImpl;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.systemManage.domain.VisitReason;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.visitorManage.domain.AddBatParam;
import com.jinglun.guard.visitorManage.domain.BatVisitor;
import com.jinglun.guard.visitorManage.domain.FaceVisitInfo;
import com.jinglun.guard.visitorManage.domain.VisitQueryInfo;
import com.jinglun.guard.visitorManage.domain.VisitorDetailShow;
import com.jinglun.guard.visitorManage.service.VisitorManagerService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/visitorManage")
@Slf4j
public class VisitorManagerController {

	@Resource(name = "employeeManageServiceImpl")
	private EmployeeManageServiceImpl employeeManageServiceImpl;

	@Autowired
	private VisitorManagerService visitormanagerService;


	private static final String VISITDATASTAUS = "visitdatastaus";
	
	@RequestMapping("/toVisitorManage")
	public String toVisitorManage(HttpSession session,Model model) {
		User user = (User) session.getAttribute("user");
		//获取初始修订号
		String revo = "visit_info_version";
		String visitdatastaus = visitormanagerService.queryDataStatus(user,revo);
		session.setAttribute(VISITDATASTAUS, visitdatastaus);
		//获取访客列表自动刷新的时间参数
		int time = visitormanagerService.queryVisitrefreshTime(user);
		
		model.addAttribute("time", time);
		model.addAttribute("user", user);
		return "visitorManage/visitor";
	}

	/**
	 * 多人来访管理,点击某一条记录的"登记"按钮,会执行此方法
	 * @param bat_id 批量id
	 * @return 跳转到登记页面
	 */
	@RequestMapping("/StartRegist1")
	public String StartRegist1(HttpSession session,Model model,Integer bat_id) {
		User user = (User) session.getAttribute("user");
		//start:根据批量Id查询批量信息
		BatVisitor batVisitor = new BatVisitor();
		batVisitor.setBat_id(bat_id);
		ResultDomain<List<BatVisitor>> batVisitorResultDomain = DataService.BatVisitQuery(user, batVisitor, 0, -1);
		if (null != batVisitorResultDomain && null != batVisitorResultDomain.getResultData()) {
			batVisitor = batVisitorResultDomain.getResultData().get(0);
		}
		//end:根据批量Id查询批量信息
		//start:根据批量Id查询登记记录
		List<FaceVisitInfo> faceVisitInfoList = new ArrayList<>();
		FaceVisitInfo faceVisitInfo = new FaceVisitInfo();
		faceVisitInfo.setVisit_num(-1);
		faceVisitInfo.setBat_id(bat_id);
		ResultDomain<List<FaceVisitInfo>> faceVisitInfoResultDomain = DataService.VisitRecordQuery(user, faceVisitInfo, 0);
		if (null != faceVisitInfoResultDomain && null != faceVisitInfoResultDomain.getResultData() && faceVisitInfoResultDomain.getResultData().size() > 0) {
			faceVisitInfoList = faceVisitInfoResultDomain.getResultData().stream().filter(f -> f.getStatus() != 2).collect(Collectors.toList());
		}
		//end:根据批量Id查询登记记录
		//start:获取此批量的设备权限
		String termIdList = new ArrayList<Integer>().toString();
		if (null != batVisitor.getTerm_list() && batVisitor.getTerm_list().size() > 0) {
			termIdList = batVisitor.getTerm_list().toString();
		}
		//end:获取此批量的设备权限
		model.addAttribute("batVisitor", batVisitor);
		model.addAttribute("termIdList", termIdList);
		model.addAttribute("faceVisitInfoList", faceVisitInfoList);
		return "visitorManage/visitorRegist";
	}

	@RequestMapping("/showbatVisitor")
	public String showbatDevice(Model model) {
		return "visitorManage/batchQuery";
	}

	/**
	 * 删除来访记录
	 * @param faceVisitInfo
	 * @param httpSession
	 * @return
	 */
    @RequestMapping("/deleteVisitorRegister")
    @ResponseBody
    public String deleteVisitorRegister(FaceVisitInfo faceVisitInfo,HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        ResultDomain resultDomain = DataService.VisitRecordDelete(user, faceVisitInfo);
        if (resultDomain.getResultCode() == 0) {
            return "1";
        } else {
            log.info("VisitRecordDelete接口出错");
            return "0";
        }
    }

	@RequestMapping("/visitRecord")
	public String visitRecord(Model model, int bat_id, HttpSession session, int face_id, String name, String visit_time,
			String expire_time) {
		session.setAttribute("empName", name);
		session.setAttribute("visits_time", visit_time);
		session.setAttribute("expires_time", expire_time);
		model.addAttribute("visit_time", visit_time);
		model.addAttribute("expire_time", expire_time);
		model.addAttribute("bat_id", bat_id);
		return "visitorManage/visitorRecord";
	}

	@RequestMapping("/visitRecordQuery")
	@ResponseBody
	public String visitRecordQuery(HttpSession session,int bat_id) {
		
		User user = (User) session.getAttribute("user");
		FaceVisitInfo queryInfo = new FaceVisitInfo();
		String name = (String) session.getAttribute("empName");
		String expiretime = (String) session.getAttribute("expires_time");
		queryInfo.setBat_id(bat_id);
		queryInfo.setRec_id(0);
		queryInfo.setFace_id(0);
		queryInfo.setName(name);
		queryInfo.setVisit_time("2018-03-12 18:00:00");
		queryInfo.setExpire_time(expiretime);
		queryInfo.setVisit_num(10);
		PageResult pageResult = new PageResult();
		List<FaceVisitInfo> visitInfo=new ArrayList<>();
		ResultDomain<List<FaceVisitInfo>> faceVisitInfo = DataService.VisitRecordQuery(user, queryInfo, 0);
		
		//员工姓名
		List<FaceInfo> faceInfos=new ArrayList<>();
		FaceInfo faceInfo = new FaceInfo();
	    faceInfo.setAttribute(0);
	    faceInfo.setStatus(0);
	    ResultDomain<List<FaceInfo>> listResultDomain = DataService.FaceListGet(user, faceInfo,user.getDepartList());
	    if(null!=listResultDomain.getResultData()&&listResultDomain.getResultData().size()>0) {
			faceInfos = listResultDomain.getResultData();
		}
	    //访客姓名
	    List<FaceInfo> faceInfos1=new ArrayList<>();
		FaceInfo faceInfo1 = new FaceInfo();
	    faceInfo1.setAttribute(1);
	    faceInfo1.setStatus(0);
	    ResultDomain<List<FaceInfo>> listResultDomain1 = DataService.FaceListGet(user, faceInfo1,user.getDepartList());
	    if(null!=listResultDomain1.getResultData()&&listResultDomain1.getResultData().size()>0) {
			faceInfos1 = listResultDomain1.getResultData();
		}
	    
		if (null != faceVisitInfo.getResultData() && !faceVisitInfo.getResultData().isEmpty()) {
			if(null!=listResultDomain.getResultData()&&!listResultDomain.getResultData().isEmpty()) {
				faceInfos = listResultDomain.getResultData();
			}
			visitInfo = faceVisitInfo.getResultData().stream().filter(batvisitor -> batvisitor.getStatus()== 0).collect(Collectors.toList());
			Map<Integer, String> faceinfoMapEmp = faceInfos.stream().collect(Collectors.toMap(FaceInfo::getFace_id, FaceInfo::getName));
			Map<Integer, String> faceinfoMapVist = faceInfos1.stream().collect(Collectors.toMap(FaceInfo::getFace_id, FaceInfo::getName));
			for(FaceVisitInfo info:visitInfo){
				long l = System.currentTimeMillis();
				Date date = new Date(l);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = dateFormat.format(date);
				info.setCurrent_time(time);
				if(info.getAttribute()==0){
					info.setName(faceinfoMapEmp.get(info.getFace_id()));
				}else{
					info.setName(faceinfoMapVist.get(info.getFace_id()));
				}
			}
		} else {
			log.info("访客记录为空");
		}
		pageResult.setData(visitInfo);
		return JSON.toJSONString(pageResult);
	}

	@RequestMapping("/visitRecordQuery1")
	@ResponseBody
	public String visitRecordQuery1(HttpSession session,FaceVisitInfo queryInfo) {
		User user = (User) session.getAttribute("user");
		PageResult pageResult = new PageResult();
		List<FaceVisitInfo> faceVisitInfoList = new ArrayList<>();
		ResultDomain<List<FaceVisitInfo>> faceVisitInfo = DataService.VisitRecordQuery(user, queryInfo, 0);
		if (null != faceVisitInfo.getResultData() && !faceVisitInfo.getResultData().isEmpty()) {
			faceVisitInfoList = faceVisitInfo.getResultData().stream().filter(batvisitor -> batvisitor.getStatus()== 0).collect(Collectors.toList());
		} else {
			log.info("访客记录为空");
		}
		pageResult.setData(faceVisitInfoList);
		return JSON.toJSONString(pageResult);
	}

	@RequestMapping("/batchQuery")
	@ResponseBody
	public String batchQuery(@RequestParam(value = "page", defaultValue = "1") String page,
			@RequestParam(value = "limit", defaultValue = "10") String limit, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Integer start = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
		PageResult pageResult = visitormanagerService.batQuery(user, start, Integer.parseInt(limit));
		return JSON.toJSONString(pageResult);
	}

	@RequestMapping("/batchDel")
	@ResponseBody
	public int batchDel(HttpSession session, int bat_id) {
		User user = (User) session.getAttribute("user");
		return visitormanagerService.batDel(user, bat_id);
	}

	@RequestMapping("/visitorDel")
	@ResponseBody
	public int visitorDel(HttpSession session, int recId) {
		User user = (User) session.getAttribute("user");
		return visitormanagerService.visitorDel(user, recId);
	}

	@RequestMapping("/batqueryDevice")
	@ResponseBody
	public String batqueryDeviceList(HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		int flag=(Integer)httpSession.getAttribute("flag");
		List<Device> devicelist = employeeManageServiceImpl.queryAllDevice(user);
		PageResult pageResult = new PageResult();
		List<Integer> term_list=new ArrayList<>();
		if(flag==1) {
			term_list= (List<Integer>) httpSession.getAttribute("Eterm_list");
			if(null==term_list){
	        	for (int j = 0; j < devicelist.size(); j++) {
	        		devicelist.get(j).setLAY_CHECKED(false);
	        	}
	        }
			else {
				for (int i = 0; i < term_list.size(); i++) {
					for (int j = 0; j < devicelist.size(); j++) {
						if (devicelist.get(j).getTerm_id() == term_list.get(i)) {
							devicelist.get(j).setLAY_CHECKED(true);
							break;
						}
					}
				}
			}
		}
		
		if(flag==2) {
			term_list= (List<Integer>) httpSession.getAttribute("Dterm_list");
			if(null==term_list){
	        	for (int j = 0; j < devicelist.size(); j++) {
	        		devicelist.get(j).setLAY_CHECKED(false);
	        	}
	        }
			else {
				for (int i = 0; i < term_list.size(); i++) {
					for (int j = 0; j < devicelist.size(); j++) {
						if (devicelist.get(j).getTerm_id() == term_list.get(i)) {
							devicelist.get(j).setLAY_CHECKED(true);
							break;
						}
					}
				}
			}
		}
		pageResult.setData(devicelist);
		 
		return JSON.toJSONString(pageResult).replace("lAY_CHECKED", "LAY_CHECKED");
	}

	@RequestMapping("/visitorDetail")
	public String batchQuery(Model model, HttpSession session, Integer bat_id) {

		User user = (User) session.getAttribute("user");
		BatVisitor data = visitormanagerService.batQuerybyId(user, bat_id);
		String name = "";
		String namemodel = "";
		session.setAttribute("Dterm_list", data.getTerm_list());
		if(null!=data.getTerm_list()) {
			
			List<Device> devicelist = employeeManageServiceImpl.queryAllDevice(user);
			for (int i = 0; i < data.getTerm_list().size(); i++) {
				for (int j = 0; j < devicelist.size(); j++) {
					int termlistId = data.getTerm_list().get(i);
					int termId = devicelist.get(j).getTerm_id();
					if (termlistId == termId) {
						name += devicelist.get(j).getTerm_name();
						
						int terModel = devicelist.get(j).getTerm_model();
						if(terModel==0){
							namemodel += Integer.toString(4);//未知类型
						}else if(terModel==1||terModel==2||terModel==3||terModel==4||terModel==5||terModel==6||terModel==9){
							namemodel += Integer.toString(3);//iDR类型
						}else if(terModel==100){
							namemodel += Integer.toString(2);//facecheck动态类型
						}else{
							namemodel += Integer.toString(1);//14T类型
						}
						
						if (i < data.getTerm_list().size() - 1) {
							name += ",";
							namemodel += ",";
						}
						break;
					}
					
				}
			}
			model.addAttribute("term_list", data.getTerm_list());
		}
		else {
			model.addAttribute("term_list","null");
		}
		int namelength = name.split(",").length;
		model.addAttribute("term_name", name);
		model.addAttribute("term_model", namemodel);
		model.addAttribute("namelength", namelength);
		model.addAttribute("employee_id", data.getEmployee_id());
		model.addAttribute("visit_time", data.getVisit_time());
		model.addAttribute("expire_time", data.getExpire_time());
		model.addAttribute("title", data.getTitle());
		model.addAttribute("bat_id", data.getBat_id());
		
		return "visitorManage/visitorDetail";
	}

	@RequestMapping("/selectEmployeeInfo")
	@ResponseBody
	public FaceInfo selectEmployeeInfo(HttpSession session, Integer employee_id) {
		User user = (User) session.getAttribute("user");
		FaceInfo faceInfo = new FaceInfo();
		faceInfo.setFace_id(employee_id);
		faceInfo.setAttribute(0);
		List<FaceInfo> faceInfos = DataService.FaceInfoQuery(user, faceInfo, "", 0, -1).getResultData();

		ResultDomain<List<Depart>> departResultDomain = DataService.DepartInfoQuery(user, -1, 0);

		if (null != departResultDomain.getResultData() && !departResultDomain.getResultData().isEmpty()) {
			List<Depart> departList = departResultDomain.getResultData();
			Map<Integer, String> departMap = departList.stream()
					.collect(Collectors.toMap(Depart::getDepart_id, Depart::getDepart_name));
			faceInfos.forEach(e -> e.setDepartName(departMap.get(e.getDepart_id())));
		}
		return faceInfos.get(0);
	}

	/**
	 * 前往访客登记
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@RequestMapping("/toVisitorRegister")
	public String toVisitorRegister(Model model, HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		selectVisitReasonAndDepart(model, user);
		return "visitorManage/visitorRegister";
	}

	/**
	 * 查询来访事由/所有部门/所有员工
	 * @param model
	 * @param user
	 */
	private void selectVisitReasonAndDepart(Model model, User user) {
		List<VisitReason> visitReasonList = new ArrayList<>();
		ResultDomain<List<VisitReason>> visitReasonResultDomain = DataService.VisitReasonQuery(user, 0);
		if (null != visitReasonResultDomain.getResultData() && visitReasonResultDomain.getResultCode() == 0
				&& !visitReasonResultDomain.getResultData().isEmpty()) {
			visitReasonList = visitReasonResultDomain.getResultData();
		} else {
			log.info("查询来访事由接口出错");
		}
		model.addAttribute("visitReasonList", visitReasonList);
		List<Depart> departList = new ArrayList<>();
		ResultDomain<List<Depart>> departResultDomain = DataService.DepartInfoQueryByAuth(user, -1, 0);
		if (null != departResultDomain.getResultData() && !departResultDomain.getResultData().isEmpty()
				&& departResultDomain.getResultCode() == 0) {
			departList = departResultDomain.getResultData();
		} else {
			log.info("查询部门接口出错");
		}
		model.addAttribute("departList", departList);
		FaceInfo faceInfo = new FaceInfo();
		faceInfo.setAttribute(0);
		faceInfo.setStatus(0);
        List<FaceInfo> resultData = new ArrayList<>();
        ResultDomain<List<FaceInfo>> listResultDomain = DataService.FaceListGet(user, faceInfo,user.getDepartList());
        if (null != listResultDomain.getResultData()) {
            resultData = listResultDomain.getResultData();
        } else {
            log.info("搜索全部faceInfo出错");
        }
        model.addAttribute("faceInfoList",resultData);
    }

	@RequestMapping("editBatVisitor")
	public String editBatVisitor(Model model, HttpSession httpSession,Integer flag,
			@RequestParam(value = "term_list") List<Integer> term_list, Integer depart_id, Integer face_id,
			String tel_no, int bat_id) {
		User user = (User) httpSession.getAttribute("user");
		BatVisitor data = visitormanagerService.batQuerybyId(user, bat_id);
		if(null!=data) 
		{
			selectVisitReasonAndDepart(model, user);
			FaceInfo faceInfo = new FaceInfo();
			faceInfo.setAttribute(0);
			ResultDomain<List<FaceInfo>> listResultDomain = DataService.FaceListGet(user, faceInfo,user.getDepartList());
			httpSession.setAttribute("Eterm_list", term_list);
			httpSession.setAttribute("flag", flag);
			model.addAttribute("faceInfoList", listResultDomain.getResultData());
			model.addAttribute("face_id", face_id);
			model.addAttribute("tel_no", tel_no);
			model.addAttribute("depart_id", depart_id);
			model.addAttribute("bat_id", bat_id);
			model.addAttribute("data", data);
		}else {
			log.info("此批量数据为空");
		}
		return "visitorManage/editbatVisitor";
	}

	@RequestMapping("/addBatVisitor")
	@ResponseBody
	public String addBatVisitor(@RequestBody AddBatParam addBatParam, HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		BatVisitor batVisitor = addBatParam.getBatVisitor();
		List<Integer> termIdList = addBatParam.getDeviceList().stream().map(Device::getTerm_id)
				.collect(Collectors.toList());
		batVisitor.setTerm_list(termIdList);
		batVisitor.setTerm_num(termIdList.size());
		ResultDomain<BatVisitor> batVisitorResultDomain = DataService.BatVisitUpload(user, batVisitor);
		if (batVisitorResultDomain.getResultCode() == 0) {
			BatVisitor resultData = batVisitorResultDomain.getResultData();
			Integer batId = resultData.getBat_id();
			return String.valueOf(batId);
		} else {
			log.info("BatVisitUpload接口出错");
			return "0";
		}
	}

	/**
	 * add访客日志、update访客日志
	 * @param faceVisitInfo
	 * @param httpSession
	 * @return
	 */
	@RequestMapping("/addFaceVisitInfo")
	@ResponseBody
	public String addFaceVisitInfo(@RequestBody FaceVisitInfo faceVisitInfo, HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		ResultDomain<FaceVisitInfo> faceVisitInfoResultDomain = new ResultDomain<>();
		if (faceVisitInfo.getBook_id() == 0) {
			faceVisitInfoResultDomain = DataService.VisitRecordUpload(user,faceVisitInfo);
		} else {
			faceVisitInfoResultDomain = DataService.FaceBookUpload(user, faceVisitInfo);
		}
		if (faceVisitInfoResultDomain.getResultCode() != 0) {
			log.info("VisitRecordUpload/FaceBookUpload接口出错");
		}
		return JSON.toJSONString(faceVisitInfoResultDomain);
	}

	@RequestMapping("/queryAllVisitor")
	@ResponseBody
	public String queryVisitorList(HttpSession httpSession, 
			@RequestParam(value = "page", defaultValue = "1") String page,
			@RequestParam(value = "limit", defaultValue = "10") String limit,
			@RequestParam(value = "statusval", defaultValue = "0") Integer statusval,
			@RequestParam(value = "photonumval", defaultValue = "0") Integer photonumval,
			@RequestParam(value = "visit_time", defaultValue = "") String visitTime,
			@RequestParam(value = "expire_time", defaultValue = "") String expireTime,
			@RequestParam(value = "searchContent", defaultValue = "") String searchContent) {

		log.info("page={},limit={},statusval={},visit_time={},expire_time={},searchContent={}", page, limit, statusval,
				visitTime, expireTime, searchContent);

		User user = (User) httpSession.getAttribute("user");
		Integer start = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
		ResultDomain<List<VisitQueryInfo>> visitorResultDomain;

		String[] visitArray = visitTime.split(",");
		String[] expireArray = expireTime.split(",");
		String visittime = visitArray[visitArray.length - 1];
		String expiretime = expireArray[expireArray.length - 1];
		visitorResultDomain = visitormanagerService.queryAllVisitor(user, statusval,photonumval, visittime, expiretime,
				searchContent, Integer.parseInt(limit), start);


		List<VisitQueryInfo> visitorList = new ArrayList<>();
		Map<String, Integer> map = null;
		int result = 0;
		if (visitorResultDomain != null) {
			result = visitorResultDomain.getResultCode();
			visitorList = visitorResultDomain.getResultData();
			map = visitorResultDomain.getResultSubjoin();
			
		} else {
			log.info("查询访客来访信息为空值");
		}

		PageResult pageResult = new PageResult(result, visitorList,map);
		return JSON.toJSONString(pageResult);
	}

	@RequestMapping("/toEditVisitor")
	public String toEditVisitor(VisitQueryInfo visitQueryInfo,Model model,HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		model.addAttribute("visitQueryInfo",visitQueryInfo);
        int bat_id = visitQueryInfo.getBat_id();
        if (bat_id == 0) {
			selectVisitReasonAndDepart(model,user);
			return "visitorManage/visitorEdit1";
		} else {
			return "visitorManage/visitorEdit2";
		}
	}

	@RequestMapping("/showVisitorDetails")
	public String showVisitorDetails(HttpSession httpSession, Model model,VisitQueryInfo visitQueryInfo,
			@RequestParam(value = "status", defaultValue = "5") Integer status,
			@RequestParam(value = "faceid", defaultValue = "0") Integer faceid,
			@RequestParam(value = "recid", defaultValue = "0") Integer recid,
			@RequestParam(value = "bookid", defaultValue = "0") Integer bookid) {
		User user = (User) httpSession.getAttribute("user");
		VisitorDetailShow visitordetails = visitormanagerService.queryVisitorDetails(user, status, faceid, recid,
				bookid,visitQueryInfo.getAttribute());
		
		model.addAttribute("visitordetails", visitordetails);
		model.addAttribute("face_id", faceid);
		model.addAttribute("book_id", bookid);
		model.addAttribute("employee_id", visitQueryInfo.getEmployee_id());
		model.addAttribute("depart_id", visitQueryInfo.getDepart_id());
		model.addAttribute("visit_time", visitQueryInfo.getVisit_time());
		model.addAttribute("expire_time", visitQueryInfo.getExpire_time());
		model.addAttribute("bat_id", visitQueryInfo.getBat_id());
		model.addAttribute("reason_id", visitQueryInfo.getReason_id());
		return "visitorManage/visitordetails";
	}

	/**
	 * 根据faceInfo的字段查询faceInfo
	 * @param httpSession
	 * @param faceInfo
	 * @return
	 */
	@RequestMapping("/selectByParam")
	@ResponseBody
	public String selectByParam(HttpSession httpSession, FaceInfo faceInfo) {
		User user = (User) httpSession.getAttribute("user");
		PageResult pageResult = new PageResult();
		List<FaceInfo> resultData = new ArrayList<>();
		ResultDomain<List<FaceInfo>> listResultDomain = DataService.FaceInfoQuery(user, faceInfo, "", 0, 100);
		if (null != listResultDomain.getResultData() && !listResultDomain.getResultData().isEmpty()) {
			resultData = listResultDomain.getResultData();
		} else {
			log.info("FaceInfoQuery接口错误或无数据");
		}
		pageResult.setData(resultData);
		pageResult.setCount(resultData.size());
		return JSON.toJSONString(pageResult);
	}

	@RequestMapping("/VisitorLeave")
	@ResponseBody
	public int visitorLeave(HttpSession httpSession, int recid, int bookid,int status) {

		User user = (User) httpSession.getAttribute("user");
		int result = -1;
		// 预约取消来访bookid
		if (bookid != 0) {
			result = visitormanagerService.facebookDelete(user, bookid);
		} else if (recid != 0) {//访客离开&取消来访
			result = visitormanagerService.visitorLeave(user, recid,status);
		}

		return result;
	}

	@RequestMapping("/VisitorHistory")
	public String toVisitorHistory(Model model, @RequestParam(value = "faceid", defaultValue = "0") Integer faceid,
			@RequestParam(value = "attribute", defaultValue = "5") Integer attribute,
			@RequestParam(value = "visitime", defaultValue = "") String visitime,
			@RequestParam(value = "expiretime", defaultValue = "") String expiretime) {

		model.addAttribute("faceid", faceid);
		model.addAttribute("attribute", attribute);
		model.addAttribute("visitime", visitime);
		model.addAttribute("expiretime", expiretime);
		return "visitorManage/visitorhistory";
	}

	@RequestMapping("/queryAllVisitorHistory")
	@ResponseBody
	public String showAllVisitorHistory(HttpSession httpSession, 
			@RequestParam(value = "faceid", defaultValue = "0") Integer faceid,
			@RequestParam(value = "attribute", defaultValue = "5") Integer attribute,
			@RequestParam(value = "visitime", defaultValue = "") String visitime,
			@RequestParam(value = "expiretime", defaultValue = "") String expiretime) {
		User user = (User) httpSession.getAttribute("user");

		List<FaceRecordInfo> list = visitormanagerService.visitorHistory(user, faceid, attribute, visitime, expiretime);
		PageResult pageResult = new PageResult(list);

		return JSON.toJSONString(pageResult);
	}

	/**
	 * 查询预约的设备权限
	 * @param httpSession
	 * @param faceVisitInfo 查询条件
	 * @param start 开始条数
	 * @return
	 */
	@RequestMapping("/queryYuYueDevice")
	@ResponseBody
	public String queryYuYueDevice(HttpSession httpSession,FaceVisitInfo faceVisitInfo,@RequestParam(defaultValue = "0") Integer start) {
		User user = (User) httpSession.getAttribute("user");
		List<Device> devicelist = employeeManageServiceImpl.queryAllDevice(user);
		PageResult pageResult = new PageResult();
		ResultDomain<List<FaceVisitInfo>> listResultDomain = DataService.FaceBookQuery(user, faceVisitInfo, start);
		if (null != listResultDomain.getResultData() && listResultDomain.getResultData().size() == 1) {
			FaceVisitInfo visitInfo = listResultDomain.getResultData().get(0);
			if (null != visitInfo.getTerms() && !visitInfo.getTerms().isEmpty()){
				List<Integer> terms = visitInfo.getTerms();
				terms.forEach(t -> {
					devicelist.forEach(d -> {
						if (t == d.getTerm_id()) {
							d.setLAY_CHECKED(true);
						}
					});
				});
				pageResult.setData(devicelist);
				String string = JSON.toJSONString(pageResult).replace("lAY_CHECKED", "LAY_CHECKED");
				return string;
			} else {
				log.info("此预约无设备权限");
			}
		} else {
			log.info("FaceBookQuery接口错误或数据异常");
		}
		pageResult.setData(devicelist);
		return JSON.toJSONString(pageResult);
	}
	
	@RequestMapping("/QueryVisitrecordStatus")
	@ResponseBody
	public String queryVisitrecordStatus(HttpSession session){
		String oldstatus = String.valueOf(session.getAttribute(VISITDATASTAUS));
		
		User user=(User)session.getAttribute("user");
		String revo = "visit_info_version";
		String newstatus = visitormanagerService.queryDataStatus(user,revo);
		
		if(newstatus.equals(oldstatus)){
			return "unchange";
		}else{
			session.setAttribute(VISITDATASTAUS, newstatus);
			return "change";
		}
	}

	@RequestMapping("/judgeIfVisiting")
    @ResponseBody
    public String judgeIfVisiting(HttpSession session,Integer batId,String startTime,String endTime) {
        User user = (User)session.getAttribute("user");
        VisitQueryInfo visitQueryInfo = new VisitQueryInfo();
        visitQueryInfo.setAttribute(-1);
        visitQueryInfo.setMode(2);
        visitQueryInfo.setStatus(1);
        visitQueryInfo.setVisit_time(startTime);
        visitQueryInfo.setExpire_time(endTime);
        visitQueryInfo.setVisit_num(-1);
        ResultDomain<List<VisitQueryInfo>> listResultDomain = DataService.VisitInfoQuery(user, visitQueryInfo, 0);
        if (null != listResultDomain && null != listResultDomain.getResultData() && !listResultDomain.getResultData().isEmpty()) {
            List<VisitQueryInfo> visitQueryInfoList = listResultDomain.getResultData();
			List<Integer> visitingBatList = visitQueryInfoList.stream().map(VisitQueryInfo::getBat_id).collect(Collectors.toList());
			if (visitingBatList.contains(batId)) {
				return "exist";
			} else {
				return "noExist";
			}
		} else {
			log.error("VisitInfoQuery接口出错或无数据");
			return "noExist";
		}
    }
	
	@RequestMapping("/toAddCar")
	public String toAddCar(HttpSession session, @RequestParam(value="data",defaultValue="{carId:0}") String data) throws JSONException{
		JSONObject json = JSONObject.parseObject(data);
		session.setAttribute("data", data);
		return "visitorManage/addCar";
	}
	
}
