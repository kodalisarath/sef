package com.ericsson.raso.sef.client.air.response;

import java.util.Map;

public class ServiceOffering extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public ServiceOffering(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private Integer serviceOfferingID;
	private Boolean serviceOfferingActiveFlag;

	public Integer getServiceOfferingID() {
		if (serviceOfferingID == null) {
			serviceOfferingID = getParam("serviceOfferingID", Integer.class);
		}

		return serviceOfferingID;
	}

	public Boolean isServiceOfferingActiveFlag() {
		if (serviceOfferingActiveFlag == null) {
			serviceOfferingActiveFlag = getParam("serviceOfferingActiveFlag", Boolean.class);
		}
		return serviceOfferingActiveFlag;
	}

}
