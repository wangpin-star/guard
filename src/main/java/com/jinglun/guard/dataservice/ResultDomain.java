package com.jinglun.guard.dataservice;

import java.util.Map;

public class ResultDomain<T> {
	private int resultCode;
	private T resultData;
	private String resultMsg;
	private Map resultSubjoin;

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public ResultDomain() {
	}

	public ResultDomain(int code) {
		this.resultCode = code;
	}
	
	public ResultDomain(int code, T data) {
		this.resultCode = code;
		if(data != null)
			this.resultData = data;
	}
	
	public ResultDomain(int code, T data, Map subjoin) {
		this.resultCode = code;
		this.resultSubjoin = subjoin;
		if(data != null)
			this.resultData = data;
	}
	
	public int getResultCode() {
		return resultCode;
	}
	
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	
	public T getResultData() {
		return resultData;
	}
	
	public void setResultData(T resultData) {
		this.resultData = resultData;
	}

	public Map getResultSubjoin() {
		return resultSubjoin;
	}

	public void setResultSubjoin(Map resultSubjoin) {
		this.resultSubjoin = resultSubjoin;
	}
}
