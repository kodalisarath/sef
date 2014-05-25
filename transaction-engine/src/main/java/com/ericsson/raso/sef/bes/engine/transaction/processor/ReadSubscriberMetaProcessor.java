package com.ericsson.raso.sef.bes.engine.transaction.processor;

import java.util.Set;

import javax.xml.ws.Holder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;

public class ReadSubscriberMetaProcessor implements Processor{

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange arg0) throws Exception {
		Object[] objectArray = (Object[]) arg0.getIn().getBody(Object.class);
		Holder<String> requestId = (Holder<String>)objectArray[0];
		Holder<String> subscriberId = (Holder<String>)objectArray[1];
		Holder<Set> metaNames = (Holder<Set>)objectArray[2];
		TransactionManager transactionManager = ServiceResolver.getTransactionManager();
		transactionManager.readSubscriberMeta(requestId.value, subscriberId.value, metaNames.value);
	}

}
