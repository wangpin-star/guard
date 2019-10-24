package com.jinglun.guard.devicemanage.domain;

import java.util.List;

/**
 * 
 * @author huanggang
 * 设备管理类
 */
public class Device {
	//终端ID  由管理平台分配
	private int term_id;
	
	//人脸库ID
	private int face_library_id;  
	
	//部门ID
	private List<Integer> depart_id;
	
	//部门名称
	private String depart_name;
	
	//终端类型  0-终端；1-PC客户端
	private int term_type; 
	
	//安装位置ID
	private int position_id;
	
	//删除标志
	private DeviceFlag del_flag; 
	
	//上报方式
	private int mode;
	
	//确认显示哪种终端图片1.14T，2.facecheck动态，3.iDR系列，4.未知
	private int term_mode;
	
	//终端名称
	private String term_name;
	
	//终端代码
	private String term_code;
	
	//终端序列号
	private String term_sn;
	
	//安装位置
	private String term_position; 
	
	//授权码
	private String auth_code;
	
	//授权信息
	private String term_licence;
	
	//终端型号
	private int term_model;
	
	//终端型号名称
	private String device_type;
	
	//业务类型
	private int business_type;
	
	private String business_name;
	
	//软件版本
	private String soft_ver;
	
	// 终端IP地址
	private String term_ip;

	 // 该终端已授权员工的人员数量
	private int term_face_num;
	private String term_num;
	
	//状态
	private String status;
	
	// 终端网络
	private String term_network;

	// 终端连接时间
	private String term_connectime;
		
	private String revno;
	
	private boolean LAY_CHECKED;
	
	public boolean isLAY_CHECKED() {
		return LAY_CHECKED;
	}

	public void setLAY_CHECKED(boolean lAY_CHECKED) {
		LAY_CHECKED = lAY_CHECKED;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public int getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(int business_type) {
		this.business_type = business_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTerm_id() {
		return term_id;
	}

	public void setTerm_id(int term_id) {
		this.term_id = term_id;
	}

	public int getFace_library_id() {
		return face_library_id;
	}

	public void setFace_library_id(int face_library_id) {
		this.face_library_id = face_library_id;
	}

	public int getTerm_type() {
		return term_type;
	}

	public void setTerm_type(int term_type) {
		this.term_type = term_type;
	}

	public int getPosition_id() {
		return position_id;
	}

	public void setPosition_id(int position_id) {
		this.position_id = position_id;
	}

	public DeviceFlag getDel_flag() {
		return del_flag;
	}

	public void setDel_flag(DeviceFlag del_flag) {
		this.del_flag = del_flag;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getTerm_name() {
		return term_name;
	}

	public void setTerm_name(String term_name) {
		this.term_name = term_name;
	}

	public String getTerm_code() {
		return term_code;
	}

	public void setTerm_code(String term_code) {
		this.term_code = term_code;
	}

	public String getTerm_sn() {
		return term_sn;
	}

	public void setTerm_sn(String term_sn) {
		this.term_sn = term_sn;
	}

	public String getTerm_position() {
		return term_position;
	}

	public void setTerm_position(String term_position) {
		this.term_position = term_position;
	}

	public String getAuth_code() {
		return auth_code;
	}

	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}

	public String getTerm_licence() {
		return term_licence;
	}

	public void setTerm_licence(String term_licence) {
		this.term_licence = term_licence;
	}

	public String getRevno() {
		return revno;
	}

	public void setRevno(String revno) {
		this.revno = revno;
	}

	public String getTerm_network() {
		return term_network;
	}

	public void setTerm_network(String term_network) {
		this.term_network = term_network;
	}

	public String getTerm_connectime() {
		return term_connectime;
	}

	public void setTerm_connectime(String term_connectime) {
		this.term_connectime = term_connectime;
	}

	public String getSoft_ver() {
		return soft_ver;
	}

	public void setSoft_ver(String soft_ver) {
		this.soft_ver = soft_ver;
	}

	public String getTerm_ip() {
		return term_ip;
	}

	public void setTerm_ip(String term_ip) {
		this.term_ip = term_ip;
	}

	public int getTerm_face_num() {
		return term_face_num;
	}

	public void setTerm_face_num(int term_face_num) {
		this.term_face_num = term_face_num;
	}

	public String getDepart_name() {
		return depart_name;
	}

	public void setDepart_name(String depart_name) {
		this.depart_name = depart_name;
	}

	public List<Integer> getDepart_id() {
		return depart_id;
	}

	public void setDepart_id(List<Integer> depart_id) {
		this.depart_id = depart_id;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getTerm_num() {
		return term_num;
	}

	public void setTerm_num(String term_num) {
		this.term_num = term_num;
	}

	public int getTerm_model() {
		return term_model;
	}

	public void setTerm_model(int term_model) {
		this.term_model = term_model;
	}

	public int getTerm_mode() {
		return term_mode;
	}

	public void setTerm_mode(int term_mode) {
		this.term_mode = term_mode;
	}

	/** 终端标志，DELETED-删除, ACTIVATED-启用, DEACTIVATED-停用
	 */
	public enum DeviceFlag {
		ALL("全部", -1), ACTIVATED("启用", 0), DELETED("删除", 1) , DEACTIVATED("停用", 2), UNDELETED("未删除", 3);
		
		private String name;
        private int value;

        private DeviceFlag(String name, int value) {
            this.name = name;
            this.value = value;
        }
		
	    public String getName() {
	        return this.name;
	    }
	    
	    public int getValue() {
	    	return this.value;
	    }
	    
		public static DeviceFlag getFlag(int value) {
			for (DeviceFlag d : DeviceFlag.values()) {
				if (d.getValue() == value) {
					return d;
				}
			}
			return null;
		}
	}
}