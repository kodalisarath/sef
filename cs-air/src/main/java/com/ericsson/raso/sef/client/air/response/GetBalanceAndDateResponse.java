package com.ericsson.raso.sef.client.air.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetBalanceAndDateResponse extends AbstractAirResponse {

	private static final long serialVersionUID = 1L;

	private List<DedicatedAccountInformation> dedicatedAccountInformation;
	private List<OfferInformation> offerInformationList;

	@SuppressWarnings("unchecked")
	public List<DedicatedAccountInformation> getDedicatedAccountInformation() {
		if (dedicatedAccountInformation == null) {
			dedicatedAccountInformation = new ArrayList<DedicatedAccountInformation>();
			Object[] DedicatedAccountInformationRes = (Object[]) (((Map<?, ?>) result)
					.get("dedicatedAccountInformation"));
			if(DedicatedAccountInformationRes!=null){
				for (Object oi : DedicatedAccountInformationRes) {
					dedicatedAccountInformation.add(new DedicatedAccountInformation((Map<String, Object>) oi));
				}
			}
		}
		return dedicatedAccountInformation;
	}

	@SuppressWarnings("unchecked")
	public List<OfferInformation> getOfferInformationList() {
		if(offerInformationList == null) {
			offerInformationList = new ArrayList<OfferInformation>();
			Object[] offerInfoList = (Object[]) (((Map<?, ?>) result).get("offerInformationList"));
			if(offerInfoList!=null){
				for (Object object : offerInfoList) {
					offerInformationList.add(new OfferInformation((Map<String, Object>) object));
				}
			}
		}
		return offerInformationList;

	}
}
