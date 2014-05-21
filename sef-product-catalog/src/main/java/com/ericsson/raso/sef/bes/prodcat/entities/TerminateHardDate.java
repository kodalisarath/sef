package com.ericsson.raso.sef.bes.prodcat.entities;

import java.util.Date;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public class TerminateHardDate extends AbstractAutoTermination {
	private static final long serialVersionUID = -315675038228769117L;

	private long date = -1;

	public TerminateHardDate( Date date) {
		super(Type.HARD_STOP);
		this.date = date.getTime();
	}

	@Override
	public long getTerminationTime(Subscription subsription) throws CatalogException {
		return this.date;
	}

	@Override
	public long getTerminationTime(long activationTime, long renewalPeriod) throws CatalogException {
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
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!super.equals(obj))
			return false;
		
		if (!(obj instanceof TerminateHardDate))
			return false;
		
		TerminateHardDate other = (TerminateHardDate) obj;
		if (date != other.date)
			return false;
		
		return true;
	}

	
	
}
