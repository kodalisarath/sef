package com.ericsson.raso.sef.client.air.request;

import java.util.List;

public class GetUsageThresholdsAndCountersRequest extends AbstractAirRequest{

	private List<Integer> negotiatedCapabilities;
	private String originOperatorID;
	

	public GetUsageThresholdsAndCountersRequest() {
		super("GetUsageThresholdsAndCounters");
	}
	
	public List<Integer> getNegotiatedCompatibilities() {
		return negotiatedCapabilities;
	}
	
	public void setNegotiatedCapabilities(List<Integer> negotiatedCapabilities) {
		this.negotiatedCapabilities = negotiatedCapabilities;
		addParam("negotiatedCapabilities", negotiatedCapabilities.toArray());
	}
	
	public String getOriginOperatorID() {
		return originOperatorID;
	}
	
	public void setOriginOperatorID(String originOperatorID) {
		this.originOperatorID = originOperatorID;
		addParam("originOperatorID", originOperatorID);
	}

}
