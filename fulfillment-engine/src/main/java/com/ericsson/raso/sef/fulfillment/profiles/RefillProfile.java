package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.RefillCommand;
import com.ericsson.raso.sef.client.air.request.RefillRequest;
import com.ericsson.raso.sef.client.air.response.AccBefAndAfterRef;
import com.ericsson.raso.sef.client.air.response.DedicatedAccountInformation;
import com.ericsson.raso.sef.client.air.response.OfferInformation;
import com.ericsson.raso.sef.client.air.response.RefillResponse;
import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.CurrencyCode;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.sef.bes.api.entities.Product;

public class RefillProfile extends BlockingFulfillment<Product> {
	private static final long serialVersionUID = 5706705833688931767L;
	private String refillProfileId;

	private Integer refillType;
	private String transactionAmount;
	private CurrencyCode transactionCurrency;

	private static final Logger LOGGER = LoggerFactory.getLogger(RefillProfile.class);
	
	public RefillProfile(String name) {
		super(name);
	}
	


	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		
		LOGGER.debug("Fufill request for refill");

		RefillRequest refillRequest = new RefillRequest();
		refillRequest.setSubscriberNumber(map.get("msisdn"));
		refillRequest.setSubscriberNumberNAI(1);
		refillRequest.setRefProfID(this.refillProfileId);
		refillRequest.setRefType(this.refillType);
		refillRequest.setTransacAmount(this.transactionAmount);
		refillRequest.setTransacCurrency(this.transactionCurrency.name());
		refillRequest.setRefAccBeforeFlag(true);
		refillRequest.setRefAccAfterFlag(true);
		
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
		
		RefillResponse response = null;
		RefillCommand command = new RefillCommand(refillRequest);
		
		try {
			response = command.execute();
		} catch (SmException e1) {
			LOGGER.error("Refill execution failed!!", e1);
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}
		
		LOGGER.debug("Refill successful. Preparing response to return.");
		return createResponse(e, response);
	}


	@Override
	public List<Product> prepare(Product e, Map<String, String> map) {
		LOGGER.debug("Preparing.......");
		List<Product> products = new ArrayList<Product>();
		return products;
	}


	@Override
	public List<Product> query(Product e, Map<String, String> map) {
		List<Product> products = new ArrayList<Product>();
		return products;
	}


	@Override
	public List<Product> revert(Product e, Map<String, String> map) {
		List<Product> products = new ArrayList<Product>();
		return products;
	}
	
	//TODO: Move to smart-commons
	private List<Product> createResponse(Product p, RefillResponse response) {
		List<Product> products = new ArrayList<Product>();
		
		Map<String, String> responseDetails = new HashMap<String, String>();
		
		AccBefAndAfterRef accBef= response.getAccountBeforeRefill();
		int index = 0;
		for (DedicatedAccountInformation daInfo: accBef.getDedicatedAccInfo()) {
			responseDetails.put("ACC_BEFORE_DA_ID" + ++index, "" + daInfo.getDedicatedAccountID());
			responseDetails.put("ACC_BEFORE_DA_VALUE" + index, "" + daInfo.getDedicatedAccountValue1());
		}
		responseDetails.put("ACC_BEFORE_SERVICE_FEE_EXPIRY_DATE", "" + accBef.getServiceFeeExpiryDate().getTime());
		responseDetails.put("ACC_BEFORE_SUPERVISION_EXPIRY_DATE", "" + accBef.getSupervisionExpiryDate().getTime());
	
		index = 0;
		for (OfferInformation offerInfo: accBef.getOfferInformationList()) {
			responseDetails.put("ACC_BEFORE_OFFER_ID" + ++index, "" + offerInfo.getOfferID());
			responseDetails.put("ACC_BEFORE_OFFER_EXPIRY_DATE" + index, "" + ((offerInfo.getExpiryDateTime() == null)? null: offerInfo.getExpiryDateTime().getTime()));			
		}
		
		AccBefAndAfterRef  accAft=  response.getAccountAfterRefill();
		index = 0;
		for (DedicatedAccountInformation daInfo: accAft.getDedicatedAccInfo()) {
			responseDetails.put("ACC_AFTER_DA_ID" + ++index, "" + daInfo.getDedicatedAccountID());
			responseDetails.put("ACC_AFTER_DA_VALUE" + index, "" + daInfo.getDedicatedAccountValue1());
		}
		responseDetails.put("ACC_AFTER_SERVICE_FEE_EXPIRY_DATE", "" + accAft.getServiceFeeExpiryDate().getTime());
		responseDetails.put("ACC_AFTER_SUPERVISION_EXPIRY_DATE", "" + accAft.getSupervisionExpiryDate().getTime());
	
		index = 0;
		for (OfferInformation offerInfo: accAft.getOfferInformationList()) {
			responseDetails.put("ACC_AFTER_OFFER_ID" + ++index, "" + offerInfo.getOfferID());
			responseDetails.put("ACC_AFTER_OFFER_EXPIRY_DATE" + index, "" + ((offerInfo.getExpiryDateTime() == null)? null: offerInfo.getExpiryDateTime().getTime()));			
		}
		
		p.setMetas(responseDetails);
		products.add(p);
		return products;
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

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((refillProfileId == null) ? 0 : refillProfileId.hashCode());
		result = prime * result
				+ ((refillType == null) ? 0 : refillType.hashCode());
		result = prime
				* result
				+ ((transactionAmount == null) ? 0 : transactionAmount
						.hashCode());
		result = prime
				* result
				+ ((transactionCurrency == null) ? 0 : transactionCurrency
						.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RefillProfile other = (RefillProfile) obj;
		if (refillProfileId == null) {
			if (other.refillProfileId != null)
				return false;
		} else if (!refillProfileId.equals(other.refillProfileId))
			return false;
		if (refillType == null) {
			if (other.refillType != null)
				return false;
		} else if (!refillType.equals(other.refillType))
			return false;
		if (transactionAmount == null) {
			if (other.transactionAmount != null)
				return false;
		} else if (!transactionAmount.equals(other.transactionAmount))
			return false;
		if (transactionCurrency != other.transactionCurrency)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "RefillProfile [refillProfileId=" + refillProfileId
				+ ", refillType=" + refillType + ", transactionAmount="
				+ transactionAmount + ", transactionCurrency="
				+ transactionCurrency + "]";
	}

}
