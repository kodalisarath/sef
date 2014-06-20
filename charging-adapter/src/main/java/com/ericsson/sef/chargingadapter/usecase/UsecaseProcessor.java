package com.ericsson.sef.chargingadapter.usecase;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.raso.sef.core.Constants;

public class UsecaseProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		String operation = (String) exchange.getIn().getHeader(Constants.operationName);
		Usecase usecase = Usecase.getUsecaseByOperation(operation);
		usecase.getRequestProcessor().process(exchange);
	}
}
