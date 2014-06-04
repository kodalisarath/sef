package com.ericsson.raso.sef.bes.engine.transaction.entities;



public final class DeleteSubscriberRequest extends AbstractRequest {
	private static final long	serialVersionUID	= 5113481758520068651L;
	
	private String subscriberId = null;

	public DeleteSubscriberRequest(String requestCorrelator, String subscriberId) {
		super(requestCorrelator);
		this.subscriberId = subscriberId;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	
	public com.ericsson.raso.sef.core.db.model.Subscriber persistableEntity() {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public String toString() {
		return "DeleteSubscriberRequest [subscriberId=" + subscriberId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((subscriberId == null) ? 0 : subscriberId.hashCode());
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
		DeleteSubscriberRequest other = (DeleteSubscriberRequest) obj;
		if (subscriberId == null) {
			if (other.subscriberId != null)
				return false;
		} else if (!subscriberId.equals(other.subscriberId))
			return false;
		return true;
	}

			

}
