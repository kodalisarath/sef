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
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
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
		IConfig config = SefCoreServiceResolver.getConfigService();
		String requestId = RequestContextLocalStore.get().getRequestId();
		List<Meta> metas = new ArrayList<Meta>();
		metas.add(new Meta("eventInfo",String.valueOf(request.getEventInfo())));
		metas.add(new Meta("messageId",String.valueOf(request.getMessageId())));
		metas.add(new Meta("customerId",String.valueOf(request.getCustomerId())));
		metas.add(new Meta("daysOfExtension",String.valueOf(request.getDaysOfExtension())));
		//To:DO Find an alternative to distinguish between the use-cases,currently its been added as a Meta key,
		metas.add(new Meta("HANDLE_LIFE_CYCLE","ModifyCustomerPreactive"));
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		logger.info("Before read subscriber call");
		SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), metas);
		if(subscriberObj.getSubscriber() == null){
			throw ExceptionUtil.toSmException(new ResponseCode(504,"Subscriber Not Found"));
		}
		    logger.info("check pre_active");			
			if(ContractState.apiValue("PRE_ACTIVE").toString().equals(subscriberObj.getSubscriber().getContractState().toString())){
				String resultId=iSubscriberRequest.handleLifeCycle(requestId, request.getCustomerId(), ContractState.PREACTIVE.getName(), metas);
			     PurchaseResponse response = new PurchaseResponse();
				logger.debug("Got past event class....");
					RequestCorrelationStore.put(resultId, response);
					ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
					
					try {
					semaphore.init(0);
					semaphore.acquire();
					} catch(InterruptedException e) {
						e.printStackTrace();
						logger.debug("Exception while sleep     :"+e.getMessage());
					}
					SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
					if(subscriberInfo.getStatus() != null)
					{
						throw ExceptionUtil.toSmException(new ResponseCode(subscriberInfo.getStatus().getCode(),subscriberInfo.getStatus().getDescription()));
					}
					DummyProcessor.response(exchange);
					
					
			}else{
				throw ExceptionUtil.toSmException(ErrorCode.invalidOperationState);
			}
		
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

	

}
