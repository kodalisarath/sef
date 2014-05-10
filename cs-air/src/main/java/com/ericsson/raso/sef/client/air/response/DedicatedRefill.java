package com.ericsson.raso.sef.client.air.response;

import java.util.List;
import java.util.Map;

public class DedicatedRefill extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public DedicatedRefill(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private Integer dedicatedAccountID;
	private String refillAmount1;
	private String refillAmount2;
	private Integer expiryDateExtended;
	private String clearedValue1;
	private String clearedValue2;
	private Integer offerId;
	private List<SubDedicatedAccount> subDedicatedAccountRefInfo;
	private Integer dedicatedAccountUnitType;

	public Integer getDedicatedAccountID() {
		if (dedicatedAccountID == null) {
			dedicatedAccountID = getParam("dedicatedAccountID", Integer.class);
		}
		return dedicatedAccountID;
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

	public Integer getExpiryDateExtended() {
		if (expiryDateExtended == null) {
			expiryDateExtended = getParam("expiryDateExtended", Integer.class);
		}
		return expiryDateExtended;
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

	public Integer getOfferId() {
		if (offerId == null) {
			offerId = getParam("offerId", Integer.class);
		}
		return offerId;
	}

	public List<SubDedicatedAccount> getSubDedicatedAccountRefInfo() {
		return subDedicatedAccountRefInfo;
	}

	public Integer getDedicatedAccountUnitType() {
		if (dedicatedAccountUnitType == null) {
			dedicatedAccountUnitType = getParam("dedicatedAccountUnitType", Integer.class);
		}
		return dedicatedAccountUnitType;
	}

}
