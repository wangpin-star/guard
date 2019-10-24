package com.jinglun.guard.privilege.domain;

public class Menu {
	private int menu_id;
	private int parent_id;
	private int order;
	private String name;
	private String remark;
	
	public int getMenu_id() {
		return menu_id;
	}
	
	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}
	
	public int getParent_id() {
		return parent_id;
	}
	
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	
	public int getOrder() {
		return order;
	}
	
	public void setOrder(int order) {
		this.order = order;
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
}