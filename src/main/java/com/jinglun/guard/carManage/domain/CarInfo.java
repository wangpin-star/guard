package com.jinglun.guard.carManage.domain;

import java.util.List;

public class CarInfo {
	private String plate_id;
	private String model;
	private String color;
	private int plate_type;
	private int attribute;
	private int owner_id;
	private String owner_name;
	private String tel;
	private String remark;
	private String create_time;
	private String update_time;
	private String expire_begin;
	private String expire_end;
	private String revno;
	private List<Integer> area_list;
	private int status;

	public String getPlate_id() {
		return plate_id;
	}

	public void setPlate_id(String plate_id) {
		this.plate_id = plate_id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getPlate_type() {
		return plate_type;
	}

	public void setPlate_type(int plate_type) {
		this.plate_type = plate_type;
	}

	public int getAttribute() {
		return attribute;
	}

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	public int getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExpire_begin() {
		return expire_begin;
	}

	public void setExpire_begin(String expire_begin) {
		this.expire_begin = expire_begin;
	}

	public String getExpire_end() {
		return expire_end;
	}

	public void setExpire_end(String expire_end) {
		this.expire_end = expire_end;
	}

	public List<Integer> getArea_list() {
		return area_list;
	}

	public void setArea_list(List<Integer> area_list) {
		this.area_list = area_list;
	}

	public String getOwner_name() {
		return owner_name;
	}

	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
