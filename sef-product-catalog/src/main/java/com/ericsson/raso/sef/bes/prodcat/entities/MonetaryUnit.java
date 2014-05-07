package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;

public abstract class MonetaryUnit implements Serializable {
	private static final long serialVersionUID = 337653224125706248L;

	private String iso4217CurrencyCode = null;
	private long amount = -1L;

	public MonetaryUnit(String iso4217CurrencyCode, long amount) {
		this.iso4217CurrencyCode = iso4217CurrencyCode;
		this.amount = amount;
	}

	public String getIso4217CurrencyCode() {
		return iso4217CurrencyCode;
	}

	public void setIso4217CurrencyCode(String iso4217CurrencyCode) {
		this.iso4217CurrencyCode = iso4217CurrencyCode;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}
	
	

}
