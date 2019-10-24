package com.jinglun.guard.user.controller;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jinglun.guard.dataservice.DataService;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinglun.guard.GuardApplication;
import com.jinglun.guard.common.utils.ManifestUtils;
import com.jinglun.guard.common.utils.SecurityCode;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.systemManage.service.ParametercontrolService;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.user.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
	@Resource(name = "userServiceImpl")
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ParametercontrolService parametercontrolService;
	
	@Value("${version}")
	private String version;

	Logger logger = LoggerFactory.getLogger(UserController.class);

	//登录返回代码
	String string="loginResult";
	//登录页面
	String login="user/login";
	//验证码
	String randCheckCode="randCheckCode";
	//修改密码页面
	String useredit="user/usereditpwd";


	@RequestMapping({"/login",""})
	public String toLogin(Model model) {
		model.addAttribute(string, "null");
		return login;
	}

	
	@RequestMapping("/login/authen")
	public String authen(HttpServletResponse response, String name, String password, String securitycode,
						 HttpSession session, Model model, String rememberMe,HttpServletRequest request) throws IOException {

		ResultDomain<User> resultDomain = userServiceImpl.queryUserStatus(name, password);
		if (resultDomain == null) {
			model.addAttribute(string, 10);
			return login;
		} else {
			int userStatus = resultDomain.getResultCode();
			model.addAttribute("name", name);

			// 判断是否为初始密码
			User user = resultDomain.getResultData();
			// session传值
			session.setAttribute("user", user);

			response.setContentType("text/html;charset=gb2312");
			
			// 检查验证码输入
			if (session.getAttribute(randCheckCode) != null) {
				if (!securitycode.toLowerCase()
						.equals(session.getAttribute(randCheckCode).toString().toLowerCase())) {
					model.addAttribute(string, 5);
					this.logger.info("User {} login fail, securitycode incorrect, expected[{}],actual[{}]", name,
							session.getAttribute(randCheckCode).toString().toLowerCase(), securitycode.toLowerCase());
					return login;
				}
			}
			// 检查验证码是否失效
			if (session.getAttribute(randCheckCode) == null) {
				model.addAttribute(string, 6);
				this.logger.info("User {} login fail, securitycode invalidation", name);
				return login;
			}

			if (user.getPassword_status() == 1) {
				model.addAttribute(string, 0);
				return useredit;
			}

			if (userStatus > 0) {
				model.addAttribute(string, userStatus);
				return login;
			}
			
			ApplicationHome home = new ApplicationHome(getClass());
			File jarFile = home.getSource();
			String deployVersion = "";
			if(jarFile.exists() && jarFile.isFile()) {
				deployVersion = ManifestUtils.getDeployVersion(jarFile.getPath());
			} else {
				deployVersion = version;
			}
			
			session.setAttribute(string, 2);
			session.setAttribute("name",name);
			session.setAttribute("deployVersion",deployVersion);
			Subject subject = SecurityUtils.getSubject();
			// 2.封装用户数据
            UsernamePasswordToken token = new UsernamePasswordToken(name, password);
            // 3.执行登录方法
            subject.login(token);
           
            return "redirect:/main"; //重定向到跳转到首页的方法,改变地址栏为首页地址
		}
	}

	/**
	 * 生成登录用的验证码
	 * 
	 * @param response Http响应
	 * 
	 **/
	@RequestMapping(value = "/graphics", method = RequestMethod.GET)
	public void securityCode(HttpServletResponse response, HttpSession session) throws IOException {
		SecurityCode sc = SecurityCode.generateCode(4, true, true, false, true, new Color(0, 0, 255),
				new Color(0, 0, 0));
		// 禁止缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		// 指定生成的响应是图片
		response.setContentType("image/jpeg");

		// 将生成的验证码保存到Session中
		session.setAttribute(randCheckCode, sc.getCode());
		this.logger.info("Generate security code:" + sc.getCode());

		ServletOutputStream output = response.getOutputStream();
		ImageIO.write(sc.getImg(), "GIF", output);
		output.flush();
		output.close();
	}

	/**
	 * 跳转到修改密码页面
	 * 
	 * @return 跳转到修改密码页面
	 */
	@RequestMapping(value = "user/editPwd")
	public String editPwd(HttpServletResponse res,int loginResult,Model model) {
		model.addAttribute(string, loginResult);
		return useredit;
	}

	/**
	 * 修改密码
	 * 
	 * @param response     页面弹框
	 * @param request      从session取值
	 * @param new_password 新密码
	 * @return 修改成功跳转回登录页面
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/editUserPwd")
	public String editUserpwd(HttpServletResponse response, HttpServletRequest request,
			@Param("new_password") String new_password, @Param("old_password") String old_password, Model model)
			throws IOException {
		// 从session取值
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		int result = 1;
		if (!user.getUser_password().equals(old_password)) {
			model.addAttribute(string, 1);
			return "user/usereditpwd";
		} else {
			// 设置新密码
			user.setNew_password(new_password);
			ResultDomain<User> resultDomain = userServiceImpl.userInfoUpload(user, 1);
			result = resultDomain.getResultCode();
			// 页面弹框
			if (result == 0) {
				model.addAttribute(string, 9);
				return login;
			}else {
				model.addAttribute(string, 12);
				return login;
			}
		}
	}

	/**
	 * 注销
	 * 
	 * @param request
	 * @return 返回登录页面
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request,Model model) {
		// 清除session
		 Subject subject = SecurityUtils.getSubject();
	        if (subject != null) {
	            subject.logout();
	        }
		request.getSession().removeAttribute("user");
		request.getSession().invalidate();
		model.addAttribute(string, "null");
		return login;
	}

	@RequestMapping("/faceLogin")
	@ResponseBody
	public String oneToNFaceCheck(String imgBase64,HttpSession session) {
		ResultDomain<List<DataService.FDResult>> listResultDomain = DataService.FaceImageDetect(imgBase64, 2);
		if (null == listResultDomain.getResultData() || listResultDomain.getResultCode() <= 0) {
			return "-3";
		}
		DataService.FDResult fdResult = listResultDomain.getResultData().get(0);
		if (fdResult.getFace_width() < 200) {
			return "-2";
		}
		String feature = fdResult.getFeature();
		Integer faceId = oneToManyFaceCheck(imgBase64);
		if (faceId <= 0) {
			return "-1";
		}
		User user = selectUserByFaceId(faceId);
		session.setAttribute("user",user);
		String name = user.getUser_name();
		String password = user.getUser_password();
		ApplicationHome home = new ApplicationHome(getClass());
		File jarFile = home.getSource();
		String deployVersion = "";
		if(jarFile.exists() && jarFile.isFile()) {
			deployVersion = ManifestUtils.getDeployVersion(jarFile.getPath());
		} else {
			deployVersion = version;
		}
		session.setAttribute(string, 2);
		session.setAttribute("name",name);
		session.setAttribute("deployVersion",deployVersion);
		Subject subject = SecurityUtils.getSubject();
		// 2.封装用户数据
		UsernamePasswordToken token = new UsernamePasswordToken(name, password);
		// 3.执行登录方法
		subject.login(token);
		return  "0";
	}

	private Integer oneToManyFaceCheck(String feature) {
		ResultDomain<DataService.FMResult> fmResultResultDomain= DataService.FaceImageMatch(feature);
		if (fmResultResultDomain.getResultCode()==0)
		{
			return fmResultResultDomain.getResultData().getFace_id();
		}
		return -1;
	}

	private User selectUserByFaceId(Integer faceId) {

		User user = new User();
		user.setUser_id(2);
		user.setFace_library_id(1);
		user.setUser_name("admin");
		user.setUser_password("123456");
		user.setCompany_name("普利商用");
		return user;
	}
	
	
	@RequestMapping("/main")
	private  String toStart(HttpSession httpSession,Model model) {
		User user=(User)httpSession.getAttribute("user");
			String name = String.valueOf(httpSession.getAttribute("name"));
			String loginResult = String.valueOf(httpSession.getAttribute("loginResult"));
			String deployVersion = String.valueOf(httpSession.getAttribute("deployVersion"));
			model.addAttribute("name",name);
			model.addAttribute("deployVersion",deployVersion);
			model.addAttribute("loginResult",loginResult);
			GuardApplication.s_matchServerIP=parametercontrolService.netServerip(user);
			return "start";
	}
}
