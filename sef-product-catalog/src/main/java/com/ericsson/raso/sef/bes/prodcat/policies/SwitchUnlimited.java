package com.ericsson.raso.sef.bes.prodcat.policies;

import java.io.Serializable;

import com.ericsson.raso.sef.bes.prodcat.entities.Offer;

public final class SwitchUnlimited extends AbstractSwitchPolicy implements Serializable {
	private static final long serialVersionUID = 4167988743023760975L;

	public SwitchUnlimited(Offer offer) {
		super(offer);
	}

	@Override
	public boolean execute() {
		// this is unlimited accumulation... nothing to check... just allow it
		return true;
	}

	
}
