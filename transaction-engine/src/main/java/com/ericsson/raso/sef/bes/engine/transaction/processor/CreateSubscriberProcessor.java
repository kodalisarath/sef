package com.ericsson.raso.sef.bes.engine.transaction.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;

public class CreateSubscriberProcessor implements Processor{

	private static final Logger logger = LoggerFactory.getLogger(CreateSubscriberProcessor.class);
	
	@Override
	public void process(Exchange arg0) throws Exception {
		logger.debug("Entering CreateSubscriberProcessor");
		try{
			Object[] objectArray = (Object[]) arg0.getIn().getBody(Object[].class);
			String requestId = (String)objectArray[0];
			com.ericsson.sef.bes.api.entities.Subscriber subscriber = (com.ericsson.sef.bes.api.entities.Subscriber)objectArray[1];
			logger.debug("Checking subscriber in CreateSubscriberProcessor: " + subscriber);
			//Subscriber subscriberTransaction= ServiceResolver.parseToCoreSubscriber(subscriber.value);
			TransactionManager transactionManager = ServiceResolver.getTransactionManager();
			logger.debug("Request ID and Subscriber ID" + requestId + " " + subscriber);
			transactionManager.createSubscriber(requestId, subscriber);
			
		}catch(Exception e){
			logger.error("Error in the processor class:",this.getClass().getName(),e);
		}
		
		
	}

}
