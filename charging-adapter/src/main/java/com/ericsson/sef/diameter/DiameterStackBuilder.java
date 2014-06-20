package com.ericsson.sef.diameter;

import com.ericsson.pps.diameter.dccapi.DCCStack;
import com.ericsson.pps.diameter.rfcapi.base.ApplicationAlreadyInUseException;
import com.ericsson.pps.diameter.rfcapi.base.DiameterConfig;
import com.ericsson.pps.diameter.rfcapi.base.PeerConnectionListener;
import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.ApplicationId;
import com.ericsson.pps.diameter.rfcapi.base.message.ApplicationRequestListener;
import com.ericsson.pps.diameter.scapv2.SCAPStack;

public class DiameterStackBuilder {

	private DCCStack dccStack;

	public DiameterStackBuilder(Stack stack) {
		switch (stack) {
		case SCAPV2:
			//SCAPStack.loadAvpClasses();
			this.dccStack = new SCAPStack();
			break;
		default:
			//DCCStack.loadAvpClasses();
			this.dccStack = new DCCStack();
			break;
		}
		this.dccStack = new DCCStack();
	}

	public DCCStack build() {
		return dccStack;
	}

	public DiameterStackBuilder ownRealm(String realm) {
		dccStack.setOriginRealm(realm);
		return this;
	}

	public DiameterStackBuilder ownProductId(String productId) {
		dccStack.setOwnProductId(productId);
		return this;
	}
	
	public DiameterStackBuilder ownIpAddress(String ipAddress) {
		dccStack.getDiameterConfig().setValue(DiameterConfig.OWN_IP_ADDRESS, ipAddress);
		return this;
	}

	public DiameterStackBuilder fqdn(String fqdn) {
		dccStack.setOwnFqdn(fqdn);
		return this;
	}
	
	public DiameterStackBuilder tcpPort(int port) {
		dccStack.setOwnTcpPort(port);
		return this;
	}
	
	public DiameterStackBuilder addStaticRoute(String domain, String address) {
		try {
			dccStack.addStaticRoute(domain, -1, -1, address);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return this;
	}
	
	public DiameterStackBuilder messageTimeout(long timeout) {
		dccStack.getDiameterConfig().setValue(DiameterConfig.DEFAULT_MESSAGE_TIMEOUT, timeout);
		return this;
	}

	public DiameterStackBuilder peerConnectionListener(PeerConnectionListener listener) {
		dccStack.getDiameterStack().addPeerConnectionListener(listener);
		return this;
	}

	public DiameterStackBuilder application(ApplicationId applicationId) {
		dccStack.getDiameterConfig().addApplication(applicationId);
		return this;
	}

	public DiameterStackBuilder ownStackUri(String uri) {
		dccStack.setOwnDiameterUri(uri);
		return this;
	}

	public DiameterStackBuilder requestListener(ApplicationRequestListener listener, ApplicationId applicationId) {
		try {
			dccStack.getDiameterConfig().addRequestListener(listener, applicationId);
		} catch (ApplicationAlreadyInUseException e) {
			throw new IllegalArgumentException(e);
		}
		return this;
	}

	public static enum Stack {
		DCC, SCAPV2
	}
}
