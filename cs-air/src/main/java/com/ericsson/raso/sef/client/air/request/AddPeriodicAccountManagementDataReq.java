package com.ericsson.raso.sef.client.air.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddPeriodicAccountManagementDataReq extends AbstractAirRequest {

	public AddPeriodicAccountManagementDataReq() {
		super("AddPeriodicAccountManagementData");
	}
	
	private List<PamInformation> pamInformationList;

	public List<PamInformation> getPamInformationList() {
		return pamInformationList;
	}

	public void setPamInformationList(List<PamInformation> pamInformationList) {
		this.pamInformationList = pamInformationList;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (PamInformation pamInformation : pamInformationList) {
			list.add(pamInformation.toNative());
		}
		addParam("pamInformationList", list);
	}

}
