package com.ericsson.raso.sef.bes.prodcat;

import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.SecureSerializationHelper;

public class OfferManager {
	
	private static String offerStoreLocation = null;

	private SecureSerializationHelper ssh = null;
	
	private OfferContainer container = new OfferContainer();

	public OfferManager() {
		// TODO: fetch the store location from config, once the config services is ready....

		ssh = new SecureSerializationHelper();
		if (ssh.fileExists(this.offerStoreLocation)) {
			try {
				this.container = (OfferContainer) ssh.fetchFromFile(this.offerStoreLocation);
			} catch (FrameworkException e) {
				// TODO: LOgger on this error...
			}
		}
	}
	
	public void persistOffer(Offer offer) throws CatalogException {
		if (offer == null)
			throw new CatalogException("Given Offer was null");
		
		container.createOffer(offer);
	}
}
