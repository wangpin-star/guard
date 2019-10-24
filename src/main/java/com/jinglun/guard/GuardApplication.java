package com.jinglun.guard;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.stereotype.Controller;

/**
 * 项目启动类，直接run启动即可，里面内置servlet容器，默认为tomcat
 * @author wangxiwei
 *
 */
/*@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})*/
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@Controller
public class GuardApplication {
    public static String ip;
    public static String port;
    public static String netserverip;
	public static String s_matchServerIP;
    public static String comport;
	

	public static void main(String[] args) {
		SpringApplication.run(GuardApplication.class, args);
		if(null!=args) {
			for(int i=0;i<args.length;i++) {
				if("ip=".equals(args[i].substring(0, 3))) {
					ip=args[i].substring(3);
				}
				if("port=".equals(args[i].substring(0, 5))) {
					port=args[i].substring(5);
				}
			}
		}
	}
}
