package com.ericsson.raso.sef.client.air.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class UpdateFaFListResponse extends AbstractAirResponse {

	private static final long serialVersionUID = 1L;
	private String originTransactionID;
	private String originOperatorID;
	private Integer[] allowedOptions;
	private Boolean fafMaxAllowedNumbersReachedFlag;
	private Date fafChangeUnbarDate;
	private ChargingResultInformation chargingResultInformation;
	private Integer[] negotiatedCapabilities;
	private Integer[] availableServerCapabilities;

	public String getOriginTransactionID() {
		if (originTransactionID == null) {
			originTransactionID = (String) (((Map<?, ?>) result)
					.get("originTransactionID"));
		}
		return originTransactionID;
	}

	public String getOriginOperatorID() {
		if (originOperatorID == null) {
			originOperatorID = (String) (((Map<?, ?>) result)
					.get("originOperatorID"));
		}
		return originOperatorID;
	}

	public Integer[] getAllowedOptions() {
		if (allowedOptions == null) {
			Object[] response = (Object[]) ((Map<?, ?>) result)
					.get("allowedOptions");
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (Object obj : response) {
				Integer val = (Integer) obj;
				list.add(val);
			}
			allowedOptions = list.toArray(new Integer[] {});
		}
		return allowedOptions;
	}

	public Boolean getFafMaxAllowedNumbersReachedFlag() {
		if (fafMaxAllowedNumbersReachedFlag) {
			fafMaxAllowedNumbersReachedFlag = (Boolean) (((Map<?, ?>) result)
					.get("fafMaxAllowedNumbersReachedFlag"));
		}
		return fafMaxAllowedNumbersReachedFlag;
	}

	public Date getFafChangeUnbarDate() {
		if (fafChangeUnbarDate == null) {
			fafChangeUnbarDate = (Date) (((Map<?, ?>) result)
					.get("fafChangeUnbarDate"));
		}

		return fafChangeUnbarDate;
	}

	@SuppressWarnings("unchecked")
	public ChargingResultInformation getChargingResultInformation() {

		if (chargingResultInformation == null) {
			Object chargingResultInformations = (Object) (((Map<?, ?>) result)
					.get("chargingResultInformation"));
			chargingResultInformation = new ChargingResultInformation(
					(Map<String, Object>) chargingResultInformations);
		}
		return chargingResultInformation;

	}

	public Integer[] getNegotiatedCapabilities() {
		if (negotiatedCapabilities == null) {
			Object[] response = (Object[]) ((Map<?, ?>) result)
					.get("negotiatedCapabilities");
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (Object obj : response) {
				Integer val = (Integer) obj;
				list.add(val);
			}
			negotiatedCapabilities = list.toArray(new Integer[] {});
		}
		return negotiatedCapabilities;
	}

	public Integer[] getAvailableServerCapabilities() {
		if (availableServerCapabilities == null) {
			Object[] response = (Object[]) ((Map<?, ?>) result)
					.get("availableServerCapabilities");
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (Object obj : response) {
				Integer val = (Integer) obj;
				list.add(val);
			}
			availableServerCapabilities = list.toArray(new Integer[] {});
		}

		return availableServerCapabilities;
	}

}
