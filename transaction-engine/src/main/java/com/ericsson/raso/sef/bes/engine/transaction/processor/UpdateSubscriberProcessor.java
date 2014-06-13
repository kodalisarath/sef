package com.ericsson.raso.sef.bes.engine.transaction.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionManager;
import com.ericsson.sef.bes.api.entities.Meta;

public class UpdateSubscriberProcessor implements Processor{
	private static final Logger logger = LoggerFactory.getLogger(ReadSubscriberProcessor.class);
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange arg0) throws Exception {
		try {
			Object[] objectArray = (Object[]) arg0.getIn().getBody(Object[].class);
			String requestId = (String) objectArray[0];
			String subscriberId = (String) objectArray[1];
			List<Meta> metas=(List<Meta>) objectArray[2];
			String useCase=(String) objectArray[3];
			logger.debug("Here iis the use case recieved"+useCase);
			TransactionManager transactionManager = ServiceResolver.getTransactionManager();
			transactionManager.updateSubscriber(requestId, subscriberId, getMap(metas),useCase);
		} catch (Exception e) {
			logger.error("Error in processor class:",this.getClass().getName(),e);
		}
		
	}
	
	private Map<String,String> getMap(List<Meta> metas) {
		
		Map<String, String> map = new HashMap<String, String>();
		if(metas != null) {
			for(Meta meta: metas) {
				map.put(meta.getKey(), meta.getValue());
			}
		}
		return map;
		
	}

}
