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
import com.ericsson.raso.sef.cg.engine.ChargingSession;
import com.ericsson.raso.sef.cg.engine.IpcCluster;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.cg.engine.ResponseCodeUtil;
import com.ericsson.raso.sef.cg.engine.TransactionStatus;
import com.ericsson.raso.sef.charginggateway.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.SmException;

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

		//IpcCluster cluster = CgEngineContext.getIpcCluster();
		if (sessionId != null) {
			ChargingSession session = IpcCluster.getChargingSession(sessionId);
			if (session != null) {
				session.setTransactionStatus(TransactionStatus.FAILED);
				IpcCluster.updateChargingSession(sessionId, session);
			}
		}
		exchange.getOut().setBody(chargingInfo);
	}
}
