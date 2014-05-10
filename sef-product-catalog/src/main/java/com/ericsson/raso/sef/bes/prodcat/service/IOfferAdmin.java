package com.ericsson.raso.sef.bes.prodcat.service;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.State;

public interface IOfferAdmin {

	public abstract void changeLifeCycle(String offerId, int vesion, State newState) throws CatalogException;

	public abstract void createOffer(Offer offer) throws CatalogException;

	/**
	 * This is purely housekeeping method and must never be called from GUI. Use changeLifeCycle() instead to put the offer in RETIRED
	 * state. Not adhering to this directive, will cause a lot of customer disputes with active subscriptions.
	 * 
	 * @param offerId
	 * @throws CatalogException 
	 */
	public abstract void deleteOffer(String offerId) throws CatalogException;

	public abstract void updateOffer(Offer offer) throws CatalogException;

	public abstract Offer getOfferById(String id);

	public abstract Offer getOfferById(String id, int version);

	boolean offerExists(String offerId);

}