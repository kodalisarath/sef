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
		log.debug("Receivng message from testNotificationQueue: "+messageTxt);
		String messages[] = messageTxt.split(";");
		
		if ( messages.length != 2){
			//93695571186;1001,SC:1001 //activateSubscriber workflow
			//93695571186;090109999000,SC:090109999000,AP:638000000151,BP:638000000152,circleID:K10 //alkansyaWorkflow?
			//93695571186;777788889999,SC:777788889999,PID:1 //alkansyaWorkflow 
			throw new IllegalArgumentException(String.format("Valid message format to test SMPP is <msisdn>;<messagebody>, 93695571186;1001,SC:1001", messageTxt));
		}
		SmppMessage message = new SmppMessage();
		message.setDestinationMsisdn(messages[0]);
		message.setMessageBody(messages[1]);
		
		template.sendBody("activemq:queue:notification", message);
		
	}
}
