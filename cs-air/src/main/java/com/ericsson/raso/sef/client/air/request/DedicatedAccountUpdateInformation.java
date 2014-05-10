package com.ericsson.raso.sef.client.air.request;

import java.util.Date;

public class DedicatedAccountUpdateInformation extends NativeAirRequest {
	
	private Integer dedicatedAccountID;
	private String dedicatedAccountValueNew;
	private String adjustmentAmountRelative;
	private Date expiryDate;
	private Date startDate;
	private Date startDateCurrent;
	private Date expiryDateCurrent;
	private Integer dedicatedAccountUnitType;
	
	public Integer getDedicatedAccountID() {
		return dedicatedAccountID;
	}
	
	public void setDedicatedAccountID(Integer dedicatedAccountID) {
		this.dedicatedAccountID = dedicatedAccountID;
		addParam("dedicatedAccountID", dedicatedAccountID);
	}
	
	public String getDedicatedAccountValueNew() {
		return dedicatedAccountValueNew;
	}
	public void setDedicatedAccountValueNew(String dedicatedAccountValueNew) {
		this.dedicatedAccountValueNew = dedicatedAccountValueNew;
		addParam("dedicatedAccountValueNew", dedicatedAccountValueNew);
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
		addParam("expiryDate", expiryDate);
	}

	public String getAdjustmentAmountRelative() {
		return adjustmentAmountRelative;
	}

	public void setAdjustmentAmountRelative(String adjustmentAmountRelative) {
		this.adjustmentAmountRelative = adjustmentAmountRelative;
		addParam("adjustmentAmountRelative", adjustmentAmountRelative);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		addParam("startDate", startDate);
	}
	
	public Date getStartDateCurrent() {
		return startDateCurrent;
	}

	public void setStartDateCurrent(Date startDateCurrent) {
		this.startDateCurrent = startDateCurrent;
		addParam("startDateCurrent", startDateCurrent);
	}

	public Date getExpiryDateCurrent() {
		return expiryDateCurrent;
	}

	public void setExpiryDateCurrent(Date expiryDateCurrent) {
		this.expiryDateCurrent = expiryDateCurrent;
		addParam("expiryDateCurrent", expiryDateCurrent);
	}

	public Integer getDedicatedAccountUnitType() {
		return dedicatedAccountUnitType;
	}

	public void setDedicatedAccountUnitType(Integer dedicatedAccountUnitType) {
		this.dedicatedAccountUnitType = dedicatedAccountUnitType;
		addParam("dedicatedAccountUnitType", dedicatedAccountUnitType);
	}
}
