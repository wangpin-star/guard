package com.jinglun.guard.systemManage.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import com.jinglun.guard.GuardApplication;
import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.ResultDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jinglun.guard.dataservice.DataService.ParamConfigInfo;
import com.jinglun.guard.dataservice.PageResult;
import com.jinglun.guard.systemManage.service.PublicparaterService;
import com.jinglun.guard.user.domain.User;

/**
 * 
 * @author huanggang
 * 公共参数control
 */
@Controller
@RequestMapping("/publicparameter")
public class PublicParameterController {

	private final String publicParamPassword = "8792";
	
	@Autowired
	private PublicparaterService publicparaterService;
	
	@RequestMapping("/showPublicparam")
    public String showPublicParam() {
		return "systemManage/parameterpublic";
	}
	
	@RequestMapping("/getPublicparam")
	@ResponseBody
    public String getPublicparam(HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		List<ParamConfigInfo> publicparaterlist = publicparaterService.queryAllPublicParater(user);
		if (publicparaterlist != null) {
			publicparaterlist = publicparaterlist.stream().filter(e ->
					e.getParam_key().equals("comm_server_ip")
					|| e.getParam_key().equals("heartbeat_server_ip")
					|| e.getParam_key().equals("net_servip"))
					.collect(Collectors.toList());
			PageResult pageResult = new PageResult();
			pageResult.setData(publicparaterlist);
			return JSON.toJSONString(pageResult);
		}
		return null;
	}

	@RequestMapping("/showPublicparamAdmin")
    public String showPublicparamAdmin(HttpSession httpSession) {
		String publicParamAuth = (String) httpSession.getAttribute("publicParamAuth");
		return "systemManage/parameterpublicAdmin";
	}

	@RequestMapping("/getPublicparamAdmin")
	@ResponseBody
    public String getPublicparamAdmin(HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		List<ParamConfigInfo> publicparaterlist = publicparaterService.queryAllPublicParater(user);
		if (publicparaterlist != null) {
			PageResult pageResult = new PageResult();
			pageResult.setData(publicparaterlist);
			return JSON.toJSONString(pageResult);
		}
		return null;
	}

	@RequestMapping("/checkPassword")
	@ResponseBody
	public String checkPassword(String password) {
		if (password.equals(publicParamPassword)) {
			return "0";
		} else {
			return "-1";
		}
	}

	@RequestMapping("/configPublicParam")
	@ResponseBody
	public String configPublicParam(HttpSession httpSession, @RequestBody List<ParamConfigInfo> paramConfigInfoList) {
		User user = (User) httpSession.getAttribute("user");
		ResultDomain<List<ParamConfigInfo>> listResultDomain = DataService.CommonParamConfig(user, paramConfigInfoList);
		//保存成功后修改比对服务ip全局变量
		if (listResultDomain.getResultCode()==0&&listResultDomain.getResultData()!=null){
			List<ParamConfigInfo> publicparaterlist = listResultDomain.getResultData();
				publicparaterlist = publicparaterlist.stream().filter(e -> e.getParam_key().equals("net_servip"))
						.collect(Collectors.toList());
				GuardApplication.s_matchServerIP = publicparaterlist.get(0).getParam_value();

		}
		return JSON.toJSONString(listResultDomain);
	}
	
	@RequestMapping("/getMaxorgStructure")
	@ResponseBody
    public String getMaxorgStructure(HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		List<ParamConfigInfo> publicparaterlist = publicparaterService.queryAllPublicParater(user);
		if (publicparaterlist != null) {
			for(ParamConfigInfo parm : publicparaterlist){
				if("max_organizational_structure".equals(parm.getParam_key())){
					return parm.getParam_value();
				}
			}
		}
		return null;
	}
}
