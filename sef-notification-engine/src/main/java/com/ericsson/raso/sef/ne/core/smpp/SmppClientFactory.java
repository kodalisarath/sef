package com.ericsson.raso.sef.ne.core.smpp;


public interface SmppClientFactory {
	
	SmppClient create(String endpointId);
	
}
