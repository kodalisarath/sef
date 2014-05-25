package com.ericsson.raso.sef.bes.engine.transaction.processor;

import javax.xml.ws.Holder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;

public class DeleteSubscriberProcessor implements Processor {

	@Override
	public void process(Exchange arg0) throws Exception {
		Object[] objectArray = (Object[]) arg0.getIn().getBody(Object.class);
		Holder<String> requestId = (Holder<String>)objectArray[0];
		Holder<String> subscriberId=(Holder<String>)objectArray[1];
		TransactionManager transactionManager = ServiceResolver.getTransactionManager();
		transactionManager.deleteSubscriber(requestId.value, subscriberId.value);
	}

}
