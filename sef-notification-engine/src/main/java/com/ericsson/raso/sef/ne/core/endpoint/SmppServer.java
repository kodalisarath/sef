package com.ericsson.raso.sef.ne.core.endpoint;

import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.SmppSessionHandler;
import com.ericsson.raso.sef.ne.core.smpp.DefaultSessionHandler;
import com.ericsson.raso.sef.ne.core.smpp.SmppGateway;
import com.ericsson.raso.sef.ne.core.smpp.SmppGatewayCallback;

public class SmppServer {
	
	private String endpointId;
	private SmppGateway smppGateway;
	
	public SmppServer(String endpointId, SmppGateway smppGateway) {
		this.endpointId = endpointId;
		this.smppGateway = smppGateway;
	}
	
	public SmppServer( SmppGateway smppGateway) {
//		/this.endpointId = endpointId;
		this.smppGateway = smppGateway;
	}

	public void publish() {
		smppGateway.createSmppGateway(endpointId, new SmppGatewayCallback() {
			
			public SmppSessionHandler createSessionHandler(SmppServerSession session) {
				return new DefaultSessionHandler(session);
			}
		});
	}
}
