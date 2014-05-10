package com.ericsson.raso.sef.client.air.response;

import java.util.Map;

public class UsageThresholdInformation extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	private Integer usageThresholdID;
	private String usageThresholdValue;
	private String usageThresholdMonetaryValue1;
	private String usageThresholdMonetaryValue2;
	private Integer usageThresholdSource;
	private String associatedPartyID;

	public UsageThresholdInformation(Map<String, Object> paramMap) {
		super(paramMap);
	}

	public Integer getUsageThresholdID() {
		if (usageThresholdID == null) {
			usageThresholdID = getParam("usageThresholdID", Integer.class);
		}
		return usageThresholdID;
	}

	public String getUsageThresholdValue() {
		if (usageThresholdValue == null) {
			usageThresholdValue = getParam("usageThresholdValue", String.class);
		}
		return usageThresholdValue;
	}

	public String getUsageThresholdMonetaryValue1() {
		if (usageThresholdMonetaryValue1 == null) {
			usageThresholdMonetaryValue1 = getParam("usageThresholdMonetaryValue1", String.class);
		}
		return usageThresholdMonetaryValue1;
	}

	public String getUsageThresholdMonetaryValue2() {
		if (usageThresholdMonetaryValue2 == null) {
			usageThresholdMonetaryValue2 = getParam("usageThresholdMonetaryValue2", String.class);
		}
		return usageThresholdMonetaryValue2;
	}

	public Integer getUsageThresholdSource() {
		if (usageThresholdSource == null) {
			usageThresholdSource = getParam("usageThresholdSource", Integer.class);
		}
		return usageThresholdSource;
	}

	public String getAssociatedPartyID() {
		if (associatedPartyID == null) {
			associatedPartyID = getParam("associatedPartyID", String.class);
		}
		return associatedPartyID;
	}

	public void setUsageThresholdID(Integer usageThresholdID) {
		this.usageThresholdID = usageThresholdID;
	}

	public void setUsageThresholdValue(String usageThresholdValue) {
		this.usageThresholdValue = usageThresholdValue;
	}

	public void setUsageThresholdMonetaryValue1(String usageThresholdMonetaryValue1) {
		this.usageThresholdMonetaryValue1 = usageThresholdMonetaryValue1;
	}

	public void setUsageThresholdMonetaryValue2(String usageThresholdMonetaryValue2) {
		this.usageThresholdMonetaryValue2 = usageThresholdMonetaryValue2;
	}

	public void setUsageThresholdSource(Integer usageThresholdSource) {
		this.usageThresholdSource = usageThresholdSource;
	}

	public void setAssociatedPartyID(String associatedPartyID) {
		this.associatedPartyID = associatedPartyID;
	}

}
