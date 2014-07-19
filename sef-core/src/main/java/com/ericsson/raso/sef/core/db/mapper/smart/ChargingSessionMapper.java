package com.ericsson.raso.sef.core.db.mapper.smart;

import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;

public interface ChargingSessionMapper {
	
	/*public void putNew(ChargingSession chargingSession);
	
	public void putExisting(ChargingSession chargingSession);*/
	
	public void delete(String sessionId);
	
	public ChargingSession read(String sessionId);
	
	public void create(ChargingSession chargingSession);
	
	public void update(ChargingSession chargingSession);
	
	
}
