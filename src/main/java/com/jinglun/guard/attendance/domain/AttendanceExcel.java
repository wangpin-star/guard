package com.jinglun.guard.attendance.domain;


public class AttendanceExcel {
	private int detail;
	private int exist;
	private String departid;
	private String first_time;
	private String last_time;
	private int date;
	private int fristtime;
	private int depart;
	private int endtime;
	private int name;
	private int fristplace;
	private int eno;
	private int endplace;
	private String face_id;
	private String idcard; // 身份证号
	
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public int getDetail() {
		return detail;
	}
	public void setDetail(int detail) {
		this.detail = detail;
	}
	public int getExist() {
		return exist;
	}
	public void setExist(int exist) {
		this.exist = exist;
	}
	
	
	public String getDepartid() {
		return departid;
	}
	public void setDepartid(String departid) {
		this.departid = departid;
	}
	public String getFirst_time() {
		return first_time;
	}
	public void setFirst_time(String first_time) {
		this.first_time = first_time;
	}
	public String getLast_time() {
		return last_time;
	}
	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public int getFristtime() {
		return fristtime;
	}
	public void setFristtime(int fristtime) {
		this.fristtime = fristtime;
	}
	public int getDepart() {
		return depart;
	}
	public void setDepart(int depart) {
		this.depart = depart;
	}
	public int getEndtime() {
		return endtime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	public int getName() {
		return name;
	}
	public void setName(int name) {
		this.name = name;
	}
	public int getFristplace() {
		return fristplace;
	}
	public void setFristplace(int fristplace) {
		this.fristplace = fristplace;
	}
	public int getEno() {
		return eno;
	}
	public void setEno(int eno) {
		this.eno = eno;
	}
	public int getEndplace() {
		return endplace;
	}
	public void setEndplace(int endplace) {
		this.endplace = endplace;
	}
	public String getFace_id() {
		return face_id;
	}
	public void setFace_id(String face_id) {
		this.face_id = face_id;
	}
	
	
}
