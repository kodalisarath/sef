package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.entities.BlockingFulfillment;
import com.ericsson.sef.bes.api.entities.Product;

public class DedicatedAccountProfile extends BlockingFulfillment<com.ericsson.sef.bes.api.entities.Product> {

	
	private Integer dedicatedAccountID;
	private Integer dedicatedAccountUnitType;
	private String transactionCurrency;
	private String transactionType;
	private String transactionCode;
	private String externalData1;
	private String externalData2;
	
	protected DedicatedAccountProfile(String name) {
		super(name);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	public String getExternalData1() {
		return externalData1;
	}

	public void setExternalData1(String externalData1) {
		this.externalData1 = externalData1;
	}

	public String getExternalData2() {
		return externalData2;
	}

	public void setExternalData2(String externalData2) {
		this.externalData2 = externalData2;
	}


	@Override
	public void fulfill(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void query(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revert(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

}
