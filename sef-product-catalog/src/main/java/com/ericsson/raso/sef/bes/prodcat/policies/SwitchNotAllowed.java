package com.ericsson.raso.sef.bes.prodcat.policies;

import java.io.Serializable;

import com.ericsson.raso.sef.bes.prodcat.entities.Offer;

public final class SwitchNotAllowed extends AbstractSwitchPolicy implements Serializable {
	private static final long serialVersionUID = 6705544103267417362L;

	public SwitchNotAllowed(Offer offer) {
		super(offer);
	}

	@Override
	public boolean execute() {
		// allow super to enforce resource level max quota enforcement...
		if (super.execute()) {
		//TODO: implement the logic when you get the time....
			return true;
		}
		return false; // what? the super rejected already isnt it?
	}

	
}
