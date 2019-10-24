package com.jinglun.guard.conferenceManage.domain;

import java.util.List;

public class Conference {
	private int face_library_id;
	private int conf_id;
	private int parent_conf_id;
	private int term_layout;
	private int need_face_compare;
	private int status;
	private int term_num;
	private int auth_level;
	private int sign_mode;          //签到模式
	private int show_logo;          //显示精伦logo
	private int add_company;        //新增公司
	private int reg_type;           //现场注册方式
	private int hand_allow;         //手动通过
	private int face_num;           //报名人数
	private int is_show_sign_list;	//是否显示签到列表
	private int business_ctrl;		//初始业务控制
	
	private List<Integer> term_list;

	private String name;
	private String addr;
	private String start_time;            //会议开始时间
    private String end_time;              //会议结束时间
    private String sign_deadline;         //会议报名截止时间
    private String sign_time;            //会议签到时段
    private String logo;            //logo base64
    private String background;      //背景图 base64
    private String register_logo;        //报名logo
    private String register_url;        //报名链接
    private String recognittype;              //识别方式
    private String revno;
    
	public int getFace_library_id() {
		return face_library_id;
	}

	public void setFace_library_id(int face_library_id) {
		this.face_library_id = face_library_id;
	}

	public int getConf_id() {
		return conf_id;
	}

	public void setConf_id(int conf_id) {
		this.conf_id = conf_id;
	}

	public int getParent_conf_id() {
		return parent_conf_id;
	}

	public void setParent_conf_id(int parent_conf_id) {
		this.parent_conf_id = parent_conf_id;
	}

	public int getTerm_layout() {
		return term_layout;
	}

	public void setTerm_layout(int term_layout) {
		this.term_layout = term_layout;
	}

	public int getNeed_face_compare() {
		return need_face_compare;
	}

	public void setNeed_face_compare(int need_face_compare) {
		this.need_face_compare = need_face_compare;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTerm_num() {
		return term_num;
	}

	public void setTerm_num(int term_num) {
		this.term_num = term_num;
	}

	public int getAuth_level() {
		return auth_level;
	}

	public void setAuth_level(int auth_level) {
		this.auth_level = auth_level;
	}

	public int getSign_mode() {
		return sign_mode;
	}

	public void setSign_mode(int sign_mode) {
		this.sign_mode = sign_mode;
	}

	public int getShow_logo() {
		return show_logo;
	}

	public void setShow_logo(int show_logo) {
		this.show_logo = show_logo;
	}

	public int getAdd_company() {
		return add_company;
	}

	public void setAdd_company(int add_company) {
		this.add_company = add_company;
	}

	public int getReg_type() {
		return reg_type;
	}

	public void setReg_type(int reg_type) {
		this.reg_type = reg_type;
	}

	public int getHand_allow() {
		return hand_allow;
	}

	public void setHand_allow(int hand_allow) {
		this.hand_allow = hand_allow;
	}

	public int getFace_num() {
		return face_num;
	}

	public void setFace_num(int face_num) {
		this.face_num = face_num;
	}

	public List<Integer> getTerm_list() {
		return term_list;
	}

	public void setTerm_list(List<Integer> term_list) {
		this.term_list = term_list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
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

	public String getSign_deadline() {
		return sign_deadline;
	}

	public void setSign_deadline(String sign_deadline) {
		this.sign_deadline = sign_deadline;
	}

	public String getSign_time() {
		return sign_time;
	}

	public void setSign_time(String sign_time) {
		this.sign_time = sign_time;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getRegister_logo() {
		return register_logo;
	}

	public void setRegister_logo(String register_logo) {
		this.register_logo = register_logo;
	}

	public String getRegister_url() {
		return register_url;
	}

	public void setRegister_url(String register_url) {
		this.register_url = register_url;
	}

	public String getRecognittype() {
		return recognittype;
	}

	public void setRecognittype(String recognittype) {
		this.recognittype = recognittype;
	}

	public String getRevno() {
		return revno;
	}

	public void setRevno(String revno) {
		this.revno = revno;
	}

	public int getIs_show_sign_list() {
		return is_show_sign_list;
	}

	public void setIs_show_sign_list(int is_show_sign_list) {
		this.is_show_sign_list = is_show_sign_list;
	}

	public int getBusiness_ctrl() {
		return business_ctrl;
	}

	public void setBusiness_ctrl(int business_ctrl) {
		this.business_ctrl = business_ctrl;
	}
}
