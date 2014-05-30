package com.ericsson.raso.sef.bes.engine.transaction;

import java.io.Serializable;

public enum Phase implements Serializable {
	TX_PHASE_CHARGING,
	TX_PHASE_PREP_FULFILLMENT,
	TX_PHASE_FULFILLMENT,
	TX_PHASE_SCHEDULE,
	TX_PHASE_PERSISTENCE,  
	
}
