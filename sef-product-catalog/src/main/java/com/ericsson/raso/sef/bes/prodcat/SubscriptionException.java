package com.ericsson.raso.sef.bes.prodcat;

public final class SubscriptionException extends Exception {
	private static final long serialVersionUID = 7048031339044130846L;

	public SubscriptionException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public SubscriptionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SubscriptionException(String arg0) {
		super(arg0);
	}

	public SubscriptionException(Throwable arg0) {
		super(arg0);
	}

	
}
