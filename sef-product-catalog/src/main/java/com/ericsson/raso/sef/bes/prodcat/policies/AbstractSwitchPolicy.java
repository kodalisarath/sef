package com.ericsson.raso.sef.bes.prodcat.policies;

import java.io.Serializable;

import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.ruleengine.Action;

public abstract class AbstractSwitchPolicy implements Action, Serializable {
	private static final long serialVersionUID = -4705320662386261256L;
	
	private Offer offer = null;
	
	public AbstractSwitchPolicy(Offer offer) {
		this.offer = offer;
	}

	public boolean execute() {
		//TODO: implement this when you get time...
		return true;
	}
	
	
	

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}



	enum Type implements Serializable {
		NO_LIMIT,
		THRESHOLD_REJECT,
		THRESHOLD_SCHEDULE,
		NOT_ALLOWED;
		
	}
	
	public abstract String toString();
}
