package com.ericsson.raso.sef.fulfillment.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ResponseProcessor implements Processor {

	@Override
	public void process(Exchange arg0) throws Exception {
		
		// Set correlationId in the the response body
		String correlationId = (String) arg0.getIn().getHeader("CORRELATIONID");
		arg0.getOut().setBody(correlationId);
	}

}
