package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.GetAccountDetailsCommand;
import com.ericsson.raso.sef.client.air.request.GetAccountDetailsRequest;
import com.ericsson.raso.sef.client.air.response.AccountFlags;
import com.ericsson.raso.sef.client.air.response.GetAccountDetailsResponse;
import com.ericsson.raso.sef.client.air.response.OfferInformation;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.sef.bes.api.entities.Product;

public class ReadSubscriberProfile extends BlockingFulfillment<Product> {
	private static final long serialVersionUID = -7488149893022619584L;


	private static final Logger LOGGER = LoggerFactory.getLogger(ReadSubscriberProfile.class);
	
	
	private static final String READ_SUBSCRIBER_ACTIVATION_DATE = "READ_SUBSCRIBER_ACTIVATION_DATE";
	private static final String READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE = "READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE";
	private static final String READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE = "READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE";
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

	public ReadSubscriberProfile(String name) {
		super(name);
	}
	

	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		throw new FulfillmentException("ffe", new ResponseCode(1000, "Not Implemented!"));

	}


	@Override
	public List<Product> prepare(Product e, Map<String, String> map) throws FulfillmentException {
		throw new FulfillmentException("ffe", new ResponseCode(1000, "Not Implemented!"));
	}


	@Override
	public List<Product> query(Product e, Map<String, String> map) throws FulfillmentException {
		
		LOGGER.debug("Query request for read subscriber...");
		
		if (map == null || map.isEmpty())
			throw new FulfillmentException("ffe", new ResponseCode(1001, "runtime parameters 'metas' missing in request!!"));
		
		GetAccountDetailsRequest request = new GetAccountDetailsRequest();
		String subscriberId = map.get("SUBSCRIBER_ID");
		if (subscriberId == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'SUBSCRIBER_ID' missing in request!!"));
		request.setSubscriberNumber(subscriberId);
		request.setSubscriberNumberNAI(1);
		
		GetAccountDetailsResponse response = null;
		GetAccountDetailsCommand command = new GetAccountDetailsCommand(request);
		
		try {
			response = command.execute();
		} catch (SmException e1) {
			e1.printStackTrace();
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}
		return createResponse(e, response);

	}


	@Override
	public List<Product> revert(Product e, Map<String, String> map) throws FulfillmentException {
		throw new FulfillmentException("ffe", new ResponseCode(1000, "Not Implemented!"));
	}
	
	//TODO: Move to smart-commons
	private List<Product> createResponse(Product p, GetAccountDetailsResponse response) {
		
		LOGGER.debug("Convering CS - IL response");
		List<Product> products = new ArrayList<Product>();
		
		Product product = new Product();
		product.setResourceName(p.getResourceName());
		product.setQuotaDefined(-1);
		product.setValidity(-1);
		
		// Start processing flattening of GetAccountDetailsResponse
		Map<String, String> accountDetails = new HashMap<String, String>();
		
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
			accountDetails.put(READ_SUBSCRIBER_SERVICE_OFFERING_ID + "." + ++index, "" + serviceOffering.getServiceOfferingID());
			accountDetails.put(READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG + "." + index, "" + serviceOffering.isServiceOfferingActiveFlag());
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
		
		// offer info...
		index = 0;
		for (OfferInformation offerInformation: response.getOfferInformationList()) {
			if(offerInformation != null) {
				accountDetails.put(READ_SUBSCRIBER_OFFER_INFO_OFFER_ID + "." + ++index, "" + offerInformation.getOfferID());
				
				if(offerInformation.getStartDate() != null)
				accountDetails.put(READ_SUBSCRIBER_OFFER_INFO_START_DATE + "." + index, "" + offerInformation.getStartDate().getTime());
				
				if(offerInformation.getStartDateTime() != null)
				accountDetails.put(READ_SUBSCRIBER_OFFER_INFO_START_DATE_TIME + "." + index, "" + offerInformation.getStartDateTime().getTime());
				
				if(offerInformation.getExpiryDate() != null)
				accountDetails.put(READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE + "." + index, "" + offerInformation.getExpiryDate().getTime());
				
				if(offerInformation.getExpiryDateTime() != null)
				accountDetails.put(READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME + "." + index, "" + offerInformation.getExpiryDateTime().getTime());
			}
		}
		LOGGER.debug("Packed all offer info..." + accountDetails.toString());
		
	
		product.setMetas(accountDetails);
		products.add(product);
		return products;
	}


	@Override
	public String toString() {
		return "ReadSubscriberProfile []";
	}
	
}
