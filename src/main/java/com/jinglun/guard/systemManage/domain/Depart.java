package com.jinglun.guard.systemManage.domain;

public class Depart {
	private int face_library_id;  //人脸库ID
	private int parent_depart_id;  //父部门ID
	private int depart_id;  //部门ID
    private String depart_name;  //部门名称 --2018/4/25从33修改为128
    private String revno;  //更新的版本号
    private int del_flag; //删除标志

	public int getFace_library_id() {
		return face_library_id;
	}

	public void setFace_library_id(int face_library_id) {
		this.face_library_id = face_library_id;
	}

	public int getParent_depart_id() {
		return parent_depart_id;
	}

	public void setParent_depart_id(int parent_depart_id) {
		this.parent_depart_id = parent_depart_id;
	}

	public int getDepart_id() {
		return depart_id;
	}

	public void setDepart_id(int depart_id) {
		this.depart_id = depart_id;
	}

	public String getDepart_name() {
		return depart_name;
	}

	public void setDepart_name(String depart_name) {
		this.depart_name = depart_name;
	}

	public String getRevno() {
		return revno;
	}

	public void setRevno(String revno) {
		this.revno = revno;
	}

	public int getDel_flag() {
		return del_flag;
	}

	public void setDel_flag(int del_flag) {
		this.del_flag = del_flag;
	}
}
