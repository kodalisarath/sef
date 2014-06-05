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
		Object[] objectArray = (Object[]) arg0.getIn().getBody(Object[].class);
		String requestId = (String)objectArray[0];
		com.ericsson.sef.bes.api.entities.Subscriber subscriber = (com.ericsson.sef.bes.api.entities.Subscriber)objectArray[1];
		logger.debug(subscriber.getMsisdn());
		//Subscriber subscriberTransaction= ServiceResolver.parseToCoreSubscriber(subscriber.value);
		TransactionManager transactionManager = ServiceResolver.getTransactionManager();
		transactionManager.createSubscriber(requestId, subscriber);
		
	}

}
