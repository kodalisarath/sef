package com.ericsson.raso.sef.bes.engine.transaction.processor;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;

public class HandleLifeCycleProcessor implements Processor{

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange arg0) throws Exception {
		String requestId = arg0.getIn().getBody(String.class);
		String subscriberId =arg0.getIn().getBody(String.class);
		String lifeCycleState = arg0.getIn().getBody(String.class);
		Map<String,String> metas = arg0.getIn().getBody(Map.class);
		TransactionManager transactionManager = ServiceResolver.getTransactionManager();
		transactionManager.handleLifeCycle(requestId, subscriberId, lifeCycleState, metas);
	}

}
