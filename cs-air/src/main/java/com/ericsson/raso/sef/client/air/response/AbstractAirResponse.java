package com.ericsson.raso.sef.client.air.response;

import java.io.Serializable;
import java.util.Map;

import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcResponse;

public abstract class AbstractAirResponse extends XmlRpcResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer responseCode;

	public int getResponseCode() {
		if (responseCode != null) {
			return responseCode;
		}
		responseCode = (Integer) (((Map<?, ?>) result).get("responseCode"));
		return responseCode;
	}

	public boolean isResponseAvailable() {
		return result != null;
	}
	
	public Object getResult() {
		return result;
	}
	
}
