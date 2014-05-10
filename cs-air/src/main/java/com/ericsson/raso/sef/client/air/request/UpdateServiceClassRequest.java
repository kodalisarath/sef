package com.ericsson.raso.sef.client.air.request;

public class UpdateServiceClassRequest extends AbstractAirRequest {

	public UpdateServiceClassRequest() {
		super("UpdateServiceClass");
	}

	private String serviceClassAction;
	private int serviceClassNew;
	private int serviceClassCurrent;

	public String getServiceClassAction() {
		return serviceClassAction;
	}

	public void setServiceClassAction(String serviceClassAction) {
		this.serviceClassAction = serviceClassAction;
		this.addParam("serviceClassAction", serviceClassAction);
	}

	public int getServiceClassNew() {
		return serviceClassNew;
	}

	public void setServiceClassNew(int serviceClassNew) {
		this.serviceClassNew = serviceClassNew;
		this.addParam("serviceClassNew", serviceClassNew);
	}

	public int getServiceClassCurrent() {
		return serviceClassCurrent;
	}

	public void setServiceClassCurrent(int serviceClassCurrent) {
		this.serviceClassCurrent = serviceClassCurrent;
		this.addParam("serviceClassCurrent", serviceClassCurrent);
	}
}
