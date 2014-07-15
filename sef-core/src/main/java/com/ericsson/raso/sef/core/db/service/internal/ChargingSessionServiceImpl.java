package com.ericsson.raso.sef.core.db.service.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.db.mapper.smart.ChargingSessionMapper;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;
import com.ericsson.raso.sef.core.db.service.smart.ChargingSessionService;

public class ChargingSessionServiceImpl implements ChargingSessionService {
	private static final Logger logger = LoggerFactory.getLogger(ChargingSessionServiceImpl.class);

	private ChargingSessionMapper chargingSessionMapper = null;

	public void setChargingSessionMapper(ChargingSessionMapper chargingSessionMapper) {
		this.chargingSessionMapper = chargingSessionMapper;
	}
	
	@Override
	public void put(ChargingSession session) {
		if (chargingSessionMapper.get(session.getSessionId()) != null) {
			logger.debug("Will update the charging Session: " + session.getSessionId());
			chargingSessionMapper.putExisting(session);
		} else {
			logger.debug("Will make new charging Session: " + session.getSessionId());
			chargingSessionMapper.putNew(session);
		}
	}

	@Override
	public void remove(String sessionId) {
		logger.debug("removing the charging Session: " + sessionId);
		chargingSessionMapper.remove(sessionId);		
	}

	@Override
	public ChargingSession get(String sessionId) {
		logger.debug("fetching charging Session: " + sessionId);
		return chargingSessionMapper.get(sessionId);
	}

}