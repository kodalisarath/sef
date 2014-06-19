package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.GetAccountDetailsCommand;
import com.ericsson.raso.sef.client.air.command.GetBalanceAndDateCommand;
import com.ericsson.raso.sef.client.air.request.GetAccountDetailsRequest;
import com.ericsson.raso.sef.client.air.request.GetBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.response.AccountFlags;
import com.ericsson.raso.sef.client.air.response.DedicatedAccountInformation;
import com.ericsson.raso.sef.client.air.response.GetAccountDetailsResponse;
import com.ericsson.raso.sef.client.air.response.GetBalanceAndDateResponse;
import com.ericsson.raso.sef.client.air.response.OfferInformation;
import com.ericsson.raso.sef.client.air.response.ServiceOffering;
import com.ericsson.raso.sef.client.air.response.SubDedicatedInfo;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.sef.bes.api.entities.Product;

public class PartialReadSubscriberProfile extends BlockingFulfillment<Product> {
	private static final long serialVersionUID = 2168287678631825571L;
	private static final Logger LOGGER = LoggerFactory.getLogger(PartialReadSubscriberProfile.class);

	//Get Account Details...
	private static final String READ_SUBSCRIBER_ACTIVATION_DATE = "READ_SUBSCRIBER_ACTIVATION_DATE";
	private static final String READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE = "READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE";
	private static final String READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE = "READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE";
	private static final String	READ_SUBSCRIBER_SERVICE_OFFERING	= "READ_SUBSCRIBER_SERVICE_OFFERING";
	private static final String READ_SUBSCRIBER_SERVICE_OFFERING_ID = "READ_SUBSCRIBER_SERVICE_OFFERING_ID";
	private static final String READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG = "READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG";
	private static final String READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG = "READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG";
	private static final String READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG = "READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG";
	private static final String READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG = "READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG";
	private static final String READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG = "READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG";
	private static final String READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG = "READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG";
	private static final String READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG = "READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG";
	private static final String READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG = "READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG";
	private static final String READ_SUBSCRIBER_OFFER_INFO_OFFER_ID = "READ_SUBSCRIBER_OFFER_ID";
	private static final String READ_SUBSCRIBER_OFFER_INFO_START_DATE = "READ_SUBSCRIBER_START_DATE";
	private static final String READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE = "READ_SUBSCRIBER_EXPIRY_DATE";
	private static final String READ_SUBSCRIBER_OFFER_INFO_START_DATE_TIME = "READ_SUBSCRIBER_START_DATE_TIME";
	private static final String READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME = "READ_SUBSCRIBER_EXPIRY_DATE_TIME";
	private static final String	READ_SUBSCRIBER_OFFER_INFO	= "READ_SUBSCRIBER_OFFER_INFO";

	

	public PartialReadSubscriberProfile(String name) {
		super(name);
	}



	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();
		returned.add(e);
		return returned;	
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();
		returned.add(e);
		return returned;	
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) throws FulfillmentException {
		LOGGER.debug("Request for Get Account Details...");

		HashMap<String, String> details = new HashMap<String, String>();

		if (map == null || map.isEmpty())
			throw new FulfillmentException("ffe", new ResponseCode(1001, "runtime parameters 'metas' missing in request!!"));

		GetAccountDetailsRequest accountDetailsRequest = new GetAccountDetailsRequest();
		String subscriberId = map.get("SUBSCRIBER_ID");
		if (subscriberId == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'SUBSCRIBER_ID' missing in request!!"));
		accountDetailsRequest.setSubscriberNumber(subscriberId);
		accountDetailsRequest.setSubscriberNumberNAI(1);

		GetAccountDetailsResponse accountDetailsResponse = null;
		GetAccountDetailsCommand accountDetailsCommand = new GetAccountDetailsCommand(accountDetailsRequest);

		try {
			accountDetailsResponse = accountDetailsCommand.execute();
		} catch (SmException e1) {
			e1.printStackTrace();
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}

		this.processAccountDetailsResponse(details, accountDetailsResponse);

		List<Product> products = new ArrayList<Product>();
		Product product = new Product();
		product.setResourceName(e.getResourceName());
		product.setQuotaDefined(-1);
		product.setValidity(-1);product.setMetas(details);
		products.add(product);
		return products;


	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();
		returned.add(e);
		return returned;	
	}

	private void processAccountDetailsResponse(HashMap<String, String> accountDetails, GetAccountDetailsResponse response) {
		// direct attributes...
		if (response.getActivationDate() != null)
			accountDetails.put(READ_SUBSCRIBER_ACTIVATION_DATE, "" + response.getActivationDate().getTime());

		if (response.getSupervisionExpiryDate() != null)
			accountDetails.put(READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE, "" + response.getSupervisionExpiryDate().getTime());

		if (response.getServiceFeeExpiryDate() != null)
			accountDetails.put(READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE, "" + response.getServiceFeeExpiryDate().getTime());

		LOGGER.debug("Packed all date attributes...");

		// service offerings
		int index = 0;
		for (com.ericsson.raso.sef.client.air.response.ServiceOffering serviceOffering: response.getServiceOfferings()) {
			LOGGER.debug("extracting service offering: " + serviceOffering);
			accountDetails.put(READ_SUBSCRIBER_SERVICE_OFFERING + "." + ++index, "" + serviceOffering.getServiceOfferingID() + "," + serviceOffering.isServiceOfferingActiveFlag());
		}
		LOGGER.debug("Packed all service offerings...");

		// account flags
		AccountFlags accountFlags = response.getAccountFlags();
		Boolean flag = accountFlags.isActivationStatusFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG, "" + flag);

		flag = accountFlags.isNegativeBarringStatusFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG, "" + flag);

		flag = accountFlags.isSupervisionPeriodWarningActiveFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG, "" + flag);

		flag = accountFlags.isServiceFeePeriodWarningActiveFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG, "" + flag);

		flag = accountFlags.isSupervisionPeriodExpiryFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG, "" + flag);

		flag = accountFlags.isServiceFeePeriodExpiryFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG, "" + flag);

		flag = accountFlags.isTwoStepActivationFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG, "" + flag);

		LOGGER.debug("Packed all account flags...");

		if (response.getServiceOfferings() != null) {
			LOGGER.debug("About to process ServiceOfferings. Size: " + response.getServiceOfferings().size());
			index = 0;
			for (ServiceOffering serviceOffering: response.getServiceOfferings()) {
				accountDetails.put(READ_SUBSCRIBER_SERVICE_OFFERING + "." + ++index, serviceOffering.getServiceOfferingID() + 
						"," + serviceOffering.isServiceOfferingActiveFlag());
			}
		}
		
		
		// offer info...
		index = 0;
		if (response.getOfferInformationList() != null) {
			LOGGER.debug("About to process OfferInfoList. Size: " + response.getOfferInformationList().size());
			for (OfferInformation offerInformation: response.getOfferInformationList()) {
				accountDetails.put(READ_SUBSCRIBER_OFFER_INFO + "." + ++index, (offerInformation.getOfferID() + 
						"," + ((offerInformation.getStartDate()==null)?"null":offerInformation.getStartDate().getTime()) + 
						"," + ((offerInformation.getStartDateTime()==null)?"null":offerInformation.getStartDateTime().getTime()) + 
						"," + ((offerInformation.getExpiryDate()==null)?"null":offerInformation.getExpiryDate().getTime()) + 
						"," + ((offerInformation.getExpiryDateTime()==null)?"null":offerInformation.getExpiryDateTime().getTime()) ));
			}
		} else {
			LOGGER.error("DID NOT RECEIVE Offer Info List from UCIP Response!!");
		}
		
		// service class
		//TODO: IGroup UCIP stack does not have this impl...
		
		LOGGER.debug("Packed all offer info..." + accountDetails.toString());

	}


	 

	@Override
	public String toString() {
		return "EntireReadSubscriberProfile []";
	}


}
