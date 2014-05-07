package com.ericsson.raso.sef.bes.prodcat.entities;

public final class TaxPercentage extends Tax {
	private static final long serialVersionUID = 5842494988982857966L;

	private byte taxPercentile = 0;

	public TaxPercentage() {
		super(Taxation.TAX_PERCENTAGE);
	}

	@Override
	public MonetaryUnit calculateTax(MonetaryUnit baseAmount) {
		long taxAmount = ((baseAmount.getAmount() * taxPercentile) / 100);
		return new MonetaryUnit(baseAmount.getIso4217CurrencyCode(), taxAmount) {
			private static final long serialVersionUID = 2869828489736276348L;
		};
	}

	public byte getTaxPercentile() {
		return taxPercentile;
	}

	public void setTaxPercentile(byte taxPercentile) {
		this.taxPercentile = taxPercentile;
	}

	
}
