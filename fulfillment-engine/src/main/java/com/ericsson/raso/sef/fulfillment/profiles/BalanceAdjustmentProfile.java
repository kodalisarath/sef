package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.UpdateBalanceAndDateCommand;
import com.ericsson.raso.sef.client.air.request.DedicatedAccountUpdateInformation;
import com.ericsson.raso.sef.client.air.request.UpdateBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.response.DedicatedAccountChangeInformation;
import com.ericsson.raso.sef.client.air.response.UpdateBalanceAndDateResponse;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.sef.bes.api.entities.Product;

public class BalanceAdjustmentProfile extends BlockingFulfillment<Product> {
	private static final long serialVersionUID = 5706705833688931767L;
	private static final Logger LOGGER = LoggerFactory.getLogger(BalanceAdjustmentProfile.class);
	private static final String DA_ID = null;
	private static final String DA_VALUE_1 = null;
	private static final String DA_UNIT_TYPE = null; 

	private String transactionCurrency;
	private String transactionType;
	private String transactionCode;
	private int dedicatedAccountID;
	private int dedicatedAccountUnitType;
	

	public BalanceAdjustmentProfile(String name) {
		super(name);
	}
	
	
	

	@Override
	public List<Product> fulfill(Product p, Map<String, String> map) throws FulfillmentException {
		List<Product> products = new ArrayList<Product>();	
		
		String msisdn = map.get("msisdn");
		String externalData1 = map.get("eventName");
		String externalData2 = map.get("eventInfo");
		String amount = map.get("amountOfUnits");
		
		UpdateBalanceAndDateRequest request = new UpdateBalanceAndDateRequest();
		
		List<DedicatedAccountUpdateInformation> dedicatedAccountUpdateInformation = new ArrayList<DedicatedAccountUpdateInformation>();
		DedicatedAccountUpdateInformation daUpdateInfo = new DedicatedAccountUpdateInformation();
		daUpdateInfo.setDedicatedAccountID(this.dedicatedAccountID);
		daUpdateInfo.setDedicatedAccountUnitType(dedicatedAccountUnitType);
		daUpdateInfo.setAdjustmentAmountRelative(amount);
		dedicatedAccountUpdateInformation.add(daUpdateInfo);
		
		request.setDedicatedAccountUpdateInformation(dedicatedAccountUpdateInformation);
		request.setExternalData1(externalData1);
		request.setExternalData2(externalData2);
		request.setSubscriberNumber(msisdn);
		request.setSubscriberNumberNAI(1);
		
		UpdateBalanceAndDateCommand command = new UpdateBalanceAndDateCommand(request);
		UpdateBalanceAndDateResponse response = null;
		try {
			response = command.execute();
		} catch (SmException e) {
			LOGGER.error("Failed BalanceAdjustment. Cause: " + e.getMessage(), e);
			throw new FulfillmentException(e.getComponent(), e.getStatusCode());
		}
		
		p.setMetas(this.handleResponse(response));
		products.add(p);
		return products;


	}


	

	@Override
	public List<Product> prepare(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> products = new ArrayList<Product>();	
		return products;
	}


	@Override
	public List<Product> query(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> products = new ArrayList<Product>();	
		return products;
	}


	@Override
	public List<Product> revert(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> products = new ArrayList<Product>();	
		return products;
	}
	
	private Map<String, String> handleResponse(UpdateBalanceAndDateResponse response) {
		Map<String, String> responseDetails = new HashMap<String, String>();
		
		int index = 0;
		for (DedicatedAccountChangeInformation daInfo: response.getDedicatedAccountInformation()) {
			responseDetails.put(DA_ID + "." + ++index, "" + daInfo.getDedicatedAccountID());
			responseDetails.put(DA_VALUE_1 + "." + index, "" + daInfo.getDedicatedAccountValue1());
			responseDetails.put(DA_UNIT_TYPE + "." + index, "" + daInfo.getDedicatedAccountUnitType());			
		}
		
		return responseDetails;
	}




	public String getTransactionCurrency() {
		return transactionCurrency;
	}




	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}



	public String getTransactionType() {
		return transactionType;
	}




	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}




	public String getTransactionCode() {
		return transactionCode;
	}




	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}




	@Override
	public String toString() {
		return "ReadBalancesProfile []";
	}




	public void setDedicatedAccountID(int dedicatedAccountID) {
		this.dedicatedAccountID = dedicatedAccountID;		
	}




	public void setDedicatedAccountUnitType(int dedicatedAccountUnitType) {
		this.dedicatedAccountUnitType = dedicatedAccountUnitType;
		
	}

}
