package com.ericsson.raso.sef.cg.engine;

import java.util.HashMap;
import java.util.Map;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;

public class IpcCluster {

	private static final String CHARGING_REQUEST_STORE = "cge.session.store";

/*	private SmCache smCache;

	public IpcCluster(SmCache smCache) {
		this.smCache = smCache;
	}*/
	
	public IpcCluster() {
		
	}

	public  void initiateIpcRequest(String sessionId, Operation operation, String msisdn) {

		Map<String, ChargingSession> map = SefCoreServiceResolver.getCloudAwareCluster().getMap(CHARGING_REQUEST_STORE);

		if(map ==null)
			map = new HashMap<String, ChargingSession>();
		ChargingSession session = new ChargingSession();
		
		session.setMsisdn(msisdn);
		session.setSessionId(sessionId);
		session.setOperation(operation);
		map.put(sessionId, session);
	}

	public  ChargingSession getChargingSession(String sessionId) {
		Map<String, ChargingSession> map = SefCoreServiceResolver.getCloudAwareCluster().getMap(CHARGING_REQUEST_STORE);
		return map.get(sessionId);
	}
	
	public   void updateChargingSession(String sessionId, ChargingSession session) {
		Map<String, ChargingSession> map = SefCoreServiceResolver.getCloudAwareCluster().getMap(CHARGING_REQUEST_STORE);
		map.put(sessionId, session);
	}
	
	public  void invalidate(String sessionId) {
		Map<String, ChargingSession> map = SefCoreServiceResolver.getCloudAwareCluster().getMap(CHARGING_REQUEST_STORE);
		map.remove(sessionId);
	}
}
