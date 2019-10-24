package com.jinglun.guard.visitorManage.domain;

/**
 * 回填访客详情界面
 * @author huanggang
 *
 */
public class VisitorDetailShow {
	
	private String visitor_name;//访客姓名
	private int visitor_sex;//访客性别
	private String visitor_idcard;//访客身份证号
	private String visitor_tel;//访客手机号
	private String visitor_address;//访客家庭地址
	private String visitor_company;//访客公司名
	private String visitor_postion;//访客职位
	private String visitor_content;//来访事由
	private String visitor_time;//来访时间
	private String visitor_departname;//访问部门
	private String visitor_photo;//访客头像
	
	private String employee_name;//被访人
	private String employee_tel;//被访人手机号
	private String term_permit;//权限设备
	private String term_permitmode;//权限设备类型
	private int termlength;//权限名称个数
	private int status;//为0时详情页面显示编辑按钮
	public String getVisitor_name() {
		return visitor_name;
	}
	public void setVisitor_name(String visitor_name) {
		this.visitor_name = visitor_name;
	}
	
	public String getVisitor_idcard() {
		return visitor_idcard;
	}
	public void setVisitor_idcard(String visitor_idcard) {
		this.visitor_idcard = visitor_idcard;
	}
	public String getVisitor_tel() {
		return visitor_tel;
	}
	public void setVisitor_tel(String visitor_tel) {
		this.visitor_tel = visitor_tel;
	}
	public String getVisitor_address() {
		return visitor_address;
	}
	public void setVisitor_address(String visitor_address) {
		this.visitor_address = visitor_address;
	}
	public String getVisitor_company() {
		return visitor_company;
	}
	public void setVisitor_company(String visitor_company) {
		this.visitor_company = visitor_company;
	}
	public String getVisitor_postion() {
		return visitor_postion;
	}
	public void setVisitor_postion(String visitor_postion) {
		this.visitor_postion = visitor_postion;
	}
	public String getVisitor_content() {
		return visitor_content;
	}
	public void setVisitor_content(String visitor_content) {
		this.visitor_content = visitor_content;
	}
	public String getVisitor_time() {
		return visitor_time;
	}
	public void setVisitor_time(String visitor_time) {
		this.visitor_time = visitor_time;
	}
	public String getVisitor_departname() {
		return visitor_departname;
	}
	public void setVisitor_departname(String visitor_departname) {
		this.visitor_departname = visitor_departname;
	}
	public String getVisitor_photo() {
		return visitor_photo;
	}
	public void setVisitor_photo(String visitor_photo) {
		this.visitor_photo = visitor_photo;
	}
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getEmployee_tel() {
		return employee_tel;
	}
	public void setEmployee_tel(String employee_tel) {
		this.employee_tel = employee_tel;
	}
	public String getTerm_permit() {
		return term_permit;
	}
	public void setTerm_permit(String term_permit) {
		this.term_permit = term_permit;
	}
	public int getVisitor_sex() {
		return visitor_sex;
	}
	public void setVisitor_sex(int visitor_sex) {
		this.visitor_sex = visitor_sex;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTermlength() {
		return termlength;
	}
	public void setTermlength(int termlength) {
		this.termlength = termlength;
	}
	public String getTerm_permitmode() {
		return term_permitmode;
	}
	public void setTerm_permitmode(String term_permitmode) {
		this.term_permitmode = term_permitmode;
	}
	
}
