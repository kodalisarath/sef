package com.ericsson.raso.sef.core.camelprocessors;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public abstract class RequestExitProcessor implements Processor {

	@Override
	public void process(Exchange arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public abstract void  cleanup();
}
