package com.jinglun.guard.employeeManage.service;

import java.util.List;
import java.util.Map;

import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.employeeManage.domain.AddFaceInfoParam;
import com.jinglun.guard.employeeManage.domain.AddFaceInfoParam1;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.user.domain.User;

/**
 * 员工管理Service
 * 
 * @author cq
 *
 */
public interface EmployeeManageService {

	/**
	 * 解析excel为faceinfo对象
	 * 
	 * @param imgPath  照片路径
	 * @param filePath 文件路径
	 * @param map      部门映射
	 * @return faceinfo对象
	 */

	public List<FaceInfo> getDataFromExcel(String imgPath, String filePath);

	/**
	 * 校验excel
	 * 
	 * @param filePath 文件路径
	 * @return 校验结果，String型
	 */
	public String checkExcel(String filePath);

	/**
	 * 上报
	 * 
	 * @param user 登录用户
	 * @param face faceinfo对象
	 * @return 上报结果ResultDomain
	 */
	public ResultDomain<FaceInfo> FaceInfoUpload(User user, FaceInfo face);

	/**
	 * 部门id和部门名称映射
	 * 
	 * @param user     登录用户
	 * @param filePath 文件路径
	 * @return 部门映射map
	 */
	public Map<String, Integer> DepartInfoUpload(User user, String filePath);

	/**
	 * 获取excel中部门名称list
	 * 
	 * @param filePath
	 * @return 部门名称list
	 */
	public List<String> DepartInfo(String filePath);

	/**
	 * 上传完成后删除文件
	 * 
	 * @param filePath 文件路径
	 */
	public void delFolder(String filePath);

	/**
	 * 查询所有设备,不包含网络状态和终端连接时间
	 * @param user
	 * @return
	 */
	public List<Device> queryAllDevice(User user);

	ResultDomain<FaceInfo> addFaceInfo1(AddFaceInfoParam1 addFaceInfoParam, User user);
}
