package com.jinglun.guard.devicemanage.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.WriterException;
import com.jinglun.guard.dataservice.DataService.ParamConfigInfo;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.devicemanage.domain.DeviceOnline;
import com.jinglun.guard.devicemanage.domain.TermFace;
import com.jinglun.guard.devicemanage.domain.TermInfo;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.employeeManage.domain.TreeDomain;
import com.jinglun.guard.user.domain.User;

/**
 * 
 * @author huanggang
 * 设备管理接口
 */
public interface DeviceService {
	
	public List<Device> queryAllDevice(User user,Integer categoryval,Integer networkval,Integer statusval,String queryval);
	public void qRCodeInit(HttpServletResponse response,HttpServletRequest request,String companyName,String bottom,String name) throws IOException;
	public TermInfo queryAllParam(User user,int id);
	public List<Integer> queryDept(User user,int termid);
	public int paramConfig(User user,int id,TermInfo termInfo,int length);
	public int  deptConfig(User user,String[] termName,String[] termId,String ids);
	public int  operation(User user,int id,String mode);
	public DeviceOnline queryTermonlineStatus(User user);
	public TreeDomain getDepartTree(User user);
	public Map<String, Map<Integer, String>> queryAllEmployee(User user,String[] termIdarray);
	public int saveTermFaceinfo(User user,String[] faceIdarray,String[] termIdarray);
	public List<TermFace> queryTermFace(User user, int termId);
	public List<FaceInfo> queryDepartFaceinfo(User user,int departId);
}
