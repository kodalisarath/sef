package com.ericsson.raso.sef.core.camelprocessors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.ericsson.raso.sef.core.RequestContext;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.UniqueIdGenerator;

public abstract class RequestIdGenerator implements Processor {

	Logger logger = LoggerFactory.getLogger(RequestIdGenerator.class);
	
	@Override
	public void process(Exchange arg0) throws Exception {
		
		String requestId = fetchRequestIdFromRequest(arg0);
		if(requestId == null) {
			requestId = UniqueIdGenerator.generateId();
			MDC.put("requestId", requestId);
			logger.info("New request Id generated: " + requestId);
			RequestContext ctx = new RequestContext();
			ctx.setRequestId(requestId);
			RequestContextLocalStore.put(ctx);
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
