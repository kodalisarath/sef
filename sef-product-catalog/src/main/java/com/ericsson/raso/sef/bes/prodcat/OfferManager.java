package com.ericsson.raso.sef.bes.prodcat;

import java.io.Serializable;
import java.util.Set;

import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.State;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferAdmin;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.SecureSerializationHelper;

public class OfferManager implements IOfferAdmin, IOfferCatalog {
	
	private static String offerStoreLocation = null;

	private SecureSerializationHelper ssh = null;
	
	private OfferContainer container = new OfferContainer();

	public OfferManager() {
		// TODO: fetch the store location from config, once the config services is ready....

		ssh = new SecureSerializationHelper();
		if (ssh.fileExists(offerStoreLocation)) {
			try {
				this.container = (OfferContainer) ssh.fetchFromFile(offerStoreLocation);
			} catch (FrameworkException e) {
				// TODO: LOgger on this error...
			}
		}
	}
	
	//-----------============ Admin Functions ========================================================================================================
	
	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferAdmin#changeLifeCycle(java.lang.String, int, com.ericsson.raso.sef.bes.prodcat.entities.State)
	 */
	@Override
	public void changeLifeCycle(String offerId, int vesion, State newState) throws CatalogException {
		container.changeLifeCycle(offerId, vesion, newState);
		
		try {
			this.ssh.persistToFile(offerStoreLocation, (Serializable) this.container);
		} catch (FrameworkException e) {
			throw new CatalogException("Could not save the changes!!!", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferAdmin#createOffer(com.ericsson.raso.sef.bes.prodcat.entities.Offer)
	 */
	@Override
	public void createOffer(Offer offer) throws CatalogException {
		container.createOffer(offer);
		
		try {
			this.ssh.persistToFile(offerStoreLocation, (Serializable) this.container);
		} catch (FrameworkException e) {
			throw new CatalogException("Could not save the changes!!!", e);
		}
	}
		
	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferAdmin#deleteOffer(java.lang.String)
	 */
	@Override
	@Deprecated
	public void deleteOffer(String offerId) throws CatalogException {
		container.deleteOffer(offerId);
		
		try {
			this.ssh.persistToFile(offerStoreLocation, (Serializable) this.container);
		} catch (FrameworkException e) {
			throw new CatalogException("Could not save the changes!!!", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferAdmin#updateOffer(com.ericsson.raso.sef.bes.prodcat.entities.Offer)
	 */
	@Override
	public void updateOffer(Offer offer) throws CatalogException {
		container.updateOffer(offer);
		
		try {
			this.ssh.persistToFile(offerStoreLocation, (Serializable) this.container);
		} catch (FrameworkException e) {
			throw new CatalogException("Could not save the changes!!!", e);
		}
	}
	
	//-----------============ Admin & Catalog Functions ==============================================================================================
	
	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferAdmin#getOfferById(java.lang.String)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferCatalog#getOfferById(java.lang.String)
	 */
	@Override
	public Offer getOfferById(String id) {
		return container.getOfferById(id);
	}
	
	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferAdmin#getOfferById(java.lang.String, int)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferCatalog#getOfferById(java.lang.String, int)
	 */
	@Override
	public Offer getOfferById(String id, int version) {
		return container.getOfferById(id, version);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferAdmin#offerExists(java.lang.String)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferCatalog#offerExists(java.lang.String)
	 */
	@Override
	public boolean offerExists(String offerId) {
		return container.offerExists(offerId);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferCatalog#getOfferByExternalHandle(java.lang.String)
	 */
	@Override
	public Offer getOfferByExternalHandle(String handle) {
		return container.getOfferByExternalHandle(handle);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferCatalog#getOffersByResource(java.lang.String)
	 */
	@Override
	public Set<Offer> getOffersByResource(String resourceName) {
		return container.getOffersByResource(resourceName);
	}
	
	//-----------============ Catalog Functions ======================================================================================================
	
	
}
