package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.CloneHelper;
import com.ericsson.raso.sef.bes.prodcat.Constants;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleState;
import com.ericsson.raso.sef.bes.prodcat.policies.AbstractAccumulationPolicy;
import com.ericsson.raso.sef.bes.prodcat.policies.AbstractSwitchPolicy;
import com.ericsson.raso.sef.bes.prodcat.tasks.Charging;
import com.ericsson.raso.sef.bes.prodcat.tasks.ChargingMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.FetchSubscription;
import com.ericsson.raso.sef.bes.prodcat.tasks.Fulfillment;
import com.ericsson.raso.sef.bes.prodcat.tasks.FulfillmentMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.Future;
import com.ericsson.raso.sef.bes.prodcat.tasks.FutureMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.HasSubscribedToThisOfferEver;
import com.ericsson.raso.sef.bes.prodcat.tasks.Notification;
import com.ericsson.raso.sef.bes.prodcat.tasks.NotificationMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.Persistence;
import com.ericsson.raso.sef.bes.prodcat.tasks.PersistenceMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.RequestContext;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.db.model.Subscriber;


//TODO: add dependency check on purchase
//TODO: sort fulfillment tasks based on resource dependancies
//TODO: check suppress fulfilment is configured before building the transaction tasks
public class Offer implements Serializable {
	private static final long serialVersionUID = 5704479496440496263L;
	private static final Logger LOGGER = LoggerFactory.getLogger(Offer.class);
	
	
	//==========----------------------------- Attributes Section ---------------====================================================================/

	/**
	 * Uniquely identifies a product (Offer) within the product catalog.
	 */
	private String name = null;
	
	/**
	 * A more descriptive text relating the Offer to the end-user
	 */
	private String description = null;

	/**
	 * List of external identifiers to this product (Offer)
	 */
	private Set<String> externalHandles = null;

	/**
	 * version of this product. A version is revised/incremented only when its state is published to discovery and it has atleast one valid
	 * active subscription that refers to this product for its subscription lifecycle.
	 */
	private int version = -1;

	/**
	 * indicates the state of the offer... this state will automatically be reset based on end-user interaction, timeline, automatic
	 * execution...
	 * 
	 * @see {@link State}
	 */
	private State offerState = State.IN_CREATION;

	/**
	 * indicates if this product (Offer) is recurrent... indicating there will be an auto-renewal at the end of every validity period.
	 */
	private boolean isRecurrent = false;

	/**
	 * Specifies the period of validity after which the subscription is auto-renewed.
	 */
	private AbstractTimeCharacteristic renewalPeriod = null;

	/**
	 * Specifies a trial period for this product (Offer) after which the product is charged and moved to subscription mode.
	 * 
	 * If this produt was ever subscribed to in the history, then the user can never access trial period again in his life-cycle.
	 */
	private AbstractTimeCharacteristic trialPeriod = null;

	/**
	 * Specifies if immediate termination is allowed for each subscriber type.
	 * 
	 * Typically, request for termination will be handled as cancel renewal and allow the user to consume the rest of his validity period.
	 */
	private ImmediateTermination immediateTermination = null;
	
	/**
	 * Specifies if an auto-termination policy needs to be applied to the subscription and its lifecycle. The following are supported...<br>
	 * <li> No Termination - This is the default is no termination policy is defined.
	 * <li> After 'n' renewals
	 * <li> After 'n' days from Purchase/ Activation
	 * <li> Hard Stop - Exact Date on which this subscription will auto-terminate irrespective of purchase/ activation date.
	 */
	private AbstractAutoTermination autoTermination = null;
	
	/**
	 * Specifies if minimum commitment policy needs to be applied to the subscription and its lifecycle. The following are supported...<br>
	 * <li> No Commitment - This is the default is no termination policy is defined.
	 * <li> Until 'n' renewals
	 * <li> Until 'n' days from Purchase/ Activation
	 * <li> Hard Limit - Exact Date until which this subscription cannot terminate irrespective of purchase/ activation date.
	 */
	private AbstractMinimumCommitment minimumCommitment = null;
	


	/**
	 * An Offer can be made up of one or more AtomicProduct thus creating a polymorphic Commercial Offer or Workflow Package. Such forms can
	 * be <br> 
	 * 
	 * 
	 * <li>Bundle - A set of {@link AtomicProduct} that must be provisioned collectively or not at all<br> 
	 * 
	 * <li>Package - A mixed set of {@link AtomicProduct} and {@link TechnicalProduct} to create a hierarchy of services that can be assigned or purchased. Such feature
	 * setup allows ease of administration and very high end-user experience, specially targeted at high-arpu subscribers.<br> 
	 * 
	 * <li>Promotion - A mixed set of {@link AtomicProduct} and {@link TechnicalProduct} but conditionally provisioned for the same {@link Price}<br> 
	 * 
	 * <li> Entitlement - A variant of Promotion, where an Offer is either assigned to or purchased by a subscriber but the validity period
	 * starts only at the first access/ consumption to the service.<br> 
	 * 
	 * <li>Subscription - All forms of Offer is a subscription when attached to a subscriber's profile. From this perspective, the Offer's life-cycle is also governed by the subscription life-cycle. For example,
	 * you cannot retire a product until all of its subscriptions are lived out or an exit product change is defined.
	 */
	private Set<Product> products = null;
		
	/**
	 * Indicates if this Offer is a commecial product or a workflow pertinent to assignment or fulfillment profile orchestration supporting a use-case.
	 */
	private boolean isCommercial = true;

	/**
	 * Identifies who is the owner for this resource. From info security context, this concept will allow a stakeholder to access all of his
	 * own resources as well as resources for all stakeholders chained under the current level.
	 */
	private Owner owner = null;

	/**
	 * Identifies a Offer Group to which this offer belongs. Each offer in a group is assigned a unique priority that determines if a
	 * 'switch' request is an upgrade or downgrade. This priority overrides auto-calculated weightage that will be used to determine
	 * upgrade/ downgrade. In the absence of this value, then the system auto-calculates the weightage using Characteristics - Quota,
	 * Validity, SLA & Resource Priority.
	 */
	private String offerGroup = null;

	/**
	 * All Pricing, Rating and Taxation components required for Advice of Charge and Charging on Purchase are encapsulated here.
	 */
	private Price price = null;

	/**
	 * A set of visibility, availability & eligibility rules that determine if this offer is allowed for discovery and purchase and renewal to a subscriber.
	 */
	private PolicyTree eligibility = null;

	/**
	 * A set of Accumulation rules that determine if it is allowed for accumulation of services/ resources.
	 */
	private AbstractAccumulationPolicy accumulation = null;

	/**
	 * A set of Switch rules that determine if it is allowed for upgrading or downgrade of services/ resources.
	 */
	private AbstractSwitchPolicy switching = null;
	
	/**
	 * A list of users that are allowed to discover, purchase, avail and subscribe to this offer in TESTING state.
	 */
	private Set<String> whiteListedUsers = null;
	
	/**
	 * Specifies an exit Offer to shift the subscriber, when the subscription runs out or if the offer is retired.
	 */
	private String exitOfferId = null;
	
	/**
	 * Implicit audit trail of Offer Lifecycle dates to track history and manage versioning.
	 */
	private OfferLifeCycle history = null;

	/**
	 * pre-orchestrated event actions for pre-exipry period
	 */
	private EventProfile preExiry = null;
	
	/**
	 * pre-orchestrated event actions for pre-renewal period
	 */
	private EventProfile preRenewal = null;
	
	//==========----------------------------- End of Attributes Section ---------------==============================================================/
	
	//==========----------------------------- Functional Section ---------------=====================================================================/
	public Offer(String name) {
		this.name = name;
		this.history = new OfferLifeCycle();
		this.history.setCreationTime(System.currentTimeMillis());
	}
	
	public List<Resource> getAllResources() {
		List<Resource> resources = null;
		
		if (this.products != null) {
			resources = new ArrayList<Resource>();
			
			for (Product product: this.products) {
				if (product instanceof AtomicProduct)
					resources.add(((AtomicProduct) product).getResource());
				
				if (product instanceof ProductPackage) 
					for (AtomicProduct atomicProduct: ((ProductPackage) product).getAtomicProducts()) 
						resources.add(atomicProduct.getResource());
			}
		}
		return resources;
	}
	
	public long getCumulativeQuotaForResource(String resourceName) {
		long cumulativeQuota = 0;
		
		if (this.products != null) {
			for (Product product: this.products) {
				if (product instanceof AtomicProduct) {
					Resource resource = ((AtomicProduct)product).getResource(); 
					if (resource.getName().equals(resourceName)) {
						if (resource.isConsumable()) {
							cumulativeQuota += ((AtomicProduct) product).getQuota().getDefinedQuota();
						}
					}
				}
				if (product instanceof ProductPackage) { 
					for (AtomicProduct atomicProduct: ((ProductPackage) product).getAtomicProducts()) {
						Resource resource = atomicProduct.getResource(); 
						if (resource.getName().equals(resourceName)) {
							if (resource.isConsumable()) {
								cumulativeQuota += atomicProduct.getQuota().getDefinedQuota();
							}
						}
					}
				}
			}
		}
		
		return cumulativeQuota;
	}
	
	public List<TransactionTask> execute(String subscriber, SubscriptionLifeCycleEvent event, boolean override, Map<String, Object> metas) throws CatalogException {

		String subscriptionId = null;
		Subscription subscription = null;
		
		if (metas == null || metas.isEmpty()) {
			if (event != SubscriptionLifeCycleEvent.PURCHASE && event != SubscriptionLifeCycleEvent.DISCOVERY) {
				LOGGER.info("metas were not available and event[" + event + "] found. Cannot proceed without inputs!!");
				throw new CatalogException("metas were not available and event[" + event + "] found. Cannot proceed without inputs!!");
			} 
		}
		
		
		if (metas != null)
			subscriptionId = (String) metas.get(Constants.SUBSCRIPTION_ID.name());
		
		if (subscriptionId == null) {
			if (event != SubscriptionLifeCycleEvent.PURCHASE && event != SubscriptionLifeCycleEvent.DISCOVERY) {
				LOGGER.info("subscriptionId is not populated in a subscription event[" + event + "]. Cannot proceed with assumptions");
				throw new CatalogException("Subscription Identifier was null!!");
			}
		} else {
			try {
				subscription = new FetchSubscription(subscriptionId).execute();
				
				//TODO: this is a temporary fix until subcription entity model is available....
				if (subscription == null) {
					subscription = new Subscription(this);
				}
				metas.put(Constants.SUBSCRIPTION_ENTITY.name(), subscription);
			} catch (FrameworkException e) {
				if (e instanceof CatalogException)
					throw ((CatalogException) e);
				throw new CatalogException("Unable to fetch subscription for: " + subscriptionId, e);
			}
		}


		
		switch (event) {
			case DISCOVERY:
				// If an offer is already executing, then discovery is complete. Assuming to be a query on subscription status and consumption; or it must be a Subscriber Workflow
				if (subscriptionId == null)
					return this.query(subscriber, metas);
				else
					return subscription.subscriptionQuery(subscriber, override, metas);
			case PURCHASE:
				return this.purchase(subscriber, override, metas);
			case PRE_EXPIRY:
				return subscription.preExpiry(subscriber, override, metas);
			case EXPIRY:
				return subscription.expiry(subscriber, override, metas);
			case PRE_RENEWAL:
				return subscription.preRenewal(subscriber, override, metas);
			case RENEWAL:
				return subscription.renewal(subscriber, override, metas);
			case TERMINATE:
				return subscription.terminate(subscriber, override, metas);
		}
		return null;
	}
	
	public Subscription createPurchaseSubscription(String subscriberId, long timestamp) {
		Offer clone = CloneHelper.deepClone(this);
		Subscription purchaseSubscription = new Subscription(clone);
		purchaseSubscription.setSubscriberId(subscriberId);
		purchaseSubscription.addSubscriptionHistory(SubscriptionLifeCycleState.IN_ACTIVATION, timestamp);
		LOGGER.debug("Subscription Purchase created: " + purchaseSubscription);
		return purchaseSubscription;
		
	}
	
	public void validate(boolean suppressWarnings) throws CatalogException {
		StringBuilder problems = new StringBuilder();
		
		if (this.description == null || this.description.isEmpty())
			if (!suppressWarnings)
				problems.append("*WARNING* Description of Offer is not defined to allow any discovery\n");
		
		if (this.externalHandles == null || this.externalHandles.isEmpty())
			if (!suppressWarnings)
				problems.append("*WARNING* External Handles to this Offer is not defined to allow any discovery\n");
		
		if (this.isRecurrent && this.renewalPeriod instanceof InfiniteTime)
			problems.append("**ERROR** Recurrence is enabled but validity period is infnite!!\n");
		
		if (this.trialPeriod != null) {
			if (this.trialPeriod instanceof InfiniteTime)
				throw new CatalogException("Trial Period cannot be inifite!!");
			
			if (!(this.renewalPeriod instanceof InfiniteTime)) {
				if (this.trialPeriod.getExpiryTimeInMillis() >=  this.renewalPeriod.getExpiryTimeInMillis())
					throw new CatalogException("Trial Period cannot be longer than Offer Validity Period");
			}
		}
		
		if (this.immediateTermination == null)
			if (!suppressWarnings)
				problems.append("*WARNING* Immediate Termination is not set. Assuming to be NOT ALLOWED.\n");
		
		if (this.products == null || this.products.isEmpty())
			problems.append("**ERROR** There are no Products listed in this Offer!!!\n");
			
		if (this.price == null && this.isCommercial)
			problems.append("**ERROR** The Offer is declared commercial but no Price elements set!!!\n");
			
		if (this.eligibility == null || this.eligibility.isEmpty())
			if (!suppressWarnings)
				problems.append("*WARNING* Eligibility Rules are not set. Assuming open discovery.\n");
			
		if (this.exitOfferId == null || this.isRecurrent)
			if (!suppressWarnings)
				problems.append("*WARNING* Offer is not recurrent & Exit Offer is not specified.\n");
			
		if (this.history == null)
			problems.append("**ERROR** History is not initialized. Lifecycle is impossible!!.\n");
			
		String validityMessage = problems.toString();
		if (validityMessage.isEmpty())
			return;
		
		if (suppressWarnings && !validityMessage.contains("ERROR"))
			return;
		
		throw new CatalogException(problems.toString());
			
	}

	public boolean isVariant(Offer other) {
		if (other == null)
			return false;
		
		if (!this.name.equalsIgnoreCase(other.name))
			return false;
		
		if (this.version != other.version)
			return false;
		
		
		Set<Product> myProducts = this.getProducts();
		Set<Product> otherProducts = this.getProducts();
		int match = 0;
		
		if (myProducts.size() <= otherProducts.size()) {			
			for (Product otherProduct: otherProducts)
				for (Product myProduct: myProducts)
					if (otherProduct.equals(myProduct))
						match++;
			return (match == myProducts.size()) ? true : false;
		} else {
			for (Product myProduct: myProducts)
				for (Product otherProduct: otherProducts)
					if (myProduct.equals(otherProduct))
						match++;
			return (match == otherProducts.size()) ? true : false;
		}		
	}
	
	public boolean checkEditSanity(Offer other) {
		if (other == null)
			return false;
		
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		
		if (history == null) {
			if (other.history != null)
				return false;
		} else if (!history.equals(other.history))
			return false;
		
		if (isCommercial != other.isCommercial)
			return false;
		
		if (isRecurrent != other.isRecurrent)
			return false;
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (offerState != other.offerState)
			return false;
		
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		
		if (renewalPeriod == null) {
			if (other.renewalPeriod != null)
				return false;
		} else if (!renewalPeriod.equals(other.renewalPeriod))
			return false;
		
		if (trialPeriod == null) {
			if (other.trialPeriod != null)
				return false;
		} else if (!trialPeriod.equals(other.trialPeriod))
			return false;
		
		return true;
	}
	
	protected List<TransactionTask> query(String subscriberId, Map<String, Object> metas) {
		List<TransactionTask> tasks = new ArrayList<TransactionTask>();
		Map<String, Object> context = RequestContextLocalStore.get().getInProcess();
		
		
		context.putAll(metas);
		LOGGER.debug("Trying to add CharingStep Task......");
		if (this.isCommercial) 
		{
			LOGGER.debug("Yes commercial offer......");
			MonetaryUnit rate = this.price.getSimpleAdviceOfCharge();
			if (rate.getAmount() != 0) {
				LOGGER.debug("Rated AMount: " + rate.getAmount() + rate.getIso4217CurrencyCode());
				tasks.add(new Charging(ChargingMode.CHARGE, rate, subscriberId, metas));
			} else {
				LOGGER.debug("Rated amount was zero... not sending charging request...");
			}
			
		}else{
			LOGGER.debug("ChargingStep Task not added");
		}
		
		for (AtomicProduct product: this.getAllAtomicProducts()) {
			tasks.add(new Fulfillment(FulfillmentMode.QUERY, product, subscriberId, metas));
		}
		return tasks;
	}
	
	
	
	protected List<TransactionTask> purchase(String subscriberId, boolean override, Map<String, Object> metas) throws CatalogException {
		LOGGER.debug(".....inside purchase method....");
		long PURCHASE_TIMESTAMP = System.currentTimeMillis();
		
		List<TransactionTask> tasks = new ArrayList<TransactionTask>();
		
		Map<String, Object> context = RequestContextLocalStore.get().getInProcess();
		context.put(Constants.SUBSCRIPTION_EVENT.name(), SubscriptionLifeCycleEvent.PURCHASE);
		
		Subscription purchase = this.createPurchaseSubscription(subscriberId, PURCHASE_TIMESTAMP);

		
		// check for offer state first
		if (this.offerState != State.PUBLISHED) {
			if (this.offerState == State.TESTING) {
				if (!this.whiteListedUsers.contains(subscriberId)) {
					LOGGER.error("Offer is not published and subscriber is not whitelisted for testing!!");
					throw new CatalogException("Offer is not published and subscriber is not whitelisted for testing!!");
				}
			}
		}
		
		// Fetch the subscriber entity
		Subscriber subscriber = null;	
//		try {
//			subscriber = new FetchSubscriber(subscriberId).execute();
//			context.put(Constants.SUBSCRIBER_ENTITY.name(), subscriber);
//			
//		} catch (FrameworkException e) {
//			if (e instanceof CatalogException)
//				throw (CatalogException) e;
//			throw new CatalogException("Unable to fetch Subscriber!", e);
//		}
	
		// check for eligibility
		if (this.eligibility != null && !this.eligibility.execute(subscriber, SubscriptionLifeCycleEvent.PURCHASE)) {
			Long schedule = (Long) context.get(Constants.FUTURE_SCHEDULE);
			if (schedule == null) {
				if (!override) {
					LOGGER.error("Eligibility Policies rejected processing purchase/ service order request!!");
					throw new CatalogException("Eligibility Policies rejected processing purchase/ service order request!!");
				} else { 
					//TODO: Logger - log the exception you were about to throw
				}
			}
			tasks.add(new Future(FutureMode.SCHEDULE, SubscriptionLifeCycleEvent.PURCHASE, this.name, subscriberId, schedule, metas));
			context.remove(Constants.FUTURE_SCHEDULE);
		}
		
		// check for accumulation policies
		if (this.accumulation != null && !this.accumulation.execute()) {
			Long schedule = (Long) context.get(Constants.FUTURE_SCHEDULE);
			if (schedule == null)
				if (!override) {
					LOGGER.error("Accumulation Policies rejected processing purchase/ service order request!!");
					throw new CatalogException("Accumulation Policies rejected processing purchase/ service order request!!");
				} else { 
					//TODO: Logger - log the exception you were about to throw
				}
			tasks.add(new Future(FutureMode.SCHEDULE, SubscriptionLifeCycleEvent.PURCHASE, this.name, subscriberId, schedule, metas));
			context.remove(Constants.FUTURE_SCHEDULE);
		}		
		// check for switch policies
		if (this.switching != null && this.switching.execute()) {
			Long schedule = (Long) context.get(Constants.FUTURE_SCHEDULE);
			if (schedule == null)
				if (!override) {
					LOGGER.error("Switching Policies rejected processing purchase/ service order request!!");
					throw new CatalogException("Switching Policies rejected processing purchase/ service order request!!");
				} else { 
					//TODO: Logger - log the exception you were about to throw
				}
			tasks.add(new Future(FutureMode.SCHEDULE, SubscriptionLifeCycleEvent.PURCHASE, this.name, subscriberId, schedule, metas));
			context.remove(Constants.FUTURE_SCHEDULE);
		}
		
		// trial period
		boolean isTrialAllowed = true;
		try {
			if (this.trialPeriod != null) {
				if ( new HasSubscribedToThisOfferEver(subscriberId, this.name).execute()) {
					isTrialAllowed = false;
				}
			} else
				isTrialAllowed = false;
		} catch (FrameworkException e) {
			LOGGER.error("Unable to verify if this user has already availed trial!!", e);
			if (e instanceof CatalogException)
				throw ((CatalogException)e);
			throw new CatalogException("Unable to verify if this user has already availed trial!!", e);
		}
		
		
		// pretty much start packing up the tasks now....
		
		//---------- Charging Tasks
		/*
		 * 1. Evaluate Price and create Charging Task
		 */
		context.putAll(metas);
		LOGGER.debug("Trying to add CharingStep Task......");
		if (this.isCommercial && !isTrialAllowed) 
		{
			LOGGER.debug("Yes commercial offer......");
			MonetaryUnit rate = this.price.getSimpleAdviceOfCharge();
			if (rate.getAmount() != 0) {
				LOGGER.debug("Rated AMount: " + rate.getAmount() + rate.getIso4217CurrencyCode());
				tasks.add(new Charging(ChargingMode.CHARGE, rate, subscriberId, metas));
			} else {
				LOGGER.debug("Rated amount was zero... not sending charging request...");
			}
			
			LOGGER.debug("Adding purchase history for charging...");
			purchase.addPurchaseHistory(SubscriptionLifeCycleEvent.PURCHASE, PURCHASE_TIMESTAMP, rate);
		}else{
			LOGGER.debug("ChargingStep Task not added");
		}
				
		//---------- Fulfillment Tasks
		/*
		 * 1. get a list of all Atomic Products
		 * 2. create a list of Fulfillment tasks
		 * 3. if trial period is being set, then adjust the validity period with trial period
		 * 4. add the fulfillment tasks to the transaction tasks
		 */
		
		for(AtomicProduct atomicProduct: this.getAllAtomicProducts()) {
			//if (atomicProduct.getResource().isDiscoverable()) {
				AtomicProduct clone = CloneHelper.deepClone(atomicProduct);
				if (isTrialAllowed) {
					// If trial period, then ignore the defined validity and reset the validity to trialPeriod.
					AbstractTimeCharacteristic trialPeriod = CloneHelper.deepClone(this.trialPeriod);
					trialPeriod.setActivationTime(PURCHASE_TIMESTAMP);
					clone.setValidity(trialPeriod);
					purchase.addProduct(clone);
				} 
				
				LOGGER.debug("Adding a Fulfillment for: " + subscriberId + " with: " + clone);
				tasks.add(new Fulfillment(FulfillmentMode.FULFILL, clone, subscriberId, metas));
			//}
		}
		
		if (isTrialAllowed) {
			// Make the scheduler call this offer at the end of trial period...
			tasks.add(new Future(FutureMode.SCHEDULE, SubscriptionLifeCycleEvent.RENEWAL, purchase.getSubscriptionId(), subscriberId, this.trialPeriod.getExpiryTimeInMillis(), metas));
		} else {
			if (this.isRecurrent) {
				tasks.add(new Future(FutureMode.SCHEDULE, SubscriptionLifeCycleEvent.RENEWAL, purchase.getSubscriptionId(), subscriberId, purchase.getRenewalPeriod().getExpiryTimeInMillis(), metas));
			} else {
				if (!(this.renewalPeriod instanceof InfiniteTime))
					tasks.add(new Future(FutureMode.SCHEDULE, SubscriptionLifeCycleEvent.EXPIRY, purchase.getSubscriptionId(), subscriberId, purchase.getRenewalPeriod().getExpiryTimeInMillis(), metas));
			}
		}
		
		//----------- Notification Tasks
		/*
		 * 1. Send Notification for each state of the Request Processing
		 * 2. Transaction Engine must be able to use this task persistently across the entire process... 
		 */
		tasks.add(new Notification(NotificationMode.NOTIFY_USER, this.name, subscriberId, SubscriptionLifeCycleEvent.PURCHASE.name(), metas));
		
		// finally save this transaction to DB...
		if (this.isCommercial)
			tasks.add(new Persistence<Subscription>(PersistenceMode.SAVE, purchase, subscriberId));
		
		return tasks;
	}
	
	
	public List<AtomicProduct> getAllAtomicProducts() {
		List<AtomicProduct> atomicProducts = new ArrayList<AtomicProduct>();
		
		for (Product product: this.products) {
			if (product instanceof AtomicProduct)
				atomicProducts.add((AtomicProduct)product);
			
			if (product instanceof ProductPackage)
				atomicProducts.addAll(((ProductPackage)product).getAtomicProducts());
		}
		
		return atomicProducts;
	}

	
	
	//==========----------------------------- End of Functional Section ---------------==============================================================/
	
	//==========----------------------------- Java Niceties Section ---------------==================================================================/
	
	/**
	 * Adds an identifier to this product (Offer) to relate to an external discovery/ synchronized/ federated system.
	 * 
	 * @param handle
	 *            - identifier to this product in an external system.
	 */
	public void addExternalHandle(String handle) {
		if (handle == null)
			return;

		if (this.externalHandles == null)
			this.externalHandles = new TreeSet<String>();

		this.externalHandles.add(handle); // since String is immutable, it is ok to attempt duplicate since this will be replaced anyway.
	}

	/**
	 * Removes an identifier to this product (Offer) that relates to an external discovery/ synchronized/ federated system.
	 * 
	 * @param handle
	 *            - identifier to this product in an external system.
	 */
	public void removeExternalHandle(String handle) {
		if (handle == null)
			return;

		if (this.externalHandles != null)
			this.externalHandles.remove(handle);

	}

	/**
	 * Returns a list of external identifiers related to this product.
	 * 
	 * @return List<String>
	 */
	public Set<String> getExternalHandles() {
		return this.externalHandles;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setOfferState(State state) {
		if (state == null)
			return;

		if (this.offerState.getNextAllowedTransitions().contains(state))
			this.offerState = state;
	}

	public State getOfferState() {
		return offerState;
	}

	public boolean isRecurrent() {
		return isRecurrent;
	}

	public void setRecurrent(boolean isRecurrent) {
		this.isRecurrent = isRecurrent;
	}

	public void setRenewalPeriod(AbstractTimeCharacteristic period) throws CatalogException {
		if (period == null)
			throw new CatalogException("Given Renewal Period was null");

		if (period instanceof HardDateTime)
			throw new CatalogException("HardDate is not acceptable for Renewal Period. Must be Hours or Days");

		this.renewalPeriod = period;
	}

	public AbstractTimeCharacteristic getRenewalPeriod() {
		return renewalPeriod;
	}

	public void setTrialPeriod(AbstractTimeCharacteristic period) throws CatalogException {
		if (period == null)
			throw new CatalogException("Given Trial Period was null");

		if (period instanceof HardDateTime)
			throw new CatalogException("HardDate is not acceptable for Trial Period. Must be Hours or Days or Infinite");

		if (period instanceof InfiniteTime && this.isRecurrent)
			throw new CatalogException("InfiniteTime is not acceptable for Trial Period, when the product is recurrent. Must be Hours or Days");

		this.renewalPeriod = period;
	}

	public AbstractTimeCharacteristic getTrialPeriod() {
		return trialPeriod;
	}

//	public AbstractTimeCharacteristic getTrialDate() {
//		return this.renewalPeriod;
//	}

	public ImmediateTermination getImmediateTermination() {
		return immediateTermination;
	}

	public void setImmediateTermination(ImmediateTermination immediateTermination) {
		this.immediateTermination = immediateTermination;
	}

	public void addProduct(Product product) throws CatalogException {
		if (product == null)
			throw new CatalogException("Given Product was null");

		if (this.products == null)
			this.products = new TreeSet<Product>();

		this.products.add(product);
	}

	public void removeProduct(AtomicProduct product) throws CatalogException {
		if (product == null)
			throw new CatalogException("Given Product was null");

		if (this.products != null)
			this.products.add(product);
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void addProducts(Set<Product> products) {
		this.products.addAll(products);
	}
	
	protected void setProducts(Set<Product> products) {
		this.products = products;
	}

	public boolean isCommercial() {
		return isCommercial;
	}

	public void setCommercial(boolean isCommercial) {
		this.isCommercial = isCommercial;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public String getOfferGroup() {
		return offerGroup;
	}

	public void setOfferGroup(String offerGroup) {
		this.offerGroup = offerGroup;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}
	
	

	public PolicyTree getEligibility() {
		return eligibility;
	}

	public void setEligibility(PolicyTree eligibility) {
		this.eligibility = eligibility;
	}

	public AbstractAccumulationPolicy getAccumulation() {
		return accumulation;
	}

	public void setAccumulation(AbstractAccumulationPolicy accumulation) {
		this.accumulation = accumulation;
	}

	public AbstractSwitchPolicy getSwitching() {
		return switching;
	}

	public void setSwitching(AbstractSwitchPolicy switching) {
		this.switching = switching;
	}

	public Set<String> getWhiteListedUsers() {
		return whiteListedUsers;
	}

	public void setWhiteListedUsers(Set<String> whiteListedUsers) {
		this.whiteListedUsers = whiteListedUsers;
	}

	
	public String getExit() {
		return exitOfferId;
	}

	protected void setExit(String exitOfferId) {
		this.exitOfferId = exitOfferId;
	}
	
	public void setExit(Offer other) throws CatalogException {
		if (other == null)
			throw new CatalogException("Given exit offer was null");
		
		if (other.offerState != State.PUBLISHED)
			throw new CatalogException("The exit offer is not in active state to accept as exit plan!!");
		
		this.exitOfferId = other.name;
	}

	
	public OfferLifeCycle getHistory() {
		return history;
	}

	
	public void setHistory(OfferLifeCycle history) {
		this.history = history;
	}

	public AbstractAutoTermination getAutoTermination() {
		return autoTermination;
	}

	public void setAutoTermination(AbstractAutoTermination autoTermination) {
		this.autoTermination = autoTermination;
	}

	public AbstractMinimumCommitment getMinimumCommitment() {
		return minimumCommitment;
	}

	public void setMinimumCommitment(AbstractMinimumCommitment minimumCommitment) {
		this.minimumCommitment = minimumCommitment;
	}
	
	

	public EventProfile getPreExiry() {
		return preExiry;
	}

	public void setPreExiry(EventProfile preExiry) {
		this.preExiry = preExiry;
	}

	public EventProfile getPreRenewal() {
		return preRenewal;
	}

	public void setPreRenewal(EventProfile preRenewal) {
		this.preRenewal = preRenewal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accumulation == null) ? 0 : accumulation.hashCode());
		result = prime * result + ((autoTermination == null) ? 0 : autoTermination.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((eligibility == null) ? 0 : eligibility.hashCode());
		result = prime * result + ((history == null) ? 0 : history.hashCode());
		result = prime * result + (isCommercial ? 1231 : 1237);
		result = prime * result + (isRecurrent ? 1231 : 1237);
		result = prime * result + ((minimumCommitment == null) ? 0 : minimumCommitment.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((offerGroup == null) ? 0 : offerGroup.hashCode());
		result = prime * result + ((offerState == null) ? 0 : offerState.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((products == null) ? 0 : products.hashCode());
		result = prime * result + ((renewalPeriod == null) ? 0 : renewalPeriod.hashCode());
		result = prime * result + ((switching == null) ? 0 : switching.hashCode());
		result = prime * result + ((trialPeriod == null) ? 0 : trialPeriod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;

		if (!(obj instanceof Offer))
			return false;
		
		Offer other = (Offer) obj;
		if (accumulation == null) {
			if (other.accumulation != null)
				return false;
		} else if (!accumulation.equals(other.accumulation))
			return false;
		
		if (autoTermination == null) {
			if (other.autoTermination != null)
				return false;
		} else if (!autoTermination.equals(other.autoTermination))
			return false;
		
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		
		if (eligibility == null) {
			if (other.eligibility != null)
				return false;
		} else if (!eligibility.equals(other.eligibility))
			return false;
		
		if (history == null) {
			if (other.history != null)
				return false;
		} else if (!history.equals(other.history))
			return false;
		
		if (isCommercial != other.isCommercial)
			return false;
		
		if (isRecurrent != other.isRecurrent)
			return false;
		
		if (minimumCommitment == null) {
			if (other.minimumCommitment != null)
				return false;
		} else if (!minimumCommitment.equals(other.minimumCommitment))
			return false;
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (offerGroup == null) {
			if (other.offerGroup != null)
				return false;
		} else if (!offerGroup.equals(other.offerGroup))
			return false;
		
		if (offerState != other.offerState)
			return false;
		
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		
		if (products == null) {
			if (other.products != null)
				return false;
		} else if (!products.equals(other.products))
			return false;
		
		if (renewalPeriod == null) {
			if (other.renewalPeriod != null)
				return false;
		} else if (!renewalPeriod.equals(other.renewalPeriod))
			return false;
		
		if (switching == null) {
			if (other.switching != null)
				return false;
		} else if (!switching.equals(other.switching))
			return false;
		
		if (trialPeriod == null) {
			if (other.trialPeriod != null)
				return false;
		} else if (!trialPeriod.equals(other.trialPeriod))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "Offer [name=" + name + ", description=" + description + ", externalHandles=" + externalHandles + ", version=" + version
				+ ", offerState=" + offerState + ", isRecurrent=" + isRecurrent + ", renewalPeriod=" + renewalPeriod + ", trialPeriod="
				+ trialPeriod + ", immediateTermination=" + immediateTermination + ", autoTermination=" + autoTermination
				+ ", minimumCommitment=" + minimumCommitment + ", products=" + products + ", isCommercial=" + isCommercial + ", owner="
				+ owner + ", offerGroup=" + offerGroup + ", price=" + price + ", eligibility=" + eligibility + ", accumulation="
				+ accumulation + ", switching=" + switching + ", whiteListedUsers=" + whiteListedUsers + ", exitOfferId=" + exitOfferId
				+ ", history=" + history + ", preExiry=" + preExiry + ", preRenewal=" + preRenewal + "]";
	}
	
	
	
	

	
		
	
	//==========----------------------------- End of Java Niceties Section ---------------===========================================================/
	

}
