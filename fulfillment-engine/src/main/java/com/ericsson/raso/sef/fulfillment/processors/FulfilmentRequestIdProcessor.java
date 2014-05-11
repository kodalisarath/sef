package com.ericsson.raso.sef.fulfillment.processors;

import java.util.List;

import org.apache.camel.Exchange;

import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.core.camelprocessors.RequestIdGenerator;
import com.ericsson.sef.bes.api.fulfillment.ActivationData;

public class FulfilmentRequestIdProcessor extends RequestIdGenerator {

	@Override
	public String fetchRequestIdFromRequest(Exchange arg0) {
		List<ActivationData> metas = arg0.getIn().getBody(List.class);
			
		for(ActivationData meta: metas) {
			if(meta.getKey().equals("requestId")) {
				return meta.getValue();
			}
		}
		return UniqueIdGenerator.generateId();
	}

}
