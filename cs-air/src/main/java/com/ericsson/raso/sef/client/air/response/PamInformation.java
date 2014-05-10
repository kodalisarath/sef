package com.ericsson.raso.sef.client.air.response;

import java.util.Map;

public class PamInformation extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public PamInformation(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private Integer pamServiceID;
	private Integer pamIndicator;

	public int getPamServiceID() {
		if (pamServiceID == null) {
			pamServiceID = getParam("pamServiceID", Integer.class);
		}
		return pamServiceID;
	}

	public void setPamServiceID(int pamServiceID) {
		this.pamServiceID = pamServiceID;
	}

	public int getPamIndicator() {
		if (pamIndicator == null) {
			pamIndicator = getParam("pamIndicator", Integer.class);
		}
		return pamIndicator;
	}

	public void setPamIndicator(int pamIndicator) {
		this.pamIndicator = pamIndicator;
	}
	/*
	 * public int getPamServiceID() { return pamServiceID; } public void
	 * setPamServiceID(int pamServiceID) { this.pamServiceID = pamServiceID; }
	 * public int getPamIndicator() { return pamIndicator; } public void
	 * setPamIndicator(int pamIndicator) { this.pamIndicator = pamIndicator; }
	 */

}
