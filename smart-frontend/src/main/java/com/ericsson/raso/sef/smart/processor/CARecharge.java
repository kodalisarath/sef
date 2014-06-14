package com.ericsson.raso.sef.smart.processor;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.PasaloadRule;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.commons.WalletOfferMapping;
import com.ericsson.raso.sef.smart.commons.WalletOfferMappingHelper;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.RechargeRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ListParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;


public class CARecharge implements Processor {
	
	private static final Logger logger = LoggerFactory.getLogger(CARecharge.class);
	private static final ThreadLocal<String> eventClassCache = new ThreadLocal<String>();
	private static final ThreadLocal<Subscriber> subscriberCache = new ThreadLocal<Subscriber>();
	private static final ThreadLocal<Map<String, String>> requestContextCache = new ThreadLocal<Map<String, String>>();
	private static final ThreadLocal<Map<String, OfferInfo>> subscriberOffersCache = new ThreadLocal<Map<String, OfferInfo>>();
	
	private static final String REVERSAL_DEDICATED_ACCOUNT_ID = "REVERSAL_DEDICATED_ACCOUNT_ID";
	private static final String REVERSAL_DEDICATED_ACCOUNT_NEW_VALUE = "REVERSAL_BALANCES_DEDICATED_ACCOUNT_NEW_VALUE";
	private static final String REVERSAL_DEDICATED_ACCOUNT_REVERSED_AMOUNT = "REVERSAL_DEDICATED_ACCOUNT_REVERSED_AMOUNT";
	private static final String REVERSAL_OFFER_ID = "REVERSAL_OFFER_ID";
	private static final String REVERSAL_OFFER_EXPIRY = "REVERSAL_OFFER_EXPIRY";


	@Override
	public void process(Exchange arg0) throws SmException {
		logger.debug("CA Recharge started");
		
		try {
			
		
			logger.debug("Getting exchange body....");
			RechargeRequest rechargeRequest = (RechargeRequest) arg0.getIn().getBody();
			logger.debug("Got it!");
			
			if (rechargeRequest.getEventClass() == null) {
				throw new SmException(new ResponseCode(500, "Recharge Type is not defined"));
			}
			
			// Prepare parameters for transaction engine purchase
			logger.debug("Prepare params for transaction....");
			String msisdn = rechargeRequest.getCustomerId();
			String offerid= null;
			String requestId = null;
			Map<String, String> metas = new HashMap<String, String>();
			
			logger.debug("Finished preparing params....");
			
			if (msisdn == null) {
				throw new SmException(new ResponseCode(8002, "CustomerId or AccesKey is not defined in input parameter"));
			}
			
			requestContextCache.set(metas);
			
			logger.debug("Getting event class....");
			String eventClass = rechargeRequest.getEventClass();
			if (eventClass.equals("predefined") || eventClass.equals("unli")) {
				metas = prepareRecharge(rechargeRequest);
				offerid = rechargeRequest.getEventName();
			} else if (eventClass.equals("flexible")) {
				metas = prepareFlexibleRecharge(rechargeRequest);
				offerid = rechargeRequest.getRatingInput1();
			} else if (eventClass.equals("pasaload")) {
				rechargeRequest.setRatingInput0("pasaload");
				metas = prepareRecharge(rechargeRequest);
				offerid = rechargeRequest.getEventName();
			} else if (eventClass.equals("reversal")) {
				metas = prepareReversalRecharge(rechargeRequest);
				offerid = rechargeRequest.getEventName();;
			} else {
				throw new SmException(new ResponseCode(500, "Recharge Type is not defined"));
			}
			eventClassCache.set(eventClass);
			
			logger.debug("Got event class....");
			requestId = RequestContextLocalStore.get().getRequestId();
			ISubscriptionRequest subscriptionRequest = SmartServiceResolver.getSubscriptionRequest();
			PurchaseResponse response = new PurchaseResponse();
			
			metas.put("msisdn", msisdn);
			metas.put("SUBSCRIBER_ID", msisdn);
			List<Meta> listMeta=convertToList(metas);
			String correlationId = subscriptionRequest.purchase(requestId, offerid, msisdn, true, listMeta);
			logger.debug("Got past event class....");
			RequestCorrelationStore.put(correlationId, response);
			
			ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
			
			try {
			semaphore.init(0);
			semaphore.acquire();
			} catch(InterruptedException e) {
				e.printStackTrace();
				logger.debug("Exception while sleep     :"+e.getMessage());
			}

			
			logger.debug("Awake from sleep.. going to check response in store with id: " +  correlationId);
			
			PurchaseResponse purchaseResponse = (PurchaseResponse) RequestCorrelationStore.get(correlationId);
			logger.debug("PurchaseResponse recieved here is "+purchaseResponse);
			if(purchaseResponse == null) {
				//request timed out but no response. possible request missing from correlation store
				// there is no response time out error code in smart interface and hence throw internal server error
				logger.debug("No response arrived???");
				throw new SmException(ErrorCode.internalServerError);
			}
			
			logger.debug("Get requestId: " + purchaseResponse.getRequestId());
			
			logger.debug("Response purchase received.. now creating front end response");
			
			CommandResponseData responseData = createResponse(rechargeRequest.isTransactional(),purchaseResponse);
			arg0.getOut().setBody(responseData);
		
		} catch(Exception e) {
			logger.debug("In Excecption block" );
			e.printStackTrace();
			if(e instanceof SmException){
				logger.debug("exception is an instance of SmException");
				throw e;
			}else{
				logger.error("RuntimeException? ", e);
				throw new SmException("Catch-All Pathetic", ErrorCode.internalSystemError, e);
			}
			
		}
		
	}
	
	
	private Map<String,String> prepareRecharge(RechargeRequest rechargeRequest) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.TX_AMOUNT, rechargeRequest.getAmountOfUnits().toString());

		if (rechargeRequest.getRatingInput0() != null) {
			map.put(Constants.CHANNEL_NAME, rechargeRequest.getRatingInput0());
			map.put(Constants.EX_DATA3, rechargeRequest.getRatingInput0());
		}

		map.put(Constants.EX_DATA1, rechargeRequest.getEventName());
		map.put(Constants.EX_DATA2, rechargeRequest.getEventInfo());
		map.put(SmartConstants.USECASE, "recharge");

		return map;

	}

	private Map<String, String> prepareReversalRecharge(RechargeRequest rechargeRequest) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.TX_AMOUNT, rechargeRequest.getAmountOfUnits().toString());

		if (rechargeRequest.getRatingInput0() != null) {
			map.put(Constants.CHANNEL_NAME, rechargeRequest.getRatingInput0());
			map.put(Constants.EX_DATA3, rechargeRequest.getRatingInput0());
		}
		
		map.put(Constants.EX_DATA1, rechargeRequest.getEventName());
		map.put(Constants.EX_DATA2, rechargeRequest.getEventInfo());
		map.put(SmartConstants.USECASE, "reversal");

		return map;
	}


	private Map<String, String> prepareFlexibleRecharge(RechargeRequest rechargeRequest) throws SmException {
		Map<String, String> requestContext = requestContextCache.get();
		requestContext.put("amountOfUnits", "" + rechargeRequest.getAmountOfUnits());
		requestContext.put("externalData1", "" + rechargeRequest.getEventName());
		requestContext.put("externalData2", "" + rechargeRequest.getEventInfo());
		requestContext.put("channelName", "" + rechargeRequest.getRatingInput0());
		requestContext.put("walletName", "" + rechargeRequest.getRatingInput1());
		requestContext.put("expirationDatePolicy", "" + rechargeRequest.getRatingInput2());
		requestContext.put("daysOfExtension", "" + rechargeRequest.getRatingInput3());
		requestContext.put("absoluteDate", "" + rechargeRequest.getRatingInput4());
		
		
		/*
		 * Step 1: GetAccountDetails first....
		 *  - Collect all DA
		 *  - Collect all Offers
		 *  - Determine longest expiry date
		 *  - Check if customer is in Grace
		 */
		
		this.partialReadSubscriber(rechargeRequest.getCustomerId());
		this.checkGraceAndLongestExpiryDate();
		
	/*	 Step 2: Evaluate the Flexible request
		   - Event Class should be "flexible"
		   - Amount of Unit - Units to be recharged
		   - Input 0 is channel
		   - Input 1 is "Wallet Name"
		   - Input 2 (Expiration date policy):
		    if 0
		     then set the expiry date as "absolute date" as, mentioned in input 4
		    if 1
		     then set it relative to the expiry date of the offer
		    if 2 
		     then no change in expiry date
		    if 3 
		     then set it relative to the current date of the offer
		     
		   Input 3 (relative days extension):
		    - If Input 2 is (1 or 3)
		     Then consider it and use "No. of days" to be extended
		    - Else ignore
		   - Input 4 (Absolute Date) 
		    - If Input 2 is (0)
		     - Then consider it and set the expiry dates to absolute datetime sent here.
		     - The value sent here is dateitme in Unixtime (EPOCH)
		    - Else ignore
		
		*/
		
		long currentExpiryDate = Long.parseLong(requestContext.get("longestExpiry"));
		switch (rechargeRequest.getRatingInput2()) {
			case "0": // ABSOLUTE DATE SCENARIO
				long requestedExpiryDate = Long.parseLong(rechargeRequest.getRatingInput4());
				if (requestedExpiryDate > currentExpiryDate) {
					requestContext.put("supervisionExpiryPeriod", "" + requestedExpiryDate);
					requestContext.put("serviceFeeExpiryPeriod", "" + requestedExpiryDate);
					requestContext.put("longestExpiry", "" + requestedExpiryDate);
				}
				logger.debug("Absolute Date scenario handled. New Expiry: " + requestedExpiryDate);
				break;
			case "1": // relative to current expiry date
				boolean found = false;
				Map<String, OfferInfo> subscriberOffers = subscriberOffersCache.get();
				for (OfferInfo oInfo: subscriberOffers.values()) {
					if (oInfo.walletName.equals(rechargeRequest.getRatingInput1())) {
						long newExpiryDate = oInfo.offerExpiry + (Long.parseLong(rechargeRequest.getRatingInput3()) * 86400000);
						if (newExpiryDate > currentExpiryDate) {
							requestContext.put("supervisionExpiryPeriod", "" + newExpiryDate);
							requestContext.put("serviceFeeExpiryPeriod", "" + newExpiryDate);
							requestContext.put("longestExpiry", "" + newExpiryDate);
							found = true;
							logger.debug("Found the right offer: " + oInfo.offerID + ", wallet: " + oInfo.walletName );
						}
					}
				}
				
				if (!found) {
					logger.debug("User not subscribed to this wallet: " + rechargeRequest.getRatingInput1());
					WalletOfferMapping offerMapping = WalletOfferMappingHelper.getInstance().getOfferMapping(rechargeRequest.getRatingInput1());
					 String requiredOfferID = offerMapping.getOfferID();
					 String requiredDA = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", requiredOfferID);
					 
					 long startTime = new Date().getTime();
					 long endTime = new Date().getTime() + (Long.parseLong(rechargeRequest.getRatingInput3()) * 86400000);
					 if (endTime > currentExpiryDate) {
							requestContext.put("supervisionExpiryPeriod", "" + endTime);
							requestContext.put("serviceFeeExpiryPeriod", "" + endTime);
							requestContext.put("longestExpiry", "" + endTime);
					 }
					 requestContext.put("newDaID", requiredDA);
					 requestContext.put("newOfferID", requiredOfferID);
					 requestContext.put("daStartTime", "" + startTime);
					 requestContext.put("daEndTime", "" + endTime);
				}
				break;
			case "3": // relative to current date
				found = false;
				subscriberOffers = subscriberOffersCache.get();
				for (OfferInfo oInfo: subscriberOffers.values()) {
					if (oInfo.walletName.equals(rechargeRequest.getRatingInput1())) {
						long newExpiryDate = new Date().getTime() + (Long.parseLong(rechargeRequest.getRatingInput3()) * 86400000);
						if (newExpiryDate > currentExpiryDate) {
							requestContext.put("supervisionExpiryPeriod", "" + newExpiryDate);
							requestContext.put("serviceFeeExpiryPeriod", "" + newExpiryDate);
							requestContext.put("longestExpiry", "" + newExpiryDate);
							found = true;
							logger.debug("Found the right offer: " + oInfo.offerID + ", wallet: " + oInfo.walletName );
						}
					}
				}
				
				if (!found) {
					logger.debug("User not subscribed to this wallet: " + rechargeRequest.getRatingInput1());
					WalletOfferMapping offerMapping = WalletOfferMappingHelper.getInstance().getOfferMapping(rechargeRequest.getRatingInput1());
					 String requiredOfferID = offerMapping.getOfferID();
					 String requiredDA = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", requiredOfferID);
					 
					 long startTime = new Date().getTime();
					 long endTime = new Date().getTime() + (Long.parseLong(rechargeRequest.getRatingInput3()) * 86400000);
					 if (endTime > currentExpiryDate) {
							requestContext.put("supervisionExpiryPeriod", "" + endTime);
							requestContext.put("serviceFeeExpiryPeriod", "" + endTime);
							requestContext.put("longestExpiry", "" + endTime);
					 }
					 requestContext.put("newDaID", requiredDA);
					 requestContext.put("newOfferID", requiredOfferID);
					 requestContext.put("daStartTime", "" + startTime);
					 requestContext.put("daEndTime", "" + endTime);
				}
				break;
		}
		
		return requestContext;
	}
	
	


	private void checkGraceAndLongestExpiryDate() {
		Map<String, String> requestContext = requestContextCache.get();
		
		Subscriber subscriber = subscriberCache.get();
		Map<String, String> subscriberMetas = subscriber.getMetas();
		
		OfferInfo oInfo = null; long longestExpiry = 0; OfferInfo endurantOffer = null;
		Map<String, OfferInfo> subscriberOffers = new HashMap<String, CARecharge.OfferInfo>(); 
		for (String key: subscriberMetas.keySet()) {
			String[] keyPart = key.split(".");
			if (subscriberOffers.containsKey(keyPart[1])) {
				oInfo = subscriberOffers.get(keyPart[1]);
			} else {
				oInfo = new OfferInfo();
				subscriberOffers.put(keyPart[1], oInfo);
			}
			
			if (keyPart[0].equals("READ_SUBSCRIBER_OFFER_INFO_OFFER_ID")) {
				oInfo.offerID = subscriberMetas.get(key);
				oInfo.daID = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", oInfo.offerID);
				oInfo.walletName = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", oInfo.offerID);
				
				if (oInfo.offerID.equals("2"))
					requestContext.put("inGrace", "true");
			}
			
			if (keyPart[0].equals("READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE")) {
				oInfo.offerExpiry = Long.parseLong(subscriberMetas.get(key));
				
				if (longestExpiry < oInfo.offerExpiry) {
					longestExpiry = oInfo.offerExpiry;
					endurantOffer = oInfo;
				}
			}
		}
		
		requestContext.put("longestExpiry", "" + longestExpiry);
		requestContext.put("endurantOfferID", endurantOffer.offerID);
		requestContext.put("endurantDA", "" + endurantOffer.daID);
		
		subscriberOffersCache.set(subscriberOffers);
	}


	private void partialReadSubscriber(String customerId) throws SmException {
		Map<String, String> metas = requestContextCache.get();
		metas.put("msisdn", customerId);
		metas.put("SUBSCRIBER_ID", customerId);
		metas.put("READ_SUBSCRIBER", "PARTIAL_READ_SUBSCRIBER");
		
		
		ISubscriberRequest subscriberRequest = SmartServiceResolver.getSubscriberRequest();
		String requestId = subscriberRequest.readSubscriber(UniqueIdGenerator.generateId(), customerId, convertToList(metas));
		
		SubscriberResponseStore.put(requestId, new SubscriberInfo());
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {

		}
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		if (subscriberInfo.getStatus() != null)
			throw new SmException(new ResponseCode(subscriberInfo.getStatus().getCode(), subscriberInfo.getStatus().getDescription()));
		
		subscriberCache.set(subscriberInfo.getSubscriber());

		
	}


	private Map<String,String> preparePasaload(RechargeRequest rechargeRequest) {
		// evaluate truth table for eligibilities (2do: this will be later moved to imlicit ootb features of prodcat when subscription db is available
		
		
		
		// process the refill....
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.TX_AMOUNT, rechargeRequest.getAmountOfUnits().toString());

		if (rechargeRequest.getRatingInput0() != null) {
			map.put(Constants.CHANNEL_NAME, rechargeRequest.getRatingInput0());
			map.put(Constants.EX_DATA3, rechargeRequest.getRatingInput0());
		}

		map.put(Constants.EX_DATA1, rechargeRequest.getEventName());
		map.put(Constants.EX_DATA2, rechargeRequest.getEventInfo());
		map.put(SmartConstants.USECASE, "pasaload");

		return map;

	}

	private List<PasaloadRule> getPasaloadRules() {
		List<PasaloadRule> rules = null;
		String pasaloadRules = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "pasaload-eiligibility");
		
		return rules;
	}
	
	private CommandResponseData createResponse(boolean isTransactional, PurchaseResponse response) throws SmException {
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);
		OperationResult operationResult = new OperationResult();
		Operation operation = new Operation();
		operation.setName("Recharge");
		operationResult.getOperation().add(operation);
		ParameterList parameterList = new ParameterList();
		operation.setParameterList(parameterList);
		ListParameter listParameter = new ListParameter();
		listParameter.setName("RechargedBalances");
		parameterList.getParameterOrBooleanParameterOrByteParameter().add(listParameter);

		if(isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}
		
		if( response != null && response.getFault().getCode() > 0) {
			logger.info("No products found in the response");
			ResponseCode responseCode=new ResponseCode(response.getFault().getCode(), response.getFault().getDescription());
			throw new SmException(responseCode);
		}
		
		//convert resulted products to SMART response
		String eventClass = eventClassCache.get();
		if (eventClass.equals("predefined") || eventClass.equals("unli")) {
			this.handleRefillResponse(listParameter, response);
		} else if (eventClass.equals("flexible")) {
			this.handleFlexiRefillResponse(listParameter, response);
		} else if (eventClass.equals("pasaload")) {
			this.handlePasaloadRefillResponse(listParameter, response);
		} else if (eventClass.equals("reversal")) {
			this.handleReversalResponse(listParameter, response);
		} 
		
		
		

//		List<Product> products = response.getProducts();
//		if(products != null) {
//			for (Product product: products) {
//				StringElement stringElement = new StringElement();
//				String offer = product.getResourceName();
//				int offerId = Integer.parseInt(offer);
//				String name = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", offer);
//				String walletName = name;
//				logger.debug("OfferID: " + offerId + "WalletName: " + name);
//
//				long delta = product.getQuotaConsumed() - product.getQuotaDefined();
//				long curr = product.getQuotaConsumed();
//				long validity = product.getValidity();
//				logger.debug("Current bal: " + curr + "Delta: " + delta + "Validity: " + validity);
//				if(offerId != SmartConstants.AIRTIME_OFFER_ID && offerId != SmartConstants.ALKANSYA_OFFER_ID) {
//					name += ":s_PeriodicBonus";
//				}
//
//
//				if(offerId >= SmartConstants.UNLI_OFFER_START_ID) {
//					delta = 1;
//					curr = 1;
//				} else {
//					String conversionFactor = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletConversionFactor", walletName);
//					logger.debug("Conversion factor for this offer: " + conversionFactor);
//					long confec= Long.parseLong(conversionFactor); 
//					delta = delta/confec;
//					curr = curr/confec;
//				}
//
//				String val = name + ";" + delta + ";" + curr + ";" + getMillisToDate(validity);
//
//				logger.debug("Balance String: " + val);
//
//				stringElement.setValue(val);
//				listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
//			}
//		} 
		
		return responseData;
	}
	
	private void handleRefillResponse(ListParameter listParameter, PurchaseResponse response) {
		
		long beforeServiceFeeExpiry = 0;
		long beforeSupervisionFeeExpiry = 0;
		long afterServiceFeeExpiry = 0;
		long afterSupervisionFeeExpiry = 0;
		
		logger.debug("Processing Refill Metas: " + response.getBillingMetas());
		String index = "1"; BalInfo balInfo = null;
		Map<String, BalInfo> beforeEntries = new HashMap<String, CARecharge.BalInfo>();
		Map<String, BalInfo> afterEntries = new HashMap<String, CARecharge.BalInfo>();
		
		for (Meta meta: response.getBillingMetas()) {
			if (meta.getKey().equals("ACC_BEFORE_SERVICE_FEE_EXPIRY_DATE")) 
				beforeServiceFeeExpiry = Long.parseLong(meta.getValue());
			if (meta.getKey().equals("ACC_BEFORE_SUPERVISION_EXPIRY_DATE")) 
				beforeSupervisionFeeExpiry = Long.parseLong(meta.getValue());
			if (meta.getKey().equals("ACC_AFTER_SERVICE_FEE_EXPIRY_DATE")) 
				afterServiceFeeExpiry = Long.parseLong(meta.getValue());
			if (meta.getKey().equals("ACC_AFTER_SUPERVISION_EXPIRY_DATE")) 
				afterSupervisionFeeExpiry = Long.parseLong(meta.getValue());
			
			String[] keyPart = meta.getKey().split(".");
			if (keyPart[0].contains("BEFORE_")) {
				if (!beforeEntries.containsKey(keyPart[1])) {
					balInfo = new BalInfo();
					beforeEntries.put(keyPart[0], balInfo);
				} else {
					balInfo = beforeEntries.get(keyPart[1]);
				}	
			} else if (keyPart[0].contains("AFTER_")) {
				if (!afterEntries.containsKey(keyPart[1])) {
					balInfo = new BalInfo();
					afterEntries.put(keyPart[0], balInfo);
				} else {
					balInfo = afterEntries.get(keyPart[1]);
				}	
			}
			
			if (meta.getKey().equals("ACC_BEFORE_DA_ID")) 
				balInfo.daID = meta.getValue();

			if (meta.getKey().equals("ACC_BEFORE_DA_VALUE")) 
				balInfo.daValue = Integer.parseInt(meta.getValue());

			if (meta.getKey().equals("ACC_BEFORE_OFFER_ID")) 
				balInfo.offerId = meta.getValue();

			if (meta.getKey().equals("ACC_BEFORE_OFFER_EXPIRY_DATE")) 
				balInfo.offerExpiry = (meta.getValue() != null) ?Long.parseLong(meta.getValue()): 0;

			if (meta.getKey().equals("ACC_AFTER_DA_ID")) 
				balInfo.daID = meta.getValue();

			if (meta.getKey().equals("ACC_AFTER_DA_VALUE")) 
				balInfo.daValue = Integer.parseInt(meta.getValue());

			if (meta.getKey().equals("ACC_AFTER_OFFER_ID")) 
				balInfo.offerId = meta.getValue();

			if (meta.getKey().equals("ACC_AFTER_OFFER_EXPIRY_DATE")) 
				balInfo.offerExpiry = (meta.getValue() != null) ?Long.parseLong(meta.getValue()): 0;

		}
		
		// Calculate...
		for (String key: beforeEntries.keySet()) {
			BalInfo before = beforeEntries.get(key);
			BalInfo after = afterEntries.get(key);
			
			String balanceId = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", before.offerId) + "s_PeriodicBonus";
			int daBalanceDiff = after.daValue - before.daValue;
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String responseEntry = balanceId + ";" + daBalanceDiff + ";" + after.daValue  + ";" + format.format(new Date(after.offerExpiry));
			
			StringElement stringElement = new StringElement();
			stringElement.setValue(responseEntry);
			listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
			logger.debug("Adding response item to CARecharge: " + responseEntry);		
		}
		logger.debug("Done with the processing of Refill reponse...");
		return;
	}


	
	private void handleReversalResponse(ListParameter listParameter, PurchaseResponse response) {
		Map<String, ReversalEntry> reversalEntries = new HashMap<String, CARecharge.ReversalEntry>();
		
		
		logger.debug("Processing Reversal Metas: " + response.getBillingMetas());
		String index = "1"; ReversalEntry entry = null;
		for (Meta meta: response.getBillingMetas()) {
			String[] keyPart = meta.getKey().split(".");
			if (!reversalEntries.containsKey(keyPart[1])) {
				entry = new ReversalEntry();
				reversalEntries.put(keyPart[0], entry);
			} else {
				entry = reversalEntries.get(keyPart[1]);
			}
			
			//Process the entries....
			if (keyPart[0].equals(REVERSAL_OFFER_ID)) {
				String timerOffer; 
				timerOffer = meta.getValue();
				entry.walletName = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", timerOffer);
			}
			
			if (keyPart[0].equals(REVERSAL_OFFER_EXPIRY)) {
				entry.finalExpiryDate = meta.getValue();
			}
			
			if (keyPart[0].equals(REVERSAL_DEDICATED_ACCOUNT_NEW_VALUE)) {
				entry.finalBalance = meta.getValue();
			}
			
			if (keyPart[0].equals(REVERSAL_DEDICATED_ACCOUNT_REVERSED_AMOUNT)) {
				entry.reversedAmount = meta.getValue();
			}
			
			if (entry.isComplete()) {
				StringElement stringElement = new StringElement();
				stringElement.setValue(entry.toString());
				listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
				logger.debug("Adding response item to CARecharge: " + entry.toString());
			}
		}
		logger.debug("Finished processing of response for Reversal....");
		return;
	}

	

	private void handlePasaloadRefillResponse(ListParameter listParameter, PurchaseResponse response) {
		// TODO Auto-generated method stub
		return;
	}


	private void handleFlexiRefillResponse(ListParameter listParameter, PurchaseResponse response) {
		Map<String, String> requestContext = requestContextCache.get();
		
		Map<String, String> airResponse = this.convertToMap(response.getBillingMetas());
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String responseEntry = requestContext.get("walletName") + ";" + 
				requestContext.get("amountOfUnits") + ";" + 
				airResponse.get("DA_VALUE_1.1") + ";" + 
				format.format(new Date(Long.parseLong(requestContext.get("longestExpiry"))));
		
		StringElement stringElement = new StringElement();
		stringElement.setValue(responseEntry);
		listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
		logger.debug("Adding response item to CARecharge: " + responseEntry);		
		logger.debug("Done with the processing of Refill reponse...");
		
	}


	
	private String getMillisToDate(long millis) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return df.format(calendar.getTime());
	}
	
	/*Method to convert a map to a list*/
	private List<Meta> convertToList(Map<String,String> metas){
		List<Meta> metaList = new ArrayList<Meta>();
		for(String metaKey:metas.keySet()){
			Meta meta=new Meta();
			meta.setKey(metaKey);
			meta.setValue(metas.get(metaKey));
			metaList.add(meta);
		}
		return metaList;
		
	}
	
	private Map<String,String> convertToMap(List<Meta> metas){
		Map<String,String> metaList = new HashMap<String, String>();
		for(Meta meta:metas){
			metaList.put(meta.getKey(), meta.getValue());
		}
		return metaList;
		
	}
	
	
	class OfferInfo {
		private String offerID;
		private long offerExpiry;
		private String daID;
		private String walletName;
	}
	
	class BalInfo {
		private String daID;
		private Integer daValue;
		private String offerId;
		private Long offerExpiry;
		
		public boolean isComplete() {
			if (daID != null && daValue != null && offerId != null && offerExpiry != null)
				return true;
			return false;
		}
	}
	
	class ReversalEntry {
		private String walletName;
		private String reversedAmount;
		private String finalBalance;
		private String finalExpiryDate;
		
		@Override
		public String toString() {
			return walletName + ";" + reversedAmount + ";" + finalBalance + ";" + finalExpiryDate;
		}
		
		public boolean isComplete() {
			if (walletName != null && reversedAmount != null && finalBalance != null && finalExpiryDate != null)
				return true;
			return false;
		}
		
	}
}
