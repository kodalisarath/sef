package com.ericsson.raso.sef.smart.subscription.response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.sef.bes.api.entities.Offer;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class DiscoverOfferByFederatedIdResponseProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(DiscoverOfferByFederatedIdResponseProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {

		logger.debug("Discover Offer with Federated Id Response arrived!!!");
		Object[] objectArray=(Object[]) exchange.getIn().getBody(Object[].class);
		logger.debug("back to basics... Object[] size: " + objectArray.length);
		String correlationId = (String)objectArray[0];
	 	TransactionStatus fault = null;
	 	if (objectArray[1] != null)
	 		fault = (TransactionStatus)objectArray[1];
	 	
	 	Offer offer = null;
	 	if (objectArray[2] != null)
	 		offer = (Offer) objectArray[2]; 
		
	 	SubscriptionResponseHandler responseHandler = new SubscriptionResponseHandler();
	 	logger.debug("Notifying handler with correlationID: " + correlationId + " fetched Offer: " + offer);
	 	responseHandler.discoverOfferByFederatedId(correlationId, fault, offer);
	}

}
