package com.ericsson.raso.sef.cg.engine.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.ExperimentalResultAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.ExperimentalResultCodeAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.ResultCodeAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.VendorIdAvp;
import com.ericsson.raso.sef.cg.engine.CgConstants;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.ResponseCodeUtil;
import com.ericsson.raso.sef.cg.engine.SmartChargingSession;
import com.ericsson.raso.sef.cg.engine.TransactionStatus;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;
import com.google.gson.Gson;

public class CgExceptionHandler implements Processor {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void process(Exchange exchange) throws Exception {
		String messageId = (String)exchange.getIn().getHeader(CgConstants.messageId);
		String sessionId = (String)exchange.getIn().getHeader(CgConstants.sessionId);
		
		Throwable error = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
		log.error(error.getMessage(), error);
		ChargingInfo chargingInfo = new ChargingInfo();
		chargingInfo.setUniqueMessageId(messageId);

		List<Avp> errorAvps = new ArrayList<Avp>();
		long resultCode = ResponseCode.DIAMETER_UNABLE_TO_COMPLY.getCode();
		if (error instanceof SmException) {
			long errorCode = ((SmException) error).getStatusCode().getCode();
			resultCode = ResponseCodeUtil.getMappedResultCode(errorCode);
		}

		ResultCodeAvp resultCodeAvp = new ResultCodeAvp(resultCode);
		chargingInfo.setResultCodeAvp(resultCodeAvp);
		errorAvps.add(resultCodeAvp);

		ExperimentalResultAvp experimentalResultAvp = new ExperimentalResultAvp();
		experimentalResultAvp.addSubAvp(new VendorIdAvp(28458));
		Long experimentalResultCode = ResponseCodeUtil.getMappedExperimentalResultCode(resultCode, 0);
		if (experimentalResultCode != null) {
			experimentalResultAvp.addSubAvp(new ExperimentalResultCodeAvp(experimentalResultCode));
			errorAvps.add(experimentalResultAvp);
		}

		chargingInfo.setAvpList(errorAvps);

		if (sessionId != null) {
			ChargingSession session = SefCoreServiceResolver.getChargingSessionService().get(sessionId);
			if (session != null) {
				SmartChargingSession smartSession = this.getSmartSession(session.getSessionInfo());
				smartSession.setTransactionStatus(TransactionStatus.FAILED);
				session.setSessionInfo(this.getSessionInfo(smartSession));
				SefCoreServiceResolver.getChargingSessionService().put(session);
			}		
		}
		exchange.getOut().setBody(chargingInfo);
	}
		
		
		private SmartChargingSession getSmartSession(String sessionInfo) {
			Gson gson = new Gson();
			return gson.fromJson(sessionInfo, SmartChargingSession.class);		
		}
		
		private String getSessionInfo(SmartChargingSession session) {
			Gson gson = new Gson();
			return gson.toJson(session);
		}
}
