package com.ericsson.raso.sef.client.air.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateAccumulatorRequest extends AbstractAirRequest {

	public UpdateAccumulatorRequest() {
		super("UpdateAccumulators");
	}

	private List<AccumulatorInformation> accumulatorUpdateInformation;
	private List<Integer> negotiatedCapabilities;
	private String serviceClassCurrent;


	public List<AccumulatorInformation> getAccumulatorUpdateInformation() {
		return accumulatorUpdateInformation;
	}

	public void setAccumulatorUpdateInformation(List<AccumulatorInformation> accumulatorUpdateInformation) {
		this.accumulatorUpdateInformation = accumulatorUpdateInformation;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (AccumulatorInformation acc : accumulatorUpdateInformation) {
			list.add(acc.toNative());        
		}

		@SuppressWarnings("unchecked")
		HashMap<String, Object>[] map = new HashMap[]{};
		addParam("accumulatorUpdateInformation", list.toArray(map));
	}

	public String getServiceClassCurrent() {
		return serviceClassCurrent;
	}

	public void setServiceClassCurrent(String serviceClassCurrent) {
		this.serviceClassCurrent = serviceClassCurrent;
		addParam("serviceClassCurrent", this.serviceClassCurrent);
	}

	public void setNegotiatedCapabilities(List<Integer> negotiatedCapabilities) {
		this.negotiatedCapabilities = negotiatedCapabilities;
		addParam("negotiatedCapabilities", this.negotiatedCapabilities.toArray());
	}
}
