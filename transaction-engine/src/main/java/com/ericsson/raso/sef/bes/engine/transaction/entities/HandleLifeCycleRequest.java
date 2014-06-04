package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.tasks.FetchSubscriber;
import com.ericsson.raso.sef.core.FrameworkException;


public final class HandleLifeCycleRequest extends AbstractRequest {
	private static final long	serialVersionUID	= -921884001442299482L;
	
	private String subscriberId = null;
	private Map<String,String>	metas = null;

	public HandleLifeCycleRequest(String requestCorrelator, String subscriberId, Map<String,String> metas) {
		super(requestCorrelator);
		this.subscriberId = subscriberId;
		this.setMetas(metas);
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	
	public com.ericsson.raso.sef.core.db.model.Subscriber persistableEntity() throws FrameworkException {
		FetchSubscriber fetchSubscriber=new FetchSubscriber(this.subscriberId);
		return fetchSubscriber.execute();
	}

	public Map<String,String> getMetas() {
		return metas;
	}

	public void setMetas(Map<String,String> metas) {
		this.metas = metas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((metas == null) ? 0 : metas.hashCode());
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
		HandleLifeCycleRequest other = (HandleLifeCycleRequest) obj;
		if (metas == null) {
			if (other.metas != null)
				return false;
		} else if (!metas.equals(other.metas))
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
		return "HandleLifeCycleRequest [subscriberId=" + subscriberId
				+ ", metas=" + metas + "]";
	}

}
