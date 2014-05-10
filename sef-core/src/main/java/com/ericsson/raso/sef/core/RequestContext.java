package com.ericsson.raso.sef.core;

import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.auth.Actor;

public class RequestContext {
	
	private String requestId = null;
	private Actor actor = null;
	private String interfaceName = null;
	private String primitive = null;
	private Map<String, Object> requestParameters = null;
	private Map<String, Object> inProcess = null;
	
	public Actor getActor() {
		return actor;
	}
	public void setActor(Actor actor) {
		this.actor = actor;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public String getPrimitive() {
		return primitive;
	}
	public void setPrimitive(String primitive) {
		this.primitive = primitive;
	}
	public Map<String, Object> getRequestParameters() {
		if (this.requestParameters == null)
			requestParameters = new TreeMap<String, Object>();
		return requestParameters;
	}
	public void setRequestParameters(Map<String, Object> requestParameters) {
		this.requestParameters = requestParameters;
	}
	public Map<String, Object> getInProcess() {
		if (this.inProcess == null)
			this.inProcess = new TreeMap<String, Object>();
		return inProcess;
	}
	public void setInProcess(Map<String, Object> inProcess) {
		this.inProcess = inProcess;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	

}
