package com.ericsson.raso.sef.bes.engine.transaction.processor;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;

public class UpdateSubscriberProcessor implements Processor{

	@Override
	public void process(Exchange arg0) throws Exception {
		Object[] objectArray = (Object[]) arg0.getIn().getBody(Object[].class);
		String requestId = (String) objectArray[0];
		String subscriberId = (String) objectArray[1];
		Map metas=(Map) objectArray[2];
		TransactionManager transactionManager = ServiceResolver.getTransactionManager();
		transactionManager.updateSubscriber(requestId, subscriberId, metas);
	}

}
