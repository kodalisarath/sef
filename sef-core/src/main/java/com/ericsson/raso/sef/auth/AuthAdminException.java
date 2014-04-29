package com.ericsson.raso.sef.auth;

public class AuthAdminException extends Exception {

	private static final long	serialVersionUID	= -6885739647923129844L;

	public AuthAdminException(String arg0) {
		super(arg0);
	}

	public AuthAdminException(Throwable arg0) {
		super(arg0);
	}

	public AuthAdminException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AuthAdminException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
