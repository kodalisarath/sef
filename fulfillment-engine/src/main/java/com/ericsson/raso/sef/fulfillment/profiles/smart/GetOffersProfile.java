package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.GetOffersCommand;
import com.ericsson.raso.sef.client.air.request.GetOffersRequest;
import com.ericsson.raso.sef.client.air.request.OfferSelection;
import com.ericsson.raso.sef.client.air.response.GetOffersResponse;
import com.ericsson.raso.sef.client.air.response.OfferInformation;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.fulfillment.profiles.BlockingFulfillment;
import com.ericsson.sef.bes.api.entities.Product;

public class GetOffersProfile extends BlockingFulfillment<Product> {
	private static final long serialVersionUID = -8794821146350582536L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GetOffersProfile.class);
	
	private static final String GET_OFFERS_OFFER_INFO_OFFER_ID = "OFFER_INFO_OFFER_ID";
	private static final String GET_OFFERS_OFFER_INFO_START_DATE = "OFFER_INFO_START_DATE";
	private static final String GET_OFFERS_OFFER_INFO_EXPIRY_DATE = "OFFER_INFO_EXPIRY_DATE";
	private static final String GET_OFFERS_OFFER_INFO_START_DATE_TIME = "OFFER_INFO_START_DATE_TIME";
	private static final String GET_OFFERS_OFFER_INFO_EXPIRY_DATE_TIME = "OFFER_INFO_EXPIRY_DATE_TIME";


	private String offerRequestedTypeFlag;	
	private OfferSelection[] offerSelection;
	
	
	
	protected GetOffersProfile(String name) {
		super(name);
	}


	/**
	 * Ideally, this logic of this method must be implemented in query() by design but is intentionally implemented in fulfill() since the 
	 * BC will be executed from Offer.purchase() and hence fulfill() will be auto-engaged which must not be hacked!!
	 */
	@Override
	public List<Product> fulfill(Product p, Map<String, String> map) throws FulfillmentException {
		
		String subscriberId = null;
		List<Product> returned = new ArrayList<Product>();
		try {
			
			GetOffersRequest request = new GetOffersRequest();
			
			subscriberId = map.get("SUBSCRIBER_ID");
			if (subscriberId == null)
				throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'SUBSCRIBER_ID' missing in request!!"));
			request.setSubscriberNumber(subscriberId);
			request.setSubscriberNumberNAI(1);
			
			request.setOfferRequestedTypeFlag(this.offerRequestedTypeFlag);
			request.setOfferSelection(this.offerSelection);
			
			GetOffersCommand command = new GetOffersCommand(request);
			GetOffersResponse response = command.execute();
			
			Map<String, String> offerDetails = new HashMap<String, String>();
			int index = 0;
			for (OfferInformation offerInformation: response.getOfferInformation()) {
				offerDetails.put(GET_OFFERS_OFFER_INFO_OFFER_ID + "." + ++index, "" + offerInformation.getOfferID());
				
				if(offerInformation.getStartDate() != null)
					offerDetails.put(GET_OFFERS_OFFER_INFO_START_DATE + "." + index, "" + offerInformation.getStartDate().getTime());
				
				if(offerInformation.getStartDateTime() != null)
					offerDetails.put(GET_OFFERS_OFFER_INFO_EXPIRY_DATE + "." + index, "" + offerInformation.getStartDateTime().getTime());
				
				if(offerInformation.getExpiryDate() != null)
					offerDetails.put(GET_OFFERS_OFFER_INFO_START_DATE_TIME + "." + index, "" + offerInformation.getExpiryDate().getTime());
				
				if(offerInformation.getExpiryDateTime() != null)
					offerDetails.put(GET_OFFERS_OFFER_INFO_EXPIRY_DATE_TIME + "." + index, "" + offerInformation.getExpiryDateTime().getTime());
			}
			
			p.setMetas(offerDetails);
			
		} catch(SmException e) {
			LOGGER.error("Failed getOffers for:" + subscriberId);
			throw new FulfillmentException(e.getComponent(), new ResponseCode(e.getStatusCode().getCode(), e.getStatusCode().getMessage()));
		}
		
		return returned;
	}


	@Override
	public List<Product> prepare(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();
		return returned;
	}


	@Override
	public List<Product> query(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();
		return returned;
	}


	@Override
	public List<Product> revert(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();
		return returned;
	}


	public String getOfferRequestedTypeFlag() {
		return offerRequestedTypeFlag;
	}


	public void setOfferRequestedTypeFlag(String offerRequestedTypeFlag) {
		this.offerRequestedTypeFlag = offerRequestedTypeFlag;
	}


	public OfferSelection[] getOfferSelection() {
		return offerSelection;
	}


	public void setOfferSelection(OfferSelection[] offerSelection) {
		this.offerSelection = offerSelection;
	}


	@Override
	public String toString() {
		return "GetOffersProfile [offerRequestedTypeFlag=" + offerRequestedTypeFlag + ", offerSelection=" + Arrays.toString(offerSelection)
				+ "]";
	}


	
	
	
}
