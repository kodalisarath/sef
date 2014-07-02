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

public class ExpiryProcessor implements Processor {

	Logger logger = LoggerFactory.getLogger(ExpiryProcessor.class);

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange arg0) throws Exception {
		try {

			logger.debug("Inside Process Method of ExpiryProcessor ");
			Object[] objectArray = arg0.getIn().getBody(Object[].class);
			String requestId = (String) objectArray[0];
			String subscriptionId = (String) objectArray[1];
			logger.debug("requestID: " + requestId);
			logger.debug("subscriptionId: " + subscriptionId);
			boolean override = (boolean) objectArray[2];
			List<Meta> metas = (List<Meta>) objectArray[3];
			TransactionManager transactionManager = ServiceResolver
					.getTransactionManager();
			String req = transactionManager.expiry(requestId, subscriptionId,
					override, convertToList(metas));
			logger.debug("Transaction manager correlationID: " + req);
		} catch (Exception e) {
			logger.error("Error in processor class :", this.getClass()
					.getName(), e);
		}
	}

	/* Method to convert a list to a map */
	private Map<String, Object> convertToList(List<Meta> metas) {
		Map<String, Object> metaMap = new HashMap<String, Object>();
		for (Meta meta : metas) {
			metaMap.put(meta.getKey(), meta.getValue());
		}
		return metaMap;

	}
}
