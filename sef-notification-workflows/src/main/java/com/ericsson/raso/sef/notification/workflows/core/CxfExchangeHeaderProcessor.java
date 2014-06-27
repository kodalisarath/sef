package com.ericsson.raso.sef.notification.workflows.core;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.MessagePartInfo;

public class CxfExchangeHeaderProcessor implements Processor {
	
	public static final ThreadLocal<String> providerIdHolder = new ThreadLocal<String>();

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setHeader("stopwatch", System.currentTimeMillis());
		BindingOperationInfo info = (BindingOperationInfo) exchange.getProperty(BindingOperationInfo.class.getName());
		List<MessagePartInfo> paramters = info.getInput().getMessageParts();
		MessageContentsList values = (MessageContentsList) exchange.getIn().getBody();
		for (int i = 0; i < paramters.size(); i++) {
			String key = paramters.get(i).getName().getLocalPart();
			if(key.equalsIgnoreCase("requestId")) {
				ExchangeUtil.setValue(exchange, "requestId", values.get(i));
			}
			ExchangeUtil.setValue(exchange,paramters.get(i).getName().getLocalPart(), values.get(i));
		}
		
		if(providerIdHolder.get() != null) {
			ExchangeUtil.setValue(exchange, "providerId", providerIdHolder.get());
		}
	}
}
