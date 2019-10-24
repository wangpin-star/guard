package com.jinglun.guard.user.domain;

import java.io.Serializable;
import java.util.List;

import com.jinglun.guard.privilege.domain.Menu;
import com.jinglun.guard.privilege.domain.Role;

/**
 * 
 * @author wangxiwei
 *
 */
public class User{
	/**
	 * 
	 */
	
	private int user_id;  			//用户ID
	private int face_library_id;    //人脸库ID
	private int business_id;        //业务类型
	private int net_library_id;
	private int face_id;  			//对应的员工ID
	
	private int user_function; 		//用户权限
	private int disable_role;
	private int password_status;	// 0-正常；1-初始密码；

	private String user_name;
	private String user_password;
	private String new_password;
	private String company_name;
	private String create_time;
    private String update_time;
    private String revno;
    private String empname;
    
  public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}
	//部门名称
  	private List<String> depart_name;
  	
	public List<String> getDepart_name() {
		return depart_name;
	}

	public void setDepart_name(List<String> depart_name) {
		this.depart_name = depart_name;
	}
	private List<Integer> termList;
	
	private List<Integer> departList;
	
	private List<Menu> menuList;
	
	private List<Role> roleList;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getFace_library_id() {
		return face_library_id;
	}

	public void setFace_library_id(int face_library_id) {
		this.face_library_id = face_library_id;
	}

	public int getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(int business_id) {
		this.business_id = business_id;
	}

	public int getNet_library_id() {
		return net_library_id;
	}

	public void setNet_library_id(int net_library_id) {
		this.net_library_id = net_library_id;
	}

	public int getFace_id() {
		return face_id;
	}

	public void setFace_id(int face_id) {
		this.face_id = face_id;
	}
	
	public int getUser_function() {
		return user_function;
	}

	public void setUser_function(int user_function) {
		this.user_function = user_function;
	}

	public int getDisable_role() {
		return disable_role;
	}

	public void setDisable_role(int disable_role) {
		this.disable_role = disable_role;
	}

	public int getPassword_status() {
		return password_status;
	}

	public void setPassword_status(int password_status) {
		this.password_status = password_status;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	
	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}
	
	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getRevno() {
		return revno;
	}

	public void setRevno(String revno) {
		this.revno = revno;
	}

	public List<Integer> getTermList() {
		return termList;
	}

	public void setTermList(List<Integer> termList) {
		this.termList = termList;
	}

	public List<Integer> getDepartList() {
		return departList;
	}

	public void setDepartList(List<Integer> departList) {
		this.departList = departList;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}
	
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
}
