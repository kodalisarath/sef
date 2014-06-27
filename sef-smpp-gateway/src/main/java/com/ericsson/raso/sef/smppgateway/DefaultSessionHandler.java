package com.ericsson.raso.sef.smppgateway;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.ericsson.raso.sef.core.smpp.SmppMessage;

public class DefaultSessionHandler extends DefaultSmppSessionHandler {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings("unused")
	private SmppServerSession session;

	public DefaultSessionHandler(SmppServerSession session) {
		this.session = session;
	}

	@Override
	public PduResponse firePduRequestReceived(@SuppressWarnings("rawtypes") PduRequest pduRequest) {
		log.info("Inside firePduRequestReceived pduRequest is "+pduRequest);
		if (pduRequest instanceof SubmitSm) {
			log.info(pduRequest.getName());
			SubmitSm sm = (SubmitSm) pduRequest;
			CamelContext camelContext = GatewayContext.getCamelContext();
			ProducerTemplate template = camelContext.createProducerTemplate();
			SmppMessage message = new SmppMessage();
			message.setDestinationMsisdn(sm.getDestAddress().getAddress());
			message.setMessageBody(new String(sm.getShortMessage()));
			template.sendBody("activemq:queue:notification", message);
		} 
		return pduRequest.createResponse();
	}
}
