package com.jinglun.guard.privilege.domain;

import java.util.List;

public class Role_Permission {
	private String name;
	private String descript;
	private List<Integer> menuList;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public List<Integer> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<Integer> menuList) {
		this.menuList = menuList;
	}
}
