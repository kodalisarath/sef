package com.ericsson.raso.sef.core.db.model;

import java.util.List;

public class RolePermissionMappingDto {

	
	private String roleName;
	private List<String> permissionIdList;
	
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public List<String> getPermissionIdList() {
		return permissionIdList;
	}
	public void setPermissionIdList(List<String> permissionIdList) {
		this.permissionIdList = permissionIdList;
	}
	
	
}
