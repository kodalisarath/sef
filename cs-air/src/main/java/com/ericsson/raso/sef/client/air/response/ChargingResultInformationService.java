package com.ericsson.raso.sef.client.air.response;

import java.util.Map;

public class ChargingResultInformationService extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public ChargingResultInformationService(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private String cost1;
	private String cost2;

	public String getCost1() {
		if (cost1 == null) {
			cost1 = getParam("cost1", String.class);
		}
		return cost1;
	}

	public String getCost2() {
		if (cost2 == null) {
			cost2 = getParam("cost2", String.class);
		}
		return cost2;
	}

	public void setCost1(String cost1) {
		this.cost1 = cost1;
	}

	public void setCost2(String cost2) {
		this.cost2 = cost2;
	}

	
}
