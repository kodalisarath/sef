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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((isAllowed == null) ? 0 : isAllowed.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;

		if (!(obj instanceof ImmediateTermination))
			return false;
		
		ImmediateTermination other = (ImmediateTermination) obj;
		if (isAllowed == null) {
			if (other.isAllowed != null)
				return false;
		} else if (!isAllowed.equals(other.isAllowed))
			return false;
		
		return true;
	}
	
	
}
