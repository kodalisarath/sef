package com.ericsson.raso.sef.bes.prodcat.entities;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public class CommitUntilNRenewals extends AbstractMinimumCommitment {
	private static final long serialVersionUID = 8114289722437175411L;

	private int renewals = -1;

	public CommitUntilNRenewals(int renewals) {
		super(Type.UNTIL_X_RENEWALS);
		this.renewals = renewals;
	}

	@Override
	public long getCommitmentTime(Subscription subsription) throws CatalogException {
		SubscriptionHistory history = subsription.getSubscriptionHistory();
		if (history == null) 
			throw new CatalogException("Seems like this subcription is not activated, since it contains no history!!");
		
		long activationTime = history.getActivationTime();
		
		return (activationTime + (renewals  * subsription.getRenewalPeriod().getExpiryTimeInMillis()));
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
		
		if (!(obj instanceof CommitUntilNRenewals))
			return false;
		
		CommitUntilNRenewals other = (CommitUntilNRenewals) obj;
		if (renewals != other.renewals)
			return false;
		
		return true;
	}

	

}
