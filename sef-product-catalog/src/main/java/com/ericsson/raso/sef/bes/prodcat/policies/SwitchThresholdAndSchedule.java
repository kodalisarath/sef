package com.ericsson.raso.sef.bes.prodcat.policies;

import java.io.Serializable;

import com.ericsson.raso.sef.bes.prodcat.entities.Offer;

public final class SwitchThresholdAndSchedule extends AbstractSwitchPolicy implements Serializable {
	private static final long serialVersionUID = -455624226271922414L;

	public SwitchThresholdAndSchedule(Offer offer) {
		super(offer);
	}

	@Override
	public boolean execute() {
		//TODO: implement this when you get time!!
		return true;
	}

	
}
