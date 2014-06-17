package com.ericsson.raso.sef.smppgateway;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DummyProcessor implements Processor {

	
	public void process(Exchange exchange) throws Exception {
		System.out.println("In the process");

	}

}
