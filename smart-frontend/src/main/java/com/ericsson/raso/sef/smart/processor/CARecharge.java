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
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.PasaServiceManager;
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
			String offerid = null;
			String requestId = RequestContextLocalStore.get().getRequestId();
			
			Map<String, String> metas = new HashMap<String, String>();

			logger.debug("Finished preparing params....");

			if (msisdn == null) {
				throw new SmException(new ResponseCode(8002, "CustomerId or AccesKey is not defined in input parameter"));
			}

			requestContextCache.set(metas);
			
//			Subscriber susbcriber = readSubscriber(requestId, msisdn);
//			if (susbcriber == null)
//				throw ExceptionUtil.toSmException(ErrorCode.invalidAccount);

			logger.debug("Getting event class....");
			String eventClass = rechargeRequest.getEventClass();
			if (eventClass.equals("predefined") || eventClass.equals("unli")) {
				metas = prepareRecharge(rechargeRequest);
				offerid = rechargeRequest.getEventName();
			} else if (eventClass.equals("flexible")) {
				metas = prepareFlexibleRecharge(rechargeRequest);
				offerid = "FlexiRefill";
			} else if (eventClass.equals("pasaload")) {
				rechargeRequest.setRatingInput0("pasaload");
				metas = prepareRecharge(rechargeRequest);
				offerid = rechargeRequest.getEventName();
			} else if (eventClass.equals("reversal")) {
				metas = prepareReversalRecharge(rechargeRequest);
				offerid = rechargeRequest.getEventName();
				;
			} else {
				throw new SmException(new ResponseCode(500, "Recharge Type is not defined"));
			}
			eventClassCache.set(eventClass);

			logger.debug("Got event class....");
			ISubscriptionRequest subscriptionRequest = SmartServiceResolver.getSubscriptionRequest();
			PurchaseResponse response = new PurchaseResponse();

			metas.put("msisdn", msisdn);
			metas.put("SUBSCRIBER_ID", msisdn);
			metas.put("pasaload", rechargeRequest.getEventName());
			List<Meta> listMeta = convertToList(metas);
			logger.debug("Confirm metas: " + listMeta);
			String correlationId = subscriptionRequest.purchase(requestId, offerid, msisdn, true, listMeta);
			logger.debug("Got past event class....");
			RequestCorrelationStore.put(correlationId, response);

			ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(correlationId);

			try {
				semaphore.init(0);
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.debug("Exception while sleep     :" + e.getMessage());
			}

			logger.debug("Awake from sleep.. going to check response in store with id: " + correlationId);

			logger.debug("If noli really did a pull & rebuild, you would see this!!");
			PurchaseResponse purchaseResponse = (PurchaseResponse) RequestCorrelationStore.remove(correlationId);
			logger.debug("PurchaseResponse recieved here is " + purchaseResponse);
			
			if (purchaseResponse.getFault() != null) {
				logger.debug("Backend Error: " + purchaseResponse.getFault());
				throw ExceptionUtil.toSmException(new ResponseCode(purchaseResponse.getFault().getCode(), purchaseResponse.getFault().getDescription()));
			}

			logger.debug("Response purchase received.. now creating front end response");

			CommandResponseData responseData = createResponse(rechargeRequest.isTransactional(), purchaseResponse);
			arg0.getOut().setBody(responseData);

		} catch (Exception e) {
			logger.debug("In Excecption block");
			e.printStackTrace();
			if (e instanceof SmException) {
				logger.debug("exception is an instance of SmException");
				throw e;
			} else {
				logger.error("RuntimeException? ", e);
				throw new SmException("Catch-All Pathetic", ErrorCode.internalSystemError, e);
			}

		}

	}
	
	private Map<String, String> prepareRecharge(RechargeRequest rechargeRequest) {
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
		 * Step 1: GetAccountDetails first.... - Collect all DA - Collect all Offers - Determine longest expiry date - Check if customer is
		 * in Grace
		 */

		this.partialReadSubscriber(rechargeRequest.getCustomerId());
		this.checkGraceAndLongestExpiryDate();

		/*
		 * Step 2: Evaluate the Flexible request - Event Class should be "flexible" - Amount of Unit - Units to be recharged - Input 0 is
		 * channel - Input 1 is "Wallet Name" - Input 2 (Expiration date policy): if 0 then set the expiry date as "absolute date" as,
		 * mentioned in input 4 if 1 then set it relative to the expiry date of the offer if 2 then no change in expiry date if 3 then set
		 * it relative to the current date of the offer
		 * 
		 * Input 3 (relative days extension): - If Input 2 is (1 or 3) Then consider it and use "No. of days" to be extended - Else ignore -
		 * Input 4 (Absolute Date) - If Input 2 is (0) - Then consider it and set the expiry dates to absolute datetime sent here. - The
		 * value sent here is dateitme in Unixtime (EPOCH) - Else ignore
		 */

		WalletOfferMapping offerMapping = WalletOfferMappingHelper.getInstance().getOfferMapping(rechargeRequest.getRatingInput1());
		if (offerMapping == null) {
			logger.debug("No DA or Offer confgured for this wallet... Request will fail!!");
			throw ExceptionUtil.toSmException(ErrorCode.entityWasNotFound);
		}
		String requiredOfferID = offerMapping.getOfferID();
		String requiredDA = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", requiredOfferID);

		long currentExpiryDate = Long.parseLong(requestContext.get("longestExpiry"));
		switch (rechargeRequest.getRatingInput2()) {
			case "0": // ABSOLUTE DATE SCENARIO
				logger.debug("Handling ABSOLUTE DATE SCENARIO...");
				long requestedExpiryDate = Long.parseLong(rechargeRequest.getRatingInput4());
				if (requestedExpiryDate > currentExpiryDate) {
					requestContext.put("supervisionExpiryPeriod", "" + requestedExpiryDate);
					requestContext.put("serviceFeeExpiryPeriod", "" + requestedExpiryDate);
					requestContext.put("longestExpiry", "" + requestedExpiryDate);
					requestContext.put("offerExpiry", "" + requestedExpiryDate);
					requestContext.put("newDaID", requiredDA);
					requestContext.put("endurantOfferID", requiredDA);
					requestContext.put("newOfferID", requiredOfferID);
					requestContext.put("daEndTime", "" + requestedExpiryDate);
				} else {
					requestContext.put("offerExpiry", "" + requestedExpiryDate);
					requestContext.put("newDaID", requiredDA);
					requestContext.put("newOfferID", requiredOfferID);
					requestContext.put("daStartTime", "" + new Date().getTime());
					requestContext.put("daEndTime", "" + requestedExpiryDate);
				}
				logger.debug("Absolute Date scenario handled. New Expiry: " + requestedExpiryDate);
				break;
			case "1": // relative to current expiry date
				logger.debug("Handling RELATIVE TO CURRENT EXPIRY SCENARIO...");
				boolean found = false;
				Map<String, OfferInfo> subscriberOffers = subscriberOffersCache.get();
				for (OfferInfo oInfo : subscriberOffers.values()) {
					if (oInfo.walletName.equals(rechargeRequest.getRatingInput1())) {
						long newExpiryDate = oInfo.offerExpiry + (Long.parseLong(rechargeRequest.getRatingInput3()) * 86400000L);
						if (newExpiryDate > currentExpiryDate) {
							requestContext.put("supervisionExpiryPeriod", "" + newExpiryDate);
							requestContext.put("serviceFeeExpiryPeriod", "" + newExpiryDate);
							requestContext.put("longestExpiry", "" + newExpiryDate);
							requestContext.put("offerExpiry", "" + newExpiryDate);
							found = true;
							logger.debug("Found the right offer: " + oInfo.offerID + ", wallet: " + oInfo.walletName);
						}
					}
				}
				
				long startTime = new Date().getTime();
				long endTime = new Date().getTime() + (Long.parseLong(rechargeRequest.getRatingInput3()) * 86400000L);
				if (!found) {
					logger.debug("User not subscribed to this wallet: " + rechargeRequest.getRatingInput1());
					if (endTime > currentExpiryDate) {
						requestContext.put("supervisionExpiryPeriod", "" + endTime);
						requestContext.put("serviceFeeExpiryPeriod", "" + endTime);
						requestContext.put("longestExpiry", "" + endTime);
					}
					requestContext.put("offerExpiry", "" + endTime);
					requestContext.put("newDaID", requiredDA);
					requestContext.put("newOfferID", requiredOfferID);
					requestContext.put("daStartTime", "" + startTime);
					requestContext.put("daEndTime", "" + endTime);
				}
				requestContext.put("offerExpiry", "" + endTime);
				logger.debug("Relative to current expiry Date scenario handled. New Expiry: " + endTime);
				break;
			case "3": // relative to current date
				logger.debug("Handling RELATIVE TO CURRENT DATE SCENARIO...");
				found = false;
				subscriberOffers = subscriberOffersCache.get();
				for (OfferInfo oInfo : subscriberOffers.values()) {
					if (oInfo.walletName.equals(rechargeRequest.getRatingInput1())) {
						long newExpiryDate = new Date().getTime() + (Long.parseLong(rechargeRequest.getRatingInput3()) * 86400000L);
						if (newExpiryDate > currentExpiryDate) {
							requestContext.put("supervisionExpiryPeriod", "" + newExpiryDate);
							requestContext.put("serviceFeeExpiryPeriod", "" + newExpiryDate);
							requestContext.put("longestExpiry", "" + newExpiryDate);
							found = true;
							logger.debug("Found the right offer: " + oInfo.offerID + ", wallet: " + oInfo.walletName);
						}
					}
				}

				startTime = new Date().getTime();
				endTime = new Date().getTime() + (Long.parseLong(rechargeRequest.getRatingInput3()) * 86400000L);
				if (!found) {
					logger.debug("User not subscribed to this wallet: " + rechargeRequest.getRatingInput1());
					if (endTime > currentExpiryDate) {
						requestContext.put("supervisionExpiryPeriod", "" + endTime);
						requestContext.put("serviceFeeExpiryPeriod", "" + endTime);
						requestContext.put("longestExpiry", "" + endTime);
					}
					
					if (!requestContext.get("endurantOfferID").equals(requiredOfferID)) {
						requestContext.remove("enduranteOfferID");
					}
					
					requestContext.put("offerExpiry", "" + endTime);
					requestContext.put("newDaID", requiredDA);
					requestContext.put("newOfferID", requiredOfferID);
					requestContext.put("daStartTime", "" + startTime);
					requestContext.put("daEndTime", "" + endTime);
				}
				logger.debug("Relative to current Date scenario handled. New Expiry: " + endTime);
				break;
		}
		logger.debug("Quick Check on Request Context: " + requestContext);
		return requestContext;
	}

	private static final String READ_SUBSCRIBER_OFFER_INFO_OFFER = "READ_SUBSCRIBER_OFFER";

	private void checkGraceAndLongestExpiryDate() throws SmException {
		Map<String, String> requestContext = requestContextCache.get();

		Subscriber subscriber = subscriberCache.get();
		Map<String, String> subscriberMetas = subscriber.getMetas();

		OfferInfo oInfo = null;
		long longestExpiry = 0;
		OfferInfo endurantOffer = null; boolean anyOfferFound = false;
		Map<String, OfferInfo> subscriberOffers = new HashMap<String, CARecharge.OfferInfo>();
		for (String key : subscriberMetas.keySet()) {
			logger.debug("FLEXI:: processing meta:" + key + "=" + subscriberMetas.get(key));

			if (key.startsWith(READ_SUBSCRIBER_OFFER_INFO_OFFER)) {
				anyOfferFound = true;
				logger.debug("FLEXI:: OFFER_ID...." + subscriberMetas.get(key));
				String offerForm = subscriberMetas.get(key);
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

				/*
				 * As per Tanzeem the following offers must be ignored from considering life-cycle date impacts
				 */
				int offerID = Integer.parseInt(offerId);
				if (offerID == 1 || offerID == 2 || offerID == 4 || offerID == 1241 || (offerID >= 7000 && offerID <= 9999)) {
					logger.debug("FLEXI:: Offer listed in omission case. Ignoring...");
					continue;
				}
				
				if (offerID == 4) {
					logger.debug("FLEXI:: CUSTOMER IN RECYCLE. REJECTING...");
					throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
				}
				
				if (daID == null) {
					logger.debug("OfferID: " + offerId + " is not configure with a DA. Ignoring...");
					continue;
				}
				
				oInfo = new OfferInfo(offerId, Long.parseLong(expiry), Long.parseLong(start), daID, walletName);
				subscriberOffers.put(offerId, oInfo);
				logger.debug("FLEXI:: OFFER_INFO: " + oInfo);

				if (offerID == 2) {
					requestContext.put("inGrace", "true");
					logger.debug("FLEXI:: CUSTOMER IN GRACE!!!");
				}

				if (longestExpiry < oInfo.offerExpiry) {
					logger.debug("FLEXI:: Is this the longest Expiry? " + longestExpiry + " <==> " + oInfo);
					longestExpiry = oInfo.offerExpiry;
					endurantOffer = oInfo;
				}
			}
		}
		
		if (!anyOfferFound) {
			logger.debug("Seems like subscriber is in PREACTIVE state. Not Allowed");
			throw ExceptionUtil.toSmException(ErrorCode.invalidCustomerLifecycleState);
		}

		requestContext.put("longestExpiry", "" + longestExpiry);
		if (endurantOffer != null) {
			requestContext.put("endurantOfferID", endurantOffer.offerID);
			requestContext.put("endurantDA", "" + endurantOffer.daID);
		}
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
			logger.error("Abnormal execution with semaphore being released!!!");
		}
		
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		if (subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() > 0)
			throw new SmException(new ResponseCode(subscriberInfo.getStatus().getCode(), subscriberInfo.getStatus().getDescription()));

		subscriberCache.set(subscriberInfo.getSubscriber());

	}

	private Map<String, String> preparePasaload(RechargeRequest rechargeRequest) throws SmException {
		// evaluate truth table for eligibilities (2do: this will be later moved to implicit ootb features of prodcat when subscription db
		// is available
		PasaServiceManager pasaService = PasaServiceManager.getInstance();
		if (!pasaService.isPasaReceiveAllowed(rechargeRequest.getCustomerId(), rechargeRequest.getEventName())) {
			logger.debug("Pasaload is not allowed until tomorrow for this promo: " + rechargeRequest.getEventName());
			throw ExceptionUtil.toSmException(ErrorCode.maxCreditViolation);
		}

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

		if (isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}

		if (response != null && response.getFault() != null && response.getFault().getCode() > 0) {
			logger.info("No products found in the response");
			ResponseCode responseCode = new ResponseCode(response.getFault().getCode(), response.getFault().getDescription());
			throw new SmException(responseCode);
		}

		// convert resulted products to SMART response
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

		// List<Product> products = response.getProducts();
		// if(products != null) {
		// for (Product product: products) {
		// StringElement stringElement = new StringElement();
		// String offer = product.getResourceName();
		// int offerId = Integer.parseInt(offer);
		// String name = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", offer);
		// String walletName = name;
		// logger.debug("OfferID: " + offerId + "WalletName: " + name);
		//
		// long delta = product.getQuotaConsumed() - product.getQuotaDefined();
		// long curr = product.getQuotaConsumed();
		// long validity = product.getValidity();
		// logger.debug("Current bal: " + curr + "Delta: " + delta + "Validity: " + validity);
		// if(offerId != SmartConstants.AIRTIME_OFFER_ID && offerId != SmartConstants.ALKANSYA_OFFER_ID) {
		// name += ":s_PeriodicBonus";
		// }
		//
		//
		// if(offerId >= SmartConstants.UNLI_OFFER_START_ID) {
		// delta = 1;
		// curr = 1;
		// } else {
		// String conversionFactor = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletConversionFactor", walletName);
		// logger.debug("Conversion factor for this offer: " + conversionFactor);
		// long confec= Long.parseLong(conversionFactor);
		// delta = delta/confec;
		// curr = curr/confec;
		// }
		//
		// String val = name + ";" + delta + ";" + curr + ";" + getMillisToDate(validity);
		//
		// logger.debug("Balance String: " + val);
		//
		// stringElement.setValue(val);
		// listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
		// }
		// }

		return responseData;
	}

	private void handleRefillResponse(ListParameter listParameter, PurchaseResponse response) throws SmException {

		long beforeServiceFeeExpiry = 0;
		long beforeSupervisionFeeExpiry = 0;
		long afterServiceFeeExpiry = 0;
		long afterSupervisionFeeExpiry = 0;

		logger.debug("PREDEFINED:: Processing Refill Metas: " + response.getBillingMetas());

		Map<String, OfferInfo> beforeOfferEntries = new HashMap<String, CARecharge.OfferInfo>();
		Map<String, DaInfo> beforeDaEntries = new HashMap<String, CARecharge.DaInfo>();
		Map<String, OfferInfo> afterOfferEntries = new HashMap<String, CARecharge.OfferInfo>();
		Map<String, DaInfo> afterDaEntries = new HashMap<String, CARecharge.DaInfo>();

		for (Meta meta : response.getBillingMetas()) {
			logger.debug("PREDEFINED::Next Meta: " + meta.getKey());
			if (meta.getKey().equals("ACC_BEFORE_SERVICE_FEE_EXPIRY_DATE"))
				beforeServiceFeeExpiry = Long.parseLong(meta.getValue());
			if (meta.getKey().equals("ACC_BEFORE_SUPERVISION_EXPIRY_DATE"))
				beforeSupervisionFeeExpiry = Long.parseLong(meta.getValue());
			if (meta.getKey().equals("ACC_AFTER_SERVICE_FEE_EXPIRY_DATE"))
				afterServiceFeeExpiry = Long.parseLong(meta.getValue());
			if (meta.getKey().equals("ACC_AFTER_SUPERVISION_EXPIRY_DATE"))
				afterSupervisionFeeExpiry = Long.parseLong(meta.getValue());

			if (meta.getKey().startsWith("ACC_BEFORE_OFFER")) {
				OfferInfo oInfo = new OfferInfo();
				String offerPart[] = meta.getValue().split(",");
				oInfo.offerID = offerPart[0];
				oInfo.offerExpiry = Long.parseLong(offerPart[1]);
				beforeOfferEntries.put(oInfo.offerID, oInfo);
				logger.debug("Adding Before Offer " + oInfo);
			}

			if (meta.getKey().startsWith("ACC_AFTER_OFFER")) {
				OfferInfo oInfo = new OfferInfo();
				String offerPart[] = meta.getValue().split(",");
				oInfo.offerID = offerPart[0];
				oInfo.offerExpiry = Long.parseLong(offerPart[1]);
				afterOfferEntries.put(oInfo.offerID, oInfo);
				logger.debug("Adding After Offer " + oInfo);
			}

			if (meta.getKey().startsWith("ACC_BEFORE_DA")) {
				DaInfo daInfo = new DaInfo();
				String daPart[] = meta.getValue().split(",");
				daInfo.daID = daPart[0];
				daInfo.daValue = Integer.parseInt(daPart[1]);
				beforeDaEntries.put(daInfo.daID, daInfo);
				logger.debug("Adding Before DA " + daInfo);
			}

			if (meta.getKey().startsWith("ACC_AFTER_DA")) {
				DaInfo daInfo = new DaInfo();
				String daPart[] = meta.getValue().split(",");
				daInfo.daID = daPart[0];
				daInfo.daValue = Integer.parseInt(daPart[1]);
				afterDaEntries.put(daInfo.daID, daInfo);
				logger.debug("Adding After DA " + daInfo);
			}

		}

		logger.debug("PREDEFINED::Before Offer Entries Size: " + beforeOfferEntries.size() + ", After Entries Size: "
				+ afterOfferEntries.size());
		logger.debug("PREDEFINED::Before DA Entries Size: " + beforeDaEntries.size() + ", After DA Size: " + afterDaEntries.size());

		// Calculate...

		for (OfferInfo afterOffer : afterOfferEntries.values()) {
			logger.debug("Offer: " + afterOffer);
			String balanceId = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", afterOffer.offerID);
			String daId = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", afterOffer.offerID);

			logger.debug("Mapped Wallet: " + balanceId + ", DA: " + daId);
			if (balanceId == null || daId == null) {
				logger.error("Please check config.xml for mapping entries for OfferID:" + afterOffer.offerID
						+ " under sections 'GLOBAL_walletMapping' & 'Global_offerMapping' ");
				// throw ExceptionUtil.toSmException(ErrorCode.missingMandatoryParameterError);
				logger.debug("Doesnt seem to be an offer that will go in SMART response... Offer: " + afterOffer);
				continue;
			}

			DaInfo beforeDA = beforeDaEntries.get(daId);
			DaInfo afterDA = afterDaEntries.get(daId);

			
			/*
			 * After many dily-dallying, which is evident of ericsson being smart'iszed.... the latest understanding is that
			 * we no longer have before and after correlation....
			 * - if before is not available, then use after's balance
			 * - if after is not available, then use before's balance
			 * - if both are available only, then we actually calculate any balance....
			 * 
			 * 
			 * even before I finish this comment(9pm)... the requirement has changed to....
			 * if offerId > 2000, then hardcode the responses...
			 * 
			 * and another change by 11pm is that... though OfferID & DA are linekd in CS, they may not be returned
			 * with correlation.
			 * * For Unli Offers, we ignore DA in the response
			 * * For other offers, we check for DA and handle...
			 * 
			 * by 12:17am, another change...
			 * - check for offer in after which not in before, then select for handling
			 * - check for offer in after and before but changes in expiry, then select for handling
			 * - check for offer which have no DA and ignore them...
			 * 
			 */
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String responseEntry;


			OfferInfo beforeOffer = beforeOfferEntries.get(afterOffer.offerID);

			String requiredDA = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", afterOffer.offerID);
			if (requiredDA == null) {
				logger.debug("Seems like OfferID: " + afterOffer.offerID + " can be ignored, since there is no DA associated...");
				continue;
			}
			
			
			//================ Case Ladder for Predefined & Unli - START
			int daBalanceDiff = 0; long expiryDiff = 0;
			
			if (beforeDA != null && afterDA != null && beforeOffer != null && afterOffer != null) {
				logger.debug("CASE 1: All DA and Offer is available.. BeforeDA: " + beforeDA + ", afterDA: " + afterDA + ", beforeOffer: " + beforeOffer + ", afterOffer: " + afterOffer);
				
				daBalanceDiff = afterDA.daValue - beforeDA.daValue;
				expiryDiff = afterOffer.offerExpiry - beforeOffer.offerExpiry;
				
				if (daBalanceDiff == 0 && expiryDiff == 0) {
					logger.debug("There seems to be no impact with this DA & Offer. Ignoring...");
					continue;
				}
				
				if (Integer.parseInt(afterOffer.offerID) >= 2000)
					responseEntry = balanceId + ":s_PeriodicBonus" + ";" + 1 + ";" + 1 + ";"	+ format.format(new Date(afterOffer.offerExpiry));
				else
					responseEntry = balanceId + ":s_PeriodicBonus"+ ";" + daBalanceDiff + ";" + afterDA.daValue + ";" + format.format(new Date(afterOffer.offerExpiry));
				
				StringElement stringElement = new StringElement();
				stringElement.setValue(responseEntry);
				listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
				logger.debug("PREDEFINED::Adding response item to CARecharge: " + responseEntry);


			} else if (beforeDA == null && afterDA != null && beforeOffer != null && afterOffer != null) {
				logger.debug("CASE 2: Before DA is not available.. BeforeDA: " + beforeDA + ", afterDA: " + afterDA + ", beforeOffer: " + beforeOffer + ", afterOffer: " + afterOffer);
				
				daBalanceDiff = afterDA.daValue;
				expiryDiff = afterOffer.offerExpiry - beforeOffer.offerExpiry;
				
				if (Integer.parseInt(afterOffer.offerID) >= 2000)
					responseEntry = balanceId + ":s_PeriodicBonus" + ";" + 1 + ";" + 1 + ";"	+ format.format(new Date(afterOffer.offerExpiry));
				else
					responseEntry = balanceId + ":s_PeriodicBonus"+ ";" + daBalanceDiff + ";" + afterDA.daValue + ";" + format.format(new Date(afterOffer.offerExpiry));
				
				StringElement stringElement = new StringElement();
				stringElement.setValue(responseEntry);
				listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
				logger.debug("PREDEFINED::Adding response item to CARecharge: " + responseEntry);


			} else if (beforeDA == null && afterDA == null && beforeOffer != null && afterOffer != null) {
				logger.debug("CASE 3: No DA and All Offer is available.. BeforeDA: " + beforeDA + ", afterDA: " + afterDA + ", beforeOffer: " + beforeOffer + ", afterOffer: " + afterOffer);
				
				expiryDiff = afterOffer.offerExpiry - beforeOffer.offerExpiry;
				
				if (expiryDiff == 0) {
					logger.debug("No DA available and Offer expiry has not changed. Ignoring...");
					continue;
				}
				
				if (Integer.parseInt(afterOffer.offerID) >= 2000)
					responseEntry = balanceId + ":s_PeriodicBonus" + ";" + 1 + ";" + 1 + ";" + format.format(new Date(afterOffer.offerExpiry));
				else {
					logger.error("No DA for Offer is not valid in SMART as per Rev G compliant delivery. If this logic is negated, check with Imrul/ Tanzeem/ Navneet and ask for a CR...");
					continue;
					//responseEntry = balanceId + ":s_PeriodicBonus"+ ";" + daBalanceDiff + ";" + afterDA.daValue + ";" + format.format(new Date(afterOffer.offerExpiry));
				}
				
				StringElement stringElement = new StringElement();
				stringElement.setValue(responseEntry);
				listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
				logger.debug("PREDEFINED::Adding response item to CARecharge: " + responseEntry);

				
			} else if (beforeDA != null && afterDA != null && beforeOffer == null && afterOffer != null) {
				logger.debug("CASE 4: After DA & Before Offer is not available.. BeforeDA: " + beforeDA + ", afterDA: " + afterDA + ", beforeOffer: " + beforeOffer + ", afterOffer: " + afterOffer);
				
				daBalanceDiff = afterDA.daValue - beforeDA.daValue;
				
				if (Integer.parseInt(afterOffer.offerID) >= 2000)
					responseEntry = balanceId + ":s_PeriodicBonus" + ";" + 1 + ";" + 1 + ";" + format.format(new Date(afterOffer.offerExpiry));
				else
					responseEntry = balanceId + ":s_PeriodicBonus"+ ";" + daBalanceDiff + ";" + afterDA.daValue + ";" + format.format(new Date(afterOffer.offerExpiry));
				
				StringElement stringElement = new StringElement();
				stringElement.setValue(responseEntry);
				listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
				logger.debug("PREDEFINED::Adding response item to CARecharge: " + responseEntry);

				
			} else if (beforeDA == null && afterDA != null && beforeOffer == null && afterOffer != null) {
				logger.debug("CASE 5: Before DA & Before Offer is not available.. BeforeDA: " + beforeDA + ", afterDA: " + afterDA + ", beforeOffer: " + beforeOffer + ", afterOffer: " + afterOffer);
				
				daBalanceDiff = afterDA.daValue;
				
				if (Integer.parseInt(afterOffer.offerID) >= 2000)
					responseEntry = balanceId + ":s_PeriodicBonus" + ";" + 1 + ";" + 1 + ";" + format.format(new Date(afterOffer.offerExpiry));
				else
					responseEntry = balanceId + ":s_PeriodicBonus"+ ";" + daBalanceDiff + ";" + afterDA.daValue + ";" + format.format(new Date(afterOffer.offerExpiry));
				
				StringElement stringElement = new StringElement();
				stringElement.setValue(responseEntry);
				listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
				logger.debug("PREDEFINED::Adding response item to CARecharge: " + responseEntry);
				
			} 
			
			
			//================= Case Ladder for Predefined & Unli - END
				
		}
		logger.debug("PREDEFINED:: Done with the processing of Refill reponse...");

		return;
	}

	private void handleReversalResponse(ListParameter listParameter, PurchaseResponse response) {
		Map<String, ReversalEntry> reversalEntries = new HashMap<String, CARecharge.ReversalEntry>();

		logger.debug("Processing Reversal Metas: " + response.getBillingMetas());
		String index = "1";
		ReversalEntry entry = null;
		for (Meta meta : response.getBillingMetas()) {
			String[] keyPart = meta.getKey().split(".");
			if (!reversalEntries.containsKey(keyPart[1])) {
				entry = new ReversalEntry();
				reversalEntries.put(keyPart[0], entry);
			} else {
				entry = reversalEntries.get(keyPart[1]);
			}

			// Process the entries....
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

	private void handlePasaloadRefillResponse(ListParameter listParameter, PurchaseResponse response) throws SmException {
		Map<String, String> reqestContext = requestContextCache.get();
		
		logger.debug("PASA:: Processing Refill Metas: " + response.getBillingMetas());

		Map<String, OfferInfo> beforeOfferEntries = new HashMap<String, CARecharge.OfferInfo>();
		Map<String, DaInfo> beforeDaEntries = new HashMap<String, CARecharge.DaInfo>();
		Map<String, OfferInfo> afterOfferEntries = new HashMap<String, CARecharge.OfferInfo>();
		Map<String, DaInfo> afterDaEntries = new HashMap<String, CARecharge.DaInfo>();

		for (Meta meta : response.getBillingMetas()) {
			logger.debug("PASA::Next Meta: " + meta.getKey());

			if (meta.getKey().startsWith("ACC_BEFORE_OFFER")) {
				OfferInfo oInfo = new OfferInfo();
				String offerPart[] = meta.getValue().split(",");
				oInfo.offerID = offerPart[0];
				oInfo.offerExpiry = Long.parseLong(offerPart[1]);
				beforeOfferEntries.put(oInfo.offerID, oInfo);
				logger.debug("Adding Before Offer " + oInfo);
			}

			if (meta.getKey().startsWith("ACC_AFTER_OFFER")) {
				OfferInfo oInfo = new OfferInfo();
				String offerPart[] = meta.getValue().split(",");
				oInfo.offerID = offerPart[0];
				oInfo.offerExpiry = Long.parseLong(offerPart[1]);
				afterOfferEntries.put(oInfo.offerID, oInfo);
				logger.debug("Adding After Offer " + oInfo);
			}

			if (meta.getKey().startsWith("ACC_BEFORE_DA")) {
				DaInfo daInfo = new DaInfo();
				String daPart[] = meta.getValue().split(",");
				daInfo.daID = daPart[0];
				daInfo.daValue = Integer.parseInt(daPart[1]);
				beforeDaEntries.put(daInfo.daID, daInfo);
				logger.debug("Adding Before DA " + daInfo);
			}

			if (meta.getKey().startsWith("ACC_AFTER_DA")) {
				DaInfo daInfo = new DaInfo();
				String daPart[] = meta.getValue().split(",");
				daInfo.daID = daPart[0];
				daInfo.daValue = Integer.parseInt(daPart[1]);
				afterDaEntries.put(daInfo.daID, daInfo);
				logger.debug("Adding After DA " + daInfo);
			}

		}

		logger.debug("PASA::Before Offer Entries Size: " + beforeOfferEntries.size() + ", After Entries Size: "
				+ afterOfferEntries.size());
		logger.debug("PASA::Before DA Entries Size: " + beforeDaEntries.size() + ", After DA Size: " + afterDaEntries.size());

		// Calculate...

		for (OfferInfo afterOffer : afterOfferEntries.values()) {
			logger.debug("Offer: " + afterOffer);
			String balanceId = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", afterOffer.offerID);
			String daId = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", afterOffer.offerID);

			logger.debug("Mapped Wallet: " + balanceId + ", DA: " + daId);
			if (balanceId == null || daId == null) {
				logger.error("Please check config.xml for mapping entries for OfferID:" + afterOffer.offerID
						+ " under sections 'GLOBAL_walletMapping' & 'Global_offerMapping' ");
				// throw ExceptionUtil.toSmException(ErrorCode.missingMandatoryParameterError);
				logger.debug("Doesnt seem to be an offer that will go in SMART response... Offer: " + afterOffer);
				continue;
			}

			DaInfo beforeDA = beforeDaEntries.get(daId);
			DaInfo afterDA = afterDaEntries.get(daId);

			
			/*
			 * After many dily-dallying, which is evident of ericsson being smart'iszed.... the latest understanding is that
			 * we no longer have before and after correlation....
			 * - if before is not available, then use after's balance
			 * - if after is not available, then use before's balance
			 * - if both are available only, then we actually calculate any balance....
			 * 
			 * 
			 * even before I finish this comment(9pm)... the requirement has changed to....
			 * if offerId > 2000, then hardcode the responses...
			 * 
			 * and another change by 11pm is that... though OfferID & DA are linekd in CS, they may not be returned
			 * with correlation.
			 * * For Unli Offers, we ignore DA in the response
			 * * For other offers, we check for DA and handle...
			 * 
			 * by 12:17am, another change...
			 * - check for offer in after which not in before, then select for handling
			 * - check for offer in after and before but changes in expiry, then select for handling
			 * - check for offer which have no DA and ignore them...
			 * 
			 */
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String responseEntry;


			OfferInfo beforeOffer = beforeOfferEntries.get(afterOffer.offerID);

			String requiredDA = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", afterOffer.offerID);
			if (requiredDA == null) {
				logger.debug("Seems like OfferID: " + afterOffer.offerID + " can be ignored, since there is no DA associated...");
				continue;
			}
			
			
			//================ Case Ladder for Predefined & Unli - START
			int daBalanceDiff = 0; long expiryDiff = 0;
			
			if (beforeDA != null && afterDA != null && beforeOffer != null && afterOffer != null) {
				logger.debug("CASE 1: All DA and Offer is available.. BeforeDA: " + beforeDA + ", afterDA: " + afterDA + ", beforeOffer: " + beforeOffer + ", afterOffer: " + afterOffer);
				
				daBalanceDiff = afterDA.daValue - beforeDA.daValue;
				expiryDiff = afterOffer.offerExpiry - beforeOffer.offerExpiry;
				
				if (daBalanceDiff == 0 && expiryDiff == 0) {
					logger.debug("There seems to be no impact with this DA & Offer. Ignoring...");
					continue;
				}
				
				if (Integer.parseInt(afterOffer.offerID) >= 2000)
					responseEntry = balanceId + ":s_PeriodicBonus" + ";" + 1 + ";" + 1 + ";"	+ format.format(new Date(afterOffer.offerExpiry));
				else
					responseEntry = balanceId + ":s_PeriodicBonus"+ ";" + daBalanceDiff + ";" + afterDA.daValue + ";" + format.format(new Date(afterOffer.offerExpiry));
				
				StringElement stringElement = new StringElement();
				stringElement.setValue(responseEntry);
				listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
				logger.debug("PASA::Adding response item to CARecharge: " + responseEntry);
				PasaServiceManager.getInstance().setPasaReceived(reqestContext.get("msisdn"), reqestContext.get("pasaload"), daBalanceDiff);


			} else if (beforeDA == null && afterDA != null && beforeOffer != null && afterOffer != null) {
				logger.debug("CASE 2: Before DA is not available.. BeforeDA: " + beforeDA + ", afterDA: " + afterDA + ", beforeOffer: " + beforeOffer + ", afterOffer: " + afterOffer);
				
				daBalanceDiff = afterDA.daValue;
				expiryDiff = afterOffer.offerExpiry - beforeOffer.offerExpiry;
				
				if (Integer.parseInt(afterOffer.offerID) >= 2000)
					responseEntry = balanceId + ":s_PeriodicBonus" + ";" + 1 + ";" + 1 + ";"	+ format.format(new Date(afterOffer.offerExpiry));
				else
					responseEntry = balanceId + ":s_PeriodicBonus"+ ";" + daBalanceDiff + ";" + afterDA.daValue + ";" + format.format(new Date(afterOffer.offerExpiry));
				
				StringElement stringElement = new StringElement();
				stringElement.setValue(responseEntry);
				listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
				logger.debug("PASA::Adding response item to CARecharge: " + responseEntry);
				PasaServiceManager.getInstance().setPasaReceived(reqestContext.get("msisdn"), reqestContext.get("pasaload"), daBalanceDiff);


			} else if (beforeDA == null && afterDA == null && beforeOffer != null && afterOffer != null) {
				logger.debug("CASE 3: No DA and All Offer is available.. BeforeDA: " + beforeDA + ", afterDA: " + afterDA + ", beforeOffer: " + beforeOffer + ", afterOffer: " + afterOffer);
				
				expiryDiff = afterOffer.offerExpiry - beforeOffer.offerExpiry;
				
				if (expiryDiff == 0) {
					logger.debug("No DA available and Offer expiry has not changed. Ignoring...");
					continue;
				}
				
				if (Integer.parseInt(afterOffer.offerID) >= 2000)
					responseEntry = balanceId + ":s_PeriodicBonus" + ";" + 1 + ";" + 1 + ";" + format.format(new Date(afterOffer.offerExpiry));
				else {
					logger.error("No DA for Offer is not valid in SMART as per Rev G compliant delivery. If this logic is negated, check with Imrul/ Tanzeem/ Navneet and ask for a CR...");
					continue;
					//responseEntry = balanceId + ":s_PeriodicBonus"+ ";" + daBalanceDiff + ";" + afterDA.daValue + ";" + format.format(new Date(afterOffer.offerExpiry));
				}
				
				StringElement stringElement = new StringElement();
				stringElement.setValue(responseEntry);
				listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
				logger.debug("PASA::Adding response item to CARecharge: " + responseEntry);
				PasaServiceManager.getInstance().setPasaReceived(reqestContext.get("msisdn"), reqestContext.get("pasaload"), daBalanceDiff);

				
			} else if (beforeDA != null && afterDA != null && beforeOffer == null && afterOffer != null) {
				logger.debug("CASE 4: After DA & Before Offer is not available.. BeforeDA: " + beforeDA + ", afterDA: " + afterDA + ", beforeOffer: " + beforeOffer + ", afterOffer: " + afterOffer);
				
				daBalanceDiff = afterDA.daValue - beforeDA.daValue;
				
				if (Integer.parseInt(afterOffer.offerID) >= 2000)
					responseEntry = balanceId + ":s_PeriodicBonus" + ";" + 1 + ";" + 1 + ";" + format.format(new Date(afterOffer.offerExpiry));
				else
					responseEntry = balanceId + ":s_PeriodicBonus"+ ";" + daBalanceDiff + ";" + afterDA.daValue + ";" + format.format(new Date(afterOffer.offerExpiry));
				
				StringElement stringElement = new StringElement();
				stringElement.setValue(responseEntry);
				listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
				logger.debug("PASA::Adding response item to CARecharge: " + responseEntry);
				PasaServiceManager.getInstance().setPasaReceived(reqestContext.get("msisdn"), reqestContext.get("pasaload"), daBalanceDiff);

				
			} else if (beforeDA == null && afterDA != null && beforeOffer == null && afterOffer != null) {
				logger.debug("CASE 5: Before DA & Before Offer is not available.. BeforeDA: " + beforeDA + ", afterDA: " + afterDA + ", beforeOffer: " + beforeOffer + ", afterOffer: " + afterOffer);
				
				daBalanceDiff = afterDA.daValue;
				
				if (Integer.parseInt(afterOffer.offerID) >= 2000)
					responseEntry = balanceId + ":s_PeriodicBonus" + ";" + 1 + ";" + 1 + ";" + format.format(new Date(afterOffer.offerExpiry));
				else
					responseEntry = balanceId + ":s_PeriodicBonus"+ ";" + daBalanceDiff + ";" + afterDA.daValue + ";" + format.format(new Date(afterOffer.offerExpiry));
				
				StringElement stringElement = new StringElement();
				stringElement.setValue(responseEntry);
				listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
				logger.debug("PASA::Adding response item to CARecharge: " + responseEntry);
				PasaServiceManager.getInstance().setPasaReceived(reqestContext.get("msisdn"), reqestContext.get("pasaload"), daBalanceDiff);
				
			} 
			
			
			//================= Case Ladder for Predefined & Unli - END
				
		}
		
		logger.debug("PASA:: Done with the processing of Refill reponse...");
		return;
	}

	private void handleFlexiRefillResponse(ListParameter listParameter, PurchaseResponse response) {
		Map<String, String> requestContext = requestContextCache.get();

		Map<String, String> airResponse = this.convertToMap(response.getBillingMetas());
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for (String key: airResponse.keySet()) {
			if (key.startsWith("DA")) {
				logger.debug("Processing key:=" + key + ", value:=" + airResponse.get(key));
				String walletName = airResponse.get("walletName");
				//String requiredDA = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", requiredOfferID);
				String requiredDA = requestContext.get("newDaID");

				String daInfo = airResponse.get(key);
				String daParts[] = daInfo.split(",");

				if (requiredDA.equals(daParts[0])) {
					logger.debug("Checking requiredDA:" + requiredDA + ", fromAirResponse:" + daParts[0]);
					String responseEntry = requestContext.get("walletName") + ":s_PeriodicBonus;" + requestContext.get("amountOfUnits") + ";"
							+ daParts[1] + ";" + format.format(new Date(Long.parseLong(requestContext.get("longestExpiry"))));

					StringElement stringElement = new StringElement();
					stringElement.setValue(responseEntry);
					listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
					logger.debug("Adding response item to CARecharge: " + responseEntry);
				}
			}
		}
		logger.debug("Done with the processing of Refill reponse...");

	}

	private String getMillisToDate(long millis) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return df.format(calendar.getTime());
	}

	/* Method to convert a map to a list */
	private List<Meta> convertToList(Map<String, String> metas) {
		List<Meta> metaList = new ArrayList<Meta>();
		for (String metaKey : metas.keySet()) {
			Meta meta = new Meta();
			meta.setKey(metaKey);
			meta.setValue(metas.get(metaKey));
			metaList.add(meta);
		}
		return metaList;

	}

	private Map<String, String> convertToMap(List<Meta> metas) {
		Map<String, String> metaList = new HashMap<String, String>();
		for (Meta meta : metas) {
			metaList.put(meta.getKey(), meta.getValue());
		}
		return metaList;

	}

	class OfferInfo {
		private String offerID;
		private long offerExpiry;
		private long offerStart;
		private String daID;
		private String walletName;

		public OfferInfo() {
		}

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

	class DaInfo {
		private String daID;
		private Integer daValue;

		@Override
		public String toString() {
			return "DaInfo [daID=" + daID + ", daValue=" + daValue + "]";
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
