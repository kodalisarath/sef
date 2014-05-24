package com.ericsson.raso.sef.fulfillment.processors;

import org.apache.camel.Exchange;
import org.slf4j.MDC;

import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.core.camelprocessors.RequestIdGenerator;

public class FulfilmentRequestIdProcessor extends RequestIdGenerator {
	
	@Override
	public void process(Exchange arg0) throws Exception {
		//Fetch universal requestId & set for logging
		String requestId = fetchRequestIdFromRequest(arg0);
		if(requestId != null)
			MDC.put("requestId", requestId);
		
		//Generate correlationId and pass it on exchange
		String correlationId = UniqueIdGenerator.generateId();
		arg0.getIn().setHeader("CORRELATIONID", correlationId);
	}

	@Override
	public String fetchRequestIdFromRequest(Exchange arg0) {
		String requestId = arg0.getIn().getBody(String.class);
		return requestId;
	}

}
