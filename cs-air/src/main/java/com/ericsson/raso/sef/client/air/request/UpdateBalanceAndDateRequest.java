package com.ericsson.raso.sef.client.air.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateBalanceAndDateRequest extends AbstractAirRequest {

	public UpdateBalanceAndDateRequest() {
		super("UpdateBalanceAndDate");
	}
	
	private String transactionCurrency;
	private Date serviceFeeExpiryDate;
	private Date supervisionExpiryDate;
	private String transactionType;
	private String transactionCode;
	private String externalData1;
	private String externalData2;
	
	
	private List<DedicatedAccountUpdateInformation> dedicatedAccountUpdateInformation;

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
		addParam("transactionCurrency", transactionCurrency);
	}

	public Date getServiceFeeExpiryDate() {
		return serviceFeeExpiryDate;
	}

	public void setServiceFeeExpiryDate(Date serviceFeeExpiryDate) {
		this.serviceFeeExpiryDate = serviceFeeExpiryDate;
		addParam("serviceFeeExpiryDate", serviceFeeExpiryDate);
	}

	public Date getSupervisionExpiryDate() {
		return supervisionExpiryDate;
	}

	public void setSupervisionExpiryDate(Date supervisionExpiryDate) {
		this.supervisionExpiryDate = supervisionExpiryDate;
		addParam("supervisionExpiryDate", supervisionExpiryDate);
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
		addParam("transactionType", transactionType);
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
		addParam("transactionCode", transactionCode);
	}

	public String getExternalData1() {
		return externalData1;
	}

	public void setExternalData1(String externalData1) {
		this.externalData1 = externalData1;
		addParam("externalData1", externalData1);
	}

	public String getExternalData2() {
		return externalData2;
	}

	public void setExternalData2(String externalData2) {
		this.externalData2 = externalData2;
		addParam("externalData2", externalData2);
	}

	public List<DedicatedAccountUpdateInformation> getDedicatedAccountUpdateInformation() {
		return dedicatedAccountUpdateInformation;
	}

	public void setDedicatedAccountUpdateInformation(
			List<DedicatedAccountUpdateInformation> dedicatedAccountUpdateInformation) {
		this.dedicatedAccountUpdateInformation = dedicatedAccountUpdateInformation;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (DedicatedAccountUpdateInformation da : dedicatedAccountUpdateInformation) {
			list.add(da.toNative());	
		}
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object>[] map = new HashMap[]{};
		addParam("dedicatedAccountUpdateInformation", list.toArray(map));
	}
}
