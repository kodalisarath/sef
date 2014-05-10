package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;

public abstract class MonetaryUnit implements Serializable {
	private static final long serialVersionUID = 10L;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (amount ^ (amount >>> 32));
		result = prime * result + ((iso4217CurrencyCode == null) ? 0 : iso4217CurrencyCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!(obj instanceof MonetaryUnit))
			return false;
		
		MonetaryUnit other = (MonetaryUnit) obj;
		if (amount != other.amount)
			return false;
		
		if (iso4217CurrencyCode == null) {
			if (other.iso4217CurrencyCode != null)
				return false;
		} else if (!iso4217CurrencyCode.equals(other.iso4217CurrencyCode))
			return false;
		
		return true;
	}
	
	

}
