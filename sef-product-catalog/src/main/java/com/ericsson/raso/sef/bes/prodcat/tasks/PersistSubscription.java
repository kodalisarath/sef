package com.ericsson.raso.sef.bes.prodcat.tasks;

import com.ericsson.raso.sef.bes.prodcat.entities.Subscription;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.ruleengine.ExternDataUnitTask;

public class PersistSubscription extends ExternDataUnitTask<Boolean> {
	private static final long serialVersionUID = 1669478045714425234L;
	
	private Subscription subscription = null;
	
	public PersistSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	@Override
	public Boolean execute() throws FrameworkException {
		// TODO implement the following logic...
		/*
		 * 1. save the subscription entity to subscription table...
		 * 1a. ensure the offerId, version, description are saved
		 * 2. Save the entries in Subscription History
		 * 3. Save new entries to purchase table (simple audit trail on event, timestamp, chargedAmount). Steps 2 & 3 can be the same physical DB table.
		 * 4. Each AtomicProduct needs to be saved to table also. (SubscriberId + Product Name + activationTimestamp) => composite primary key.
		 * 4a. Update AtomicProduct when validity period is run out & create new entry with activationTimestamp for renewal. This is critical for TimeCharacteristic calculations.  
		 */
		
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subscription == null) ? 0 : subscription.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PersistSubscription))
			return false;
		PersistSubscription other = (PersistSubscription) obj;
		if (subscription == null) {
			if (other.subscription != null)
				return false;
		} else if (!subscription.equals(other.subscription))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "<PersistSubscription subscription='" + subscription + "' />";
	}

	

}
