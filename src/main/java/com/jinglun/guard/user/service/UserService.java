package com.jinglun.guard.user.service;


import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.user.domain.User;


public interface UserService {
	//登录
	public ResultDomain<User> queryUserStatus(String name,String password);

    //修改密码
	public ResultDomain<User> userInfoUpload(User user,int model);
	}
