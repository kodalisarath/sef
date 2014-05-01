package com.ericsson.raso.sef.core.db.service.internal;

import org.springframework.transaction.annotation.Transactional;

import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.service.DbTransactionService;
import com.ericsson.raso.sef.core.db.service.TransactionCallback;

public class SmDbTransactionService implements DbTransactionService {

	@Override
	@Transactional(rollbackFor={Exception.class})
	public <T> T execute(TransactionCallback<T> callback) throws SmException {
		return callback.doInTranscation();
	}

}
