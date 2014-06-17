package com.ericsson.raso.sef.smppgateway;

import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.SmppSessionHandler;

public interface SmppGatewayCallback {
	
	SmppSessionHandler createSessionHandler(SmppServerSession session);

}
