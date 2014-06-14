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
			Map<String, String> metas = null;
			
			logger.debug("Finished preparing params....");
			
			if (msisdn == null) {
				throw new SmException(new ResponseCode(8002, "CustomerId or AccesKey is not defined in input parameter"));
			}
			
//			//TODO: Subscriber validation/caching goes here
			
			
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
			
			PurchaseResponse purchaseResponse = (PurchaseResponse) SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.SMFE_TXE_CORRELLATOR);
			
			//PurchaseResponse purchaseResponse = (PurchaseResponse) RequestCorrelationStore.get(correlationId);
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
		/*
		 * Step 1: GetAccountDetails first....
		 *  - Collect all DA
		 *  - Collect all Offers
		 *  - Determine longest expiry date
		 *  - Check if customer is in Grace
		 */
		
		this.partialReadSubscriber(rechargeRequest.getCustomerId());
		
		
		
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put(Constants.TX_AMOUNT, rechargeRequest.getAmountOfUnits().toString());
		map.put(Constants.TX_TYPE, rechargeRequest.getRatingInput0());
		map.put(Constants.TX_CODE, rechargeRequest.getEventName());
		map.put(SmartConstants.EXPIRY_POLICY, rechargeRequest.getRatingInput2());

		int expiryDatePolicy = Integer.valueOf(rechargeRequest.getRatingInput2());
		switch (expiryDatePolicy) {
		case 0://absolute
			map.put(SmartConstants.EXPIRY_DATE, rechargeRequest.getRatingInput4());
			break;
		case 2://No change to expiry date. days of extension = 0
			map.put(SmartConstants.EXPIRY_DAYS_OF_EXTENSION, String.valueOf(0));
			break;
		case 3://releative to current date (make it absolute) 
		case 1://releative to expiry date
		case 4:// 
			map.put(SmartConstants.EXPIRY_DAYS_OF_EXTENSION, rechargeRequest.getRatingInput3());
			break;
		default:
			break;
		}

		map.put(Constants.EX_DATA1, rechargeRequest.getEventName());
		if(rechargeRequest.getEventInfo() != null) {
			map.put(Constants.EX_DATA2, rechargeRequest.getEventInfo());
		}
		map.put(SmartConstants.USECASE, "recharge");

		return map;
	}
	
	private void partialReadSubscriber(String customerId) throws SmException {
		List<Meta> metas = new ArrayList<Meta>();
		metas.add(new Meta("msisdn", customerId));
		
		ISubscriberRequest subscriberRequest = SmartServiceResolver.getSubscriberRequest();
		//subscriberRequest.readSubscriber(UniqueIdGenerator.generateId(), customerId, metas);
		throw new SmException(new ResponseCode(999, "Not Ready Yet"));
		
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
				balInfo.offerExpiry = Long.parseLong(meta.getValue());

			if (meta.getKey().equals("ACC_AFTER_DA_ID")) 
				balInfo.daID = meta.getValue();

			if (meta.getKey().equals("ACC_AFTER_DA_VALUE")) 
				balInfo.daValue = Integer.parseInt(meta.getValue());

			if (meta.getKey().equals("ACC_AFTER_OFFER_ID")) 
				balInfo.offerId = meta.getValue();

			if (meta.getKey().equals("ACC_AFTER_OFFER_EXPIRY_DATE")) 
				balInfo.offerExpiry = Long.parseLong(meta.getValue());

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
		
		return;
	}

	

	private CommandResponseData handlePasaloadRefillResponse(ListParameter listParameter, PurchaseResponse response) {
		// TODO Auto-generated method stub
		return null;
	}


	private CommandResponseData handleFlexiRefillResponse(ListParameter listParameter, PurchaseResponse response) {
		// TODO Auto-generated method stub
		return null;
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
