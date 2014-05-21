package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;


public final class HandleSubscriptionEventRequest extends AbstractRequest {
	private static final long serialVersionUID = -5742652213191533447L;
	
	private String offerId = null;
	private String subscriberId = null;
	private String subscriptionId = null;
	private SubscriptionLifeCycleEvent event = null;
	private Boolean override = null;
	private Map<String, Object> metas = null;

	public HandleSubscriptionEventRequest(String requestCorrelator, String offerId, String subscriberId, String subscriptionId, SubscriptionLifeCycleEvent event, Boolean override, Map<String, Object> metas) {
		super(requestCorrelator);
		this.offerId = offerId;
		this.subscriberId = subscriberId;
		this.subscriptionId = subscriptionId;
		this.event = event;
		this.override = override;
		this.metas = metas;
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

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public SubscriptionLifeCycleEvent getEvent() {
		return event;
	}

	public void setEvent(SubscriptionLifeCycleEvent event) {
		this.event = event;
	}

	public Boolean getOverride() {
		return override;
	}

	public void setOverride(Boolean override) {
		this.override = override;
	}

	public Map<String, Object> getMetas() {
		return metas;
	}

	public void setMetas(Map<String, Object> metas) {
		this.metas = metas;
	}

	
			
}
