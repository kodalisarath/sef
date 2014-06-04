package com.ericsson.raso.sef.client.air.response;

import java.util.Map;

public class ChargingResultInformation extends NativeAirResponse {

	public ChargingResultInformation(Map<String, Object> paramMap) {
		super(paramMap);

	}

	private static final long serialVersionUID = 1L;
	private String cost1;
	private String currency1;
	private String cost2;
	private String currency2;
	private Integer chargingResultCode;
	private Integer reservationCorrelationID;
	private ChargingResultInformationService chargingResultInformationService;

	public String getCost1() {
		if (cost1 == null) {
			cost1 = getParam("cost1", String.class);
		}
		return cost1;
	}

	public String getCurrency1() {
		if (currency1 == null) {
			currency1 = getParam("currency1", String.class);
		}
		return currency1;
	}

	public String getCost2() {
		if (cost2 == null) {
			cost2 = getParam("cost2", String.class);
		}
		return cost2;
	}

	public String getCurrency2() {
		if (currency2 == null) {
			currency2 = getParam("currency2", String.class);
		}
		return currency2;
	}

	public Integer getChargingResultCode() {
		if (chargingResultCode == null) {
			chargingResultCode = getParam("chargingResultCode", Integer.class);
		}
		return chargingResultCode;
	}

	public Integer getReservationCorrelationID() {
		if (reservationCorrelationID == null) {
			reservationCorrelationID = getParam("reservationCorrelationID",
					Integer.class);
		}

		return reservationCorrelationID;
	}

	public ChargingResultInformationService getChargingResultInformationService() {
		if (chargingResultInformationService == null){
			chargingResultInformationService = getParam("chargingResultInformationService",
					ChargingResultInformationService.class );
		}
		return chargingResultInformationService;
		
	}

}
