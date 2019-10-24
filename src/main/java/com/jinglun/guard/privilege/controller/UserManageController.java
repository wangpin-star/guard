package com.jinglun.guard.privilege.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.PageResult;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.devicemanage.service.DeviceService;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.employeeManage.service.impl.EmployeeManageServiceImpl;
import com.jinglun.guard.privilege.domain.Role;
import com.jinglun.guard.privilege.service.RoleService;
import com.jinglun.guard.privilege.service.UserManageService;
import com.jinglun.guard.systemManage.service.CompanyService;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.user.service.impl.UserServiceImpl;



@Controller
@RequestMapping("/userManage")
public class UserManageController {
	@Autowired
	private RoleService roleService;
	@Resource(name = "employeeManageServiceImpl")
    private EmployeeManageServiceImpl employeeManageServiceImpl;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private UserManageService userManageService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@RequestMapping("/toUserManage")
	public String toUserManage(HttpSession httpSession,Model model) {
		User user = (User) httpSession.getAttribute("user");
		model.addAttribute("user", user);
		return "userManage/index";
	}
	
	
	@RequestMapping("/toAddUser")
	public String toAddUser(HttpSession httpSession,Model model,@RequestParam(defaultValue = "0") Integer userId) {
		User user = (User) httpSession.getAttribute("user");
		List<FaceInfo> faceInfo=userManageService.faceInfo(user);
		List<Role> roleList=roleService.roleList(user, 0);
		List<Device> devicelist = userManageService.queryAllDevice(user,0,0,0,"");
		User u = new User();
		u.setUser_id(user.getUser_id());
		u.setFace_library_id(user.getFace_library_id());
		List<User> us=userManageService.userlist(u);
		
		if(null!=us) {
			model.addAttribute("userRole", us.get(0).getRoleList());
		}
		
		if(userId==0) {
			model.addAttribute("ids", "null");
			model.addAttribute("userlist", "null");
		}
		else {
			User users = new User();
			users.setUser_id(userId);
			users.setFace_library_id(user.getFace_library_id());
			List<User> userList=userManageService.userlist(users);
			if(null!=userList) {
				model.addAttribute("userlist", userList.get(0));
			}
			
		}
		model.addAttribute("flag", companyService.isAdmin(user.getUser_id()));
		model.addAttribute("userDepart", user.getDepartList());
		model.addAttribute("userTerm", user.getTermList());
		model.addAttribute("emplist", faceInfo);
		model.addAttribute("rolelist", roleList);
		model.addAttribute("devicelist", devicelist);
		model.addAttribute("userId", userId);              //选中用户ID
		return "userManage/addUser";
	}
	@RequestMapping("/termQuery")
	@ResponseBody
	public List<Device> termQuery(HttpSession httpSession,String ids) {
		String[] departlist=ids.split(",");
		User user = (User) httpSession.getAttribute("user");
		List<Device> devicelist = deviceService.queryAllDevice(user,0,0,0,"");
		List<Device> devicelists = new ArrayList<>();
		if(null!=devicelist&&!("").equals(departlist[0])) {
			for(Device device:devicelist) {
				for(String departs:departlist) {
					for(Integer depart:device.getDepart_id()) {
						if(Integer.parseInt(departs)==depart) {
							devicelists.add(device);
							break;
						}
					}
				}
			}
		}
		
		return devicelists;
	}
	
	@RequestMapping("/userlist")
	@ResponseBody
	public String userlist(HttpSession httpSession) {
		User users=(User) httpSession.getAttribute("user");
		User user = new User();
		user.setUser_id(0);
		user.setFace_library_id(users.getFace_library_id());
		List<User> userList=userManageService.userlist(user);
		if(userList!=null) {
			for(User u:userList) {
				List<String> departname=userManageService.queryDepart(user, u.getDepartList());
				if(null!=departname&&!departname.isEmpty()) {
					u.setDepart_name(departname);
				}
				if(u.getFace_id()!=0) {
					String empname=userManageService.queryEmpname(user, u.getFace_id());
					u.setEmpname(empname);
				}
			}
		}
		PageResult pageResult = new PageResult(userList);
		return JSON.toJSONString(pageResult);
	}
	
	
	@RequestMapping("delUser")
	@ResponseBody
	public int delUser(Integer userId) {
		int result;
		User user=new User();
		user.setUser_id(userId);
		result=userManageService.delUser(user);
		return result;
	}
	
	@RequestMapping("resetPwd")
	@ResponseBody
	public int resetPwd(@RequestBody User data) {
		data.setUser_password("123456");
		ResultDomain<User> resultDomain = userServiceImpl.userInfoUpload(data, 2);
		return resultDomain.getResultCode();
	}
	
	@RequestMapping("addUser")
	@ResponseBody
	public int addUser(Integer userId,@RequestBody User params,HttpSession httpSession) {
		int result;
		User users=(User) httpSession.getAttribute("user");
		params.setUser_id(userId);
		params.setFace_library_id(users.getFace_library_id());
		params.setCompany_name(users.getCompany_name());
		result=userManageService.addUser(params);
		return result;
	}
	
	
}
