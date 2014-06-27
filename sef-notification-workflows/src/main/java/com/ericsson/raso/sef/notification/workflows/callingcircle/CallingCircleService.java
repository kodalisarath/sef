package com.ericsson.raso.sef.notification.workflows.callingcircle;

import java.util.Collection;

import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.CallingCircle;

/**
 * No long required, will call TransactionEngine Helper to do the job
 * @author Khai Shen
 *
 */
@Deprecated 
public interface CallingCircleService {

	Collection<CallingCircle> addCircle(String callingCircleId, String aparty, String bparty) throws SmException;

	void removeCircle(long circleId) throws SmException;
}
