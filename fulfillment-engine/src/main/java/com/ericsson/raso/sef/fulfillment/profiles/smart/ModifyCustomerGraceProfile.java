package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.GetOffersCommand;
import com.ericsson.raso.sef.client.air.command.RefillCommand;
import com.ericsson.raso.sef.client.air.request.GetOffersRequest;
import com.ericsson.raso.sef.client.air.request.OfferSelection;
import com.ericsson.raso.sef.client.air.request.RefillRequest;
import com.ericsson.raso.sef.client.air.response.GetOffersResponse;
import com.ericsson.raso.sef.client.air.response.OfferInformation;
import com.ericsson.raso.sef.client.air.response.RefillResponse;
import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.CurrencyCode;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.fulfillment.profiles.BlockingFulfillment;
import com.ericsson.sef.bes.api.entities.Product;

public class ModifyCustomerGraceProfile extends BlockingFulfillment<Product> {
	private static final long serialVersionUID = 5066306075945796606L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ModifyCustomerGraceProfile.class);
	
	// Get Offers
	private String offerRequestedTypeFlag;	
	private OfferSelection[] offerSelection;
	
	// Refill
	private String refillProfileId;
	private Integer refillType;
	private String transactionAmount;
	private CurrencyCode transactionCurrency;

	
	public ModifyCustomerGraceProfile(String name) {
		super(name);
	}
	
	@Override
	public List<Product> fulfill(Product p, Map<String, String> map) throws FulfillmentException {
		
		String subscriberId = null;
		GetOffersRequest getOffersRequest = null;
		GetOffersCommand getOffersCommand = null;
		GetOffersResponse getOffersResponse = null;
		RefillRequest refillRequest = null;
		RefillResponse refillResponse = null;
		RefillCommand refillCommand = null;
		
		List<Product> returned = new ArrayList<Product>();
//		try {
//			LOGGER.debug("Starting GetOffers...");
//			getOffersRequest = new GetOffersRequest();
//			
//			subscriberId = map.get("SUBSCRIBER_ID");
//			if (subscriberId == null)
//				throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'SUBSCRIBER_ID' missing in request!!"));
//			getOffersRequest.setSubscriberNumber(subscriberId);
//			getOffersRequest.setSubscriberNumberNAI(1);
//			
//			getOffersRequest.setOfferRequestedTypeFlag(this.offerRequestedTypeFlag);
//			getOffersRequest.setOfferSelection(this.offerSelection);
//			
//			getOffersCommand = new GetOffersCommand(getOffersRequest);
//			getOffersResponse = getOffersCommand.execute();
//			LOGGER.error("GetOffers succesful...");
//		} catch(SmException e) {
//			LOGGER.error("Failed getOffers for:" + subscriberId);
//			throw new FulfillmentException(e.getComponent(), new ResponseCode(e.getStatusCode().getCode(), e.getStatusCode().getMessage()));
//		}
//		
//		// Sanity before proceeding....
//		for (OfferInformation offerInfo: getOffersResponse.getOfferInformation()) {
//			if (!(offerInfo.getOfferID() == 1  || offerInfo.getOfferID() == 2))
//				throw new FulfillmentException("ffe", new ResponseCode(402, "Invalid subscriber life cycle state"));
//		}
		
		
		
		try {
			LOGGER.debug("Starting Refill...");
			refillRequest = new RefillRequest();
			refillRequest.setSubscriberNumber(map.get("SUBSCRIBER_ID"));
			refillRequest.setRefProfID(this.refillProfileId);
			refillRequest.setRefType(this.refillType);
			refillRequest.setTransacAmount(this.transactionAmount);
			refillRequest.setTransacCurrency(this.transactionCurrency.name());
			refillRequest.setExternalData2(map.get("daysOfExtension"));
			
			String extData1 = map.get(Constants.EX_DATA1);
			if(extData1 != null) {
				refillRequest.setExternalData1(extData1);
			}
			
			String extData2 = map.get(Constants.EX_DATA2);
			if(extData2 != null) {
				refillRequest.setExternalData2(extData2);
			}
			
			String extData3 = map.get(Constants.EX_DATA3);
			if(extData3 != null) {
				refillRequest.setExternalData3(extData3);
			}
			
			refillRequest.setRefAccBeforeFlag(true);
			refillRequest.setRefAccAfterFlag(true);
			
			
			refillCommand = new RefillCommand(refillRequest);
			refillResponse = refillCommand.execute();
			LOGGER.debug("Refill successful...");
		} catch (SmException e) {
			LOGGER.error("Failed Refill for:" + subscriberId);
			throw new FulfillmentException(e.getComponent(), new ResponseCode(e.getStatusCode().getCode(), e.getStatusCode().getMessage()));
		}
		p.setMetas(map);
		returned.add(p);
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

	public String getRefillProfileId() {
		return refillProfileId;
	}

	public void setRefillProfileId(String refillProfileId) {
		this.refillProfileId = refillProfileId;
	}

	public Integer getRefillType() {
		return refillType;
	}

	public void setRefillType(Integer refillType) {
		this.refillType = refillType;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public CurrencyCode getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(CurrencyCode transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ModifyCustomerGraceProfile [offerRequestedTypeFlag=" + offerRequestedTypeFlag + ", offerSelection="
				+ Arrays.toString(offerSelection) + ", refillProfileId=" + refillProfileId + ", refillType=" + refillType
				+ ", transactionAmount=" + transactionAmount + ", transactionCurrency=" + transactionCurrency + "]";
	}


}
