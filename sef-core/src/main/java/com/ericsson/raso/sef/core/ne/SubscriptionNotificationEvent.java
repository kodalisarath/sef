package com.ericsson.raso.sef.core.ne;

import com.ericsson.raso.sef.core.db.model.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.core.ne.NotificationEvent;

public class SubscriptionNotificationEvent extends NotificationEvent {

	private static final long serialVersionUID = 1L;

	private SubscriptionLifeCycleEvent eventType;

	public SubscriptionLifeCycleEvent getEventType() {
		return eventType;
	}

	public void setEventType(SubscriptionLifeCycleEvent eventType) {
		this.eventType = eventType;
	}
}
