package com.jinglun.guard.devicemanage.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.zxing.WriterException;
import com.jinglun.guard.dataservice.PageResult;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.dataservice.DataService.HintConfigInfo;
import com.jinglun.guard.dataservice.DataService.ParamConfigInfo;
import com.jinglun.guard.devicemanage.domain.DeviceOnline;
import com.jinglun.guard.devicemanage.domain.TermInfo;
import com.jinglun.guard.devicemanage.service.DeviceService;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.employeeManage.domain.TreeDomain;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.visitorManage.service.VisitorManagerService;


/**
 * 
 * @author huanggang
   * 设备管理管控类
 */
@Controller
@RequestMapping("/device")
public class DeviceController {
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private VisitorManagerService visitormanagerService;
	
	private static final String DEVICEDATASTATUS = "devicedatastaus";
	private static final String DEVICEONLINESTATUS = "deviceonlinestatus";
	
    @RequestMapping("/showDevice")
    public String showDevice(HttpSession session,Model model) {
    	User user = (User) session.getAttribute("user");
		//获取初始修订号
    	String revo = "terminal_info_version";
		String devicedatastaus = visitormanagerService.queryDataStatus(user,revo);
		session.setAttribute(DEVICEDATASTATUS, devicedatastaus);
		//获取设备初始在线状态
		DeviceOnline ds = deviceService.queryTermonlineStatus(user);
		String deviceonlinestatus = ds.getOnline()+","+ds.getUnderline();
		session.setAttribute(DEVICEONLINESTATUS, deviceonlinestatus);
		//获取设备列表自动刷新的时间参数
		int time = visitormanagerService.queryVisitrefreshTime(user);
		model.addAttribute("time", time);
		model.addAttribute("user", user);
        return "devicemanage/device";
    }
    
    @RequestMapping("/showConfig")
    public String showConfig(HttpServletRequest request,String id,String termname,Model model) {
    	HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		String[] termIdarray = id.split(",");
		//显示设备名称
		model.addAttribute("termid", id);
		model.addAttribute("termnamelist", termname);
		
		Map<String, Map<Integer, String>> map = deviceService.queryAllEmployee(user,termIdarray);
		List<String> facename = new ArrayList<>();
		List<String> authfacename = new ArrayList<>();
		
		
		for (Map.Entry<String, Map<Integer, String>> entry : map.entrySet()) {
		    String key = entry.getKey();
		    if("auth".equals(key)){
		    	//已授权人员信息
		    	Map<Integer, String> authlist = entry.getValue();
				for(Map.Entry<Integer, String> authentry: authlist.entrySet()){
					facename.add(authentry.getValue());
					authfacename.add(authentry.getValue());
				}
				model.addAttribute("authlist", authlist);
		    }else{
		    	//未授权人员信息
				Map<Integer, String> faceinfolist = entry.getValue();
				for(Map.Entry<Integer, String> faceinfoentry : faceinfolist.entrySet()){
					facename.add(faceinfoentry.getValue());
				}
				model.addAttribute("faceinfolist", faceinfolist);
		    }
		}
		
		int authlistsize = authfacename.size();
		model.addAttribute("authlistsize", authlistsize);
		
		String facenameStr="";
		for(int i=0;i<facename.size();i++){
			facenameStr += facename.get(i);
            if(i<facename.size()-1){
            	facenameStr += ",";
            }
        }
		model.addAttribute("facenameStr", facenameStr);
		
        return "devicemanage/authconfig";
    }
    
    @RequestMapping("/queryDevice")
	@ResponseBody
    public String queryDeviceList(HttpServletRequest request,
    		@RequestParam(value = "categoryval", defaultValue = "0") Integer categoryval,
    		@RequestParam(value = "networkval", defaultValue = "0") Integer networkval,
    		@RequestParam(value = "statusval", defaultValue = "0") Integer statusval,
    		@RequestParam(value = "searchContent", defaultValue = "") String searchContent) {
    	HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		List<Device> devicelist = deviceService.queryAllDevice(user,categoryval,networkval,statusval,searchContent);
		//确认显示哪种终端图片1.14T，2.facecheck动态，3.iDR系列，4.未知
		if(null != devicelist){
			for(Device d:devicelist){
				if(null != d.getDevice_type()){
					if(d.getDevice_type().contains("iDR")){
						d.setTerm_mode(3);
					}else if(d.getDevice_type().contains("CI")){
						d.setTerm_mode(1);
					}else if(d.getDevice_type().contains("FACE")){
						d.setTerm_mode(2);
					}else if("未知".equals(d.getDevice_type())){
						d.setTerm_mode(4);
					}
				}
			}
		}
		
		PageResult pageResult = new PageResult();
		pageResult.setData(devicelist);
		if(null!=devicelist) {
			pageResult.setCount(devicelist.size());
		}
		else {
			pageResult.setCount(0);
		}
		return JSON.toJSONString(pageResult);
		
	}
    
    @RequestMapping("/deviceOnline")
	@ResponseBody
    public DeviceOnline onlineDevice(HttpServletRequest request) {
    	HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		return deviceService.queryTermonlineStatus(user);
		
	}
    /**
     * 
     * @param session
     * @param id           查询选中设备中所有参数是否有空
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/checknull")
    public String checknull(HttpSession session,String ids,String names,Model model) {
    	User user=(User)session.getAttribute("user");
    	String termId_array[] = ids.split(",");
    	String termName_array[] = names.split(",");
    	int count=0;
    	for(int i=0;i<termId_array.length;i++) {
			TermInfo paramInfo=deviceService.queryAllParam(user,Integer.parseInt(termId_array[i]));
			if(paramInfo==null) {
				return termName_array[i];
			}
			else
				count++;
		}
    	if(count==termId_array.length) {
    		return "ok";
    	}
    	return names;
		
    }
    
    
    @RequestMapping("/QueryTerm")
    public String QueryTerm(HttpSession session,String name,String id,Model model) {
    	User user=(User)session.getAttribute("user");
		String termId_array[] = id.split(",");
		String termName_array[] = name.split(",");
		session.setAttribute("Term_ids",termId_array);
		TermInfo termInfo=deviceService.queryAllParam(user,Integer.parseInt(termId_array[0]));
		List<ParamConfigInfo> paramInfo=termInfo.getParamInfo();
		List<HintConfigInfo> hintInfo=termInfo.getHintInfo();
		if(null!=paramInfo&&null!=hintInfo) 
		{
			model.addAttribute("paramInfo", paramInfo);
			model.addAttribute("hintInfo", hintInfo);
			if(termName_array.length==1)
			{
				model.addAttribute("fg", 1);
				model.addAttribute("names", termName_array[0]);
				return "devicemanage/terminfo";
			}
			//选中多个设备时进行参数的设置
			else {
				model.addAttribute("fg",0);
				model.addAttribute("names", name);
				return "devicemanage/terminfos";

			}
			
		}
		else {
			model.addAttribute("paramInfo", paramInfo);
			model.addAttribute("hintInfo", hintInfo);
			model.addAttribute("fg", 0);
			model.addAttribute("names", name);
			return "devicemanage/terminfos";
		}
    }
    
    
    /**
     * 
     * @param paramList
     * @param session         参数配置
     * @param model
     * @return
     */
    
    @RequestMapping("/TermConfig")
    @ResponseBody
    public int  TermConfig(@RequestBody TermInfo termInfo, HttpSession session,Model model) {
    	int result=1;
    	User user=(User)session.getAttribute("user");
    	String ids[]=(String[])session.getAttribute("Term_ids");
    	for(int i=0;i<ids.length;i++)
		{ 
    		result= deviceService.paramConfig(user,Integer.parseInt(ids[i]), termInfo,ids.length);
		}
    	return result;
    }
    
    
    
    @RequestMapping("/deptManage")
    public String DeptManage(HttpSession session,String name,String termid,Model model) 
    {
    	User user=(User)session.getAttribute("user");
    	String TermId_array[] = termid.split(",");
    	String TermName_array[] = name.split(",");
    	session.setAttribute("termId", TermId_array);
    	session.setAttribute("termName", TermName_array);
    	model.addAttribute("userDepart", user.getDepartList());
    	if(TermId_array.length==1)
    	{
    		List<Integer> deptId=deviceService.queryDept(user,Integer.parseInt(TermId_array[0]));
    		if(deptId==null)
    		{
    			model.addAttribute("ids", "null");
    		}
    		else
    		{
    			model.addAttribute("ids", deptId);
    		}
    		
    		model.addAttribute("names",TermName_array[0] );
    	}
    	else 
    	{
    		
    		model.addAttribute("ids", "null");
    		model.addAttribute("names","");
    	}
        return "devicemanage/deptmanage";
    }
    
    //启用\停用\删除
    
    @RequestMapping("/operation")
    @ResponseBody
    public int Operation(HttpSession session,String id,String mode,Model model) 
    {
    	int result=1;
    	int res=1;
    	int count=0;
    	User user = (User) session.getAttribute("user");
    	String[] ids=id.split(",");
    	for(int i=0;i<ids.length;i++) {
    		count++;
    		res=deviceService.operation(user, Integer.parseInt(id), mode);
    	}
    	if(count==ids.length&&res==0) {
    		result=0;
    	}
        return result;
    }
    
    
    @RequestMapping("/deptConfig")
    @ResponseBody
    public int DeptConfig(HttpSession session,String ids,String names,Model model) 
    {
    	int result;
    	String TermName[]=null;
    	if("".equals(names))
    	{
    		TermName=(String [])session.getAttribute("termName");
    	}
    	else {
    		TermName=names.split(",");
    	}
    	User user = (User) session.getAttribute("user");
        String TermId[]=(String [])session.getAttribute("termId");
    	result=deviceService.deptConfig(user, TermName, TermId, ids);
        return result;
    }
    /**
	    * 弹出层显示二维码
	 */
	@RequestMapping(value = "/getQRCode")
	public void QRCode(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws IOException, WriterException
	{
		String name="开通二维码";
		String bottom="二维码可从\"设备管理->开通二维码\"处获取";
		User user=(User)session.getAttribute("user");
		String companyName=user.getCompany_name();
		deviceService.qRCodeInit(response, request,companyName,bottom,name);
	}
	/**
	     * 下载二维码
	 */
	@RequestMapping(value = "/downloadQRCode")
	public void downloadQRCode(HttpServletResponse response,HttpServletRequest request,HttpSession session) throws IOException, WriterException
	{
		String name="开通二维码";
		String bottom="二维码可从\"设备管理->开通二维码\"处获取";
		User user=(User)session.getAttribute("user");
		String companyName=user.getCompany_name();
		deviceService.qRCodeInit(response, request,companyName,bottom,name);
	}
	
	/**
	 * 显示权限配置部门树
	 * @param httpSession
	 * @return
	 */
	@RequestMapping("/selectDepartTree")
	@ResponseBody
	public String selectDepartTree(HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("user");
		
		TreeDomain treeDomain = deviceService.getDepartTree(user);
		
		return JSON.toJSONString(treeDomain);
	}
	
	 @RequestMapping("/showfaceinfo")
	 @ResponseBody
	 public List<Integer> showFaceinfo(HttpSession httpSession,int id) {
		 User user = (User) httpSession.getAttribute("user");
		 List<FaceInfo> faceinfolist = deviceService.queryDepartFaceinfo(user, id);
		 List<Integer> list = new ArrayList<>();
		 if(faceinfolist != null){
			 for(FaceInfo faceinfo:faceinfolist){
				 list.add(faceinfo.getFace_id());
			 }
		 }
			 
		 return list;
	 }
	 
	 @RequestMapping("/savefaceinfo")
	 @ResponseBody
	 public int saveFaceinfo(HttpSession httpSession,String id,String termid) {
		 User user = (User) httpSession.getAttribute("user");
		 String[] faceIdarray = id.split(",");
		 String[] termIdarray = termid.split(",");
		 
		 return deviceService.saveTermFaceinfo(user,faceIdarray,termIdarray);
	 }
	 
	 @RequestMapping("/QueryDevicerecordStatus")
	 @ResponseBody
	 public String queryDevicerecordStatus(HttpSession session){
		String oldstatus = String.valueOf(session.getAttribute(DEVICEDATASTATUS));
		String oldonlinestatus = String.valueOf(session.getAttribute(DEVICEONLINESTATUS));
		
		User user=(User)session.getAttribute("user");
		String revo = "terminal_info_version";
		String newstatus = visitormanagerService.queryDataStatus(user,revo);
		
		DeviceOnline ds = deviceService.queryTermonlineStatus(user);
		String newonlinestatus = ds.getOnline()+","+ds.getUnderline();
			
		if(newstatus.equals(oldstatus)){
			if(newonlinestatus.equals(oldonlinestatus)){
				return "unchange";
			}else{
				session.setAttribute(DEVICEONLINESTATUS, newonlinestatus);
				return "change";
			}
		}else{
			session.setAttribute(DEVICEDATASTATUS, newstatus);
			session.setAttribute(DEVICEONLINESTATUS, newonlinestatus);
			return "change";
		}
	}
	 
}
