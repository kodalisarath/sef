package com.ericsson.raso.sef.core.db.model;

import java.util.List;

public class RoleProviderDto {
	
	
	private String providerId;
	private List<String> roleIdList ;
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public List<String> getRoleIdList() {
		return roleIdList;
	}
	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}
	
	

}
