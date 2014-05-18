package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.io.Serializable;

enum Status implements Serializable {
	WAITING,
	PROCESSING,
	DONE_SUCCESS,
	DONE_FAILED,
	DONE_FAULT;
}