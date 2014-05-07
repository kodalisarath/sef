package com.ericsson.raso.sef.auth;



public class User extends Identity {
	private static final long serialVersionUID = 8710713213301954026L;

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
