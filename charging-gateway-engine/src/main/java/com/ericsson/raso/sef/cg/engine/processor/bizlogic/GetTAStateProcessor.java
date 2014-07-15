package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

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
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.SmartChargingSession;
import com.ericsson.raso.sef.cg.engine.TransactionStatus;
import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.nsn.avp.PPIInformationAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.ServiceInfoAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.TransactionStatusAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.TransparentDataAvp;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;
import com.google.gson.Gson;

public class GetTAStateProcessor implements Processor {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		log.debug(String.format("Enter GetTAStateProcessor.process. Exchange is %s", exchange));
		ChargingRequest request = (ChargingRequest) exchange.getIn().getBody();	

		ChargingInfo response = new ChargingInfo();

		log.info("Message ID: ");
		
		List<Avp> answerAvp = new ArrayList<Avp>();
		response.setAvpList(answerAvp);

		
		response.setUniqueMessageId(request.getMessageId());
		response.setSessionId(request.getSessionId());

		ChargingSession session = SefCoreServiceResolver.getChargingSessionService().get(response.getSessionId());
		SmartChargingSession smartSession = null;
		if (session == null) {
			log.error("Invalid request with session ID: " + request.getSessionId());
			throw new SmException(ResponseCode.DIAMETER_UNKNOWN_SESSION_ID);

		}
		
		smartSession = this.getSmartSession(session.getSessionInfo());
		if (smartSession == null) {
			log.error("Invalid request with session ID: " + request.getSessionId());
			throw new SmException(ResponseCode.DIAMETER_UNKNOWN_SESSION_ID);
		}
		

		long resultCode = ResponseCode.DIAMETER_SUCCESS.getCode();
		if (smartSession == null) {
			resultCode = ResponseCode.DIAMETER_UNKNOWN_SESSION_ID.getCode();
		}
		
		if(session != null) {
			//long messageTimeout = CgEngineContext.getChargingApi().getDiameterConfig().getMessageTimeout();
			long messageTimeout = Long.parseLong(CgEngineContext.getConfig().getValue("scapClient", Constants.MESSAGETIMEOUT));
			long sessionPeriod = System.currentTimeMillis() - smartSession.getCreationTime();
			if (sessionPeriod >= messageTimeout) {
				smartSession.setTransactionStatus(TransactionStatus.TIMEDOUT);
			}
		}

		TransactionStatusAvp transactionStatusAvp = null;
		switch (smartSession.getTransactionStatus()) {
		case AUTHORIZED:
			transactionStatusAvp = new TransactionStatusAvp(TransactionStatusAvp.AUTHORIZED);
			break;
		case FAILED:
			transactionStatusAvp = new TransactionStatusAvp(TransactionStatusAvp.FAILED);
			break;
		case PROCESSED:
			transactionStatusAvp = new TransactionStatusAvp(TransactionStatusAvp.PROCESSED);
			break;
		case TIMEDOUT:
			transactionStatusAvp = new TransactionStatusAvp(TransactionStatusAvp.TIMEOUT);
			break;
		default:
			transactionStatusAvp = new TransactionStatusAvp(TransactionStatusAvp.FAILED);
			break;
		}

		ResultCodeAvp resultCodeAvp = new ResultCodeAvp(resultCode);
		response.setResultCodeAvp(resultCodeAvp);
		answerAvp.add(resultCodeAvp);

		long experimentalResultCode = 1;
		if(smartSession.getTransactionStatus() == TransactionStatus.FAILED) {
			experimentalResultCode = 3;
		}
		ExperimentalResultAvp experimentalResultAvp = new ExperimentalResultAvp();
		experimentalResultAvp.addSubAvp(new VendorIdAvp(28458));
		experimentalResultAvp.addSubAvp(new ExperimentalResultCodeAvp(experimentalResultCode));
		answerAvp.add(experimentalResultAvp);
		

		PPIInformationAvp ppi = new PPIInformationAvp();
		TransparentDataAvp transparentDataAvp = new TransparentDataAvp();
		ppi.addSubAvp(transparentDataAvp);

		ppi.addSubAvp(transactionStatusAvp);
		
		ServiceInfoAvp serviceInfoAvp = new ServiceInfoAvp();
		serviceInfoAvp.addPPiInformationAvp(ppi);
		answerAvp.add(serviceInfoAvp);

		exchange.getOut().setBody(response);
		log.debug("End GetTAStateProcessor.process");
	
	}
	
	
	private SmartChargingSession getSmartSession(String sessionInfo) {
		Gson gson = new Gson();
		return gson.fromJson(sessionInfo, SmartChargingSession.class);		
	}

}