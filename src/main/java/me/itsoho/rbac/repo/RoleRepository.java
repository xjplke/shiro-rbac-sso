package me.itsoho.rbac.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import me.itsoho.rbac.domain.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	@Query("select a from Role a where a.role = :role") 
	public List<Role> findByRole(@Param("role")String role); 
}
