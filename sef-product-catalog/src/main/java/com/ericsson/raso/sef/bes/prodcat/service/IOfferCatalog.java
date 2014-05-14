package com.ericsson.raso.sef.bes.prodcat.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;

public interface IOfferCatalog {

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferAdmin#getOfferById(java.lang.String)
	 */
	public abstract Offer getOfferById(String id);

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferAdmin#getOfferById(java.lang.String, int)
	 */
	public abstract Offer getOfferById(String id, int version);

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferAdmin#offerExists(java.lang.String)
	 */
	public abstract boolean offerExists(String offerId);

	public abstract Offer getOfferByExternalHandle(String handle);

	public abstract Set<Offer> getOffersByResource(String resourceName);

	public abstract List<TransactionTask> handleEvent(String subscriber, String offerId, int version, SubscriptionLifeCycleEvent event, boolean override,
			Map<String, Object> metas) throws CatalogException;

}