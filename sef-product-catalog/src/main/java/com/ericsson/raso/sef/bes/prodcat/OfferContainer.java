package com.ericsson.raso.sef.bes.prodcat;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.Product;
import com.ericsson.raso.sef.bes.prodcat.entities.ProductPackage;

public class OfferContainer implements Serializable {
	private static final long serialVersionUID = -5036814035013312974L;

	/**
	 * hierarchical store for managing offers; with the structure: Offer Name --> Version --> Offer Instance
	 */
	private TreeMap<String, TreeMap<Integer, Offer>> offersById = new TreeMap<String, TreeMap<Integer,Offer>>();
	/**
	 * hierarchical store for mapping resources to offers - to enable quick discoveries; use of identifiers in String is ensure memory leak specially serialization is involved.
	 * structure: Resource Name --> List of Offer Names
	 */
	private Map<String, Set<String>> offersByResourceName = new TreeMap<String, Set<String>>();
	
	/**
	 * simple mapping store to identify the products with external handles; this will allow federation scenarios to external catalogs to rapidly identify offer in our catalog. 
	 */
	private Map<String, String> offersByExternalHandle = new TreeMap<String, String>();
	
	public void createOffer(Offer offer) throws CatalogException {
		if (offer == null)
			throw new CatalogException("Given Offer was null");
		
		if (this.offerExists(offer.getName()))
			throw new CatalogException("Offer already exists!! Use updateOffer() instead, which can manage versions, if this offer is a variant!");
		
		offer.validate(true);
		
		// creating a transaction context
		synchronized (this) {
			// first manage offer....
			TreeMap<Integer, Offer> versionedOffer = new TreeMap<Integer, Offer>();
			versionedOffer.put(offer.getVersion(), offer);
			this.offersById.put(offer.getName(), versionedOffer);
			
			// now.. map the resources to offer 
			for (Product product: offer.getProducts()) {
				if (product instanceof AtomicProduct) {
					String resourceName = ((AtomicProduct)product).getResource().getName();
					Set<String> offers = this.offersByResourceName.get(resourceName);
					if (offers == null)
						offers = new TreeSet<String>();
					offers.add(offer.getName());
					this.offersByResourceName.put(resourceName, offers);
				}
				
				if (product instanceof ProductPackage) {
					for (AtomicProduct atomicProduct: ((ProductPackage) product).getAtomicProducts()) {
						String resourceName = atomicProduct.getResource().getName();
						Set<String> offers = this.offersByResourceName.get(resourceName);
						if (offers == null)
							offers = new TreeSet<String>();
						offers.add(offer.getName());
						this.offersByResourceName.put(resourceName, offers);
					}
				}
			}
			
			// then deal with federatd catalog - external handles
			Set<String> handles = offer.getExternalHandles();
			for (String handle: handles) {
				this.offersByExternalHandle.put(handle, offer.getName());
			}
		}
	}
	
	public boolean offerExists(String offerId) {
		if (this.offersById.containsKey(offerId))
			return true;
		return false;
	}
	
	public Offer getOfferById(String id) {
		Offer offer = null;
		
		TreeMap<Integer, Offer> versionedOffer = this.offersById.get(id);
		if (versionedOffer != null) {
			int latestVersion = versionedOffer.descendingKeySet().first();
			offer = versionedOffer.get(latestVersion);
		}
		
		return offer;
	}
	
	
	public Offer getOfferById(String id, int version) {
		Offer offer = null;
		
		TreeMap<Integer, Offer> versionedOffer = this.offersById.get(id);
		if (versionedOffer != null) {
			offer = versionedOffer.get(version);
		}
		
		return offer;
	}
	
	public Offer getOfferByExternalHandle(String handle) {
		Offer offer = null;
		
		String offerId = this.offersByExternalHandle.get(handle);
		if (offerId != null) {
			offer = this.getOfferById(offerId);
		}
		
		return offer;
	}
	
	public Set<Offer> getOffersByResource(String resourceName) {
		Set<Offer> offers = new TreeSet<Offer>();
		
		
		
		return offers;
	}
	
	

}
