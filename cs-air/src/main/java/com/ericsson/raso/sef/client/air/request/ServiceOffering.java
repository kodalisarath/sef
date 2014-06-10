package com.ericsson.raso.sef.client.air.request;

import java.io.Serializable;

public class ServiceOffering extends NativeAirRequest implements Serializable {

	private static final long serialVersionUID = -1162307293609919468L;
	
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
