package com.ericsson.raso.sef.smppgateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppServerHandler;
import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.pdu.BaseBind;
import com.cloudhopper.smpp.pdu.BaseBindResp;
import com.cloudhopper.smpp.type.SmppProcessingException;

public class DefaultSmppServerHandler implements SmppServerHandler {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private String systemId;
	private String password;
	
	private SmppGatewayCallback callback;
	
	public DefaultSmppServerHandler(SmppGatewayCallback callback) {
		this.callback = callback;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public void sessionBindRequested(Long sessionId, SmppSessionConfiguration sessionConfiguration, @SuppressWarnings("rawtypes") BaseBind bindRequest)
			throws SmppProcessingException {
		if(!sessionConfiguration.getSystemId().equals(systemId) || !sessionConfiguration.getPassword().equals(password)) {
			throw new SmppProcessingException(SmppConstants.STATUS_BINDFAIL, "Invalid credentials.");
		}
		
		sessionConfiguration.setName("Application.SMPP." + sessionConfiguration.getSystemId());
		log.info("sessionBindRequested:: sessionID: " + sessionId + " bindRequest:: " + bindRequest);
	}

	public void sessionCreated(Long sessionId, SmppServerSession session, BaseBindResp preparedBindResponse)
			throws SmppProcessingException {
		log.info("sessionCreated:: sessionID: " + sessionId + " preparedBindResponse:: " + preparedBindResponse);
		session.serverReady(callback.createSessionHandler(session));
	}

	public void sessionDestroyed(Long sessionId, SmppServerSession session) {
		log.info("sessionDestroyed:: sessionID: " + sessionId);
        if (session.hasCounters()) {
           log.info(" final session rx-submitSM: {}", session.getCounters().getRxSubmitSM());
        }
        // make sure it's really shutdown
        session.destroy();
	}

}
