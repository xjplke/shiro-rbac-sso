package me.itsoho.rbac.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import me.itsoho.rbac.domain.Manager;

//more message find on http://spring.io/docs

public interface ManagerRepository extends PagingAndSortingRepository<Manager, Long> {
	 
	 @Query("select a from Manager a where a.username like %:username%") 
	 public Page<Manager> findByUsername(@Param("username")String username,Pageable pageable); 
	 
	 @Query("select a from Manager a where a.fullname like %:fullname%") 
	 public Page<Manager> findByFullname(@Param("fullname")String fullname,Pageable pageable); 
	 
	 @Query("select a from Manager a where a.username = :username") 
	 public List<Manager> findByUsername(@Param("username")String username); 
	 
}
