package com.jinglun.guard.systemManage.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.dataservice.DataService.ParamConfigInfo;
import com.jinglun.guard.systemManage.service.ParametercontrolService;
import com.jinglun.guard.user.domain.User;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author huanggang
 * 控制参数实现类
 */
@Service
@Slf4j
public class ParametercontrolServiceImpl implements ParametercontrolService{

	@Override
	public int insertParaterControl(User user,int value) {

		ArrayList<ParamConfigInfo> params = new ArrayList<DataService.ParamConfigInfo>();
		
		ParamConfigInfo para =new DataService.ParamConfigInfo();
		para.setParam_key("max_organizational_structure");
		para.setParam_name("最大组织结构");
		para.setParam_value(String.valueOf(value));
		
		params.add(para);
		//上报控制参数
		try {
			ResultDomain<List<ParamConfigInfo>> r = DataService.CommonParamConfig(user, params);
			
			return r.getResultCode();
		} catch (Exception e) {
			log.error("控制参数接口调用出错");
		}
		
		return 9;
		
	}
	@Override
	public String netServerip(User user) {
		try {
			ResultDomain<List<ParamConfigInfo>> r = DataService.CommonParamQuery(user);
			int resut = r.getResultCode();
			if(resut==0){
				List<ParamConfigInfo> publicparaterlist = r.getResultData();
				if (publicparaterlist != null) {
					publicparaterlist = publicparaterlist.stream().filter(e -> e.getParam_key().equals("net_servip"))
							.collect(Collectors.toList());
					
					return publicparaterlist.get(0).getParam_value();
				}
				
			}else{
				return "";
			}
		} catch (Exception e) {
			log.error("公共参数接口调用出错");
		}
		return "";
	}
}
