package com.jinglun.guard.employeeManage.domain;

import java.util.List;

import org.apache.commons.lang.enums.EnumUtils;

public class FaceInfo {
	private Integer face_library_id; // 人脸库ID
	private Integer face_id; // 人脸ID
	private Integer depart_id; // 部门ID
	private Integer attribute; // 属性 0-员工 1-访客
	private Integer sex; // 性别 1-男 2-女
	private Integer type; // 图片类型 0-近照1-身份证照
	private Integer status; // 是否注销
	private Integer visit_num; // 访客人数
	private Integer leader_id; // 上级领导id
	private Integer rec_id; // 来访日志ID
	private Integer bat_id; // 批量访客登记ID
	private Integer pic_num; //比对照片数量
	private String company; // 公司名称
	private String name; // 姓名
	private String nick_name; // 别名
	private String idcard; // 身份证号
	private String idcard_cid; // 卡体管理号
	private String idcard_expire; // 身份证有效期
	private String empcard; // 员工号
	private String tel_no; // 手机号
	private String revno; // 更新的版本号
	private String visit_time; // 来访时间
	private String expire_time; // 有效期限
	private String employee; // 受访人
	private String face_info_url; // 上传图片路径
	private String face_info_base64; // 照片base64编码内容
	private String id_pic_url; // 身份证照片路径
	private String photo_wlt; // 身份证照片wlt数据
	private String employee_id; // 工号
	private String position; // 职位
	private String nation; // 民族
	private String addr; // 地址
	private String agency; // 发证机关
	private String departName;
	private List<Picture> pictures;

	public FaceInfo() {
	}

	public FaceInfo(Integer face_library_id, Integer face_id, Integer depart_id, Integer attribute, Integer sex,
			Integer type, Integer status, Integer visit_num, Integer leader_id, Integer rec_id, Integer bat_id,
			String company, String name, String nick_name, String idcard, String idcard_cid, String idcard_expire,
			String empcard, String tel_no, String revno, String visit_time, String expire_time, String employee,
			String face_info_url, String id_pic_url, String photo_wlt, String employee_id, String position,
			String nation, String addr) {
		this.face_library_id = face_library_id;
		this.face_id = face_id;
		this.depart_id = depart_id;
		this.attribute = attribute;
		this.sex = sex;
		this.type = type;
		this.status = status;
		this.visit_num = visit_num;
		this.leader_id = leader_id;
		this.rec_id = rec_id;
		this.bat_id = bat_id;
		this.company = company;
		this.name = name;
		this.nick_name = nick_name;
		this.idcard = idcard;
		this.idcard_cid = idcard_cid;
		this.idcard_expire = idcard_expire;
		this.empcard = empcard;
		this.tel_no = tel_no;
		this.revno = revno;
		this.visit_time = visit_time;
		this.expire_time = expire_time;
		this.employee = employee;
		this.face_info_url = face_info_url;
		this.id_pic_url = id_pic_url;
		this.photo_wlt = photo_wlt;
		this.employee_id = employee_id;
		this.position = position;
		this.nation = nation;
		this.addr = addr;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public Integer getFace_library_id() {
		return face_library_id;
	}

	public void setFace_library_id(Integer face_library_id) {
		this.face_library_id = face_library_id;
	}

	public Integer getFace_id() {
		return face_id;
	}

	public void setFace_id(Integer face_id) {
		this.face_id = face_id;
	}

	public Integer getDepart_id() {
		return depart_id;
	}

	public void setDepart_id(Integer depart_id) {
		this.depart_id = depart_id;
	}

	public Integer getAttribute() {
		return attribute;
	}

	public void setAttribute(Integer attribute) {
		this.attribute = attribute;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getVisit_num() {
		return visit_num;
	}

	public void setVisit_num(Integer visit_num) {
		this.visit_num = visit_num;
	}

	public Integer getLeader_id() {
		return leader_id;
	}

	public void setLeader_id(Integer leader_id) {
		this.leader_id = leader_id;
	}

	public Integer getRec_id() {
		return rec_id;
	}

	public void setRec_id(Integer rec_id) {
		this.rec_id = rec_id;
	}

	public Integer getBat_id() {
		return bat_id;
	}

	public void setBat_id(Integer bat_id) {
		this.bat_id = bat_id;
	}

	public Integer getPic_num() {
		return pic_num;
	}

	public void setPic_num(Integer pic_num) {
		this.pic_num = pic_num;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getIdcard_cid() {
		return idcard_cid;
	}

	public void setIdcard_cid(String idcard_cid) {
		this.idcard_cid = idcard_cid;
	}

	public String getIdcard_expire() {
		return idcard_expire;
	}

	public void setIdcard_expire(String idcard_expire) {
		this.idcard_expire = idcard_expire;
	}

	public String getEmpcard() {
		return empcard;
	}

	public void setEmpcard(String empcard) {
		this.empcard = empcard;
	}

	public String getTel_no() {
		return tel_no;
	}

	public void setTel_no(String tel_no) {
		this.tel_no = tel_no;
	}

	public String getRevno() {
		return revno;
	}

	public void setRevno(String revno) {
		this.revno = revno;
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

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String getFace_info_url() {
		return face_info_url;
	}

	public void setFace_info_url(String face_info_url) {
		this.face_info_url = face_info_url;
	}

	public String getId_pic_url() {
		return id_pic_url;
	}

	public void setId_pic_url(String id_pic_url) {
		this.id_pic_url = id_pic_url;
	}

	public String getPhoto_wlt() {
		return photo_wlt;
	}

	public void setPhoto_wlt(String photo_wlt) {
		this.photo_wlt = photo_wlt;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getFace_info_base64() {
		return face_info_base64;
	}

	public void setFace_info_base64(String face_info_base64) {
		this.face_info_base64 = face_info_base64;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	public static class Picture {
		private int pic_id;
		private int option;	//0-不变 ,1-新增或修改, 2-删除
		private int image_type;	//0-比对照片，1-头像
		private int width;
		private int height;
		private String data;
		private String feature;
		private String feature_ver;
		
		public enum Option {
			/**
			 * 指定pic_id的照片保持不变
			 */
			UNCHANGE,
			/**
			 * pic_id为0则上报，不为0则修改
			 */
			UPLOAD_OR_MODIFY,
			/**
			 * 删除指定pic_id的照片
			 */
			DELETE;
		}

		public int getPic_id() {
			return pic_id;
		}

		public void setPic_id(int pic_id) {
			this.pic_id = pic_id;
		}

		public int getOption() {
			return option;
		}

		public void setOption(Option option) {
			switch (option) {
			case UNCHANGE:
				this.option = 0;
				break;
			case UPLOAD_OR_MODIFY:
				this.option = 1;
				break;
			case DELETE:
				this.option = 2;
				break;
			default:
				break;
			}
		}

		public int getImage_type() {
			return image_type;
		}

		public void setImage_type(int image_type) {
			this.image_type = image_type;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}

		public String getFeature() {
			return feature;
		}

		public void setFeature(String feature) {
			this.feature = feature;
		}

		public String getFeature_ver() {
			return feature_ver;
		}

		public void setFeature_ver(String feature_ver) {
			this.feature_ver = feature_ver;
		}
	}
}