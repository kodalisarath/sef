package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public abstract class AbstractAutoTermination implements Serializable {
	private static final long serialVersionUID = -2246119428227257046L;
	
	private Type type = Type.NO_TERMINATION;
	
	protected AbstractAutoTermination(Type type) {
		this.type = type;
	}
	
	public abstract long getTerminationTime(Subscription subsription) throws CatalogException;

	public abstract long getTerminationTime(long activationTime, long renewalPeriod) throws CatalogException;

	public Type getType() {
		return this.type;
	}
	
	enum Type implements Serializable {
		NO_TERMINATION,
		AFTER_X_DAYS,
		AFTER_X_RENEWALS,
		HARD_STOP;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!(obj instanceof AbstractAutoTermination))
			return false;
		
		AbstractAutoTermination other = (AbstractAutoTermination) obj;
		if (type != other.type)
			return false;
		
		return true;
	}
	
	
}
