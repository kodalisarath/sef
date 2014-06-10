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
import com.ericsson.raso.sef.smart.usecase.CreateOrWriteRopRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;



public class CreateOrWriteRop implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(CreateOrWriteRop.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		try {
			
			logger.info("CreateOrWriteCustomerProcessor: process()");
			CreateOrWriteRopRequest request = (CreateOrWriteRopRequest) exchange.getIn().getBody();
			
			
			//SubscriberManagement subscriberManagement = SmartContext.getSubscriberManagement();
			List<Meta> metas = new ArrayList<Meta>();
			metas.add(new Meta("key", String.valueOf(request.getKey())));
			metas.add(new Meta("isSmsAllowed", String.valueOf(request.getIsSmsAllowed())));
			metas.add(new Meta("isUSCAllowed", String.valueOf(request.getIsUSCAllowed())));
			metas.add(new Meta("isGPRSUsed", String.valueOf(request.getIsGPRSUsed())));
			metas.add(new Meta("isLastTransactionEnqUsed", String.valueOf(request.getIsLastTransactionEnqUsed())));
			metas.add(new Meta("languageId", String.valueOf(request.getLanguageId())));
			
			if(request.getActiveEndDate()!=null)
				metas.add(new Meta("activeEndDate",DateUtil.convertISOToSimpleDateFormat(request.getActiveEndDate())));
			
			if(request.getGraceEndDate()!=null)
				metas.add(new Meta("graceEndDate", DateUtil.convertISOToSimpleDateFormat(request.getGraceEndDate())));
			
			String preActiveEndDate = DateUtil.convertISOToSimpleDateFormat(request.getPreActiveEndDate());
			
			if(request.getPreActiveEndDate()!=null)
				metas.add(new Meta("preActiveEndDate",preActiveEndDate));

			metas.add(new Meta("firstCallDate", request.getFirstCallDate()));
			metas.add(new Meta("isFirstCallPassed", String.valueOf(request.getIsFirstCallPassed())));
			metas.add(new Meta("lastKnownPeriod", request.getLastKnownPeriod()));
			metas.add(new Meta("category", request.getCategory()));
			metas.add(new Meta("messageId", String.valueOf(request.getMessageId())));

			//subscriberManagement.updateSubscriber(request.getCustomerId(), metas);
			String requestId = RequestContextLocalStore.get().getRequestId();
			updateSubscriber(requestId, request.getCustomerId(), metas);
			logger.info("Invoking handleLifeCycle on tx-engine subscriber interface");
			
			/* 	 @To Do.  To be completed after scheduler is ready.*/
			if(preActiveEndDate!=null) {
					//	Date preActiveEndDateDt=		new StringToDateTransformer(SmartContext.getProperty(SmartContext.DATE_FORMAT)).transform(preActiveEndDate);				
				// Subscriber subscriber = subscriberManagement.getSubscriberProfile(request.getCustomerId(), null);
				// RescheduleRecycleCommand command = new RescheduleRecycleCommand(subscriber, preActiveEndDateDt);
				//command.execute();
			}
			logger.info("Sending subscriber response");
			DummyProcessor.response(exchange);
			
		} catch (Exception e) {
			logger.error("Error in the processor:",e.getClass().getName(),e);
		}
		
		
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
			logger.error("Error while calling acquire()");
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
