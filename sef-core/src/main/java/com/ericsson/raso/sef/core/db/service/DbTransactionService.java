package com.ericsson.raso.sef.core.db.service;

import com.ericsson.raso.sef.core.SmException;

public interface DbTransactionService {
	
	<T> T execute(TransactionCallback<T> callback) throws SmException;

}
