package com.ericsson.raso.sef.core.db.model;

import java.io.Serializable;

public class ChargeAmount implements Serializable {

	private static final long serialVersionUID = 2593633411309401507L;
	
	private long amount = 0l;
	private CurrencyCode currencyCode = null;

	public ChargeAmount() {
		super();
	}
	
	public ChargeAmount(long amount, CurrencyCode currencyCode) {
		super();
		this.amount = amount;
		this.currencyCode = currencyCode;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public CurrencyCode getCurrencyCode() {
		return currencyCode;
	}
	
	public void setCurrencyCode(CurrencyCode currencyCode) {
		this.currencyCode = currencyCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Charging Amount [ ");
		sb.append(String.format("%s: %d, ", "Amount", this.getAmount()));
		sb.append(String.format("%s: %s", "Currence Code", this.currencyCode));
		return sb.toString();
	}
}
