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

	

}
