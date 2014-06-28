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
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.processor.ModifyCustomerGrace.OfferInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.BalanceAdjustmentRequest;
import com.ericsson.raso.sef.watergate.FloodGate;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;
import com.hazelcast.core.ISemaphore;



public class BalanceAdjustment implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(BalanceAdjustment.class);
	private static final String READ_SUBSCRIBER_OFFER_INFO_OFFER = "READ_SUBSCRIBER_OFFER_INFO";
	private Long FixedAmountOfUnits;


	@Override
	public void process(Exchange exchange) throws Exception {

		logger.info("BalanceAdjustment: process()");

		BalanceAdjustmentRequest request = (BalanceAdjustmentRequest) exchange.getIn().getBody();
		String requestId = RequestContextLocalStore.get().getRequestId();
		List<Meta> metas = new ArrayList<Meta>();
		FixedAmountOfUnits = request.getAmountOfUnits();
		FixedAmountOfUnits = FixedAmountOfUnits * -1; 
		ISubscriptionRequest iSubscriptionRequest = SmartServiceResolver.getSubscriptionRequest();
		logger.info("Collecting SOAP parameters");
		metas.add(new Meta("customerId", request.getCustomerId()));
		metas.add(new Meta("accessKey", request.getAccessKey()));
		metas.add(new Meta("balanceId",  request.getBalanceId()));
		metas.add(new Meta("amountOfUnits", String.valueOf(FixedAmountOfUnits)));
		metas.add(new Meta("chargeCode",String.valueOf(request.getChargeCode())));
		metas.add(new Meta("messageId",String.valueOf(request.getMessageId())));
		metas.add(new Meta("messageId",String.valueOf(request.getMessageId())));
		metas.add(new Meta("eventInfo",request.getEventInfo()));
		List<Meta> workflowMetas= new ArrayList<Meta>();
		workflowMetas.add(new Meta("msisdn", String.valueOf(request.getCustomerId())));
		workflowMetas.add(new Meta("eventName", String.valueOf(request.getChargeCode())));
		workflowMetas.add(new Meta("balanceId", String.valueOf(request.getBalanceId())));
		workflowMetas.add(new Meta("eventInfo",request.getEventInfo()));

		workflowMetas.add(new Meta("amountOfUnits", String.valueOf(FixedAmountOfUnits)));
		List<Meta> metaSubscriber=new ArrayList<Meta>();
		metaSubscriber.add(new Meta("SUBSCRIBER_ID",request.getCustomerId()));
		metaSubscriber.add(new Meta("READ_SUBSCRIBER","PARTIAL_READ_SUBSCRIBER"));

		logger.info("Collected SOAP parameters");
		logger.info("Going for DB check and AIR call");
		logger.info("Before read subscriber call");

		SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), metaSubscriber);

		logger.info("subscriber call done");
		if (subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() >0){
			logger.debug("Inside the if condition for status check");
			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
		}
		logger.info("Recieved a SubscriberInfo Object and it is not null");
		logger.info("Printing subscriber onject value "+subscriberObj.getSubscriber());


		logger.info("check pre_active");			
		if(ContractState.PREACTIVE.getName().equals(subscriberObj.getSubscriber().getContractState())) {
			throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
		}
		else {
			logger.info("check grace and recycle metas");
			//logger.info("SK GET METAS BALANCE " + subscriberObj.getMetas());

			Subscriber subscriber = subscriberObj.getSubscriber();
			if (subscriber == null) {
				logger.error("Unable to fetch the subscriber entity out");
				throw ExceptionUtil.toSmException(ErrorCode.technicalError);
			}

			logger.info("SK GET METAS BALANCE " + subscriber.getMetas());


			Map<String, String> subscriberMetas = subscriber.getMetas();
			//				int index = 0;
			//				String offers = "";
			OfferInfo oInfo = null;
			Map<String, OfferInfo> subscriberOffers = new HashMap<String, BalanceAdjustment.OfferInfo>(); 
			boolean IsGraceRecycle = false;
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

					if (oInfo.offerID.equals("2") || oInfo.offerID.equals("4")) {
						logger.debug("FLEXI:: CUSTOMER IN GRACE!!!");
						IsGraceRecycle=true;
					}
				}
			}

			if (IsGraceRecycle==true) {
				throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);

			}
		}
		String resultId=iSubscriptionRequest.purchase(requestId, request.getBalanceId(), request.getCustomerId(), true, workflowMetas);
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
		semaphore.destroy();


		logger.debug("Awake from sleep.. going to check response in store with id: " +  resultId);

		PurchaseResponse purchaseResponse = (PurchaseResponse) RequestCorrelationStore.remove(requestId);

		logger.debug("PurchaseResponse recieved here is "+purchaseResponse);
		if(purchaseResponse == null) {
			logger.debug("No response arrived???");
			throw new SmException(ErrorCode.internalServerError);
		}
		
		logger.error("FloodGate acknowledging exgress...");
		FloodGate.getInstance().exgress();

		DummyProcessor.response(exchange);
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
		semaphore.destroy();
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		logger.debug("Hi HERE I AM::: Result " + subscriberInfo.getStatus() );
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
