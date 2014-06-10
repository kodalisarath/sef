package com.ericsson.raso.sef.smart.commons;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RechargeBalance implements Serializable {

	private static final long serialVersionUID = 1L;

	private int offerId;
	private String name;
	private long currentValue;
	private long previousValue;
	private Date expiryDate;
	
	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDelta() {
		if (currentValue < 0 || previousValue < 0) {
			return 0;
		} else {
			return (currentValue - previousValue);
		}
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public long getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(long currentValue) {
		this.currentValue = currentValue;
	}

	public long getPreviousValue() {
		return previousValue;
	}

	public void setPreviousValue(long previousValue) {
		this.previousValue = previousValue;
	}
	
	public String getExpiryDate(String dateformat) {
		return new SimpleDateFormat(dateformat).format(expiryDate);
	}
}
