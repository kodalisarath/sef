package com.ericsson.raso.sef.smart.subscription.response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class PreRenewResponseProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(PreRenewResponseProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {

		logger.debug("PreRenew Response arrived!!!");
		Object[] objectArray=(Object[]) exchange.getIn().getBody(Object[].class);
		String correlationId = (String)objectArray[0];
	 	TransactionStatus fault =(TransactionStatus)objectArray[1];
	 	Boolean result = (Boolean) objectArray[2]; 
	 	
	 	SubscriptionResponseHandler responseHandler = new SubscriptionResponseHandler();
	 	logger.debug("Notifying handler with correlationID: " + correlationId + " result: " + result);
	 	responseHandler.preRenewal(correlationId, fault, result);
	}

}
