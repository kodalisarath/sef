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
		return new Cost(baseAmount.getIso4217CurrencyCode(), taxAmount); 

	}

	public byte getTaxPercentile() {
		return taxPercentile;
	}

	public void setTaxPercentile(byte taxPercentile) {
		this.taxPercentile = taxPercentile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + taxPercentile;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!super.equals(obj))
			return false;
		
		if (!(obj instanceof TaxPercentage))
			return false;
		
		TaxPercentage other = (TaxPercentage) obj;
		if (taxPercentile != other.taxPercentile)
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "<Tax type='" + this.getTaxation() + "' name='" + this.getName() + "' tax='" + taxPercentile + "' />";
	}
	
	
	
}
