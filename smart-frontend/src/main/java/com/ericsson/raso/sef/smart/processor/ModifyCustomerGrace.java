package com.ericsson.raso.sef.smart.processor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.ericsson.raso.sef.core.SmException;
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
import com.ericsson.raso.sef.smart.usecase.ModifyCustomerGraceRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateTimeParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class ModifyCustomerGrace implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(ModifyCustomerGrace.class);
	private static final String READ_SUBSCRIBER_OFFER_INFO_OFFER = "READ_SUBSCRIBER_OFFER_INFO";
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
			metas.add(new Meta("DaysOfExtension", String.valueOf(request.getDaysOfExtension())));
			
			List<Meta> metasReadSubscriber = new ArrayList<Meta>();
			metas.add(new Meta("HANDLE_LIFE_CYCLE", "ModifyCustomerGrace"));
			metasReadSubscriber.add(new Meta("SUBSCRIBER_ID",request.getCustomerId()));
			metasReadSubscriber.add(new Meta("READ_SUBSCRIBER", "PARTIAL_READ_SUBSCRIBER"));
			
			logger.info("Collected SOAP parameters");
		    logger.info("Going for DB check and AIR call");
		    logger.info("Before read subscriber call");
		     
			ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
			//ISubscriptionRequest iSubscriptionRequest = SmartServiceResolver.getSubscriptionRequest();
			logger.info("Before read subscriber call");
			SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), metasReadSubscriber);
			
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
			
				logger.info("check grace metas only");
				//Date newExpiryDate = new Date();
				Subscriber subscriber = subscriberObj.getSubscriber();
				if (subscriber == null) {
					logger.error("Unable to fetch the subscriber entity out");
					throw ExceptionUtil.toSmException(ErrorCode.technicalError);
				}
				
					 Map<String, String> subscriberMetas = subscriber.getMetas();
//						int index = 0;
//						String offers = "";
						OfferInfo oInfo = null;
						Map<String, OfferInfo> subscriberOffers = new HashMap<String, ModifyCustomerGrace.OfferInfo>(); 
						boolean IsGrace = false; long graceExpiry = 0;
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

								if (oInfo.offerID.equals("1")){
									logger.debug("EXTRACTING EXISTING DATE IS" + oInfo.offerExpiry);
									graceExpiry = oInfo.offerExpiry;
								}
									
								if (oInfo.offerID.equals("2")) {
									logger.debug("FLEXI:: CUSTOMER IN GRACE!!!");
									IsGrace=true;
								}
							}
						}
						if (IsGrace==true) {
							logger.debug("Am in Grace S");
//							String date=subscriberObj.getSubscriber().getMetas().get("GraceEndDate");
//							if(date != null){
								logger.debug("There is a grace end date entered and adding days to it now");
								//String newDate=DateUtil.addDaysToDate(date,request.getDaysOfExtension());
								
								// handle the date extension
								//SimpleDateFormat metaStoreFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
								SimpleDateFormat smartDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
								//Date currentExpiryDate = metaStoreFormat.parse(date);
								Date newExpiryDate = new Date( graceExpiry + (request.getDaysOfExtension() * 86400000));
								String newExpiry = smartDateFormat.format(newExpiryDate);
								
								
								
								metas.add(new Meta("GraceEndDate",newExpiry));
								logger.debug("There is a new GraceEndDate entered and adding days to it now "+ newExpiry);
//							}
//							else{
//								logger.debug("date is not found");
//							}
							String resultId=iSubscriberRequest.handleLifeCycle(requestId, request.getCustomerId(), ContractState.GRACE.getName(), metas);
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
								
								//PurchaseResponse purchaseResponse = (PurchaseResponse) SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.SMFE_TXE_CORRELLATOR);
								//PurchaseResponse purchaseResponse = (PurchaseResponse) RequestCorrelationStore.remove(requestId);
								//PurchaseResponse purchaseResponse = (PurchaseResponse) RequestCorrelationStore.get(correlationId);
								SubscriberInfo purchaseResponse = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
								logger.debug("PurchaseResponse recieved here is "+purchaseResponse);
								if(purchaseResponse == null) {
									logger.debug("No response arrived???");
									throw new SmException(ErrorCode.internalServerError);
								}
								else{
									 CommandResponseData cr = this.createResponse(true, newExpiry);
								      exchange.getOut().setBody(cr);
								}
								
								
								
								//SubscriberInfo subscriberInfo= updateSubscriber(requestId, request.getCustomerId(), metas, Constants.ModifyCustomerGrace);
								
//								if(subscriberInfo.getStatus() != null){
//									throw ExceptionUtil.toSmException(ErrorCode.invalidOperationState);
//								}
//								exchange.getOut().setBody(subscriberInfo);
						}
//						else {
//							throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
//						}
			}						
					
					}
					
	private CommandResponseData createResponse(boolean isTransactional, String newDate ) {
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
		operation.setModifier("CustomerGrace");
		operation.setName("Modify");
		operationResult.getOperation().add(operation);

		ParameterList parameterList = new ParameterList();
		List<Object> dataSetList = parameterList.getParameterOrBooleanParameterOrByteParameter();

		DateTimeParameter dParameter = new DateTimeParameter();
		dParameter.setName("GraceEndDate");
		dParameter.setModifier(newDate);
		dataSetList.add(dParameter);

		operation.setParameterList(parameterList);

		return responseData;
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
