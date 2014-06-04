package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.util.Set;



public final class ReadSubscriberMetaRequest extends AbstractRequest {
	private static final long	serialVersionUID	= 5310036737033766847L;

	private String subscriberId = null;
	private Set<String> metaNames = null;

	public ReadSubscriberMetaRequest(String requestCorrelator, String subscriberId, Set<String> metaNames) {
		super(requestCorrelator);
		this.subscriberId = subscriberId;
		this.metaNames = metaNames;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public Set<String> getMetaNames() {
		return metaNames;
	}

	public void setMetaNames(Set<String> metaName) {
		this.metaNames = metaName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((metaNames == null) ? 0 : metaNames.hashCode());
		result = prime * result + ((subscriberId == null) ? 0 : subscriberId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReadSubscriberMetaRequest other = (ReadSubscriberMetaRequest) obj;
		if (metaNames == null) {
			if (other.metaNames != null)
				return false;
		} else if (!metaNames.equals(other.metaNames))
			return false;
		if (subscriberId == null) {
			if (other.subscriberId != null)
				return false;
		} else if (!subscriberId.equals(other.subscriberId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "<ReadSubscriberMetaRequest subscriberId='" + subscriberId + "' metaName='" + metaNames + "' />";
	}
	
	


}
