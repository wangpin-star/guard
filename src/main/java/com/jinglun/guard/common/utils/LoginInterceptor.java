package com.jinglun.guard.common.utils;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends FormAuthenticationFilter implements HandlerInterceptor {

	  @Override
	  protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
	    if (this.isLoginRequest(servletRequest, servletResponse)) {
	      if (this.isLoginSubmission(servletRequest, servletResponse)) {
	        return this.executeLogin(servletRequest, servletResponse);
	      } else {
	        return true;
	      }
	    } else {
	      if (isAjax((HttpServletRequest) servletRequest)) {
	        HttpServletResponse responses = (HttpServletResponse) servletResponse;        
            responses.setCharacterEncoding("UTF-8");
            responses.setHeader("sessionStatus", "timeout");
	      } else {
/*
	        this.saveRequestAndRedirectToLogin(servletRequest, servletResponse);
*/
	    	  servletResponse.setContentType("text/html; charset=UTF-8"); //转码
              PrintWriter out = servletResponse.getWriter();
              out.println("<script type='text/javascript' src='/js/jquery.min.js'></script>" +
                      "	<script type='text/javascript' src='/layui/layui.js'></script>");
              out.println("<script>");
              out.println("layui.use(['layer'], function() {"
                      + "var layer = layui.layer;"
                      + "layer.alert('登录超时,请重新登录',{icon: 0,title:'提示',end:function(){" +
                      "       var p = window;" +
                      "       while(p != p.parent){" +
                      "           p = p.parent;" +
                      "       }" +
                      "       p.location.href='/login';" +
                      "   }});" +
                      "   });");
              out.println("</script>");
              out.flush();
              out.close();
	      }
	      return false;
	    }
	  }

	  public boolean isAjax(HttpServletRequest httpServletRequest) {
	    String header = httpServletRequest.getHeader("X-Requested-With");
	    if ("XMLHttpRequest".equalsIgnoreCase(header)) {
	      return Boolean.TRUE;
	    }
	    return Boolean.FALSE;
	  }
	  
	  @Override
	  protected boolean pathsMatch(String path, ServletRequest request) {
	        String requestURI = getPathWithinApplication(request);
	        if("/".equals(requestURI)) {
	        	requestURI="/login";
	        }
	        return pathsMatch(path, requestURI);
	    }
}
