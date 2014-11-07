package me.itsoho.rbac.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;


@Entity
@Table(name="tbl_role")
public class Role implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5001431057892316553L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique=true,length = 32, nullable = false)
	private String role;
	
	@Column(length = 128, nullable = false)
	private String description;

	
	@Column(nullable = false)
	private Boolean available;
	
//	@Cascade(value = CascadeType.SAVE_UPDATE)
//	@ManyToMany(fetch = FetchType.LAZY) 
//	@JoinTable(name="tbl_role_perminissions",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="perminissionId")})
//	private Set<Permission> permissions;
//	@OneToMany(mappedBy="role", fetch=FetchType.EAGER)

	@ElementCollection(targetClass = String.class)
	@CollectionTable(name = "tbl_role_permission", joinColumns = @JoinColumn(name = "role"))
	@Column(name = "permission")
	private List<String> permissions = new ArrayList<String>();

//	@Cascade(value={CascadeType.SAVE_UPDATE}) 
//	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY) 
//	private Set<User> users;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
//	public Set<Permission> getPermissions() {
//		return permissions;
//	}
//	public void setPermissions(Set<Permission> permissions) {
//		this.permissions = permissions;
//	}
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	public void addPermission(String permission){
		this.permissions.add(permission);
	}

	
}
