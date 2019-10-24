package com.jinglun.guard.privilege.service;

import java.util.List;

import com.jinglun.guard.employeeManage.domain.TreeDomain;
import com.jinglun.guard.privilege.domain.Menu;
import com.jinglun.guard.privilege.domain.Role;
import com.jinglun.guard.user.domain.User;

public interface RoleService {
	public TreeDomain getMenuTree(User user);
	public TreeDomain getMenuTree(User user,List<Menu> menulist);
	public List<Role> roleList(User user,int roleId);
	public int addOreditRole(User user,Role role,List<User> users);
	public List<Menu> menulist(List<Integer> menuIds);
	public int delRole(User user,Role role);
}
