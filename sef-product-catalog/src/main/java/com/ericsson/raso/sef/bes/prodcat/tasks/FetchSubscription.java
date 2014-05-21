package com.ericsson.raso.sef.bes.prodcat.tasks;

import com.ericsson.raso.sef.bes.prodcat.entities.Subscription;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.ruleengine.ExternDataUnitTask;

public final class FetchSubscription extends ExternDataUnitTask<Subscription> {
	private static final long serialVersionUID = 6708044977435525889L;
	
	private String subscriptionId = null;
	
	public FetchSubscription(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	@Override
	public Subscription execute() throws FrameworkException {
		Subscription subscription = null;
		
		//TODO: Need DB logic to implement here....
		/*
		 * Dont forget to fetch the following...
		 * 1. use the subscriptionId to query for the subscription
		 * 2. get subscriberId from there...
		 * 3. fetch subscription history and load the arraylist with exact order as sorted by timestamp
		 * 4. synchronize rest of the attributes from Offer and its version, before returning the entity...
		 * 
		 */
		
		return subscription;
	}
	
	
	
	
	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subscriptionId == null) ? 0 : subscriptionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!(obj instanceof FetchSubscription))
			return false;
		
		FetchSubscription other = (FetchSubscription) obj;
		if (subscriptionId == null) {
			if (other.subscriptionId != null)
				return false;
		} else if (!subscriptionId.equals(other.subscriptionId))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "<FetchSubscriber subscriberId='" + subscriptionId + "' /> ";
	}
	
	

	
}
