package com.ericsson.sm.core.db.model;

public enum CurrencyCode {
	
	PHP(100), CENTS(1);
	
	private int exponent;

	private CurrencyCode(int exponent) {
		this.exponent = exponent;
	}

	public int getExponent() {
		return exponent;
	}
}
