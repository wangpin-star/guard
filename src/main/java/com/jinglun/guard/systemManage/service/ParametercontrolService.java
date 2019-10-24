package com.jinglun.guard.systemManage.service;

import com.jinglun.guard.user.domain.User;

/**
 * 
 * @author huanggang
 * 控制参数service
 */
public interface ParametercontrolService {
	
	public int insertParaterControl(User user,int value);
	public String netServerip(User user);
}
