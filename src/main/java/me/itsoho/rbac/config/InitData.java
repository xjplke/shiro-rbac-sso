package me.itsoho.rbac.config;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.itsoho.rbac.domain.Manager;
import me.itsoho.rbac.domain.Role;
import me.itsoho.rbac.repo.ManagerRepository;
import me.itsoho.rbac.repo.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;




@Configuration
public class InitData {
	@Bean
	@Autowired
	PermissionDetect permissionDetect(@Qualifier("requestMappingHandlerMapping")RequestMappingHandlerMapping handlerMapping,
			WebApplicationContext webApplicationContext){
		//TODO:add license control
		return PermissionDetect.getPermissionDetect(handlerMapping, webApplicationContext);
	}
	
	@Bean
	@Autowired
	Manager initAdmin( PermissionDetect permissionDetect, RoleRepository roleRepo,ManagerRepository managerRepo){
		Role adminRole;
		List<Role> rlst = roleRepo.findByRole("admin");
		if (rlst==null || rlst.size()==0){
			adminRole = new Role();
			adminRole.setRole("admin");
			adminRole.setDescription("manager has all permimissions!");
			adminRole.addPermission("*:*");
			adminRole.setAvailable(true);
			roleRepo.save(adminRole);
		}else{
			adminRole = rlst.get(0);
		}
		
		Role viewRole;
		rlst = roleRepo.findByRole("viewer");
		if (rlst==null || rlst.size()==0){
			viewRole = new Role();
			viewRole.setRole("viewer");
			viewRole.setDescription("manager has all view permissions!");
			viewRole.addPermission("*:view");
			viewRole.setAvailable(true);
			roleRepo.save(viewRole);
		}else{
			viewRole = rlst.get(0);
		}
		
		Manager admin;
		List<Manager> lst = managerRepo.findByUsername("admin");
		if(lst == null || lst.size()==0){
			admin = new Manager();
			admin.setUsername("admin");
			admin.setIsActive(true);
			admin.setCreatedDate(new Date());
			admin.setFullname("administrator");
			admin.setLastAccessed(new Date());
			admin.setPassword("1234qpmz");
			Set<Role> hsRole = new HashSet<Role>();
			hsRole.add(adminRole);
			admin.setRoles(hsRole);
			managerRepo.save(admin);
		}else{
			admin = lst.get(0);
		}
		
		Manager viewer;
		lst = managerRepo.findByUsername("viewer");
		if(lst == null || lst.size()==0){
			viewer = new Manager();
			viewer.setUsername("viewer");
			viewer.setIsActive(true);
			viewer.setCreatedDate(new Date());
			viewer.setFullname("viewer");
			viewer.setLastAccessed(new Date());
			viewer.setPassword("qpmzabcd");
			Set<Role> hsRole = new HashSet<Role>();
			hsRole.add(viewRole);
			viewer.setRoles(hsRole);
			managerRepo.save(viewer);
		}
		
		return admin;
	}
	
}
