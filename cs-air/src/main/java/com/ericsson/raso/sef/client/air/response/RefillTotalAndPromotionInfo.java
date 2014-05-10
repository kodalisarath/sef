package com.ericsson.raso.sef.client.air.response;

import java.util.List;
import java.util.Map;

public class RefillTotalAndPromotionInfo extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public RefillTotalAndPromotionInfo(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private String refillAmount1;
	private String refillAmount2;
	private Integer supervisionDaysExtended;
	private Integer serviceFeeDaysExtended;
	private List<DedicatedRefill> dedicatedAccRefInfo;
	private List<UsageAccumulatorInfo> usageAccumulatorInformation;

	public String getRefillAmount1() {
		if (refillAmount1 == null) {
			refillAmount1 = getParam("refillAmount1", String.class);
		}
		return refillAmount1;
	}

	public String getRefillAmount2() {
		if (refillAmount2 == null) {
			refillAmount2 = getParam("refillAmount2", String.class);
		}
		return refillAmount2;
	}

	public Integer getSupervisionDaysExtended() {
		if (supervisionDaysExtended == null) {
			supervisionDaysExtended = getParam("supervisionDaysExtended", Integer.class);
		}
		return supervisionDaysExtended;
	}

	public Integer getServiceFeeDaysExtended() {
		if (serviceFeeDaysExtended == null) {
			serviceFeeDaysExtended = getParam("serviceFeeDaysExtended", Integer.class);
		}
		return serviceFeeDaysExtended;
	}

	public List<DedicatedRefill> getDedicatedAccRefInfo() {
		return dedicatedAccRefInfo;
	}

	public List<UsageAccumulatorInfo> getUsageAccumulatorInformation() {
		return usageAccumulatorInformation;
	}

}
