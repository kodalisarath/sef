package com.ericsson.raso.sef.bes.prodcat.policies;

import java.io.Serializable;

import com.ericsson.raso.sef.bes.prodcat.entities.Offer;

public final class AccumulateThresholdAndSchedule extends AbstractAccumulationPolicy implements Serializable {
	private static final long serialVersionUID = 5153076075007757404L;

	public AccumulateThresholdAndSchedule(Offer offer) {
		super(offer);
	}

	@Override
	public boolean execute() {
		//TODO: implement this when you get time!!
		return true;
	}

	
}
