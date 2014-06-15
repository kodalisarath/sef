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
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.CreateOrWriteServiceAccessKeyRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public class CreateOrWriteServiceAccessKey implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(CreateOrWriteServiceAccessKey.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("CreateOrWriteServiceAccessKey: process()");
		CreateOrWriteServiceAccessKeyRequest request = (CreateOrWriteServiceAccessKeyRequest) exchange.getIn().getBody();
		logger.info("Printing values from SOAP before Processing :"+request.getCategory()+" "+request.getKeyType()+" "+request.getvValidFrom()+" "+request.getMessageId());
		List<Meta> metas = new ArrayList<Meta>();
		metas.add(new Meta("Key", request.getCategory()));
		metas.add(new Meta("KeyType", String.valueOf(request.getKeyType())));
		metas.add(new Meta("vValidFrom", request.getvValidFrom()));
		metas.add(new Meta("MessageId", String.valueOf(request.getMessageId())));

		String requestId = RequestContextLocalStore.get().getRequestId();
		
		SubscriberInfo subscriberInfo = updateSubscriber(requestId,request.getCustomerId(), metas,Constants.CreateOrWriteServiceAccessKey);
		exchange.getOut().setBody(subscriberInfo);
	if (subscriberInfo.getStatus() != null) {
		
		throw ExceptionUtil.toSmException(new ResponseCode(subscriberInfo.getStatus().getCode(),subscriberInfo.getStatus().getDescription()));
		
	}
	DummyProcessor.response(exchange);

	}
	
	private SubscriberInfo updateSubscriber(String requestId,
			String customer_id, List<Meta> metas,String useCase) throws SmException {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver
				.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		logger.debug("Requesting ");
		iSubscriberRequest.updateSubscriber(requestId, customer_id, metas,useCase);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			logger.error("Error while calling acquire()");
		}
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		
		return subscriberInfo;
	}

}
