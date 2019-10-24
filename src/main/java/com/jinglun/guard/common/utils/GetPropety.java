package com.jinglun.guard.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jinglun.guard.GuardApplication;

public class GetPropety {
	Logger logger = LoggerFactory.getLogger(GetPropety.class);
	
	public  String getProperty()
	{
		String ip="";
		String port="";
		String content=null;
		ip=GuardApplication.ip;
		port=GuardApplication.port;
		Properties pro = new Properties();
		InputStream in = getClass().getResourceAsStream("/application.properties");
		try {
			pro.load(in);
			if(null==ip) {
				ip = pro.getProperty("web_server_addr").trim();
			}
			if(null==port) {
				port = pro.getProperty("web_server_port").trim();
			}
			content = "from=pc&ip="+ip+"&port="+port+"&cmpy=" ;
		} catch (IOException e) {
			this.logger.error("读取application.properties配置文件失败！");
			e.printStackTrace();
			
		}

		return content;
	}
	
	/*public  void setProperty(String Ip,String Port)
	{
		Properties pro = new Properties();
		InputStream in = getClass().getResourceAsStream("/application.properties");
		try {
			pro.load(in);
				pro.setProperty("web_server_addr", Ip);
				pro.setProperty("web_server_port", Port);
		} catch (IOException e) {
			this.logger.error("读取application.properties配置文件失败！");
			e.printStackTrace();
			
		}
	}*/
}
