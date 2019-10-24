package com.jinglun.guard.guest.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jinglun.guard.employeeManage.domain.TreeDomain;
import com.jinglun.guard.guest.domain.Group;
import com.jinglun.guard.guest.domain.GroupFace;
import com.jinglun.guard.guest.domain.GuestInfo;
import com.jinglun.guard.guest.service.GuestService;
import com.jinglun.guard.privilege.domain.Role;
import com.jinglun.guard.user.domain.User;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/guest")
public class GuestController {

	@Autowired
	private GuestService guestService;
	
	@RequestMapping("/showGuest")
    public String showGuest() {
		
		return "guestmanage/guest";
	}
	
	@RequestMapping("/queryGroup")
	@ResponseBody
    public String queryGroup(HttpSession session) {
		User user = (User) session.getAttribute("user");
		//查询有无分组
		TreeDomain grouplist = guestService.queryGroup(user);
		
		return JSON.toJSONString(grouplist);
	}
	
	@RequestMapping("/showGroup")
    public String showGroup(HttpSession session,Model model,
    		@RequestParam(value = "groupName", defaultValue = "") String groupName,
    		@RequestParam(value = "groupId", defaultValue = "0") Integer groupId){
		User user = (User) session.getAttribute("user");
		//查询所有嘉宾
		List<GuestInfo> guestlist = guestService.queryAllguest(user);
		
		//若groupName,groupId不为空，为编辑分组
		String groupFace = "";
		if(null != groupId && groupId > 0){
			Group groupInfo = new Group();
			groupInfo.setGroup_id(groupId);
			groupFace = guestService.queryGroupface(user, groupInfo);
		}
		
		model.addAttribute("groupFace", groupFace);
		model.addAttribute("groupId", groupId);
		model.addAttribute("groupNamedit", groupName);
		model.addAttribute("guestlist", JSON.toJSONString(guestlist));
		return "guestmanage/group";
	}

	@RequestMapping("/toGroupManage")
	public String  createGroup(){

		return "guestmanage/groupManage";

	}

	@RequestMapping("/createGroup")
	@ResponseBody
	public Group createGroup(HttpSession session, String groupName,Integer groupId){
		User user = (User) session.getAttribute("user");
		
		return  guestService.createGroup(user, groupName ,groupId);
	}
	
	@RequestMapping("/groupFaceConfig")
	@ResponseBody
	public int groupFaceConfig(HttpSession session,
			@RequestParam(value = "jsonGroup", defaultValue = "") String jsonGroup,
			@RequestParam(value = "jsonData", defaultValue = "") String jsonData,
			@RequestParam(value = "append", defaultValue = "") String append){
		User user = (User) session.getAttribute("user");
		
		Group group = new Group();
		if(!"".equals(jsonGroup)){
			JSONObject jsStr = JSONObject.fromObject(jsonGroup);
			int groupid = jsStr.getInt("group_id");
			group.setGroup_id(groupid);
		}
		
		//追加分组
		if("append".equals(append)){
			group.setMode(0);
		}else{//设置全集
			group.setMode(1);
		}
		
		//获取分组人员信息
		List<GroupFace> list = JSON.parseArray(jsonData,GroupFace.class);
		return guestService.groupFaceConfig(user, group, list);
	}
	
	@RequestMapping("/delGroup")
	@ResponseBody
	public int delGroup(HttpSession session,Integer groupId){
		User user = (User) session.getAttribute("user");
		
		return guestService.delGroup(user, groupId);
	}
	
	//测试
	@RequestMapping("/toAddGuest")
	public String toAddGuest(HttpSession httpSession, Model model, @RequestParam(defaultValue = "0") Integer faceId){
		model.addAttribute("life_photo_max_size", "1024");
		model.addAttribute("faceId", faceId);
		model.addAttribute("face_info_input_mod", "1");
		JSONObject[] data = new JSONObject[10];
		for(int i=1;i<11;i++) {
			data[i-1] = new JSONObject();
			data[i-1].put("id", i);
			data[i-1].put("name", "meeting"+i);
		}
		JSONObject jsons = new JSONObject();
		jsons.put("data", data);
		
		model.addAttribute("meetingList",jsons.toString());
		return "guestmanage/addGuest";
	}
	
}
