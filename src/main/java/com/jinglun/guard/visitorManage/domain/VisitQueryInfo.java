package com.jinglun.guard.visitorManage.domain;

/**访客来访信息
 * @author Tong
 */
public class VisitQueryInfo {
	private int face_library_id; // 人脸库ID
	private int attribute; // 0-员工 1-访客
	private int face_id; // 人脸ID
	private int book_id; // 预约记录id
	private int rec_id; // 访客记录id
	private int bat_id; // 批量登记id，为0则是个人来访
	private int depart_id; // 被访人员部门ID
	private int visit_num; // 来访人数
	private int status; // 状态 0-预约 1-在访 2-离开
	private String statustr; // 状态 0-预约 1-在访 2-离开
	private int mode; // 类型，0-全部，1-个人，2-批量
	private int employee_id; // 被访人id
	private int reason_id;
	private int pic_num;	//比对照片数量
	
	private String bat_title;//批量名称
	private String employee; // 被访人员
	private String name;
	private String content;
	private String visit_time; // 来访时间
	private String expire_time; // 有效期限
	private String query_info; // 查询信息

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

	public int getBook_id() {
		return book_id;
	}

	public void setBook_id(int book_id) {
		this.book_id = book_id;
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

	public int getDepart_id() {
		return depart_id;
	}

	public void setDepart_id(int depart_id) {
		this.depart_id = depart_id;
	}
	
	public int getPic_num() {
		return pic_num;
	}

	public void setPic_num(int pic_num) {
		this.pic_num = pic_num;
	}

	public int getVisit_num() {
		return visit_num;
	}

	public void setVisit_num(int visit_num) {
		this.visit_num = visit_num;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getQuery_info() {
		return query_info;
	}

	public void setQuery_info(String query_info) {
		this.query_info = query_info;
	}

	public String getStatustr() {
		return statustr;
	}

	public void setStatustr(String statustr) {
		this.statustr = statustr;
	}
	
	public String getBat_title() {
		return bat_title;
	}

	public void setBat_title(String bat_title) {
		this.bat_title = bat_title;
	}

	public int getReason_id() {
		return reason_id;
	}

	public void setReason_id(int reason_id) {
		this.reason_id = reason_id;
	}
}