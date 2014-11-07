package me.itsoho.rbac.controller;

import java.util.Date;

import me.itsoho.rbac.domain.Manager;
import me.itsoho.rbac.exceptions.UserNotFoundException;
import me.itsoho.rbac.exceptions.UserPasswordErrorException;
import me.itsoho.rbac.repo.ManagerRepository;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




@EnableTransactionManagement
@RestController
@EnableAutoConfiguration
@RequestMapping("/manager")
@Transactional
public class ManagerController {
	
	Sort sort = new Sort(new Order(Direction.DESC,"id"));
	
	@Autowired
	ManagerRepository managerRepository;

	
	@RequiresPermissions("manager:edit")
	@RequestMapping(method=RequestMethod.POST)
	public Manager add(@RequestBody Manager manager){
		manager.setCreatedDate(new Date());
		manager.setLastAccessed(new Date());
		//userEnable(user);
		manager.setIsActive(true);
		return managerRepository.save(manager);
	}
	
	@RequiresPermissions("manager:view")
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public Manager get(@PathVariable("id") Long id) throws Exception {
		Manager manager = managerRepository.findOne(id);
		if(null==manager){
			throw new UserNotFoundException("id for "+id+" not found!");
		}
		return manager;
	}
	
	public Manager update(Manager manager,Manager upto){
		
		manager.setEmail(upto.getEmail());
		manager.setExpire(upto.getExpire());
		manager.setLastAccessed(new Date());
		manager.setUsername(upto.getUsername());
		manager.setWeibo(upto.getWeibo());
		manager.setWeixin(upto.getWeixin());
		manager.setQq(upto.getQq());
		manager.setPassword(upto.getPassword());
		//return userRepository.save(user);
		manager.setIsActive(upto.getIsActive());
		return managerRepository.save(manager);
	}
	
	@RequiresPermissions("manager:edit")
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public Manager update(@PathVariable("id") Long id,@RequestBody Manager manager) throws Exception{
		
		Manager find = managerRepository.findOne(id);
		if(null==find){
			throw new UserNotFoundException("id for "+id+" not found!");
		}
		
		return update(find,manager);
	}
	
	@RequiresPermissions("manager:edit")
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) throws Exception{
		managerRepository.delete(id);
	}
	
	@RequiresPermissions("manager:view")
	@RequestMapping(method=RequestMethod.GET)
	public Page<Manager> get(@RequestParam("page")int page,
					@RequestParam("size") int size){
		Pageable pageable = new PageRequest(page, size, Direction.DESC, "id");
		return managerRepository.findAll(pageable);
	}
	
	@RequiresPermissions("manager:view")
	@RequestMapping(value="/fullname/{fullname}",method=RequestMethod.GET)
	public Page<Manager> findByFullname(@PathVariable("fullname")String fullname,
			@RequestParam("page")int page, @RequestParam("size") int size){
		Pageable pageable = new PageRequest(page, size, Direction.DESC, "id");
		return managerRepository.findByFullname (fullname,pageable);
	}
	
	@RequiresPermissions("manager:view")
	@RequestMapping(value="/username/{username}",method=RequestMethod.GET)
	public Page<Manager> findByUsername(@PathVariable("username")String username,
			@RequestParam("page")int page, @RequestParam("size") int size){
		Pageable pageable = new PageRequest(page, size, Direction.DESC, "id");
		return managerRepository.findByUsername(username,pageable);
	}
	
	
	@RequiresPermissions("manager:edit")
	@RequestMapping(value="/{id}/passowrd",method=RequestMethod.POST)
	public void changePassword(@PathVariable("id")Long id,
							@RequestParam("oldpassword") String oldpassword,
							@RequestParam("newpassword") String newpassword ) throws Exception 
	{
		Manager find = managerRepository.findOne(id);
		if(null==find){
			throw new UserNotFoundException("id for "+id+" not found!");
		}
		//check oldpassword;
		if(!oldpassword.equals(find.getPassword())){
			throw new UserPasswordErrorException("Old Password error!");
		}
		//modify to new password;
		find.setPassword(newpassword);
		managerRepository.save(find);
	}
	
}
