package com.ericsson.raso.sef.fulfillment.processors;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.core.camelprocessors.RequestIdGenerator;

public class FulfilmentRequestIdProcessor extends RequestIdGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(FulfilmentRequestIdProcessor.class);
	@Override
	public void process(Exchange arg0) throws Exception {
		//Fetch universal requestId & set for logging
		String requestId = fetchRequestIdFromRequest(arg0);
		if(requestId != null)
			MDC.put("requestId", requestId);
		
		//Generate correlationId and pass it on exchange
		String correlationId = UniqueIdGenerator.generateId();
		logger.debug("Correlation ID: " + correlationId);
		arg0.getIn().setHeader("CORRELATIONID", correlationId);
	}

	@Override
	public String fetchRequestIdFromRequest(Exchange arg0) {
		String requestId = arg0.getIn().getBody(String.class);
		return requestId;
	}

}
