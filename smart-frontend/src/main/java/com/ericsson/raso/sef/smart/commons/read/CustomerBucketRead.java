package com.ericsson.raso.sef.smart.commons.read;


public class CustomerBucketRead {

	@Override
	public String toString() {
		return "CustomerBucketRead [customerId=" + customerId + ", bSeriesId="
				+ bSeriesId + ", bCategory=" + bCategory + ", bValidFrom="
				+ bValidFrom + ", bInvalidFrom=" + bInvalidFrom + "]";
	}

	private String customerId;
	private Integer bSeriesId;
	private String bCategory;
	private String bValidFrom;
	private String bInvalidFrom;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getbSeriesId() {
		return bSeriesId;
	}

	public void setbSeriesId(Integer bSeriesId) {
		this.bSeriesId = bSeriesId;
	}

	public String getbCategory() {
		return bCategory;
	}

	public void setbCategory(String bCategory) {
		this.bCategory = bCategory;
	}

	public String getbValidFrom() {
		return bValidFrom;
	}

	public void setbValidFrom(String bValidFrom) {
		this.bValidFrom = bValidFrom;
	}

	public String getbInvalidFrom() {
		return bInvalidFrom;
	}

	public void setbInvalidFrom(String bInvalidFrom) {
		this.bInvalidFrom = bInvalidFrom;
	}
}
