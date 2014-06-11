package com.ericsson.raso.ne.core.smpp;

public interface SmppClientFactory {
	
	SmppClient create(String endpointId);
	
}
