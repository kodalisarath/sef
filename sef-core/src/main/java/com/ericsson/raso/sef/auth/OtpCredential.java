package com.ericsson.raso.sef.auth;

public class OtpCredential implements Credential {

	public OtpCredential() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Type getCredentialType() {
		return Type.PIN_OTP;
	}

}
