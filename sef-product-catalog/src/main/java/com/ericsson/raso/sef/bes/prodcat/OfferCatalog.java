package com.ericsson.raso.sef.bes.prodcat;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.State;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferAdmin;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;
import com.ericsson.raso.sef.core.FrameworkException;

public class OfferCatalog implements IOfferCatalog {
	
	public static String offerStoreLocation = null;;

	private SecureSerializationHelper ssh = null;
	
	private OfferContainer container = new OfferContainer();

	Logger logger = LoggerFactory.getLogger(OfferCatalog.class);
	
	public OfferCatalog() {
		// TODO: fetch the store location from config, once the config services is ready....

		logger.info("Offer Catalog Start======>");
		//System.out.println("Config access: " + ServiceResolver.getConfig());
		//offerStoreLocation = ServiceResolver.getConfig().getValue("GLOBAL", "offerStoreLocation");
		offerStoreLocation = getStoreLocation();
		logger.info("offerstore location: " + offerStoreLocation);
		ssh = new SecureSerializationHelper();
		if (ssh.fileExists(offerStoreLocation)) {
			try {
				this.container = (OfferContainer) ssh.fetchFromFile(offerStoreLocation);
				logger.debug("E/// Offer Catalog is loaded");
				logger.debug("Fetching a sample offer J: "  + container.getOfferById("AllText10").getName());
			} catch (FrameworkException e) {
				logger.debug("E/// I encountered an error here: " + e.getMessage() + " " + e.getCause());
				// TODO: LOgger on this error...
			}
		} else {
			logger.debug("E/// File does not exist");
		}
	}
	
	private String getStoreLocation() {
		String offerStoreLocation = System.getenv("SEF_CATALOG_HOME");
		String filename = "offerStore.ccm";
		String finalfile = "";
		String your_os = System.getProperty("os.name").toLowerCase();
		if(your_os.indexOf("win") >= 0){
			finalfile = offerStoreLocation + "\\" + filename;
		}else if(your_os.indexOf( "nix") >=0 || your_os.indexOf( "nux") >=0){
			finalfile = offerStoreLocation + "/" + filename;
		}else{
			finalfile = offerStoreLocation + "{others}" + filename;
		}
		
		return finalfile;
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

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferCatalog#handleEvent(java.lang.String, java.lang.String, int, com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent)
	 */
	@Override
	public List<TransactionTask> handleEvent(String subscriber, String offerId, int version, SubscriptionLifeCycleEvent event, boolean override, Map<String, Object> metas) throws CatalogException {
		Offer offer = container.getOfferById(offerId, version);
		if (offer == null)
			throw new CatalogException("Given Offer: " + offerId + " of version: " + version + " does not exist");
		
		return offer.execute(subscriber, event, override, metas);
	}
	
	
	
}
