package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

public final class StepExecutionException extends Exception {
	private static final long	serialVersionUID	= 2133541011151799027L;

	StepExecutionException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	StepExecutionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	StepExecutionException(String arg0) {
		super(arg0);
	}

	
}
