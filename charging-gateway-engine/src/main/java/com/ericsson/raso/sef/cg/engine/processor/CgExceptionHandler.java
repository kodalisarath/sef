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
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.ResponseCodeUtil;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.model.Operation;
import com.ericsson.raso.sef.core.cg.model.TransactionStatus;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;

public class CgExceptionHandler implements Processor {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		String messageId = (String) exchange.getIn().getHeader(
				CgConstants.messageId);
		String operationName = ((String) exchange.getIn().getHeader(
				CgConstants.operationName));
		String sessionId = null;
		ChargingRequest request = null;
		Object obj = exchange.getIn().getBody();
		ChargingSession session = null;

		if (log.isDebugEnabled())
			log.debug("Inside CgExceptionHandler operationName is "
					+ operationName);
		if (Operation.DIRECT_DEBIT.name().equals(operationName)
				|| Operation.CANCEL.name().equals(operationName)
				|| Operation.COMMIT.name().equals(operationName)
				|| Operation.TA_STATE.name().equals(operationName)) {
			if (obj instanceof ChargingRequest) {
				request = (ChargingRequest) exchange.getIn().getBody();
				if (log.isDebugEnabled())
					log.debug("Inside CgExceptionHandler object instance of ChargingRequest and the object is "
							+ request);
				if (request != null) {
					session = request.getChargingSession();
				}
			} else {
				if (log.isDebugEnabled())
					log.debug("Inside CgExceptionHandler object instance of ChargingInfo and the object is "
							+ (ChargingInfo) obj);
				sessionId = (String) exchange.getIn().getHeader(
						CgConstants.sessionId);
				if (log.isDebugEnabled())
					log.debug("Inside CgExceptionHandler object sessionId is "
							+ sessionId);
				session = SefCoreServiceResolver.getChargingSessionService()
						.read(sessionId);

			}
		}

		if (log.isDebugEnabled())
			log.debug("Inside CgExceptionHandler ChargingSession object is "
					+ session);

		/*
		 * if (session == null) { sessionId = (String)
		 * exchange.getIn().getHeader( CgConstants.sessionId); if (sessionId !=
		 * null) session = SefCoreServiceResolver
		 * .getChargingSessionService().read(sessionId); }
		 */
		if (log.isDebugEnabled())
			log.debug("Inside CgExceptionHandler session is " + request);
		Throwable error = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,
				Throwable.class);
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
		Long experimentalResultCode = ResponseCodeUtil
				.getMappedExperimentalResultCode(resultCode, 0);
		if (experimentalResultCode != null) {
			experimentalResultAvp.addSubAvp(new ExperimentalResultCodeAvp(
					experimentalResultCode));
			errorAvps.add(experimentalResultAvp);
		}

		chargingInfo.setAvpList(errorAvps);

		/*
		 * if (sessionId != null) { //ChargingSession session =
		 * SefCoreServiceResolver.getChargingSessionService().get(sessionId); if
		 * (session != null) { //SmartChargingSession smartSession =
		 * this.getSmartSession(session.getSessionInfo());
		 * //smartSession.setTransactionStatus(TransactionStatus.FAILED);
		 * //session.setSessionInfo(this.getSessionInfo(smartSession));
		 * SefCoreServiceResolver.getChargingSessionService().put(session);
		 * 
		 * } }
		 */

		if (session != null) {
			session.setTransactionStatus(TransactionStatus.FAILED);
			SefCoreServiceResolver.getChargingSessionService().update(session);
		}

		exchange.getOut().setBody(chargingInfo);
	}

	/*
	 * private SmartChargingSession getSmartSession(String sessionInfo) { Gson
	 * gson = new Gson(); return gson.fromJson(sessionInfo,
	 * SmartChargingSession.class); }
	 * 
	 * private String getSessionInfo(SmartChargingSession session) { Gson gson =
	 * new Gson(); return gson.toJson(session); }
	 */
}