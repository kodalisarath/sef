package com.ericsson.raso.sef.smart.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class VersionRetrieveReadROP implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		
		 String edrIdentifier = (String)exchange.getIn().getHeader("EDR_IDENTIFIER");
         
        exchange.getOut().setHeader("EDR_IDENTIFIER", edrIdentifier);
		
	}
	
}
