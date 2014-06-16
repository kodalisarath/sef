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
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.processor.BalanceAdjustment.OfferInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.SubscribePackageItemRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ListParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class SubscribePackageItem implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(SubscribePackageItem.class);
	private static final String READ_SUBSCRIBER_OFFER_INFO_OFFER = "READ_SUBSCRIBER_OFFER_INFO";
	@Override
	public void process(Exchange exchange) throws Exception {
		SubscribePackageItemRequest request = (SubscribePackageItemRequest) exchange.getIn().getBody();
		String requestId = RequestContextLocalStore.get().getRequestId();
		List<Meta> metas = new ArrayList<Meta>();
		logger.info("Collecting SOAP parameters");
		metas.add(new Meta("AccessKey",request.getAccessKey()));
		metas.add(new Meta("Package",request.getPackaze()));
		metas.add(new Meta("MessageId",String.valueOf(request.getMessageId())));
		List<Meta> workflowMetas= new ArrayList<Meta>();
		workflowMetas.add(new Meta("packaze", String.valueOf(request.getPackaze())));
		List<Meta> metaSubscriber=new ArrayList<Meta>();
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		ISubscriptionRequest iSubscriptionRequest = SmartServiceResolver.getSubscriptionRequest();
		
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
			  if(isWelcomePack(request.getPackaze())) {
				  IConfig config = SefCoreServiceResolver.getConfigService();
				  metas.add(new Meta("HANDLE_LIFE_CYCLE","SUBSCRIBER_PACKAGE_ITEM_WelcomePackServiceClass"));
				  metas.add(new Meta("ServiceClass",config.getValue("GLOBAL_welcomePackMapping", request.getPackaze())));
				  String resultId=iSubscriberRequest.handleLifeCycle(requestId, request.getCustomerId(), null, metas);
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
					else{
						 CommandResponseData cr = this.createResponse(true);
					      exchange.getOut().setBody(cr);
					}
			  }
			  else {
				  throw ExceptionUtil.toSmException(ErrorCode.invalidEventName);
			  }
			}
		  
		  Subscriber subscriber = subscriberObj.getSubscriber();
			if (subscriber == null) {
				logger.error("Unable to fetch the subscriber entity out");
				throw ExceptionUtil.toSmException(ErrorCode.technicalError);
			}
			
			logger.info("SK GET METAS BALANCE " + subscriber.getMetas());
		  
		    logger.info("check grace and recycle metas as subscriber is not pre-active");
			Map<String, String> subscriberMetas = subscriber.getMetas();
			OfferInfo oInfo = null;
			Map<String, OfferInfo> subscriberOffers = new HashMap<String, SubscribePackageItem.OfferInfo>(); 
			boolean IsGrace = false;
			boolean NotRecycle = false;
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
					if (oInfo.offerID.equals("4")) {
						logger.debug("FLEXI:: CUSTOMER IN RECYCLE!!!");
						NotRecycle=true;
					}
				}
			}
			if (IsGrace==true || NotRecycle==false) {
				    String resultId=iSubscriptionRequest.purchase(requestId, request.getPackaze(), request.getCustomerId(), true, workflowMetas);
			        PurchaseResponse response = new PurchaseResponse();
					logger.debug("Got past event class in subscription for grace and active....");
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
					else{
						 CommandResponseData cr = this.createResponse(true);
					      exchange.getOut().setBody(cr);
					}	
			}
			else {
				throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
			}
			     
	}


	private static boolean isWelcomePack(String packaze) {
		IConfig config = SefCoreServiceResolver.getConfigService();
		if (config.getValue("GLOBAL_welcomePackMapping", packaze)!=null)
			return true;
		else return false;

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
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;
	}

	private CommandResponseData createResponse(boolean isTransactional) throws SmException {
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);
		OperationResult operationResult = new OperationResult();
		Operation operation = new Operation();
		operation.setName("Subscribe");
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