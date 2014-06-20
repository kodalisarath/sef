package com.ericsson.sef.chargingadapter.processor;

import org.apache.camel.Exchange;

import com.ericsson.raso.sef.core.Constants;


public class RequestIdentityProcessor {

	public void process(Exchange exchange) throws Exception {
		Object requestId = exchange.getIn().getHeader("referenceCode");
		if(requestId != null) {
			exchange.getIn().setHeader(Constants.REQUEST_ID, requestId.toString());
		}
	}
}
