package com.ericsson.raso.sef.core.db.model;

public class RolePermissionDto {
	
	
	private String role_name;
	private String permission_id;
	
	
	public String getRole_id() {
		return role_name;
	}
	public void setRole_id(String role_name) {
		this.role_name = role_name;
	}
	public String getPermission_id() {
		return permission_id;
	}
	public void setPermission_id(String permission_id) {
		this.permission_id = permission_id;
	}

}
