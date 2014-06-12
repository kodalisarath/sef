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
import com.ericsson.raso.sef.smart.usecase.ModifyPackageItemRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public class ModifyPackageItem implements Processor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void process(Exchange exchange) throws Exception {
		try {
			ModifyPackageItemRequest request = (ModifyPackageItemRequest) exchange.getIn().getBody();
			
			String requestId = RequestContextLocalStore.get().getRequestId();
			
				//SubscriberManagement subscriberManagement = SmartContext.getSubscriberManagement();
				//Subscriber subscriber = subscriberManagement.getSubscriberProfile(request.getCustomerId(), null);
				SubscriberInfo subscriberInfo = readSubscriber(requestId,request.getCustomerId(),null);
				if (!ContractState.PREACTIVE.name().equals(subscriberInfo.getLocalState())) {
					logger.error("Subscriber:" + request.getCustomerId() + "is not in preactive stage. Opeartion can not be performed");
						throw ExceptionUtil.toSmException(ErrorCode.notPreActive);
				}
				
				List<Meta> metas = new ArrayList<Meta>();
				metas.add(new Meta("package", request.getPackaze()));
				metas.add(new Meta("messageId", String.valueOf(request.getMessageId())));
				metas.add(new Meta("federation-profile", "modifypackageItem"));

				//subscriberManagement.updateSubscriber(request.getCustomerId(), metas);
				updateSubscriber(requestId, request.getCustomerId(), metas,Constants.ModifyCustomerPreActive);
		} catch (Exception e) {
			logger.error("Error in process class:",e.getClass().getName(),e);
		}
		
		
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
	private SubscriberInfo updateSubscriber(String requestId, String customer_id,List<Meta> metas,String useCase) {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.updateSubscriber(requestId,customer_id, metas,useCase);
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
}

