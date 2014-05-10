package com.ericsson.raso.sef.client.air.request;

public class PamInformation extends NativeAirRequest {

	private Integer pamServiceID;
	private Integer pamClassID;
	private Integer scheduleID;
	private Integer pamIndicator;

	public Integer getPamServiceID() {
		return pamServiceID;
	}

	public void setPamServiceID(Integer pamServiceID) {
		this.pamServiceID = pamServiceID;
		addParam("pamServiceID", pamServiceID);
	}

	public Integer getPamClassID() {
		return pamClassID;
	}

	public void setPamClassID(Integer pamClassID) {
		this.pamClassID = pamClassID;
		addParam("pamClassID", pamClassID);
	}

	public Integer getScheduleID() {
		return scheduleID;
	}

	public void setScheduleID(Integer scheduleID) {
		this.scheduleID = scheduleID;
		addParam("scheduleID", scheduleID);
	}

	public Integer getPamIndicator() {
		return pamIndicator;
	}

	public void setPamIndicator(Integer pamIndicator) {
		this.pamIndicator = pamIndicator;
		addParam("pamIndicator", pamIndicator);
	}
}
