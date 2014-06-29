package com.ericsson.raso.sef.bes.prodcat.tasks;

import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;

public final class Future extends TransactionTask {
	private static final long serialVersionUID = -7774372771842184423L;

	private FutureMode mode = null;
	private SubscriptionLifeCycleEvent event = null;
	private String offerId = null;
	private String subscriberId = null;
	private Long schedule;
	private Map<String, Object> metas;

	public Future(FutureMode mode, SubscriptionLifeCycleEvent event, String offerId, String subscriberId,
			Long schedule, Map<String, Object> metas) {
		super(TaskType.FUTURE);
		this.mode = mode;
		this.event = event;
		this.offerId = offerId;
		this.subscriberId = subscriberId;
		this.schedule = schedule;
		this.metas = metas;
	}

	public FutureMode getMode() {
		return mode;
	}

	public void setMode(FutureMode mode) {
		this.mode = mode;
	}

	public SubscriptionLifeCycleEvent getEvent() {
		return event;
	}

	public void setEvent(SubscriptionLifeCycleEvent event) {
		this.event = event;
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

	public Long getSchedule() {
		return schedule;
	}

	public void setSchedule(Long schedule) {
		this.schedule = schedule;
	}

	public Map<String, Object> getMetas() {
		return metas;
	}

	public void setMetas(Map<String, Object> metas) {
		this.metas = metas;
	}

	@Override
	public String toString() {
		return "Future [mode=" + mode + ", event=" + event + ", offerId=" + offerId + ", subscriberId=" + subscriberId + ", schedule="
				+ schedule + ", metas=" + metas + "]";
	}

	

}
