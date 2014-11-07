package me.itsoho.rbac.angularshiro;

import java.util.Set;

public class AngularShiroAuthz {
	Set<String> roles;
	Set<String> permissions;
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	public Set<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}
	
}
