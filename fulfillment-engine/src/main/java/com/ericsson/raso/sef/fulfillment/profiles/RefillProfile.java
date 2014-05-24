package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.entities.BlockingFulfillment;
import com.ericsson.raso.sef.client.air.command.RefillCommand;
import com.ericsson.raso.sef.client.air.request.RefillRequest;
import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.CurrencyCode;
import com.ericsson.sef.bes.api.entities.Product;

public class RefillProfile extends BlockingFulfillment<Product> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5706705833688931767L;
	private String refillProfileId;

	private Integer refillType;
	private String transactionAmount;
	private CurrencyCode transactionCurrency;


	protected RefillProfile(String name) {
		super(name);
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
	public void fulfill(Product e, Map<String, String> map) {
		
		RefillRequest refillRequest = new RefillRequest();
		refillRequest.setSubscriberNumber(map.get("msisdn"));
		refillRequest.setRefProfID(this.refillProfileId);
		refillRequest.setRefType(this.refillType);
		refillRequest.setTransacAmount(this.transactionAmount);
		refillRequest.setTransacCurrency(this.transactionCurrency.name());
		
		String extData1 = map.get(Constants.TRANSPARENT_META1);
		if(extData1 != null) {
			refillRequest.setExternalData1(extData1);
		}
		
		String extData2 = map.get(Constants.TRANSPARENT_META2);
		if(extData2 != null) {
			refillRequest.setExternalData2(extData2);
		}
		
		String extData3 = map.get(Constants.TRANSPARENT_META3);
		if(extData3 != null) {
			refillRequest.setExternalData3(Constants.TRANSPARENT_META3);
		}
		
		refillRequest.setRefAccBeforeFlag(true);
		refillRequest.setRefAccAfterFlag(true);
		
		RefillCommand command = new RefillCommand(refillRequest);
		try {
			command.execute();
		} catch (SmException e1) {
			e1.printStackTrace();
			//TODO: handle 114 response with different error code for alkansya 
		}
	}


	@Override
	public void prepare(Product e, Map<String, String> map) {
		
	}


	@Override
	public void query(Product e, Map<String, String> map) {
		
	}


	@Override
	public void revert(Product e, Map<String, String> map) {
		
	}

}
