package com.ericsson.raso.sef.core.ne;

public class ExternalNotifcationEvent extends NotificationEvent {

	private static final long serialVersionUID = 1L;

	private String eventId;
	private String wsClientId;
	
	private NotificationAction action;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getWsClientId() {
		return wsClientId;
	}

	public void setWsClientId(String wsClientId) {
		this.wsClientId = wsClientId;
	}

	public NotificationAction getAction() {
		return action;
	}

	public void setAction(NotificationAction action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "ExternalNotifcationEvent [eventId=" + eventId + ", wsClientId=" + wsClientId + ", action=" + action
				+ ", getChargeAmount()=" + getChargeAmount() + ", getDescription()=" + getDescription() + ", getMessages()="
				+ getMessages() + ", getMetas()=" + getMetas() + ", getSenderAddr()=" + getSenderAddr() + "]";
	}
	
	
}
