package com.ericsson.raso.sef.core.db.service;

import com.ericsson.raso.sef.core.SmException;

public interface TransactionCallback<T> {
	
	T doInTranscation() throws SmException;

}
