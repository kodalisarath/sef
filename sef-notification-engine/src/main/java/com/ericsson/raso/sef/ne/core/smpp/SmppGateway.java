package com.ericsson.raso.sef.ne.core.smpp;

public interface SmppGateway {
	
	void createSmppGateway(String endpointId, SmppGatewayCallback callback);

}
