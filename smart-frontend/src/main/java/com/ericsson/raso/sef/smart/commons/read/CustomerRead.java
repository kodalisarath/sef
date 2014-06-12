package com.ericsson.raso.sef.smart.commons.read;

public class CustomerRead {

	@Override
	public String toString() {
		return "CustomerRead [customerId=" + customerId + ", billCycleId="
				+ billCycleId + ", billCycleIdAfterSwitch="
				+ billCycleIdAfterSwitch + ", billCycleSwitch="
				+ billCycleSwitch + ", category=" + category
				+ ", prefetchFilter=" + prefetchFilter + "]";
	}

	private String customerId;
	private Integer billCycleId;
	private Integer billCycleIdAfterSwitch;
	private String billCycleSwitch ;
	private String category;
	private Integer prefetchFilter;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getBillCycleId() {
		return billCycleId;
	}

	public void setBillCycleId(Integer billCycleId) {
		this.billCycleId = billCycleId;
	}

	public Integer getBillCycleIdAfterSwitch() {
		return billCycleIdAfterSwitch;
	}

	public void setBillCycleIdAfterSwitch(Integer billCycleIdAfterSwitch) {
		this.billCycleIdAfterSwitch = billCycleIdAfterSwitch;
	}

	public String getBillCycleSwitch() {
		return billCycleSwitch;
	}

	public void setBillCycleSwitch(String billCycleSwitch) {
		this.billCycleSwitch = billCycleSwitch;
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
}
