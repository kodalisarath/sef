package com.ericsson.raso.sef.bes.engine.transaction.entities;



public final class GetAdviceOfChargeResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 1953619690977342269L;

	private long amount = 0;
	private String iso4217Currency = null;

	public GetAdviceOfChargeResponse(String requestCorrelator) {
		super(requestCorrelator);
	}

	public GetAdviceOfChargeResponse(String requestCorrelator, long amount, String iso4217Currency) {
		super(requestCorrelator);
		this.amount = amount;
		this.iso4217Currency = iso4217Currency;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getIso4217Currency() {
		return iso4217Currency;
	}

	public void setIso4217Currency(String iso4217Currency) {
		this.iso4217Currency = iso4217Currency;
	}

	
		
	
}
