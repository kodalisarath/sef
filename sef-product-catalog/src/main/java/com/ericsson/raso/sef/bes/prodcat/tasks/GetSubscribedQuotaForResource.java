package com.ericsson.raso.sef.bes.prodcat.tasks;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.bes.prodcat.Constants;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.Product;
import com.ericsson.raso.sef.bes.prodcat.entities.ProductPackage;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.entities.Subscription;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.RequestContext;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.ruleengine.ExternDataUnitTask;

public class GetSubscribedQuotaForResource extends ExternDataUnitTask<Long> {
	private static final long serialVersionUID = 322048054820262603L;

	private String subscriberId = null;
	private String resourceName = null;

	public GetSubscribedQuotaForResource(String susbcriberId, String resourceName) {
		this.subscriberId = susbcriberId;
		this.resourceName = resourceName;
	}


	@Override
	public Long execute() throws FrameworkException {
		Subscriber subscriber = new FetchSubscriber(subscriberId).execute();
		
		//TODO: implement the following logic....
		/*
		 * 1. fetch all active subscriptions to offers with this resource ---------------------------------- THIS IS PENDING DB TIER
		 * 2. identify all the atomic products under these offers pertinent to the resources --------------- DONE
		 * 3. sum up the defined quota from these products ------------------------------------------------- DONE
		 */
		List<Subscription> subscriptions = new GetActiveUserSubscriptionsForResource(subscriberId, resourceName).execute(); 
		long provisionedQuota = 0;
		for (Subscription subscription: subscriptions) {
			for (Product product: subscription.getProducts()) {
				if (product instanceof AtomicProduct) {
					Resource resource = ((AtomicProduct)product).getResource(); 
					if (resource.getName().equals(resourceName)) {
						if (resource.isConsumable()) {
							provisionedQuota += ((AtomicProduct) product).getQuota().getDefinedQuota();
						}
					}	
				}
				
				if (product instanceof ProductPackage) {
					for (AtomicProduct atomicProduct: ((ProductPackage)product).getAtomicProducts()) {
						Resource resource = atomicProduct.getResource();
						if (resource.getName().equals(resourceName)) {
							if (resource.isConsumable()) {
								provisionedQuota += ((AtomicProduct) product).getQuota().getDefinedQuota();
							}
						}
					}
				}
			}
		}
		return provisionedQuota;
	}

	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resourceName == null) ? 0 : resourceName.hashCode());
		result = prime * result + ((subscriberId == null) ? 0 : subscriberId.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!(obj instanceof GetSubscribedQuotaForResource))
			return false;
		
		GetSubscribedQuotaForResource other = (GetSubscribedQuotaForResource) obj;
		if (resourceName == null) {
			if (other.resourceName != null)
				return false;
		} else if (!resourceName.equals(other.resourceName))
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
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
