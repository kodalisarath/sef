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
	public void create(ChargingSession session) {
/*		if (chargingSessionMapper.get(session.getSessionId()) != null) {
			logger.debug("Will update the charging Session: " + session.getSessionId());
			chargingSessionMapper.putExisting(session);
		} else {
			logger.debug("Will make new charging Session: " + session.getSessionId());
			chargingSessionMapper.putNew(session);
		}*/
		
		logger.debug("adding the charging Session: " + session);
		chargingSessionMapper.create(session);
	}

	@Override
	public void delete(String sessionId) {
		logger.debug("removing the charging Session: " + sessionId);
		chargingSessionMapper.delete(sessionId);		
	}

	@Override
	public ChargingSession read(String sessionId) {
		logger.debug("fetching charging Session: " + sessionId);
		return chargingSessionMapper.read(sessionId);
	}

	@Override
	public void update(ChargingSession session) {
		logger.debug("update  charging Session: " + session);
		 chargingSessionMapper.update(session);
		
	}

}
