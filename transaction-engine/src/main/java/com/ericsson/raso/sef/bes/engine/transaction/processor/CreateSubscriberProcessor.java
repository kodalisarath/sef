package com.ericsson.raso.sef.bes.engine.transaction.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;
import com.ericsson.sef.bes.api.entities.Subscriber;

public class CreateSubscriberProcessor implements Processor{

	@Override
	public void process(Exchange arg0) throws Exception {
		
		String requestId = arg0.getIn().getBody(String.class);
		com.ericsson.sef.bes.api.entities.Subscriber subscriber = arg0.getIn().getBody(Subscriber.class);
		com.ericsson.raso.sef.bes.engine.transaction.entities.Subscriber subscriberTransaction= ServiceResolver.parseToCoreSubscriber(subscriber);
		TransactionManager transactionManager = ServiceResolver.getTransactionManager();
		//TO:DO   to be implemented
		transactionManager.createSubscriber(requestId, subscriberTransaction);
		
	}

}
