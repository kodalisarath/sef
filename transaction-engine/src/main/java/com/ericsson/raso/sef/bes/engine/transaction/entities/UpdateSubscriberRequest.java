package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.TransactionServiceHelper;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.Meta;

public final class UpdateSubscriberRequest extends AbstractRequest {
	private static final long serialVersionUID = 5113481758520068651L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSubscriberRequest.class);
	private String subscriberId = null;
	private Map<String, String> metas = null;

	public UpdateSubscriberRequest(String requestCorrelator, String subscriberId, Map<String, String> metas) {
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

	public Map<String, String> getMetas() {
		return metas;
	}

	public void setMetas(Map<String, String> metas) {
		this.metas = metas;
	}

	public com.ericsson.raso.sef.core.db.model.Subscriber persistableEntity()
			throws FrameworkException {
		LOGGER.debug("Called persistableEntity method and querying for subscriber" + this.subscriberId);
		return TransactionServiceHelper.fetchSubscriberFromDb(subscriberId);
	}

	@Override
	public String toString() {
		return "UpdateSubscriberRequest [subscriberId=" + subscriberId
				+ ", metas=" + metas + "]";
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
		UpdateSubscriberRequest other = (UpdateSubscriberRequest) obj;
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

	
	//This method is to get the metas from the processor and converted to List<META>
	public List<Meta> getRequestMetas() {
		List<Meta> metaList=TransactionServiceHelper.getSefCoreList(this.metas);
		return metaList;
	}
}
