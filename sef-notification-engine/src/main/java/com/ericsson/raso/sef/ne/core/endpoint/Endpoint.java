package com.ericsson.raso.sef.ne.core.endpoint;

public abstract class Endpoint {

	private String componentName;
	private String endpointId;
	private Protocol protocol;
	private ProtocolType protocolType;

	private StackParams stackParams;

	public String getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(String endpointId) {
		this.endpointId = endpointId;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public ProtocolType getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(ProtocolType protocolType) {
		this.protocolType = protocolType;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public StackParams getStackParams() {
		return stackParams;
	}

	public void setStackParams(StackParams stackParams) {
		this.stackParams = stackParams;
	}

	public String getValue(String key) {
		if (stackParams != null && stackParams.getProperties() != null) {
			for (Property property : stackParams.getProperties()) {
				if (property.getName().equals(key)) {
					return property.getValue();
				}
			}
		}
		return null;
	}

}
