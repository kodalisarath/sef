package com.ericsson.raso.sef.fulfillment.commons;

import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.StatusCode;

public class FulfillmentException extends FrameworkException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3320035247553002761L;
	
	public FulfillmentException(String component, StatusCode code) {
		super(component, code);
	}
	
	public FulfillmentException(String component, StatusCode code, Throwable e) {
		super(component, code, e);
	}
	
}
