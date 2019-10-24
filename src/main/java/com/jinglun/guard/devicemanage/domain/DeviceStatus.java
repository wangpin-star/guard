package com.jinglun.guard.devicemanage.domain;

//终端心跳类
public class DeviceStatus {
	private int term_id;
	
	// 网络
	private String network;

	// 连接时间
	private String connect_time;

	public int getTerm_id() {
		return term_id;
	}

	public void setTerm_id(int term_id) {
		this.term_id = term_id;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getConnect_time() {
		return connect_time;
	}

	public void setConnect_time(String connect_time) {
		this.connect_time = connect_time;
	}

}
