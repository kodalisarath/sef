package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public abstract class AbstractMinimumCommitment implements Serializable {
	private static final long serialVersionUID = -7299692764954263499L;
	
	private Type type = Type.NO_COMMITMENT;
	
	protected AbstractMinimumCommitment(Type type) {
		this.type = type;
	}
	
	public abstract long getCommitmentTime(Subscription subsription) throws CatalogException;

	public abstract long getCommitmentTime(long activationTime, long renewalPeriod) throws CatalogException;
	
	public Type getType() {
		return this.type;
	}
	
	enum Type implements Serializable {
		NO_COMMITMENT,
		UNTIL_X_DAYS,
		UNTIL_X_RENEWALS,
		HARD_LIMIT;
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
		
		if (!(obj instanceof AbstractMinimumCommitment))
			return false;
		
		AbstractMinimumCommitment other = (AbstractMinimumCommitment) obj;
		if (type != other.type)
			return false;
		
		return true;
	}

	
	
}
