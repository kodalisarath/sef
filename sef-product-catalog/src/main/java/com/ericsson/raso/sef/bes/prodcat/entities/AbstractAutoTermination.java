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

	public Type getType() {
		return this.type;
	}
	
	enum Type {
		NO_TERMINATION,
		AFTER_X_DAYS,
		AFTER_X_RENEWALS,
		HARD_STOP;
	}
}
