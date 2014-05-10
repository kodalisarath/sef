package com.ericsson.raso.sef.client.air.request;

public class ServiceOffering extends NativeAirRequest {

	private Integer serviceOfferingId;
	private boolean serviceOfferingActiveFlag;

	public Integer getServiceOfferingId() {
		return serviceOfferingId;
	}

	public void setServiceOfferingId(Integer serviceOfferingId) {
		this.serviceOfferingId = serviceOfferingId;
		addParam("serviceOfferingID", serviceOfferingId);
	}

	public boolean isServiceOfferingActiveFlag() {
		return serviceOfferingActiveFlag;
	}

	public void setServiceOfferingActiveFlag(boolean serviceOfferingActiveFlag) {
		this.serviceOfferingActiveFlag = serviceOfferingActiveFlag;
		addParam("serviceOfferingActiveFlag", Boolean.valueOf(serviceOfferingActiveFlag));
	}
}
