package com.ericsson.raso.sef.ne.notification;

import com.ericsson.raso.sef.core.db.model.SubscriptionLifeCycleEvent;

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
