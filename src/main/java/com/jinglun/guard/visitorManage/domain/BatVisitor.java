package com.jinglun.guard.visitorManage.domain;

import java.util.List;

public class BatVisitor {
	private int face_library_id;                  //人脸库ID
	private int bat_id;                           //预约ID
	private int employee_id;                      //被访人员ID
	private int reason_id;                        //来访原因
	private int term_num;                         //终端数量
	private int status;
    
	
	private String title;                       //来访标题
	private String content;                      //来访事由
	private String visit_time;                  //来访时间
	private String expire_time;                 //有效期限
	private String create_time;                 //创建时间
	private String employee_name;
    

	private List<Integer> term_list;                    //终端ID数组

	public int getFace_library_id() {
		return face_library_id;
	}

	public void setFace_library_id(int face_library_id) {
		this.face_library_id = face_library_id;
	}

	public int getBat_id() {
		return bat_id;
	}

	public void setBat_id(int bat_id) {
		this.bat_id = bat_id;
	}

	public int getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	public int getReason_id() {
		return reason_id;
	}

	public void setReason_id(int reason_id) {
		this.reason_id = reason_id;
	}

	public int getTerm_num() {
		return term_num;
	}

	public void setTerm_num(int term_num) {
		this.term_num = term_num;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVisit_time() {
		return visit_time;
	}

	public void setVisit_time(String visit_time) {
		this.visit_time = visit_time;
	}

	public String getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public List<Integer> getTerm_list() {
		return term_list;
	}

	public void setTerm_list(List<Integer> term_list) {
		this.term_list = term_list;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
}