package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.List;

import com.ericsson.raso.sef.client.air.request.PamInformation;

public class PamInformationList {
	
	private List<PamInformation> pamInfolist;

	public List<PamInformation> getPamInfolist() {
		return pamInfolist;
	}

	public void setPamInfolist(List<PamInformation> pamInfolist) {
		this.pamInfolist = pamInfolist;
	}
	
}
