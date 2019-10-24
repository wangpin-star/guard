package com.jinglun.guard.guest.service;

import java.util.List;

import com.jinglun.guard.employeeManage.domain.TreeDomain;
import com.jinglun.guard.guest.domain.Group;
import com.jinglun.guard.guest.domain.GroupFace;
import com.jinglun.guard.guest.domain.GuestInfo;
import com.jinglun.guard.user.domain.User;

public interface GuestService {

	public TreeDomain queryGroup(User user);
	
	public List<GuestInfo> queryAllguest(User user);
	
	public Group createGroup(User user,String groupName,Integer groupId);
	
	public int groupFaceConfig(User user,Group group,List<GroupFace> list);
	
	public String queryGroupface(User user,Group groupInfo);
	
	public int delGroup(User user, Integer groupId);
}
