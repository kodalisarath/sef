package com.ericsson.raso.sef.client.air.response;

import java.util.Map;

public class DedicatedAccountChangeInformation extends NativeAirResponse {

	public DedicatedAccountChangeInformation(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private static final long serialVersionUID = 1L;
	
	private Integer dedicatedAccountID;
	private String dedicatedAccountValue1;
	private Integer dedicatedAccountUnitType;

	public Integer getDedicatedAccountID() {
		if(dedicatedAccountID == null) {
			dedicatedAccountID = getParam("dedicatedAccountID", Integer.class);
		}
		return dedicatedAccountID;
	}

	public String getDedicatedAccountValue1() {
		if(dedicatedAccountValue1 == null) {
			dedicatedAccountValue1 = getParam("dedicatedAccountValue1", String.class);
		}
		return dedicatedAccountValue1;
	}

	public Integer getDedicatedAccountUnitType() {
		if(dedicatedAccountUnitType == null) {
			dedicatedAccountUnitType = getParam("dedicatedAccountID", Integer.class);
		}
		return dedicatedAccountUnitType;
	}
}
