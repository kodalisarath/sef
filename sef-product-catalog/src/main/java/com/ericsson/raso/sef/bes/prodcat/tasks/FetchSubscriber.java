package com.ericsson.raso.sef.bes.prodcat.tasks;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.Constants;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.RequestContext;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.raso.sef.ruleengine.ExternDataUnitTask;

public final class FetchSubscriber extends ExternDataUnitTask<Subscriber> {
	private static final long serialVersionUID = 6708044977435525889L;
	
	private String subscriberId = null;
	
	public FetchSubscriber(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	@Override
	public Subscriber execute() throws FrameworkException {
		Subscriber subscriber = null;
		
		// check in request context first....
		RequestContext context = RequestContextLocalStore.get();
		subscriber = (Subscriber) context.getInProcess().get(Constants.SUBSCRIBER_ENTITY.name());
		
		// if failed, attempt to fetch from db...
		if (subscriber == null) {
			SubscriberService subscriberStore = SefCoreServiceResolver.getSusbcriberStore();
			if (subscriberStore == null)
				throw new CatalogException("Unable to fetch Subscriber Profile for further processing of the request!! Please check configuration");
			subscriber = subscriberStore.getSubscriber(subscriberId);
			
			// now place it in Request Context...
			context.getInProcess().put(Constants.SUBSCRIBER_ENTITY.name(), subscriber);
		}
		
		return subscriber;
	}
	
	
	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

		@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subscriberId == null) ? 0 : subscriberId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!(obj instanceof FetchSubscriber))
			return false;
		
		FetchSubscriber other = (FetchSubscriber) obj;
		if (subscriberId == null) {
			if (other.subscriberId != null)
				return false;
		} else if (!subscriberId.equals(other.subscriberId))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "<FetchSubscriber subscriberId='" + subscriberId + "' /> ";
	}
	
	

	
}
