package com.jinglun.guard.systemManage.service.impl;

//import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.dataservice.DataService.ParamConfigInfo;
//import com.jinglun.guard.systemManage.domain.PublicParater;
import com.jinglun.guard.systemManage.service.PublicparaterService;
import com.jinglun.guard.user.domain.User;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author huanggang
 * 公共参数实现类
 */
@Service
@Slf4j
public class PublicparaterServiceImpl implements PublicparaterService{

	//通过DataService.CommonParamQuery接口获取所有公共参数
	@Override
	public List<ParamConfigInfo> queryAllPublicParater(User user) {
		
//		List<PublicParater> list = new ArrayList<PublicParater>();
//		ArrayList<ParamConfigInfo> params = new ArrayList<DataService.ParamConfigInfo>();
//		User u = new User();
//		u.setFace_library_id(1);
		//获取公共参数
		try {
//			int resut = DataService.CommonParamQuery(u, params);
			ResultDomain<List<ParamConfigInfo>> r = DataService.CommonParamQuery(user);
			int resut = r.getResultCode();
			if(resut==0){
				return r.getResultData();
			}else{
				return null;
			}
		} catch (Exception e) {
			log.error("公共参数接口调用出错");
		}
		
		return null;
		
	}

}
