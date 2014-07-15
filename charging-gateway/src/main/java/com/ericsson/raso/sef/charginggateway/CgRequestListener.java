package com.ericsson.raso.sef.charginggateway;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.dccapi.avp.CCRequestNumberAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCRequestTypeAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AuthApplicationIdAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.SessionIdAvp;
import com.ericsson.pps.diameter.rfcapi.base.message.ApplicationRequestListener;
import com.ericsson.pps.diameter.rfcapi.base.message.DiameterAnswer;
import com.ericsson.pps.diameter.rfcapi.base.message.DiameterRequest;
import com.ericsson.raso.sef.core.PerformanceStatsLogger;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.diameter.DiameterErrorCode;

public class CgRequestListener implements ApplicationRequestListener {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static final long DEFAULT_TIMEOUT = 10000;

	private ProducerTemplate producerTemplate;

	@SuppressWarnings("unused")
	private long timeout = DEFAULT_TIMEOUT;

	public CgRequestListener(CamelContext camelContext) {
		log.debug("CgiRequestListener  ..Inside the constructor... About to createProducerTemplate");
		this.producerTemplate = camelContext.createProducerTemplate();
		log.debug("CgiRequestListener  ..producerTemplate is created");
	}

	private Map<String, ResponseLock> locks = new HashMap<String, ResponseLock>();

	@Override
	public DiameterAnswer processRequest(DiameterRequest request) {
		long startTime = System.currentTimeMillis();
		log.debug("CgiRequestListener  ..processRequest  Reached inside the method");
		ChargingInfo chargingRequest = new ChargingInfo();
		DiameterAnswer answer = null;
		try {
			chargingRequest.setUniqueMessageId(RequestContextLocalStore.get().getRequestId());

			if (request.getAvp(263) != null) {
				chargingRequest.setSessionId(request.getAvp(263).getAsUTF8String());
			}

			chargingRequest.setAvpList(request.getAvps());

			log.debug("CgiRequestListener  ..Set the AVp.");
			ChargingInfo response = producerTemplate.requestBody("seda:charging-gateway", chargingRequest, ChargingInfo.class);

			answer = createAnswer(request, response.getResultCodeAvp().getAsInt());
			answer.addAll(response.getAvpList());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (answer == null) {
				answer = createAnswer(request, DiameterErrorCode.DIAMETER_UNABLE_TO_COMPLY.getCode());
			}
			PerformanceStatsLogger.log("CG", System.currentTimeMillis() - startTime, chargingRequest.getUniqueMessageId());
		}
		return answer;
	}

	public void sendResponse(@Body ChargingInfo response) {
		ResponseLock lock = locks.get(response.getUniqueMessageId());
		if (lock == null) {
			log.error("lock does not exist for the response with messageID: " + response.getUniqueMessageId());
			throw new RuntimeException("lock does not exist for the response with messageID: " + response.getUniqueMessageId());
		}
		locks.remove(response.getUniqueMessageId());
		lock.setResponse(response);

		synchronized (lock) {
			lock.notify();
		}
	}

	public static class ResponseLock {
		private ChargingInfo chargingResponse;

		public void setResponse(ChargingInfo response) {
			this.chargingResponse = response;
		}

		public ChargingInfo getChargingResponse() {
			return chargingResponse;
		}
	}

	@SuppressWarnings("unused")
	private void snapshotRequest(ChargingInfo chargingInfo) {
		log.info("Creating the snapshot of the request.");
		try {
			String smHome = System.getenv("SEF_HOME");
			FileOutputStream stream = new FileOutputStream(smHome + File.separator + chargingInfo.getUniqueMessageId());
			ObjectOutputStream os = new ObjectOutputStream(stream);
			os.writeObject(chargingInfo);
			os.close();
		} catch (Exception e) {
			log.error("Exception in saving the request to file system.");
		}
	}

	private DiameterAnswer createAnswer(DiameterRequest request, long statusCode) {
		DiameterAnswer answer = new DiameterAnswer(request, statusCode);
		Avp sessionIdAvp = request.getAvp(SessionIdAvp.AVP_CODE);
		if (sessionIdAvp != null) {
			answer.getDiameterMessage().add(sessionIdAvp);
		}
		Avp requestTypeAvp = request.getAvp(CCRequestTypeAvp.AVP_CODE);
		if (requestTypeAvp != null) {
			answer.getDiameterMessage().add(requestTypeAvp);
		}

		Avp requestNumberAvp = request.getAvp(CCRequestNumberAvp.AVP_CODE);
		if (requestNumberAvp != null) {
			answer.getDiameterMessage().add(requestNumberAvp);
		}

		Avp authAvp = request.getAvp(AuthApplicationIdAvp.AVP_CODE);
		if (authAvp != null) {
			answer.getDiameterMessage().add(authAvp);
		}
		return answer;
	}

	public void setTimeout(long timeout) {
		if (timeout > 0l) {
			this.timeout = timeout;
		}
	}
}
