package com.jinglun.guard.privilege.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.PageResult;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.employeeManage.domain.TreeDomain;
import com.jinglun.guard.employeeManage.domain.TreeUtil;
import com.jinglun.guard.privilege.domain.Menu;
import com.jinglun.guard.privilege.domain.Role;
import com.jinglun.guard.privilege.service.RoleService;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.user.domain.User;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class RoleServiceImpl implements RoleService{
	
	/**
	 * 
	 * @param user
	 * @return    菜单树
	 */
	public TreeDomain getMenuTree(User user) {
		List<Menu> menus=new ArrayList<>();
		try {
			ResultDomain<List<Menu>> menusResult=DataService.MenuQuery(0);
			if(null!=menusResult.getResultData()&&!menusResult.getResultData().isEmpty()) {
				menus=menusResult.getResultData();
			}
			
		}catch(Exception e) {
			log.info("菜单树接口调用出错");
		}
		return getMenuTree(user,menus);
	}
	
	public TreeDomain getMenuTree(User user,List<Menu> menulist) {
		TreeDomain treeDomain = new TreeDomain(0, user.getCompany_name(),user.getCompany_name(),0,null);
		if (null != menulist&&menulist.size() > 0) {
			ArrayList<TreeDomain> treeDomainList = new ArrayList<>();
			for(Menu menu:menulist) {
				TreeDomain domain = new TreeDomain();
				domain.setData(menu);
				domain.setDepart_id(menu.getMenu_id());
				domain.setParentId(menu.getParent_id());
				domain.setName(menu.getName());
				treeDomainList.add(domain);
			}
			treeDomain = TreeUtil.getTree(treeDomain, treeDomainList);
		} else {
			log.info("菜单树接口调用出错");
		}
		return treeDomain;
	}
	
	/**
	 *    通过menuId获取menuList
	 */
	public List<Menu> menulist(List<Integer> menuIds) {
		List<Menu> menulists=new ArrayList<Menu>();
		try {
			
			ResultDomain<List<Menu>> menusResult=DataService.MenuQuery(0);
			List<Menu> menus=menusResult.getResultData();
			for(Integer menuId:menuIds) {
				for(Menu menu:menus) {
					if(menu.getMenu_id()==menuId) {
						menulists.add(menu);
						break;
					}
				}
			}
			
		}catch(Exception e) {
			log.info("菜单树接口调用出错");
		}
		return menulists;
	}
	
	
	/**
	     *     角色列表显示
	 */
	public List<Role> roleList(User user,int roleId){
		ResultDomain<List<Role>> roleListResultDomain=DataService.RoleQuery(user, roleId);
		List<Role> rolelists=null;
		if(null!=roleListResultDomain.getResultData()&&roleListResultDomain.getResultData().size()>0) {
			rolelists=roleListResultDomain.getResultData();
		}else {
			log.info("角色查询接口调用出错");
		}
		//PageResult pageResult = new PageResult(roleListResultDomain.getResultCode(), rolelists);
		return rolelists;
	}


	@Override
	public int addOreditRole(User user, Role role,List<User> users) {
		int result = 1;
		try {
			if(role.getRole_id()==0) {
				List<Role> roles=new ArrayList<>();
				if(null!=users&&!users.isEmpty()) {
					roles=users.get(0).getRoleList();
				}
				ResultDomain resultDomain=DataService.RoleInfoUpload(user,role);
				if(null!=resultDomain.getResultData()&&resultDomain.getResultCode()==0) {
					roles.add((Role)resultDomain.getResultData());
					user.setRoleList(roles);
					ResultDomain resultDomains=DataService.UserInfoUpload(user, 0);
					int res= resultDomains.getResultCode();
					if(res==1) {
						delRole(user,(Role)resultDomain.getResultData());
					}
					else {
						result=0;
					}
				}
			}
			else {
				ResultDomain resultDomain=DataService.RoleInfoUpload(user,role);
				result = resultDomain.getResultCode();
			}
			
		} 
		catch(Exception e) {
			log.info("角色添加接口调用出错");
		}
		
		return result;
	}

	@Override
	public int delRole(User user, Role role) {
		int result=1;
		try {
			ResultDomain resultDomain=DataService.RoleInfoDelete(user, role);
			result = resultDomain.getResultCode();
		}catch(Exception e) {
			log.info("角色删除接口调用出错");
		}
		return result;
	}
	
	
}
