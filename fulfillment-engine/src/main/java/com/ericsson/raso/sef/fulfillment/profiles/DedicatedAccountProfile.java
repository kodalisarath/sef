package com.ericsson.raso.sef.fulfillment.profiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.sef.bes.api.entities.Product;

public class DedicatedAccountProfile extends BlockingFulfillment<Product> {
	private static final long	serialVersionUID	= -3496238267989562281L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DedicatedAccountProfile.class);
	
	private static final String	DA_DEDICATED_ACCOUNT_ID	= "READ_BALANCES_DEDICATED_ACCOUNT_ID";
	private static final String	DA_DEDICATED_ACCOUNT_UNIT_TYPE	= "DA_DEDICATED_ACCOUNT_UNIT_TYPE";
	private static final String	DA_DEDICATED_ACCOUNT_VALUE_1	= "DA_DEDICATED_ACCOUNT_VALUE_1";
	
	
	private Integer dedicatedAccountID;
	private Integer dedicatedAccountUnitType;
	private String transactionCurrency;
	private String transactionType;
	private String transactionCode;
	
	public DedicatedAccountProfile(String name) {
		super(name);
	}

	
	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		LOGGER.debug("Entering fulfill of DedicatedAccountProfile....");
		
		if (map == null || map.isEmpty())
			throw new FulfillmentException("ffe", new ResponseCode(1001, "runtime parameters 'metas' missing in request!!"));
		
		UpdateBalanceAndDateRequest request = new UpdateBalanceAndDateRequest();
		String requestParam = map.get("SUSBCRIBER_ID");
		if (requestParam == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'SUBSCRIBER_ID' missing in request!!"));
		request.setSubscriberNumber(requestParam);
		request.setSubscriberNumberNAI(1);
		request.setTransactionCurrency(this.transactionCurrency);
		request.setTransactionType(this.transactionType);
		request.setTransactionCode(this.transactionCode);
		LOGGER.debug("Packed direct attributes...");
		
		// pack the DA now
		DedicatedAccountUpdateInformation daInfo = new DedicatedAccountUpdateInformation();
		daInfo.setDedicatedAccountID(this.dedicatedAccountID);
		daInfo.setDedicatedAccountUnitType(this.dedicatedAccountUnitType);
		
		requestParam = map.get("ADJUSTMENT_AMOUNT_RELATIVE");
		if (requestParam == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'ADJUSTMENT_AMOUNT_RELATIVE' missing in request!!"));
		daInfo.setAdjustmentAmountRelative(requestParam);
		
		requestParam = map.get("DA_EXPIRY_DATE");
		if (requestParam == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'DA_EXPIRY_DATE' missing in request!!"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			daInfo.setExpiryDate(formatter.parse(requestParam));
		} catch (ParseException e1) {
			LOGGER.error("Bad Expiry Date: " + requestParam, e1);
			throw new FulfillmentException("ffe", new ResponseCode(1003, "runtime parameter 'DA_EXPIRY_DATE' bad in request!!"));
		}
		
		requestParam = map.get("DA_START_DATE");
		if (requestParam == null)
			throw new FulfillmentException("ffe", new ResponseCode(1002, "runtime parameter 'DA_START_DATE' missing in request!!"));
		try {
			daInfo.setStartDate(formatter.parse(requestParam));
		} catch (ParseException e1) {
			LOGGER.error("Bad Start Date: " + requestParam, e1);
			throw new FulfillmentException("ffe", new ResponseCode(1003, "runtime parameter 'DA_START_DATE' bad in request!!"));
		}
		LOGGER.debug("Packed dedicated account...");
		
		
		
		UpdateBalanceAndDateCommand command = new UpdateBalanceAndDateCommand(request);
		UpdateBalanceAndDateResponse response = null;
		try {
			response = command.execute();
		} catch (SmException e1) {
			LOGGER.error("Execution Failure for update balance & date!!", e1);
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}
		
		LOGGER.debug("Sending back response to fulfill now...");
		return createResponse(e, response);

	}

	private List<Product> createResponse(Product p, UpdateBalanceAndDateResponse response) {
		List<Product> products = new ArrayList<Product>();
		
		Product product = new Product();
		product.setResourceName(p.getResourceName());
		product.setQuotaDefined(-1);
		product.setValidity(-1);
		
		Map<String, String> balanceAndDateInfo = new HashMap<String, String>();
		for (DedicatedAccountChangeInformation daInfo: response.getDedicatedAccountInformation()) {
			balanceAndDateInfo.put(DA_DEDICATED_ACCOUNT_ID, "" + daInfo.getDedicatedAccountID());
			balanceAndDateInfo.put(DA_DEDICATED_ACCOUNT_UNIT_TYPE, "" + daInfo.getDedicatedAccountUnitType());
			balanceAndDateInfo.put(DA_DEDICATED_ACCOUNT_VALUE_1, "" + daInfo.getDedicatedAccountValue1());		
		}
		
		product.setMetas(balanceAndDateInfo);
		products.add(product);
		return products;
	}


	@Override
	public List<Product> prepare(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		List<Product> products = new ArrayList<Product>();
		return products;
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTransactionAmount(String transactionAmount) {
		// TODO Auto-generated method stub
		
	}
	

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public Integer getDedicatedAccountID() {
		return dedicatedAccountID;
	}

	public void setDedicatedAccountID(Integer dedicatedAccountID) {
		this.dedicatedAccountID = dedicatedAccountID;
	}

	public Integer getDedicatedAccountUnitType() {
		return dedicatedAccountUnitType;
	}

	public void setDedicatedAccountUnitType(Integer dedicatedAccountUnitType) {
		this.dedicatedAccountUnitType = dedicatedAccountUnitType;
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

}
