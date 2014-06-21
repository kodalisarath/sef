package com.ericsson.raso.sef.cg.engine.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.cg.engine.CgConstants;
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ChargingSession;
import com.ericsson.raso.sef.cg.engine.IpcCluster;
import com.ericsson.raso.sef.cg.engine.Operation.Type;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;

public class TransactionIdentityProcessor implements Processor {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		ChargingRequest request = (ChargingRequest) exchange.getIn().getBody();
		String sessionId = request.getSourceCcr().getSessionId();
		request.setSessionId(sessionId);

		exchange.getIn().setHeader(CgConstants.sessionId, sessionId);

		

		if (request.getOperation().getType() == Type.TRANSACATION_START
				|| request.getOperation().getType() == Type.NO_TRANSACTION) {
			IpcCluster.initiateIpcRequest(sessionId, request.getOperation(), request.getMsisdn());
		} else if (request.getOperation().getType() == Type.INTERMEDIATE_TRANSACTION
				|| request.getOperation().getType() == Type.TRANSACTION_END) {
			ChargingSession session = IpcCluster.getChargingSession(sessionId);
			if (session == null || session.getOperation().getType() != Type.TRANSACATION_START) {
				log.error("Invalid request with session ID: " + sessionId);
				throw new SmException(ResponseCode.DIAMETER_UNKNOWN_SESSION_ID);
			}
		}
	}
}
