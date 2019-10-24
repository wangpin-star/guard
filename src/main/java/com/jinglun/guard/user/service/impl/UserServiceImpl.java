package com.jinglun.guard.user.service.impl;

import org.springframework.stereotype.Service;
import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	

	//登录
	public ResultDomain<User> queryUserStatus(String name, String password) 
	{
		try {
			User user = new User();
			user.setUser_name(name);
			user.setUser_password(password);
			ResultDomain<User> result = DataService.UserLogin(user);	
			if(result!=null)
			{	
				return result;
			}
		} catch (Exception e) {
			log.error("用户登录状态接口调用出错");
		}
		return null;
	}
	
	@Override
	//修改密码
	public ResultDomain<User> userInfoUpload(User user, int mode) {
		return DataService.UserInfoUpload(user, mode);
	}
}
