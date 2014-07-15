package com.ericsson.raso.sef.cg.engine.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.pps.diameter.dccapi.avp.SubscriptionIdAvp;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.raso.sef.cg.engine.CgConstants;
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.Operation;
import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.RequestContext;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.nsn.avp.MethodNameAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.PPIInformationAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.ServiceInfoAvp;

public class RequestEntryProcessor implements Processor {
	
	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setHeader("stopwatch", System.currentTimeMillis());
		ChargingInfo chargingInfo = (ChargingInfo) exchange.getIn().getBody();
		
		RequestContext requestContext = RequestContextLocalStore.get();
		requestContext.getInProcess().put("sessionId", chargingInfo.getSessionId());

		ChargingRequest request = new ChargingRequest();
		request.setMessageId(chargingInfo.getUniqueMessageId());

		Ccr sourceCcr = CgEngineContext.getChargingApi().createDummyCcr();
		sourceCcr.getDiameterMessage().setAvps(chargingInfo.getAvpList());
		request.setSourceCcr(sourceCcr);
		setSessionId(sourceCcr, exchange);
		
		String serviceContextID = sourceCcr.getServiceContextId();

		Operation operation = null;

		if (serviceContextID.contains("32270@3gpp.org")) {
			operation = Operation.MMS_CHARGING;
		} else if (serviceContextID.contains("32274@3gpp.org")) {
			if(sourceCcr.getCCRequestType().intValue() == 1) {
				operation = Operation.SMS_CHARGING_INITIAL;
			} else if(sourceCcr.getCCRequestType().intValue() == 3) {
				operation = Operation.SMS_CHARGING_TERMMINATE;
			}
		} else {
			ServiceInfoAvp serviceInfoAvp = new ServiceInfoAvp(sourceCcr.getAvp(ServiceInfoAvp.AVP_CODE));
			PPIInformationAvp ppi = serviceInfoAvp.getPpiInformationAvp();
			MethodNameAvp methodNameAvp = ppi.getMethodNameAvp();
			operation = Operation.toOperation(String.valueOf(methodNameAvp.getAsInt()));
		}

		request.setOperation(operation);

		exchange.getIn().setHeader(CgConstants.operationName, operation.name());
		exchange.getIn().setHeader(CgConstants.messageId, request.getMessageId());
		if (sourceCcr.getOriginHost()!= null) {
			exchange.getIn().setHeader("providerId", sourceCcr.getOriginHost());
		}
		if (sourceCcr.getSubscriptionIdArray() != null) {
			SubscriptionIdAvp subscriptionIdAvp = new SubscriptionIdAvp(sourceCcr.getSubscriptionIdArray()[0]);
			String msisdn = subscriptionIdAvp.getSubscriptionIdData();
			request.setMsisdn(msisdn);
		}
		exchange.getIn().setBody(request);
	}

	private void setSessionId(Ccr ccr, Exchange exchange) {
		String sessionId;
		try {
			sessionId = ccr.getSessionId();
			if(sessionId != null) {
				exchange.getIn().setHeader(CgConstants.sessionId, sessionId);
			}
		} catch (Exception e) {
		}
	}
}
