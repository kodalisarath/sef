package com.ericsson.raso.sef.auth;

public class AuthException extends Exception {

	private static final long	serialVersionUID	= -3880865432095678346L;

	public AuthException(String arg0) {
		super(arg0);
	}

	public AuthException(Throwable arg0) {
		super(arg0);
	}

	public AuthException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AuthException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
