package com.jinglun.guard.visitorManage.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jinglun.guard.attendance.domain.FaceRecordInfo;
import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.PageResult;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.dataservice.DataService.ParamConfigInfo;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.devicemanage.domain.TermFace;
import com.jinglun.guard.devicemanage.domain.Device.DeviceFlag;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.employeeManage.domain.FaceInfo.Picture;
import com.jinglun.guard.employeeManage.service.impl.EmployeeManageServiceImpl;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.visitorManage.domain.BatVisitor;
import com.jinglun.guard.visitorManage.domain.FaceVisitInfo;
import com.jinglun.guard.visitorManage.domain.VisitQueryInfo;
import com.jinglun.guard.visitorManage.domain.VisitorDetailShow;
import com.jinglun.guard.visitorManage.service.VisitorManagerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VisitorManagerServiceImpl implements VisitorManagerService {

	@Resource(name = "employeeManageServiceImpl")
	private EmployeeManageServiceImpl employeeManageServiceImpl;

	private static final String EMPTY_PARAM = "暂未设置";
	
	@Override
	public ResultDomain<List<VisitQueryInfo>> queryAllVisitor(User user, Integer statuval,Integer photonumval, String visit_time,
			String expire_time, String query_info, int visit_num, int start) {

		try {
			VisitQueryInfo queryInfo = new VisitQueryInfo();
			queryInfo.setAttribute(-1);
			queryInfo.setMode(0);
			queryInfo.setEmployee_id(0);
			queryInfo.setVisit_time(visit_time);
			queryInfo.setExpire_time(expire_time);
			queryInfo.setQuery_info(query_info);
			queryInfo.setVisit_num(visit_num);
			// 状态 -1-所有 0-预约 1-在访 2-离开
			if (statuval == 1) {// 预约
				queryInfo.setStatus(0);
			} else if (statuval == 2) {// 在访
				queryInfo.setStatus(1);
			} else if (statuval == 3) {// 离开
				queryInfo.setStatus(2);
			} else {// 所有
				queryInfo.setStatus(-1);
			}
			// 0-所有，1-有照片，2-无照片
			queryInfo.setPic_num(photonumval);

			ResultDomain<List<VisitQueryInfo>> r = DataService.VisitInfoQuery(user, queryInfo, start);
			if (r.getResultCode() > 0) {
				List<VisitQueryInfo> visitorlist = r.getResultData();
				for (VisitQueryInfo visitorinfo : visitorlist) {
					if (visitorinfo.getStatus() == 0) {
						visitorinfo.setStatustr("预约");
					} else if (visitorinfo.getStatus() == 1) {
						visitorinfo.setStatustr("在访");
					} else {
						visitorinfo.setStatustr("离开");
					}
				}

				return r;
			}
		} catch (Exception e) {
			log.info("查询访客来访信息VisitInfoQuery接口调用出错");
		}
		return null;
	}
	@Override
	public BatVisitor batQuerybyId(User user, int bat_id) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String visit_time=formatter.format(date);
		// String expire_time="2018-08-22 18:00:00";
		String expire_time = formatter.format(date);

		String visit_time = "2018-01-16 00:00:00";
		BatVisitor batVisitor = new BatVisitor();
		batVisitor.setBat_id(bat_id);
		batVisitor.setExpire_time("2039-08-16 00:00:00");
		batVisitor.setVisit_time(visit_time);
		try {
			ResultDomain<List<BatVisitor>> batVisitorQuery = DataService.BatVisitQuery(user, batVisitor, 0, -1);
			List<BatVisitor> batVisitors = batVisitorQuery.getResultData();
			return batVisitors.get(0);
		} catch (Exception e) {
			log.error("访客批量查询列表接口调用出错");
		}
		return null;
	}
	@Override
	public PageResult batQuery(User user, int start, int limit) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String visit_time=formatter.format(date);
		// String expire_time="2018-08-22 18:00:00";
		//String expire_time = formatter.format(date);

		String visit_time = "2018-01-16 00:00:00";
		BatVisitor batVisitor = new BatVisitor();
		batVisitor.setBat_id(0);
		batVisitor.setExpire_time("2039-08-16 00:00:00");
		batVisitor.setVisit_time(visit_time);
		List<BatVisitor> batVisitors=null;
		List<FaceInfo> faceInfos=null;
		PageResult pageResult=null;
		int result=0;
	/*	int count = 0;
		List<BatVisitor> batVisitorlist = new ArrayList<BatVisitor>();*/
		try {
			ResultDomain<List<BatVisitor>> batVisitorQuery = DataService.BatVisitQuery(user, batVisitor, start, limit);
			if(null!=batVisitorQuery.getResultData()&&batVisitorQuery.getResultData().size()>0) {
				batVisitors = batVisitorQuery.getResultData();
				result=batVisitorQuery.getResultCode();
				FaceInfo faceInfo = new FaceInfo();
			    faceInfo.setAttribute(0);
			    faceInfo.setStatus(0);
			    ResultDomain<List<FaceInfo>> listResultDomain = DataService.FaceListGet(user, faceInfo,user.getDepartList());
				if(null!=listResultDomain.getResultData()&&listResultDomain.getResultData().size()>0) {
					faceInfos = listResultDomain.getResultData();
					Map<Integer, String> faceinfoMap = faceInfos.stream()
							.collect(Collectors.toMap(FaceInfo::getFace_id, FaceInfo::getName));
					batVisitors.forEach(e ->e.setEmployee_name(faceinfoMap.get(e.getEmployee_id()))); 
					pageResult = new PageResult(batVisitorQuery.getResultCode(), batVisitors);
				}
				return pageResult;
			}
			
		} catch (Exception e) {
			log.error("访客批量查询列表接口调用出错");
		}

		return new PageResult(result,batVisitors);

	}
	@Override
	public int batDel(User user, int batid) {
		int result = 1;
		BatVisitor batVisitor = new BatVisitor();
		batVisitor.setBat_id(batid);
		try {
			ResultDomain resultDomain = DataService.BatVisitDelete(user, batVisitor);
			result = resultDomain.getResultCode();
		} catch (Exception e) {
			log.error("访客批量删除接口调用出错");
		}

		return result;
	}

	@Override
	public int visitorDel(User user, int recId) {
		int result = 1;
		FaceVisitInfo faceInfo = new FaceVisitInfo();
		faceInfo.setRec_id(recId);
		faceInfo.setStatus(1);
		try {
			ResultDomain faceVisitInfo = DataService.VisitRecordDelete(user, faceInfo);
			result = faceVisitInfo.getResultCode();
		} catch (Exception e) {
			log.info("访客记录删除接口调用出错");
		}

		return result;
	}

	@Override
	public VisitorDetailShow queryVisitorDetails(User user, Integer status, Integer faceid, Integer recid,
			Integer bookid,Integer attribute) {
		VisitorDetailShow visitordetail = new VisitorDetailShow();
		visitordetail.setStatus(status);
		FaceVisitInfo queryInfo = new FaceVisitInfo();
		List<FaceVisitInfo> facevisitorinfo = null;
		FaceVisitInfo facevisit = null;
		List<FaceInfo> faceinfolist = null;
		FaceInfo faceinfo = null;
		List<Device> devicelist = employeeManageServiceImpl.queryAllDevice(user);
		String termNamestr = "";
		try {
			// 在访和离开调用接口状态 1.VisitRecordQuery 0-预约 1-在访 2-离开
			if (recid>0) {//预约在访和离开
				queryInfo.setRec_id(recid);
				queryInfo.setVisit_num(1);
				ResultDomain<List<FaceVisitInfo>> r1 = DataService.VisitRecordQuery(user, queryInfo, 0);

				int result1 = r1.getResultCode();
				if (result1 > 0) {
					facevisitorinfo = r1.getResultData();
					if (facevisitorinfo != null) {
						facevisit = facevisitorinfo.get(0);
					}

					visitordetail.setVisitor_content(facevisit.getContent());
					visitordetail.setVisitor_time(facevisit.getVisit_time() + "至" + facevisit.getExpire_time());
					visitordetail.setEmployee_name(facevisit.getEmployee());
					// 设置访问部门
					ResultDomain<List<Depart>> departr = DataService.DepartInfoQuery(user, -1,
							facevisit.getDepart_id());
					int result = departr.getResultCode();
					if (result == 0) {
						List<Depart> departlist = departr.getResultData();
						Depart depart = departlist.get(0);
						visitordetail.setVisitor_departname(depart.getDepart_name());
					}
				}
				
				// 3.设置设备权限FacePermitQuery
				TermFace face = new TermFace();
				face.setAttribute(1);
				face.setFace_id(faceid);
				face.setVisit_id(recid);
				ResultDomain<List<TermFace>> tf = DataService.FacePermitQuery(user, face);
				int result_term = tf.getResultCode();
				if (result_term == 0) {
					List<TermFace> termlist = tf.getResultData();
					StringBuilder termName = new StringBuilder();
					if(null != termlist){
						String termMode = "";
						for(int i=0;i<termlist.size();i++){
							int term_mode = termlist.get(i).getTerm_model();
							if(term_mode==0){
								termMode += Integer.toString(4);//未知类型
							}else if(term_mode==1||term_mode==2||term_mode==3||term_mode==4||term_mode==5||term_mode==6||term_mode==9){
								termMode += Integer.toString(3);//iDR类型
							}else if(term_mode==100){
								termMode += Integer.toString(2);//facecheck动态类型
							}else{
								termMode += Integer.toString(1);//14T类型
							}
							
			                if(i<termlist.size()-1){
			                	termMode += ",";
			                }
			            }
						visitordetail.setTerm_permitmode(termMode);
					}
					
					
					if (termlist != null) {
						termlist.forEach(t -> {
							devicelist.forEach(d -> {
								if (d.getTerm_id() == t.getTerm_id()) {
									termName.append(d.getTerm_name() + ",");
								}
							});
						});
					}
					String bufferStr = termName.toString();
					if (!"".equals(bufferStr)) {
						termNamestr = bufferStr.substring(0, bufferStr.length() - 1);
					}
					if(termNamestr==null || "".equals(termNamestr)){
						visitordetail.setTerm_permit(EMPTY_PARAM);
					}else{
						visitordetail.setTerm_permit(termNamestr);
					}
					
				}
				
			} else if (bookid > 0) {//预约
				// 预约调用接口 1.FaceBookQuery
				queryInfo.setBook_id(bookid);
				queryInfo.setVisit_num(1);
				ResultDomain<List<FaceVisitInfo>> rd = DataService.FaceBookQuery(user, queryInfo, 0);
				int result_book = rd.getResultCode();
				if (result_book > 0) {
					facevisitorinfo = rd.getResultData();
					if (facevisitorinfo != null) {
						facevisit = facevisitorinfo.get(0);
					}

					visitordetail.setVisitor_content(facevisit.getContent());
					visitordetail.setVisitor_time(facevisit.getVisit_time() + "至" + facevisit.getExpire_time());
					visitordetail.setEmployee_name(facevisit.getEmployee());
					// 设置访问部门
					ResultDomain<List<Depart>> departr = DataService.DepartInfoQuery(user, -1,
							facevisit.getDepart_id());
					int result = departr.getResultCode();
					if (result == 0) {
						List<Depart> departlist = departr.getResultData();
						Depart depart = departlist.get(0);
						visitordetail.setVisitor_departname(depart.getDepart_name());
					}
					
					List<Integer> termlist = facevisit.getTerms();
					StringBuilder termName = new StringBuilder();
					StringBuilder termMode = new StringBuilder();
					if (termlist != null) {
						termlist.forEach(t -> {
							devicelist.forEach(d -> {
								if (d.getTerm_id() == t) {
									termName.append(d.getTerm_name() + ",");
									
									int term_model = d.getTerm_model();
									if(term_model==0){
										termMode.append(4);//未知类型
									}else if(term_model==1||term_model==2||term_model==3||term_model==4||term_model==5||term_model==6||term_model==9){
										termMode.append(3);//iDR类型
									}else if(term_model==100){
										termMode.append(2);;//facecheck动态类型
									}else{
										termMode.append(1);//14T类型
									}
								}
							});
						});
					}
					String term_model=termMode.toString();
					String str = "";
					if (!"".equals(term_model)) {
						str = term_model.substring(0, term_model.length() - 1);
					}
					visitordetail.setTerm_permitmode(str);
					
					
					String bufferStr = termName.toString();
					if (!"".equals(bufferStr)) {
						termNamestr = bufferStr.substring(0, bufferStr.length() - 1);
					}
					if(termNamestr==null || "".equals(termNamestr)){
						visitordetail.setTerm_permit(EMPTY_PARAM);
					}else{
						visitordetail.setTerm_permit(termNamestr);
					}
				}
			}

			// 2.FaceInfoQuery设置访客缺失信息
			FaceInfo query_face = new FaceInfo();
			// 查询访客信息
			query_face.setAttribute(attribute);
			query_face.setFace_id(faceid);
			query_face.setDepart_id(0);
			query_face.setStatus(status);
			ResultDomain<List<FaceInfo>> r = DataService.FaceInfoQuery(user, query_face, "", 0, -1);
			int result = r.getResultCode();
			if (result > 0) {
				faceinfolist = r.getResultData();
				if (faceinfolist != null) {
					faceinfo = faceinfolist.get(0);
				}
				visitordetail.setVisitor_name(faceinfo.getName());
				visitordetail.setVisitor_sex(faceinfo.getSex());
				if(faceinfo.getIdcard()==null || "".equals(faceinfo.getIdcard())){
					visitordetail.setVisitor_idcard(EMPTY_PARAM);
				}else{
					visitordetail.setVisitor_idcard(faceinfo.getIdcard());
				}
				visitordetail.setVisitor_tel(faceinfo.getTel_no());
				if(faceinfo.getCompany()==null || "".equals(faceinfo.getCompany())){
					visitordetail.setVisitor_company(EMPTY_PARAM);
				}else{
					visitordetail.setVisitor_company(faceinfo.getCompany());
				}
				if(faceinfo.getAddr()==null || "".equals(faceinfo.getAddr())){
					visitordetail.setVisitor_address(EMPTY_PARAM);
				}else{
					visitordetail.setVisitor_address(faceinfo.getAddr());
				}
				if(faceinfo.getPosition()==null || "".equals(faceinfo.getPosition())){
					visitordetail.setVisitor_postion(EMPTY_PARAM);
				}else{
					visitordetail.setVisitor_postion(faceinfo.getPosition());
				}
			}
			// 设置员工缺失信息
			query_face.setAttribute(0);
			query_face.setFace_id(facevisit.getEmployee_id());
			ResultDomain<List<FaceInfo>> r2 = DataService.FaceInfoQuery(user, query_face, "", 0, -1);
			int result2 = r2.getResultCode();
			if (result2 > 0) {
				faceinfolist = r2.getResultData();
				FaceInfo faceinfoEmp = null;
				if (faceinfolist != null) {
					faceinfoEmp = faceinfolist.get(0);
					visitordetail.setEmployee_tel(faceinfoEmp.getTel_no());
				}
			}
			
			String termlist[] = termNamestr.split(",");
			int length = termlist.length;
			visitordetail.setTermlength(length);
			
			//4.查询访客详情照片FacePhotoQuery
			FaceInfo face = new FaceInfo();
			face.setAttribute(attribute);
			face.setFace_id(faceid);

			ResultDomain<List<Picture>> facephoto = DataService.FacePhotoQuery(user, face);

			if(facephoto!=null){
				int resultFace = facephoto.getResultCode();
				if(resultFace==0){
					List<Picture> photolist = facephoto.getResultData();
					int len = photolist.size();
					if(len > 0){
						for(Picture p : photolist){
							int imageType = p.getImage_type();
							if(imageType==1){
								//生活照
								String photoLive = p.getData();
								if(photoLive!=null && !"".equals(photoLive)){
									visitordetail.setVisitor_photo(photoLive);
								}
							}
						}
						if(null == visitordetail.getVisitor_photo() || ("").equals(visitordetail.getVisitor_photo())){
							//生活照为空时查比对照片
							for(Picture p : photolist){
								int imageType = p.getImage_type();
								if(imageType==0){
									//比对照
									String photoComparison = p.getData();
									if(photoComparison!=null && !"".equals(photoComparison)){
										visitordetail.setVisitor_photo(photoComparison);
									}
								}
							}
						}
					}else{
						//找身份证照片
						String photoWlt = faceinfo.getPhoto_wlt();
						if(photoWlt!=null && !"".equals(photoWlt)){
							visitordetail.setVisitor_photo(photoWlt);
						}
					}
				}
			}

		} catch (Exception e) {
			log.info("查询人员信息FaceInfoQuery接口调用出错");
		}

		return visitordetail;
	}

	@Override
	public int visitorLeave(User user, Integer recid,Integer status) {
		FaceVisitInfo info = new FaceVisitInfo();
		try {
			// 访客离开recid
			info.setRec_id(recid);
			if(status==0){//预约状态数据
				info.setStatus(2);
			}else{//访客离开
				info.setStatus(1);
			}
			
			ResultDomain r = DataService.VisitRecordDelete(user, info);
			int result = r.getResultCode();
			if(result==0){
				if(status==0){
					return 0;
				}else{
					return 1;
				}
			}
			
		} catch (Exception e) {
			log.info("查询访客离开VisitRecordDelete接口调用出错");
		}
		return -1;
	}

	@Override
	public int facebookDelete(User user, Integer bookid) {
		FaceVisitInfo info = new FaceVisitInfo();
		try {
			// 预约取消来访bookid
			info.setBook_id(bookid);
			info.setStatus(0);
			ResultDomain r = DataService.FaceBookDelete(user, info);
			int result = r.getResultCode();
			if(result==0){
				return 0;
			}

		} catch (Exception e) {
			log.info("查询访客离开VisitLeave接口调用出错");
		}
		return -1;
	}

	@Override
	public List<FaceRecordInfo> visitorHistory(User user, Integer faceid, Integer attribute, String visitime,
			String expiretime) {
		FaceVisitInfo info = new FaceVisitInfo();
		info.setFace_id(faceid);
		info.setAttribute(attribute);
		info.setTerm_id(0);
		info.setVisit_time(visitime);
		info.setExpire_time(expiretime);
		try {
			ResultDomain<List<FaceRecordInfo>> r = DataService.FaceRecordQuery(user, info, 0, -1);
			int result = r.getResultCode();
			if (result > 0) {
				List<FaceRecordInfo> facerecordinfolist = r.getResultData();

				// 根据termid查找设备名称
				Device device = new Device();
				device.setTerm_id(0);
				device.setTerm_type(0);
				device.setDel_flag(DeviceFlag.ALL);
				ResultDomain<List<Device>> deviceResultDomain = DataService.TermInfoQuery(user, device,"");
				int resultDevice = deviceResultDomain.getResultCode();
				if(resultDevice==0){
					List<Device> devicelist = deviceResultDomain.getResultData();
					if (facerecordinfolist != null) {
						facerecordinfolist.forEach(f -> {
							devicelist.forEach(d -> {
								if (d.getTerm_id() == f.getTerm_id()) {
									f.setTerm_name(d.getTerm_name());
								}
							});
						});
					}
				}
				return facerecordinfolist;
			}

		} catch (Exception e) {
			log.info("查询来访历史FaceRecordQuery接口调用出错");
		}
		return null;
	}

	@Override
	public String queryDataStatus(User user,String revo) {
		try {
			ResultDomain<Map<String, String>> r = DataService.DataStatusQuery(user, 0);
			int result = r.getResultCode();
			if(result == 0){
				Map<String, String> map = r.getResultData();
				if(null != map){
					for(String key : map.keySet()) {
						if(revo.equals(key)){
							String status = map.get(key);
							return status;
						}
					}
				}
			}
		} catch (Exception e) {
			log.info("查询数据状态DataStatusQuery接口调用出错");
		}
		return "";
	}

	@Override
	public int queryVisitrefreshTime(User user) {
		try {
			ResultDomain<List<ParamConfigInfo>> r = DataService.LibraryParamQuery(user);
			int result = r.getResultCode();
			if(result == 0){
				List<ParamConfigInfo> list = r.getResultData();
				int time = 0;
				if(null != list){
					for(ParamConfigInfo p : list){
						if("auto_refresh_interval".equals(p.getParam_key())){
							if(null != p.getParam_value() && !"".equals(p.getParam_value())){
								time = Integer.valueOf(p.getParam_value());
							}else{
								time = 10;
							}
						}
					}
					return time;
				}
			}
		} catch (Exception e) {
			log.info("查询数据状态DataStatusQuery接口调用出错");
		}
		return 10;
	}

}
