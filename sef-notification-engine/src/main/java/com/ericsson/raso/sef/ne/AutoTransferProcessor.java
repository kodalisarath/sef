package com.ericsson.raso.sef.ne;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.util.StringUtils;

import com.ericsson.raso.sef.core.smpp.SmppMessage;

public class AutoTransferProcessor implements Processor {

	@SuppressWarnings("unchecked")
	public void process(Exchange exchange) throws Exception {
		String csvheader = (String) exchange.getIn().getHeader("csvHeader");
		String[] header = StringUtils.commaDelimitedListToStringArray(csvheader);
		
		List<List<String>> data = (List<List<String>>) exchange.getIn().getBody();
		for (List<String> record : data) {
			SmppMessage message = new SmppMessage();
			message.setDestinationMsisdn(record.get(1));
			
			StringBuilder builder = new StringBuilder("alkansya-activation,");
			for (int i = 0; i<header.length; i++) {
				if(!record.get(i).isEmpty()) {
					builder.append(header[i] + ":" + record.get(i) + ",");
				}
			}
			message.setMessageBody(builder.toString());
			ProducerTemplate template = exchange.getContext().createProducerTemplate();
			template.sendBody("activemq:queue:notification", message);
		}
	}
	
}
