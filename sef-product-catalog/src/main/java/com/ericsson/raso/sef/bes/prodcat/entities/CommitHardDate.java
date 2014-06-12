package com.ericsson.raso.sef.bes.prodcat.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public class CommitHardDate extends AbstractMinimumCommitment {
	private static final long serialVersionUID = -858943043023615771L;

	private long date = -1;

	public CommitHardDate( Date date) {
		super(Type.HARD_LIMIT);
		this.date = date.getTime();
	}

	@Override
	public long getCommitmentTime(Subscription subsription) throws CatalogException {
		return this.date;
	}

	@Override
	public long getCommitmentTime(long activationTime, long renewalPeriod) throws CatalogException {
		return this.date;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (date ^ (date >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof CommitHardDate))
			return false;
		CommitHardDate other = (CommitHardDate) obj;
		if (date != other.date)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CommitHardDate [date=" + date + "]";
	}


	

}
