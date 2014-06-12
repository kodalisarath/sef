package com.ericsson.raso.sef.smart.commons.read;

public class WelcomePackRead {

	private String customerId;
	private Integer offerProfileKey;
	private Integer key;
	private String category;
	private Integer prefetchFilter;
	private String sCrmTitle;
	private Boolean sCanBeSharedByMultipleRops;
	private Boolean sInsertedViaBatch;
	private String sPackageId;
	private Boolean sPreActive;
	private Long sActivationEndTime;
	private Long sActivationStartTime;
	private Integer sPeriodStartPoint;

	@Override
	public String toString() {
		return "WelcomePackRead [customerId=" + customerId
				+ ", offerProfileKey=" + offerProfileKey + ", key=" + key
				+ ", category=" + category + ", prefetchFilter="
				+ prefetchFilter + ", sCrmTitle=" + sCrmTitle
				+ ", sCanBeSharedByMultipleRops=" + sCanBeSharedByMultipleRops
				+ ", sInsertedViaBatch=" + sInsertedViaBatch + ", sPackageId="
				+ sPackageId + ", sPreActive=" + sPreActive
				+ ", sActivationEndTime=" + sActivationEndTime
				+ ", sActivationStartTime=" + sActivationStartTime
				+ ", sPeriodStartPoint=" + sPeriodStartPoint + "]";
	}

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

	public String getsCrmTitle() {
		return sCrmTitle;
	}

	public void setsCrmTitle(String sCrmTitle) {
		this.sCrmTitle = sCrmTitle;
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

	public Long getsActivationEndTime() {
		return sActivationEndTime;
	}

	public void setsActivationEndTime(Long sActivationEndTime) {
		this.sActivationEndTime = sActivationEndTime;
	}

	public Long getsActivationStartTime() {
		return sActivationStartTime;
	}

	public void setsActivationStartTime(Long sActivationStartTime) {
		this.sActivationStartTime = sActivationStartTime;
	}

	public Integer getsPeriodStartPoint() {
		return sPeriodStartPoint;
	}

	public void setsPeriodStartPoint(Integer sPeriodStartPoint) {
		this.sPeriodStartPoint = sPeriodStartPoint;
	}
}
