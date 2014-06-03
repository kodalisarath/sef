package com.ericsson.raso.sef.bes.engine.transaction.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionServiceHelper;
import com.ericsson.sef.bes.api.entities.Meta;

public class ReadSubscriberProcessor implements Processor{
	
	private static final Logger logger = LoggerFactory.getLogger(ReadSubscriberProcessor.class);

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange arg0) throws Exception {
		
		logger.debug("Read Subscriber Profile request received!!");
		Object[] objectArray = (Object[]) arg0.getIn().getBody(Object.class);
		String requestId = (String) objectArray[0];
		String subscriberId = (String) objectArray[1];
		List<Meta> metas = (List<Meta>) objectArray[2];
		logger.debug("Before delegation...");
		TransactionManager transactionManager = ServiceResolver.getTransactionManager();
		transactionManager.readSubscriber(requestId, subscriberId, TransactionServiceHelper.getMap(metas));
		
	}
	
}
