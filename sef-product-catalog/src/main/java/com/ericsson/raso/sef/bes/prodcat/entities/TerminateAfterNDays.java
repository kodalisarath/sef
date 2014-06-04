package com.ericsson.raso.sef.bes.prodcat.entities;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public class TerminateAfterNDays extends AbstractAutoTermination {
	private static final long serialVersionUID = -315675038228769117L;

	private static final int DAYS2MILLIS = 86400000; // (24 hours * 60 mins * 60 secs * 1000 millis)

	private int days = -1;

	public TerminateAfterNDays(int days) {
		super(Type.AFTER_X_DAYS);
		this.days = days;
	}

	@Override
	public long getTerminationTime(Subscription subsription) throws CatalogException {
		SubscriptionHistory history = subsription.getSubscriptionHistory();
		if (history == null)
			throw new CatalogException("Seems like this subcription is not activated, since it contains no history!!");

		long activationTime = history.getActivationTime();

		return (activationTime + (days * DAYS2MILLIS));

	}

	@Override
	public long getTerminationTime(long activationTime, long renewalPeriod) throws CatalogException {
		return (activationTime + (days * DAYS2MILLIS));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + days;
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
		
		if (!(obj instanceof TerminateAfterNDays))
			return false;
		
		TerminateAfterNDays other = (TerminateAfterNDays) obj;
		if (days != other.days)
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "<AutoTermination name='AfterNDays' days='" + this.days + "' />";
	}

	
	

}
