package com.ericsson.raso.sef.smart.subscriber.response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class CreateSubscriberProcessor implements Processor{

	Logger logger = LoggerFactory.getLogger(CreateSubscriberProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Object[] objectArray=(Object[]) exchange.getIn().getBody(Object[].class);
		String requestCorrelator = (String)objectArray[0];
		logger.debug(requestCorrelator);
	 	TransactionStatus fault =(TransactionStatus)objectArray[1];
	    boolean result=(Boolean)objectArray[2];
	    SubscriberResponseHandler subscriberResponsehandler=new SubscriberResponseHandler();
	    subscriberResponsehandler.createSubscriber(requestCorrelator, fault, result);
		
	}

}
