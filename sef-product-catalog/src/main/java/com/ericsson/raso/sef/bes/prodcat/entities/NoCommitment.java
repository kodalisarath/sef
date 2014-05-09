package com.ericsson.raso.sef.bes.prodcat.entities;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public final class NoCommitment extends AbstractMinimumCommitment {
	private static final long serialVersionUID = 1373790813022717739L;

	private static final long INFINITE = -1L;

	public NoCommitment(Type type) {
		super(type);
	}

	@Override
	public long getCommitmentTime(Subscription subsription) throws CatalogException {
		return INFINITE;
	}

	
	

}
