package com.ericsson.raso.sef.bes.prodcat.entities;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public class TerminateAfterNRenewals extends AbstractAutoTermination {
	private static final long serialVersionUID = 8103032481877700675L;
	
	private int renewals = -1;

	public TerminateAfterNRenewals(int renewals) {
		super(Type.AFTER_X_RENEWALS);
		this.renewals = renewals;
	}

	@Override
	public long getTerminationTime(Subscription subsription) throws CatalogException {
		SubscriptionHistory history = subsription.getSubscriptionHistory();
		if (history == null) 
			throw new CatalogException("Seems like this subcription is not activated, since it contains no history!!");
		
		long activationTime = history.getActivationTime();
		
		return (activationTime + (renewals  * subsription.getRenewalPeriod().getExpiryTimeInMillis()));
	}
	
	@Override
	public long getTerminationTime(long activationTime, long renewalPeriod) throws CatalogException {
		return (activationTime + (renewals  * renewalPeriod));
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + renewals;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!super.equals(obj))
			return false;
		
		if (!(obj instanceof TerminateAfterNRenewals))
			return false;
		
		TerminateAfterNRenewals other = (TerminateAfterNRenewals) obj;
		if (renewals != other.renewals)
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "TerminateAfterNRenewals [renewals=" + renewals + ", getType()=" + getType() + "]";
	}

	

}
