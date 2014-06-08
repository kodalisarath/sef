package com.ericsson.raso.sef.smart.commons.read;

public class WelcomePackBucketRead {

	private Integer key;
	private String sPackageId;
	private String customerId;
	private Integer offerProfileKey;
	private Integer bSeriesId;
	private String bCategory;
	private String bValidFrom;
	private String bInvalidFrom;
	private Boolean sActive;
	private Byte sError;
	private Integer sInfo;
	private Boolean sValid;
	private String sExpiryDate;
	private Long sNextPeriodAct;

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

	public Boolean getsActive() {
		return sActive;
	}

	public void setsActive(Boolean sActive) {
		this.sActive = sActive;
	}

	public Byte getsError() {
		return sError;
	}

	public void setsError(Byte sError) {
		this.sError = sError;
	}

	public Integer getsInfo() {
		return sInfo;
	}

	public void setsInfo(Integer sInfo) {
		this.sInfo = sInfo;
	}

	public Boolean getsValid() {
		return sValid;
	}

	public void setsValid(Boolean sValid) {
		this.sValid = sValid;
	}

	public String getsExpiryDate() {
		return sExpiryDate;
	}

	public void setsExpiryDate(String sExpiryDate) {
		this.sExpiryDate = sExpiryDate;
	}

	public Long getsNextPeriodAct() {
		return sNextPeriodAct;
	}

	public void setsNextPeriodAct(Long sNextPeriodAct) {
		this.sNextPeriodAct = sNextPeriodAct;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getsPackageId() {
		return sPackageId;
	}

	public void setsPackageId(String sPackageId) {
		this.sPackageId = sPackageId;
	}
}
