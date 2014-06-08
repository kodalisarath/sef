package com.ericsson.raso.sef.smart.commons.read;

public class RppVersionRead {

	private String customerId;
	private Integer offerProfileKey;
	private Integer key;
	private String vValidFrom;
	private String vInvalidFrom;
	private String category;
	private String sPeriodicBonusExpiryDate;
	private Long sPeriodicBonusCreditLimit;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getOfferProfileKey() {
		return offerProfileKey;
	}

	public void setOfferProfileKey(Integer offerProfileKey) {
		this.offerProfileKey = offerProfileKey;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
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

	public String getsPeriodicBonusExpiryDate() {
		return sPeriodicBonusExpiryDate;
	}

	public void setsPeriodicBonusExpiryDate(String sPeriodicBonusExpiryDate) {
		this.sPeriodicBonusExpiryDate = sPeriodicBonusExpiryDate;
	}

	public Long getsPeriodicBonusCreditLimit() {
		return sPeriodicBonusCreditLimit;
	}

	public void setsPeriodicBonusCreditLimit(Long sPeriodicBonusCreditLimit) {
		this.sPeriodicBonusCreditLimit = sPeriodicBonusCreditLimit;
	}
}
