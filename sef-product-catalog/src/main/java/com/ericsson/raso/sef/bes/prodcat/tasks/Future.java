package com.ericsson.raso.sef.bes.prodcat.tasks;

import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;

public final class Future extends TransactionTask  {
	private static final long serialVersionUID = -7774372771842184423L;

	private FutureMode mode = null;
	private SubscriptionLifeCycleEvent event = null;
	private String offerId = null;
	private String subscriberId = null;
	private Long schedule;

	
	public Future(FutureMode mode, SubscriptionLifeCycleEvent event, String offerId, String subscriberId, Long schedule) {
		super(Type.FUTURE);
		this.mode = mode;
		this.offerId = offerId;
		this.subscriberId = subscriberId;
		this.schedule = schedule;
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
	
	

	
}
