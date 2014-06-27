package com.ericsson.raso.sef.smppgateway.test;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.smpp.SmppMessage;
import com.ericsson.raso.sef.smppgateway.GatewayContext;

public class TestSMSC {
	
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Handler
	public void process(Exchange exchange) throws SmException {

		CamelContext camelContext = GatewayContext.getCamelContext();
		ProducerTemplate template = camelContext.createProducerTemplate();
		
		String messageTxt = exchange.getIn().getBody(String.class);
		SmppMessage message = new SmppMessage();
		message.setDestinationMsisdn("9275615248");
		
		SmppMessage smppMessage = new SmppMessage();
		smppMessage.setDestinationMsisdn("9275615248");
		smppMessage.setMessageBody("1001,SC:1001");
		message.setMessageBody(messageTxt+":from SMPP server");
		
		template.sendBody("activemq:queue:notification", message);
		
		log.debug("sent message:"+messageTxt+":from SMPP server");;
		
	}
}
