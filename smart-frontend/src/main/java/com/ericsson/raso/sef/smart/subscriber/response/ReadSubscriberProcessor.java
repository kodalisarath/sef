package com.ericsson.raso.sef.smart.subscriber.response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class ReadSubscriberProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Object[] objectArray=(Object[]) exchange.getIn().getBody(Object[].class);
		String correlationId = (String)objectArray[0];
	 	TransactionStatus fault =(TransactionStatus)objectArray[1];
	 	Subscriber subscriber =(Subscriber)objectArray[2];
	 	
	 	
	 	
	}

}
