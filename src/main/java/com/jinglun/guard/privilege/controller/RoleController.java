package com.jinglun.guard.privilege.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jinglun.guard.dataservice.PageResult;
import com.jinglun.guard.employeeManage.domain.TreeDomain;
import com.jinglun.guard.privilege.domain.Menu;
import com.jinglun.guard.privilege.domain.Role;
import com.jinglun.guard.privilege.domain.Role_Permission;
import com.jinglun.guard.privilege.service.RoleService;
import com.jinglun.guard.privilege.service.UserManageService;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.user.service.impl.UserServiceImpl;


@Controller
@RequestMapping("/roleManage")
public class RoleController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserManageService userManageService;
	
	@RequestMapping("/roleSet")
	public String selectDepartTree(Model model,HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		model.addAttribute("user", user);
		return "roleManage/index";
	}
	
	
	@RequestMapping("/selectRoleList")
	@ResponseBody
	public String selectRoleList(HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		
		List<Role> roleList=roleService.roleList(user, 0);
		PageResult pageResult = new PageResult(roleList);
		return JSON.toJSONString(pageResult);
	}
	
	
	@RequestMapping("/toAddRole")
	public String toaddRole(HttpSession httpSession,Model model,@RequestParam(defaultValue = "0") Integer roleId) {
		User user = (User) httpSession.getAttribute("user");
		
		TreeDomain treeDomain = roleService.getMenuTree(user);  //所有的menulist
		TreeDomain tree = roleService.getMenuTree(user,user.getMenuList()); //当前用户的menulist
		if(roleId!=0) {
			List<Role> roleList=roleService.roleList(user, roleId);  //选中角色的menulist
			if(null!=roleList&&!roleList.isEmpty()) {
				List<Menu> menulist = roleList.get(0).getMenu_list();
				TreeDomain treeDomains = roleService.getMenuTree(user,menulist);
				model.addAttribute("menulist", treeDomains.getChildren());
				model.addAttribute("size", treeDomains.getChildren().size());
			}
			else {
				model.addAttribute("menulist", null);
				model.addAttribute("size", 0);
			}
			
		}
		else {
			model.addAttribute("menulist", 0);
			model.addAttribute("size",0);
		}
		model.addAttribute("menu", treeDomain);
		model.addAttribute("usermenulist", tree);
		model.addAttribute("roleId", roleId);
		return "roleManage/addRole";
	}
	
	@RequestMapping("addRole")
	@ResponseBody
	public int addRole(@RequestBody Role_Permission paramList,Integer roleId,HttpSession httpSession) {
		int result;
		User user = (User) httpSession.getAttribute("user");
		List<Menu> menulist=roleService.menulist(paramList.getMenuList());
		List<User> userList=userManageService.userlist(user);
		Role role=new Role();
		role.setRole_id(roleId);
		role.setName(paramList.getName());
		role.setMenu_list(menulist);
		role.setRemark(paramList.getDescript());
		result=roleService.addOreditRole(user, role,userList);
		return result;
	}
	
	@RequestMapping("delRole")
	@ResponseBody
	public int delRole(Integer roleId,HttpSession httpSession) {
		int result;
		User user = (User) httpSession.getAttribute("user");
		Role role=new Role();
		role.setRole_id(roleId);
		result=roleService.delRole(user, role);
		return result;
	}
	
	@RequestMapping("QueryRole")
	@ResponseBody
	public List<Role> queryRole(@RequestBody List<Integer> roleLists,HttpSession httpSession){
		User user = (User) httpSession.getAttribute("user");
		List<Role> rolelist = new ArrayList<>();
		for(Integer roleId:roleLists) {
			rolelist.add(roleService.roleList(user, roleId).get(0));
		}
		
		return rolelist;
	}
	
}
