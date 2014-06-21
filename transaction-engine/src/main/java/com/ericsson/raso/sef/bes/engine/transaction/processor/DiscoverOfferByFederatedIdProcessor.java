package com.ericsson.raso.sef.bes.engine.transaction.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;

public class DiscoverOfferByFederatedIdProcessor  implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(DiscoverOfferByFederatedIdProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		try{
			Object[] objectArray = exchange.getIn().getBody(Object[].class);
			String requestId = (String) objectArray[0];
			String handle = (String)objectArray[1];
			String subscriberId=(String)objectArray[2];
			logger.debug("requestID: " + requestId);
			logger.debug("handle: " + handle);
			logger.debug("subscriberId: " + subscriberId);
			TransactionManager transactionManager=ServiceResolver.getTransactionManager();
			String req = transactionManager.discoverOfferByFederatedId(requestId, handle, subscriberId);
			 
			logger.debug("Transaction manager correlationID: " + req);
			
		}catch(Exception e){
			logger.error("Error in processor class :",this.getClass().getName(),e);
		}
		
	}
	

}
