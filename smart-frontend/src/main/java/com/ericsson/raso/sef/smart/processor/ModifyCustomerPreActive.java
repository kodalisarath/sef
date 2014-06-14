package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.ModifyCustomerPreActiveRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public class ModifyCustomerPreActive implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(ModifyCustomerPreActive.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		ModifyCustomerPreActiveRequest request = (ModifyCustomerPreActiveRequest) exchange
				.getIn().getBody();
		String requestId = RequestContextLocalStore.get().getRequestId();
		List<Meta> metas = new ArrayList<Meta>();
		metas.add(new Meta("eventInfo",String.valueOf(request.getEventInfo())));
		metas.add(new Meta("messageId",String.valueOf(request.getMessageId())));
		metas.add(new Meta("customerId",String.valueOf(request.getCustomerId())));
		SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), metas);
		SubscriberInfo subscriberInfo=null;
		
		//ISubscriberResponse response=
		if(subscriberObj ==  null)
			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
		
		if (subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() >0)
			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
		logger.info("Recieved a SubscriberInfo Object and it is not null");
		logger.info("Printing subscriber onject value "+subscriberObj.getSubscriber());
		
		
		if(ContractState.apiValue("PRE_ACTIVE").toString().equals(subscriberObj.getSubscriber().getContractState().toString())){
			
			String date=subscriberObj.getSubscriber().getMetas().get("activeEndDate");
			String newDate=DateUtil.addDaysToDate(date,request.getDaysOfExtension());
			metas.add(new Meta("daysOfExtension",String.valueOf(newDate)));
			subscriberInfo= updateSubscriber(requestId, request.getCustomerId(), metas, Constants.ModifyCustomerPreActive);
			logger.info("Before read subscriber call");
			if(subscriberInfo.getStatus() != null){
				throw ExceptionUtil.toSmException(ErrorCode.invalidOperationState);
			}
		}else{
			throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
		}
		//DummyProcessor.response(exchange);
		exchange.getOut().setBody(subscriberInfo);
		
	}
	
	
	private SubscriberInfo readSubscriber(String requestId,String customer_id, List<Meta> metas) {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, customer_id, metas);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {

		}
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;
	}
	private SubscriberInfo updateSubscriber(String requestId,String customer_id, List<Meta> metas,String useCase) {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.updateSubscriber(requestId, customer_id, metas,useCase);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {

		}
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		return subscriberInfo;
	}

	

}
