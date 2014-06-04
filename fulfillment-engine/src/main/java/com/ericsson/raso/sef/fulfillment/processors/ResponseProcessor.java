package com.ericsson.raso.sef.fulfillment.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(ResponseProcessor.class); 
	@Override
	public void process(Exchange arg0) throws Exception {
		
		// Set correlationId in the the response body
		String correlationId = (String) arg0.getIn().getHeader("CORRELATIONID");
		logger.debug("I have a correlation_id in ResponseProcessor: " + correlationId);
		arg0.getOut().setBody(correlationId);
	}

}
