package com.ericsson.raso.sef.client.air.response;

import java.util.Map;

public class UsageAccumulatorInfo extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public UsageAccumulatorInfo(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private Integer accumulatorID;
	private Integer accumulatorValue;

	public Integer getAccumulatorID() {
		if (accumulatorID == null) {
			accumulatorID = getParam("accumulatorID", Integer.class);
		}
		return accumulatorID;
	}

	public Integer getAccumulatorValue() {
		if (accumulatorValue == null) {
			accumulatorValue = getParam("accumulatorValue", Integer.class);
		}
		return accumulatorValue;
	}

}
