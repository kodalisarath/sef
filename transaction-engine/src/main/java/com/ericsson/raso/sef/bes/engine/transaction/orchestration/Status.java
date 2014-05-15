package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.io.Serializable;

enum Status implements Serializable {
	WAITING,
	PROCESSING,
	ROLLING_BACK,
	DONE_SUCCESS,
	DONE_FAILED,
	DONE_FAULT;
}