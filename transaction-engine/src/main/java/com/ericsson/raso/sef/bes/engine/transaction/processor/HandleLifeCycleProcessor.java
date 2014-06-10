package com.ericsson.raso.sef.bes.engine.transaction.processor;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionServiceHelper;
import com.ericsson.sef.bes.api.entities.Meta;

public class HandleLifeCycleProcessor implements Processor{

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange arg0) throws Exception {
		final Logger logger = LoggerFactory.getLogger(HandleLifeCycleProcessor.class);
		try{
			Object[] objectArray = (Object[]) arg0.getIn().getBody(Object[].class);
			String requestId = (String)objectArray[0];
			String subscriberId =(String)objectArray[1];
			String lifeCycleState = (String)objectArray[2];
			List<Meta> apiMetas = (List<Meta>)objectArray[3];
			TransactionManager transactionManager = ServiceResolver.getTransactionManager();
			
			Map<String, String> sefMetas = TransactionServiceHelper.getApiMap(apiMetas);
			
			transactionManager.handleLifeCycle(requestId, subscriberId, lifeCycleState, sefMetas);
		}catch(Exception e){
		logger.error("Error in processor class:",this.getClass().getName(),e);
		}
		
	}

}
