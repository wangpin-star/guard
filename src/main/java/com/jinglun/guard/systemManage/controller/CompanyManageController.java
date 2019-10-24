package com.jinglun.guard.systemManage.controller;

import com.alibaba.fastjson.JSON;
import com.google.zxing.WriterException;
import com.jinglun.guard.dataservice.PageResult;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.devicemanage.service.DeviceService;
import com.jinglun.guard.privilege.service.RoleService;
import com.jinglun.guard.privilege.service.UserManageService;
import com.jinglun.guard.systemManage.domain.Company;
import com.jinglun.guard.systemManage.service.CompanyService;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.user.service.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequestMapping("/companyManage")
@Controller
public class CompanyManageController {

	@Autowired
	private CompanyService companyService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private UserManageService userManageService;
	
    @RequestMapping("/toCompanyManage")
    public String toCompanyManage(Model model) {
    	User user=new User();
    	user.setFace_library_id(0);
        List<Company> companyList=companyService.companyQuery(user);
        if(null!=companyList&&!companyList.isEmpty()) {
        	model.addAttribute("count",companyList.size() );
        }
        else {
        	model.addAttribute("count",0);
        }
        return "companyManage/companyList";
    }

    @RequestMapping("/selectCompanyList")
    @ResponseBody
    public String selectCompanyList(HttpSession session) {
    	User user=new User();
    	user.setFace_library_id(0);
        List<Company> companyList=companyService.companyQuery(user);
        
        User users=null;
        if(null!=companyList) {
        	for(int i=0;i<companyList.size();i++) {
            	users=companyService.userQuery(user,i);
            	if(null!=users) {
            		companyList.get(i).setLoginname(users.getUser_name());
                    companyList.get(i).setRolename("公司管理员");
                    companyList.get(i).setUserid(users.getUser_id());
                    companyList.get(i).setUserfaceid(users.getFace_id());
            	}
        	}
        }
        
        PageResult pageResult = new PageResult();
        pageResult.setData(companyList);
        return JSON.toJSONString(pageResult);
    }

    @RequestMapping("/toAddCompany")
    public String toAddCompany(Model model, @RequestParam(value = "companyId", defaultValue = "0") Integer companyId) {
        model.addAttribute("companyId",companyId);
        return "companyManage/addCompanys";
    }
    
    @RequestMapping("/toEditCompany")
    public String toEditCompany(Model model, Integer companyId,Integer userId,Integer userFaceId) {
        model.addAttribute("companyId",companyId);
        model.addAttribute("userId",userId);
        model.addAttribute("userFaceId",userFaceId);
        return "companyManage/editCompanys";
    }

    @RequestMapping("/addCompany")
    @ResponseBody
    public int addCompany(@RequestBody Company company) {
       int result;
        result=companyService.addOreditCompany(company);
        return result;
    }
    
    
    @RequestMapping("delCompany")
	@ResponseBody
	public int delCompany(Integer companyId) {
		int result;
		User user=new User();
		user.setFace_library_id(companyId);
		result=companyService.delCompany(user);
		return result;
	}
    
    @RequestMapping("delCompanyUser")
	@ResponseBody
	public int delCompanyUser(Integer companyId,Integer userId) {
		int result;
		User user=new User();
		user.setFace_library_id(companyId);
		user.setUser_id(userId);
		int m=companyService.delCompany(user);
		int n=userManageService.delUser(user);
		if(m==0&&n==0) {
			result=0;
		}
		else {
			result=1;
		}
		return result;
	}
    
    @RequestMapping("/addCompanyUser")
    @ResponseBody
    public int addCompanyUser(@RequestBody User params) {
       int result;
       User users=companyService.addCompanyUser(params);
       result=userManageService.addUser(users);
       return result;
    }
    
    /**
	 * 弹出层显示二维码
	 */
	@RequestMapping(value = "/getQRCode")
	public void QRCode(HttpServletResponse response,HttpServletRequest request,String companyName) throws IOException, WriterException
	{
		String bottom="二维码可从\"系统管理->公司管理\"处获取";
		deviceService.qRCodeInit(response, request,companyName,bottom,companyName);
	}
	/**
	 * 下载二维码
	 */
	@RequestMapping(value = "/downloadQRCode")
	public void downloadQRCode(HttpServletResponse response,HttpServletRequest request,String companyName) throws IOException, WriterException
	{
		String bottom="二维码可从\"系统管理->公司管理\"处获取";
		deviceService.qRCodeInit(response, request,companyName,bottom,companyName);
	}
}
