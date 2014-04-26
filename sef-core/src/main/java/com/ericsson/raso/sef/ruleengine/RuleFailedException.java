package com.ericsson.raso.sef.ruleengine;

import com.ericsson.raso.sef.core.FrameworkException;

public class RuleFailedException extends FrameworkException {
	private static final long serialVersionUID = -5926982542833453655L;

	public RuleFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuleFailedException(String message) {
		super(message);
	}

	public RuleFailedException(Throwable cause) {
		super(cause);
	}

}
