package com.jinglun.guard.systemManage.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinglun.guard.systemManage.service.ParametercontrolService;
import com.jinglun.guard.user.domain.User;


/**
 * 
 * @author huanggang
 * 控制参数controller
 */
@Controller
@RequestMapping("/parametercontrol")
public class ParameterController {
	
	@Autowired
	private ParametercontrolService parametercontrolService;
			
	@RequestMapping("/showParam")
    public String showControlParam() {
		
		return "systemManage/parametercontro";
	}
	
	@RequestMapping("/insertParam")
	@ResponseBody
    public int insertControlParam(HttpSession httpSession, int value) {
		User user = (User) httpSession.getAttribute("user");
		
		return parametercontrolService.insertParaterControl(user,value);
		
	}
}
