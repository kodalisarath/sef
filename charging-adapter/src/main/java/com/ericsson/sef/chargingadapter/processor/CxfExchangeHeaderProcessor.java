package com.ericsson.sef.chargingadapter.processor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.csapi.schema.parlayx.common.v2_1.ChargingInformation;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CxfExchangeHeaderProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(CxfExchangeHeaderProcessor.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("---------CxfExchangeHeaderProcessor-------");
    	logger.debug("Request Start time: "+System.currentTimeMillis());
        Map<String, Object> inHeaders = exchange.getIn().getHeaders();
        BindingOperationInfo info = (BindingOperationInfo) exchange.getProperty(BindingOperationInfo.class.getName());
		List<MessagePartInfo> paramters = info.getInput().getMessageParts();
        Object[] values = exchange.getIn().getBody(Object[].class);
        for (int i = 0; i < paramters.size(); i++) {
            inHeaders.put(paramters.get(i).getName().getLocalPart(), values[i]);
        }
        String endUserIdentifier = (String) exchange.getIn().getHeader("endUserIdentifier");
		logger.debug("endUserIdentifier: "+endUserIdentifier);
	}

}
