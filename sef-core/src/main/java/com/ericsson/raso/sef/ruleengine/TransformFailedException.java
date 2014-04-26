package com.ericsson.raso.sef.ruleengine;

public class TransformFailedException extends Exception {
	private static final long serialVersionUID = 3725108026906883704L;

	public TransformFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransformFailedException(String message) {
		super(message);
	}

	public TransformFailedException(Throwable cause) {
		super(cause);
	}

}
