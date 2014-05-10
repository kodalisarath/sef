package com.ericsson.raso.sef.client.air.response;

import java.util.Date;
import java.util.Map;

public class SubDedicatedAccount extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public SubDedicatedAccount(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private Date startDate = new Date();
	private Date expiryDate = new Date();
	private String refillAmount1;
	private String refillAmount2;
	private String clearedValue1;
	private String clearedValue2;

	public Date getStartDate() {
		if (startDate == null) {
			startDate = getParam("startDate", Date.class);
		}
		return startDate;
	}

	public Date getExpiryDate() {
		if (expiryDate == null) {
			expiryDate = getParam("expiryDate", Date.class);
		}
		return expiryDate;
	}

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

	public String getClearedValue1() {
		if (clearedValue1 == null) {
			clearedValue1 = getParam("clearedValue1", String.class);
		}
		return clearedValue1;
	}

	public String getClearedValue2() {
		if (clearedValue2 == null) {
			clearedValue2 = getParam("clearedValue2", String.class);
		}
		return clearedValue2;
	}

}
