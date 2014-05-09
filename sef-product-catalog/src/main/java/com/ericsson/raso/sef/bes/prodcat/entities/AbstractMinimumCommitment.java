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

	public Type getType() {
		return this.type;
	}
	
	enum Type {
		NO_COMMITMENT,
		UNTIL_X_DAYS,
		UNTIL_X_RENEWALS,
		HARD_LIMIT;
	}
}
