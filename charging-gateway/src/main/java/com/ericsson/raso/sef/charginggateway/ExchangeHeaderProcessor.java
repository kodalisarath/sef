package com.ericsson.raso.sef.charginggateway;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangeHeaderProcessor implements Processor {

	private static Logger log = LoggerFactory.getLogger(ExchangeHeaderProcessor.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setHeader(Exchange.HTTP_METHOD,
				org.apache.camel.component.http4.HttpMethods.POST);
		exchange.getIn().setHeader(Exchange.HTTP_QUERY,
				"connectionsPerRoute=100");
		log.debug("Adding the HTTP Method and HTTP Query Param");
	}
}
