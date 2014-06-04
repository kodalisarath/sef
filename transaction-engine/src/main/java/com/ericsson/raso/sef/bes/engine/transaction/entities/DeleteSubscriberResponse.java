package com.ericsson.raso.sef.bes.engine.transaction.entities;


public final class DeleteSubscriberResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 9103853104666390991L;

	private Boolean result = null;

	public DeleteSubscriberResponse(String requestCorrelator, Boolean result) {
		super(requestCorrelator);
		this.result = result;
	}

	public Boolean getResult() {
		return result;
	}

	@Override
	public String toString() {
		return "DeleteSubscriberResponse [result=" + result + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.result == null) ? 0 : this.result.hashCode());
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
		DeleteSubscriberResponse other = (DeleteSubscriberResponse) obj;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		return true;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	
	
}
