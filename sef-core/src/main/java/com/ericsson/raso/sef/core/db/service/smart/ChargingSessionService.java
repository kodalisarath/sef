package com.ericsson.raso.sef.core.db.service.smart;

import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;

public interface ChargingSessionService {
	
	public void put(ChargingSession session);
	
	public void remove(String sessionId);
	
	public ChargingSession get(String sessionId);
	

}
