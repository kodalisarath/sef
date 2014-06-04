package com.ericsson.raso.sef.bes.prodcat.policies;

import java.io.Serializable;

import com.ericsson.raso.sef.bes.prodcat.entities.Offer;

public final class SwitchThresholdAndReject extends AbstractSwitchPolicy implements Serializable {
	private static final long serialVersionUID = 7976410281572763280L;

	public SwitchThresholdAndReject(Offer offer) {
		super(offer);
	}

	@Override
	public boolean execute() {
		//TODO: implement this when you get time!!
		return true;
	}

	@Override
	public String toString() {
		return "<Switch type='ThresholdAndReject' />";
	}


	
}
