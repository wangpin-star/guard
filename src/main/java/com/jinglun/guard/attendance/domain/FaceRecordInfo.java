package com.jinglun.guard.attendance.domain;

/** 刷脸记录
 * @author Tong
 *
 */
public class FaceRecordInfo {
	private int rec_id; // 刷脸记录id
	private int attribute; // 0-员工 1-访客
	private int face_id; // 人脸ID
	private int term_id;
	
	private int compare_result; // 比对结果
	private double compare_score; // 比对分值

	private String term_position; // 设备地点
	private String idcard;
	private String preview_photo_path;
	private String time;
	private String name;
	private String term_name;

	public String getTerm_name() {
		return term_name;
	}

	public void setTerm_name(String term_name) {
		this.term_name = term_name;
	}

	public int getRec_id() {
		return rec_id;
	}

	public void setRec_id(int rec_id) {
		this.rec_id = rec_id;
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

	public int getTerm_id() {
		return term_id;
	}

	public void setTerm_id(int term_id) {
		this.term_id = term_id;
	}

	public int getCompare_result() {
		return compare_result;
	}

	public void setCompare_result(int compare_result) {
		this.compare_result = compare_result;
	}

	public double getCompare_score() {
		return compare_score;
	}

	public void setCompare_score(double compare_score) {
		this.compare_score = compare_score;
	}

	public String getTerm_position() {
		return term_position;
	}

	public void setTerm_position(String term_position) {
		this.term_position = term_position;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getPreview_photo_path() {
		return preview_photo_path;
	}

	public void setPreview_photo_path(String preview_photo_path) {
		this.preview_photo_path = preview_photo_path;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}