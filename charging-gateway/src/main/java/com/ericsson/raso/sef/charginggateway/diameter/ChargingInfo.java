package com.ericsson.raso.sef.charginggateway.diameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.ResultCodeAvp;

public class ChargingInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String uniqueMessageId;
	private String sessionId;

	private List<Avp> avpList = new ArrayList<Avp>();
	
	private ResultCodeAvp resultCodeAvp;

	public String getUniqueMessageId() {
		return uniqueMessageId;
	}

	public void setUniqueMessageId(String uniqueMessageId) {
		this.uniqueMessageId = uniqueMessageId;
	}

	public List<Avp> getAvpList() {
		return avpList;
	}

	public void setAvpList(List<Avp> avpList) {
		this.avpList = avpList;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public ResultCodeAvp getResultCodeAvp() {
		return resultCodeAvp;
	}

	public void setResultCodeAvp(ResultCodeAvp resultCodeAvp) {
		this.resultCodeAvp = resultCodeAvp;
	}
}
