package com.ericsson.raso.sef.bes.prodcat.entities;

public final class TaxAbsolute extends Tax {
	private static final long serialVersionUID = -7404700515612932232L;
	
	private long taxAbsolute = 0;

	public TaxAbsolute() {
		super(Taxation.TAX_ABSOLUTE);
	}
	
	@Override
	public MonetaryUnit calculateTax(MonetaryUnit baseAmount) {
		long taxAmount = (baseAmount.getAmount() + taxAbsolute);
		return new MonetaryUnit(baseAmount.getIso4217CurrencyCode(), taxAmount) {
			private static final long serialVersionUID = -1883988856558138590L;
		};
	}


	public long getTaxAbsolute() {
		return taxAbsolute;
	}

	public void setTaxAbsolute(byte taxPercentile) {
		this.taxAbsolute = taxPercentile;
	}

}
