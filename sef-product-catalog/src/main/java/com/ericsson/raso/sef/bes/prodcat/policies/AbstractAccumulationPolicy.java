package com.ericsson.raso.sef.bes.prodcat.policies;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.Constants;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.tasks.GetSubscribedQuotaForResource;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.ruleengine.Action;

public abstract class AbstractAccumulationPolicy implements Action, Serializable {
	private static final long serialVersionUID = -4705320662386261256L;
	
	private Offer offer = null;
	
	public AbstractAccumulationPolicy(Offer offer) {
		this.offer = offer;
	}

	public boolean execute() {
		Map<String, Object> localContext =  RequestContextLocalStore.get().getInProcess(); 
		Subscriber subscriber = (Subscriber) localContext.get(Constants.SUBSCRIBER_ENTITY.name());
		List<Resource> resources = this.offer.getAllResources();
		
		for (Resource resource: resources) {
			try {
				Long provisionedQuota = new GetSubscribedQuotaForResource(subscriber.getMsisdn(), resource.getName()).execute();
				if (resource.getEnforcedMaxQuota() > (provisionedQuota + this.offer.getCumulativeQuotaForResource(resource.getName()))) {
					//TODO: logger - which rsource breached the max quota enforcement....
					return false;
				}
			} catch (FrameworkException e) {
				// TODO logger - actually there is no piece of code that throws any Framework or Catalog Exception in this block... so just print exception trace for troubleshooting.
				return false;
			}
		}
		
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
}
