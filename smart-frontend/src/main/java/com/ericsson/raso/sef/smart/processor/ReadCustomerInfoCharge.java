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
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.ReadCustomerInfoChargeRequest;
import com.ericsson.raso.sef.watergate.FloodGate;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class ReadCustomerInfoCharge implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(ReadCustomerInfoCharge.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("Customer Info Charge: process()");
		ReadCustomerInfoChargeRequest request = (ReadCustomerInfoChargeRequest) exchange.getIn().getBody();
		IConfig config = SefCoreServiceResolver.getConfigService();
		String channel = String.valueOf(request.getChannel());
		if(channel == null||channel.isEmpty()){
			logger.debug("Channel is null/empty");
			throw ExceptionUtil.toSmException(ErrorCode.invalidOperation);
		}
		int channelValue = Integer.parseInt(config.getValue("SMART_customerInfoChannel",channel));
		logger.info("ChannelName: "+channel+", ChannelValue: "+channelValue);
		String requestId = RequestContextLocalStore.get().getRequestId();
		logger.info("Collecting SOAP parameters");
		List<Meta> workflowMetas= new ArrayList<Meta>();
		workflowMetas.add(new Meta("msisdn", String.valueOf(request.getCustomerId())));
		workflowMetas.add(new Meta("AccessKey", String.valueOf(request.getAccessKey())));
		workflowMetas.add(new Meta("channelName", String.valueOf(request.getChannel())));
		workflowMetas.add(new Meta("MessageId",String.valueOf(request.getMessageId())));

		//List<Meta> metaSubscriber=new ArrayList<Meta>();
		if(channelValue > 0){
			workflowMetas.add(new Meta("READ_SUBSCRIBER","CUSTOMER_INFO_CHARGE")); 
		}
		else{
			workflowMetas.add(new Meta("READ_SUBSCRIBER","READ_BALANCES"));
		}
		workflowMetas.add(new Meta("SUBSCRIBER_ID",request.getCustomerId()));


		logger.info("Collected SOAP parameters");
		logger.info("Going for Customer Info Charge Call");
		logger.info("Before read subscriber call");
		SubscriberInfo subscriberObj=readSubscriber(requestId, request.getCustomerId(), workflowMetas);
		String value = String.valueOf(subscriberObj);
		logger.info("subscriber call "+value);
		if (subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() > 0) {
			logger.error("DB response: " + subscriberObj.getStatus());
			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
		}
		
		if (subscriberObj.getLocalState() == null || subscriberObj.getSubscriber() == null) {
			logger.error("seems like subscriber was not found!!!");
			throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);
		}

		if(subscriberObj.getStatus() != null && subscriberObj.getStatus().getCode() >0 && subscriberObj.getLocalState().name().equalsIgnoreCase(ContractState.PREACTIVE.name())){
			logger.info("PRE_ACTIVE state");
			throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
		}
		if(subscriberObj!=null && subscriberObj.getLocalState().name().equalsIgnoreCase(ContractState.RECYCLED.name())){
			logger.info("DE_ACTIVE state");
			throw ExceptionUtil.toSmException(ErrorCode.invalidLifecycleError1);
		}
		logger.info("Recieved a SubscriberInfo Object and it is not null");
		logger.info("Printing subscriber onject value "+subscriberObj.getSubscriber());
		logger.info("Billing Metas: " + subscriberObj.getMetas());
		String edrIdentifier = (String)exchange.getIn().getHeader("EDR_IDENTIFIER"); 

		logger.error("FloodGate acknowledging exgress...");
		FloodGate.getInstance().exgress();

		exchange.getOut().setBody(readAccountInfo(request.getCustomerId(),request.isTransactional(), subscriberObj.getSubscriber().getMetas()));
		exchange.getOut().setHeader("EDR_IDENTIFIER", edrIdentifier);
	}


	private SubscriberInfo readSubscriber(String requestId, String customerId, List<Meta> metas) {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		logger.info("workflowMetas size : "+metas.size());
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
				logger.debug("daId: "+daID+", daVal1: "+daVal1);
				String daVal2 = mainDaElements[2];
				Long startDate =("null".equals(mainDaElements[3]))?null: Long.parseLong(mainDaElements[3]);
				Long expiryDate = (("null").equals(mainDaElements[4]))?null:Long.parseLong(mainDaElements[4]);
				Integer pamServiceID = (("null").equals(mainDaElements[5]))?null:Integer.parseInt(mainDaElements[5]);
				Integer offerID = (("null").equals(mainDaElements[6]))?null:Integer.parseInt(mainDaElements[6]);
				Integer productID = (("null").equals(mainDaElements[7]))?null:Integer.parseInt(mainDaElements[7]);
				Boolean isRealMoney = (("null").equals(mainDaElements[8]))?null:Boolean.parseBoolean(mainDaElements[8]);
				Long closestExpiryDate = (("null").equals(mainDaElements[9]))?null:Long.parseLong(mainDaElements[9]);
				String closestExpiryValue1 = (("null").equals(mainDaElements[10]))?null:mainDaElements[10];
				String closestExpiryValue2 = (("null").equals(mainDaElements[11]))?null:mainDaElements[11];
				Long closestAccessibleDate = (("null".equals(mainDaElements[12])))?null:Long.parseLong(mainDaElements[12]);
				String closestAccessibleValue1 = (("null").equals(mainDaElements[13]))?null:mainDaElements[13];
				String closestAccessibleValue2 = (("null").equals(mainDaElements[14]))?null:mainDaElements[14];
				String daActiveValue1 = (("null").equals(mainDaElements[15]))?null:mainDaElements[15];
				String daActiveValue2 = (("null").equals(mainDaElements[16]))?null:mainDaElements[16];
				Integer daUnitType = (("null").equals(mainDaElements[17]))?null:Integer.parseInt(mainDaElements[17]);
				Boolean isCompositeDAFlag = (("null").equals(mainDaElements[18]))?null:Boolean.parseBoolean(mainDaElements[18]);

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
				} else {
					logger.debug("Sub DAs does not seem to available to extract...");
				}
				daList.put(daID, daInfo);
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
		logger.debug("Size of offerList "+offerList.size()+" offerList.keyset: "+offerList.keySet());
		logger.debug("Size of daList "+daList.size()+" daList.keyset: "+daList.keySet());


		String accountsEntry = ""; String subscriptionsEntry = "";

		for (int offerID: offerList.keySet()) {
			OfferInfo oinfo = offerList.get(offerID);
			logger.debug("OfferId: "+offerID+" OfferInfo: "+oinfo);
			String convertedDate = convertDateToReadableFormat(oinfo.offerExpiry+"");
			if (offerID > 2000) {
				subscriptionsEntry += ((subscriptionsEntry.isEmpty())?"":"|") + oinfo.walletName + ";" + convertedDate;			
			} else {	
				if(oinfo.daID != null){
					accountsEntry += ((accountsEntry.isEmpty())?"":"|") + oinfo.walletName + ";"+ this.applyConversion(oinfo.walletName, (daList.get(Integer.parseInt(oinfo.daID))).daVal1)+ ";" + convertedDate;				}
			}
		}


		logger.info("accountsEntry: "+accountsEntry);				
		if (!accountsEntry.isEmpty()) {
			StringParameter accounts = new StringParameter();
			accounts.setName("Accounts");
			accounts.setValue(accountsEntry);
			parameterList.getParameterOrBooleanParameterOrByteParameter().add(accounts);
		}

		logger.info("subscriptionsEntry: "+subscriptionsEntry);				
		if (!subscriptionsEntry.isEmpty())  {
			StringParameter subscriptions = new StringParameter();
			subscriptions.setName("Subscriptions");
			subscriptions.setValue(subscriptionsEntry);
			parameterList.getParameterOrBooleanParameterOrByteParameter().add(subscriptions);
		}
		return responseData;
	}

	private String applyConversion(String walletName, String daVal1) {
		String conversionFactor = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletConversionFactor", walletName);
		if (conversionFactor == null)
			return daVal1;

		try {
			int conversion = Integer.parseInt(conversionFactor);
			int value = Integer.parseInt(daVal1);
			return "" + (value/conversion);
		} catch (Exception e) {
			logger.warn("Cannot convert '" + daVal1 + "'. Cause: " + e.getMessage(), e);
			return daVal1;
		}
	}


	private String convertDateToReadableFormat(String expiryDate) {
		logger.debug("ExpirtyDate : "+expiryDate);
		if(!expiryDate.equals("null")){
			Date date=new Date(Long.parseLong(expiryDate));
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return simpleDateFormat.format(date);
		}	
		return expiryDate;
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

