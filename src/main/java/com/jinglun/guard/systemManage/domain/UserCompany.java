package com.jinglun.guard.systemManage.domain;

import com.jinglun.guard.user.domain.User;

public class UserCompany {
	private User user;                //用户信息
    private Company company;         //公司信息
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
   
}
