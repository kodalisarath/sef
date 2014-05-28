package com.ericsson.raso.sef.bes.engine.transaction.processor;

import javax.xml.ws.Holder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;
import com.ericsson.sef.bes.api.entities.Subscriber;

public class CreateSubscriberProcessor implements Processor{

	@Override
	public void process(Exchange arg0) throws Exception {
		Object[] objectArray = (Object[]) arg0.getIn().getBody(Object.class);
		Holder<String> requestId = (Holder<String>)objectArray[0];
		Holder<com.ericsson.sef.bes.api.entities.Subscriber> subscriber = (Holder<com.ericsson.sef.bes.api.entities.Subscriber>)objectArray[1];
		//Subscriber subscriberTransaction= ServiceResolver.parseToCoreSubscriber(subscriber.value);
		TransactionManager transactionManager = ServiceResolver.getTransactionManager();
		transactionManager.createSubscriber(requestId.value, subscriber.value);
		
	}

}
