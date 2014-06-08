package com.ericsson.raso.sef.smart.commons.read;


public class RppRead {

	private String customerId;
	private Integer offerProfileKey;
	private Integer key;
	private String category;
	private Integer prefetchFilter;
	private String sCRMTitle;
	private Boolean sCanBeSharedByMultipleRops;
	private Boolean sInsertedViaBatch;
	private String sPackageId;
	private Boolean sPreActive;
	private String sActivationEndTime;
	private Long c_TokenBasedExpiredDate;
	private String sActivationStartTime;
	private Integer sPeriodStartPoint;
	private Long cIACCreditLimitValidity;
	private Integer cUnliResetRechargeValidity;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getPrefetchFilter() {
		return prefetchFilter;
	}

	public void setPrefetchFilter(Integer prefetchFilter) {
		this.prefetchFilter = prefetchFilter;
	}

	public String getsCRMTitle() {
		return sCRMTitle;
	}

	public void setsCRMTitle(String sCRMTitle) {
		this.sCRMTitle = sCRMTitle;
	}

	public Boolean getsCanBeSharedByMultipleRops() {
		return sCanBeSharedByMultipleRops;
	}

	public void setsCanBeSharedByMultipleRops(Boolean sCanBeSharedByMultipleRops) {
		this.sCanBeSharedByMultipleRops = sCanBeSharedByMultipleRops;
	}

	public Boolean getsInsertedViaBatch() {
		return sInsertedViaBatch;
	}

	public void setsInsertedViaBatch(Boolean sInsertedViaBatch) {
		this.sInsertedViaBatch = sInsertedViaBatch;
	}

	public String getsPackageId() {
		return sPackageId;
	}

	public void setsPackageId(String sPackageId) {
		this.sPackageId = sPackageId;
	}

	public Boolean getsPreActive() {
		return sPreActive;
	}

	public void setsPreActive(Boolean sPreActive) {
		this.sPreActive = sPreActive;
	}

	public String getsActivationEndTime() {
		return sActivationEndTime;
	}

	public void setsActivationEndTime(String sActivationEndTime) {
		this.sActivationEndTime = sActivationEndTime;
	}

	public Integer getsPeriodStartPoint() {
		return sPeriodStartPoint;
	}

	public void setsPeriodStartPoint(Integer sPeriodStartPoint) {
		this.sPeriodStartPoint = sPeriodStartPoint;
	}

	public Long getcIACCreditLimitValidity() {
		return cIACCreditLimitValidity;
	}

	public void setcIACCreditLimitValidity(Long cIACCreditLimitValidity) {
		this.cIACCreditLimitValidity = cIACCreditLimitValidity;
	}

	public Integer getcUnliResetRechargeValidity() {
		return cUnliResetRechargeValidity;
	}

	public void setcUnliResetRechargeValidity(Integer cUnliResetRechargeValidity) {
		this.cUnliResetRechargeValidity = cUnliResetRechargeValidity;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getsActivationStartTime() {
		return sActivationStartTime;
	}
	
	public void setsActivationStartTime(String sActivationStartTime) {
		this.sActivationStartTime = sActivationStartTime;
	}

	public  Long getC_TokenBasedExpiredDate() {
		return c_TokenBasedExpiredDate;
	}

	public void setC_TokenBasedExpiredDate(Long c_TokenBasedExpiredDate) {
		this.c_TokenBasedExpiredDate = c_TokenBasedExpiredDate;
	}
	
	
}
