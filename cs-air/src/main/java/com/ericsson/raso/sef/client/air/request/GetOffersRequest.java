package com.ericsson.raso.sef.client.air.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetOffersRequest extends AbstractAirRequest {

	public GetOffersRequest() {
		super("GetOffers");
	}

	private OfferSelection[] offerSelection;
	private String offerRequestedTypeFlag;	
	
	public String getOfferRequestedTypeFlag() {
		return offerRequestedTypeFlag;
	}

	public void setOfferRequestedTypeFlag(String offerRequestedTypeFlag) {
		this.offerRequestedTypeFlag = offerRequestedTypeFlag;
		addParam("offerRequestedTypeFlag", offerRequestedTypeFlag);
	}

	public OfferSelection[] getOfferSelection() {
		return offerSelection;
	}

	public void setOfferSelection(OfferSelection[] offerSelection) {
		this.offerSelection = offerSelection;
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		for (int i = 0; i < offerSelection.length; i++) {
			list.add(offerSelection[i].toNative());	
		}
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object>[] map = new HashMap[]{};
		addParam("offerSelection", list.toArray(map));
	}
}
