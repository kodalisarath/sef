package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.CreateOrWriteServiceAccessKeyRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public class CreateOrWriteServiceAccessKey implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(CreateOrWriteRop.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("CreateOrWriteServiceAccessKey: process()");
		CreateOrWriteServiceAccessKeyRequest request = (CreateOrWriteServiceAccessKeyRequest) exchange.getIn().getBody();

		List<Meta> metas = new ArrayList<Meta>();
		metas.add(new Meta("key", request.getCategory()));
		metas.add(new Meta("keyType", request.getKeyType()));
		metas.add(new Meta("vInvalidFrom", DateUtil.convertISOToSimpleDateFormat(request.getvInvalidFrom())));
		metas.add(new Meta("messageId", String.valueOf(request.getMessageId())));

		String requestId = RequestContextLocalStore.get().getRequestId();
		
		SubscriberInfo subscriberinfo = readSubscriber(requestId, request.getCustomerId(),null);
		
		
if(subscriberinfo ==null)
{
logger.error("Subscriber Not Found. msisdn: "
		+ request.getCustomerId());	
throw ExceptionUtil.toSmException(ErrorCode.nonExistentAccount);
}
else
		if (!ContractState.PREACTIVE.name().equals(subscriberinfo.getLocalState())) {
			
			logger.error("Subscriber should be in GRACE state to extend the graceEndDate. msisdn: "
					+ request.getCustomerId());	
			throw ExceptionUtil.toSmException(ErrorCode.notPreActive);
		}

		
		updateSubscriber(requestId, request.getCustomerId(), metas);
		DummyProcessor.response(exchange);

	}
	
	private SubscriberInfo readSubscriber(String requestId, String subscriberId, List<Meta> metas){
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, subscriberId, metas);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
		semaphore.init(0);
		semaphore.acquire();
		} catch(InterruptedException e) {
		}
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;
		
	}
	
	
	private SubscriberInfo updateSubscriber(String requestId, String customer_id,List<Meta> metas) throws SmException {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");

		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();

		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);

		iSubscriberRequest.updateSubscriber(requestId,customer_id, metas);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch(InterruptedException e) {
			logger.warn("Interrupted while waiting for response to " + requestId);
		}
		
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		if(subscriberInfo != null){
			
			try{
				if(subscriberInfo.getStatus().getCode() > 0){
					ResponseCode resonseCode = new ResponseCode(subscriberInfo.getStatus().getCode(),subscriberInfo.getStatus().getDescription());
					throw new SmException(resonseCode);
					}
			}catch(Exception e){
				logger.error("subscriberInfo fields are null");
				throw null;
			}
			
		}
		return subscriberInfo;
	}

}
