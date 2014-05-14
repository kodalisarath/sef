package com.ericsson.raso.sef.bes.prodcat.tasks;

import java.util.List;

import com.ericsson.raso.sef.bes.prodcat.entities.Subscription;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.ruleengine.ExternDataUnitTask;

public final class GetActiveUserSubscriptionsForResource extends ExternDataUnitTask<List<Subscription>> {
	private static final long serialVersionUID = 6708044977435525889L;
	
	private String subscriberId = null;
	private String resource = null;
	
	public GetActiveUserSubscriptionsForResource(String subscriberId, String resource) {
		this.subscriberId = subscriberId;
		this.resource = resource;
	}

	@Override
	public List<Subscription> execute() throws FrameworkException {
		// TODO implement the following logic...
		/*
		 * 1. Get Subscription Entity Manager
		 * 2. Execute the query the active subscriptions for the resource...
		 * 3. for each subscription, fetch the offer thru id + version from the OfferManager...
		 * 3. return the result here...
		 */
		return null;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + ((subscriberId == null) ? 0 : subscriberId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (!(obj instanceof GetActiveUserSubscriptionsForResource))
			return false;
		
		GetActiveUserSubscriptionsForResource other = (GetActiveUserSubscriptionsForResource) obj;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		
		if (subscriberId == null) {
			if (other.subscriberId != null)
				return false;
		} else if (!subscriberId.equals(other.subscriberId))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "<GetActiveUserSubscriptionsForResource subscriberId='" + subscriberId + "' resource='" + resource + "'/> ";
	}
	
	

	
}
