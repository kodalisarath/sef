package com.ericsson.raso.sef.smart.commons.read;

public class RopBucketRead {

	private String customerId;
	private Integer key;
	private String bCategory;
	private Integer bSeriesId;
	private String bValidFrom;
	private String bInvalidFrom;
	private Long onPeakFuBalance;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getbCategory() {
		return bCategory;
	}

	public void setbCategory(String bCategory) {
		this.bCategory = bCategory;
	}

	public Integer getbSeriesId() {
		return bSeriesId;
	}

	public void setbSeriesId(Integer bSeriesId) {
		this.bSeriesId = bSeriesId;
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

	public Long getOnPeakFuBalance() {
		return onPeakFuBalance;
	}

	public void setOnPeakFuBalance(Long onPeakFuBalance) {
		this.onPeakFuBalance = onPeakFuBalance;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}
