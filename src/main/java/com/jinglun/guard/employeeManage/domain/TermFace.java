package com.jinglun.guard.employeeManage.domain;

public class TermFace {
	private int face_library_id;
	private int face_id;
	private int term_id;
	private int attribute;
	private int visit_id;
	private String expire_begin;
	private String expire_end;
	private String revno;

	public int getFace_library_id() {
		return face_library_id;
	}

	public void setFace_library_id(int face_library_id) {
		this.face_library_id = face_library_id;
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

	public int getAttribute() {
		return attribute;
	}

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	public int getVisit_id() {
		return visit_id;
	}

	public void setVisit_id(int visit_id) {
		this.visit_id = visit_id;
	}

	public String getExpire_begin() {
		return expire_begin;
	}

	public void setExpire_begin(String expire_begin) {
		this.expire_begin = expire_begin;
	}

	public String getExpire_end() {
		return expire_end;
	}

	public void setExpire_end(String expire_end) {
		this.expire_end = expire_end;
	}

	public String getRevno() {
		return revno;
	}

	public void setRevno(String revno) {
		this.revno = revno;
	}
}