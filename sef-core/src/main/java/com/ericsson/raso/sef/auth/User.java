package com.ericsson.raso.sef.auth;

public class User extends Identity {
	
	private Credential credential = null;

	public User(String name) {
		super(name);
	}
	
	public User(String name, Credential credential) {
		super(name);
		this.credential = credential;
	}
	
	
	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}
	
	

	

}
