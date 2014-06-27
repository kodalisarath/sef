package com.ericsson.raso.sef.core.ne;

import org.springframework.context.SmartLifecycle;

public interface CatalogChangeObserver extends SmartLifecycle {

	void notifyOfferChanges();

	void notifyPrtChanges();

	void notifyExternalResourceChanges();

	void notifyExternalNotificationChanges();
	
	void notifyAuthorizationChanges();
	
}
