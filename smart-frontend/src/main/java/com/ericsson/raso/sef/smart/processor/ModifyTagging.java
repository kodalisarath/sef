package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.Collection;
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
import com.ericsson.raso.sef.smart.processor.BalanceAdjustment.OfferInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.ModifyTaggingRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class ModifyTagging implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(ModifyTagging.class);
	private static final String READ_SUBSCRIBER_OFFER_INFO_OFFER = "READ_SUBSCRIBER_OFFER_INFO";

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws SmException {
		ModifyTaggingRequest request = (ModifyTaggingRequest) exchange
				.getIn().getBody();
		String requestId = RequestContextLocalStore.get().getRequestId();
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		Integer tag = Integer.valueOf(request.getTagging());
		logger.error("Subscriber Tagging Code is:", tag);
//
		List<Meta> metas = new ArrayList<Meta>();
		List<Meta> metaSubscriber=new ArrayList<Meta>();
		List<Meta> workflowMetas= new ArrayList<Meta>();
		metas.add(new Meta("CustomerId", String.valueOf(request.getCustomerId())));
		metas.add(new Meta("msisdn", String.valueOf(request.getCustomerId())));
		metas.add(new Meta("AccessKey", String.valueOf(request.getAccessKey())));
		metas.add(new Meta("Tagging", String.valueOf(request.getTagging())));
		metas.add(new Meta("EventInfo", String.valueOf(request.getEventInfo())));
		metas.add(new Meta("MessageId", String.valueOf(request.getMessageId())));

		metaSubscriber.add(new Meta("SUBSCRIBER_ID",request.getCustomerId()));
	    metaSubscriber.add(new Meta("READ_SUBSCRIBER","PARTIAL_READ_SUBSCRIBER"));
//		Object[] objectArray = (Object[]) exchange.getIn().getBody(Object[].class);
//		metas.add(new Meta("customerId", (String) objectArray[0].toString()));
//		metas.add(new Meta("accessKey", (String) objectArray[1].toString()));
//		metas.add(new Meta("tagging", (String)objectArray[2].toString()));
//		metas.add(new Meta("eventInfo", (String)objectArray[3].toString()));
//		metas.add(new Meta("messageId", (String)objectArray[4].toString()));

		String tagging = String.valueOf(request.getTagging());
		//Integer tag = Integer.valueOf(tagging);
		
		switch (tag) {
			case 0: metas.add(new Meta("HANDLE_LIFE_CYCLE", "MODIFY_SUBSCRIBER_TAGGING_SetResetBit"));
					break;
			case 1: metas.add(new Meta("HANDLE_LIFE_CYCLE", "MODIFY_SUBSCRIBER_TAGGING_SetForcedDeleteBit"));
					break;
			case 2: metas.add(new Meta("HANDLE_LIFE_CYCLE", "MODIFY_SUBSCRIBER_TAGGING_SetBarGeneralBit"));
					break;
			case 3: metas.add(new Meta("HANDLE_LIFE_CYCLE", "MODIFY_SUBSCRIBER_TAGGING_SetBarIRMBit"));
					break;
			case 4:	metas.add(new Meta("HANDLE_LIFE_CYCLE", "MODIFY_SUBSCRIBER_TAGGING_SetBarOtherBit"));
					break;
			case 5:	metas.add(new Meta("HANDLE_LIFE_CYCLE", "MODIFY_SUBSCRIBER_TAGGING_SetSpecialFraudBit"));
					break;
			case 6:	metas.add(new Meta("HANDLE_LIFE_CYCLE", "MODIFY_SUBSCRIBER_TAGGING_AccountActivationBlockingBit"));
					break;
			case 7:	metas.add(new Meta("HANDLE_LIFE_CYCLE", "MODIFY_SUBSCRIBER_TAGGING_SetRecycleBit"));
					break;
			default:
					throw ExceptionUtil.toSmException(ErrorCode.invalidOperationState);
		}
		logger.debug("Usecase Metas: " + metas);
		
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
		
		Subscriber subscriber = subscriberObj.getSubscriber();
		if (subscriber == null) {
			logger.error("Unable to fetch the subscriber entity out");
			throw ExceptionUtil.toSmException(ErrorCode.technicalError);
		}
		
		logger.info("SK GET METAS BALANCE " + subscriber.getMetas());
		
		
		Map<String, String> subscriberMetas = subscriber.getMetas();
//		int index = 0;
//		String offers = "";
		OfferInfo oInfo = null;
		Map<String, OfferInfo> subscriberOffers = new HashMap<String, ModifyTagging.OfferInfo>(); 
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

				if (oInfo.offerID.equals("4")) {
					logger.debug("FLEXI:: CUSTOMER IN RECYCLE!!!");
					IsGraceRecycle=true;
					throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
				}
			}
		}
		
		String resultId=iSubscriberRequest.handleLifeCycle(requestId, request.getCustomerId(), null, metas);
		SubscriberInfo response = new SubscriberInfo();
	      logger.debug("Got past event class....SK");
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

		
		logger.debug("Awake from sleep.. going to check response in store with id: " +  resultId);
		
		
		SubscriberInfo purchaseResponse = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		logger.debug("PurchaseResponse recieved here is "+purchaseResponse);
		if(purchaseResponse == null) {
			logger.debug("No response arrived???");
			throw new SmException(ErrorCode.internalServerError);
		}
		else{
			 CommandResponseData cr = this.createResponse(true,request.getTagging());
		      exchange.getOut().setBody(cr);
		}	
}
		

	private CommandResponseData createResponse(boolean isTransactional, int tag ) {
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);

		OperationResult operationResult = new OperationResult();

		if (isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}

		Operation operation = new Operation();
		operation.setModifier("Tagging");
		operation.setName("Modify");
		operationResult.getOperation().add(operation);

		ParameterList parameterList = new ParameterList();
		List<Object> dataSetList = parameterList.getParameterOrBooleanParameterOrByteParameter();

		IntParameter intParameter = new IntParameter();
		intParameter.setName("ResultTagging");
		intParameter.setValue(tag);
		dataSetList.add(intParameter);

		operation.setParameterList(parameterList);

		return responseData;
	}


	private SubscriberInfo updateSubscriber(String requestId, String customer_id, List<Meta> metas,String processRequestKey) throws SmException {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		logger.debug("Requesting ");
		iSubscriberRequest.updateSubscriber(requestId, customer_id, metas, processRequestKey);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			logger.error("Error while calling acquire()");
		}
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		return subscriberInfo;
	}

	private SubscriberInfo updateSubscriberHandleLifeCycle(String requestId, String msisdn, List<Meta> metas) {
		logger.info("Invoking update subscriber on tx-engine subscriber interface for hande modifyhandle life cycle");

		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();

		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.handleLifeCycle(requestId, msisdn, null, metas);

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
	

	
	

