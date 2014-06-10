package com.ericsson.raso.sef.smart.commons;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int offerId;
	private int daId;
	private String name;
	private long balance;
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

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public String getExpiryDate(String dateformat) {
		return new SimpleDateFormat(dateformat).format(expiryDate);
	}
	
	public int getDaId() {
		return daId;
	}
	
	public void setDaId(int daId) {
		this.daId = daId;
	}
}
