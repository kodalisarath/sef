package com.ericsson.raso.sef.auth;

import com.ericsson.raso.sef.core.FrameworkException;


public class CredentialFactory implements Credential {

	public static Credential getInstance(Type type) throws FrameworkException {
		switch (type) {
			case CERTIFICATE:
				//TODO: return new CertificateCredential();
				throw new FrameworkException("Not Implemented yet!!");
			case PASSWORD:
				return new PasswordCredential();
			case PIN_OTP:
				//TODO: return new OtpCredential();
				throw new FrameworkException("Not Implemented yet!!");
			case UNAUTHENTICATED:
				return null;
			default:
				return null;
		}
	}

	@Override
	public Type getCredentialType() {
		return null;
	}
}
