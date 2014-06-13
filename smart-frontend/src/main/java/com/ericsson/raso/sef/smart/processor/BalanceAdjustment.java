package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.BalanceAdjustmentRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;
import com.hazelcast.core.ISemaphore;



public class BalanceAdjustment implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(CreateOrWriteCustomerProcessor.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		
			 logger.info("BalanceAdjustment: process()");
				
		     BalanceAdjustmentRequest request = (BalanceAdjustmentRequest) exchange.getIn().getBody();
		     String requestId = RequestContextLocalStore.get().getRequestId();
		     List<Meta> metas = new ArrayList<Meta>();
		     ISubscriptionRequest iSubscriptionRequest = SmartServiceResolver.getSubscriptionRequest();
		     logger.info("Collecting SOAP parameters");
		     metas.add(new Meta("customerId", request.getCustomerId()));
		     metas.add(new Meta("accessKey", request.getAccessKey()));
		     metas.add(new Meta("balanceId",  request.getBalanceId()));
		     metas.add(new Meta("amountOfUnits", String.valueOf(request.getAmountOfUnits())));
		     metas.add(new Meta("chargeCode",String.valueOf(request.getChargeCode())));
		     metas.add(new Meta("messageId",String.valueOf(request.getMessageId())));
		     metas.add(new Meta("messageId",String.valueOf(request.getMessageId())));
		     metas.add(new Meta("eventInfo",request.getEventInfo()));
		     List<Meta> workflowMetas= new ArrayList<Meta>();
		     workflowMetas.add(new Meta("eventName", String.valueOf(request.getChargeCode())));
		     workflowMetas.add(new Meta("balanceId", String.valueOf(request.getBalanceId())));
		     workflowMetas.add(new Meta("eventInfo",request.getEventInfo()));
		     workflowMetas.add(new Meta("amountOfUnits", String.valueOf(request.getAmountOfUnits())));
		     List<Meta> metaSubscriber=new ArrayList<Meta>();
		     metaSubscriber.add(new Meta("SUBSCRIBER_ID",request.getCustomerId()));
		     metaSubscriber.add(new Meta("READ_SUBSCRIBER","PARTIAL_READ_SUBSCRIBER"));
		     
		     logger.info("Collected SOAP parameters");
		     logger.info("Going for DB check and AIR call");
		     logger.info("Before read subscriber call");
             
		     SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), metaSubscriber);
		     
		     logger.info("subscriber call done");
			if(subscriberObj.getSubscriber() == null){
				throw ExceptionUtil.toSmException(new ResponseCode(504,"Subscriber Not Found"));
			}
			  logger.info("check pre_active");			
			if(ContractState.apiValue("PRE_ACTIVE").toString().equals(subscriberObj.getSubscriber().getContractState().toString())){
				throw ExceptionUtil.toSmException(ErrorCode.invalidOperationState);
			}else{
				
				logger.info("check grace and recycle metas");
				   Map<String, String> subscriberMetas = subscriberObj.getMetas();
					String offers = "";
					for (String key: subscriberMetas.keySet()) {
						if (key.contains(".")) {
							if (key.startsWith("READ_SUBSCRIBER_OFFER_INFO_OFFER_ID"))
								offers += "," + subscriberMetas.get(key) + " ";
						}
						
					}
					if (offers.contains(",2 ") || offers.contains(",4 ")) {
						throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
						
					}
					String resultId=iSubscriptionRequest.purchase(requestId, request.getBalanceId(), request.getCustomerId(), true, metaSubscriber);
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

						
						logger.debug("Awake from sleep.. going to check response in store with id: " +  resultId);
						
						PurchaseResponse purchaseResponse = (PurchaseResponse) SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.SMFE_TXE_CORRELLATOR);
						
						//PurchaseResponse purchaseResponse = (PurchaseResponse) RequestCorrelationStore.get(correlationId);
						logger.debug("PurchaseResponse recieved here is "+purchaseResponse);
						if(purchaseResponse == null) {
							logger.debug("No response arrived???");
							throw new SmException(ErrorCode.internalServerError);
						}
						DummyProcessor.response(exchange);
			}
				
			
		}
		     
	private SubscriberInfo readSubscriber(String requestId, String customerId,
			List<Meta> metas) {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, customerId, metas);
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
