package com.jinglun.guard.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.jinglun.guard.common.utils.LoginInterceptor;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;




@Configuration
public class Shiro {
/*	@Bean("hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //加密次数
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }*/

	
    @Bean(name = "sessionDAO")
    public MemorySessionDAO getMemorySessionDAO() {
        return new MemorySessionDAO();
    }
    
    @Bean(name = "sessionIdCookie") 
    public SimpleCookie getSimpleCookie() { 
    	SimpleCookie simpleCookie = new SimpleCookie(); 
    	simpleCookie.setName("SHRIOSESSIONID"); 
    	return simpleCookie; 
    	}
 
	
  //配置shiro session 的一个管理器，添加缓存操作DAO
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(getMemorySessionDAO());
       // sessionManager.setSessionIdCookie(getSimpleCookie());
        return sessionManager;
    }
    
    
    @Bean("Realm")
    public Realm userRealm(/*@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher*/) {
        Realm userRealm = new Realm();
        /*userRealm.setCredentialsMatcher(matcher);*/
        return userRealm;
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(@Qualifier("securityManager")DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 设置 SecurityManager
        bean.setSecurityManager(securityManager);

        bean.setSuccessUrl("/main");
        
        // 设置登录跳转页面
        bean.setLoginUrl("/login");
        // 设置未授权提示页面
        bean.setUnauthorizedUrl("/login/authen");
        
     
        
        /**
         * Shiro内置过滤器，可以实现拦截器相关的拦截器
         *    常用的过滤器：
         *      anon：无需认证（登录）可以访问
         *      authc：必须认证才可以访问
         *      user：如果使用rememberMe的功能可以直接访问
         *      perms：该资源必须得到资源权限才可以访问
         *      role：该资源必须得到角色权限才可以访问
         **/
        Map<String, String> filterMap = new LinkedHashMap<>();

        
        filterMap.put("/login/authen","anon");
        filterMap.put("/user/editUserPwd","anon");
        filterMap.put("/faceLogin","anon");
        filterMap.put("/employeeManage/downloadPlugin","anon");
        filterMap.put("/flashplayerax_install_cn.exe","anon");
        filterMap.put("/jscam.swf","anon");
        filterMap.put("/jscam_canvas_only.swf","anon");
        filterMap.put("/graphics","anon");
        filterMap.put("/css/**","anon");
        filterMap.put("/design/**","anon");
        filterMap.put("/fonts/**","anon");
        filterMap.put("/images/**","anon");
        filterMap.put("/js/**","anon");
        filterMap.put("/layui/**","anon");
        filterMap.put("/layui2.5.1/**","anon");
        filterMap.put("/layui2.5.4/**","anon");
        filterMap.put("/lib/**","anon");
        filterMap.put("/stylesheets/**","anon");
        //filterMap.put("/**","authc");
        filterMap.put("/logout", "logout");
        filterMap.put("/user/login","anon");
        filterMap.put("/**","authc");
        bean.setFilterChainDefinitionMap(filterMap);
        
        //自定义拦截器
        Map<String, Filter> filtersMaps = new LinkedHashMap<>();
        filtersMaps.put("authc", new LoginInterceptor());
        bean.setFilters(filtersMaps);
        
        
        return bean;
    }

    //shiro标签使用需开启
    @Bean
    public ShiroDialect shiroDialect() 
    { 
      return new ShiroDialect(); 
    }
    /**
             * 注入 securityManager
     */
 
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(/*HashedCredentialsMatcher hashedCredentialsMatcher*/) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 关联realm.
        securityManager.setRealm(userRealm(/*hashedCredentialsMatcher*/));
    //    securityManager.setSessionManager(sessionManager());
        return securityManager;
    }
    
   
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
	
	/**
	 *  开启shiro aop注解支持.
	 *  使用代理方式;所以需要开启代码支持;
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
	
}
