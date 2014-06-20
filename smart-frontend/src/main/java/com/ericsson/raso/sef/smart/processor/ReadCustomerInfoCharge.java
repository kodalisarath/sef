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
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.AccountInfo;
import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.commons.SmartModel;
import com.ericsson.raso.sef.smart.commons.SmartServiceHelper;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.BalanceAdjustmentRequest;
import com.ericsson.raso.sef.smart.usecase.ReadCustomerInfoChargeRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class ReadCustomerInfoCharge implements Processor {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private static final Logger logger = LoggerFactory.getLogger(ReadCustomerInfoCharge.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		ReadCustomerInfoChargeRequest request = (ReadCustomerInfoChargeRequest) exchange.getIn().getBody();
		
		 logger.info("Customer Info Charge: process()");
	     String requestId = RequestContextLocalStore.get().getRequestId();
	     List<Meta> metas = new ArrayList<Meta>();
	     logger.info("Collecting SOAP parameters");
	     List<Meta> workflowMetas= new ArrayList<Meta>();
	     workflowMetas.add(new Meta("msisdn", String.valueOf(request.getCustomerId())));
	     workflowMetas.add(new Meta("AccessKey", String.valueOf(request.getAccessKey())));
	     workflowMetas.add(new Meta("Channel", String.valueOf(request.getChannel())));
	     workflowMetas.add(new Meta("MessageId",String.valueOf(request.getMessageId())));

	     //List<Meta> metaSubscriber=new ArrayList<Meta>();
	     workflowMetas.add(new Meta("SUBSCRIBER_ID",request.getCustomerId()));
	     workflowMetas.add(new Meta("READ_SUBSCRIBER","CUSTOMER_INFO_CHARGE"));
	     
	     logger.info("Collected SOAP parameters");
	     logger.info("Going for Customer Info Charge Call");
	     logger.info("Before read subscriber call");
		
	     SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), workflowMetas);
	     
	     logger.info("subscriber call done");
		 if (subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() >0){
			logger.debug("Inside the if condition for status check");
			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
		 }
         logger.info("Recieved a SubscriberInfo Object and it is not null");
		 logger.info("Printing subscriber onject value "+subscriberObj.getSubscriber());
		 logger.info("Billing Metas: " + subscriberObj.getMetas());
	     
		exchange.getOut().setBody(readAccountInfo(request.getCustomerId(),request.isTransactional(), subscriberObj.getMetas()));

	}


	private SubscriberInfo readSubscriber(String requestId, String customerId, List<Meta> metas) {
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
		logger.debug("Hi HERE I AM::: Result " + subscriberInfo.getStatus() );
		return subscriberInfo;
	}
	
	class OfferInfo {
		private Integer offerID;
		private long offerExpiry;
		private long offerStart;
		private String daID;
		private String walletName;
		
		public OfferInfo() {}
		
		public OfferInfo(Integer offerID, long offerExpiry, long offerStart, String daID, String walletName) {
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
	
	
	private CommandResponseData readAccountInfo(String msisdn,boolean isTransactional, Map<String, String> metas) throws SmException {
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);


		OperationResult operationResult = new OperationResult();

		if(isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}

		Operation operation = new Operation();
		operation.setName("Read");
		operation.setModifier("CustomerInfoCharge");
		operationResult.getOperation().add(operation);
		ParameterList parameterList = new ParameterList();
		operation.setParameterList(parameterList);
		
	// Process Balances & Offers...
		
		Map<Integer, DAInfo> daList = new HashMap<Integer, ReadCustomerInfoCharge.DAInfo>();
		Map<Integer, OfferInfo> offerList = new HashMap<Integer, ReadCustomerInfoCharge.OfferInfo>();
		for (String key : metas.keySet()) {
			
			if (key.startsWith("DA")) {
				logger.debug("CIC:: meta: " + key + " = " + metas.get(key));
				String daForm = metas.get(key);
				logger.debug("Check before split: " + daForm);
				String daPart[] = daForm.split(":+:");
				logger.debug("DA Parts: " + daPart.length);
				int i=0; for (String part: daPart) {
					logger.debug("daPart[" + i++ +"]" + part);
				}
				
				// first part is main DA...
				logger.debug("Main DA: " + daPart[0]);
				String mainDaElements[] = daPart[0].split(",");
				
				int daID = Integer.parseInt(mainDaElements[0]);
				String daVal1 = mainDaElements[1];
				String daVal2 = mainDaElements[2];
				Long startDate = Long.parseLong(((mainDaElements[3].equals("null"))?null:mainDaElements[3]));
				Long expiryDate = Long.parseLong(((mainDaElements[4].equals("null"))?null:mainDaElements[4]));
				Integer pamServiceID = Integer.parseInt(((mainDaElements[5].equals("null"))?null:mainDaElements[5]));
				Integer offerID = Integer.parseInt(((mainDaElements[6].equals("null"))?null:mainDaElements[6]));
				Integer productID = Integer.parseInt(((mainDaElements[7].equals("null"))?null:mainDaElements[7]));
				Boolean isRealMoney = Boolean.parseBoolean(((mainDaElements[8].equals("null"))?null:mainDaElements[8]));
				Long closestExpiryDate = Long.parseLong(((mainDaElements[9].equals("null"))?null:mainDaElements[9]));
				String closestExpiryValue1 = ((mainDaElements[10].equals("null"))?null:mainDaElements[10]);
				String closestExpiryValue2 = ((mainDaElements[11].equals("null"))?null:mainDaElements[11]);
				Long closestAccessibleDate = Long.parseLong(((mainDaElements[12].equals("null"))?null:mainDaElements[12]));
				String closestAccessibleValue1 = ((mainDaElements[13].equals("null"))?null:mainDaElements[13]);
				String closestAccessibleValue2 = ((mainDaElements[14].equals("null"))?null:mainDaElements[14]);
				String daActiveValue1 = ((mainDaElements[15].equals("null"))?null:mainDaElements[15]);
				String daActiveValue2 = ((mainDaElements[16].equals("null"))?null:mainDaElements[16]);
				Integer daUnitType = Integer.parseInt(((mainDaElements[17].equals("null"))?null:mainDaElements[17]));
				Boolean isCompositeDAFlag = Boolean.parseBoolean(((mainDaElements[18].equals("null"))?null:mainDaElements[18]));
				
				DAInfo daInfo = new DAInfo(daID, daVal1, daVal2, startDate, expiryDate, pamServiceID, offerID, productID, isRealMoney, 
						closestExpiryDate, closestExpiryValue1, closestExpiryValue2, closestAccessibleDate, closestAccessibleValue1, 
						closestAccessibleValue2, daActiveValue1, daActiveValue2, daUnitType, isCompositeDAFlag);
				
				// second part is subDA list...
				if (daPart.length > 1) {
					logger.debug("All SubDA list: " + daPart[1]);
					String subDAs[] = daPart[1].split("|||");
					logger.debug("Number of SubDA: " + subDAs.length);
					for (String subDAForm: subDAs) {
						String subDA[] = subDAForm.split(",");
						String subDAValue1 = subDA[0];
						String subDAValue2 = subDA[1];
						Long subDAStartDate = Long.parseLong(((subDA[2].equals("null"))?null:subDA[2]));
						Long subDAExpiryDate = Long.parseLong(((subDA[3].equals("null"))?null:subDA[3]));

						SubDAInfo subDAInfo = new SubDAInfo(subDAValue1, subDAValue2, subDAStartDate, subDAExpiryDate);
						daInfo.addSubDA(subDAInfo);
						logger.debug("Added SubDA: " + subDAInfo);
					}
					daList.put(daID, daInfo);
				} else {
					logger.debug("Sub DAs does not seem to available to extract...");
				}
				logger.debug("Packed DA: " + daInfo);
			} // end of if for DA handling 
			
			if (key.startsWith("OFFER_INFO")) {
				logger.debug("FLEXI:: OFFER_ID...." + metas.get(key));
				String offerForm = metas.get(key);
				logger.debug("Check before split - offerForm: " + offerForm);
				String offerParts[] = offerForm.split(",");
				logger.debug("Offer Parts: " + offerParts.length);
				int i= 0; for (String part: offerParts) {
					logger.debug("OfferPart[" + i++ + "] :=" + part);
				}
				String offerId = offerParts[0];
				String start = offerParts[1];
				if (start.equals("null"))
					start = offerParts[2];
				String expiry = offerParts[3];
				if (expiry.equals("null"))
					expiry = offerParts[4];

				String daID = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", offerId);
				String walletName = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", offerId);

				int offerID = Integer.parseInt(offerId);
				if (offerID == 1 || offerID == 2 || offerID == 4 || offerID == 1241 || (offerID >= 7000 && offerID <= 9999)) {
					logger.debug("FLEXI:: Offer listed in omission case. Ignoring...");
					continue;
				}
				
				if (daID == null) {
					logger.debug("OfferID: " + offerId + " is not configure with a DA. Ignoring...");
					continue;
				}
				OfferInfo oInfo = new OfferInfo(offerID, Long.parseLong(expiry), Long.parseLong(start), daID, walletName);
				offerList.put(offerID, oInfo);
				
			}
		} // end of for loop... 
			
		
	// End of Get Balances & Dates processing....
		
		String accountsEntry = ""; String subscriptionsEntry = "";
		
		for (int offerID: offerList.keySet()) {
			OfferInfo oinfo = offerList.get(offerID);
			if (offerID > 2000) {
				subscriptionsEntry += ((subscriptionsEntry.isEmpty())?"":"|") + oinfo.walletName + ";" + oinfo.offerExpiry + ";" + daList.get(oinfo.daID).daVal1;
			} else {
				accountsEntry += ((accountsEntry.isEmpty())?"":"|") + oinfo.walletName + ";" + oinfo.offerExpiry;
				
			}
		}
		

		
		
		
	// End of Get Balances & Dates processing....
		
		
		StringParameter accounts = new StringParameter();
		accounts.setName("Accounts");
		accounts.setValue(accountsEntry);
		parameterList.getParameterOrBooleanParameterOrByteParameter().add(accounts);
		
		
		StringParameter subscriptions = new StringParameter();
		subscriptions.setName("Subscriptions");
		subscriptions.setValue(subscriptionsEntry);
		parameterList.getParameterOrBooleanParameterOrByteParameter().add(subscriptions);
		
		return responseData;
	}
	
	
	
	class SubDAInfo {
		String subDAValue1;
		String subDAValue2;
		Long subDAStartDate;
		Long subDAExpiryDate;
		
		public SubDAInfo(String subDAValue1, String subDAValue2, Long subDAStartDate, Long subDAExpiryDate) {
			this.subDAValue1 = subDAValue1;
			this.subDAValue2 = subDAValue2;
			this.subDAStartDate = subDAStartDate;
			this.subDAExpiryDate = subDAExpiryDate;
		}

		@Override
		public String toString() {
			return "SubDAInfo [subDAValue1=" + subDAValue1 + ", subDAValue2=" + subDAValue2 + ", subDAStartDate=" + subDAStartDate
					+ ", subDAExpiryDate=" + subDAExpiryDate + "]";
		}
		
	}
	
	class DAInfo {
		Integer daID;
		String daVal1;
		String daVal2;
		Long startDate;
		Long expiryDate;
		Integer pamServiceID;
		Integer offerID;
		Integer productID;
		Boolean isRealMoney;
		Long closestExpiryDate;
		String closestExpiryValue1;
		String closestExpiryValue2;
		Long closestAccessibleDate;
		String closestAccessibleValue1;
		String closestAccessibleValue2;
		String daActiveValue1;
		String daActiveValue2;
		Integer daUnitType;
		Boolean isComposite;
		List<SubDAInfo> subDAList;
		
		
		public DAInfo(Integer daID, String daVal1, String daVal2, Long startDate, Long expiryDate, Integer pamServiceID, Integer offerID,
				Integer productID, Boolean isRealMoney, Long closestExpiryDate, String closestExpiryValue1, String closestExpiryValue2,
				Long closestAccessibleDate, String closestAccessibleValue1, String closestAccessibleValue2, String daActiveValue1,
				String daActiveValue2, Integer daUnitType, Boolean isComposite) {
			this.daID = daID;
			this.daVal1 = daVal1;
			this.daVal2 = daVal2;
			this.startDate = startDate;
			this.expiryDate = expiryDate;
			this.pamServiceID = pamServiceID;
			this.offerID = offerID;
			this.productID = productID;
			this.isRealMoney = isRealMoney;
			this.closestExpiryDate = closestExpiryDate;
			this.closestExpiryValue1 = closestExpiryValue1;
			this.closestExpiryValue2 = closestExpiryValue2;
			this.closestAccessibleDate = closestAccessibleDate;
			this.closestAccessibleValue1 = closestAccessibleValue1;
			this.closestAccessibleValue2 = closestAccessibleValue2;
			this.daActiveValue1 = daActiveValue1;
			this.daActiveValue2 = daActiveValue2;
			this.daUnitType = daUnitType;
			this.isComposite = isComposite;
		}
		
		public void addSubDA(SubDAInfo subDA) {
			if (this.subDAList == null)
				this.subDAList = new ArrayList<SubDAInfo>();
			this.subDAList.add(subDA);
		}
		
		
		
		

		@Override
		public String toString() {
			return "DAInfo [daID=" + daID + ", daVal1=" + daVal1 + ", daVal2=" + daVal2 + ", startDate=" + startDate + ", expiryDate="
					+ expiryDate + ", pamServiceID=" + pamServiceID + ", offerID=" + offerID + ", productID=" + productID
					+ ", isRealMoney=" + isRealMoney + ", closestExpiryDate=" + closestExpiryDate + ", closestExpiryValue1="
					+ closestExpiryValue1 + ", closestExpiryValue2=" + closestExpiryValue2 + ", closestAccessibleDate="
					+ closestAccessibleDate + ", closestAccessibleValue1=" + closestAccessibleValue1 + ", closestAccessibleValue2="
					+ closestAccessibleValue2 + ", daActiveValue1=" + daActiveValue1 + ", daActiveValue2=" + daActiveValue2
					+ ", daUnitType=" + daUnitType + ", isComposite=" + isComposite + "]";
		}
		
		
		
	}
	
}
	
