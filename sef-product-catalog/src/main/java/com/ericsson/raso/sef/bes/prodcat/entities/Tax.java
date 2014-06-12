package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;

public abstract class Tax implements Serializable {
	private static final long serialVersionUID = 8594693872789683320L;

	private String name = null;
	private Taxation taxation = null;

	protected Tax(Taxation type) {
		this.taxation = type;
	}

	public static Tax getInstance(Taxation type) {
		return null;
	}

	public abstract MonetaryUnit calculateTax(MonetaryUnit baseAmount);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Taxation getTaxation() {
		return taxation;
	}

	public void setTaxation(Taxation taxation) {
		this.taxation = taxation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((taxation == null) ? 0 : taxation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;
		
		if (!(obj instanceof Tax))
			return false;
		
		Tax other = (Tax) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (taxation != other.taxation)
			return false;
		
		return true;
	}

	enum Taxation implements Serializable {
		NO_TAX,
		TAX_PERCENTAGE,
		TAX_ABSOLUTE;
	}

	@Override
	public String toString() {
		return "Tax [name=" + name + ", taxation=" + taxation + "]";
	}
	
	

}
