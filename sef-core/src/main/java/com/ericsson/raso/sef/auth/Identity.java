package com.ericsson.raso.sef.auth;

import java.util.List;

import com.ericsson.raso.sef.auth.permissions.Privilege;


public abstract class Identity {
	
	private String name = null;
	private List<Privilege> privileges = null;
	
	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}
	
	public List<Privilege> getPrivileges() {
		return this.privileges;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	

}
