package com.ericsson.raso.sef.client.air.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetUsageThresholdsAndCountersResponse extends AbstractAirResponse {

	private static final long serialVersionUID = 1L;

	private String currency1;
	private String currency2;
	private Integer[] negotiatedCapabilities;
	private Integer[] availableServerCapabilities;
	private List<UsageCounterUsageThresholdInformation> usageCounterUsageThresholdInformation;

	public String getCurrency1() {
		if (currency1 == null) {
			currency1 = (String) (((Map<?, ?>) result).get("currency1"));
		}
		return currency1;
	}

	public String getCurrency2() {
		if (currency2 == null) {
			currency2 = (String) (((Map<?, ?>) result).get("currency2"));
		}
		return currency2;
	}

	public Integer[] getNegotiatedCapabilities() {
		if (negotiatedCapabilities == null) {
			negotiatedCapabilities = (Integer[]) (((Map<?, ?>) result).get("negotiatedCapabilities"));
		}
		return negotiatedCapabilities;
	}

	public Integer[] getAvailableServerCapabilities() {
		if (availableServerCapabilities == null) {
			availableServerCapabilities = (Integer[]) (((Map<?, ?>) result).get("availableServerCapabilities"));
		}
		return availableServerCapabilities;
	}

	@SuppressWarnings("unchecked")
	public List<UsageCounterUsageThresholdInformation> getUsageCounterUsageThresholdInformation() {
		if (usageCounterUsageThresholdInformation == null) {
			Object[] usageCounterUsageThresholdInformationsRes = (Object[]) (((Map<?, ?>) result)
					.get("usageCounterUsageThresholdInformation"));
			usageCounterUsageThresholdInformation = new ArrayList<UsageCounterUsageThresholdInformation>();
			if(usageCounterUsageThresholdInformationsRes != null) {
				for (Object oi : usageCounterUsageThresholdInformationsRes) {
					usageCounterUsageThresholdInformation.add(new UsageCounterUsageThresholdInformation((Map<String, Object>) oi));
				}
			}
		}
		return usageCounterUsageThresholdInformation;
	}

}
