package me.itsoho.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.itsoho.rbac.domain.Manager;
import me.itsoho.rbac.repo.ManagerRepository;

@Component
public class UserRealm extends AuthorizingRealm{
	@Autowired
	ManagerRepository managerRepo;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		String username = (String)principals.getPrimaryPrincipal();
		
		List<Manager> list = managerRepo.findByUsername(username);
		Manager manager = list.get(0);
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setRoles(manager.getRolesStringSet());
		authorizationInfo.setStringPermissions(manager.getPermissionStringSet());
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		String username = (String)token.getPrincipal();
		List<Manager> list = managerRepo.findByUsername(username);
		if(null==list || list.size() ==0){
			throw new UnknownAccountException("Username or Password error!");//没找到帐号
		}
		Manager manager = list.get(0);

        if(Boolean.FALSE.equals(manager.getIsActive())) {
            throw new LockedAccountException("Username is locked!"); //帐号锁定
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
        		manager.getUsername(), //用户名
                manager.getPassword(), //密码
                ByteSource.Util.bytes(""),//salt=username+salt
                getName()  //realm name
        );
		return authenticationInfo;
	}

}
