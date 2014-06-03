package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.util.Map;



public final class ReadSubscriberRequest extends AbstractRequest {
	private static final long	serialVersionUID	= 5310036737033766847L;

	private String subscriberId = null;
	private Map<String, String> metas = null;

	public ReadSubscriberRequest(String requestCorrelator, String subscriberId, Map<String, String> metas) {
		super(requestCorrelator);
		this.subscriberId = subscriberId;
		this.metas = metas;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public Map<String, String> getMetas() {
		return metas;
	}

	public void setMetas(Map<String, String> metas) {
		this.metas = metas;
	}

	

}
