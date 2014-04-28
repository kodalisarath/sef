package com.ericsson.raso.sef.auth;

public interface Credential {
	
	

	public enum Type {
		UNAUTHENTICATED,
		PASSWORD,
		CERTIFICATE,
		PIN_OTP;

	}

}
