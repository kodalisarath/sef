package com.ericsson.raso.sef.client.air.response;

import java.util.Date;
import java.util.Map;

public class SubDedicatedInfo extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public SubDedicatedInfo(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private String dedicatedAccountValue1;
	private String dedicatedAccountValue2;
	private Date startDate = new Date();
	private Date expiryDate = new Date();

	public String getDedicatedAccountValue1() {
		if (dedicatedAccountValue1 == null) {
			dedicatedAccountValue1 = getParam("dedicatedAccountValue1", String.class);
		}
		return dedicatedAccountValue1;
	}

	public String getDedicatedAccountValue2() {
		if (dedicatedAccountValue2 == null) {
			dedicatedAccountValue2 = getParam("dedicatedAccountValue2", String.class);
		}
		return dedicatedAccountValue2;
	}

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

}
