package com.jinglun.guard.systemManage.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.privilege.domain.Menu;
import com.jinglun.guard.privilege.domain.Role;
import com.jinglun.guard.systemManage.domain.Company;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.systemManage.service.CompanyService;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.visitorManage.domain.BatVisitor;
import com.jinglun.guard.visitorManage.domain.FaceVisitInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService{
	@Override
	public List<Company> companyQuery(User user) {
	List<Company> companylist = null;
		try {
			ResultDomain<List<Company>> companys=DataService.CompanyQuery(user);
			companylist=companys.getResultData();
		}catch(Exception e) {
			log.error("公司查询接口调用出错");
		}
		return companylist;
	}
	@Override
	public User userQuery(User user,int count) {
		   List<User> userlist = null;
			try {
				ResultDomain<List<User>> users=DataService.UserInfoQuery(user);
				if(null!=users.getResultData()&&!users.getResultData().isEmpty()) {
					userlist=users.getResultData();
					if(userlist.size()-1>=count&&null!=userlist.get(count)) {
						return userlist.get(count);
					}
				}
				
				
			}catch(Exception e) {
				log.error("公司管理员查询接口调用出错");
			}
			return null;
		}
	
	public boolean isAdmin(Integer id) {
		User user=new User();
		user.setFace_library_id(0);
		try {
			ResultDomain<List<User>> users=DataService.UserInfoQuery(user);
			if(null!=users.getResultData()&&!users.getResultData().isEmpty()) {
				List<User> userlist=users.getResultData();
				userlist = userlist.stream().filter(e -> e.getUser_id()==id)
						.collect(Collectors.toList());
				if(null!=userlist&&!userlist.isEmpty()) {
					return true;
				}
			}
		}catch(Exception e) {
			log.error("公司管理员查询接口调用出错");
		}
		return false;
	}
	
	@Override
	public int addOreditCompany(Company company) {
		try {
			ResultDomain<Company> companyResultDomain = DataService.CompanyInfoUpload(company);
			if (companyResultDomain.getResultCode() == 0&&null!=companyResultDomain.getResultData()) {
				Company resultData = companyResultDomain.getResultData();
				return resultData.getFace_library_id();
			}
			if(companyResultDomain.getResultCode() == -8) {
				return -8;
			}
		}catch(Exception e) {
			log.error("公司上传接口调用出错");
		}
		return -1;
		
	}
	@Override
	public int delCompany(User user) {
		int result=1;
		try {
			ResultDomain resultDomain=DataService.CompanyDelete(user);
			result = resultDomain.getResultCode();
		}catch(Exception e) {
			log.info("公司删除接口调用出错");
		}
		return result;
	}
	@Override
	public User addCompanyUser(User user) {
	        List<Role>  roleList=new ArrayList<>();
	        Role role=new Role();
			role.setRole_id(2);
			role.setName("公司管理员");
			role.setRemark("公司管理员");
			roleList.add(role);
			user.setRoleList(roleList);
			return user;
	}
    

}
