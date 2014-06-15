package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.processor.CARecharge.OfferInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.ModifyCustomerGraceRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public class ModifyCustomerGrace implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(ModifyCustomerGrace.class);
	private static final String READ_SUBSCRIBER_OFFER_INFO_OFFER = "READ_SUBSCRIBER_OFFER";
	@Override
	public void process(Exchange exchange) throws Exception {
		
			
			ModifyCustomerGraceRequest request = (ModifyCustomerGraceRequest) exchange.getIn().getBody();
			
			String requestId = RequestContextLocalStore.get().getRequestId();
			//SubscriberInfo subscriberInfo = readSubscriber(requestId,request.getCustomerId(),null);
			
			
			List<String> keys = new ArrayList<String>();
			keys.add(SmartConstants.GRACE_ENDDATE);
			
			List<Meta> metas = new ArrayList<Meta>();
			metas.add(new Meta("EventInfo" , String.valueOf(request.getEventInfo())));
			metas.add(new Meta("MessageId" , String.valueOf(request.getMessageId())));
			metas.add(new Meta("AccessKey",request.getAccessKey()));
			List<Meta> metasReadSubscriber = new ArrayList<Meta>();
			metasReadSubscriber.add(new Meta("HANDLE_LIFE_CYCLE", "ModifyCustomerGrace"));
			metasReadSubscriber.add(new Meta("READ_SUBSCRIBER", "PARTIAL_READ_SUBSCRIBER"));
			
			ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
			//ISubscriptionRequest iSubscriptionRequest = SmartServiceResolver.getSubscriptionRequest();
			logger.info("Before read subscriber call");
			SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), metasReadSubscriber);
			//if(subscriberObj.getSubscriber() == null)
			//ISubscriberResponse response=
			
			if (subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() >0){
				logger.debug("Inside the if condition for status check");
				throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
			}
			logger.info("Recieved a SubscriberInfo Object and it is not null");
			logger.info("Printing subscriber onject value "+subscriberObj.getSubscriber());
			if(ContractState.PREACTIVE.getName().equals(subscriberObj.getSubscriber().getContractState()))
				throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
			
					 
					 Map<String, String> subscriberMetas = subscriberObj.getMetas();
//						int index = 0;
//						String offers = "";
						OfferInfo oInfo = null;
						Map<String, OfferInfo> subscriberOffers = new HashMap<String, ModifyCustomerGrace.OfferInfo>(); 
						boolean IsGrace = false;
						for (String key: subscriberMetas.keySet()) {
							logger.debug("FLEXI:: processing meta:" + key + "=" + subscriberMetas.get(key));
							if (key.startsWith(READ_SUBSCRIBER_OFFER_INFO_OFFER)) {
								logger.debug("FLEXI:: OFFER_ID...." + subscriberMetas.get(key));
								String offerForm = subscriberMetas.get(key);
								
								String offerParts[] = offerForm.split(",");
								String offerId = offerParts[0];
								String start = offerParts[1];
								if (start.equals("null"))
									start = offerParts[2];
								String expiry = offerParts[3];
								if (expiry.equals("null"))
									expiry = offerParts[4];
								
								oInfo = new OfferInfo(offerId, Long.parseLong(expiry), Long.parseLong(start), null, null); 
								subscriberOffers.put(offerId, oInfo);
								logger.debug("FLEXI:: OFFER_INFO: " + oInfo);

								if (oInfo.offerID.equals("2")) {
									logger.debug("FLEXI:: CUSTOMER IN GRACE!!!");
									IsGrace=true;
								}
							}
						}
						if (IsGrace==false) {
							throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
							
						}
						
					String resultId=iSubscriberRequest.handleLifeCycle(requestId, request.getCustomerId(), ContractState.GRACE.getName(), metas);
				     PurchaseResponse response = new PurchaseResponse();
					logger.debug("Got past event class....");
						RequestCorrelationStore.put(resultId, response);
						String date=subscriberObj.getSubscriber().getMetas().get("GraceEndDate");
						if(date != null){
							String newDate=DateUtil.addDaysToDate(date,request.getDaysOfExtension());
							metas.add(new Meta("GraceEndDate",String.valueOf(newDate)));
						}
						SubscriberInfo subscriberInfo= updateSubscriber(requestId, request.getCustomerId(), metas, Constants.ModifyCustomerGrace);
						if(subscriberInfo.getStatus() != null){
							throw ExceptionUtil.toSmException(ErrorCode.invalidOperationState);
						}
						exchange.getOut().setBody(subscriberInfo);
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
		//SubscriberResponseStore.get(requestId);
		return subscriberInfo;
	}
	
	class OfferInfo {
		private String offerID;
		private long offerExpiry;
		private long offerStart;
		private String daID;
		private String walletName;
		
		public OfferInfo() {}
		
		public OfferInfo(String offerID, long offerExpiry, long offerStart, String daID, String walletName) {
			super();
			this.offerID = offerID;
			this.offerExpiry = offerExpiry;
			this.offerStart = offerStart;
			this.daID = daID;
			this.walletName = walletName;
		}


		@Override
		public String toString() {
			return "OfferInfo [offerID=" + offerID + ", offerExpiry=" + offerExpiry + ", daID=" + daID + ", walletName=" + walletName + "]";
		}
		
		
	}
	
}
