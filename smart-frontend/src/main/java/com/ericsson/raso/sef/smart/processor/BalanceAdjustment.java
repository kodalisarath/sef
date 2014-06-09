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
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.BalanceAdjustmentRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;



public class BalanceAdjustment implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(CreateOrWriteCustomerProcessor.class);
	@Override
	public void process(Exchange exchange) throws Exception {
     logger.info("BalanceAdjustment: process()");
		
     BalanceAdjustmentRequest request = (BalanceAdjustmentRequest) exchange.getIn().getBody();
     IConfig config = SefCoreServiceResolver.getConfigService();
     List<Meta> metas = new ArrayList<Meta>();
     metas.add(new Meta("customerId", request.getCustomerId()));
     metas.add(new Meta("accessKey", request.getAccessKey()));
     metas.add(new Meta("balanceId",  request.getBalanceId()));
     metas.add(new Meta("amountOfUnits", String.valueOf(request.getAmountOfUnits())));
     metas.add(new Meta("chargeCode",String.valueOf(request.getChargeCode())));
     metas.add(new Meta("messageId",String.valueOf(request.getMessageId())));
     metas.add(new Meta("messageId",String.valueOf(request.getMessageId())));
     metas.add(new Meta("eventInfo",request.getEventInfo()));
     
     String requestId = RequestContextLocalStore.get().getRequestId();
     Subscriber subscriber=new Subscriber();
     updateSusbcriber(requestId, request.getCustomerId(), metas);
     
	}
	private SubscriberInfo updateSusbcriber(String requestId,String customerId,List<Meta> metas) throws SmException{
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.updateSubscriber(requestId, customerId, metas);
		
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		
		try {
		semaphore.init(0);
		semaphore.acquire();
		} catch(InterruptedException e) {
			
		}
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		if(subscriberInfo != null){
			if(subscriberInfo.getStatus().getCode() > 0){
				ResponseCode resonseCode = new ResponseCode(subscriberInfo.getStatus().getCode(),subscriberInfo.getStatus().getDescription());
				throw new SmException(resonseCode);
				}
		}
		
		return subscriberInfo;
		
	}
	
}
