package com.ericsson.raso.sef.core.db.model;

import java.util.List;

public class RoleAssignDto {
	
	private String id;
	private List<String> roleIdList;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getRoleIdList() {
		return roleIdList;
	}
	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

}
