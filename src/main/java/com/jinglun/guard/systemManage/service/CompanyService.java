package com.jinglun.guard.systemManage.service;

import java.util.List;

import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.systemManage.domain.Company;
import com.jinglun.guard.user.domain.User;


public interface CompanyService {
	public List<Company> companyQuery(User user);
	public User userQuery(User user,int count);
	public int addOreditCompany(Company company);
	public int delCompany(User user);
	public boolean isAdmin(Integer id);
	public User addCompanyUser(User user);
}
