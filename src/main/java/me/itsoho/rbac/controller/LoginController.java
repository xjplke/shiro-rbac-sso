package me.itsoho.rbac.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.itsoho.rbac.angularshiro.AngularShiroAuthc;
import me.itsoho.rbac.angularshiro.AngularShiroAuthz;
import me.itsoho.rbac.angularshiro.AngularShiroCredentials;
import me.itsoho.rbac.angularshiro.AngularShiroInfo;
import me.itsoho.rbac.angularshiro.AngularShiroLoginResponse;
import me.itsoho.rbac.angularshiro.AngularShiroPrincipal;
import me.itsoho.rbac.angularshiro.TokenWarpper;
import me.itsoho.rbac.domain.Manager;
import me.itsoho.rbac.repo.ManagerRepository;




@RestController
@RequestMapping("/aaa")
public class LoginController {
	@Autowired
	ManagerRepository userRepo;
	
	
	private Manager loginInner(String username,String password){
		Subject subject = SecurityUtils.getSubject();
		subject.login(new UsernamePasswordToken(username,password));
		if(subject.isAuthenticated()){
			List<Manager> lst = userRepo.findByUsername(username);
			Session session = subject.getSession();
			session.setAttribute("user", lst.get(0));
			return lst.get(0);
		}
		return null;
	
	}
	@RequestMapping(value="login",method=RequestMethod.POST)
	public Manager login(@RequestBody Manager user){
		return loginInner(user.getUsername(),user.getPassword());
	}
	
	
	
	@RequestMapping(value="login",method=RequestMethod.GET)
	public Manager login(@RequestParam("username")String username,@RequestParam("password")String password){
		return loginInner(username,password);
	}

	@RequestMapping(value="me",method=RequestMethod.GET)
	public Manager getMe(){
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
	
		return (Manager)session.getAttribute("user");
	}

	@RequestMapping(value="logout",method=RequestMethod.GET)
	public void logout(){
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
	}
	
	@RequestMapping(value="authenticate",method=RequestMethod.POST)
	public AngularShiroLoginResponse shiroLogin(@RequestBody TokenWarpper tokenWarpper) throws Exception{
		
		Subject subject = SecurityUtils.getSubject();
		subject.login(new UsernamePasswordToken(tokenWarpper.getToken().getPrincipal(),
						tokenWarpper.getToken().getCredentials()));
		
		Manager user;
		if(subject.isAuthenticated()){
			List<Manager> lst = userRepo.findByUsername(tokenWarpper.getToken().getPrincipal());
			Session session = subject.getSession();
			session.setAttribute("user", lst.get(0));
			user = lst.get(0);
		}else{
			throw new Exception("Username or Password error!");
		}
		
		AngularShiroAuthc authc = new AngularShiroAuthc();
		AngularShiroPrincipal principal = new AngularShiroPrincipal();
		principal.setLogin(user.getUsername());
		principal.setName(user.getFullname());
		principal.setEmail(user.getEmail());
		authc.setPrincipal( principal );
		
		AngularShiroCredentials credentials = new AngularShiroCredentials();
		credentials.setLogin(user.getUsername());
		credentials.setName(user.getFullname());
		credentials.setEmail(user.getEmail());
		authc.setCredentials(credentials);
		
		AngularShiroAuthz authz = new AngularShiroAuthz();
		authz.setRoles(user.getRolesStringSet());
		authz.setPermissions(user.getPermissionStringSet());
		
		AngularShiroInfo info = new AngularShiroInfo();
		info.setAuthc(authc);
		info.setAuthz(authz);
		AngularShiroLoginResponse resp = new AngularShiroLoginResponse();
		resp.setInfo(info);
		return resp;
	}
	
}
