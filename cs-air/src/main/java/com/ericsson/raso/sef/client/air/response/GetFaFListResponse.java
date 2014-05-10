package com.ericsson.raso.sef.client.air.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GetFaFListResponse extends AbstractAirResponse {

	private static final long serialVersionUID = 1L;

	private String originTransactionID;
	private List<FafInformationList> fafInformationList;
	private Date fafChangeUnbarDate;
	private Boolean fafMaxAllowedNumbersReachedFlag;
	private Boolean fafChargingNotAllowedFlag;
	private Integer[] negotiatedCapabilities;
	private Integer[] availableServerCapabilities;

	public String getOriginTransactionID() {
		if (originTransactionID == null) {
			originTransactionID = (String) (((Map<?, ?>) result)
					.get("originTransactionID"));
		}
		return originTransactionID;
	}

	@SuppressWarnings("unchecked")
	public List<FafInformationList> getFafInformationList() {
		if (fafInformationList == null) {
			Object[] fafInformationsRes = (Object[]) (((Map<?, ?>) result)
					.get("fafInformationList"));
			fafInformationList = new ArrayList<FafInformationList>();
			for (Object so : fafInformationsRes) {
				fafInformationList.add(new FafInformationList(
						(Map<String, Object>) so));
			}
		}

		return fafInformationList;
	}

	public Date getFafChangeUnbarDate() {
		if (fafChangeUnbarDate == null) {
			fafChangeUnbarDate = (Date) (((Map<?, ?>) result)
					.get("fafChangeUnbarDate"));
		}
		return fafChangeUnbarDate;
	}

	public Boolean isFafMaxAllowedNumbersReachedFlag() {
		if (fafMaxAllowedNumbersReachedFlag) {
			fafMaxAllowedNumbersReachedFlag = (Boolean) (((Map<?, ?>) result)
					.get("fafMaxAllowedNumbersReachedFlag"));
		}
		return fafMaxAllowedNumbersReachedFlag;
	}

	public Boolean isFafChargingNotAllowedFlag() {
		if (fafChargingNotAllowedFlag) {
			fafChargingNotAllowedFlag = (Boolean) (((Map<?, ?>) result)
					.get("fafChargingNotAllowedFlag"));
		}
		return fafChargingNotAllowedFlag;
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
