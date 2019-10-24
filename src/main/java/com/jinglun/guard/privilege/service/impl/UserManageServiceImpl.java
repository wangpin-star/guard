package com.jinglun.guard.privilege.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.devicemanage.domain.Device.DeviceFlag;
import com.jinglun.guard.devicemanage.service.DeviceService;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.privilege.service.UserManageService;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.user.domain.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserManageServiceImpl implements UserManageService{
	@Autowired
	private DeviceService deviceService;
	
	@Override
	public List<Device> deviceQuery(User user,List<Integer> departlist) {
		List<Device> devicelist = deviceService.queryAllDevice(user,0,0,0,"");
		List<Device> devicelists = new ArrayList();
		if(null!=devicelist) {
			for(Device device:devicelist) {
				for(Integer departs:departlist) {
					for(Integer depart:device.getDepart_id()) {
						if(departs==depart) {
							devicelists.add(device);
						}
					}
				}
			}
		}
		return devicelists;
	}
	
	@Override
	public List<User> userlist(User user){
		List<User> userlist=new ArrayList<>();
		try {
			ResultDomain<List<User>> resultDomain=DataService.UserInfoQuery(user);
			userlist=resultDomain.getResultData();
			
		}catch(Exception e) {
			log.error("用户查询接口调用出错");
		}
		return userlist;
	}
	
	@Override
	public List<FaceInfo> faceInfo(User user){
		ResultDomain<List<FaceInfo>> faceInfoResultDomain;
        faceInfoResultDomain = DataService.FaceInfoSearch(user, 0, 0, 0, -1, "");
        if(null!=faceInfoResultDomain.getResultData()&&!faceInfoResultDomain.getResultData().isEmpty()) {
        	for(int i=0;i<faceInfoResultDomain.getResultData().size();i++) {
        		if(faceInfoResultDomain.getResultData().get(i).getStatus()==1) {
        			faceInfoResultDomain.getResultData().remove(i);
        		}
        	}
        	return faceInfoResultDomain.getResultData();
        }
        return null;
	}
	
	@Override
	public List<Device> queryAllDevice(User user,Integer categoryval,Integer networkval,Integer statusval,String queryval){
		List<Device> devicelist=new ArrayList<>();
		try {
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
			ResultDomain<List<Device>> resultDomain = DataService.TermInfoQuerys(user, device1, "");
			devicelist=resultDomain.getResultData();
			
		}catch(Exception e) {
			log.error("用户查询接口调用出错");
		}
		return devicelist;
	}
	
	@Override
	public int delUser(User user) {
		int result=1;
		try {
			ResultDomain resultDomain=DataService.UserInfoDelete(user);
			result = resultDomain.getResultCode();
		}catch(Exception e) {
			log.info("用户删除接口调用出错");
		}
		return result;
	}
	
	@Override
	public int addUser(User user) {
		int result=1;
		try {
			ResultDomain resultDomain=DataService.UserInfoUpload(user, 0);
			result = resultDomain.getResultCode();
		}catch(Exception e) {
			log.info("用户上传接口调用出错");
		}
		return result;
	}
	
	
	@Override
	public List<String> queryDepart(User user,List<Integer> departId) {
		List<String> departlist=new ArrayList<>();
		ResultDomain<List<Depart>> departResultDomain = DataService.DepartInfoQuery(user, -1, 0);
		if (null != departResultDomain.getResultData() && departResultDomain.getResultCode() == 0 && !departResultDomain.getResultData().isEmpty()) {
			List<Depart> departlists = departResultDomain.getResultData();
			Map<Integer, String> departMap = departlists.stream().collect(Collectors.toMap(Depart::getDepart_id, Depart::getDepart_name));
			if(departId!=null) {
				for(Integer Id:departId) {
					String name=departMap.get(Id);
					if(!"".equals(name)) {
						departlist.add(departMap.get(Id));
					}
					else {
						departlist.add(null);
					}
			    }
			}
		}else{
			log.info("部门查询接口调用出错");
		}
		return departlist;
	}
	
	@Override
	public String queryEmpname(User user,int faceId) {
		FaceInfo faceInfo=new FaceInfo();
		faceInfo.setAttribute(0);
		faceInfo.setFace_id(faceId);
	     ResultDomain<List<FaceInfo>> listResultDomain = DataService.FaceInfoQuery(user, faceInfo, "",
	             0, -1);
	     if (null != listResultDomain.getResultData() && listResultDomain.getResultData().size() == 1) {
	         List<FaceInfo> resultData = listResultDomain.getResultData();
	         return resultData.get(0).getName();
	     } else {
	         log.info("FaceInfoQuery接口错误,或查询结果有多个");
	         return null;
	     }
	}
	 
}
