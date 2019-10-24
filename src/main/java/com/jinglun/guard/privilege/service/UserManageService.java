package com.jinglun.guard.privilege.service;

import java.util.List;

import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.user.domain.User;

public interface UserManageService {
	public List<Device> deviceQuery(User user,List<Integer> departlist);
	public List<User> userlist(User user);
	public int delUser(User user);
	public int addUser(User user);
	public List<FaceInfo> faceInfo(User user);
	public List<Device> queryAllDevice(User user,Integer categoryval,Integer networkval,Integer statusval,String queryval);
	public List<String> queryDepart(User user,List<Integer> departId);
	public String queryEmpname(User user,int faceId);
}
