package com.ericsson.raso.sef.smppgateway;

public interface SmppGateway {
	
	void createSmppGateway(String endpointId, SmppGatewayCallback callback);

}
