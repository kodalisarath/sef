package com.ericsson.raso.sef.smart.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.watergate.FloodGate;

public class VersionRetrieveReadROP implements Processor{
	private static final Logger logger = LoggerFactory.getLogger(VersionRetrieveReadROP.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub

		
		
		String edrIdentifier = (String)exchange.getIn().getHeader("EDR_IDENTIFIER");

		logger.error("FloodGate acknowledging exgress...");
		FloodGate.getInstance().exgress();
		exchange.getOut().setHeader("EDR_IDENTIFIER", edrIdentifier);

	}

}
