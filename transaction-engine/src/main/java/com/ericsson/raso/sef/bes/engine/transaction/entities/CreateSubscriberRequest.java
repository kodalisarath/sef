package com.ericsson.raso.sef.bes.engine.transaction.entities;

import com.ericsson.sef.bes.api.entities.Subscriber;


public final class CreateSubscriberRequest extends AbstractRequest {
	private static final long serialVersionUID = -5742652213191533447L;
	
	private Subscriber subscriber = null;

	public CreateSubscriberRequest(String requestCorrelator, Subscriber subscriber) {
		super(requestCorrelator);
		this.subscriber = subscriber;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	@Override
	public String toString() {
		return "CreateSubscriberRequest [subscriber=" + subscriber + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
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
		CreateSubscriberRequest other = (CreateSubscriberRequest) obj;
		if (subscriber == null) {
			if (other.subscriber != null)
				return false;
		} else if (!subscriber.equals(other.subscriber))
			return false;
		return true;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public com.ericsson.raso.sef.core.db.model.Subscriber persistableEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
