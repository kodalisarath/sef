package com.ericsson.raso.sef.smart.processor;

import org.apache.camel.Exchange;

import com.ericsson.raso.sef.core.camelprocessors.RequestIdGenerator;

public class RequestIdProcessor extends RequestIdGenerator {

	@Override
	public String fetchRequestIdFromRequest(Exchange arg0) {
		//Intentionally return null as SMART front end is supposed to generate the request
		return null;
	}

}
