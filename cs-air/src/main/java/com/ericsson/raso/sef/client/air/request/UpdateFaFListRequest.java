package com.ericsson.raso.sef.client.air.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateFaFListRequest extends AbstractAirRequest {

	public UpdateFaFListRequest() {
		super("UpdateFaFList");
	}

	private String fafAction;
	private FafInformation fafInformation;
	private List<FafInformation> fafInformationList;
	private Integer selectedOption;
	private ChargingRequestInformation chargingRequestInformation;
	private Boolean enableFafMNPFlag;
	private List<Integer> negotiatedCapabilities;

	
	public void setFafAction(String fafAction) {
		this.fafAction = fafAction;
		addParam("fafAction", this.fafAction);
	}

	public void setFafInformation(FafInformation fafInformation) {
		this.fafInformation = fafInformation;
		addParam("fafInformation", this.fafInformation.toNative());
	}

	public void setFafInformationList(List<FafInformation> fafInformationList) {
		this.fafInformationList = fafInformationList;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (FafInformation fafInfo : this.fafInformationList) {
			list.add(fafInfo.toNative());
		}
		addParam("fafInformationList", list);
	}

	public void setSelectedOption(Integer selectedOption) {
		this.selectedOption = selectedOption;
		addParam("selectedOption", this.selectedOption);
	}

	public void setChargingRequestInformation(ChargingRequestInformation chargingRequestInformation) {
		this.chargingRequestInformation = chargingRequestInformation;
		addParam("chargingRequestInformation", this.chargingRequestInformation);
	}

	public void setEnableFafMNPFlag(Boolean enableFafMNPFlag) {
		this.enableFafMNPFlag = enableFafMNPFlag;
		addParam("enableFafMNPFlag", this.enableFafMNPFlag);
	}

	public void setNegotiatedCapabilities(List<Integer> negotiatedCapabilities) {
		this.negotiatedCapabilities = negotiatedCapabilities;
		addParam("negotiatedCapabilities", this.negotiatedCapabilities.toArray());
	}
}
