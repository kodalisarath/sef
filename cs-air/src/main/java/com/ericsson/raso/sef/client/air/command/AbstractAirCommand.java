package com.ericsson.raso.sef.client.air.command;

import com.ericsson.raso.sef.core.Command;

public abstract class AbstractAirCommand<T> implements Command<T> {
	
	protected String endpointId;

	public void setEndpointId(String endpointId) {
		this.endpointId = endpointId;
	}
}
