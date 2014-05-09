package com.ericsson.raso.sef.bes.prodcat.entities;

public final class NoTermination extends AbstractAutoTermination {
	private static final long serialVersionUID = -7360809560501354535L;
	
	private static final long INFINITE = -1L;

	public NoTermination() {
		super(Type.NO_TERMINATION);
	}

	@Override
	public long getTerminationTime(Subscription subsription) {
		return INFINITE;
	}

	

}
