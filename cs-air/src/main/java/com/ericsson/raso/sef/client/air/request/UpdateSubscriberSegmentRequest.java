package com.ericsson.raso.sef.client.air.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateSubscriberSegmentRequest extends AbstractAirRequest {
	
	public UpdateSubscriberSegmentRequest() {
		super("UpdateSubscriberSegmentation");
	}

	private List<ServiceOffering> serviceOfferings;

	public List<ServiceOffering> getServiceOfferings() {
		return serviceOfferings;
	}

	public void setServiceOfferings(List<ServiceOffering> serviceOfferings) {
		this.serviceOfferings = serviceOfferings;
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (ServiceOffering serviceOffering : serviceOfferings) {
			list.add(serviceOffering.toNative());
		}
		addParam("serviceOfferings", list);
	}
}
