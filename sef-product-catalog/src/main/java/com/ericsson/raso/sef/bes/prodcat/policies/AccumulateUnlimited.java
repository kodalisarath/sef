package com.ericsson.raso.sef.bes.prodcat.policies;

import java.io.Serializable;

import com.ericsson.raso.sef.bes.prodcat.entities.Offer;

public final class AccumulateUnlimited extends AbstractAccumulationPolicy implements Serializable {
	private static final long serialVersionUID = 6705544103267417362L;

	public AccumulateUnlimited(Offer offer) {
		super(offer);
	}

	@Override
	public boolean execute() {
		// this is unlimited accumulation... nothing to check... just allow it
		return true;
	}

	
}
