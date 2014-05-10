package com.ericsson.raso.sef.bes.prodcat.tasks;

import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.ruleengine.ExternDataUnitTask;

public final class GetActiveSubscriptionCountForOffer extends ExternDataUnitTask<Integer> {
	private static final long serialVersionUID = 6708044977435525889L;
	
	private String offerId = null;
	private int version = -1;
	
	public GetActiveSubscriptionCountForOffer(String offerId, int version) {
		super();
		this.offerId = offerId;
		this.version = version;
	}

	@Override
	public Integer execute() throws FrameworkException {
		// TODO implement the following logic...
		/*
		 * 1. Get Subscriber Entity Manager
		 * 2. Execute the query to count the active subscriptions for the offer Id + its version...
		 * 3. return the value here...
		 */
		return null;
	}
	
	

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offerId == null) ? 0 : offerId.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!(obj instanceof GetActiveSubscriptionCountForOffer))
			return false;
		
		GetActiveSubscriptionCountForOffer other = (GetActiveSubscriptionCountForOffer) obj;
		if (offerId == null) {
			if (other.offerId != null)
				return false;
		} else if (!offerId.equals(other.offerId))
			return false;
		
		if (version != other.version)
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "<GetActiveSubscriptionCountForOffer offerId='" + offerId + "' version='" + version + "'/> ";
	}
	
	

	
}
