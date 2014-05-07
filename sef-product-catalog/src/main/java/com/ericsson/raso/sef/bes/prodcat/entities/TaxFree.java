package com.ericsson.raso.sef.bes.prodcat.entities;

public final class TaxFree extends Tax {
	private static final long serialVersionUID = 7724032620961630707L;

	public TaxFree() {
		super(Taxation.NO_TAX);
	}

	@Override
	public MonetaryUnit calculateTax(MonetaryUnit baseAmount) {
		return new MonetaryUnit(baseAmount.getIso4217CurrencyCode(), 0) {
			private static final long serialVersionUID = 5916881446317655006L;
		};
	}

	
}
