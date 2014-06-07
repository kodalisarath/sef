package com.ericsson.raso.sef.core.db.service;

import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.StatusCode;

public final class PersistenceError extends FrameworkException {
	private static final long serialVersionUID = 8934988537691219565L;

	public PersistenceError(String nbCorrelator, String component, StatusCode code) {
		super(component, code);
	}

	public PersistenceError(String nbCorrelator, String component, StatusCode code, Throwable e) {
		super(component, code, e);
	}


}