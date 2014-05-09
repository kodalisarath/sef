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

}
