package com.jinglun.guard.shiro;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.privilege.domain.Menu;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.user.service.impl.UserServiceImpl;


public class Realm extends AuthorizingRealm {
    
    @Resource(name = "userServiceImpl")
	private UserServiceImpl userServiceImpl;

    /**
             *  为当前用户进行授权处理
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("执行授权");

        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        if(user != null){
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            // 角色与权限字符串集合
            Collection <String> rolesCollection = new HashSet<>();
            Collection<String> premissionCollection = new HashSet<>();
            List<Menu> menu=user.getMenuList();
            if(menu!=null) {
            	
	            for(Menu menus:menu) {
	            	 premissionCollection.add(menus.getName());
	            }
	           /* Set<RoleBean> roles = user.getRole();
	            for(RoleBean role : roles){
	                rolesCollection.add(role.getName());
	                Set<PermissionBean> permissions = role.getPermissions();
	                for (PermissionBean permission : permissions){
	                    premissionCollection.add(permission.getUrl());
	                }
	                info.addStringPermissions(premissionCollection);
	            }*/
	            
	            info.addStringPermissions(premissionCollection);
            }
           // info.addRoles(rolesCollection);
            return info;
        }
        return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		 System.out.println("执行认证");
            String username=(String) authenticationToken.getPrincipal();
            String password=new String((char[]) authenticationToken.getCredentials());
	      //  UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
	        ResultDomain<User> resultDomain = userServiceImpl.queryUserStatus(username,password);
	       // User bean = userService.findByName(token.getUsername());
	        User user = resultDomain.getResultData();
	        if(user == null){
	            throw new UnknownAccountException();
	        }
	        return new SimpleAuthenticationInfo(user, user.getUser_password(),
	               /* credentialsSalt,*/ getName());
	}
}

