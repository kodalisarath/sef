package com.ericsson.raso.sef.core.db.mapper.smart;

import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;

public interface ChargingSessionMapper {
	
	public void putNew(ChargingSession chargingSession);
	
	public void putExisting(ChargingSession chargingSession);
	
	public void remove(String sessionId);
	
	public ChargingSession get(String sessionId);
	
}