package com.ericsson.raso.sef.core.db.model;

import java.io.Serializable;

public enum SubscriptionLifeCycleEvent implements Serializable {
	NEW_PURCHASE,
	RENEWAL,
	EXPIRY,
	TERMINATION,
	PRE_EXPIRY;
}
