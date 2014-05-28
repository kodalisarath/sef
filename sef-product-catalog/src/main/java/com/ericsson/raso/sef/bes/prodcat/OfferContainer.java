package com.ericsson.raso.sef.bes.prodcat;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.OfferLifeCycle;
import com.ericsson.raso.sef.bes.prodcat.entities.Product;
import com.ericsson.raso.sef.bes.prodcat.entities.ProductPackage;
import com.ericsson.raso.sef.bes.prodcat.entities.State;
import com.ericsson.raso.sef.bes.prodcat.tasks.GetActiveSubscriptionCountForOffer;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.ruleengine.ExternDataUnitTask;

public class OfferContainer implements Serializable {
	private static final long serialVersionUID = -5036814035013312974L;

	private static final int INITIAL_VERSION = 1;

	/**
	 * hierarchical store for managing offers; with the structure: Offer Name --> Version --> Offer Instance
	 */
	private TreeMap<String, TreeMap<Integer, Offer>> offersById = new TreeMap<String, TreeMap<Integer, Offer>>();
	/**
	 * hierarchical store for mapping resources to offers - to enable quick discoveries; use of identifiers in String is ensure memory leak
	 * specially serialization is involved. structure: Resource Name --> List of Offer Names
	 */
	private TreeMap<String, TreeSet<String>> offersByResourceName = new TreeMap<String, TreeSet<String>>();

	/**
	 * simple mapping store to identify the products with external handles; this will allow federation scenarios to external catalogs to
	 * rapidly identify offer in our catalog.
	 */
	private TreeMap<String, String> offersByExternalHandle = new TreeMap<String, String>();

	private static final Logger logger = LoggerFactory.getLogger(OfferContainer.class);
	
	public void createOffer(Offer offer) throws CatalogException {
		if (offer == null)
			throw new CatalogException("Given Offer was null");

		if (this.offerExists(offer.getName()))
			throw new CatalogException("Offer already exists!! Use updateOffer() instead, which can manage versions, if this offer is a variant!");

		offer.validate(true);
		offer.setVersion(INITIAL_VERSION);
		offer.setOfferState(State.IN_CREATION);
		if (offer.getHistory() == null)
			offer.setHistory(new OfferLifeCycle());
		offer.getHistory().setCreationTime(System.currentTimeMillis());

		// creating a transaction context
		synchronized (this) {
			// first manage offer....
			TreeMap<Integer, Offer> versionedOffer = new TreeMap<Integer, Offer>();
			versionedOffer.put(offer.getVersion(), offer);
			this.offersById.put(offer.getName(), versionedOffer);

			// now.. map the resources to offer
			for (Product product : offer.getProducts()) {
				if (product instanceof AtomicProduct) {
					String resourceName = ((AtomicProduct) product).getResource().getName();
					TreeSet<String> offers = this.offersByResourceName.get(resourceName);
					if (offers == null)
						offers = new TreeSet<String>();
					offers.add(offer.getName());
					this.offersByResourceName.put(resourceName, offers);
				}

				if (product instanceof ProductPackage) {
					for (AtomicProduct atomicProduct : ((ProductPackage) product).getAtomicProducts()) {
						String resourceName = atomicProduct.getResource().getName();
						TreeSet<String> offers = this.offersByResourceName.get(resourceName);
						if (offers == null)
							offers = new TreeSet<String>();
						offers.add(offer.getName());
						this.offersByResourceName.put(resourceName, offers);
					}
				}
			}

			// then deal with federatd catalog - external handles
			Set<String> handles = offer.getExternalHandles();
			for (String handle : handles) {
				this.offersByExternalHandle.put(handle, offer.getName());
			}
		}
	}

	public void updateOffer(Offer offer) throws CatalogException {
		if (offer == null)
			throw new CatalogException("Given Offer was null");

		if (!this.offerExists(offer.getName()))
			throw new CatalogException("Offer: " + offer.getName() + " does not exist to update!!");

		// check the version on offer and the latest version in catalog
		int version2update = offer.getVersion();
		Offer lastKnownOfferRevision = this.getOfferById(offer.getName());
		int versionFromStore = lastKnownOfferRevision.getVersion();

		if (version2update < versionFromStore)
			throw new CatalogException("Version is prepopulated with value: " + version2update + ". Seems like a previous version is being edited . "
					+ "Fucking around with Historic Offers WILL cuase major subscriber issues!!");

		if (version2update > versionFromStore)
			throw new CatalogException("Version is prepopulated with value: " + version2update
					+ ". Seems like a previous version is being edited OR an arbitrary version is set implying a potential hack atttack. "
					+ "Such actions WILL corrupt the persistence beyond repair!!");

		// check if really changes exist, that can qualify for version change...
		// /// check if the edit is validated for sanity
		if (!lastKnownOfferRevision.checkEditSanity(offer))
			throw new CatalogException("Seems like critical core elements of Offer: " + offer.getName()
					+ " are altered drastically to approve as a polymorphic version!! Please create a new Offer with a unique name and try again");

		// /// check if the current offer in store is already published...
		if (lastKnownOfferRevision.getHistory().getPublishedTime() > -1) {
			// /// check if any active subscriptions exist for the latest version....
			try {
				Integer activeSusbriptions = new GetActiveSubscriptionCountForOffer(offer.getName(), version2update).execute();
				if (activeSusbriptions == null)
					throw new CatalogException(
							"Unable to assert no active subscriptions exist for this offer. Please raise a Bug Report to Ericsson!!");

				if (activeSusbriptions >= 1) {
					// need to increment the version..
					offer.setVersion(++versionFromStore);
					offer.setOfferState(State.IN_CREATION);
					if (offer.getHistory() == null)
						offer.setHistory(new OfferLifeCycle());
					offer.getHistory().setCreationTime(System.currentTimeMillis());

				} else {
					// can keep the existing version..
					offer.setVersion(versionFromStore); // technically this dead code since there is no impact on this assignment....
				}
			} catch (FrameworkException e) {
				throw new CatalogException("Unable to assert no active subscriptions exist for this offer. Try later!!", e);
			}
		}

		// creating a transaction context
		synchronized (this) {
			// first manage offer....
			TreeMap<Integer, Offer> versionedOffer = this.offersById.get(offer.getName());
			versionedOffer.put(offer.getVersion(), offer);
			this.offersById.put(offer.getName(), versionedOffer);

			// now.. map the resources to offer
			for (Product product : offer.getProducts()) {
				if (product instanceof AtomicProduct) {
					String resourceName = ((AtomicProduct) product).getResource().getName();
					TreeSet<String> offers = this.offersByResourceName.get(resourceName);
					if (offers == null)
						offers = new TreeSet<String>();
					offers.add(offer.getName());
					this.offersByResourceName.put(resourceName, offers);
				}

				if (product instanceof ProductPackage) {
					for (AtomicProduct atomicProduct : ((ProductPackage) product).getAtomicProducts()) {
						String resourceName = atomicProduct.getResource().getName();
						TreeSet<String> offers = this.offersByResourceName.get(resourceName);
						if (offers == null)
							offers = new TreeSet<String>();
						offers.add(offer.getName());
						this.offersByResourceName.put(resourceName, offers);
					}
				}
			}

			// then deal with federatd catalog - external handles
			Set<String> handles = offer.getExternalHandles();
			for (String handle : handles) {
				this.offersByExternalHandle.put(handle, offer.getName());
			}
		}

	}

	/**
	 * This is purely housekeeping method and must never be called from GUI. Use changeLifeCycle() instead to put the offer in RETIRED
	 * state. Not adhering to this directive, will cause a lot of customer disputes with active subscriptions.
	 * 
	 * @param offerId
	 * @throws CatalogException 
	 */
	@Deprecated
	public void deleteOffer(String offerId) throws CatalogException {
		if (offerId == null)
			throw new CatalogException("given offerId was null");
		
		synchronized (this) {
			TreeMap<Integer, Offer> versionedOffer = this.offersById.get(offerId);
			if (versionedOffer != null) {
				this.offersById.remove(offerId);
			}
			
			if (this.offersByExternalHandle.containsValue(offerId)) {
				for (String handle: this.offersByExternalHandle.descendingKeySet())
					if (this.offersByExternalHandle.get(handle).equals(offerId))
						this.offersByExternalHandle.remove(handle);
			}
			
			if (this.offersByResourceName.containsValue(offerId)){
				for (String resourceName: this.offersByResourceName.descendingKeySet())
					if (this.offersByResourceName.get(resourceName).equals(offerId))
						this.offersByResourceName.remove(resourceName);
			}
		}
		

	}

	public void changeLifeCycle(String offerId, int vesion, State newState) throws CatalogException {
		Offer offer = this.getOfferById(offerId, vesion);
		synchronized (offer) {
			offer.setOfferState(newState);
			if (offer.getHistory() == null)
				offer.setHistory(new OfferLifeCycle());
			offer.getHistory().promote(newState, System.currentTimeMillis());
		}
	}

	public boolean offerExists(String offerId) {
		if (this.offersById.containsKey(offerId))
			return true;
		return false;
	}

	
	public Offer getOfferById(String id) {
		Offer offer = null;
		
		logger.debug("E/// Getting latest version of this offer: " + id);
		TreeMap<Integer, Offer> versionedOffer = this.offersById.get(id);
		logger.debug("E/// How many offers?: " + versionedOffer.size());
		if (versionedOffer != null) {
			int latestVersion = versionedOffer.descendingKeySet().first();
			offer = versionedOffer.get(latestVersion);
			logger.debug("E/// Offer Name: " + offer.getName());
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
		if (offerId != null)
			offer = this.getOfferById(offerId);

		return offer;
	}

	public Set<Offer> getOffersByResource(String resourceName) {
		Set<Offer> offers = new TreeSet<Offer>();

		Set<String> offerNames = this.offersByResourceName.get(resourceName);

		for (String offerId : offerNames)
			offers.add(this.getOfferById(offerId));

		return offers;
	}

	
	
}
