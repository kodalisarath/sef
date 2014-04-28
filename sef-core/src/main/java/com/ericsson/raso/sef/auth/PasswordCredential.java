package com.ericsson.raso.sef.auth;

public abstract class PasswordCredential implements Credential {

	private String simplePassword = null;
	private Algorithm algorithm = Algorithm.UNDEF;
	
	public String getClearPassword() {
		return this.simplePassword;
	}
	
	public abstract byte[] getCipheredPassword();
	
	public void setPassword(String clearPassword) {
		this.simplePassword = clearPassword;
	}

}
