package com.ericsson.raso.sef.core.camelprocessors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.MDC;

import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.UniqueIdGenerator;

public abstract class RequestIdGenerator implements Processor {

	@Override
	public void process(Exchange arg0) throws Exception {
		
		String requestId = fetchRequestIdFromRequest(arg0);
		if(requestId == null) {
			requestId = UniqueIdGenerator.generateId();
			MDC.put("requestId", requestId);
			RequestContextLocalStore.get().setRequestId(requestId);
		}
	}
	
	/**
	 * Exposed end point shall implement its own logic for fetching requestId from request data
	 * 
	 * @param arg0
	 * @return
	 */
	public abstract String fetchRequestIdFromRequest(Exchange arg0);

}
