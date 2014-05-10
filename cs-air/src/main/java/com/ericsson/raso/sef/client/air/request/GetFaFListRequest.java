package com.ericsson.raso.sef.client.air.request;

import java.util.List;

public class GetFaFListRequest extends AbstractAirRequest {

	public GetFaFListRequest() {
		super("GetFaFList");
	}

	private List<Integer> negotiatedCapabilities;
	private Integer requestedOwner;

	public List<Integer> getNegotiatedCapabilities() {
		return negotiatedCapabilities;
	}

	public void setNegotiatedCapabilities(List<Integer> negotiatedCapabilities) {
		this.negotiatedCapabilities = negotiatedCapabilities;
		addParam("negotiatedCapabilities", negotiatedCapabilities.toArray());
	}

	public Integer getRequestedOwner() {
		return requestedOwner;
	}

	public void setRequestedOwner(Integer requestedOwner) {
		this.requestedOwner = requestedOwner;
		addParam("requestedOwner", requestedOwner);
	}

}
