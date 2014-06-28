package com.ericsson.raso.sef.bes.prodcat.entities;

//TODO: add cascaded cancel on terminate
//TODO: sort fulfillment tasks based on resource dependancies

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.Constants;
import com.ericsson.raso.sef.bes.prodcat.ServiceResolver;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleState;
import com.ericsson.raso.sef.bes.prodcat.entities.SubscriptionHistory.HistoryEvent;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.bes.prodcat.tasks.Charging;
import com.ericsson.raso.sef.bes.prodcat.tasks.ChargingMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.FetchSubscriber;
import com.ericsson.raso.sef.bes.prodcat.tasks.Fulfillment;
import com.ericsson.raso.sef.bes.prodcat.tasks.FulfillmentMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.Future;
import com.ericsson.raso.sef.bes.prodcat.tasks.FutureMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.Notification;
import com.ericsson.raso.sef.bes.prodcat.tasks.NotificationMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.Persistence;
import com.ericsson.raso.sef.bes.prodcat.tasks.PersistenceMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;
import com.ericsson.raso.sef.core.CloneHelper;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.db.model.Subscriber;

/**
 * This class represents an instance of {@link Offer} that is attached to subscriber profile. This decoupling allows handling a few complex use-cases...
 * <br><li> Track the subcription's history in its lifecycle such as repetitive suspension, barring, reactivation, etc.
 * <br><li> Create flexibility on switching between related offers, exit offers, versions of the same product.
 * <br><li> Tracking user behavior and produce enablers for analytics such as purchases, consumption, payment behavior & credit risks/rating.
 * @author esatnar
 *
 */
public class Subscription extends Offer {
	private static final long serialVersionUID = 218826816576313417L;
	private static final Logger logger = LoggerFactory.getLogger(Subscription.class);
	
	private String subscriberId = null;
	private String subscriptionId = null;
	private SubscriptionHistory subscriptionHistory = null;
	private PurchaseHistory purchaseHistory = null;
	private Set<AtomicProduct> provisionedProducts = null;
	
	public Subscription(Offer offer) {
		super(offer.getName());
		this.subscriptionId = this.createSusbcriptionId();
		this.setAccumulation(offer.getAccumulation());
		this.setAutoTermination(offer.getAutoTermination());
		this.setDescription(offer.getDescription());
		this.setEligibility(offer.getEligibility());
		this.setExit(offer.getExit());
		this.setHistory(offer.getHistory());
		this.setImmediateTermination(offer.getImmediateTermination());
		this.setCommercial(offer.isCommercial());
		this.setRecurrent(offer.isRecurrent());
		this.setMinimumCommitment(offer.getMinimumCommitment());
		this.setOfferGroup(offer.getOfferGroup());
		this.setOfferState(offer.getOfferState());
		this.setOwner(offer.getOwner());
		this.setPreExiry(offer.getPreExiry());
		this.setPreRenewal(offer.getPreRenewal());
		this.setPrice(offer.getPrice());
		this.setProducts(offer.getProducts());
		this.setSwitching(offer.getSwitching());
		this.setVersion(offer.getVersion());
		this.setWhiteListedUsers(offer.getWhiteListedUsers());
		
		try {
			this.setRenewalPeriod(offer.getRenewalPeriod());
			this.setTrialPeriod(offer.getTrialPeriod());
		} catch (CatalogException e) {
			logger.info("failed setting renewal period and trial period when creating subscription for offer: " + offer.getName());
		}
		
	}
	
	protected List<TransactionTask> subscriptionQuery(String subscriberId, boolean override, Map<String, Object> metas) {
		List<TransactionTask> tasks = new ArrayList<TransactionTask>();
		logger.debug("Going to prepare query tasks for subsriberId: " + subscriberId + ", subcription");
		for (AtomicProduct product: this.getProvisionedProducts()) {
			tasks.add(new Fulfillment(FulfillmentMode.QUERY, product, subscriberId, null));
		}
		return tasks;
	}
	
	
	
	protected List<TransactionTask> preExpiry(String subscriberId, boolean override, Map<String, Object> metas) {
		//TODO: implement this when you have time....
		List<TransactionTask> tasks = new ArrayList<TransactionTask>();
		return tasks;
	}
	
	protected List<TransactionTask> expiry(String subscriberId, boolean override, Map<String, Object> metas) throws CatalogException {
		List<TransactionTask> tasks = new ArrayList<TransactionTask>();
		final long EXPIRY_TIMESTAMP = System.currentTimeMillis();
		
		
		// first, pack all the fulfillment tasks pertinent to deprovisioning
		for(AtomicProduct atomicProduct: this.getProvisionedProducts()) {
			tasks.add(new Fulfillment(FulfillmentMode.CANCEL, atomicProduct, subscriberId, null));
		}

		tasks.add(new Notification(NotificationMode.NOTIFY_USER, this.getName(), subscriberId, SubscriptionLifeCycleEvent.EXPIRY.name(), metas));
				
		// now, pack all tasks related to exit event...
		if (this.getExit() != null) {
			IOfferCatalog catalog = ServiceResolver.getOfferCatalog();
			Offer exitOffer = catalog.getOfferById(this.getExit());
			if (exitOffer == null) {
				logger.info("Exit Offer was defined for expiry, but the relevant offer: " + this.getExit() + " was not found in the Catalog");
			}
			
			if (exitOffer.getOfferState() != State.PUBLISHED) {
				logger.info("Exit Offer was defined for expiry, but the relevant offer: " + this.getExit() + " is not in PUBLISHED state now");
				
			} else {

				try {
					tasks.addAll(exitOffer.purchase(subscriberId, false, metas));
				} catch (CatalogException e) {
					logger.error("Unable to get exit offer provisioning prepared for: " + this.getExit());
				}
			}
		}
		
		
		// save states and history & ask to be saved in DB
		this.subscriptionHistory.add(SubscriptionLifeCycleState.IN_EXPIRY, EXPIRY_TIMESTAMP);
		tasks.add(new Persistence<Subscription>(PersistenceMode.SAVE, this, subscriberId));
		
		return tasks;
	}
	
	protected List<TransactionTask> preRenewal(String subscriberId, boolean override, Map<String, Object> metas) {
		//TODO: implement this when you have time....
		List<TransactionTask> tasks = new ArrayList<TransactionTask>();
		return tasks;
	}
	
	protected List<TransactionTask> renewal(String subscriberId, boolean override, Map<String, Object> metas) throws CatalogException {
		List<TransactionTask> tasks = new ArrayList<TransactionTask>();
		Map<String, Object> context = RequestContextLocalStore.get().getInProcess();
		
		if (this.getOfferState() != State.PUBLISHED) {
			// current subscribed offer is not renewable anymore
			logger.info("This offer is not available for RENEWAAL anymore... Converting to expiry...");
			tasks.addAll(this.expiry(subscriberId, true, metas));
			return tasks;
		} 

		// current subscribed offer is renewable...
		long RENEWAL_TIMESTAMP = System.currentTimeMillis();

		// Get the Subscriber
		Subscriber subscriber = null;
		try {
			subscriber = (Subscriber) context.get(Constants.SUBSCRIBER_ENTITY.name());
			if (subscriber == null) {
				subscriber = new FetchSubscriber(subscriberId).execute();

				if (subscriber == null)
					throw new CatalogException("Unable fetch Subscriber: " + subscriberId + 
							"!! Cannot proceed with renewal... Will cause inconsistencies!!");

				context.put(Constants.SUBSCRIBER_ENTITY.name(), subscriber);
			}
		} catch (FrameworkException e) {
			if (e instanceof CatalogException) 
				throw ((CatalogException) e);
			throw new CatalogException("Failed to fetch Susbcriber: " + subscriber, e);
		}

		// enforce auto-termination....
		if (!override && this.getAutoTermination() != null  && !(this.getAutoTermination().getTerminationTime(this) > RENEWAL_TIMESTAMP)) {
			// its time for auto-termination... covert to expire...
			tasks.addAll(this.expiry(subscriberId, true, metas));
			return tasks;
		}


		// check for additional policies for renewal event
		context.put(Constants.SUBSCRIPTION_EVENT.name(), SubscriptionLifeCycleEvent.RENEWAL);
		if (!override && !this.getEligibility().execute(subscriber, SubscriptionLifeCycleEvent.RENEWAL)) {
			// eligibility failed for this renewal.... convert to expire...
			tasks.addAll(this.expiry(subscriberId, true, metas));
			return tasks;
		}

		// check if this renewal will breach accumulation....
		if (!override && !this.getAccumulation().execute()) {
			// accumulation failed this renewal.... concert to expire...
			tasks.addAll(this.expiry(subscriberId, true, metas));
			return tasks;
		}


		// pretty much start packing up the tasks now....

		//---------- Charging Tasks
		/*
		 * 1. Evaluate Price and create Charging Task
		 */

		if (this.isCommercial()) {
			context.put(Constants.SUBSCRIPTION_EVENT.name(), SubscriptionLifeCycleEvent.RENEWAL);
			MonetaryUnit rate = this.getPrice().getSimpleAdviceOfCharge();
			tasks.add(new Charging(ChargingMode.CHARGE, rate, subscriberId));
			this.addPurchaseHistory(SubscriptionLifeCycleEvent.RENEWAL, RENEWAL_TIMESTAMP, rate);
		}

		//---------- Fulfillment Tasks
		/*
		 * 1. get a list of all Atomic Products
		 * 2. create a list of Fulfillment tasks
		 * 3. if trial period is being set, then adjust the validity period with trial period
		 * 4. add the fulfillment tasks to the transaction tasks
		 */

		this.provisionedProducts.clear();
		for(AtomicProduct atomicProduct: this.getAllAtomicProducts()) {
			AtomicProduct cloned = CloneHelper.deepClone(atomicProduct);
			cloned.getValidity().setActivationTime(RENEWAL_TIMESTAMP);
			this.addProvisionedProduct(cloned);
			tasks.add(new Fulfillment(FulfillmentMode.FULFILL, cloned, subscriberId, null));
		} 
	
		this.setActivatedTimeNow();
		tasks.add(new Future(FutureMode.SCHEDULE, SubscriptionLifeCycleEvent.RENEWAL, this.subscriptionId, subscriberId, this.getRenewalPeriod().getExpiryTimeInMillis(), metas));
		
		//----------- Notification Tasks
		/*
		 * 1. Send Notification for each state of the Request Processing
		 * 2. Transaction Engine must be able to use this task persistently across the entire process... 
		 */
		tasks.add(new Notification(NotificationMode.NOTIFY_USER, this.getName(), subscriberId, SubscriptionLifeCycleEvent.RENEWAL.name(), metas));

		// finally save this transaction to DB...
		this.addSubscriptionHistory(SubscriptionLifeCycleState.IN_RENEWAL, RENEWAL_TIMESTAMP);
		tasks.add(new Persistence<Subscription>(PersistenceMode.SAVE, this, subscriberId));
		
		
		return tasks;
	}
	
	protected List<TransactionTask> terminate(String subscriberId, boolean override, Map<String, Object> metas) throws CatalogException {
		List<TransactionTask> tasks = new ArrayList<TransactionTask>();
		Map<String, Object> context = RequestContextLocalStore.get().getInProcess();
		
		final long TERMINATE_TIMESTAMP = System.currentTimeMillis();
		context.put(Constants.MINIMUM_COMMITMENT_BREACH.name(), true);
		
		// check for minimum commitment...
		if (this.getMinimumCommitment() != null) {
			if (!(this.getMinimumCommitment() instanceof NoCommitment)) {
				long commitmentTime = this.getMinimumCommitment().getCommitmentTime(this); 
				if ( commitmentTime > TERMINATE_TIMESTAMP ) {
					// minimum commitment breached.... set the context for penalty calculation...
					if (!override) {
						throw new CatalogException("Minimum Commitment Period (" + (new Date(commitmentTime)).toString() + ") is still Active!!");
					}
				}
			}
		}
		
		// Try to get subscriber instance...
		Subscriber subscriber = null;
		try {
			subscriber = (Subscriber) context.get(Constants.SUBSCRIBER_ENTITY.name());
			if (subscriber == null) {
				subscriber = new FetchSubscriber(subscriberId).execute();

				if (subscriber == null)
					throw new CatalogException("Unable fetch Subscriber: " + subscriberId + 
							"!! Cannot proceed with renewal... Will cause inconsistencies!!");

				context.put(Constants.SUBSCRIBER_ENTITY.name(), subscriber);
			}
		} catch (FrameworkException e) {
			if (e instanceof CatalogException) 
				throw ((CatalogException) e);
			throw new CatalogException("Failed to fetch Susbcriber: " + subscriber, e);
		}


		// check for additional policies in terminate...
		if (!override && !this.getEligibility().execute(subscriber, SubscriptionLifeCycleEvent.TERMINATE)) {
			// eligibility failed for this renewal.... convert to expire...
			throw new CatalogException("Terminate related policies rejected the request!!");
		}

		// pretty much start packing up the tasks now....

		//---------- Charging Tasks
		/*
		 * 1. Evaluate Penalties and create Charging Task
		 */

		context.put(Constants.SUBSCRIPTION_EVENT.name(), SubscriptionLifeCycleEvent.RENEWAL);
		Map<String, MonetaryUnit> penalties = this.getPrice().getPrintableAdviceOfCharge();
		
		MonetaryUnit minimumCommitmentPenalty = penalties.get(Constants.MINIMUM_COMMITMENT_BREACH.name());
		if (minimumCommitmentPenalty != null) { 
			tasks.add(new Charging(ChargingMode.CHARGE, minimumCommitmentPenalty, subscriberId));
			this.addPurchaseHistory(SubscriptionLifeCycleEvent.TERMINATE, TERMINATE_TIMESTAMP, minimumCommitmentPenalty);
		}

		MonetaryUnit terminationPenalty = penalties.get(Constants.MINIMUM_COMMITMENT_BREACH.name());
		if (terminationPenalty != null) {
			tasks.add(new Charging(ChargingMode.CHARGE, terminationPenalty, subscriberId));
			this.addPurchaseHistory(SubscriptionLifeCycleEvent.TERMINATE, TERMINATE_TIMESTAMP, terminationPenalty);
		}


		//---------- Fulfillment Tasks
		/*
		 * 1. get a list of all Atomic Products
		 * 2. create a list of Fulfillment tasks
		 * 3. add the fulfillment tasks to the transaction tasks
		 */
		// first, pack all the fulfillment tasks pertinent to deprovisioning
		for(AtomicProduct atomicProduct: this.getProvisionedProducts()) {
			tasks.add(new Fulfillment(FulfillmentMode.CANCEL, atomicProduct, subscriberId, null));
		}
		
		tasks.add(new Notification(NotificationMode.NOTIFY_USER, this.getName(), subscriberId, SubscriptionLifeCycleEvent.TERMINATE.name(), metas));
		
		// save states and history & ask to be saved in DB
		this.subscriptionHistory.add(SubscriptionLifeCycleState.IN_TERMINATION, TERMINATE_TIMESTAMP);
		tasks.add(new Persistence<Subscription>(PersistenceMode.SAVE, this, subscriberId));
				
		return tasks;
	}
	

	
	private String createSusbcriptionId() {
		long sysTime = System.nanoTime();
		return this.getName() + this.getVersion() + (sysTime & 0x0000000000ffffff) + ((sysTime & 0xffffff0000000000L) >> 80);
	}
	
	public void addSubscriptionHistory(HistoryEvent historyEvent) {
		if (this.subscriptionHistory == null)
			this.subscriptionHistory = new SubscriptionHistory();
		this.subscriptionHistory.add(historyEvent);
	}


	public void addSubscriptionHistory(SubscriptionLifeCycleState state, long timestamp) {
		if (this.subscriptionHistory == null)
			this.subscriptionHistory = new SubscriptionHistory();
		this.subscriptionHistory.add(state, timestamp);
	}


	public void addPurchaseHistory(com.ericsson.raso.sef.bes.prodcat.entities.PurchaseHistory.HistoryEvent historyEvent) {
		if (this.purchaseHistory == null)
			this.purchaseHistory = new PurchaseHistory();
		this.purchaseHistory.add(historyEvent);
	}


	public void addPurchaseHistory(SubscriptionLifeCycleEvent event, long timestamp, MonetaryUnit charge) {
		if (this.purchaseHistory == null)
			this.purchaseHistory = new PurchaseHistory();
		this.purchaseHistory.add(event, timestamp, charge);
	}

	public void addProvisionedProduct(AtomicProduct product) throws CatalogException {
		if (product == null)
			throw new CatalogException("Given Product was null");

		if (this.provisionedProducts == null)
			this.provisionedProducts = new TreeSet<AtomicProduct>();

		this.provisionedProducts.add(product);
	}

	public void removeProvisionedProduct(AtomicProduct product) throws CatalogException {
		if (product == null)
			throw new CatalogException("Given Product was null");

		if (this.provisionedProducts != null)
			this.provisionedProducts.add(product);
	}

	public Set<AtomicProduct> getProvisionedProducts() {
		return this.provisionedProducts;
	}

	public void addProvisionedProducts(Set<AtomicProduct> products) {
		this.provisionedProducts.addAll(products);
	}
	


	
	
	public String getSubscriberId() {
		return subscriberId;
	}
	
	

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public SubscriptionHistory getSubscriptionHistory() {
		return subscriptionHistory;
	}
	
	public void setActivationRequestTimeNow() {
		subscriptionHistory.add(SubscriptionLifeCycleState.IN_ACTIVATION, System.currentTimeMillis()); 
	}
	
	public void setActivationRequestTimeFuture(long timestamp) {
		subscriptionHistory.add(SubscriptionLifeCycleState.IN_ACTIVATION, timestamp); 
	}
	
	public void setActivatedTimeNow() {
		subscriptionHistory.add(SubscriptionLifeCycleState.ACTIVE, System.currentTimeMillis()); 
	}
	
	public void setActivatedTimeFuture(long timestamp) {
		subscriptionHistory.add(SubscriptionLifeCycleState.ACTIVE, timestamp); 
	}
	
	public void setBarringSuspensionTimeNow() {
		subscriptionHistory.add(SubscriptionLifeCycleState.BARRING_SUSPENSION, System.currentTimeMillis()); 
	}
	
	public void setBarringSuspensionTimeFuture(long timestamp) {
		subscriptionHistory.add(SubscriptionLifeCycleState.BARRING_SUSPENSION, timestamp); 
	}
	
	public void setRenewalSuspensionTimeNow() {
		subscriptionHistory.add(SubscriptionLifeCycleState.RENEWAL_SUSPENSION, System.currentTimeMillis()); 
	}
	
	public void setRenewalSuspensionTimeFuture(long timestamp) {
		subscriptionHistory.add(SubscriptionLifeCycleState.RENEWAL_SUCCESS, timestamp); 
	}
	
	public void setRenewalSuccessTimeNow() {
		subscriptionHistory.add(SubscriptionLifeCycleState.RENEWAL_SUCCESS, System.currentTimeMillis()); 
	}
	
	public void setRenewalSuccessTimeFuture(long timestamp) {
		subscriptionHistory.add(SubscriptionLifeCycleState.RENEWAL_SUSPENSION, timestamp); 
	}
	
	public void setClosedTimeNow() {
		subscriptionHistory.add(SubscriptionLifeCycleState.CLOSED, System.currentTimeMillis()); 
	}
	
	public void setClosedTimeFuture(long timestamp) {
		subscriptionHistory.add(SubscriptionLifeCycleState.CLOSED, timestamp); 
	}

	@Override
	public String toString() {
		return "Subscription [subscriberId=" + subscriberId + ", subscriptionId=" + subscriptionId + ", subscriptionHistory="
				+ subscriptionHistory + ", purchaseHistory=" + purchaseHistory + ", provisionedProducts=" + provisionedProducts + "]";
	}
	
	
	

}
