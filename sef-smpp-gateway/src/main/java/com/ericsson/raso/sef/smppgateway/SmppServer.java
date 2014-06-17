package com.ericsson.raso.sef.smppgateway;

import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.SmppSessionHandler;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;

public class SmppServer {
	
	private String endpointId;
	private SmppGateway smppGateway;
	
	public SmppServer(String endpointId, SmppGateway smppGateway) {
		this.endpointId = endpointId;
		this.smppGateway = smppGateway;
	}
	
	public SmppServer(SmppGateway smppGateway) {
////this.endpointId = endpointId;
		IConfig config = SefCoreServiceResolver.getConfigService();
		this.endpointId = config.getValue("Smsc_Gateway_Props", "endpointId");
		this.smppGateway = smppGateway;
	}

	public void publish() {
		smppGateway.createSmppGateway(endpointId,new SmppGatewayCallback() {
			public SmppSessionHandler createSessionHandler(SmppServerSession session) {
				return new DefaultSessionHandler(session);
			}
		});
	}
}
