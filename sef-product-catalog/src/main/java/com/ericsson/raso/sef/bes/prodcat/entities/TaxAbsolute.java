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
		return new Cost(baseAmount.getIso4217CurrencyCode(), taxAmount); 
	}


	public long getTaxAbsolute() {
		return taxAbsolute;
	}

	public void setTaxAbsolute(byte taxPercentile) {
		this.taxAbsolute = taxPercentile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (taxAbsolute ^ (taxAbsolute >>> 32));
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
		
		if (!(obj instanceof TaxAbsolute))
			return false;
		
		TaxAbsolute other = (TaxAbsolute) obj;
		if (taxAbsolute != other.taxAbsolute)
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "<Tax type='" + this.getTaxation() + "' name='" + this.getName() + "' tax='" + taxAbsolute + "' />";
	}
	

}
