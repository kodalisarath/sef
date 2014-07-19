package com.ericsson.raso.sef.core.db.service.smart;

import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;

public interface ChargingSessionService {
	
	public void create(ChargingSession session);
	
	public void delete(String sessionId);
	
	public ChargingSession read(String sessionId);
	
	public void update( ChargingSession session);
	

}
