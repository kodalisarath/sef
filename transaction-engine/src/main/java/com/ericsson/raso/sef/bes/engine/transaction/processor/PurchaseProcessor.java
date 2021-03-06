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

public class PurchaseProcessor implements Processor{

	Logger logger = LoggerFactory.getLogger(PurchaseProcessor.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange arg0) throws Exception {
		try{
			Object[] objectArray = arg0.getIn().getBody(Object[].class);
			String requestId = (String) objectArray[0];
			String offerId = (String)objectArray[1];
			String subscriberId=(String)objectArray[2];
			logger.debug("requestID: " + requestId);
			logger.debug("offerId: " + offerId);
			logger.debug("subscriberId: " + subscriberId);
			boolean override=(boolean)objectArray[3];
			List<Meta> metas= (List<Meta>)objectArray[4];
			TransactionManager transactionManager=ServiceResolver.getTransactionManager();
			String req = transactionManager.purchase(requestId, offerId, subscriberId, override, convertToList(metas));
			 
			logger.debug("Transaction manager correlationID: " + req);
			
		}catch(Exception e){
			logger.error("Error in processor class :",this.getClass().getName(),e);
		}
		
		
	}
	/*Method to convert a list to a map*/
	private Map<String,Object> convertToList(List<Meta> metas){
		Map<String,Object> metaMap = new HashMap<String,Object>();
		for(Meta meta: metas){
			metaMap.put(meta.getKey(), meta.getValue());
		}
		logger.debug("List Metas converted to Map Metas... Contents: " + metaMap);
		return metaMap;
		
	}

}
