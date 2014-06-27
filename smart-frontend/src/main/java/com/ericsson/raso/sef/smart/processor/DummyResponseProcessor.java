package com.ericsson.raso.sef.smart.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

//
public class DummyResponseProcessor implements Processor {
	
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		//Don't implement any code here. This is just pass through processor for handling wiretap responses.
	}
	
}
