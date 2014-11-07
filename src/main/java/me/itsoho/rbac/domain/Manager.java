/**
 * 
 */
package me.itsoho.rbac.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;



/**
 * @author shaojunwu  --sjw
 * @date 2014-4-20
 */
@Entity
@Table(name="tbl_manager")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="discriminator",
    discriminatorType=DiscriminatorType.STRING
)
@DiscriminatorValue(value="User")
public class Manager implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8486460878516603500L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, length = 128, nullable = false)
	private String username;
	
	@Column(length=32)
	private String password;
	
	@Column(length=64)
	private String fullname;

	@Column(unique = true,length=64)
	private String email;
	
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@ManyToMany(fetch = FetchType.EAGER) 
	@JoinTable(name="tbl_user_roles",joinColumns={@JoinColumn(name="userId")},inverseJoinColumns={@JoinColumn(name="roleId")})
	private Set<Role> roles = new HashSet<Role>();
	

	@Column(unique = true,length=64)
	private String qq;
	
	@Column(unique = true,length=64)
	private String weibo;
	
	@Column(unique = true,length=64)
	private String weixin;
	
	@Column(name="expire")
	private Date expire;
	
	@Column(name="is_active")
	private boolean isActive;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="last_accessed")
	private Date lastAccessed;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the qq
	 */
	public String getQq() {
		return qq;
	}
	/**
	 * @param qq the qq to set
	 */
	public void setQq(String qq) {
		this.qq = qq;
	}
	/**
	 * @return the weibo
	 */
	public String getWeibo() {
		return weibo;
	}
	/**
	 * @param weibo the weibo to set
	 */
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	/**
	 * @return the weixin
	 */
	public String getWeixin() {
		return weixin;
	}
	/**
	 * @param weixin the weixin to set
	 */
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the lastAccessed
	 */
	public Date getLastAccessed() {
		return lastAccessed;
	}
	/**
	 * @param lastAccessed the lastAccessed to set
	 */
	public void setLastAccessed(Date lastAccessed) {
		this.lastAccessed = lastAccessed;
	}
	/**
	 * @return the isActive
	 */
	public boolean getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the expire
	 */
	public Date getExpire() {
		return expire;
	}
	/**
	 * @param expire the expire to set
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	
	public Set<String> getRolesStringSet(){
		Set<String> set = new HashSet<String>();
		for(Role role:roles){
			set.add(role.getRole());
		}
		return set;
	}
	
	public Set<String> getPermissionStringSet(){
		Set<String> set = new HashSet<String>();
		for(Role role:getRoles()){
			for(String permission:role.getPermissions()){
				set.add(permission);
			}
		}
		return set;
	}
	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	
	
	
}
