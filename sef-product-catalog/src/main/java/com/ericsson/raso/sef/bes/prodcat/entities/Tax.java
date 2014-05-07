package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;

public abstract class Tax implements Serializable {
	private static final long serialVersionUID = 8594693872789683320L;
	
	private String name = null;
	private Taxation taxation = null;
	
	protected Tax(Taxation type){
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




	enum Taxation {
		NO_TAX,
		TAX_PERCENTAGE,
		TAX_ABSOLUTE;
	}

}
