package com.jinglun.guard.devicemanage.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jinglun.guard.common.utils.GetPropety;
import com.jinglun.guard.common.utils.Zxing;
import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.DataService.HintConfigInfo;
import com.jinglun.guard.dataservice.DataService.ParamConfigInfo;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.devicemanage.domain.Device.DeviceFlag;
import com.jinglun.guard.devicemanage.domain.DeviceOnline;
import com.jinglun.guard.devicemanage.domain.DeviceStatus;
import com.jinglun.guard.devicemanage.domain.TermFace;
import com.jinglun.guard.devicemanage.domain.TermInfo;
import com.jinglun.guard.devicemanage.service.DeviceService;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.employeeManage.domain.TreeDomain;
import com.jinglun.guard.employeeManage.domain.TreeUtil;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.user.domain.User;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * 
 * @author huanggang
 * 设备管理业务逻辑实现类
 */
@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService{

	@Override
	public List<Device> queryAllDevice(User user,Integer categoryval,Integer networkval,Integer statusval,String queryval) {
		try {
			
			/** 查询终端基本信息
			 * @param user 当前用户
			 * @param device 终端信息，term_id为0返回全部;term_type: >0，按型号查询;del_flag:-1：全部,0：正常（启用）,1：删除,2：停用
			 * @param query_info 查询关键字，可为终端名称
			 * @return 终端列表
			 */
			Device device1 =new Device();
			device1.setTerm_id(0);
			device1.setTerm_type(0);
			
			//条件搜索类别
			if(categoryval != 0){
				device1.setTerm_type(categoryval);
			}
			//条件搜索状态
			if(statusval != 0){
				if(statusval == 1){
					device1.setDel_flag(DeviceFlag.ACTIVATED);
				}else{
					device1.setDel_flag(DeviceFlag.DEACTIVATED);
				}
			}else{
				device1.setDel_flag(DeviceFlag.UNDELETED);
			}
			
			ResultDomain<List<Device>> r = DataService.TermInfoQuery(user, device1, queryval);
			ResultDomain<List<DeviceStatus>> r2 = DataService.TermStatusQuery(user, 0);
			
			List<Device> devicelist = null;
			List<Device> returnlist = new ArrayList<Device>();
			List<DeviceStatus> devicestatuslist = null;
			
			int resut = r.getResultCode();
			if(resut==0){
				devicelist = r.getResultData();
				
				if(devicelist != null){
					returnlist = devicelist;
				}
				
			}
			
			int resut2 = r2.getResultCode();
			if(resut2==0){
				devicestatuslist = r2.getResultData();
			}
			Map<Integer, String> devicenetworkMap = devicestatuslist.stream().collect(Collectors.toMap(DeviceStatus::getTerm_id, DeviceStatus::getNetwork));
			Map<Integer, String> devicecontimeMap = devicestatuslist.stream().collect(Collectors.toMap(DeviceStatus::getTerm_id, DeviceStatus::getConnect_time));
			
			//统计离线终端数量
			for(Device device : returnlist){
				if(device.getBusiness_type()==0){
					device.setBusiness_name("门禁");
					device.setTerm_num(String.valueOf(device.getTerm_face_num()));
				}else{
					device.setBusiness_name("会议");
				}
				device.setStatus(device.getDel_flag().getName());
				
				if(device.getDepart_name()==null || device.getDepart_name().equals("")){
					device.setDepart_name("未分配");
				}
				
				//遍历网络状态
				try {
					String network = devicenetworkMap.get(device.getTerm_id());
					if(network.equals("1")){
						device.setTerm_network("在线");
					}else{
						device.setTerm_network("离线");
					}
				} catch (Exception e) {
					log.error(device.getTerm_id()+"在状态id中不存在");
				}
				
				
				//遍历终端连接时间
				String connectime = devicecontimeMap.get(device.getTerm_id());
				device.setTerm_connectime(connectime);
			}
			
			Iterator<Device> it = returnlist.iterator();
			//根据网络状态来筛选显示
			if(networkval != 0){
				//显示在线
				if(networkval == 1){
					while(it.hasNext()){
	    				Device d = it.next();
	    				String net_work = d.getTerm_network();
	    				if(net_work!=null && net_work.equals("离线")){
	        				it.remove();
	    				}
					}
					return returnlist;
				}else{//显示离线
					while(it.hasNext()){
	    				Device d = it.next();
	    				String net_work = d.getTerm_network();
	    				if(net_work!=null && net_work.equals("在线")){
	        				it.remove();
	    				}
					}
					return returnlist;
				}
			}else{
				return returnlist;
			}	
		} catch (Exception e) {
			log.error("设备列表接口调用出错");
		}
		
		return null;
		
	}
	
	private String uCode(String companyName,HttpServletRequest request) throws UnsupportedEncodingException
	{
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			// firefox浏览器
			companyName = new String(companyName.getBytes("UTF-8"), "ISO8859-1");
		} else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
			// IE浏览器
			companyName = URLEncoder.encode(companyName,"UTF-8");
		} else if (request.getHeader("User-Agent").toUpperCase().indexOf("CHROME") > 0) {
			// 谷歌
			companyName = new String(companyName.getBytes("UTF-8"), "ISO8859-1");
		}
	   return companyName;
		
	}   
	
	@Override
	public void qRCodeInit(HttpServletResponse response, HttpServletRequest request,String companyName,String bottom,String name) throws  IOException 
	{
		String names=uCode(name,request);
		GetPropety getPro=new GetPropety();
		String ip=getPro.getProperty();
		String content=ip+companyName;
		 //二维码格式
		 Map<EncodeHintType,Object> hints = new HashMap<EncodeHintType,Object>();
		 hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		 hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		 hints.put(EncodeHintType.MARGIN,2);
		 hints.put(EncodeHintType.QR_VERSION, 6);
			int width=250;
			int height=250;
			//二维码加logo
			BitMatrix bitMatrix = null;
			try {
				bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, height,width,hints);
			} catch (WriterException e) {
				e.printStackTrace();
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream;charset=UTF-8"); //response返回对象是文件流
			response.setHeader("content-disposition","attachment; filename=" +names+".jpg");// 下载框默认显示的文件名	
			OutputStream stream=response.getOutputStream();
			Zxing.writeToStream(bitMatrix, "jpg", stream,bottom);
			stream.flush();
		    stream.close();	
	}
	@Override
	public TermInfo queryAllParam(User user,int id){
		TermInfo termInfo=new TermInfo();
			try 
			{
				ResultDomain<List<ParamConfigInfo>> paramInf=DataService.TermCustomParamQuery(user,id);
				ResultDomain<List<HintConfigInfo>> hintInfo=DataService.TermHintQuery(user, id);
				List<ParamConfigInfo> paramInfos=paramInf.getResultData();
				List<HintConfigInfo> hintInfos = hintInfo.getResultData();
				if(null!=paramInfos&&!paramInfos.isEmpty()) {
					for(ParamConfigInfo paramconfig:paramInfos) {
						 if("".equals(paramconfig.getParam_value())) {
							 /*if(paramconfig.getParam_key()=="is_control_door") {
								 switch(paramconfig.getParam_key()) {
								 case "switch_mode":
									 paramconfig.setParam_value("TCP");
									 break;
								 case "lock_type":
									 paramconfig.setParam_value("PULSE");
									 break;
								 case "switch_cmd_on":
									 paramconfig.setParam_value("RELAY1=ON");
									 break;
								 case "switch_cmd_off":
									 paramconfig.setParam_value("RELAY1=OFF");
									 break;
								 case "switch_param":
									 paramconfig.setParam_value("1000,3");
									 break;
								 case "is_control_door":
									 paramconfig.setParam_value("1");
									 break;
								 case "switch_ctrmod":
									 paramconfig.setParam_value("SWITCH");
									 break;
								 case "switch_brake_direction":
									 paramconfig.setParam_value("1");
									 break;
								 }
							 }*/
							 switch(paramconfig.getParam_key()) {
							 case "timeout_readcard":
								 paramconfig.setParam_value("5000");
								 break;
							 case "timeout_cardinfo":
								 paramconfig.setParam_value("1000");
								 break;
							 case "cmp_mod_1vn":
								 paramconfig.setParam_value("local");
								 break;
							 case "timeout_result":
								 paramconfig.setParam_value("2000");
								 break;
							 case "cmp_mod_1v1":
								 paramconfig.setParam_value("local");
								 break;
							 case "detect_mode":
								 paramconfig.setParam_value("card_and_face");
								 break;
							 case "need_face_compare":
								 paramconfig.setParam_value("1");
								 break;
							 case "switch_delay":
								 paramconfig.setParam_value("4000");
								 break;
							 case "camera_res":
								 paramconfig.setParam_value("640x480");
								 break;
							 case "auth_level":
								 paramconfig.setParam_value("0");
								 break;
							 case "attend_statistics_ctrl":
								 paramconfig.setParam_value("0");
								 break;
							 case "heartbeat_interval":
								 paramconfig.setParam_value("20000");
								 break;
							 case "is_support_livecheck":
								 paramconfig.setParam_value("none");
								 break;
							 case "switch_mode":
								 paramconfig.setParam_value("TCP");
								 break;
							 case "lock_type":
								 paramconfig.setParam_value("PULSE");
								 break;
							 case "switch_cmd_on":
								 paramconfig.setParam_value("RELAY1=ON");
								 break;
							 case "switch_cmd_off":
								 paramconfig.setParam_value("RELAY1=OFF");
								 break;
							 case "switch_param":
								 paramconfig.setParam_value("1000,3");
								 break;
							 case "is_control_door":
								 paramconfig.setParam_value("1");
								 break;
							 case "switch_ctrmod":
								 paramconfig.setParam_value("SWITCH");
								 break;
							 case "switch_brake_direction":
								 paramconfig.setParam_value("1");
								 break;
							 case "business_config":
								 paramconfig.setParam_value("{\"is_check_termface\":true,\"visit_select_emp\":true,\"is_visit_use_idcard\":false,\"is_voice_hint\":true}");
								 break;
							 case "detect_config":
								 paramconfig.setParam_value("{\"quality\":true,\"whitescreen\":true,\"ledlight\":true,\"position\":true,\"brightness\":true,\"clarity\":true,\"track\":false,\"ffp\":true,\"pose\":true,\"conf\":true,\"live\":\"none\"}");
								 break;
							 }
							 
						 }
					}
				}
				if(null!=hintInfos&&!hintInfos.isEmpty()) {
					for(HintConfigInfo hintconfig:hintInfos) {
						if(hintconfig.getHint_key()=="mod_face_only") {
							if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("请刷脸");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("请刷脸");
								}
							}
                            if(hintconfig.getHint_key()=="mod_card_only") {
                            	//hintconfig.setHint_text("请刷卡");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("请刷卡");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("请刷卡");
								}
							}
                            if(hintconfig.getHint_key()=="mod_face_or_card") {
                            	//hintconfig.setHint_text("请刷脸或刷卡");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("请刷脸或刷卡");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("请刷脸或刷卡");
								}
							}
                            if(hintconfig.getHint_key()=="auth_emp_wel") {
                            	///hintconfig.setHint_text("%s您好");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("%s您好");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("%s您好");
								}
							}
                            if(hintconfig.getHint_key()=="unauth_emp_wel") {
                            	//hintconfig.setHint_text("%s欢迎来访");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("%s欢迎来访");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("%s欢迎来访");
								}
							}
                            if(hintconfig.getHint_key()=="auth_visitor_wel") {
                            	//hintconfig.setHint_text("%s欢迎来访");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("%s欢迎来访");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("%s欢迎来访");
								}
							}
                            if(hintconfig.getHint_key()=="unauth_visitor_wel") {
                            	//hintconfig.setHint_text("%s欢迎来访");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("%s欢迎来访");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("%s欢迎来访");
								}
							}
                            if(hintconfig.getHint_key()=="cmp_timeout") {
                            	//hintconfig.setHint_text("比对超时");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("比对超时");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("比对超时");
								}
							}
                            if(hintconfig.getHint_key()=="cmp_unpass") {
                            	//hintconfig.setHint_text("比对不通过");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("比对不通过");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("比对不通过");
								}
							}
                            if(hintconfig.getHint_key()=="cmp_pcard") {
                            	//hintconfig.setHint_text("请刷脸进行比对");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("请刷脸进行比对");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("请刷脸进行比对");
								}
							}
                            if(hintconfig.getHint_key()=="cmp_pphoto") {
                            	//hintconfig.setHint_text("请刷脸进行比对");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("请刷脸进行比对");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("请刷脸进行比对");
								}
							}
                            if(hintconfig.getHint_key()=="unauth_emp_tip") {
                            	//hintconfig.setHint_text("%s未授权");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("%s未授权");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("%s未授权");
								}
							}
                            if(hintconfig.getHint_key()=="unauth_visitor_face_only_tip") {
                            	//hintconfig.setHint_text("%s您未登记");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("%s您未登记");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("%s您未登记");
								}
							}
                            if(hintconfig.getHint_key()=="unauth_visitor_face_tip") {
                            	//hintconfig.setHint_text("%s请刷身份证");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("%s请刷身份证");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("%s请刷身份证");
								}
							}
                            if(hintconfig.getHint_key()=="unauth_visitor_empcard_tip") {
                            	//hintconfig.setHint_text("%s请刷身份证");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("%s请刷身份证");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("%s请刷身份证");
								}
							}
                            if(hintconfig.getHint_key()=="unauth_visitor_idcard_tip") {
                            	//hintconfig.setHint_text("%s未授权");
                            	if(hintconfig.getHint_text()=="") {
									hintconfig.setHint_text("%s未授权");
								}
								if(hintconfig.getHint_sound_text()=="") {
									hintconfig.setHint_sound_text("%s未授权");
								}
                            	
							}
					}
				}
				termInfo.setParamInfo(paramInfos);
				termInfo.setHintInfo(hintInfos);
				
				return termInfo;
			}catch (Exception e) 
			{
				log.error("终端设备查询接口调用出错");
			}
		return null;
	}
	@Override
	public List<Integer> queryDept(User user,int term_id)
	{
		try
		{
			ResultDomain<Device> dept=DataService.TermDepartQuery(user, term_id);
			if(null!=dept.getResultData()&&dept.getResultCode()==0) {
				Device device=dept.getResultData(); 
				return device.getDepart_id();
			}	
		}
		catch (Exception e) 
		{
			log.error("部门查询接口调用出错");
		}
		return null;
	}
	private String combineJson(String srcJObjStr, String addJObjStr){
		JSON.toJSONString(srcJObjStr.toString());
		if(addJObjStr == null || addJObjStr.isEmpty()) {
            return srcJObjStr;
        }
        if(srcJObjStr == null || srcJObjStr.isEmpty()) {
            return addJObjStr;
        }
        if(srcJObjStr.contains("none")) {
        	srcJObjStr =srcJObjStr.replace("none", "'none'");
        }
        if(addJObjStr.contains("none")) {
        	addJObjStr =addJObjStr.replace("none", "'none'");
        }
        srcJObjStr = srcJObjStr.replaceAll("\\\\","");
        addJObjStr = addJObjStr.replaceAll("\\\\","");
        JSONObject addJObj = JSONObject.fromObject(addJObjStr);
        JSONObject srcJObj = JSONObject.fromObject(srcJObjStr);
        combineJson(srcJObj, addJObj);
 
        return srcJObj.toString();
    }
	private JSONObject combineJson(JSONObject srcObj, JSONObject addObj){
		 
        Iterator<String> itKeys1 = addObj.keys();
        String key;
        String value;
        while(itKeys1.hasNext()){  
            key = itKeys1.next();
            value = addObj.optString(key);
            
            srcObj.put(key, value);
        }  
        return srcObj;
    }
	
	@Override
	public int paramConfig(User user,int id,TermInfo termInfo,int length)
	{
		try {
			if(length>1) {
				if(null!=termInfo.getParamInfo()) {
					for(ParamConfigInfo param:termInfo.getParamInfo()) {
						if("JSON".equals(param.getType())) {
							ResultDomain<List<ParamConfigInfo>> paramInfo=DataService.TermCustomParamQuery(user,id);
							for(ParamConfigInfo params:paramInfo.getResultData()) {
								if(param.getParam_key().equals(params.getParam_key())) {
									param.setParam_value(combineJson(params.getParam_value(),param.getParam_value()));
									break;
								}
							}
						}
					}
				}
			}
			if(null!=termInfo.getParamInfo()) {
				ResultDomain<List<ParamConfigInfo>> paramInf=DataService.TermCustomParamConfig(user,id, termInfo.getParamInfo());
				if(paramInf.getResultCode()!=0)
				{
					return 1;
					
				}
				
			}
			if(null!=termInfo.getHintInfo()) {
				ResultDomain<List<HintConfigInfo>> hint=DataService.TermHintConfig(user, id, termInfo.getHintInfo());
				if(hint.getResultCode()!=0)
				{
					return 1;
					
				}
				
			}
			return 0;
		}catch (Exception e) 
		{
			log.error("参数配置接口调用出错");
		}
		return 1;
	}
	
	
	
	
	@Override
	public int  deptConfig(User user,String[] TermName,String[] TermId,String ids)
	{
		int result;
		ResultDomain  deptConfig=null;
    	List<Integer> deptId=new ArrayList<Integer>();
    	String[] DeptId_array = ids.split(",");
    	for(int i=0;i<DeptId_array.length;i++) {
    		deptId.add(Integer.parseInt(DeptId_array[i]));
    	}
    	try {
    	if(TermName.length==1) {
    		
    		for(int i=0;i<TermId.length;i++) {
        		Device device=new Device();
        		device.setTerm_id(Integer.parseInt(TermId[i]));
            	device.setTerm_name(TermName[0]);
            	device.setDepart_id(deptId);
            	deptConfig=DataService.TermDepartConfig(user,device);
            	
        	}
    	}
    	if(TermName.length>1) {
    		for(int i=0;i<TermId.length;i++) {
    			List<Integer> depId=new ArrayList<Integer>();
    			List<Integer> deptIds=queryDept(user,Integer.parseInt(TermId[i]));
    			if(deptIds!=null) {
    				depId=deptId;
    				deptIds.removeAll(deptId);
    				depId.addAll(deptIds);
    			}
    			else {
    				depId=deptId;
    			}
    		    Device device=new Device();
        		device.setTerm_id(Integer.parseInt(TermId[i]));
            	device.setTerm_name(TermName[i]);
            	device.setDepart_id(depId);
            	deptConfig=DataService.TermDepartConfig(user,device);
    		}
    	}
    	if(null==deptConfig) {
    		return 1;
    	}   
    	result=deptConfig.getResultCode();
		return result;
    	}catch(Exception e) {
			log.error("部门配置接口调用出错");
    	}
    	return 1;
	}
	@Override
	public int  operation(User user,int id,String mode)
	{
		ResultDomain operation = new ResultDomain();
		try {
			
			if("0".equals(mode))
			{
			   operation =DataService.TermInfoDelete(user, id,0);
			}
			if("1".equals(mode))
			{
			   operation =DataService.TermInfoDelete(user, id,1);
			}
			if("2".equals(mode))
			{
			   operation =DataService.TermInfoDelete(user, id,2);
			}
			return operation.getResultCode();
		}catch (Exception e) {
			log.error("终端操作接口调用出错");
		}
		return 1;
		
	}
	
	
	@Override
	public DeviceOnline queryTermonlineStatus(User user) {
		try {
			ResultDomain<List<DeviceStatus>> r = DataService.TermStatusQuery(user, 0);
			
			int resut = r.getResultCode();
			if(resut==0){
				List<DeviceStatus> devicestatuslist = r.getResultData();
				DeviceOnline dsonline = new DeviceOnline();
				if(devicestatuslist!=null){
					int count = 0;
					for(DeviceStatus ds : devicestatuslist){
						if(ds.getNetwork().equals("1")){
							++count;
						}
					}
					
					dsonline.setUnderline(devicestatuslist.size()-count);
					dsonline.setOnline(count);
					
					return dsonline;
				}else{
					dsonline.setOnline(0);
					dsonline.setUnderline(0);
					return dsonline;
				}
				
			}
		} catch (Exception e) {
			log.error("终端状态接口调用出错");
		}
		
		return null;
	}

	@Override
	public TreeDomain getDepartTree(User user) {
		ResultDomain<List<Depart>> listResultDomain = DataService.DepartInfoQuery(user, -1, 0);
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

	@Override
	public Map<String, Map<Integer, String>> queryAllEmployee(User user,String TermId_array[]) {
		List<Integer> authList=user.getDepartList();
		try {
			Map<Integer, String> authmap = new LinkedHashMap<Integer, String>();
			Map<Integer, String> un_authmap = new LinkedHashMap<Integer, String>();
			Map<String, Map<Integer, String>> returnmap = new LinkedHashMap<String, Map<Integer, String>>();
			
			//查询所有人员信息
			FaceInfo query_face = new FaceInfo();
			query_face.setFace_id(0);
			query_face.setDepart_id(0);
			query_face.setAttribute(0);
			query_face.setStatus(0);
			ResultDomain<List<FaceInfo>> r = DataService.FaceInfoQuery(user, query_face, "",0,-1,authList);
			int resut = r.getResultCode();
			//所有人员信息
			List<FaceInfo> allfaceinfolist = r.getResultData();
			//已授权人员信息
			List<FaceInfo> authlist = new ArrayList<FaceInfo>();
			//未授权人员信息
			List<FaceInfo> un_authlist = new ArrayList<FaceInfo>();
			//接口返回已授权人员信息faceid
			List<TermFace> termfacelist = null;
			
			if(TermId_array.length == 1){
				//选择1台设备配置权限；分别查询已授权和未授权的人数
				termfacelist = queryTermFace(user, Integer.valueOf(TermId_array[0]));
				Map<Integer,Integer> faceidmap = new HashMap<Integer,Integer>();
				if(termfacelist!=null){
					for(TermFace t : termfacelist){
						if(t.getAttribute()==0){
							faceidmap.put(t.getFace_id(), t.getFace_id());
						}
					}
				}
				if(resut>0){
					//判断需要循环调用多少次FaceInfoQuery接口
					int count = (int)Math.ceil(resut/(double)200);
					if(count > 1){
						for(int i = 1; i<count ; i++){
							List<FaceInfo> list = DataService.FaceInfoQuery(user, query_face, "", i*200 ,-1,authList).getResultData();
							for(FaceInfo faceinfo:list){
								allfaceinfolist.add(faceinfo);
							}
						}
					}
					//所有人员权限信息区分为已授权和未授权
					for(FaceInfo faceinfo : allfaceinfolist){
						if(faceidmap.containsKey(faceinfo.getFace_id())){
							authlist.add(faceinfo);
						}else{
							un_authlist.add(faceinfo);
						}
					}
					
					if(authlist != null){
						for(FaceInfo faceinfo : authlist){
							String name = faceinfo.getName();
							String phone = faceinfo.getTel_no();
							if(phone == null || phone.equals("")){
								authmap.put(faceinfo.getFace_id(), name);
							}else{
								String str = name+"("+phone+")";
								authmap.put(faceinfo.getFace_id(), str);
							}
							
						}
						returnmap.put("auth",authmap);
					}
					if(un_authlist != null){
						for(FaceInfo faceinfo : un_authlist){
							String name = faceinfo.getName();
							String phone = faceinfo.getTel_no();
							if(phone == null || phone.equals("")){
								un_authmap.put(faceinfo.getFace_id(), name);
							}else{
								String str = name+"("+phone+")";
								un_authmap.put(faceinfo.getFace_id(), str);
							}
							
						}
						returnmap.put("un_auth", un_authmap);
					}
				}
			}else if(TermId_array.length > 1){
				//选择了多台设备
				if(resut>0){
					for(FaceInfo faceinfo : allfaceinfolist){
						String name = faceinfo.getName();
						String phone = faceinfo.getTel_no();
						if(phone == null || phone.equals("")){
							un_authmap.put(faceinfo.getFace_id(), name);
						}else{
							String str = name+"("+phone+")";
							un_authmap.put(faceinfo.getFace_id(), str);
						}
						
					}
				}
				returnmap.put("un_auth", un_authmap);
			}
				
			return returnmap;
		} catch (Exception e) {
			log.error("人员信息查询接口调用出错");
		}
		
		
		return null;
	}

	@Override
	public List<TermFace> queryTermFace(User user, int term_id) {
		
		try {
			ResultDomain<List<TermFace>> r = DataService.TermFaceQuery(user, term_id);
			
			int resut = r.getResultCode();
			List<TermFace> termfacelist = null;
			if(resut==0){
				termfacelist = r.getResultData();
			}
			
			
			return termfacelist;
		} catch (Exception e) {
			log.error("查询终端权限接口调用出错");
		}
		
		
		return null;
	}

	@Override
	public int saveTermFaceinfo(User user,String faceId_array[],String termId_array[]) {
		try {
			
			List<FaceInfo> faceList = new ArrayList<FaceInfo>();
			for(int i=0;i<faceId_array.length;i++){
				if(!faceId_array[i].equals("")){
					FaceInfo faceinfo = new FaceInfo();
					faceinfo.setFace_id(Integer.valueOf(faceId_array[i]));
					faceinfo.setAttribute(0);
					faceList.add(faceinfo);
				}
			}
			
			long l = System.currentTimeMillis();
			Date date = new Date(l);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String expire_begin = dateFormat.format(date);
			String expire_end = "2038-12-31 11:59:59";
			
			int count = 0;
			for(int j=0;j<termId_array.length;j++){
				int termid = Integer.valueOf(termId_array[j]);
				int resut = DataService.TermFaceConfig(user, termid, faceList, expire_begin, expire_end).getResultCode();
				if(resut==0){
					count++;
				}
			}
			
			if(count==termId_array.length){
				return 0;
			}else{
				return -1;
			}
			
		} catch (Exception e) {
			log.error("配置终端权限TermFaceConfig接口调用出错");
		}
		
		return -1;
	}

	@Override
	public List<FaceInfo> queryDepartFaceinfo(User user, int depart_id) {
		List<Integer> authList=user.getDepartList();
		FaceInfo query_face = new FaceInfo();
		query_face.setDepart_id(depart_id);
		query_face.setFace_id(0);
		query_face.setAttribute(0);
		query_face.setStatus(0);
		try {
			ResultDomain<List<FaceInfo>> r = DataService.FaceInfoQuery(user, query_face, "", 0, -1,authList);
			int result = r.getResultCode();
			List<FaceInfo> allfaceinfolist = r.getResultData();
			if(result>0){
				//判断需要循环调用多少次FaceInfoQuery接口
				int count = (int)Math.ceil(result/(double)200);
				if(count > 1){
					for(int i = 1; i<count ; i++){
						List<FaceInfo> list = DataService.FaceInfoQuery(user, query_face, "", i*200 ,-1,authList).getResultData();
						for(FaceInfo faceinfo:list){
							allfaceinfolist.add(faceinfo);
						}
					}
				}
				return allfaceinfolist;	
			}
			
		} catch (Exception e) {
			log.error("部门人员回显FaceInfoQuery接口调用出错");
		}
		return null;
	}

}
