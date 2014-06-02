package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.StatusCode;

public final class StepExecutionException extends FrameworkException {
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
	public StepExecutionException(String component, StatusCode code) {
		super(component, code);
	}
	
	public StepExecutionException(String component, StatusCode code, Throwable e) {
		super(component, code, e);
	}
	

	
}
