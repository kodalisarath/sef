package com.ericsson.raso.sef.auth;

public class User extends Identity {
	
	private Credential credential = null;

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}
	
	

	

}
