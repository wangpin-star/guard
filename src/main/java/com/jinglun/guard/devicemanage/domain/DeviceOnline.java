package com.jinglun.guard.devicemanage.domain;

public class DeviceOnline {
	//设备终端在线数量
	private int online;
	
	//设备终端离线数量
	private int underline;
	
	public int getOnline() {
		return online;
	}
	public void setOnline(int online) {
		this.online = online;
	}
	public int getUnderline() {
		return underline;
	}
	public void setUnderline(int underline) {
		this.underline = underline;
	}
}
