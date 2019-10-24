package com.jinglun.guard.systemManage.domain;

public class Company {
    private int face_library_id;                //人脸库ID
    private int net_library_id;                 //网络比对人脸库ID
    private String name;                       	//人脸库名称
    private int net_mode;                       //是否支持网络比对
    private String description;                 //描述信息
    private String legal_person;                //法人代表
    private String company_tax;                 //纳税人识别号
    private String company_no;                  //工商注册号
    private String trade;                       //行业
    private String contact_person;              //联系人
    private String tel;                         //联系电话
    private String email;                       //联系email
    private String addr;                        //地址     
    private String englishname;                 //英文名称
    private String pinyin_name;                 //拼音名称
    private int business_id;                    //业务类型

    
    private String loginname;
	private String rolename;
    private String passwd;
    private int userid;
    private int userfaceid;
    
	public int getUserfaceid() {
		return userfaceid;
	}

	public void setUserfaceid(int userfaceid) {
		this.userfaceid = userfaceid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	 public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	public int getFace_library_id() {
		return face_library_id;
	}

	public void setFace_library_id(int face_library_id) {
		this.face_library_id = face_library_id;
	}

	public int getNet_library_id() {
		return net_library_id;
	}

	public void setNet_library_id(int net_library_id) {
		this.net_library_id = net_library_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNet_mode() {
		return net_mode;
	}

	public void setNet_mode(int net_mode) {
		this.net_mode = net_mode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLegal_person() {
		return legal_person;
	}

	public void setLegal_person(String legal_person) {
		this.legal_person = legal_person;
	}

	public String getCompany_tax() {
		return company_tax;
	}

	public void setCompany_tax(String company_tax) {
		this.company_tax = company_tax;
	}

	public String getCompany_no() {
		return company_no;
	}

	public void setCompany_no(String company_no) {
		this.company_no = company_no;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getContact_person() {
		return contact_person;
	}

	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getEnglishname() {
		return englishname;
	}

	public void setEnglishname(String englishname) {
		this.englishname = englishname;
	}

	public String getPinyin_name() {
		return pinyin_name;
	}

	public void setPinyin_name(String pinyin_name) {
		this.pinyin_name = pinyin_name;
	}

	public int getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(int business_id) {
		this.business_id = business_id;
	}
}
