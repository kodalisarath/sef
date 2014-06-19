package com.ericsson.raso.sef.smart.processor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.CreateOrWriteCustomerRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;

public class CreateOrWriteCustomerProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(CreateOrWriteCustomerProcessor.class);
	@Override
	public void process(Exchange exchange) throws Exception {

		logger.info("CreateOrWriteCustomerProcessor: process()");

		CreateOrWriteCustomerRequest request = (CreateOrWriteCustomerRequest) exchange.getIn().getBody();

		// Metas...
		Map<String,String> metas = new HashMap<String,String>();
		metas.put("Category", request.getCategory());
		metas.put("billCycleId", String.valueOf(request.getBillCycleId()));
		metas.put("MessageId",  String.valueOf(request.getMessageId()));
		metas.put("Package",  "initialSC");
		

		// Subscriber...RequestContextLocalStore
		String requestId = RequestContextLocalStore.get().getRequestId();
		Subscriber subscriber = new Subscriber();
		subscriber.setMsisdn(request.getCustomerId());
		subscriber.setMetas(metas);
		logger.debug("Subscriber Metas: " + metas);

		// Creating user first...
		SubscriberInfo subscriberInfo=createSubscriber(requestId,subscriber);
		if (subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() >0) {
			logger.debug("Backend Error: " + subscriberInfo.getStatus().getCode());
			throw ExceptionUtil.toSmException(new ResponseCode(subscriberInfo.getStatus().getCode(), subscriberInfo.getStatus().getDescription()));
			
		}
		logger.debug("Response purchase received.. now creating front end response");
		//exchange.getOut().setBody(subscriberInfo);
		DummyProcessor.response(exchange);		
	}

	private SubscriberInfo createSubscriber(String requestId, Subscriber subscriber) {
		logger.info("Invoking create subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();

		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
         logger.debug("Creating a subscriber,calling method with metas of size"+subscriber.getMetas().size());
		iSubscriberRequest.createSubscriber(requestId, subscriber);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch(InterruptedException e) {
			logger.error("Interrupted from waiting for response... This use case might cause inconsistencies...");
		}

		logger.info("Check if response received for create subscriber");

		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		logger.debug("REceived Response: " + subscriberInfo);
		return subscriberInfo;
	}
}
