package com.ericsson.raso.sef.smart.subscriber.response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class DeleteSubscriberProcessor implements Processor{
	@Override
	public void process(Exchange exchange) throws Exception {
		Object[] objectArray=(Object[]) exchange.getIn().getBody(Object[].class);
		String requestCorrelator = (String)objectArray[0];
	 	TransactionStatus fault =(TransactionStatus)objectArray[1];
	    boolean result=(Boolean)objectArray[2];
	    SubscriberResponseHandler subscriberResponsehandler=new SubscriberResponseHandler();
	    subscriberResponsehandler.deleteSubscriber(requestCorrelator, fault, result);
		
	}
}
