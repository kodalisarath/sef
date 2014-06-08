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
		updateSubscriber(requestId, request.getCustomerId(), metas);
		DummyProcessor.response(exchange);

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
		if(subscriberInfo.getStatus().getCode() > 0){
			ResponseCode responseCode=new ResponseCode(subscriberInfo.getStatus().getCode() , subscriberInfo.getStatus().getDescription());
			throw new SmException(responseCode);
		}
		return subscriberInfo;
	}

}
