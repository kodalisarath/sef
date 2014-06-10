package com.ericsson.raso.sef.bes.engine.transaction.processor;

import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;

public class ReadSubscriberMetaProcessor implements Processor{

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange arg0) throws Exception {
		final org.slf4j.Logger logger = LoggerFactory.getLogger(ReadSubscriberMetaProcessor.class);
		try{
			Object[] objectArray = (Object[]) arg0.getIn().getBody(Object[].class);
			String requestId = (String)objectArray[0];
			String subscriberId = (String)objectArray[1];
			Set<String> metaNames = (Set<String>)objectArray[2];
			TransactionManager transactionManager = ServiceResolver.getTransactionManager();
			transactionManager.readSubscriberMeta(requestId, subscriberId, metaNames);
		}catch(Exception e ){
		logger.error("Error in processor class",this.getClass().getName(),e);
		}
		
	}

}
