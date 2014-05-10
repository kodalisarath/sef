package com.ericsson.raso.sef.client.air.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UsageCounterUsageThresholdInformation extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	private Integer usageCounterID;
	private String usageCounterValue;
	private String usageCounterMonetaryValue1;
	private String usageCounterMonetaryValue2;
	private List<UsageThresholdInformation> usageThresholdInformation;
	private String associatedPartyID;
	private Integer productID;

	public UsageCounterUsageThresholdInformation(Map<String, Object> paramMap) {
		super(paramMap);
	}

	public Integer getUsageCounterID() {
		if (usageCounterID == null) {
			usageCounterID = getParam("usageCounterID", Integer.class);
		}
		return usageCounterID;
	}

	public String getUsageCounterValue() {
		if (usageCounterValue == null) {
			usageCounterValue = getParam("usageCounterValue", String.class);
		}
		return usageCounterValue;
	}

	public String getUsageCounterMonetaryValue1() {
		if (usageCounterMonetaryValue1 == null) {
			usageCounterMonetaryValue1 = getParam("usageCounterMonetaryValue1", String.class);
		}
		return usageCounterMonetaryValue1;
	}

	public String getUsageCounterMonetaryValue2() {
		if (usageCounterMonetaryValue2 == null) {
			usageCounterMonetaryValue2 = getParam("usageCounterMonetaryValue2", String.class);
		}
		return usageCounterMonetaryValue2;
	}

	public List<UsageThresholdInformation> getUsageThresholdInformation() {
		if(usageThresholdInformation == null) {
			usageThresholdInformation = new ArrayList<UsageThresholdInformation>();
			Object[] list = getParam("usageThresholdInformation", Object[].class);
			for (Object object : list) {
				usageThresholdInformation.add(new UsageThresholdInformation((Map<String, Object>) object));
			}
		}
		return usageThresholdInformation;
	}

	public String getAssociatedPartyID() {
		if (associatedPartyID == null) {
			associatedPartyID = getParam("associatedPartyID", String.class);
		}
		return associatedPartyID;
	}

	public Integer getProductID() {
		if (productID == null) {
			productID = getParam("productID", Integer.class);
		}
		return productID;
	}

	public void setUsageCounterID(Integer usageCounterID) {
		this.usageCounterID = usageCounterID;
	}

	public void setUsageCounterValue(String usageCounterValue) {
		this.usageCounterValue = usageCounterValue;
	}

	public void setUsageCounterMonetaryValue1(String usageCounterMonetaryValue1) {
		this.usageCounterMonetaryValue1 = usageCounterMonetaryValue1;
	}

	public void setUsageCounterMonetaryValue2(String usageCounterMonetaryValue2) {
		this.usageCounterMonetaryValue2 = usageCounterMonetaryValue2;
	}

	public void setUsageThresholdInformation(List<UsageThresholdInformation> usageThresholdInformation) {
		this.usageThresholdInformation = usageThresholdInformation;
	}

	public void setAssociatedPartyID(String associatedPartyID) {
		this.associatedPartyID = associatedPartyID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

}
