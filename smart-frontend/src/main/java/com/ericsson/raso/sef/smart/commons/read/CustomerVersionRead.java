package com.ericsson.raso.sef.smart.commons.read;

public class CustomerVersionRead {

	private String customerId;
	private String vValidFrom;
	private String vInvalidFrom;
	private String category;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getvValidFrom() {
		return vValidFrom;
	}

	public void setvValidFrom(String vValidFrom) {
		this.vValidFrom = vValidFrom;
	}

	public String getvInvalidFrom() {
		return vInvalidFrom;
	}

	public void setvInvalidFrom(String vInvalidFrom) {
		this.vInvalidFrom = vInvalidFrom;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
