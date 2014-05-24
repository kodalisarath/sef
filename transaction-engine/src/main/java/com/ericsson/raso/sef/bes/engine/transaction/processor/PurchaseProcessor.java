package com.ericsson.raso.sef.bes.engine.transaction.processor;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;

public class PurchaseProcessor implements Processor{

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange arg0) throws Exception {
		String requestId = arg0.getIn().getBody(String.class);
		String offerId = arg0.getIn().getBody(String.class);
		String subscriberId=arg0.getIn().getBody(String.class);
		Map<String, Object> metas=arg0.getIn().getBody(Map.class);
		Boolean override=arg0.getIn().getBody(Boolean.class);
		TransactionManager transactionManager=ServiceResolver.getTransactionManager();
		transactionManager.purchase(requestId, offerId, subscriberId, override, metas);
		
	}

}
