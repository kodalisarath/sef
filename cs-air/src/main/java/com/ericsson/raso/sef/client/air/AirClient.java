package com.ericsson.raso.sef.client.air;

import com.ericsson.raso.sef.client.air.request.AbstractAirRequest;
import com.ericsson.raso.sef.client.air.response.AbstractAirResponse;
import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcException;

public interface AirClient {
	
	<R extends AbstractAirRequest,S extends AbstractAirResponse> void execute(R request, S response) throws XmlRpcException;
	
}
