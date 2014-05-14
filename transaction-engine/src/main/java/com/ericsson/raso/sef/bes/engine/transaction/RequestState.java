package com.ericsson.raso.sef.bes.engine.transaction;

public enum RequestState {
	UNDEF,
	ACKNOWLEDGED,
	USECASE_DELEGATED,
	USECASE_REJECTED,
	TX_CHARGING,
	TX_FULFILL,
	TX_ROLLBACK_FULFILL,
	TX_CHARGE_REVERSAL,
	TX_NOTIFY,
	COMPLETED;

}
