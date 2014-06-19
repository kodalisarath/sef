package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.UpdateBalanceAndDateCommand;
import com.ericsson.raso.sef.client.air.command.UpdateOfferCommand;
import com.ericsson.raso.sef.client.air.request.DedicatedAccountUpdateInformation;
import com.ericsson.raso.sef.client.air.request.UpdateBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.request.UpdateOfferRequest;
import com.ericsson.raso.sef.client.air.response.DedicatedAccountChangeInformation;
import com.ericsson.raso.sef.client.air.response.UpdateBalanceAndDateResponse;
import com.ericsson.raso.sef.client.air.response.UpdateOfferResponse;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.fulfillment.profiles.BlockingFulfillment;
import com.ericsson.sef.bes.api.entities.Product;

public class FlexiRechargeProfile extends BlockingFulfillment<Product> {
	private static final long	serialVersionUID	= -4882916995875270437L;
	private static final Logger LOGGER = LoggerFactory.getLogger(FlexiRechargeProfile.class);

	public FlexiRechargeProfile(String name) {
		super(name);
	}

	@Override
	public List<Product> fulfill(Product product, Map<String, String> map) throws FulfillmentException {
		List<Product> fulfilled = new ArrayList<Product>();
		
		LOGGER.debug("Flexi Recharge Inputs to Fulfillment: " + map);
		String amountOfUnits = map.get("amountOfUnits");
		String externalData1 = map.get("externalData1");
		String externalData2 = map.get("externalData2");
		String channel = map.get("channelName");
		String walletName = map.get("walletName");
		String expirationDatePolicy = map.get("expirationDatePolicy");
		String daysOfExtension = map.get("daysOfExtension");
		String absoluteDate = map.get("absoluteDate");
		String longestExpiry = map.get("longestExpiry");
		String offerExpiry = map.get("offerExpiry");
		String endurantOfferID = map.get("endurantOfferID");
		String endurantDA = map.get("endurantDA");
		String supervisionExpiryPeriod = map.get("supervisionExpiryPeriod");
		String serviceFeeExpiryPeriod = map.get("serviceFeeExpiryPeriod");
		String newDaID = map.get("newDaID");
		String newOfferID = map.get("newOfferID");
		String daStartTime = map.get("daStartTime");
		String daEndTime = map.get("daEndTime");
		boolean inGrace = Boolean.parseBoolean(map.get("inGrace"));
		boolean newSubscription = Boolean.parseBoolean(map.get("newSubscription"));
		
		
		String msisdn = map.get("msisdn") ;
		if (msisdn == null) 
			msisdn = map.get("SUBSCRIBER_ID");

		
		// update da peritinent to flexi....
		List<DedicatedAccountUpdateInformation> daToUpdateList = new ArrayList<DedicatedAccountUpdateInformation>();
		UpdateBalanceAndDateRequest balanceAndDateRequest = new UpdateBalanceAndDateRequest();
		balanceAndDateRequest.setSubscriberNumberNAI(1);
		balanceAndDateRequest.setSubscriberNumber(msisdn);
		
		DedicatedAccountUpdateInformation daInfo = new DedicatedAccountUpdateInformation();
		daInfo.setDedicatedAccountID(Integer.parseInt(newDaID));
//		if (newSubscription) 
//			daInfo.setStartDate(this.getDaDate(daStartTime));
		
		if (!expirationDatePolicy.equals("2")) 
			daInfo.setExpiryDate(this.getDaDate(daEndTime));
		daInfo.setAdjustmentAmountRelative(amountOfUnits);
		daInfo.setDedicatedAccountUnitType(Integer.parseInt(SefCoreServiceResolver.getConfigService().getValue("SMART_daUnitType", endurantDA)));
		daToUpdateList.add(daInfo);
		
		balanceAndDateRequest.setDedicatedAccountUpdateInformation(daToUpdateList);
		balanceAndDateRequest.setTransactionCurrency("PHP");
		
		if (supervisionExpiryPeriod != null) 
			balanceAndDateRequest.setSupervisionExpiryDate(new Date(Long.parseLong(supervisionExpiryPeriod)));
		
		if (serviceFeeExpiryPeriod != null) 
			balanceAndDateRequest.setServiceFeeExpiryDate(new Date(Long.parseLong(serviceFeeExpiryPeriod)));
		
		UpdateBalanceAndDateCommand balanceAndDateCommand = new UpdateBalanceAndDateCommand(balanceAndDateRequest);
		UpdateBalanceAndDateResponse balanceAndDateResponse = null;
		try {
			LOGGER.debug("Sending Update Balance command...");
			balanceAndDateResponse = balanceAndDateCommand.execute();
			LOGGER.debug("Update Balance successful from AIR...");
		} catch (SmException e) {
			LOGGER.error("Update Balance failed in AIR... Cause: " + e.getMessage(), e);
			throw new FulfillmentException(e.getComponent(), e.getStatusCode());
		}
		
		
		// update the offer pertinent to flexi....
		UpdateOfferRequest updateOfferRequest = new UpdateOfferRequest();
		updateOfferRequest.setSubscriberNumber(msisdn);
		updateOfferRequest.setSubscriberNumberNAI(1);
		updateOfferRequest.setOfferID(Integer.parseInt(newOfferID));
		updateOfferRequest.setExpiryDateTime(new Date(Long.parseLong(offerExpiry)));
		updateOfferRequest.setOfferType(2);
		
		
		UpdateOfferCommand updateOfferCommand = new UpdateOfferCommand(updateOfferRequest);
		UpdateOfferResponse updateOfferResponse = null;
		try {
			if (!expirationDatePolicy.equals("2")) { // in this policy, date must not be set
				LOGGER.debug("Sending Update Offer for flexi-case..");
				updateOfferResponse = updateOfferCommand.execute();
				LOGGER.debug("Flexi Offer updated in AIR...");
			}
		} catch (SmException e) {
			LOGGER.error("Update Offer for Flexi failed in AIR... Cause: " + e.getMessage(), e);
			throw new FulfillmentException(e.getComponent(), e.getStatusCode());
		}
		
		// check for grace and update the grace offer too
		UpdateOfferRequest updateGraceOfferRequest = new UpdateOfferRequest();
		UpdateOfferResponse updateGraceOfferResponse = null;
		
		if (inGrace)  {
			updateGraceOfferRequest.setOfferID(2);
			updateGraceOfferRequest.setExpiryDateTime(new Date(System.currentTimeMillis() + 2000));
			
			UpdateOfferCommand updateGraceOfferCommand = new UpdateOfferCommand(updateGraceOfferRequest);
			try {
				LOGGER.debug("Sending Update Grace Offer for flexi-case..");
				updateGraceOfferResponse = updateGraceOfferCommand.execute();
				LOGGER.debug("Flexi Offer Grace updated in AIR...");
			} catch (SmException e) {
				LOGGER.error("Update Grace Offer for Flexi failed in AIR... Cause: " + e.getMessage(), e);
				throw new FulfillmentException(e.getComponent(), e.getStatusCode());
			}
		}
		
		
		this.handleResponse(map, balanceAndDateResponse);
		this.handleResponse(map, updateOfferResponse);
		this.handleResponse(map, updateGraceOfferResponse);
		
		
		product.setMetas(map);
		fulfilled.add(product);
		return fulfilled;
	}

	private void handleResponse(Map<String, String> map, UpdateOfferResponse updateOfferResponse) {
		// cannot handle this since the AIR stack has not implemented for update offer command.... :(	
	}

	private void handleResponse(Map<String, String> map, UpdateBalanceAndDateResponse balanceAndDateResponse) {
		for (DedicatedAccountChangeInformation daInfo: balanceAndDateResponse.getDedicatedAccountInformation()) {
			map.put("DA", daInfo.getDedicatedAccountID() + "," + daInfo.getDedicatedAccountValue1() + "," + daInfo.getDedicatedAccountUnitType());
		}
	}
	
	

	private Date getDaDate(String daDate) {
		//SimpleDateFormat daDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		long millis = Long.parseLong(daDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime();
		return date;
	}

	@Override
	public List<Product> prepare(Product product, Map<String, String> map) throws FulfillmentException {
		List<Product> fulfilled = new ArrayList<Product>();
		Map<String, String> responseMetas = new HashMap<String, String>();
		
		
		product.setMetas(responseMetas);
		fulfilled.add(product);
		return fulfilled;
	}

	@Override
	public List<Product> query(Product product, Map<String, String> map) throws FulfillmentException {
		List<Product> fulfilled = new ArrayList<Product>();
		Map<String, String> responseMetas = new HashMap<String, String>();
		
		
		product.setMetas(responseMetas);
		fulfilled.add(product);
		return fulfilled;
	}

	@Override
	public List<Product> revert(Product product, Map<String, String> map) throws FulfillmentException {
		List<Product> fulfilled = new ArrayList<Product>();
		Map<String, String> responseMetas = new HashMap<String, String>();
		
		
		product.setMetas(responseMetas);
		fulfilled.add(product);
		return fulfilled;
	}

	@Override
	public String toString() {
		return "FlexiRechargeProfile []";
	}

	
}
