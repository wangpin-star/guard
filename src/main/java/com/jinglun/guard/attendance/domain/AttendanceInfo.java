package com.jinglun.guard.attendance.domain;

/** 考勤记录
 * @author Tong
 *
 */
public class AttendanceInfo {
	private int face_id;
	private int first_term_id;
	private String first_term_name;
	private int last_term_id;
	private String last_term_name;
	

	private String name;
	private String empno;
	private String date;
	private String first_time;
	private String last_time;
	private String faceinfoname;
	private int departid;
	private String depart;
	
	
	public int getDepartid() {
		return departid;
	}

	public void setDepartid(int departid) {
		this.departid = departid;
	}

	public String getFirst_term_name() {
		return first_term_name;
	}

	public void setFirst_term_name(String first_term_name) {
		this.first_term_name = first_term_name;
	}

	public String getLast_term_name() {
		return last_term_name;
	}

	public void setLast_term_name(String last_term_name) {
		this.last_term_name = last_term_name;
	}


	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public String getFaceinfoname() {
		return faceinfoname;
	}

	public void setFaceinfoname(String faceinfoname) {
		this.faceinfoname = faceinfoname;
	}

	public int getFace_id() {
		return face_id;
	}

	public void setFace_id(int face_id) {
		this.face_id = face_id;
	}

	public int getFirst_term_id() {
		return first_term_id;
	}

	public void setFirst_term_id(int first_term_id) {
		this.first_term_id = first_term_id;
	}

	public int getLast_term_id() {
		return last_term_id;
	}

	public void setLast_term_id(int last_term_id) {
		this.last_term_id = last_term_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmpno() {
		return empno;
	}

	public void setEmpno(String empno) {
		this.empno = empno;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

}