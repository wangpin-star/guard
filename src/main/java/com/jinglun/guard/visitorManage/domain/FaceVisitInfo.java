package com.jinglun.guard.visitorManage.domain;

import java.util.List;

/**访客登记详情
 * @author Tong 
 */
public class FaceVisitInfo {
	private int face_library_id; // 人脸库ID
	private int attribute; // 0-员工 1-访客
	private int face_id; // 人脸ID
	private int depart_id; // 被访人员部门ID
	private int term_id; // 终端ID
	private int employee_id;
	private int rec_id; // 来访日志ID
	private int book_id; //预约id，不为0时为预约
	private int bat_id; // 批量访客登记ID
	private int status; // 访客状态
	private int visit_num; // 随访人数
	private int sex;
	private int is_auth;
	private int del_flag;
	private int reason_id;
	private int mode;

	private String name;
	private String idcard;
	private String employee; // 被访人员
	private String create_time;
	private String visit_time; // 来访时间
	private String expire_time; // 有效期限
	private String current_time;//系统当前时间
	private String content; // 来访事由
	private String conf_id;
	private String business;
	private String tel;
	private String company;
	private String revno;
	
	private List<Integer> terms;		//终端权限

	public String getConf_id() {
		return conf_id;
	}

	public void setConf_id(String conf_id) {
		this.conf_id = conf_id;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public int getFace_library_id() {
		return face_library_id;
	}

	public void setFace_library_id(int face_library_id) {
		this.face_library_id = face_library_id;
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

	public int getDepart_id() {
		return depart_id;
	}

	public void setDepart_id(int depart_id) {
		this.depart_id = depart_id;
	}

	public int getTerm_id() {
		return term_id;
	}

	public void setTerm_id(int term_id) {
		this.term_id = term_id;
	}

	public int getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	public int getRec_id() {
		return rec_id;
	}

	public void setRec_id(int rec_id) {
		this.rec_id = rec_id;
	}

	public int getBat_id() {
		return bat_id;
	}

	public void setBat_id(int bat_id) {
		this.bat_id = bat_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getVisit_num() {
		return visit_num;
	}

	public void setVisit_num(int visit_num) {
		this.visit_num = visit_num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getBook_id() {
		return book_id;
	}

	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getIs_auth() {
		return is_auth;
	}

	public void setIs_auth(int is_auth) {
		this.is_auth = is_auth;
	}

	public int getDel_flag() {
		return del_flag;
	}

	public void setDel_flag(int del_flag) {
		this.del_flag = del_flag;
	}

	public int getReason_id() {
		return reason_id;
	}

	public void setReason_id(int reason_id) {
		this.reason_id = reason_id;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getRevno() {
		return revno;
	}

	public void setRevno(String revno) {
		this.revno = revno;
	}

	public List<Integer> getTerms() {
		return terms;
	}

	public void setTerms(List<Integer> terms) {
		this.terms = terms;
	}

	public String getCurrent_time() {
		return current_time;
	}

	public void setCurrent_time(String current_time) {
		this.current_time = current_time;
	}
}