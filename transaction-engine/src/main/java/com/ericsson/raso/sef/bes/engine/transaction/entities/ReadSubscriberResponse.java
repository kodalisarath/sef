package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.util.Map;

import com.ericsson.sef.bes.api.entities.Subscriber;


public final class ReadSubscriberResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 1953619690977342269L;
	
	private Subscriber	subscriber = null;
	private Map<String, String>	metas = null;

	
	public ReadSubscriberResponse(String requestCorrelator) {
		super(requestCorrelator);
	}


	public Subscriber getSubscriber() {
		return subscriber;
	}


	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}


	public Map<String, String> getMetas() {
		return metas;
	}


	public void setMetas(Map<String, String> metas) {
		this.metas = metas;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((metas == null) ? 0 : metas.hashCode());
		result = prime * result
				+ ((subscriber == null) ? 0 : subscriber.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReadSubscriberResponse other = (ReadSubscriberResponse) obj;
		if (metas == null) {
			if (other.metas != null)
				return false;
		} else if (!metas.equals(other.metas))
			return false;
		if (subscriber == null) {
			if (other.subscriber != null)
				return false;
		} else if (!subscriber.equals(other.subscriber))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "ReadSubscriberResponse [subscriber=" + subscriber + ", metas="
				+ metas + "]";
	}

	
	
}
