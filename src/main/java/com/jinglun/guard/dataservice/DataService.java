package com.jinglun.guard.dataservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.compress.archivers.zip.ScatterStatistics;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.jinglun.guard.GuardApplication;
import com.jinglun.guard.attendance.domain.AttendanceInfo;
import com.jinglun.guard.attendance.domain.AttendanceQueryInfo;
import com.jinglun.guard.attendance.domain.FaceRecordInfo;
import com.jinglun.guard.carManage.domain.CarInfo;
import com.jinglun.guard.conferenceManage.domain.Conference;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.devicemanage.domain.Device.DeviceFlag;
import com.jinglun.guard.devicemanage.domain.DeviceStatus;
import com.jinglun.guard.devicemanage.domain.TermFace;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.employeeManage.domain.FaceInfo.Picture;
import com.jinglun.guard.guest.domain.Group;
import com.jinglun.guard.guest.domain.GroupFace;
import com.jinglun.guard.systemManage.domain.Company;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.systemManage.domain.VisitReason;
import com.jinglun.guard.privilege.domain.Menu;
import com.jinglun.guard.privilege.domain.Role;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.visitorManage.domain.BatVisitor;
import com.jinglun.guard.visitorManage.domain.FaceVisitInfo;
import com.jinglun.guard.visitorManage.domain.VisitQueryInfo;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @author Tong 数据服务接口类，提供与门禁服务端的数据通讯接口
 */

@Slf4j

public class DataService {

	private static final String strKey = "9fdbcf267a97453a9155a42d6046dfbe";
	private static final String WEB_SERVER = "web_server";
	private static final String FACE_SERVER = "face_server";
	private static final String PARAM_VALUE = "param_value";
	
	private DataService() {
	}

	/**
	 * 用户登录接口
	 * 
	 * @param user
	 * @return 0-成功；1-用户不存在；2-获取用户信息失败；3-密码错误；4-用户已禁用
	 */
	public static ResultDomain<User> UserLogin(User user) {
		String pwd = user.getUser_password();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "UserLogin");
		jsonObject.put("version", 0x102);
		jsonObject.put("user_name", user.getUser_name());
		jsonObject.put("user_password", DataUtil.EncryptPwd(pwd, strKey));
		jsonObject.put("mode", 1);		//登录方式，1-Web客户端

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if (result == 0) {
			user.setFace_library_id(jsStr.getInt("face_library_id"));
			user.setUser_id(jsStr.getInt("user_id"));
			user.setUser_function(jsStr.getInt("user_function"));
			//user.setBusiness_id(jsStr.getInt("business_id"));
			user.setPassword_status(jsStr.getInt("password_status"));
			user.setCompany_name(jsStr.getString("name"));
			
			int menu_num = jsStr.getInt("menu_num");
			if(menu_num > 0) {
				JSONArray jsonMenus = JSONArray.fromObject(jsStr.getString("menu_list"));
				user.setMenuList(JSONArray.toList(jsonMenus, new Menu(), new JsonConfig()));
			}

			int depart_num = jsStr.getInt("depart_num");
			if(depart_num > 0) {
				JSONArray jsonDeparts = JSONArray.fromObject(jsStr.getString("depart_list"));
				List<Integer> departList = new ArrayList<Integer>(depart_num);
				for(int i = 0; i < depart_num; i++) {
					departList.add(new Integer(jsonDeparts.getJSONObject(i).getInt("depart_id")));
				}
				user.setDepartList(departList);
				//user.setDepartList(JSONArray.toList(jsonDeparts, new Integer(256), new JsonConfig()));
			}

			int term_num = jsStr.getInt("term_num");
			if(term_num > 0) {
				JSONArray jsonTerms = JSONArray.fromObject(jsStr.getString("term_list"));
				List<Integer> termList = new ArrayList<Integer>(term_num);
				for(int i = 0; i < term_num; i++) {
					termList.add(new Integer(jsonTerms.getJSONObject(i).getInt("term_id")));
				}
				user.setTermList(termList);
				//user.setTermList(JSONArray.toList(jsonTerms, new Integer(256), new JsonConfig()));
			}
		}

		return new ResultDomain<>(result, user);
	}

	/**
	 * 上报用户信息
	 * 
	 * @param user
	 *            用户信息
	 * @param mode
	 *            0：基本信息,1：修改用户密码,2：密码重置
	 * @return 0-成功；1-该用户名已存在；2-密码为空
	 *         3-保存失败；101-用户不存在；102-获取用户信息失败；103-密码错误；104-用户已禁用
	 */
	public static ResultDomain<User> UserInfoUpload(User user, int mode) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "UserInfoUpload");
		jsonObject.put("version", 0x102);
		jsonObject.put("user_name", user.getUser_name());
		if (mode == 1) {
			if (StringUtils.isEmpty(user.getNew_password())) {
				log.debug("new password must not be empty");
				throw new IllegalArgumentException("new password must not be empty");
			}

			jsonObject.put("user_password", DataUtil.EncryptPwd(user.getNew_password(), strKey));
			jsonObject.put("old_user_password", DataUtil.EncryptPwd(user.getUser_password(), strKey));
		} else {
			jsonObject.put("user_password", DataUtil.EncryptPwd(user.getUser_password(), strKey));
		}
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("user_id", user.getUser_id());
		jsonObject.put("face_id", user.getFace_id());
		jsonObject.put("mode", mode);

		if (user.getRoleList() != null && !user.getRoleList().isEmpty()) {
			jsonObject.put("role_num", user.getRoleList().size());
			
			JSONArray jsonRoles = new JSONArray();
			for (int i = 0; i < user.getRoleList().size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("role_id", user.getRoleList().get(i).getRole_id());
				jsonRoles.add(i, jsonObj);
			}
			jsonObject.put("role_list", jsonRoles);
		} else {
			jsonObject.put("role_num", 0);
		}

		if (user.getDepartList() != null && !user.getDepartList().isEmpty()) {
			jsonObject.put("depart_num", user.getDepartList().size());
			
			JSONArray jsonDeparts = new JSONArray();
			for (int i = 0; i < user.getDepartList().size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("depart_id", user.getDepartList().get(i));
				jsonDeparts.add(i, jsonObj);
			}
			jsonObject.put("depart_list", jsonDeparts);
		} else {
			jsonObject.put("depart_num", 0);
		}

		if (user.getTermList() != null && !user.getTermList().isEmpty()) {
			jsonObject.put("term_num", user.getTermList().size());
			
			JSONArray jsonTerms = new JSONArray();
			for (int i = 0; i < user.getTermList().size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("term_id", user.getTermList().get(i));
				jsonTerms.add(i, jsonObj);
			}
			jsonObject.put("term_list", jsonTerms);
		} else {
			jsonObject.put("term_num", 0);
		}

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		return new ResultDomain<>(result, user);
	}
	
	//获取用户信息接口
	public static ResultDomain<List<User>> UserInfoQuery(User user) {
		List<User> users = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "UserInfoQuery");
		jsonObject.put("version", 0x102);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("user_id", user.getUser_id());
		
		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		
		if (result == 0) {
			int user_num = jsStr.getInt("rec_num");
			JSONArray arrUsers = null;
			if (user_num > 0)
				arrUsers = jsStr.getJSONArray("rec_list");
			if (arrUsers != null && user_num == arrUsers.size()) {
				users = new ArrayList<>(user_num);

				for (int i = 0; i < user_num; i++) {
					JSONObject jstrUser = arrUsers.getJSONObject(i);
					User tmpUser = new User();
					tmpUser.setUser_id(jstrUser.getInt("user_id"));
					tmpUser.setUser_name(jstrUser.getString("user_name"));
					tmpUser.setFace_id(jstrUser.getInt("face_id"));
					tmpUser.setCreate_time(jstrUser.getString("create_time"));
					tmpUser.setUpdate_time(jstrUser.getString("update_time"));
					
					int role_num = jstrUser.getInt("role_num");
					if(role_num > 0) {
						JSONArray jsonRoles = JSONArray.fromObject(jstrUser.getString("role_list"));
						/*List<Role> roles = new ArrayList<>(role_num);
						
						for(int j = 0; j < role_num; j++) {
							Role role = new Role();
							JSONObject objRole = jsonRoles.getJSONObject(j);
							role.setRole_id(objRole.getInt("role_id"));
							role.setName(objRole.getString("name"));
							roles.add(role);
						}
						tmpUser.setRoleList(roles);*/
						tmpUser.setRoleList(JSONArray.toList(jsonRoles, new Role(), new JsonConfig()));
					}

					int depart_num = jstrUser.getInt("depart_num");
					if(depart_num > 0) {
						JSONArray jsonDeparts = JSONArray.fromObject(jstrUser.getString("depart_list"));
						List<Integer> departList = new ArrayList<>(depart_num);
						for(int j = 0; j < depart_num; j++) {
							departList.add(jsonDeparts.getJSONObject(j).getInt("depart_id"));
						}
						tmpUser.setDepartList(departList);
					}

					int term_num = jstrUser.getInt("term_num");
					if(term_num > 0) {
						JSONArray jsonTerms = JSONArray.fromObject(jstrUser.getString("term_list"));
						List<Integer> termList = new ArrayList<>(term_num);
						for(int j = 0; j < term_num; j++) {
							termList.add(jsonTerms.getJSONObject(j).getInt("term_id"));
						}
						tmpUser.setTermList(termList);
					}

					users.add(tmpUser);
				}
			}
		}

		return new ResultDomain<>(result, users);
	}
	
	//配置用户部门权限接口
	public static ResultDomain UserDepartConfig(User user, List<Integer> depart_list) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "UserDepartConfig");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("user_id", user.getUser_id());
		jsonObject.put("depart_num", depart_list.size());
		JSONArray jsonFaces = new JSONArray();
		for (int i = 0; i < depart_list.size(); i++) {
			jsonFaces.add(i, depart_list.get(i));
		}
		jsonObject.put("depart_list", jsonFaces);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		return new ResultDomain(result);
	}
	
	//获取用户部门权限接口
	public static ResultDomain<List<Integer>> UserDepartQuery(User user) {
		List<Integer> departs = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "UserDepartQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("user_id", user.getUser_id());
		
		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		
		if (result == 0) {
			int rec_num = jsStr.getInt("rec_num");
			JSONArray arrUsers = null;
			if (rec_num > 0)
				arrUsers = jsStr.getJSONArray("rec_list");
			if (arrUsers != null && rec_num == arrUsers.size()) {
				departs = new ArrayList<>(rec_num);

				for (int i = 0; i < rec_num; i++) {
					JSONObject jstrDepartId = arrUsers.getJSONObject(i);
					departs.add(jstrDepartId.getInt("depart_id"));
				}
			}
		}

		return new ResultDomain<>(result, departs);
	}
	
	//配置用户设备权限接口
	public static ResultDomain UserTermConfig(User user, List<Integer> term_list) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "UserTermConfig");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("user_id", user.getUser_id());
		jsonObject.put("term_num", term_list.size());
		JSONArray jsonFaces = new JSONArray();
		for (int i = 0; i < term_list.size(); i++) {
			jsonFaces.add(i, term_list.get(i));
		}
		jsonObject.put("term_list", jsonFaces);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		return new ResultDomain(result);
	}
	
	//获取用户设备权限接口
	public static ResultDomain<List<Integer>> UserTermQuery(User user) {
		List<Integer> terms = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "UserTermQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("user_id", user.getUser_id());
		
		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		
		if (result == 0) {
			int rec_num = jsStr.getInt("rec_num");
			JSONArray arrUsers = null;
			if (rec_num > 0)
				arrUsers = jsStr.getJSONArray("rec_list");
			if (arrUsers != null && rec_num == arrUsers.size()) {
				terms = new ArrayList<>(rec_num);

				for (int i = 0; i < rec_num; i++) {
					JSONObject jstrDepartId = arrUsers.getJSONObject(i);
					terms.add(jstrDepartId.getInt("term_id"));
				}
			}
		}

		return new ResultDomain<>(result, terms);
	}

	//删除用户信息接口
	public static ResultDomain UserInfoDelete(User user) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "UserInfoDelete");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("user_id", user.getUser_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		return new ResultDomain(result);
	}

	/**
	 * 获取终端公共参数
	 * 
	 * @param user
	 *            当前登录用户
	 * @param params
	 *            参数列表
	 * @return 0-成功；
	 */
	public static ResultDomain<List<ParamConfigInfo>> CommonParamQuery(User user) {
		List<ParamConfigInfo> params = new ArrayList<>();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermCommonParamQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if (result == 0) {
			int paramNums = jsStr.getInt("param_num");
			JSONArray arrParams = null;
			if (paramNums > 0)
				arrParams = jsStr.getJSONArray("param_list");
			if (arrParams != null && paramNums == arrParams.size()) {
				for (int i = 0; i < paramNums; i++) {
					JSONObject jstrParam = JSONObject.fromObject(arrParams.getJSONObject(i));
					// params.put(jstrParam.getString("param_key"),
					// jstrParam.getString("param_value"));
					ParamConfigInfo param = new ParamConfigInfo();

					param.setParam_key(jstrParam.getString("param_key"));
					param.setParam_value(jstrParam.getString("param_value"));
					param.setParam_name(jstrParam.getString("param_name"));

					params.add(param);
				}
			}
		}

		return new ResultDomain<>(result, params);
	}

	public static ResultDomain<List<ParamConfigInfo>> CommonParamConfig(User user, List<ParamConfigInfo> params) {
		if (user.getFace_library_id() <= 0 || params.isEmpty()) {
			throw new DataServiceException("Invoke CommonParamConfig Failed : Param is error!");
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermCommonParamConfig");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("param_num", params.size());

		JSONArray jsonParams = new JSONArray();
		for (int i = 0; i < params.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			ParamConfigInfo param = params.get(i);
			jsonObj.put("param_key", param.getParam_key());
			jsonObj.put("param_value", param.getParam_value());
			jsonObj.put("param_name", param.getParam_name());
			jsonParams.add(i, jsonObj);
		}

		jsonObject.put("param_list", jsonParams);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");

		return new ResultDomain<>(result, params);
	}

	/**
	 * @param user
	 *            当前用户
	 * @param parent_depart_id
	 *            父部门ID，0-无父部门；-1-所有部门信息
	 * @param depart_id
	 *            部门ID，为0返回全部
	 * @return 部门列表
	 */
	public static ResultDomain<List<Depart>> DepartInfoQuery(User user, int parent_depart_id, int depart_id) {
		log.debug("Invoke DepartInfoQuery Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "DepartInfoQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("parent_depart_id", parent_depart_id);
		jsonObject.put("depart_id", depart_id);
		jsonObject.put("del_flag", 0);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		List<Depart> departs = new ArrayList<>();
		int result = jsStr.getInt("result");
		if (result == 0) {
			int depart_num = jsStr.getInt("depart_num");
			JSONArray arrDeparts = null;
			if (depart_num > 0)
				arrDeparts = jsStr.getJSONArray("depart_list");
			if (arrDeparts != null && depart_num == arrDeparts.size()) {
				departs = new ArrayList<>(depart_num);

				for (int i = 0; i < depart_num; i++) {
					JSONObject jstrDepart = JSONObject.fromObject(arrDeparts.getJSONObject(i));
					Depart depart = new Depart();
					depart.setFace_library_id(jsStr.getInt("face_library_id"));
					depart.setParent_depart_id(jstrDepart.getInt("parent_depart_id"));
					depart.setDepart_id(jstrDepart.getInt("depart_id"));
					depart.setDepart_name(jstrDepart.getString("depart_name"));
					depart.setDel_flag(jstrDepart.getInt("del_flag"));

					departs.add(depart);
				}
			}
		}
		return new ResultDomain<>(result, departs);
	}

	/**
	 * @param user
	 *            当前用户
	 * @param parent_depart_id
	 *            父部门ID，0-无父部门；-1-所有部门信息
	 * @param depart_id
	 *            部门ID，为0返回全部
	 * @return 部门列表
	 */
	public static ResultDomain<List<Depart>> DepartInfoQueryByAuth(User user, int parent_depart_id, int depart_id) {
		log.debug("Invoke DepartInfoQuery Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "DepartInfoQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("parent_depart_id", parent_depart_id);
		jsonObject.put("depart_id", depart_id);
		jsonObject.put("del_flag", 0);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		List<Depart> departs = new ArrayList<>();
		int result = jsStr.getInt("result");
		if (result == 0) {
			int depart_num = jsStr.getInt("depart_num");
			JSONArray arrDeparts = null;
			if (depart_num > 0)
				arrDeparts = jsStr.getJSONArray("depart_list");
			if (arrDeparts != null && depart_num == arrDeparts.size()) {
				departs = new ArrayList<>(depart_num);

				for (int i = 0; i < depart_num; i++) {
					JSONObject jstrDepart = JSONObject.fromObject(arrDeparts.getJSONObject(i));
					Depart depart = new Depart();
					depart.setFace_library_id(jsStr.getInt("face_library_id"));
					depart.setParent_depart_id(jstrDepart.getInt("parent_depart_id"));
					depart.setDepart_id(jstrDepart.getInt("depart_id"));
					depart.setDepart_name(jstrDepart.getString("depart_name"));
					depart.setDel_flag(jstrDepart.getInt("del_flag"));

					departs.add(depart);
				}
			}
		}
		//通过用户departlist筛选出当前用户拥有权限的部门
		List<Integer> authlist = user.getDepartList();
		List<Depart> departauthlist = null;
		if(null != departs){
			departauthlist = departs.stream().filter((Depart d) -> authlist.contains(d.getDepart_id())).collect(Collectors.toList());
			//过滤只有子部门，没有父部门的情况，将其父部门添加进来
			for (int i = 0;i < departauthlist.size();i++) {
				if (!departauthlist.contains(departauthlist.get(i).getParent_depart_id())){
					for (Depart parentdepart:departs
						 ) {
						if(parentdepart.getDepart_id()==departauthlist.get(i).getParent_depart_id()){
							if (!departauthlist.contains(parentdepart))
							departauthlist.add(parentdepart);
						}
					}
				}
			}
			List<Depart> departListByDel = new ArrayList<>();
			for (Depart departByDel:departauthlist
			) {
				if (departByDel.getDel_flag()!=1){
					departListByDel.add(departByDel);
				}
			}
			return new ResultDomain<>(result, departListByDel);
		}else{
			return new ResultDomain<>(result, null);
		}


		//return new ResultDomain<>(result, departs);
	}
	
	/**
	 * @param user
	 *            当前用户
	 * @param depart
	 *            部门信息，需传部门名称，如有父部门则需传父部门id：parent_depart_id
	 * @return 返回部门id和修订号
	 */
	public static ResultDomain<Depart> DepartInfoUpload(User user, Depart depart) {
		log.debug("Invoke DepartInfoUpload Method!");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "DepartInfoUpload");
		jsonObject.put("version", 0x101);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("parent_depart_id", depart.getParent_depart_id());
		jsonObject.put("depart_id", depart.getDepart_id());
		jsonObject.put("depart_name", depart.getDepart_name());
		jsonObject.put("user_id", user.getUser_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if (result == 0) {
			depart.setDepart_id(jsStr.getInt("depart_id"));
			depart.setRevno(jsStr.getString("revno"));
		}

		return new ResultDomain<>(result, depart);
	}

	/**
	 * @param user
	 * @param depart
	 * @return
	 */
	public static ResultDomain<Depart> DepartInfoDelete(User user, Depart depart) {
		log.debug("Invoke DepartInfoDelete Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "DepartInfoDelete");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("user_id", user.getUser_id());
		jsonObject.put("parent_depart_id", depart.getParent_depart_id());
		jsonObject.put("depart_id", depart.getDepart_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if (result == 0) {
			depart.setRevno(jsStr.getString("revno"));
		}

		return new ResultDomain<>(result, depart);
	}

	/**
	 * 人员信息获取
	 * 
	 * @param user
	 *            当前用户
	 * @param attribute
	 *            属性，0-员工；1-访客
	 * @param depart_id
	 *            部门ID，为0返回全部
	 * @param start
	 *            起始索引号，从0开始
	 * @param face_num
	 *            查询记录总数，-1返回全部
	 * @param revno
	 *            当前修订号，为空串("")返回全部
	 * @return result-人员总数，list-当次返回的人员信息列表
	 */
	public static ResultDomain<List<FaceInfo>> FaceInfoSearch(User user, int attribute, int depart_id, int start,
			int face_num, String revno) {
		log.debug("Invoke FaceInfoSearch Method!");
		if (face_num > 200)
			face_num = 200;

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceInfoSearch");
		jsonObject.put("version", 0x105);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("depart_id", depart_id);
		jsonObject.put("attribute", attribute);
		jsonObject.put("business", 0);
		jsonObject.put("term_id", 0);
		jsonObject.put("mode", 1);
		jsonObject.put("revno", revno);
		jsonObject.put("start", start);
		jsonObject.put("face_num", face_num);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");
		List<FaceInfo> faces = null;
		if (result == 0) {
			int total_num = jsStr.getInt("total_num");
			int ret_face_num = jsStr.getInt("face_num");
			JSONArray arrFaces = null;
			if (ret_face_num > 0) {
				arrFaces = jsStr.getJSONArray("face_list");
			}

			if (arrFaces != null && ret_face_num == arrFaces.size()) {
				faces = new ArrayList<>(ret_face_num);
				for (int i = 0; i < ret_face_num; i++) {
					JSONObject jstrFace = JSONObject.fromObject(arrFaces.getJSONObject(i));
					FaceInfo face = new FaceInfo();

					face.setFace_id(jstrFace.getInt("face_id"));
					face.setDepart_id(jstrFace.getInt("depart_id"));
					face.setSex(jstrFace.getInt("gender"));
					// face.setType(jstrFace.getInt("type"));
					face.setStatus(jstrFace.getInt("status"));
					face.setLeader_id(jstrFace.getInt("leader_id"));

					// face.setCompany(jstrFace.getString("company"));
					face.setName(jstrFace.getString("name"));
					face.setNick_name(jstrFace.getString("nick_name"));
					face.setIdcard(DataUtil.HexStr2Id(jstrFace.getString("id")));
					face.setIdcard_cid(jstrFace.getString("idcard_no"));
					face.setPhoto_wlt(DataUtil.decodeWlt(jstrFace.getString("idwlt")));
					face.setEmpcard(jstrFace.getString("card_no"));
					face.setTel_no(jstrFace.getString("tel_no"));
					face.setEmployee_id(jstrFace.getString("employee_id"));
					face.setPosition(jstrFace.getString("position"));
					face.setNation(jstrFace.getString("nation"));
					face.setAddr(jstrFace.getString("addr"));
					face.setRevno(jstrFace.getString("revno"));
					faces.add(face);
				}
			}

			result = total_num;
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, faces);
	}

	/**
	 * 人员信息查询
	 * 
	 * @param user
	 * @param query_face
	 *            可传入face_id,depart_id,tel_no,attribute,status
	 * @param query_info
	 *            模糊查询内容
	 * @return
	 */
	public static ResultDomain<List<FaceInfo>> FaceInfoQuery(User user, FaceInfo query_face, String query_info,
			int start, int face_num) {
		log.debug("Invoke FaceInfoQuery Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceInfoQuery");
		jsonObject.put("version", 0x105);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("face_id", query_face.getFace_id());
		jsonObject.put("depart_id", query_face.getDepart_id());
		jsonObject.put("id", DataUtil.Id2HexStr(query_face.getIdcard()));
		jsonObject.put("tel_no", query_face.getTel_no());
		jsonObject.put("attribute", query_face.getAttribute());
		jsonObject.put("term_id", 0);
		jsonObject.put("picture", query_face.getPic_num());
		jsonObject.put("status", query_face.getStatus());
		jsonObject.put("query_info", query_info);
		jsonObject.put("start", start);
		jsonObject.put("face_num", face_num);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");
		List<FaceInfo> faces = null;
		if (result == 0) {
			int total_num = jsStr.getInt("total_num");
			int ret_face_num = jsStr.getInt("face_num");
			JSONArray arrFaces = null;
			if (ret_face_num > 0) {
				arrFaces = jsStr.getJSONArray("face_list");
			}

			if (arrFaces != null && ret_face_num == arrFaces.size()) {
				faces = new ArrayList<>(ret_face_num);
				for (int i = 0; i < ret_face_num; i++) {
					JSONObject jstrFace = JSONObject.fromObject(arrFaces.getJSONObject(i));
					FaceInfo face = new FaceInfo();

					face.setFace_id(jstrFace.getInt("face_id"));
					if(query_face.getAttribute() == 0) {
						face.setDepart_id(jstrFace.getInt("depart_id"));
						face.setLeader_id(jstrFace.getInt("leader_id"));		
						face.setEmployee_id(jstrFace.getString("employee_id"));
					} else {
						face.setCompany(jstrFace.getString("company"));
					}
					face.setPhoto_wlt(DataUtil.decodeWlt(jstrFace.getString("idwlt")));
					face.setStatus(jstrFace.getInt("status"));
					face.setPosition(jstrFace.getString("position"));
					face.setAddr(jstrFace.getString("addr"));
					face.setSex(jstrFace.getInt("gender"));
					face.setType(jstrFace.getInt("type"));
					face.setPic_num(jstrFace.getInt("picture_num"));
					face.setName(jstrFace.getString("name"));
					face.setNick_name(jstrFace.getString("nick_name"));
					face.setIdcard(DataUtil.HexStr2Id(jstrFace.getString("id")));
					face.setIdcard_cid(jstrFace.getString("idcard_no"));
					face.setIdcard_expire(jstrFace.getString("valid_date"));
					face.setAgency(jstrFace.getString("agency"));
					face.setEmpcard(jstrFace.getString("card_no"));
					face.setTel_no(jstrFace.getString("tel_no"));
					face.setNation(jstrFace.getString("nation"));
					face.setRevno(jstrFace.getString("revno"));
					faces.add(face);
				}
			}

			result = total_num;
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, faces);
	}
	
	public static ResultDomain<List<FaceInfo>> FaceInfoQuery(User user, FaceInfo query_face, String query_info,
			int start, int face_num, List<Integer> depart_list) {
		log.debug("Invoke FaceInfoQuery Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceInfoQuery");
		jsonObject.put("version", 0x105);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("face_id", query_face.getFace_id());
		jsonObject.put("depart_id", query_face.getDepart_id());
		jsonObject.put("id", DataUtil.Id2HexStr(query_face.getIdcard()));
		jsonObject.put("tel_no", query_face.getTel_no());
		jsonObject.put("attribute", query_face.getAttribute());
		jsonObject.put("term_id", 0);
		jsonObject.put("picture", query_face.getPic_num());
		jsonObject.put("status", query_face.getStatus());
		jsonObject.put("query_info", query_info);
		jsonObject.put("start", start);
		jsonObject.put("face_num", face_num);
		
		if(depart_list != null && !depart_list.isEmpty()) {
			JSONArray jsonDeparts = new JSONArray();
			for (int i = 0; i < depart_list.size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("depart_id", depart_list.get(i));
				jsonDeparts.add(i, jsonObj);
			}
	
			jsonObject.put("depart_list", jsonDeparts);
		}

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");
		List<FaceInfo> faces = null;
		if (result == 0) {
			int total_num = jsStr.getInt("total_num");
			int ret_face_num = jsStr.getInt("face_num");
			JSONArray arrFaces = null;
			if (ret_face_num > 0) {
				arrFaces = jsStr.getJSONArray("face_list");
			}

			if (arrFaces != null && ret_face_num == arrFaces.size()) {
				faces = new ArrayList<>(ret_face_num);
				for (int i = 0; i < ret_face_num; i++) {
					JSONObject jstrFace = JSONObject.fromObject(arrFaces.getJSONObject(i));
					FaceInfo face = new FaceInfo();

					face.setFace_id(jstrFace.getInt("face_id"));
					if(query_face.getAttribute() == 0) {
						face.setDepart_id(jstrFace.getInt("depart_id"));
						face.setLeader_id(jstrFace.getInt("leader_id"));		
						face.setEmployee_id(jstrFace.getString("employee_id"));
					} else {
						face.setCompany(jstrFace.getString("company"));
					}
					face.setPhoto_wlt(DataUtil.decodeWlt(jstrFace.getString("idwlt")));
					face.setStatus(jstrFace.getInt("status"));
					face.setPosition(jstrFace.getString("position"));
					face.setAddr(jstrFace.getString("addr"));
					face.setSex(jstrFace.getInt("gender"));
					face.setType(jstrFace.getInt("type"));
					face.setPic_num(jstrFace.getInt("picture_num"));
					face.setName(jstrFace.getString("name"));
					face.setNick_name(jstrFace.getString("nick_name"));
					face.setIdcard(DataUtil.HexStr2Id(jstrFace.getString("id")));
					face.setIdcard_cid(jstrFace.getString("idcard_no"));
					face.setIdcard_expire(jstrFace.getString("valid_date"));
					face.setAgency(jstrFace.getString("agency"));
					face.setEmpcard(jstrFace.getString("card_no"));
					face.setTel_no(jstrFace.getString("tel_no"));
					face.setNation(jstrFace.getString("nation"));
					face.setRevno(jstrFace.getString("revno"));
					faces.add(face);
				}
			}

			result = total_num;
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, faces);
	}

	/**
	 * 获取所有人员的face_id和姓名
	 * 
	 * @param user
	 *            当前用户
	 * @param query_face
	 *            传入attribute和status属性
	 * @return 人员数量及列表
	 */
	public static ResultDomain<List<FaceInfo>> FaceListGet(User user, FaceInfo query_face, List<Integer> depart_list) {
		log.debug("Invoke FaceListGet Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceListGet");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("attribute", query_face.getAttribute());
		jsonObject.put("depart_id", query_face.getDepart_id());
		jsonObject.put("status", query_face.getStatus());
		jsonObject.put("face_num", -1);
		
		if(depart_list != null && !depart_list.isEmpty()) {
			JSONArray jsonDeparts = new JSONArray();
			for (int i = 0; i < depart_list.size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("depart_id", depart_list.get(i));
				jsonDeparts.add(i, jsonObj);
			}
	
			jsonObject.put("depart_list", jsonDeparts);
		}

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");
		List<FaceInfo> faces = null;
		if (result == 0) {
			int ret_face_num = jsStr.getInt("face_num");
			JSONArray arrFaces = null;
			if (ret_face_num > 0) {
				arrFaces = jsStr.getJSONArray("face_list");
			}

			if (arrFaces != null && ret_face_num == arrFaces.size()) {
				faces = new ArrayList<>(ret_face_num);
				for (int i = 0; i < ret_face_num; i++) {
					JSONObject jstrFace = JSONObject.fromObject(arrFaces.getJSONObject(i));
					FaceInfo face = new FaceInfo();

					face.setFace_id(jstrFace.getInt("face_id"));
					face.setDepart_id(jstrFace.getInt("depart_id"));
					face.setName(jstrFace.getString("name"));
					face.setStatus(jstrFace.getInt("status"));
					face.setTel_no(jstrFace.getString("tel_no"));
					faces.add(face);
				}
			}

			result = ret_face_num;
		}

		return new ResultDomain<>(result, faces);
	}

	/**
	 * 人员照片查询
	 * 
	 * @param user
	 * @param query_face
	 *            需传入face_id,attribute
	 * @return
	 */
	public static ResultDomain<List<Picture>> FacePhotoQuery(User user, FaceInfo query_face) {
		log.debug("Invoke FacePhotoQuery Method!");
		List<Picture> photos = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FacePhotoQuery");
		jsonObject.put("version", 0x101);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("image_num", 0);
		jsonObject.put("face_num", 1);
		jsonObject.put("attribute", query_face.getAttribute());
		jsonObject.put("image_type", -1);

		JSONArray jsonFace = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("face_id", query_face.getFace_id());
		jsonObj.put("image_url", "");
		jsonFace.add(0, jsonObj);
		jsonObject.put("face_list", jsonFace);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		if (jsStr == null)
			return null;
		int result = jsStr.getInt("result");
		if (result == 0) {
			JSONArray face_list = jsStr.getJSONArray("face_list");
			if(face_list != null && !face_list.isEmpty()) {
				JSONObject jObject = face_list.getJSONObject(0);
				int image_num = jObject.getInt("image_num");
				JSONArray arrPhotos = null;

				if (image_num > 0) {
					arrPhotos = jObject.getJSONArray("image_list");
				}

				if (arrPhotos != null && image_num == arrPhotos.size()) {
					photos = new ArrayList<>(image_num);
					for (int i = 0; i < image_num; i++) {
						JSONObject jstrPhoto = JSONObject.fromObject(arrPhotos.getJSONObject(i));
						Picture picture = new FaceInfo.Picture();
						picture.setPic_id(jstrPhoto.getInt("pic_id"));
						picture.setImage_type(jstrPhoto.getInt("image_type"));
						picture.setWidth(jstrPhoto.getInt("width"));
						picture.setHeight(jstrPhoto.getInt("height"));
						picture.setData(jstrPhoto.getString("image_data"));
						photos.add(picture);
					}
				}
			}
		}

		return new ResultDomain<>(result, photos);
	}
	
	

	/**
	 * @param user
	 *            当前用户
	 * @param face
	 *            人员信息
	 * @return 上报后的人员信息，如上报成功会更新face_id和revno
	 */
	public static ResultDomain<FaceInfo> FaceInfoUpload(User user, FaceInfo face) {
		log.debug("Invoke FaceInfoUpload Method!");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceInfoUpload");
		jsonObject.put("version", 0x106);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("face_id", face.getFace_id());
		jsonObject.put("depart_id", face.getDepart_id());
		jsonObject.put("attribute", face.getAttribute());
		jsonObject.put("leader_id", face.getLeader_id());
		jsonObject.put("company", face.getCompany());
		jsonObject.put("name", face.getName());
		jsonObject.put("nick_name", face.getNick_name());
		jsonObject.put("id", DataUtil.Id2HexStr(face.getIdcard()));
		jsonObject.put("idcard_no", face.getIdcard_cid());
		jsonObject.put("card_no", face.getEmpcard());
		jsonObject.put("tel_no", face.getTel_no());
		jsonObject.put("gender", face.getSex());
		jsonObject.put("type", face.getType());
		jsonObject.put("employee_id", face.getEmployee_id());
		jsonObject.put("position", face.getPosition());
		jsonObject.put("nation", face.getNation());
		jsonObject.put("valid_date", face.getIdcard_expire());
		jsonObject.put("addr", face.getAddr());
		// 身份证照片base64编码
		jsonObject.put("idwlt", face.getPhoto_wlt());
		ResultDomain<FDResult> resultDomain = null;
		if (!StringUtils.isEmpty(face.getFace_info_base64()))
			resultDomain = FaceImageToBGR(face.getFace_info_base64());
		if (null != resultDomain && resultDomain.getResultCode() == 0) { // 人脸数量大于0
			JSONObject jsonImg = new JSONObject();
			jsonImg.put("width", resultDomain.getResultData().getFace_width());
			jsonImg.put("height", resultDomain.getResultData().getFace_height());
			jsonImg.put("data", resultDomain.getResultData().getFace_data());
			jsonObject.put("image", jsonImg);
		}
		
		//比对照片
		if(face.getPictures() != null && !face.getPictures().isEmpty()) {
			jsonObject.put("picture_num", face.getPictures().size());
			JSONArray jsonPictures = new JSONArray();
			for (int i = 0; i < face.getPictures().size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("pic_id", face.getPictures().get(i).getPic_id());
				jsonObj.put("option", face.getPictures().get(i).getOption());
				jsonObj.put("width", face.getPictures().get(i).getWidth());
				jsonObj.put("height", face.getPictures().get(i).getHeight());
				jsonObj.put("data", face.getPictures().get(i).getData());
				jsonObj.put("feature", face.getPictures().get(i).getFeature());
				jsonPictures.add(i, jsonObj);
			}
			jsonObject.put("picture_list", jsonPictures);
			jsonObject.put("feature_ver", face.getPictures().get(0).getFeature_ver());
		} else {
			jsonObject.put("picture_num", 0);
		}

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");
		if (result == 0) {
			face.setFace_id(jsStr.getInt("face_id"));
			face.setRevno(jsStr.getString("revno"));
		}

		return new ResultDomain<>(result, face);
	}

	/**
	 * 注销人员信息
	 * 
	 * @param user
	 * @param face
	 * @return
	 */
	public static ResultDomain FaceInfoDelete(User user, FaceInfo face) {
		log.debug("Invoke FaceInfoDelete Method!");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceInfoDelete");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("face_id", face.getFace_id());
		jsonObject.put("attribute", face.getAttribute());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");
		if (result == 0) {
			face.setRevno(jsStr.getString("revno"));
		}

		return new ResultDomain<>(result);
	}

	/**
	 * 配置人员权限
	 * 
	 * @param user
	 * @param face
	 * @param termList
	 * @param visit_id
	 * @return
	 */
	public static ResultDomain FacePermitConfig(User user, FaceInfo face, List<Integer> termList, int visit_id) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FacePermitConfig");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("face_id", face.getFace_id());
		jsonObject.put("attribute", face.getAttribute());
		jsonObject.put("expire_begin", face.getVisit_time());
		jsonObject.put("expire_end", face.getExpire_time());
		jsonObject.put("term_num", termList.size());
		jsonObject.put("visit_id", visit_id);
		if (!termList.isEmpty()) {
			JSONArray jsonTerms = new JSONArray();
			for (int i = 0; i < termList.size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("term_id", termList.get(i));
				jsonTerms.add(i, jsonObj);
			}
	
			jsonObject.put("term_list", jsonTerms);
		}

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = 0;
		if (null != jsStr) {
			result = jsStr.getInt("result");
		}
		return new ResultDomain<>(result);
	}

	/**
	 * 获取人员权限
	 * 
	 * @param user
	 * @param query 需传入face_id,attribute,visit_id
	 * @return
	 */
	public static ResultDomain<List<TermFace>> FacePermitQuery(User user, TermFace query) {
		log.debug("Invoke FacePermitQuery Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FacePermitQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("face_id", query.getFace_id());
		jsonObject.put("attribute", query.getAttribute());
		jsonObject.put("visit_id", query.getVisit_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");
		List<TermFace> termfaceList = null;
		if (result == 0) {
			int term_num = jsStr.getInt("term_num");
			JSONArray arrTerms = null;
			if (term_num > 0)
				arrTerms = jsStr.getJSONArray("term_list");
			if (arrTerms != null && term_num == arrTerms.size()) {
				termfaceList = new ArrayList<>(term_num);
				for (int i = 0; i < term_num; i++) {
					JSONObject jstrDevice = JSONObject.fromObject(arrTerms.getJSONObject(i));
					TermFace termFace = new TermFace();

					termFace.setTerm_id(jstrDevice.getInt("term_id"));
					termFace.setTerm_model(jstrDevice.getInt("term_model"));
					termFace.setExpire_begin(jstrDevice.getString("expire_begin"));
					termFace.setExpire_end(jstrDevice.getString("expire_end"));

					termfaceList.add(termFace);
				}
			}
		}

		return new ResultDomain<>(result, termfaceList);
	}

	/**
	 * 查询刷脸日志
	 * 
	 * @param user
	 *            当前用户
	 * @param info
	 *            传入attribute,face_id,start_time,end_time
	 * @param start
	 *            起始索引号
	 * @param num
	 *            返回的记录总数
	 * @return 记录总数及列表
	 */
	public static ResultDomain<List<FaceRecordInfo>> FaceRecordQuery(User user, FaceVisitInfo info, int start,
			int num) {
		log.debug("Invoke FaceRecordQuery Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceRecordQuery");
		jsonObject.put("version", 0x102);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("face_id", info.getFace_id());
		jsonObject.put("attribute", info.getAttribute());
		jsonObject.put("rec_id", info.getRec_id());
		jsonObject.put("rec_type", 0);
		jsonObject.put("name", info.getName());
		jsonObject.put("term_id", info.getTerm_id());
		jsonObject.put("start_time", info.getVisit_time());
		jsonObject.put("end_time", info.getExpire_time());
		jsonObject.put("start", start);
		jsonObject.put("rec_num", num);
		jsonObject.put("business", 0);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");
		List<FaceRecordInfo> recList = null;
		if (result == 0) {
			int total_num = jsStr.getInt("total_num");
			int rec_num = jsStr.getInt("rec_num");
			JSONArray arrRecs = null;
			if (rec_num > 0)
				arrRecs = jsStr.getJSONArray("rec_list");
			if (arrRecs != null && rec_num == arrRecs.size()) {
				recList = new ArrayList<>(rec_num);
				for (int i = 0; i < rec_num; i++) {
					JSONObject jstrDevice = JSONObject.fromObject(arrRecs.getJSONObject(i));
					FaceRecordInfo record = new FaceRecordInfo();

					record.setRec_id(jstrDevice.getInt("rec_id"));
					record.setFace_id(jstrDevice.getInt("face_id"));
					record.setAttribute(jstrDevice.getInt("attribute"));
					record.setTerm_id(jstrDevice.getInt("term_id"));
					record.setCompare_result(jstrDevice.getInt("compare_result"));
					record.setCompare_score(jstrDevice.getDouble("compare_score"));
					record.setName(jstrDevice.getString("name"));
					record.setTerm_position(jstrDevice.getString("term_position"));
					record.setIdcard(DataUtil.HexStr2Id(jstrDevice.getString("idcard")));
					record.setPreview_photo_path(jstrDevice.getString("preview_photo_path"));
					record.setTime(jstrDevice.getString("time"));

					recList.add(record);
				}
			}

			result = total_num;
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, recList);
	}

	/**
	 * 查询终端基本信息
	 * 
	 * @param user
	 *            当前用户
	 * @param device
	 *            终端信息，term_id为0返回全部;term_type:
	 *            >0，按型号查询;del_flag:-1：全部,0：正常（启用）,1：删除,2：停用,3：未删除
	 * @param query_info
	 *            查询关键字，可为终端名称
	 * @return 终端列表
	 */
	public static ResultDomain<List<Device>> TermInfoQuery(User user, Device device, String query_info) {
		log.debug("Invoke TermInfoQuery Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermInfoQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", device.getTerm_id());
		jsonObject.put("term_model", device.getTerm_type());
		jsonObject.put("status", device.getDel_flag().getValue());
		jsonObject.put("query_info", query_info);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");
		List<Device> devices = null;
		if (result == 0) {
			int term_num = jsStr.getInt("term_num");
			JSONArray arrDevices = null;
			if (term_num > 0)
				arrDevices = jsStr.getJSONArray("term_list");
			if (arrDevices != null && term_num == arrDevices.size()) {
				devices = new ArrayList<>(term_num);
				for (int i = 0; i < term_num; i++) {
					JSONObject jstrDevice = JSONObject.fromObject(arrDevices.getJSONObject(i));
					Device tmpDevice = new Device();

					tmpDevice.setTerm_id(jstrDevice.getInt("term_id"));
					tmpDevice.setPosition_id(jstrDevice.getInt("position_id"));
					tmpDevice.setDepart_id(Arrays.asList(jstrDevice.getInt("depart_id")));
					tmpDevice.setTerm_type(jstrDevice.getInt("term_type"));
					tmpDevice.setBusiness_type(jstrDevice.getInt("term_business"));
					tmpDevice.setTerm_face_num(jstrDevice.getInt("term_face_num"));

					tmpDevice.setTerm_ip(jstrDevice.getString("term_ip"));
					tmpDevice.setSoft_ver(jstrDevice.getString("soft_ver"));
					tmpDevice.setTerm_sn(jstrDevice.getString("term_sn"));
					tmpDevice.setTerm_name(jstrDevice.getString("term_name"));
					tmpDevice.setTerm_code(jstrDevice.getString("term_code"));
					tmpDevice.setTerm_position(jstrDevice.getString("term_position"));
					tmpDevice.setTerm_model(jstrDevice.getInt("term_model"));
					tmpDevice.setDevice_type(jstrDevice.getString("model_name"));
					tmpDevice.setAuth_code(jstrDevice.getString("auth_code"));
					tmpDevice.setTerm_licence(jstrDevice.getString("term_licence"));
					tmpDevice.setDel_flag(DeviceFlag.getFlag(jstrDevice.getInt("del_flag")));
					int depart_num = jstrDevice.getInt("depart_num");
					if(depart_num > 0) {
						JSONArray arrDeparts = jstrDevice.getJSONArray("depart_list");
						StringBuilder sBuffer = new StringBuilder();
						List<Integer> departList = new ArrayList<>(depart_num);
						for(int j = 0; j < arrDeparts.size(); j++) {
							JSONObject jobjDepart = arrDeparts.getJSONObject(j);
							sBuffer.append(jobjDepart.getString("depart_name") + ",");
							departList.add(jobjDepart.getInt("depart_id"));
						}
						tmpDevice.setDepart_name(sBuffer.substring(0, sBuffer.length() - 1));
						tmpDevice.setDepart_id(departList);
					}

					devices.add(tmpDevice);
				}
			}
		}

		//通过用户termlist筛选出当前用户拥有权限的设备
		List<Integer> authlist = user.getTermList();
		List<Device> devicelist = null;
		if(null != devices){
			devicelist = devices.stream().filter((Device d) -> authlist.contains(d.getTerm_id())).collect(Collectors.toList());
			return new ResultDomain<>(result, devicelist);
		}else{
			return new ResultDomain<>(result, null);
		}
		
//		return new ResultDomain<>(result, devices);
	}
	
	
	public static ResultDomain<List<Device>> TermInfoQuerys(User user, Device device, String query_info) {
		log.debug("Invoke TermInfoQuery Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermInfoQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", device.getTerm_id());
		jsonObject.put("term_model", device.getTerm_type());
		jsonObject.put("status", device.getDel_flag().getValue());
		jsonObject.put("query_info", query_info);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");
		List<Device> devices = null;
		if (result == 0) {
			int term_num = jsStr.getInt("term_num");
			JSONArray arrDevices = null;
			if (term_num > 0)
				arrDevices = jsStr.getJSONArray("term_list");
			if (arrDevices != null && term_num == arrDevices.size()) {
				devices = new ArrayList<>(term_num);
				for (int i = 0; i < term_num; i++) {
					JSONObject jstrDevice = JSONObject.fromObject(arrDevices.getJSONObject(i));
					Device tmpDevice = new Device();

					tmpDevice.setTerm_id(jstrDevice.getInt("term_id"));
					tmpDevice.setPosition_id(jstrDevice.getInt("position_id"));
					tmpDevice.setDepart_id(Arrays.asList(jstrDevice.getInt("depart_id")));
					tmpDevice.setTerm_type(jstrDevice.getInt("term_type"));
					tmpDevice.setBusiness_type(jstrDevice.getInt("term_business"));
					tmpDevice.setTerm_face_num(jstrDevice.getInt("term_face_num"));

					tmpDevice.setTerm_ip(jstrDevice.getString("term_ip"));
					tmpDevice.setSoft_ver(jstrDevice.getString("soft_ver"));
					tmpDevice.setTerm_sn(jstrDevice.getString("term_sn"));
					tmpDevice.setTerm_name(jstrDevice.getString("term_name"));
					tmpDevice.setTerm_code(jstrDevice.getString("term_code"));
					tmpDevice.setTerm_position(jstrDevice.getString("term_position"));
					tmpDevice.setTerm_model(jstrDevice.getInt("term_model"));
					tmpDevice.setDevice_type(jstrDevice.getString("model_name"));
					tmpDevice.setAuth_code(jstrDevice.getString("auth_code"));
					tmpDevice.setTerm_licence(jstrDevice.getString("term_licence"));
					tmpDevice.setDel_flag(DeviceFlag.getFlag(jstrDevice.getInt("del_flag")));
					int depart_num = jstrDevice.getInt("depart_num");
					if(depart_num > 0) {
						JSONArray arrDeparts = jstrDevice.getJSONArray("depart_list");
						StringBuilder sBuffer = new StringBuilder();
						List<Integer> departList = new ArrayList<>(depart_num);
						for(int j = 0; j < arrDeparts.size(); j++) {
							JSONObject jobjDepart = arrDeparts.getJSONObject(j);
							sBuffer.append(jobjDepart.getString("depart_name") + ",");
							departList.add(jobjDepart.getInt("depart_id"));
						}
						tmpDevice.setDepart_name(sBuffer.substring(0, sBuffer.length() - 1));
						tmpDevice.setDepart_id(departList);
					}

					devices.add(tmpDevice);
				}
			}
		}
		return new ResultDomain<>(result, devices);
	}

	/**
	 * 配置终端个性化参数
	 * 
	 * @param user
	 *            当前用户
	 * @param term_id
	 *            终端id
	 * @param params
	 *            参数列表
	 * @return 0-成功
	 */
	public static ResultDomain<List<ParamConfigInfo>> TermCustomParamConfig(User user, int term_id, List<ParamConfigInfo> params) {
		if (user.getFace_library_id() <= 0 || params.isEmpty()) {
			throw new DataServiceException("Invoke TermCustomParamConfig Failed : Param is error!");
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermCustomParamConfig");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", term_id);
		jsonObject.put("param_num", params.size());

		JSONArray jsonParams = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		for (int i = 0; i < params.size(); i++) {
			
			ParamConfigInfo param = params.get(i);
			if(!"".equals(param.getParam_value())) {
				if(isJson(param.getParam_value())) 
				{
					jsonObj.put("param_value", JSON.toJSONString(param.getParam_value()));
				}
				else {
					String strTmp = param.getParam_value();
					jsonObj.put("param_value", strTmp);
				}
			}
			jsonObj.put("param_key", param.getParam_key());
			jsonObj.put("param_name", param.getParam_name());
			
			jsonParams.add(jsonObj);
		}

		jsonObject.put("param_list", jsonParams);
		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");

		return new ResultDomain<>(result, params);
	}
	
	private static boolean isJson(String content) {
        try {
            JSONObject.fromObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	/**
	 * 终端个性化参数查询
	 * 
	 * @param user
	 *            当前用户
	 * @param term_id
	 *            终端id,为0表示查询全部
	 * @return 参数列表
	 */
	public static ResultDomain<List<ParamConfigInfo>> TermCustomParamQuery(User user, int term_id) {
		List<ParamConfigInfo> params = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermCustomParamQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", term_id);
		jsonObject.put("mode", 1);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int paramNums = jsStr.getInt("param_num");
			JSONArray arrParams = null;
			if (paramNums > 0)
				arrParams = jsStr.getJSONArray("param_list");
			if (arrParams != null && paramNums == arrParams.size()) {
				params = new ArrayList<>(paramNums);
				for (int i = 0; i < paramNums; i++) {
					JSONObject jstrParam = arrParams.getJSONObject(i);
					ParamConfigInfo param = new ParamConfigInfo();

					param.setParam_key(jstrParam.getString("param_key"));
					param.setParam_value(jstrParam.getString("param_value"));
					param.setParam_name(jstrParam.getString("param_name"));
					param.setParam_sheet(jstrParam.getString("param_sheet"));
					param.setType(jstrParam.getString("type"));
					param.setDisplay_controlled("Y".equals(jstrParam.getString("display_controlled")));
					if(jstrParam.containsKey("set")) {
						JSONArray arrSets = jstrParam.getJSONArray("set");
						if(arrSets != null) {
							if(arrSets.getJSONObject(0).containsKey("key")) {
								List<SetValue> svList = new ArrayList<>(arrSets.size());
								for(int j = 0; j < arrSets.size(); j++) {
									SetValue sv = new SetValue();
									JSONObject jobj = arrSets.getJSONObject(j);
		 							sv.setKey(jobj.getString("key"));
									sv.setType(jobj.getString("type"));
									sv.setName(jobj.getString("name"));
									sv.setValue_type(jobj.getString("value_type"));
									if(jobj.containsKey("set")) {
										JSONArray arrValues = jobj.getJSONArray("set");
										List<ParamValue> pvList = new ArrayList<>(arrValues.size());
										for(int k = 0; k < arrValues.size(); k++) {
											ParamValue pv = new ParamValue();
											JSONObject jobjValue = arrValues.getJSONObject(k);
				 							pv.setDispaly(jobjValue.getString("display"));
											pv.setValue(jobjValue.getString("value"));
											pv.setControl(jobjValue.getString("control"));
											
											pvList.add(pv);
										}
										sv.setSet(pvList);
									}
									
									svList.add(sv);
									param.setSets(svList);
								}
							} else {
								List<ParamValue> pvList = new ArrayList<>(arrSets.size());
								for(int j = 0; j < arrSets.size(); j++) {
									ParamValue pv = new ParamValue();
									JSONObject jobj = arrSets.getJSONObject(j);
		 							pv.setDispaly(jobj.getString("display"));
									pv.setValue(jobj.getString("value"));
									pv.setControl(jobj.getString("control"));
									
									pvList.add(pv);
								}
								param.setSet(pvList);
							}
						}
					}

					params.add(param);
				}
			}
		}

		return new ResultDomain<>(result, params);
	}
	
	public static ResultDomain TermHintConfig(User user, int term_id,
			List<HintConfigInfo> params) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermHintConfig");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", term_id);
		jsonObject.put("hint_num", params.size());

		JSONArray jsonParams = new JSONArray();
		for (int i = 0; i < params.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			HintConfigInfo param = params.get(i);
			jsonObj.put("hint_key", param.getHint_key());
			jsonObj.put("hint_sound_text", param.getHint_sound_text());
			jsonObj.put("hint_text", param.getHint_text());
			jsonObj.put("hint_name", param.getHint_name());
			jsonParams.add(i, jsonObj);
		}

		jsonObject.put("hint_list", jsonParams);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");

		return new ResultDomain<>(result);
	}
	
	public static ResultDomain<List<HintConfigInfo>> TermHintQuery(User user, int term_id) {
		List<HintConfigInfo> params = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermHintQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", term_id);
		jsonObject.put("req_name", 1);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int hint_num = jsStr.getInt("hint_num");
			JSONArray arrParams = null;
			if (hint_num > 0)
				arrParams = jsStr.getJSONArray("hint_list");
			if (arrParams != null && hint_num == arrParams.size()) {
				params = new ArrayList<>(hint_num);
				for (int i = 0; i < hint_num; i++) {
					JSONObject jstrParam = JSONObject.fromObject(arrParams.getJSONObject(i));
					HintConfigInfo param = new HintConfigInfo();

					param.setHint_key(jstrParam.getString("hint_key"));
					param.setHint_sound_text(jstrParam.getString("hint_sound_text"));
					param.setHint_text(jstrParam.getString("hint_text"));
					param.setHint_name(jstrParam.getString("hint_name"));

					params.add(param);
				}
			}
		}

		return new ResultDomain<>(result, params);
	}

	/**
	 * 配置终端权限
	 * 
	 * @param user
	 * @param term_id
	 *            终端id,必须为有效的id
	 * @param faceList
	 * @param expire_begin
	 * @param expire_end
	 * @return
	 */
	public static ResultDomain TermFaceConfig(User user, int term_id, List<FaceInfo> faceList, String expire_begin,
			String expire_end) {
		if (term_id <= 0)
			return new ResultDomain(-1);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermFaceConfig");
		jsonObject.put("version", 0x101);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", term_id);
		jsonObject.put("mode", 0);
		jsonObject.put("face_num", faceList.size());
		JSONArray jsonFaces = new JSONArray();
		for (int i = 0; i < faceList.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			FaceInfo face = faceList.get(i);
			jsonObj.put("face_id", face.getFace_id());
			jsonObj.put("attribute", face.getAttribute());
			jsonObj.put("expire_begin", expire_begin);
			jsonObj.put("expire_end", expire_end);
			jsonFaces.add(i, jsonObj);
		}

		jsonObject.put("face_list", jsonFaces);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		return new ResultDomain<>(result);
	}

	/**
	 * 获取终端权限
	 * 
	 * @param user
	 *            当前用户
	 * @param term_id
	 *            终端id,为0表示查询全部
	 * @return 终端权限列表
	 */
	public static ResultDomain<List<TermFace>> TermFaceQuery(User user, int term_id) {
		List<TermFace> termfaceList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermFaceQuery");
		jsonObject.put("version", 0x101);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", term_id);
		jsonObject.put("mode", 1);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int face_num = jsStr.getInt("face_num");
			JSONArray arrFaces = null;
			if (face_num > 0)
				arrFaces = jsStr.getJSONArray("face_list");
			if (arrFaces != null && face_num == arrFaces.size()) {
				termfaceList = new ArrayList<>(face_num);
				for (int i = 0; i < face_num; i++) {
					JSONObject jstrParam = JSONObject.fromObject(arrFaces.getJSONObject(i));
					TermFace termface = new TermFace();

					termface.setFace_id(jstrParam.getInt("face_id"));
					termface.setAttribute(jstrParam.getInt("attribute"));
					termface.setExpire_begin(jstrParam.getString("expire_begin"));
					termface.setExpire_end(jstrParam.getString("expire_end"));

					termfaceList.add(termface);
				}
			}
		}

		return new ResultDomain<>(result, termfaceList);
	}

	/**
	 * 查询终端状态
	 * 
	 * @param user
	 *            当前用户
	 * @param term_id
	 *            终端id,为0表示查询全部
	 * @return 终端状态列表
	 */
	public static ResultDomain<List<DeviceStatus>> TermStatusQuery(User user, int term_id) {
		List<DeviceStatus> devStatusList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermStatusQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", term_id);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int term_num = jsStr.getInt("term_num");
			JSONArray arrTerms = null;
			if (term_num > 0)
				arrTerms = jsStr.getJSONArray("term_list");
			if (arrTerms != null && term_num == arrTerms.size()) {
				devStatusList = new ArrayList<>(term_num);
				for (int i = 0; i < term_num; i++) {
					JSONObject jstrParam = JSONObject.fromObject(arrTerms.getJSONObject(i));
					DeviceStatus status = new DeviceStatus();

					status.setTerm_id(jstrParam.getInt("term_id"));
					status.setConnect_time(jstrParam.getString("last_report_time"));
					status.setNetwork(String.valueOf(jstrParam.getInt("status")));

					devStatusList.add(status);
				}
			}
		}

		return new ResultDomain<>(result, devStatusList);
	}

	/**
	 * 获取终端所属部门
	 * 
	 * @param user
	 * @param term_id
	 * @return
	 */
	public static ResultDomain<Device> TermDepartQuery(User user, int term_id) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermDepartQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", term_id);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		Device term = null;
		if (result == 0) {
			term = new Device();
			term.setTerm_id(term_id);
			term.setTerm_name(jsStr.getString("term_name"));

			int depart_num = jsStr.getInt("depart_num");
			JSONArray arrDeparts = null;
			if (depart_num > 0)
				arrDeparts = jsStr.getJSONArray("depart_list");
			if (arrDeparts != null && depart_num == arrDeparts.size()) {
				List<Integer> departList = new ArrayList<>(depart_num);
				for (int i = 0; i < depart_num; i++) {
					JSONObject jstrDepart = JSONObject.fromObject(arrDeparts.getJSONObject(i));
					departList.add(jstrDepart.getInt("depart_id"));
				}

				term.setDepart_id(departList);
			}
		}

		return new ResultDomain<>(result, term);
	}

	/**
	 * 配置终端所属部门
	 * 
	 * @param user
	 * @param term
	 * @return
	 */
	public static ResultDomain TermDepartConfig(User user, Device term) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermDepartConfig");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", term.getTerm_id());
		jsonObject.put("term_name", term.getTerm_name());
		jsonObject.put("depart_num", term.getDepart_id().size());
		JSONArray jsonDeparts = new JSONArray();
		for (int i = 0; i < term.getDepart_id().size(); i++) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("depart_id", term.getDepart_id().get(i));
			jsonDeparts.add(i, jsonObj);
		}

		jsonObject.put("depart_list", jsonDeparts);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		return new ResultDomain<>(result);
	}

	/**
	 * 改变终端终端使用状态
	 * 
	 * @param user
	 * @param term_id
	 * @param mode
	 *            0：删除,1：启用,2：停用,已删除的终端不允许进行启用停用操作
	 * @return
	 */
	public static ResultDomain TermInfoDelete(User user, int term_id, int mode) {
		log.debug("Invoke TermInfoDelete Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "TermInfoDelete");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", term_id);
		jsonObject.put("mode", mode);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		return new ResultDomain<>(result);
	}

	/**
	 * 检测照片+提取特征值
	 * 
	 * @param imgPath
	 *            照片路径
	 * @return 人脸区域及其特征值和抠图数据集合(一张照片中可能有多张人脸)
	 */
	public static ResultDomain<List<FDResult>> FaceImageDetect(String img, int mode) {
		List<FDResult> fdList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceImageDetect");
		jsonObject.put("version", 0x100);
		jsonObject.put("mode", 1);

		if (mode == 1) {
			String base64Img;
			if ((base64Img = DataUtil.EncodeImg(img)) != null) {
				jsonObject.put("image_data", base64Img);
			} else {
				return new ResultDomain<>();
			}
		} else if (mode == 2) {
			jsonObject.put("image_data", img);
		}

		JSONObject jsStr = DataUtil.SendRequest(jsonObject, FACE_SERVER);
		int result = jsStr.getInt("result");

		if (result == 0) {
			JSONArray arrFaces = null;
			String feature_ver = "";
			int face_num = jsStr.getInt("face_num");
			if (face_num > 0) {
				arrFaces = jsStr.getJSONArray("face_list");
				if(jsStr.containsKey("feature_ver")) feature_ver = jsStr.getString("feature_ver");
				result = face_num;
			}
			if (arrFaces != null && face_num == arrFaces.size()) {
				fdList = new ArrayList<>(face_num);
				for (int i = 0; i < face_num; i++) {
					JSONObject jstrFDRet = JSONObject.fromObject(arrFaces.getJSONObject(i));
					FDResult fdRet = new FDResult();
					fdRet.setLeft(jstrFDRet.getInt("left"));
					fdRet.setTop(jstrFDRet.getInt("top"));
					fdRet.setRight(jstrFDRet.getInt("right"));
					fdRet.setBottom(jstrFDRet.getInt("bottom"));
					fdRet.setRoll(jstrFDRet.getInt("roll"));
					fdRet.setYaw(jstrFDRet.getInt("yaw"));
					fdRet.setPitch(jstrFDRet.getInt("pitch"));
					fdRet.setQuality(jstrFDRet.getInt("quality"));
					fdRet.setClarity(jstrFDRet.getInt("clarity"));
					fdRet.setConfidence(Float.parseFloat(jstrFDRet.getString("confidence")));
					fdRet.setFeature(jstrFDRet.getString("feature"));
					fdRet.setFeature_ver(feature_ver);
					fdRet.setFace_width(jstrFDRet.getInt("face_width"));
					fdRet.setFace_height(jstrFDRet.getInt("face_height"));
					fdRet.setFace_data(jstrFDRet.getString("face_data"));

					fdList.add(fdRet);
				}
			}
		}

		return new ResultDomain<>(result, fdList);
	}

	/**
	 * 两张照片比对
	 * 
	 * @param imgPath1
	 *            照片1路径
	 * @param imgPath2
	 *            照片2路径
	 * @param mode
	 *            1-传入参数为照片路径，2-传入参数为照片内容base64编码
	 * @return 比对分值
	 */
	public static ResultDomain<Double> FaceImageCompare(String img1, String img2, int mode) {
		log.debug("Invoke FaceImageCompare Method!");

		Double score = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceImageCompare");
		jsonObject.put("version", 0x100);
		if (mode == 1) {
			String base64Img;
			if ((base64Img = DataUtil.EncodeImg(img1)) != null) {
				jsonObject.put("image_data1", base64Img);
			} else {
				return null;
			}

			if ((base64Img = DataUtil.EncodeImg(img2)) != null) {
				jsonObject.put("image_data2", base64Img);
			} else {
				return null;
			}
		} else if (mode == 2) {
			jsonObject.put("image_data1", img1);
			jsonObject.put("image_data2", img2);
		}

		JSONObject jsStr = DataUtil.SendRequest(jsonObject, FACE_SERVER);
		int result = jsStr.getInt("result");

		if (result == 0) {
			score = jsStr.getDouble("score");
		}

		return new ResultDomain<>(result, score);
	}
	
	/**
	 * 照片转换为BGR格式数据
	 * @param img
	 * @return
	 */
	public static ResultDomain<FDResult> FaceImageToBGR(String img) {
		FDResult fdRet = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceImageToBGR");
		jsonObject.put("version", 0x100);
		jsonObject.put("image_data", img);
		
		JSONObject jsStr = DataUtil.SendRequest(jsonObject, FACE_SERVER);
		int result = jsStr.getInt("result");

		if (result == 0) {
			fdRet = new FDResult();
			fdRet.setFace_width(jsStr.getInt("image_width"));
			fdRet.setFace_height(jsStr.getInt("image_height"));
			fdRet.setFace_data(jsStr.getString("image_data"));
		}

		return new ResultDomain<>(result, fdRet);
	}
	
	/**
	 * 1:N照片比对接口
	 * @param img
	 * @return
	 */
	public static ResultDomain<FMResult> FaceImageMatch(String img) {
		int result = -1;
		User user = new User();
		FMResult fmRet = new FMResult();
		JSONObject jsonObject = new JSONObject();
		ResultDomain<List<Company>> resultDomain = CompanyQuery(user);
		for(int i = 0; i < resultDomain.getResultCode(); i++) {
			jsonObject.put("command", "FaceImageMatch");
			jsonObject.put("version", 0x100);
			jsonObject.put("face_library_id", resultDomain.getResultData().get(i).getNet_library_id());
			jsonObject.put("depart_id", 0);
			jsonObject.put("face_num", 1);
			jsonObject.put("image_data", img);
			
			JSONObject jsStr = DataUtil.SendRequest(jsonObject, FACE_SERVER);
			result = jsStr.getInt("result");
			int face_num = jsStr.getInt("face_num");

			if (result == 0 && face_num > 0) {
				JSONArray arrFaces = jsStr.getJSONArray("face_list");
				if(arrFaces.getJSONObject(0).getDouble("score") - fmRet.getScore() > 0) {
					fmRet.setFace_library_id(resultDomain.getResultData().get(i).getNet_library_id());
					fmRet.setFace_id(arrFaces.getJSONObject(0).getInt("face_id"));
					fmRet.setDepart_id(arrFaces.getJSONObject(0).getInt("depart_id"));
					fmRet.setName(arrFaces.getJSONObject(0).getString("name"));
					fmRet.setScore(arrFaces.getJSONObject(0).getDouble("score"));
				}
			}
		}

		return new ResultDomain<>(result, fmRet);
	}

	/**
	 * 获取来访事由
	 * 
	 * @param user
	 *            当前用户
	 * @param reason_id
	 *            事由ID，为0返回全部
	 * @return
	 */
	public static ResultDomain<List<VisitReason>> VisitReasonQuery(User user, int reason_id) {
		log.debug("Invoke VisitReasonQuery Method!");

		List<VisitReason> reasonList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "VisitReasonQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("reason_id", reason_id);
		jsonObject.put("del_flag", 0); // 增加删除标志的请求字段

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int reason_num = jsStr.getInt("reason_num");
			JSONArray arrReasons = null;
			if (reason_num > 0)
				arrReasons = jsStr.getJSONArray("reason_list");
			if (arrReasons != null && reason_num == arrReasons.size()) {
				reasonList = new ArrayList<>(reason_num);
				for (int i = 0; i < reason_num; i++) {
					JSONObject jstrParam = JSONObject.fromObject(arrReasons.getJSONObject(i));
					VisitReason reason = new VisitReason();

					reason.setVisitReasonId(String.valueOf(jstrParam.getInt("reason_id")));
					reason.setVisitReasonName(jstrParam.getString("reason"));
					
					if(("参会").equals(reason.getVisitReasonName())) {
						reasonList.add(0, reason);
					} else {
						reasonList.add(reason);
					}
				}
			}
		}

		return new ResultDomain<>(result, reasonList);
	}

	/**
	 * 上报来访事由
	 * 
	 * @param user
	 *            当前用户
	 * @param reason
	 *            来访事由
	 * @param type
	 *            0：新增或修改,1：删除
	 * @return
	 */
	public static ResultDomain<VisitReason> VisitReasonUpload(User user, VisitReason reason, int type) {
		log.debug("Invoke VisitReasonUpload Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "VisitReasonUpload");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("type", type);
		jsonObject.put("reason_id", reason.getVisitReasonId());
		jsonObject.put("reason", reason.getVisitReasonName());
		/*
		 * jsonObject.put("reason_num", reasonList.size());
		 * 
		 * JSONArray jsonReasons = new JSONArray(); int nIndex = 0;
		 * for(VisitReason reason : reasonList) { JSONObject jsonObj = new
		 * JSONObject(); jsonObj.put("reason_id",
		 * Integer.parseInt(reason.getVisitReasonId())); jsonObj.put("reason",
		 * reason.getVisitReasonName()); jsonReasons.add(nIndex, jsonObj);
		 * nIndex++; }
		 * 
		 * jsonObject.put("reason_list", jsonReasons);
		 */

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			reason.setVisitReasonId(String.valueOf(jsStr.getInt("reason_id")));

			/*
			 * int reason_num = jsStr.getInt("reason_num"); JSONArray arrReasons
			 * = null; if(reason_num > 0) arrReasons =
			 * jsStr.getJSONArray("reason_list"); if(arrReasons != null &&
			 * reason_num == arrReasons.size()) { reasonList.clear(); for(int i
			 * = 0; i < reason_num; i++) { JSONObject jstrParam =
			 * JSONObject.fromObject(arrReasons.getJSONObject(i)); VisitReason
			 * reason = new VisitReason();
			 * 
			 * reason.setVisitReasonId(String.valueOf(jstrParam.getInt(
			 * "reason_id")));
			 * reason.setVisitReasonName(jstrParam.getString("reason"));
			 * 
			 * reasonList.add(reason); } }
			 */
		}

		return new ResultDomain<>(result, reason);
	}

	/**
	 * 获取系统参数
	 * 
	 * @param user
	 * @return
	 */
	public static ResultDomain<List<ParamConfigInfo>> LibraryParamQuery(User user) {
		List<ParamConfigInfo> params = new ArrayList<>();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "LibraryParamQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if (result == 0) {
			int paramNums = jsStr.getInt("param_num");
			JSONArray arrParams = null;
			if (paramNums > 0)
				arrParams = jsStr.getJSONArray("param_list");
			if (arrParams != null && paramNums == arrParams.size()) {
				for (int i = 0; i < paramNums; i++) {
					JSONObject jstrParam = JSONObject.fromObject(arrParams.getJSONObject(i));
					ParamConfigInfo param = new ParamConfigInfo();

					param.setParam_key(jstrParam.getString("param_key"));
					param.setParam_value(jstrParam.getString("param_value"));
					param.setParam_name(jstrParam.getString("param_name"));

					params.add(param);
				}
			}
		}

		return new ResultDomain<>(result, params);
	}

	/**
	 * 配置系统参数
	 * 
	 * @param user
	 * @param params
	 * @return
	 */
	public static ResultDomain<List<ParamConfigInfo>> LibraryParamConfig(User user, List<ParamConfigInfo> params) {
		if (user.getFace_library_id() <= 0 || params.isEmpty()) {
			throw new DataServiceException("Invoke LibraryParamConfig Failed : Param is error!");
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "LibraryParamConfig");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("param_num", params.size());

		JSONArray jsonParams = new JSONArray();
		for (int i = 0; i < params.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			ParamConfigInfo param = params.get(i);
			jsonObj.put("param_key", param.getParam_key());
			jsonObj.put("param_value", param.getParam_value());
			jsonObj.put("param_name", param.getParam_name());
			jsonParams.add(i, jsonObj);
		}

		jsonObject.put("param_list", jsonParams);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);

		int result = jsStr.getInt("result");

		return new ResultDomain<>(result, params);
	}

	/**
	 * 查询访客来访信息
	 * 
	 * @param user
	 *            当前用户
	 * @param queryInfo
	 *            模糊查询内容
	 * @param start
	 *            开始索引，从0开始
	 * @return 来访日志总数及列表，每次最多返回200条记录
	 */
	public static ResultDomain<List<VisitQueryInfo>> VisitInfoQuery(User user, VisitQueryInfo queryInfo, int start) {
		log.debug("Invoke VisitInfoQuery Method!");
		Map statistics = null;
		List<VisitQueryInfo> visitorList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "VisitInfoQuery");
		jsonObject.put("version", 0x103);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("attribute", queryInfo.getAttribute());
		jsonObject.put("mode", queryInfo.getMode());
		jsonObject.put("status", queryInfo.getStatus()); // 状态 -1-所有 0-预约 1-在访
															// 2-离开
		jsonObject.put("picture", queryInfo.getPic_num());
		jsonObject.put("employee_id", queryInfo.getEmployee_id());
		jsonObject.put("start_time", queryInfo.getVisit_time());
		jsonObject.put("end_time", queryInfo.getExpire_time());
		jsonObject.put("query_info", queryInfo.getQuery_info());
		jsonObject.put("visit_num", queryInfo.getVisit_num());
		jsonObject.put("start", start);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int total_num = jsStr.getInt("total_num");
			int visit_num = jsStr.getInt("visit_num");
			int book_num = jsStr.getInt("book_num");
			int visiting_num = jsStr.getInt("visiting_num");
			int leave_num = jsStr.getInt("leave_num");
			JSONArray arrVisitors = null;
			if (visit_num > 0)
				arrVisitors = jsStr.getJSONArray("visit_list");
			if (arrVisitors != null && visit_num == arrVisitors.size()) {
				visitorList = new ArrayList<>(visit_num);
				for (int i = 0; i < visit_num; i++) {
					JSONObject jstrVisitor = JSONObject.fromObject(arrVisitors.getJSONObject(i));
					VisitQueryInfo info = new VisitQueryInfo();

					info.setFace_id(jstrVisitor.getInt("face_id"));
					info.setBook_id(jstrVisitor.getInt("book_id"));
					info.setRec_id(jstrVisitor.getInt("rec_id"));
					info.setBat_id(jstrVisitor.getInt("bat_id"));
					info.setAttribute(jstrVisitor.getInt("attribute"));
					info.setStatus(jstrVisitor.getInt("status"));
					info.setVisit_num(jstrVisitor.getInt("visit_num"));
					info.setEmployee_id(jstrVisitor.getInt("employee_id"));
					info.setDepart_id(jstrVisitor.getInt("depart_id"));
					info.setReason_id(jstrVisitor.getInt("reason_id"));
					info.setPic_num(jstrVisitor.getInt("picture_num"));
					
					info.setName(jstrVisitor.getString("name"));
					info.setContent(jstrVisitor.getString("content"));
					info.setEmployee(jstrVisitor.getString("employee"));
					info.setVisit_time(jstrVisitor.getString("visit_time"));
					info.setExpire_time(jstrVisitor.getString("expire_time"));
					info.setBat_title(jstrVisitor.getString("bat_title"));

					visitorList.add(info);
				}
			}
			
			statistics = new HashMap<String, Integer>(3);
			statistics.put("book_num", book_num);
			statistics.put("visiting_num", visiting_num);
			statistics.put("leave_num", leave_num);
			result = total_num;
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, visitorList, statistics);
	}

	/**
	 * 上报访客来访日志
	 * 
	 * @param user
	 * @param info
	 * @return
	 */
	public static ResultDomain<FaceVisitInfo> VisitRecordUpload(User user, FaceVisitInfo info) {
		log.debug("Invoke VisitRecordUpload Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "VisitRecordUpload");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("attribute", info.getAttribute());
		jsonObject.put("face_id", info.getFace_id());
		jsonObject.put("name", info.getName());
		jsonObject.put("idcard", DataUtil.Id2HexStr(info.getIdcard()));
		jsonObject.put("employee_id", info.getEmployee_id());
		jsonObject.put("bat_id", info.getBat_id());
		jsonObject.put("rec_id", info.getRec_id());
		jsonObject.put("status", info.getStatus());
		jsonObject.put("depart_id", info.getDepart_id());
		jsonObject.put("employee", info.getEmployee());
		jsonObject.put("term_id", info.getTerm_id());
		jsonObject.put("reason_id", info.getReason_id());
		jsonObject.put("content", info.getContent());
		jsonObject.put("visit_time", info.getVisit_time());
		jsonObject.put("expire_time", info.getExpire_time());
		jsonObject.put("visit_num", info.getVisit_num());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if (result == 0) {
			info.setRec_id(jsStr.getInt("rec_id"));
		}

		return new ResultDomain<>(result, info);
	}

	/**
	 * 查询访客来访日志
	 * 
	 * @param user
	 * @param queryInfo rec_id-记录id;bat_id-批量id
	 * @param start
	 * @return
	 */
	public static ResultDomain<List<FaceVisitInfo>> VisitRecordQuery(User user, FaceVisitInfo queryInfo, int start) {
		List<FaceVisitInfo> visitorList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "VisitRecordQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("rec_id", queryInfo.getRec_id());
		jsonObject.put("bat_id", queryInfo.getBat_id());
		jsonObject.put("face_id", queryInfo.getFace_id());
		jsonObject.put("name", queryInfo.getName());
		jsonObject.put("attribute", -1);
		jsonObject.put("start_time", queryInfo.getVisit_time());
		jsonObject.put("end_time", queryInfo.getExpire_time());
		jsonObject.put("visit_num", queryInfo.getVisit_num());
		jsonObject.put("start", start);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int total_num = jsStr.getInt("total_num");
			int visit_num = jsStr.getInt("visit_num");
			JSONArray arrVisitors = null;
			if (visit_num > 0)
				arrVisitors = jsStr.getJSONArray("visit_list");
			if (arrVisitors != null && visit_num == arrVisitors.size()) {
				visitorList = new ArrayList<>(visit_num);
				for (int i = 0; i < visit_num; i++) {
					JSONObject jstrVisitor = JSONObject.fromObject(arrVisitors.getJSONObject(i));
					FaceVisitInfo info = new FaceVisitInfo();

					info.setFace_id(jstrVisitor.getInt("face_id"));
					info.setRec_id(jstrVisitor.getInt("rec_id"));
					info.setBat_id(jstrVisitor.getInt("bat_id"));
					info.setAttribute(jstrVisitor.getInt("attribute"));
					info.setStatus(jstrVisitor.getInt("status"));
					info.setVisit_num(jstrVisitor.getInt("visit_num"));
					info.setEmployee_id(jstrVisitor.getInt("employee_id"));
					info.setDepart_id(jstrVisitor.getInt("depart_id"));

					info.setName(jstrVisitor.getString("name"));
					info.setContent(jstrVisitor.getString("content"));
					info.setEmployee(jstrVisitor.getString("employee"));
					info.setVisit_time(jstrVisitor.getString("visit_time"));
					info.setExpire_time(jstrVisitor.getString("expire_time"));

					visitorList.add(info);
				}
			}

			result = total_num;
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, visitorList);
	}

	/**
	 * 更新访客来访日志状态
	 * 
	 * @param user
	 *            当前用户
	 * @param info
	 *            必填rec_id - 来访纪录id；更新状态 - 1：签离2：删除
	 * @return 更新结果，0-成功
	 */
	public static ResultDomain VisitRecordDelete(User user, FaceVisitInfo info) {
		log.debug("Invoke VisitRecordDelete Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "VisitRecordDelete");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("rec_id", info.getRec_id());
		jsonObject.put("status", info.getStatus());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		return new ResultDomain<>(result);
	}

	/**
	 * 上报访客离开
	 * 
	 * @param user
	 * @param info
	 * @return
	 */
	public static ResultDomain VisitLeave(User user, FaceVisitInfo info) {
		log.debug("Invoke VisitLeave Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "VisitLeave");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("attribute", info.getAttribute());
		jsonObject.put("rec_id", info.getRec_id());
		jsonObject.put("book_id", info.getBook_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		return new ResultDomain<>(result);
	}
	
	/** 上报访客预约信息
	 * @param user
	 * @param info
	 * @return
	 */
	public static ResultDomain<FaceVisitInfo> FaceBookUpload(User user, FaceVisitInfo info) {
		log.debug("Invoke FaceBookUpload Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceBookUpload");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("attribute", info.getAttribute());
		jsonObject.put("face_id", info.getFace_id());
		jsonObject.put("name", info.getName());
		jsonObject.put("idcard", DataUtil.Id2HexStr(info.getIdcard()));
		jsonObject.put("employee_id", info.getEmployee_id());
		jsonObject.put("book_id", info.getBook_id());
		jsonObject.put("status", info.getStatus());
		jsonObject.put("depart_id", info.getDepart_id());
		jsonObject.put("employee", info.getEmployee());
		jsonObject.put("term_num", 0);
		jsonObject.put("reason_id", info.getReason_id());
		jsonObject.put("content", info.getContent());
		jsonObject.put("visit_time", info.getVisit_time());
		jsonObject.put("expire_time", info.getExpire_time());
		jsonObject.put("visit_num", info.getVisit_num());
		jsonObject.put("is_auth", info.getIs_auth());
		jsonObject.put("company", info.getCompany());
		jsonObject.put("tel", info.getTel());
		jsonObject.put("sex", info.getSex());
		
		if(info.getTerms() != null && info.getTerms().isEmpty() == false) {
			jsonObject.put("term_num", info.getTerms().size());
			JSONArray jsonTerms = new JSONArray();
			for (int i = 0; i < info.getTerms().size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("term_id", info.getTerms().get(i));
				jsonTerms.add(i, jsonObj);
			}
			
			jsonObject.put("term_list", jsonTerms);
		}
		
		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if (result == 0) {
			info.setBook_id(jsStr.getInt("book_id"));
			info.setRevno(jsStr.getString("revno"));
		}

		return new ResultDomain<>(result, info);
	}

	/**查询访客预约信息
	 * @param user
	 * @param queryInfo
	 * @param start
	 * @return
	 */
	public static ResultDomain<List<FaceVisitInfo>> FaceBookQuery(User user, FaceVisitInfo queryInfo, int start) {
		List<FaceVisitInfo> visitorList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceBookQuery");
		jsonObject.put("version", 0x102);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("book_id", queryInfo.getBook_id());
		jsonObject.put("term_id", queryInfo.getTerm_id());
		jsonObject.put("revno", "");
		jsonObject.put("book_num", queryInfo.getVisit_num());
		jsonObject.put("start", start);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int total_num = jsStr.getInt("total_num");
			int book_num = jsStr.getInt("book_num");
			JSONArray arrVisitors = null;
			if (book_num > 0)
				arrVisitors = jsStr.getJSONArray("book_list");
			if (arrVisitors != null && book_num == arrVisitors.size()) {
				visitorList = new ArrayList<>(book_num);
				for (int i = 0; i < book_num; i++) {
					JSONObject jstrVisitor = JSONObject.fromObject(arrVisitors.getJSONObject(i));
					FaceVisitInfo info = new FaceVisitInfo();

					info.setFace_id(jstrVisitor.getInt("face_id"));
					info.setBook_id(jstrVisitor.getInt("book_id"));
					//info.setBat_id(jstrVisitor.getInt("bat_id"));
					info.setAttribute(jstrVisitor.getInt("attribute"));
					//info.setStatus(jstrVisitor.getInt("status"));
					info.setVisit_num(jstrVisitor.getInt("visit_num"));
					info.setEmployee_id(jstrVisitor.getInt("employee_id"));
					info.setDepart_id(jstrVisitor.getInt("depart_id"));
					info.setSex(jstrVisitor.getInt("sex"));
					info.setIs_auth(jstrVisitor.getInt("is_auth"));
					info.setDel_flag(jstrVisitor.getInt("del_flag"));
					//info.setReason_id(jstrVisitor.getInt("reason_id"));
					//info.setMode(jstrVisitor.getInt("mode"));

					info.setName(jstrVisitor.getString("name"));
					info.setContent(jstrVisitor.getString("content"));
					info.setEmployee(jstrVisitor.getString("employee"));
					info.setTel(jstrVisitor.getString("tel"));
					info.setCompany(jstrVisitor.getString("company"));
					info.setVisit_time(jstrVisitor.getString("visit_time"));
					info.setExpire_time(jstrVisitor.getString("expire_time"));
					info.setCreate_time(jstrVisitor.getString("create_time"));
					info.setRevno(jstrVisitor.getString("revno"));
					
					int term_num = jstrVisitor.getInt("term_num");
					if(term_num > 0) {
						JSONArray arrTerms = jstrVisitor.getJSONArray("term_list");
						List<Integer> termList = new ArrayList<>(term_num);
						for(int j = 0; j < arrTerms.size(); j++) {
							termList.add(arrTerms.getJSONObject(j).getInt("term_id"));
						}
						info.setTerms(termList);
					}

					visitorList.add(info);
				}
			}

			result = total_num;
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, visitorList);
	}
	
	
	/**取消访客预约
	 * @param user
	 * @param info 传入book_id和status 0：删除 1：签离
	 * @return
	 */
	public static ResultDomain FaceBookDelete(User user, FaceVisitInfo info) {
		log.debug("Invoke FaceBookDelete Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceBookDelete");
		jsonObject.put("version", 0x101);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("status", info.getStatus());	//0：删除 1：签离
		jsonObject.put("book_id", info.getBook_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		return new ResultDomain<>(result);
	}
	

	/**
	 * 上传批量访客登记信息
	 * 
	 * @param user
	 * @param bat_visitor
	 * @return
	 */
	public static ResultDomain<BatVisitor> BatVisitUpload(User user, BatVisitor bat_visitor) {
		log.debug("Invoke BatVisitUpload Method!");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "BatVisitUpload");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("employee_id", bat_visitor.getEmployee_id());
		jsonObject.put("reason_id", bat_visitor.getReason_id());
		jsonObject.put("term_num", bat_visitor.getTerm_num());
		jsonObject.put("bat_id", bat_visitor.getBat_id());
		jsonObject.put("title", bat_visitor.getTitle());
		jsonObject.put("visit_time", bat_visitor.getVisit_time());
		jsonObject.put("expire_time", bat_visitor.getExpire_time());
		jsonObject.put("content", bat_visitor.getContent());

		JSONArray jsonTerms = new JSONArray();
		int nIndex = 0;
		for (Integer term_id : bat_visitor.getTerm_list()) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("term_id", term_id);
			jsonTerms.add(nIndex, jsonObj);
			nIndex++;
		}

		jsonObject.put("term_list", jsonTerms);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if (result == 0) {
			bat_visitor.setBat_id(jsStr.getInt("bat_id"));
		}

		return new ResultDomain<>(result, bat_visitor);
	}

	/**
	 * 查询批量来访信
	 * 
	 * @param user
	 * @param bat_visitor
	 *            bat_id-指定id的批量信息，为0查全部
	 * @param start
	 *            起始索引号
	 * @param nums
	 *            返回的记录总数
	 * @return
	 */
	public static ResultDomain<List<BatVisitor>> BatVisitQuery(User user, BatVisitor bat_visitor, int start, int nums) {
		List<BatVisitor> batList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "BatVisitQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("bat_id", bat_visitor.getBat_id());
		jsonObject.put("employee_id", bat_visitor.getEmployee_id());
		jsonObject.put("reason_id", bat_visitor.getReason_id());
		jsonObject.put("start_time", bat_visitor.getVisit_time());
		jsonObject.put("end_time", bat_visitor.getExpire_time());
		jsonObject.put("status", bat_visitor.getStatus());
		jsonObject.put("start", start);
		jsonObject.put("bat_num", nums);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int total_num = jsStr.getInt("total_num");
			int bat_num = jsStr.getInt("bat_num");
			JSONArray arrBats = null;
			if (bat_num > 0)
				arrBats = jsStr.getJSONArray("bat_list");
			if (arrBats != null && bat_num == arrBats.size()) {
				batList = new ArrayList<>(bat_num);
				for (int i = 0; i < bat_num; i++) {
					JSONObject jstrBat = JSONObject.fromObject(arrBats.getJSONObject(i));
					BatVisitor info = new BatVisitor();

					info.setBat_id(jstrBat.getInt("bat_id"));
					info.setEmployee_id(jstrBat.getInt("employee_id"));
					info.setReason_id(jstrBat.getInt("reason_id"));
					info.setStatus(jstrBat.getInt("status"));
					info.setTitle(jstrBat.getString("title"));
					info.setContent(jstrBat.getString("content"));
					info.setVisit_time(jstrBat.getString("visit_time"));
					info.setExpire_time(jstrBat.getString("expire_time"));
					int term_num = jstrBat.getInt("term_num");
					if (term_num > 0) {
						JSONArray arrTerms = jstrBat.getJSONArray("term_list");
						List<Integer> term_list = new ArrayList<>(term_num);
						for (int j = 0; j < term_num; j++) {
							term_list.add(arrTerms.getJSONObject(j).getInt("term_id"));
						}

						info.setTerm_list(term_list);
					}

					batList.add(info);
				}
			}

			result = total_num;
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, batList);
	}

	/**
	 * 删除批量来访信息
	 * 
	 * @param user
	 *            当前用户
	 * @param bat_visitor
	 *            传入需要删除的批量id
	 * @return
	 */
	public static ResultDomain BatVisitDelete(User user, BatVisitor bat_visitor) {
		log.debug("Invoke BatVisitDelete Method!");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "BatVisitDelete");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("bat_id", bat_visitor.getBat_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		return new ResultDomain<>(result);
	}

	/**
	 * 查询考勤
	 * 
	 * @param user
	 *            当前用户
	 * @param queryInfo
	 *            查询信息相关
	 * @param start
	 *            起始索引号，从0开始
	 * @param num
	 *            记录数
	 * @return 考勤记录列表
	 */
	public static ResultDomain<List<AttendanceInfo>> AttendanceQuery(User user, AttendanceQueryInfo queryInfo,
			int start, int num) {
		List<AttendanceInfo> recordList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "AttendanceQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());

		jsonObject.put("mode", queryInfo.getMode());
		jsonObject.put("order", queryInfo.getOrder());
		jsonObject.put("face_id", queryInfo.getFace_id());
		jsonObject.put("term_id", queryInfo.getTerm_id());
		jsonObject.put("attribute", queryInfo.getAttribute());
		jsonObject.put("query_info", queryInfo.getQuery_info());
		jsonObject.put("start_time", queryInfo.getStart_time());
		jsonObject.put("end_time", queryInfo.getEnd_time());
		jsonObject.put("depart_num", queryInfo.getDeparts().size());
		jsonObject.put("start", start);
		jsonObject.put("rec_num", num);

		JSONArray jsonDeparts = new JSONArray();
		int nIndex = 0;
		for (Integer departId : queryInfo.getDeparts()) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("depart_id", departId);
			jsonDeparts.add(nIndex, jsonObj);
			nIndex++;
		}

		jsonObject.put("depart_list", jsonDeparts);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int total_num = jsStr.getInt("total_num");
			int rec_num = jsStr.getInt("rec_num");
			JSONArray arrRecords = null;
			if (rec_num > 0)
				arrRecords = jsStr.getJSONArray("rec_list");
			if (arrRecords != null && rec_num == arrRecords.size()) {
				recordList = new ArrayList<>(rec_num);
				for (int i = 0; i < rec_num; i++) {
					JSONObject jstrRecord = JSONObject.fromObject(arrRecords.getJSONObject(i));
					AttendanceInfo rec = new AttendanceInfo();

					rec.setFace_id(jstrRecord.getInt("face_id"));
					rec.setName(jstrRecord.getString("name"));
					rec.setEmpno(jstrRecord.getString("employee_id"));
					rec.setDepart(jstrRecord.getString("depart_name"));
					rec.setFirst_term_id(jstrRecord.getInt("first_term_id"));
					rec.setFirst_term_name(jstrRecord.getString("first_term_name"));
					rec.setLast_term_id(jstrRecord.getInt("last_term_id"));
					rec.setLast_term_name(jstrRecord.getString("last_term_name"));
					rec.setDate(jstrRecord.getString("date"));
					rec.setFirst_time(jstrRecord.getString("first_time"));
					rec.setLast_time(jstrRecord.getString("last_time"));

					recordList.add(rec);
				}
			}

			result = total_num;
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, recordList);
	}
	
	public static ResultDomain<List<Menu>> MenuQuery(int businessId) {
		List<Menu> menuList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "MenuQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("business_id", businessId);
		jsonObject.put("mode", 1);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int rec_num = jsStr.getInt("rec_num");
			result = rec_num;
			JSONArray arrRecords = null;
			if (rec_num > 0)
				arrRecords = jsStr.getJSONArray("rec_list");
			if (arrRecords != null && rec_num == arrRecords.size()) {
				menuList = new ArrayList<>(rec_num);
				for (int i = 0; i < rec_num; i++) {
					JSONObject jstrRecord = arrRecords.getJSONObject(i);
					Menu menu = new Menu();
					menu.setMenu_id(jstrRecord.getInt("menu_id"));
					menu.setParent_id(jstrRecord.getInt("parent_id"));
					menu.setName(jstrRecord.getString("name"));
					menu.setOrder(jstrRecord.getInt("order_index"));

					menuList.add(menu);
				}
			}
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, menuList);
	}
	
	//获取角色信息接口
	public static ResultDomain<List<Role>> RoleQuery(User user, int roleId) {
		List<Role> roleList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "RoleQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("role_id", roleId);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int rec_num = jsStr.getInt("rec_num");
			result = rec_num;
			JSONArray arrRecords = null;
			if (rec_num > 0)
				arrRecords = jsStr.getJSONArray("rec_list");
			if (arrRecords != null && rec_num == arrRecords.size()) {
				roleList = new ArrayList<>(rec_num);
				for (int i = 0; i < rec_num; i++) {
					JSONObject jstrRecord = arrRecords.getJSONObject(i);
					Role role = new Role();
					role.setRole_id(jstrRecord.getInt("role_id"));
					role.setName(jstrRecord.getString("name"));
					role.setRemark(jstrRecord.getString("remark"));
					role.setCreate_time(jstrRecord.getString("create_time"));
					role.setUpdate_time(jstrRecord.getString("update_time"));
					
					int menu_num = jstrRecord.getInt("menu_num");
					if(menu_num > 0) {
						List<Menu> menuList = new ArrayList<>(menu_num);
						JSONArray arrMenus = jstrRecord.getJSONArray("menu_list");
						for(int j = 0; j < menu_num; j++) {
							JSONObject jsonMenu = arrMenus.getJSONObject(j);
							Menu menu = new Menu();
							menu.setMenu_id(jsonMenu.getInt("menu_id"));
							menu.setParent_id(jsonMenu.getInt("parent_id"));
							menu.setName(jsonMenu.getString("name"));
							menu.setOrder(jsonMenu.getInt("order"));
							
							menuList.add(menu);
						}
						
						role.setMenu_list(menuList);
					}

					roleList.add(role);
				}
			}
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, roleList);
	}
	
	//上报角色信息接口
	public static ResultDomain<Role> RoleInfoUpload(User user, Role role) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "RoleInfoUpload");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("role_id", role.getRole_id());
		jsonObject.put("role_name", role.getName());
		
		if (role.getMenu_list() != null && !role.getMenu_list().isEmpty()) {
			jsonObject.put("menu_num", role.getMenu_list().size());
			JSONArray jsonMenus = new JSONArray();
			for (int i = 0; i < role.getMenu_list().size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("menu_id", role.getMenu_list().get(i).getMenu_id());
				jsonMenus.add(i, jsonObj);
			}
	
			jsonObject.put("menu_list", jsonMenus);
		}
		else{
			jsonObject.put("menu_num", 0);
		}

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if(result == 0) {
			role.setRole_id(jsStr.getInt("role_id"));
		}
		
		return new ResultDomain<>(result, role);
	}
	
	// 删除角色信息接口
	public static ResultDomain RoleInfoDelete(User user, Role role) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "RoleInfoDelete");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("role_id", role.getRole_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		
		return new ResultDomain<>(result);
	}
	
	//上报公司信息接口
	public static ResultDomain<Company> CompanyInfoUpload(Company company) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceLibraryUpload");
		jsonObject.put("version", 0x102);
		jsonObject.put("face_library_id", company.getFace_library_id());
		jsonObject.put("worker_num", 1);
		jsonObject.put("business_id", company.getBusiness_id());
		jsonObject.put("name", company.getName());
		jsonObject.put("net_mode", company.getNet_mode());
		jsonObject.put("description", company.getDescription());
		jsonObject.put("legal_person", company.getLegal_person());
		jsonObject.put("company_tax", company.getCompany_tax());
		jsonObject.put("company_no", company.getCompany_no());
		jsonObject.put("trade", company.getTrade());
		jsonObject.put("contact_person", company.getContact_person());
		jsonObject.put("tel", company.getTel());
		jsonObject.put("email", company.getEmail());
		jsonObject.put("addr", company.getAddr());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if(result == 0) {
			company.setFace_library_id(jsStr.getInt("face_library_id"));
		}
		
		return new ResultDomain<>(result, company);
	}
	
	//获取公司(人脸库)信息接口
	public static ResultDomain<List<Company>> CompanyQuery(User user) {
		List<Company> companyList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceLibraryQuery");
		jsonObject.put("version", 0x101);
		jsonObject.put("face_library_id", user.getFace_library_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int library_num = jsStr.getInt("face_library_num");
			result = library_num;
			JSONArray arrRecords = null;
			if (library_num > 0)
				arrRecords = jsStr.getJSONArray("face_library_list");
			if (arrRecords != null && library_num == arrRecords.size()) {
				companyList = new ArrayList<>(library_num);
				for (int i = 0; i < library_num; i++) {
					JSONObject jstrRecord = arrRecords.getJSONObject(i);
					Company company = new Company();
					company.setFace_library_id(jstrRecord.getInt("face_library_id"));
					company.setNet_mode(jstrRecord.getInt("net_mode"));
					company.setNet_library_id(jstrRecord.getInt("net_library_id"));
					company.setBusiness_id(jstrRecord.getInt("business_id"));
					
					company.setName(jstrRecord.getString("name"));
					company.setCompany_tax(jstrRecord.getString("company_tax"));
					company.setTel(jstrRecord.getString("tel"));
					company.setEmail(jstrRecord.getString("email"));
					company.setDescription(jstrRecord.getString("description"));
					company.setContact_person(jstrRecord.getString("contact_person"));
					company.setLegal_person(jstrRecord.getString("legal_person"));
					company.setCompany_no(jstrRecord.getString("company_no"));
					company.setTrade(jstrRecord.getString("trade"));
					company.setAddr(jstrRecord.getString("addr"));
					//company.setEnglishname(jstrRecord.getString("english_name"));
					//company.setPinyin_name(jstrRecord.getString("pinyin_name"));

					companyList.add(company);
				}
			}
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, companyList);
	}
	
	// 删除人脸库(公司)信息接口
	public static ResultDomain CompanyDelete(User user) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "FaceLibraryDelete");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		
		return new ResultDomain<>(result);
	}
	
	//上报会议信息
	public static ResultDomain<Conference> ConferenceInfoUpload(User user, Conference confInfo) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "ConferenceInfoUpload");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("conf_id", confInfo.getConf_id());
		jsonObject.put("parent_conf_id", confInfo.getParent_conf_id());
		jsonObject.put("term_layout", confInfo.getTerm_layout());
		jsonObject.put("need_face_compare", confInfo.getNeed_face_compare());
		jsonObject.put("start_time", confInfo.getStart_time());
		jsonObject.put("end_time", confInfo.getEnd_time());
		jsonObject.put("sign_timespan", confInfo.getSign_time());
		jsonObject.put("name", confInfo.getName());
		jsonObject.put("addr", confInfo.getAddr());
		jsonObject.put("logo", confInfo.getLogo());
		jsonObject.put("background", confInfo.getBackground());
		
		if(confInfo.getTerm_list() != null && confInfo.getTerm_list().isEmpty())
		{
			jsonObject.put("term_num", 0);
		}
		else
		{
			jsonObject.put("term_num", confInfo.getTerm_list().size());
			JSONArray jsonTerms = new JSONArray();
			for (int i = 0; i < confInfo.getTerm_list().size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("term_id", confInfo.getTerm_list().get(i));
				jsonTerms.add(i, jsonObj);
			}

			jsonObject.put("term_list", jsonTerms);
		}
		
		if (confInfo.getConf_id() == 0) {
			jsonObject.put("param_num", 21);
		} else {
			jsonObject.put("param_num", 11);
		}
		
		JSONArray jsonParas = new JSONArray();
		JSONObject jsonPara = new JSONObject();
		jsonPara.put("param_key", "register_logo");
		jsonPara.put("param_value", confInfo.getRegister_logo());
		jsonPara.put("param_name", "(1057)报名Logo");
		jsonParas.add(jsonPara);
		
		jsonPara = new JSONObject();
		jsonPara.put("param_key", "detect_mode");
		jsonPara.put("param_value", confInfo.getRecognittype());
		jsonPara.put("param_name", "(1008)识别方式");
		jsonParas.add(jsonPara);
		
		jsonPara = new JSONObject();
		jsonPara.put("param_key", "need_face_compare");
		jsonPara.put("param_value", confInfo.getNeed_face_compare());
		jsonPara.put("param_name", "(1009)人证比对");
		jsonParas.add(jsonPara);
		
		jsonPara = new JSONObject();
		jsonPara.put("param_key", "sign_mode");
		jsonPara.put("param_value", confInfo.getSign_mode());
		jsonPara.put("param_name", "(1028)签到模式");
		jsonParas.add(jsonPara);
		
		jsonPara = new JSONObject();
		jsonPara.put("param_key", "auth_level");
		jsonPara.put("param_value", confInfo.getAuth_level());
		jsonPara.put("param_name", "(1030)权限等级");
		jsonParas.add(jsonPara);
		
		jsonPara = new JSONObject();
		jsonPara.put("param_key", "is_show_jl_logo");
		jsonPara.put("param_value", confInfo.getShow_logo());
		jsonPara.put("param_name", "(1043)显示精伦logo");
		jsonParas.add(jsonPara);
		
		jsonPara = new JSONObject();
		jsonPara.put("param_key", "is_allow_add_company");
		jsonPara.put("param_value", confInfo.getAdd_company());
		jsonPara.put("param_name", "(1044)新增公司");
		jsonParas.add(jsonPara);
		
		jsonPara = new JSONObject();
		jsonPara.put("param_key", "live_reg_type");
		jsonPara.put("param_value", confInfo.getReg_type());
		jsonPara.put("param_name", "(1045)现场注册方式");
		jsonParas.add(jsonPara);
		
		jsonPara = new JSONObject();
		jsonPara.put("param_key", "register_url");
		jsonPara.put("param_value", confInfo.getRegister_url());
		jsonPara.put("param_name", "(1056)报名链接");
		jsonParas.add(jsonPara);
		
		jsonPara = new JSONObject();
		jsonPara.put("param_key", "is_manual_pass");
		jsonPara.put("param_value", confInfo.getHand_allow());
		jsonPara.put("param_name", "(1059)是否允许手动通过");
		jsonParas.add(jsonPara);
		
		jsonPara = new JSONObject();
		jsonPara.put("param_key", "register_deadline");
		jsonPara.put("param_value", confInfo.getSign_deadline());
		jsonPara.put("param_name", "(1063)网络报名截止时间");
		jsonParas.add(jsonPara);
		
		jsonPara = new JSONObject();
		jsonPara.put("param_key", "is_show_sign_list");
		jsonPara.put("param_value", confInfo.getIs_show_sign_list());
		jsonPara.put("param_name", "(1079)是否显示签到列表");
		jsonParas.add(jsonPara);
		
		jsonPara = new JSONObject();
		jsonPara.put("param_key", "business_ctrl");
		jsonPara.put("param_value", confInfo.getBusiness_ctrl());
		jsonPara.put("param_name", "(1081)初始业务控制");
		jsonParas.add(jsonPara);
		
		if(confInfo.getConf_id() == 0) {
			jsonPara = new JSONObject();
			jsonPara.put("param_key", "is_check_pay");
			jsonPara.put("param_value", 0);
			jsonPara.put("param_name", "(1046)检查缴费状态");
			jsonParas.add(jsonPara);
			
			jsonPara = new JSONObject();
			jsonPara.put("param_key", "sign_switch_zero_text");
			jsonPara.put("param_value", "开始签到");
			jsonPara.put("param_name", "(1047)签到控制按钮初始文字内容");
			jsonParas.add(jsonPara);
			
			jsonPara = new JSONObject();
			jsonPara.put("param_key", "sign_switch_one_text");
			jsonPara.put("param_value", "停止签到");
			jsonPara.put("param_name", "(1048)签到控制按钮点击后文字内容");
			jsonParas.add(jsonPara);
			
			jsonPara = new JSONObject();
			jsonPara.put("param_key", "sign_list_name");
			jsonPara.put("param_value", "姓名");
			jsonPara.put("param_name", "(1049)签到列表姓名");
			jsonParas.add(jsonPara);
			
			jsonPara = new JSONObject();
			jsonPara.put("param_key", "sign_list_company");
			jsonPara.put("param_value", "公司");
			jsonPara.put("param_name", "(1050)签到列表公司");
			jsonParas.add(jsonPara);
			
			jsonPara = new JSONObject();
			jsonPara.put("param_key", "sign_list_position");
			jsonPara.put("param_value", "职务");
			jsonPara.put("param_name", "(1051)签到列表职务");
			jsonParas.add(jsonPara);
			
			jsonPara = new JSONObject();
			jsonPara.put("param_key", "sign_list_create_time");
			jsonPara.put("param_value", "签到时间");
			jsonPara.put("param_name", "(1052)签到列表签到时间");
			jsonParas.add(jsonPara);
			
			jsonPara = new JSONObject();
			jsonPara.put("param_key", "first_sign_hint_text");
			jsonPara.put("param_value", "签到成功");
			jsonPara.put("param_name", "(1053)首次签到成功提示语");
			jsonParas.add(jsonPara);

			jsonPara = new JSONObject();
			jsonPara.put("param_key", "second_sign_hint_text");
			jsonPara.put("param_value", "您已签到");
			jsonPara.put("param_name", "(1054)重复签到成功提示语");
			jsonParas.add(jsonPara);
		
			jsonPara = new JSONObject();
			jsonPara.put("param_key", "unauth_sign_hint_text");
			jsonPara.put("param_value", "抱歉,您未报名");
			jsonPara.put("param_name", "(1055)未授权签到提示语");
			jsonParas.add(jsonPara);
		}
		jsonObject.put("param_list", jsonParas);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if(result == 0) {
			confInfo.setConf_id(jsStr.getInt("conf_id"));
		}
		
		return new ResultDomain<>(result, confInfo);
	}
	
	private static void ParseParams(JSONArray arrParas, JSONArray arrTerms, Conference conference) {
		if(arrParas != null) {
			for(int i = 0; i < arrParas.size(); i++) {
				JSONObject jObject = arrParas.getJSONObject(i);
				String paramKey = jObject.getString("param_key");
	
				if("detect_mode".equals(paramKey)) {
					conference.setRecognittype(jObject.getString(PARAM_VALUE));
				} else if("need_face_compare".equals(paramKey)) {
					conference.setNeed_face_compare(jObject.getInt(PARAM_VALUE));
				} else if("sign_mode".equals(paramKey)) {
					conference.setSign_mode(jObject.getInt(PARAM_VALUE));
				} else if("auth_level".equals(paramKey)) {
					conference.setAuth_level(jObject.getInt(PARAM_VALUE));
				} else if("is_manual_pass".equals(paramKey)) {
					conference.setHand_allow(jObject.getInt(PARAM_VALUE));
				} else if("is_show_jl_logo".equals(paramKey)) {
					conference.setShow_logo(jObject.getInt(PARAM_VALUE));
				} else if("is_allow_add_company".equals(paramKey)) {
					conference.setAdd_company(jObject.getInt(PARAM_VALUE));
				} else if("live_reg_type".equals(paramKey)) {
					conference.setReg_type(jObject.getInt(PARAM_VALUE));
				} else if("register_url".equals(paramKey)) {
					conference.setRegister_url(jObject.getString(PARAM_VALUE));
				} else if("register_deadline".equals(paramKey)) {
					conference.setSign_deadline(jObject.getString(PARAM_VALUE));
				} else if("register_logo".equals(paramKey)) {
					conference.setRegister_logo(jObject.getString(PARAM_VALUE));
				} else if("is_show_sign_list".equals(paramKey)) {
					conference.setIs_show_sign_list(jObject.getInt(PARAM_VALUE));
				} else if("business_ctrl".equals(paramKey)) {
					conference.setBusiness_ctrl(jObject.getInt(PARAM_VALUE));
				}
			}
		}
		
		if(arrTerms != null && !arrTerms.isEmpty()) {
			List<Integer> termList = new ArrayList<>(arrTerms.size());
			for(int i = 0; i < arrTerms.size(); i++) {
				termList.add(arrTerms.getJSONObject(i).getInt("term_id"));
			}
			
			conference.setTerm_list(termList);
		}
	}
	
	public static ResultDomain<List<Conference>> ConferenceInfoQuery(User user, Conference conf, int term_id) {
		List<Conference> confList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "ConferenceInfoQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("conf_id", conf.getConf_id()); 	//为0表示全部会议
		jsonObject.put("term_id", term_id);				//为0表示全部终端
		jsonObject.put("revno", "");
		jsonObject.put("conf_num", -1);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int total_num = jsStr.getInt("total_num");
			int conf_num = jsStr.getInt("conf_num");
			
			result = conf_num;
			JSONArray arrRecords = null;
			if (conf_num > 0)
				arrRecords = jsStr.getJSONArray("conf_list");
			if (arrRecords != null && conf_num == arrRecords.size()) {
				confList = new ArrayList<>(conf_num);
				for (int i = 0; i < conf_num; i++) {
					JSONObject jstrRecord = arrRecords.getJSONObject(i);
					Conference conference = new Conference();
					conference.setConf_id(jstrRecord.getInt("conf_id"));
					conference.setStatus(jstrRecord.getInt("status"));
					conference.setTerm_num(jstrRecord.getInt("term_num"));
					conference.setFace_num(jstrRecord.getInt("face_num"));
					
					conference.setName(jstrRecord.getString("name"));
					conference.setStart_time(jstrRecord.getString("start_time"));
					conference.setEnd_time(jstrRecord.getString("end_time"));
					conference.setSign_time(jstrRecord.getString("sign_timespan"));
					conference.setLogo(jstrRecord.getString("logo"));
					conference.setAddr(jstrRecord.getString("addr"));
					
					int param_num = jstrRecord.getInt("param_num");
					int term_num = jstrRecord.getInt("term_num");
					JSONArray arrParas = null;
					JSONArray arrTerms = null;
					if(param_num > 0) arrParas = jstrRecord.getJSONArray("param_list");
					if(term_num > 0) arrTerms = jstrRecord.getJSONArray("term_list");
					ParseParams(arrParas, arrTerms, conference);

					confList.add(conference);
				}
			}
		}

		return new ResultDomain<>(result, confList);
	}
	
	//删除会议信息
	public static ResultDomain ConferenceInfoDelete(User user, Conference confInfo) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "ConferenceInfoDelete");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("conf_id", confInfo.getConf_id());
		
		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		
		return new ResultDomain<>(result);
	}
	
	//获取分组信息接口
	public static ResultDomain<List<Group>> GroupQuery(User user) {
		List<Group> groupList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "GroupQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("group_id", 0);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int rec_num = jsStr.getInt("rec_num");
			
			result = rec_num;
			JSONArray arrRecords = null;
			if (rec_num > 0)
				arrRecords = jsStr.getJSONArray("rec_list");
			if (arrRecords != null && rec_num == arrRecords.size()) {
				groupList = new ArrayList<>(rec_num);
				for (int i = 0; i < rec_num; i++) {
					JSONObject jstrRecord = arrRecords.getJSONObject(i);
					Group group = new Group();
					group.setGroup_id(jstrRecord.getInt("group_id"));
					group.setName(jstrRecord.getString("group"));
					groupList.add(group);
				}
			}
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, groupList);
	}
	
	//上报分组信息
	public static ResultDomain<Group> GroupUpload(User user, Group groupInfo) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "GroupUpload");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("attribute", 1);
		jsonObject.put("group_id", groupInfo.getGroup_id());
		jsonObject.put("group", groupInfo.getName());
		
		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if(result == 0 || result == 2) {	//如上报成功或分组存在
			groupInfo.setGroup_id(jsStr.getInt("group_id"));
		}
		
		return new ResultDomain<>(result, groupInfo);
	}
	
	//配置分组名单接口
	public static ResultDomain GroupFaceConfig(User user, Group groupInfo, List<GroupFace> faceList) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "GroupFaceConfig");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("group_id", groupInfo.getGroup_id());
		jsonObject.put("mode", groupInfo.getMode());
		jsonObject.put("face_num", faceList.size());
		JSONArray jsonFaces = new JSONArray();
		for (int i = 0; i < faceList.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("face_id", faceList.get(i).getFace_id());
			jsonObj.put("attribute", faceList.get(i).getAttribute());
			
			jsonFaces.add(i, jsonObj);
		}

		jsonObject.put("face_list", jsonFaces);
		
		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		return new ResultDomain<>(result);
	}
	
	//获取分组名单接口
	public static ResultDomain<List<GroupFace>> GroupFaceQuery(User user, Group groupInfo) {
		List<GroupFace> groupFaceList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "GroupFaceQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("group_id", groupInfo.getGroup_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int rec_num = jsStr.getInt("rec_num");
			
			result = rec_num;
			JSONArray arrRecords = null;
			if (rec_num > 0)
				arrRecords = jsStr.getJSONArray("rec_list");
			if (arrRecords != null && rec_num == arrRecords.size()) {
				groupFaceList = new ArrayList<>(rec_num);
				for (int i = 0; i < rec_num; i++) {
					JSONObject jstrRecord = arrRecords.getJSONObject(i);
					GroupFace groupFace = new GroupFace();
					groupFace.setGroup_id(jstrRecord.getInt("group_id"));
					groupFace.setFace_id(jstrRecord.getInt("face_id"));
					groupFace.setAttribute(jstrRecord.getInt("attribute"));
					groupFace.setCreate_time(jstrRecord.getString("create_time"));
					
					groupFaceList.add(groupFace);
				}
			}
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, groupFaceList);
	}
	
	//注销分组信息接口
	public static ResultDomain GroupDelete(User user, Group groupInfo) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "GroupDelete");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("group_id", groupInfo.getGroup_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		
		return new ResultDomain<>(result);
	}
	
	//---------------------------------------车辆管理-------------------------------------//
	//上报车辆信息
	public static ResultDomain CarInfoUpload(User user, CarInfo car) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "CarInfoUpload");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("plate_id", car.getPlate_id());
		jsonObject.put("model", car.getModel());
		jsonObject.put("color", car.getColor());
		jsonObject.put("plate_type", car.getPlate_type());
		jsonObject.put("attribute", car.getAttribute());
		jsonObject.put("owner_id", car.getOwner_id());
		jsonObject.put("remark", car.getRemark());
		jsonObject.put("expire_begin", car.getExpire_begin());
		jsonObject.put("expire_end", car.getExpire_end());

		if(car.getArea_list() != null && car.getArea_list().isEmpty() == false) {
			JSONArray jsonTerms = new JSONArray();
			for (int i = 0; i < car.getArea_list().size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("area_id", car.getArea_list().get(i));
				jsonTerms.add(i, jsonObj);
			}
			
			jsonObject.put("area_list", jsonTerms);
		}
		
		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		
		return new ResultDomain<>(result);
	}
	
	//获取车辆信息
	public static ResultDomain<List<CarInfo>> CarInfoQuery(User user, CarInfo queryInfo) {
		List<CarInfo> carList = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "CarInfoQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("owner_id", queryInfo.getOwner_id());
		jsonObject.put("attribute", queryInfo.getAttribute());
		jsonObject.put("plate_id", queryInfo.getPlate_id());
		jsonObject.put("plate_type", queryInfo.getPlate_type());
		jsonObject.put("revno", "");
		jsonObject.put("start", 0);
		jsonObject.put("car_num", -1);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");

		if (result == 0) {
			int total_num = jsStr.getInt("total_num");
			int car_num = jsStr.getInt("car_num");
			JSONArray arrCars = null;
			if (car_num > 0)
				arrCars = jsStr.getJSONArray("car_list");
			if (arrCars != null && car_num == arrCars.size()) {
				carList = new ArrayList<>(car_num);
				for (int i = 0; i < car_num; i++) {
					JSONObject jstrCar = JSONObject.fromObject(arrCars.getJSONObject(i));
					CarInfo info = new CarInfo();

					info.setPlate_id(jstrCar.getString("plate_id"));
					info.setModel(jstrCar.getString("model"));
					info.setColor(jstrCar.getString("color"));
					info.setPlate_type(jstrCar.getInt("plate_type"));
					info.setAttribute(jstrCar.getInt("attribute"));
					info.setOwner_id(jstrCar.getInt("owner_id"));
					info.setOwner_name(jstrCar.getString("owner_name"));
					info.setTel(jstrCar.getString("tel"));
					info.setStatus(jstrCar.getInt("status"));
					info.setCreate_time(jstrCar.getString("create_time"));
					info.setUpdate_time(jstrCar.getString("update_time"));
					info.setExpire_begin(jstrCar.getString("start_time"));
					info.setExpire_end(jstrCar.getString("end_time"));
					info.setRevno(jstrCar.getString("revno"));

					JSONArray arrAreas = jstrCar.getJSONArray("area_list");
					if(arrAreas != null) {
						List<Integer> arealist = new ArrayList<>(arrAreas.size());
						for(int j = 0; j < arrAreas.size(); j++) {
							arealist.add(arrAreas.getJSONObject(j).getInt("area_id"));
						}
						info.setArea_list(arealist);
					}

					carList.add(info);
				}
			}

			result = total_num;
		} else {
			result = -result;
		}

		return new ResultDomain<>(result, carList);
	}
	
	//注销车辆信息
	public static ResultDomain CarInfoDelete(User user, CarInfo car) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "CarInfoDelete");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("owner_id", car.getOwner_id());
		jsonObject.put("attribute", car.getAttribute());
		jsonObject.put("plate_id", car.getPlate_id());

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		
		return new ResultDomain<>(result);
	}
	
	/**
	 * 查询数据状态
	 * 
	 * @param user
	 *            当前用户
	 * @param term_id
	 *            终端id,为0表示查询全部
	 * @return 修订号列表
	 */
	public static ResultDomain<Map<String, String>> DataStatusQuery(User user, int term_id) {
		Map<String, String> face_versions = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "DataStatusQuery");
		jsonObject.put("version", 0x100);
		jsonObject.put("face_library_id", user.getFace_library_id());
		jsonObject.put("term_id", term_id);

		JSONObject jsStr = DataUtil.SendRequest(jsonObject);
		int result = jsStr.getInt("result");
		if (result == 0) {
			JSONArray arrVersions = jsStr.getJSONArray("face_versions");
			if (!arrVersions.isEmpty()) {
				face_versions = new HashMap<>(arrVersions.size());
				for (int i = 0; i < arrVersions.size(); i++) {
					JSONObject jstrVersion = arrVersions.getJSONObject(i);
					face_versions.put(jstrVersion.getString("version"), jstrVersion.getString("revno"));
				}
			}
		}

		return new ResultDomain<>(result, face_versions);
	}

	/**
	 * 人脸检测结果
	 * 
	 * @author Tong 人脸检测结果
	 */
	public static class FDResult {
		private int left;
		private int top;
		private int right;
		private int bottom;
		private int roll;
		private int yaw;
		private int pitch;
		private int quality; // 质量取值范围0-100
		private int clarity; // 清晰度取值范围0-100
		private float confidence; // 置信度取值范围0-1
		private String feature; // 特征值转base64
		private String feature_ver;	//特征值版本
		private int face_width;
		private int face_height;
		private String face_data; // 裁剪扩图后的照片数据转base64

		public int getLeft() {
			return left;
		}

		public void setLeft(int left) {
			this.left = left;
		}

		public int getTop() {
			return top;
		}

		public void setTop(int top) {
			this.top = top;
		}

		public int getRight() {
			return right;
		}

		public void setRight(int right) {
			this.right = right;
		}

		public int getBottom() {
			return bottom;
		}

		public void setBottom(int bottom) {
			this.bottom = bottom;
		}

		public int getRoll() {
			return roll;
		}

		public void setRoll(int roll) {
			this.roll = roll;
		}

		public int getYaw() {
			return yaw;
		}

		public void setYaw(int yaw) {
			this.yaw = yaw;
		}

		public int getPitch() {
			return pitch;
		}

		public void setPitch(int pitch) {
			this.pitch = pitch;
		}

		public int getQuality() {
			return quality;
		}

		public void setQuality(int quality) {
			this.quality = quality;
		}

		public int getClarity() {
			return clarity;
		}

		public void setClarity(int clarity) {
			this.clarity = clarity;
		}

		public float getConfidence() {
			return confidence;
		}

		public void setConfidence(float confidence) {
			this.confidence = confidence;
		}

		public String getFeature() {
			return feature;
		}

		public void setFeature(String feature) {
			this.feature = feature;
		}
		
		public String getFeature_ver() {
			return feature_ver;
		}

		public void setFeature_ver(String feature_ver) {
			this.feature_ver = feature_ver;
		}

		public int getFace_width() {
			return face_width;
		}

		public void setFace_width(int face_width) {
			this.face_width = face_width;
		}

		public int getFace_height() {
			return face_height;
		}

		public void setFace_height(int face_height) {
			this.face_height = face_height;
		}

		public String getFace_data() {
			return face_data;
		}

		public void setFace_data(String face_data) {
			this.face_data = face_data;
		}
	}
	
	public static class FMResult {
		private int face_library_id;
		private int face_id;
		private int depart_id;
		private String name;
		private double score;

		public int getFace_library_id() {
			return face_library_id;
		}

		public void setFace_library_id(int face_library_id) {
			this.face_library_id = face_library_id;
		}

		public int getFace_id() {
			return face_id;
		}

		public void setFace_id(int face_id) {
			this.face_id = face_id;
		}

		public int getDepart_id() {
			return depart_id;
		}

		public void setDepart_id(int depart_id) {
			this.depart_id = depart_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getScore() {
			return score;
		}

		public void setScore(double score) {
			this.score = score;
		}
	}

	/**
	 * 参数结构定义
	 * 
	 * @author Tong
	 *
	 */
	public static class ParamConfigInfo {
		// 参数主键
		private String param_key;
		// 参数值
		private String param_value;
		// 参数名称，界面显示时使用
		private String param_name;
		
		private String param_sheet;
		private String type;
		private boolean display_controlled;
		private List<ParamValue> set;
		private List<SetValue> sets;	//json格式的参数集合

		public String getParam_key() {
			return param_key;
		}

		public void setParam_key(String param_key) {
			this.param_key = param_key;
		}

		public String getParam_value() {
			return param_value;
		}

		public void setParam_value(String param_value) {
			this.param_value = param_value;
		}

		public String getParam_name() {
			return param_name;
		}

		public void setParam_name(String param_name) {
			this.param_name = param_name;
		}

		public String getParam_sheet() {
			return param_sheet;
		}

		public void setParam_sheet(String param_sheet) {
			this.param_sheet = param_sheet;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public boolean isDisplay_controlled() {
			return display_controlled;
		}

		public void setDisplay_controlled(boolean display_controlled) {
			this.display_controlled = display_controlled;
		}

		public List<ParamValue> getSet() {
			return set;
		}

		public void setSet(List<ParamValue> set) {
			this.set = set;
		}

		public List<SetValue> getSets() {
			return sets;
		}

		public void setSets(List<SetValue> sets) {
			this.sets = sets;
		}
	}
	
	public static class ParamValue {
		private String dispaly;
		private String value;
		private String control;
		
		public String getDispaly() {
			return dispaly;
		}

		public void setDispaly(String dispaly) {
			this.dispaly = dispaly;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getControl() {
			return control;
		}

		public void setControl(String control) {
			this.control = control;
		}
	}
	
	public static class SetValue {
		// 参数主键
		private String key;
		// 参数名称，界面显示时使用
		private String name;
		private String type;
		private String value_type;
		private List<ParamValue> set;
		
		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<ParamValue> getSet() {
			return set;
		}

		public void setSet(List<ParamValue> set) {
			this.set = set;
		}

		public String getValue_type() {
			return value_type;
		}

		public void setValue_type(String value_type) {
			this.value_type = value_type;
		}

	}
	
	public static class HintConfigInfo {
		// 参数主键
		private String hint_key;
		// 参数值
		private String hint_sound_text;
		private String hint_text;
		// 参数名称，界面显示时使用
		private String hint_name;

		public String getHint_key() {
			return hint_key;
		}

		public void setHint_key(String hint_key) {
			this.hint_key = hint_key;
		}

		public String getHint_sound_text() {
			return hint_sound_text;
		}

		public void setHint_sound_text(String hint_sound_text) {
			this.hint_sound_text = hint_sound_text;
		}

		public String getHint_text() {
			return hint_text;
		}

		public void setHint_text(String hint_text) {
			this.hint_text = hint_text;
		}

		public String getHint_name() {
			return hint_name;
		}

		public void setHint_name(String hint_name) {
			this.hint_name = hint_name;
		}
	}
}
