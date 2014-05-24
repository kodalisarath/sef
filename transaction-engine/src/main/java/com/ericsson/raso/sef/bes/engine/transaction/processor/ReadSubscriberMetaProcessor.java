package com.ericsson.raso.sef.bes.engine.transaction.processor;

import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;

public class ReadSubscriberMetaProcessor implements Processor{

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange arg0) throws Exception {
		String requestId = arg0.getIn().getBody(String.class);
		String subscriberId = arg0.getIn().getBody(String.class);
		Set<String> metaNames = arg0.getIn().getBody(Set.class);
		TransactionManager transactionManager = ServiceResolver.getTransactionManager();
		transactionManager.readSubscriberMeta(requestId, subscriberId, metaNames);
	}

}
