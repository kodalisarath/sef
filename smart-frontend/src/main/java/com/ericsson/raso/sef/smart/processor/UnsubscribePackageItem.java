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
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.UnSubscribePackageItemRequest;
import com.ericsson.raso.sef.watergate.FloodGate;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class UnsubscribePackageItem implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(UnsubscribePackageItem.class);
	private static final String READ_SUBSCRIBER_OFFER_INFO_OFFER = "READ_SUBSCRIBER_OFFER_INFO";

	@Override
	public void process(Exchange exchange) throws Exception {

		UnSubscribePackageItemRequest request = (UnSubscribePackageItemRequest) exchange.getIn().getBody();
		String requestId = RequestContextLocalStore.get().getRequestId();
		List<Meta> metas = new ArrayList<Meta>();
		logger.info("Collecting SOAP parameters");
		metas.add(new Meta("AccessKey",request.getAccessKey()));
		metas.add(new Meta("Package",request.getPackaze()));
		metas.add(new Meta("MessageId",String.valueOf(request.getMessageId())));
		metas.add(new Meta("msisdn",request.getCustomerId()));
		metas.add(new Meta(Constants.EX_DATA1, request.getPackaze()));

		List<Meta> workflowMetas= new ArrayList<Meta>();
		workflowMetas.add(new Meta("Package", String.valueOf(request.getPackaze())));
		workflowMetas.add(new Meta("msisdn",request.getCustomerId()));
		workflowMetas.add(new Meta(Constants.EX_DATA1, request.getPackaze()));
		workflowMetas.add(new Meta("MessageId",String.valueOf(request.getMessageId())));
		workflowMetas.add(new Meta("AccessKey",request.getAccessKey()));
		List<Meta> metaSubscriber=new ArrayList<Meta>();
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		ISubscriptionRequest iSubscriptionRequest = SmartServiceResolver.getSubscriptionRequest();

		metaSubscriber.add(new Meta("SUBSCRIBER_ID",request.getCustomerId()));
		metaSubscriber.add(new Meta("READ_SUBSCRIBER","PARTIAL_READ_SUBSCRIBER"));


		logger.info("Collected SOAP parameters");
		logger.info("Going for DB check and AIR call");
	
		SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), metaSubscriber);

		logger.info("subscriber call done");

		if (subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() >0){
			logger.debug("Inside the if condition for status check");
			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
		}
		logger.info("Recieved a SubscriberInfo Object and it is not null");
		logger.info("Printing subscriber onject value "+subscriberObj.getSubscriber());

		IConfig config = SefCoreServiceResolver.getConfigService();
		String activeStatusCS = subscriberObj.getSubscriber().getMetas().get("READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG");
		logger.debug("TRY 1 "+ activeStatusCS);
		String packagefromDB = subscriberObj.getSubscriber().getMetas().get("Package");
		logger.debug("PACKAGE FROM DB "+ packagefromDB);
		if (packagefromDB == null || packagefromDB.equalsIgnoreCase("")){
			packagefromDB = subscriberObj.getSubscriber().getMetas().get("package");
			logger.debug("PACKAGE FROM DB "+ packagefromDB);
		}

		String originalWelcomePackSC = config.getValue("GLOBAL_welcomePackMapping", "initialSC");
		logger.debug("REQUESTED WELCOME PACK SC "+ originalWelcomePackSC);

		int usecase=0;
		logger.info("Failfast if pre-active and package subscribed to different from initial welcome pack");
		if (activeStatusCS.equalsIgnoreCase("false")) {
			usecase=1;
		}

		if(usecase==1) {
			logger.info("check pre_active");			
			metas.add(new Meta("HANDLE_LIFE_CYCLE","UnsubscribePackageItem"));
			metas.add(new Meta("ServiceClass",originalWelcomePackSC));
			String resultId=iSubscriberRequest.handleLifeCycle(requestId, request.getCustomerId(), null, metas);
			SubscriberInfo response = new SubscriberInfo();
			SubscriberResponseStore.put(resultId, response);
			logger.debug("Got past event class....YEAH");
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

			SubscriberInfo subscriberResponse = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
			logger.debug("subscriberResponse recieved here is "+subscriberResponse);
			if(subscriberResponse.getStatus() != null && subscriberResponse.getStatus().getCode() >0) {
				logger.debug("Fetch Subscriber failed....");
				throw ExceptionUtil.toSmException(new ResponseCode(subscriberResponse.getStatus().getCode(), 
													subscriberResponse.getStatus().getDescription()));
			}
			else{
				CommandResponseData cr = this.createResponse(true);
				String edrIdentifier = (String)exchange.getIn().getHeader("EDR_IDENTIFIER"); 
				
				logger.error("FloodGate acknowledging exgress...");
				FloodGate.getInstance().exgress();
				
				exchange.getOut().setBody(cr);
				exchange.getOut().setHeader("EDR_IDENTIFIER", edrIdentifier);
			
			}
		}
		else {
			Subscriber subscriber = subscriberObj.getSubscriber();
			Map<String, String> subscriberMetas = subscriber.getMetas();
			logger.info("SK GET METAS BALANCE " + subscriberMetas);
			
			logger.info("check grace and recycle metas as subscriber is not pre-active");
			OfferInfo oInfo = null;
			Map<String, OfferInfo> subscriberOffers = new HashMap<String, UnsubscribePackageItem.OfferInfo>(); 
			boolean isGrace = false;
			boolean inRecycle = false;
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
						isGrace=true;
					} else if (oInfo.offerID.equals("4")) {
						logger.debug("FLEXI:: CUSTOMER IN RECYCLE!!!");
						inRecycle=true;
					} else {
						logger.debug("FLEXI:: CUSTOMER IN ACTIVE!!!");
						workflowMetas.add(new Meta("Package", "Rev" + String.valueOf(request.getPackaze())));

					}
				}
			}
			if (inRecycle==false) {
				
				workflowMetas.add(new Meta("msisdn", request.getCustomerId()));
				workflowMetas.add(new Meta("SUBSCRIBER_ID", request.getCustomerId()));

				workflowMetas.add(new Meta(Constants.CHANNEL_NAME, "UREG"));
				workflowMetas.add(new Meta(Constants.EX_DATA3, "UREG"));

				workflowMetas.add(new Meta(Constants.EX_DATA1, "Rev" + request.getPackaze()));
				workflowMetas.add(new Meta("eventName", "Rev" + request.getPackaze()));
				workflowMetas.add(new Meta(Constants.EX_DATA2, request.getEventInfo()));
				workflowMetas.add(new Meta("eventInfo", request.getEventInfo()));
				
				ISubscriptionRequest subscriptionRequest = SmartServiceResolver.getSubscriptionRequest();
				String correlationId = subscriptionRequest.purchase(requestId, "Rev" + request.getPackaze(), request.getCustomerId(), true, workflowMetas);
				PurchaseResponse response = new PurchaseResponse();
				logger.debug("Got past event class in subscription for grace and active....");
				RequestCorrelationStore.put(correlationId, response);
				ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(correlationId);

				try {
					semaphore.init(0);
					semaphore.acquire();
				} catch(InterruptedException e) {
					e.printStackTrace();
					logger.debug("Exception while sleep     :"+e.getMessage());
				}
				semaphore.destroy();
				

				logger.debug("Awake from sleep.. going to check response in store with id: " +  correlationId);

				// PurchaseResponse purchaseResponse = (PurchaseResponse) SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.SMFE_TXE_CORRELLATOR);
				PurchaseResponse purchaseResponse = (PurchaseResponse) RequestCorrelationStore.remove(correlationId);
				//PurchaseResponse purchaseResponse = (PurchaseResponse) RequestCorrelationStore.get(correlationId);
				logger.debug("PurchaseResponse recieved here is "+purchaseResponse);
				
				if(purchaseResponse.getFault() != null && purchaseResponse.getFault().getCode() >0) {
					logger.debug("No response arrived???");
					throw new SmException(ErrorCode.internalServerError);
				} else if (purchaseResponse.getFault() != null && purchaseResponse.getFault().getCode() >0 ) {
					logger.debug("");
					throw ExceptionUtil.toSmException(new ResponseCode(purchaseResponse.getFault().getCode(), purchaseResponse.getFault().getDescription()));
				} else{
					CommandResponseData cr = this.createResponse(true);
					String edrIdentifier = (String)exchange.getIn().getHeader("EDR_IDENTIFIER"); 
					
					logger.error("FloodGate acknowledging exgress...");
					FloodGate.getInstance().exgress();
					
					exchange.getOut().setBody(cr);
					exchange.getOut().setHeader("EDR_IDENTIFIER", edrIdentifier);
				}	
			}
			else {
				throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
			}
		}
	}

	private static boolean isWelcomePack(String packaze) {
		IConfig config = SefCoreServiceResolver.getConfigService();
		if (config.getValue("GLOBAL_welcomePackMapping", packaze)!=null)
			return true;
		else return false;

	}


	private SubscriberInfo readSubscriber(String requestId, String subscriberId, List<Meta> metas) {
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, subscriberId, metas);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
		}
		semaphore.destroy();
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;

	}

	private SubscriberInfo updateSubscriber(String requestId, String customer_id, List<Meta> metas,String useCase) {
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
		semaphore.destroy();
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;
	}

	private CommandResponseData createResponse(boolean isTransactional) throws SmException {
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);
		OperationResult operationResult = new OperationResult();
		Operation operation = new Operation();
		operation.setName("Unsubscribe");
		operation.setModifier("PackageItem");
		operationResult.getOperation().add(operation);
		ParameterList parameterList = new ParameterList();

		if(isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}

		return responseData;
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
