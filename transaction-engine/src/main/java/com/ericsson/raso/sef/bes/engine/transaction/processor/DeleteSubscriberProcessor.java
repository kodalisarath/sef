package com.ericsson.raso.sef.bes.engine.transaction.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;

public class DeleteSubscriberProcessor implements Processor {

	@Override
	public void process(Exchange arg0) throws Exception {
		final Logger logger = LoggerFactory.getLogger(DeleteSubscriberProcessor.class);
		logger.debug("Entering CreateSubscriberProcessor");
		try{
			Object[] objectArray = (Object[]) arg0.getIn().getBody(Object[].class);
			String requestId = (String)objectArray[0];
			String subscriberId=(String)objectArray[1];
			TransactionManager transactionManager = ServiceResolver.getTransactionManager();
			transactionManager.deleteSubscriber(requestId, subscriberId);
		}catch(Exception e){
			logger.error("Error in the processor class:",this.getClass().getName(),e);
		}
		
	}

}
