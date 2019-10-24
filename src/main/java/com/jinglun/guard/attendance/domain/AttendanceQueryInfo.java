package com.jinglun.guard.attendance.domain;

import java.util.List;

/** 查询考勤信息
 * @author Tong
 *
 */
public class AttendanceQueryInfo {
	private int face_library_id;    //人脸库ID
	private int start;
	private int attribute; //0-员工 1-访客
	private int face_id;  //人脸ID
	private int depart_num;   //部门数
	private int term_id;  //终端ID
	private int mode;     //查询全部或有考勤记录的信息,0：所有,1：返回有考勤数据的记录,2：返回正常考勤数据的记录,3：返回异常考勤数据
	private int order;	  //0：最早日期优先,1：最近日期优先
	
	private String query_info;
	private String start_time;  //来访时间
	private String end_time;  //有效期限
	
	private List<Integer> departs;

	public int getFace_library_id() {
		return face_library_id;
	}

	public void setFace_library_id(int face_library_id) {
		this.face_library_id = face_library_id;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getAttribute() {
		return attribute;
	}

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	public int getFace_id() {
		return face_id;
	}

	public void setFace_id(int face_id) {
		this.face_id = face_id;
	}

	public int getDepart_num() {
		return depart_num;
	}

	public void setDepart_num(int depart_num) {
		this.depart_num = depart_num;
	}

	public int getTerm_id() {
		return term_id;
	}

	public void setTerm_id(int term_id) {
		this.term_id = term_id;
	}

	public String getQuery_info() {
		return query_info;
	}

	public void setQuery_info(String query_info) {
		this.query_info = query_info;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public List<Integer> getDeparts() {
		return departs;
	}

	public void setDeparts(List<Integer> departs) {
		this.departs = departs;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
