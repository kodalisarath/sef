package com.ericsson.raso.sef.client.air.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetOffersResponse extends AbstractAirResponse {

	private static final long serialVersionUID = 1L;

	private OfferInformation[] offerInformation;

	@SuppressWarnings("unchecked")
	public OfferInformation[] getOfferInformation() {
		if (offerInformation == null) {
			Object[] offerInformationRes = (Object[]) (((Map<?, ?>) result).get("offerInformation"));
			List<OfferInformation> oiList = new ArrayList<OfferInformation>();
			for (Object oi : offerInformationRes) {
				oiList.add(new OfferInformation((Map<String, Object>) oi));
			}

			offerInformation = new OfferInformation[oiList.size()];
			offerInformation = oiList.toArray(offerInformation);
		}
		return offerInformation;
	}
}
