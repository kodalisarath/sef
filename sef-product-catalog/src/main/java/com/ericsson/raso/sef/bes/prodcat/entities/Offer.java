package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public class Offer implements Serializable {
	private static final long serialVersionUID = 5704479496440496263L;
	
	
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
	 * <li>Bundle - A set of {@link AtomicProduct} that must be provisioned collectively or not at all<br> <li>Package - A mixed set of
	 * {@link AtomicProduct} and {@link TechnicalProduct} to create a hierarchy of services that can be assigned or purchased. Such feature
	 * setup allows ease of administration and very high end-user experience, specially targeted at high-arpu subscribers.<br> <li>Promotion - A
	 * mixed set of {@link AtomicProduct} and {@link TechnicalProduct} but conditionally provisioned for the same {@link Price}<br> <li>
	 * Entitlement - A variant of Promotion, where an Offer is either assigned to or purchased by a subscriber but the validity period
	 * starts only at the first access/ consumption to the service.<br> <li>Subscription - All forms of Offer is a subscription when attached to
	 * a subscriber's profile. From this perspective, the Offer's life-cycle is also governed by the subscription life-cycle. For example,
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
	private PolicyTree accumulation = null;

	/**
	 * A set of Switch rules that determine if it is allowed for upgrading or downgrade of services/ resources.
	 */
	private PolicyTree switching = null;
	
	/**
	 * A list of users that are allowed to discover, purchase, avail and subscribe to this offer in TESTING state.
	 */
	private Set<String> whiteListedUsers = null;
	
	/**
	 * Specifies an exit Offer to shift the subscriber, when the subscription runs out or if the offer is retired.
	 */
	private Offer exit = null;
	
	/**
	 * Implicit audit trail of Offer Lifecycle dates to track history and manage versioning.
	 */
	private OfferLifeCycle history = null;

	//==========----------------------------- End of Attributes Section ---------------==============================================================/
	
	//==========----------------------------- Functional Section ---------------=====================================================================/
	public Offer(String name) {
		this.name = name;
		this.history = new OfferLifeCycle();
		this.history.setCreationTime(System.currentTimeMillis());
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
			
		if (this.exit == null || this.isRecurrent)
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

	public AbstractTimeCharacteristic getTrialDate() {
		return this.renewalPeriod;
	}

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

	public PolicyTree getAccumulation() {
		return accumulation;
	}

	public void setAccumulation(PolicyTree accumulation) {
		this.accumulation = accumulation;
	}

	public PolicyTree getSwitching() {
		return switching;
	}

	public void setSwitching(PolicyTree switching) {
		this.switching = switching;
	}

	public Set<String> getWhiteListedUsers() {
		return whiteListedUsers;
	}

	public void setWhiteListedUsers(Set<String> whiteListedUsers) {
		this.whiteListedUsers = whiteListedUsers;
	}

	
	public Offer getExit() {
		return exit;
	}

	
	public void setExit(Offer other) throws CatalogException {
		if (other == null)
			throw new CatalogException("Given exit offer was null");
		
		if (other.offerState != State.PUBLISHED)
			throw new CatalogException("The exit offer is not in active state to accept as exit plan!!");
		
		this.exit = other;
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
	
	
	
	//==========----------------------------- End of Java Niceties Section ---------------===========================================================/
	

}
