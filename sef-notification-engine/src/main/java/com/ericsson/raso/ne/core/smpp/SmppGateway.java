package com.ericsson.raso.ne.core.smpp;

public interface SmppGateway {
	
	void createSmppGateway(String endpointId, SmppGatewayCallback callback);

}
