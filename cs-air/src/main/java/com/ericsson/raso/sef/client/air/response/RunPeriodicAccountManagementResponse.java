package com.ericsson.raso.sef.client.air.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ericsson.raso.sef.client.air.response.PamInformation;

public class RunPeriodicAccountManagementResponse extends AbstractAirResponse {

	private static final long serialVersionUID = 1L;

	private PamInformation[] pamInformation;

	@SuppressWarnings("unchecked")
	public PamInformation[] getPamInformation() {
		if (pamInformation == null) {
			Object[] pamInformationResponse = (Object[]) (((Map<?, ?>) result).get("pamInformation"));
			List<PamInformation> pamInformationList = new ArrayList<PamInformation>();
			for (Object pamInformation : pamInformationResponse) {
				pamInformationList.add(new PamInformation((Map<String, Object>) pamInformation));
			}
			pamInformation = new PamInformation[pamInformationList.size()];
			pamInformation = pamInformationList.toArray(pamInformation);
		}
		return pamInformation;
	}
}
