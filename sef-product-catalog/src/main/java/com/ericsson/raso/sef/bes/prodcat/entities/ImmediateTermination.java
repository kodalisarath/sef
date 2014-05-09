package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public final class ImmediateTermination implements Serializable {
	private static final long serialVersionUID = -8061909838812195544L;

	private Map<SubscriberType, Boolean> isAllowed = null;
	
	public void setIsAllowed(SubscriberType subscriberType, boolean allowed) {
		if (subscriberType == null)
			return;
		
		if (this.isAllowed == null)
			this.isAllowed = new TreeMap<SubscriberType, Boolean>();
		
		this.isAllowed.put(subscriberType, allowed);
	}
}
