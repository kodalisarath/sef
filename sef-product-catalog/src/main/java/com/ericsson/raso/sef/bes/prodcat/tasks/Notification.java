package com.ericsson.raso.sef.bes.prodcat.tasks;

import java.util.Map;

public final class Notification extends TransactionTask {
	private static final long serialVersionUID = 2993665754961050718L;

	private NotificationMode mode = null;
	private String offerId = null;
	private String subscriberId = null;
	private String eventId = null;
	private Map<String, Object> metas;

	public Notification(NotificationMode mode, String offerId, String subscriberId, String eventId,
			Map<String, Object> metas) {
		super(TaskType.NOTIFICATION);
		this.mode = mode;
		this.offerId = offerId;
		this.subscriberId = subscriberId;
		this.eventId = eventId;
		this.metas = metas;
	}

	public NotificationMode getMode() {
		return mode;
	}

	public void setMode(NotificationMode mode) {
		this.mode = mode;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Map<String, Object> getMetas() {
		return metas;
	}

	public void setMetas(Map<String, Object> metas) {
		this.metas = metas;
	}

	@Override
	public String toString() {
		return "Notification [mode=" + mode + ", offerId=" + offerId + ", subscriberId=" + subscriberId + ", eventId="
				+ eventId + "]";
	}

}
