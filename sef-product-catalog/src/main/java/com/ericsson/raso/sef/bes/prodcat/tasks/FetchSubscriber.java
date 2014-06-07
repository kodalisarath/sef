package com.ericsson.raso.sef.bes.prodcat.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.Constants;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.RequestContext;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.raso.sef.ruleengine.ExternDataUnitTask;

public final class FetchSubscriber extends ExternDataUnitTask<Subscriber> {
	private static final long serialVersionUID = 6708044977435525889L;
	private static final Logger LOGGER = LoggerFactory.getLogger(FetchSubscriber.class);
	
	private String subscriberId = null;
	
	public FetchSubscriber(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	@Override
	public Subscriber execute() throws FrameworkException {
		Subscriber subscriber = null;
		LOGGER.debug("Inside the execute method");
		// check in request context first....
		RequestContext context = RequestContextLocalStore.get();
		subscriber = (Subscriber) context.getInProcess().get(Constants.SUBSCRIBER_ENTITY.name());
		LOGGER.debug("querying for a suscriber getInProcess");
		// if failed, attempt to fetch from db...
		if (subscriber == null) {
			SubscriberService subscriberStore = SefCoreServiceResolver.getSusbcriberStore();
			LOGGER.debug("Subscriber Store:getSubscriberStore"+subscriberStore);
			if (subscriberStore == null)
				throw new CatalogException("Unable to fetch Subscriber Profile for further processing of the request!! Please check configuration");
			subscriber = subscriberStore.getSubscriber(UniqueIdGenerator.generateId(), subscriberId);
			
			// now place it in Request Context...
			context.getInProcess().put(Constants.SUBSCRIBER_ENTITY.name(), subscriber);
		}
		LOGGER.debug("Returning a subscriber "+subscriber.getContractState());
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
