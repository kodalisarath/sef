package com.ericsson.raso.sef.ne.core.smpp;

public interface SmppClient {
	
	void sendMessage(String msisdn, String message, String senderAddr) throws Exception;

}
