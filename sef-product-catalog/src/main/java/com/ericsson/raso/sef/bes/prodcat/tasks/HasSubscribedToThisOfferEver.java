package com.ericsson.raso.sef.bes.prodcat.tasks;

import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.ruleengine.ExternDataUnitTask;

public final class HasSubscribedToThisOfferEver extends ExternDataUnitTask<Boolean> {
	private static final long serialVersionUID = 6708044977435525889L;
	
	private String subscriberId = null;
	private String offerId = null;
	
	public HasSubscribedToThisOfferEver(String subscriberId, String offerId) {
		super();
		this.subscriberId = subscriberId;
		this.offerId = offerId;
	}

	@Override
	public Boolean execute() throws FrameworkException {
		// TODO implement the logic when you find the time...
		return null;
	}
	
	

	
	
	
	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offerId == null) ? 0 : offerId.hashCode());
		result = prime * result + ((subscriberId == null) ? 0 : subscriberId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!(obj instanceof HasSubscribedToThisOfferEver))
			return false;
		
		HasSubscribedToThisOfferEver other = (HasSubscribedToThisOfferEver) obj;
		if (offerId == null) {
			if (other.offerId != null)
				return false;
		} else if (!offerId.equals(other.offerId))
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
		return "<GetActiveSubscriptionCountForOffer offerId='" + subscriberId + "' version='" + offerId + "'/> ";
	}
	
	

	
}
