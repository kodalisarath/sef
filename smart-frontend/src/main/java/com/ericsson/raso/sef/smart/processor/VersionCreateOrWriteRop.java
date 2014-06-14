package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.VersionCreateOrWriteROPRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public class VersionCreateOrWriteRop implements Processor {
	private static final Logger logger = LoggerFactory
			.getLogger(VersionCreateOrWriteRop.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		
			VersionCreateOrWriteROPRequest request = (VersionCreateOrWriteROPRequest) exchange
					.getIn().getBody();
			List<Meta> metas = new ArrayList<Meta>();
			metas.add(new Meta("Key", String.valueOf(request.getKey())));
			metas.add(new Meta("vValidFrom", request.getvValidFrom()));
			metas.add(new Meta("vInvalidFrom", request.getvInvalidFrom()));
			metas.add(new Meta("s_OfferId", request.getS_OfferId()));
			metas.add(new Meta("category", request.getCategory()));
			metas.add(new Meta("MessageId", String.valueOf(request.getMessageId())));
			metas.add(new Meta("Precision", String.valueOf(request.getPrecision())));
			metas.add(new Meta("ExpiryDate", DateUtil.convertISOToSimpleDateFormat(request.getExpiryDate())));
			String requestId = RequestContextLocalStore.get().getRequestId();
			
			SubscriberInfo subscriberInfo = updateSubscriber(requestId,request.getCustomerId(), metas,Constants.VersionCreateOrWriteRop);
			
			exchange.getOut().setBody(subscriberInfo);
             if (subscriberInfo.getStatus() != null) {
				
			throw ExceptionUtil.toSmException(new ResponseCode(subscriberInfo.getStatus().getCode(),subscriberInfo.getStatus().getDescription()));
				
			} 

	}

	private SubscriberInfo updateSubscriber(String requestId,
			String customer_id, List<Meta> metas,String useCase) throws SmException {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver
				.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.updateSubscriber(requestId, customer_id, metas,useCase);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);

		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {

		}
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		if(subscriberInfo != null){
			try{
			if(subscriberInfo.getStatus().getCode() > 0){
				if(subscriberInfo.getStatus().getCode() != 504){
					if(!ContractState.PREACTIVE.name().equals(subscriberInfo.getLocalState()))
					{
						ResponseCode responseCode=new ResponseCode(4020,"Invalid Operation State");
						throw new SmException(responseCode);
					}
				}
				}
		}catch(Exception e){
			logger.error("subscriberInfo fields are null",e.getMessage(),e);
			
		}
			}
		return subscriberInfo;
	}

}
