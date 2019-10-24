package com.jinglun.guard.privilege.domain;

import java.util.List;

public class Role {
	private int role_id;
	private String name;
	private String remark;
	private String create_time;
	private String update_time;
	private List<Menu> menu_list;

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public List<Menu> getMenu_list() {
		return menu_list;
	}

	public void setMenu_list(List<Menu> menu_list) {
		this.menu_list = menu_list;
	}
}