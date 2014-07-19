/*package com.ericsson.raso.sef.cg.engine;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.cg.model.ChargingSession;
import com.ericsson.raso.sef.core.cg.model.Operation;
import com.google.gson.Gson;

public class IpcCluster {

	private static final String CHARGING_REQUEST_STORE = "cge.session.store";

	private SmCache smCache;

	public IpcCluster(SmCache smCache) {
		this.smCache = smCache;
	}
	
	public IpcCluster() {
		
	}

	public  void initiateIpcRequest(String sessionId, Operation operation, String msisdn) {

		Map<String, SmartChargingSession> map = SefCoreServiceResolver.getCloudAwareCluster().getMap(CHARGING_REQUEST_STORE);

		if(map ==null)
			map = new HashMap<String, SmartChargingSession>();
		SmartChargingSession session = new SmartChargingSession();
		
		session.setMsisdn(msisdn);
		session.setSessionId(sessionId);
		session.setOperation(operation);
		map.put(sessionId, session);
	}

	public  SmartChargingSession getChargingSession(String sessionId) {
		Map<String, SmartChargingSession> map = SefCoreServiceResolver.getCloudAwareCluster().getMap(CHARGING_REQUEST_STORE);
		return map.get(sessionId);
	}
	
	public   void updateChargingSession(String sessionId, SmartChargingSession session) {
		//Gson gson = new Gson();
		ChargingSession chargingSession = SefCoreServiceResolver.getChargingSessionService().get(session.getSessionId());
		if (chargingSession == null) {
			chargingSession = new ChargingSession();
			chargingSession.setCreationTime(Calendar.getInstance().getTimeInMillis());
			chargingSession.setExpiryTime(System.currentTimeMillis() + Long.parseLong(SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "chargingSessionExpiry")));
			chargingSession.setSessionId(session.getMessageId());
			//chargingSession.setSessionInfo(gson.toJson(session));
		}
		
		SefCoreServiceResolver.getChargingSessionService().put(chargingSession);
	}
	
	public  void invalidate(String sessionId) {
		Map<String, SmartChargingSession> map = SefCoreServiceResolver.getCloudAwareCluster().getMap(CHARGING_REQUEST_STORE);
		map.remove(sessionId);
	}
}
*/