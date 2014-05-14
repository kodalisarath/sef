package com.ericsson.raso.sef.bes.prodcat.tasks;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.Constants;
import com.ericsson.raso.sef.bes.prodcat.entities.Subscription;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.RequestContext;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.raso.sef.ruleengine.ExternDataUnitTask;

public final class FetchSubscription extends ExternDataUnitTask<Subscription> {
	private static final long serialVersionUID = 6708044977435525889L;
	
	private String subscriptionId = null;
	
	public FetchSubscription(String subscriberId) {
		this.subscriptionId = subscriberId;
	}

	@Override
	public Subscription execute() throws FrameworkException {
		Subscription subscriber = null;
		
		//TODO: Need DB logic to implement here....
		/*
		 * Dont forget to fetch the following...
		 * 1. use the subscriptionId to query for the subscription
		 * 2. get subscriberId from there...
		 * 3. fetch subscription history and load the arraylist with exact order as sorted by timestamp
		 * 
		 */
		
		return subscriber;
	}
	
	
	public String getSubscriberId() {
		return subscriptionId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriptionId = subscriberId;
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
