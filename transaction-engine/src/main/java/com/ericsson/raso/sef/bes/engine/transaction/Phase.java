package com.ericsson.raso.sef.bes.engine.transaction;

import java.io.Serializable;

public enum Phase implements Serializable {
	TX_PHASE_CHARGING,
	TX_PHASE_PREP_FULFILLMENT,
	TX_PHASE_FULFILLMENT,
	TX_PHASE_SCHEDULE,
	TX_PHASE_PERSISTENCE,
	TX_PHASE_ALL_COMPLETED;
	
	public Phase getNextPhase() {
		
		switch(this) {
		case TX_PHASE_CHARGING:
			return TX_PHASE_PREP_FULFILLMENT;
		case TX_PHASE_PREP_FULFILLMENT:
			return TX_PHASE_FULFILLMENT;
		case TX_PHASE_FULFILLMENT:
			return TX_PHASE_SCHEDULE;
		case TX_PHASE_PERSISTENCE:
			return TX_PHASE_ALL_COMPLETED;
		default:
			return TX_PHASE_CHARGING;
		}
	}
}
