package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.ruleengine.Rule;

public abstract class Resource implements Serializable {
	private static final long serialVersionUID = -7487212938020984237L;

	/**
	 * Unique Identifier to the Resource
	 */
	private String name = null;

	/**
	 * Descriptive text
	 */
	private String description = null;

	/**
	 * indicates if a Resource is abstract. For example, 'Data' can be an abstract resource, which is used to simply the Business Product
	 * (customer facing), while technical resources such as
	 * 
	 * If a Resource is abstract, it implies the following.... * This Resource will be used for "customer facing" purposes... implies that
	 * this resource will represent all its concrete resources toward Product Catalog. * This Resource must identify all concrete resources,
	 * that are selected thru a <code>Rule</code>. Only one concrete resource must be selectable for a given context. * An abstract resource
	 * cannot be a technical resource and hence cannot have a Fulfillment Profile in Service Registry.
	 * 
	 * If a Resource is not abstract, it implies the following.... * This Resource WILL have a Fulfillment Profile in Service Registry. *
	 * This Resource can be standalone - meaning it can serve both "technical" as well as "customer facing" purposes. * If more than ONE
	 * Fulfillment Profile is linked to a Resource, then each Profile must be selectable thru a Rule. Only one Profile must be selectable
	 * for a given context.
	 * 
	 * @see Rule
	 */
	private boolean isAbstract = false;

	/**
	 * indicates if this resource must be visible during end-user's discovery or self-care use-cases.
	 * 
	 * Because Fulfillment Engine will not support transaction (required when more than one backend operation is required), the
	 * functionality is centralized and controlled/governed by Transaction Engine. For this reason, all backend fulfillment primitives are
	 * kept atomic & mapped one-to-one and hence multiple backend operations are represented thru resources which will not be discovered/
	 * visible to end-user but can enrich the complexity of orchestration in the Transaction Engine.
	 */
	private boolean isDiscoverable = false;
	
	/**
	 * indicates if a Resource's consumption can be managed within SEF BES or must be queried and synchronized with external Service Elements or BSS 
	 * systems. 
	 */
	private boolean isExternallyConsumed = false;

	/**
	 * If this resource is abstract, then the concrete child resources must be defined and identified. If a resource is declared abstract
	 * and no concrete children are idenified, then the Service Registry will not allow this resource to be discovered for Business Product
	 * definition.
	 */
	private List<Resource> concreteChildren = null;

	/**
	 * indicates if a resource is a consumable resource. If so, then quota can be assigned. Quota related parameters can be defined and
	 * enforced automatically from Service Registry & Fulfillment Engine.
	 * 
	 * @see consumptionUnitName, enforcedMinQuota, enforcedMaxQuota
	 */
	private boolean isConsumable = false;

	/**
	 * descriptive text representing the units of consumption. For example, 'Data' can be consumed in 'KB', 'MB', 'GB' and so on.
	 * 
	 * Depending of the type of resource, the units can be abstract as well. A few examples below... - SMS is consumed in 'Message' - Voice
	 * is consumed in 'Minute' or 'Second' - Data can be consumed based on SLA which is 'Kbps', 'Mbps', etc. - An App or Music asset is
	 * consumed in 'Piece'
	 */
	private String consumptionUnitName = null;

	/**
	 * indicates a lowest threshold below which a user cannot be allowed to consume... Though SEF BES does not intercept the consumption,
	 * such thresholds can be a good way to trigger churn control, auto promotion, auto renewal, auto purchase package, auto upsell, or
	 * atleast automatic workflow to change the user into a different rate plan or pay-as-you-go models.
	 */
	private long enforcedMinQuota = -1;

	/**
	 * indicates a highest threshold above which a user cannot be allowed to accumulate... SEF BES will ensure that user cannot keep
	 * accumulating quota beyoond a limit.
	 * 
	 * Such feature can be useful in enforcing a 'fair use' policy avoiding hoarding behaviors as well as preventing users from buying more
	 * quota than they can consume and thus avoiding a customer dispute and subsequent reversals thus plugging potential revenue exposures.
	 */
	private long enforcedMaxQuota = -1;

	/**
	 * A resource can be dependent on each other and such relationship is captured here. Dependency can be useful in handling/ avoiding many
	 * customer disputes...
	 * 
	 * <code>Dependency Check</code> - It is possible to stop a user from discovering or requesting for a service which is dependent on
	 * another service for which he has no access currently. For example, 'WAP' or 'MMS' or 'GMail' services cannot be requested/ discovered
	 * if user has no 'Data'
	 * 
	 * <code>Cascaded Remove</code> - It is possible to deactivate/ terminate services/resources higher in the dependency chain, when a
	 * lower level service is terminated. For example, when 'Data' is terminated then 'WAP', 'MMS' or 'GMail' services can be automatically
	 * deactivated, thus addressing customer disputes when such higher level services keep charging user for a service that user cannot
	 * avail.
	 */
	private List<Resource> dependantOnMe = null;
	private List<Resource> dependantOnThem = null;

	/**
	 * If a resource is not abstract, it can have a cost associated with it. This can be useful when dynamic business products formulate
	 * based on certain criteria or the context.
	 * 
	 * For example, consider a scenario where a user wants to discover how much data or sms he can buy for 10USD. In such scenario, the
	 * Product Catalog can formulate a dynamic product specifically tailored for this scenario by adjusting offering characteristics on
	 * quota, validity period, etc.
	 */
	private MonetaryUnit cost = null;

	/**
	 * A resource can be configured to be provisioned in many ways based on business configuration performed in the backend systems. It may
	 * also be the case, that same resource must decide beween multiple backend systems - such as SEF BES integrated across Ericsson and
	 * other Vendors simultaneously. Such features help in isolating & abstracting backend technical operations from Business/ Marketing
	 * operations that pertain to Product Catalog.
	 * 
	 */
	private List<FulfillmentProfile> fulfillmentProfiles = null;

	/**
	 * identifies who is the owner for this resource. From info security context, this concept will allow a stakeholder to access all of his
	 * own resources as well as resources for all stakeholders chained under the current level.
	 */
	private Owner owner = null;
	
	/**
	 * identifies a Resource Group to which this Resource belongs. Each offer in a group is assigned a unique priority that determines if a
	 * 'switch' request is an upgrade or downgrade. This priority overrides auto-calculated weightage that will be used to determine
	 * upgrade/ downgrade. In the absence of this value, then the system auto-calculates the weightage using Characteristics - Quota,
	 * Validity, SLA & Resource Priority.
	 */
	private String resourceGroup = null;


	public Resource(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isConsumable() {
		return isConsumable;
	}

	public void setConsumable(boolean isConsumable) {
		this.isConsumable = isConsumable;
	}

	public String getConsumptionUnitName() {
		return consumptionUnitName;
	}

	public void setConsumptionUnitName(String consumptionUnitName) {
		this.consumptionUnitName = consumptionUnitName;
	}

	public long getEnforcedMinQuota() {
		return enforcedMinQuota;
	}

	public void setEnforcedMinQuota(long enforcedMinQuota) {
		this.enforcedMinQuota = enforcedMinQuota;
	}

	public long getEnforcedMaxQuota() {
		return enforcedMaxQuota;
	}

	public void setEnforcedMaxQuota(long enforcedMaxQuota) {
		this.enforcedMaxQuota = enforcedMaxQuota;
	}

	public MonetaryUnit getCost() {
		return cost;
	}

	public void setCost(MonetaryUnit cost) {
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public List<Resource> getConcreteChildren() {
		return concreteChildren;
	}

	public boolean addDependantOnMe(Resource other) {
		if (this.dependantOnMe == null)
			this.dependantOnMe = new ArrayList<Resource>();

		return (this.dependantOnMe.add(other) && other.dependantOnThem.add(this));
	}

	public boolean removeDependantOnMe(Resource other) {
		if (this.dependantOnMe == null)
			return false;

		this.dependantOnMe.remove(other);
		if (this.dependantOnMe.isEmpty())
			this.dependantOnMe = null;

		other.dependantOnThem.remove(this);
		if (other.dependantOnThem.isEmpty())
			other.dependantOnThem = null;
		return true;
	}

	public boolean addDependantOn(Resource other) {
		if (this.dependantOnThem == null)
			this.dependantOnThem = new ArrayList<Resource>();

		return (this.dependantOnThem.add(other) && other.dependantOnMe.add(this));
	}

	public boolean removeDependantOn(Resource other) {
		if (this.dependantOnThem == null)
			return false;

		this.dependantOnThem.remove(other);
		if (this.dependantOnThem.isEmpty())
			this.dependantOnThem = null;

		other.dependantOnMe.remove(this);
		if (other.dependantOnMe.isEmpty())
			other.dependantOnMe = null;

		return true;
	}

	public List<Resource> getDependantOnMe() {
		return dependantOnMe;
	}

	public List<Resource> getDependantOnOthers() {
		return dependantOnThem;
	}

	public boolean addFulfillmentProfile(FulfillmentProfile profile) throws CatalogException {
		if (this.fulfillmentProfiles == null)
			this.fulfillmentProfiles = new ArrayList<FulfillmentProfile>();

		return this.fulfillmentProfiles.add(profile);
	}

	public boolean removeFulfillmentProfile(FulfillmentProfile profile) {
		if (this.fulfillmentProfiles.contains(profile))
			this.fulfillmentProfiles.remove(profile);

		if (this.fulfillmentProfiles.isEmpty())
			this.fulfillmentProfiles = null;

		return true;
	}

	public List<FulfillmentProfile> getFulfillmentProfiles() {
		return this.fulfillmentProfiles;
	}

	public boolean isDiscoverable() {
		return isDiscoverable;
	}

	public void setDiscoverable(boolean isDiscoverable) {
		this.isDiscoverable = isDiscoverable;
	}
	
	

	public boolean isExternallyConsumed() {
		return isExternallyConsumed;
	}

	public void setExternallyConsumed(boolean isExternallyConsumed) {
		this.isExternallyConsumed = isExternallyConsumed;
	}

	public Owner getOwner() {
		return owner;
	}

	
	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	
	public String getResourceGroup() {
		return resourceGroup;
	}

	public void setResourceGroup(String resourceGroup) {
		this.resourceGroup = resourceGroup;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((concreteChildren == null) ? 0 : concreteChildren.hashCode());
		result = prime * result + ((consumptionUnitName == null) ? 0 : consumptionUnitName.hashCode());
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((dependantOnMe == null) ? 0 : dependantOnMe.hashCode());
		result = prime * result + ((dependantOnThem == null) ? 0 : dependantOnThem.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (enforcedMaxQuota ^ (enforcedMaxQuota >>> 32));
		result = prime * result + (int) (enforcedMinQuota ^ (enforcedMinQuota >>> 32));
		result = prime * result + ((fulfillmentProfiles == null) ? 0 : fulfillmentProfiles.hashCode());
		result = prime * result + (isAbstract ? 1231 : 1237);
		result = prime * result + (isConsumable ? 1231 : 1237);
		result = prime * result + (isDiscoverable ? 1231 : 1237);
		result = prime * result + (isExternallyConsumed ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((resourceGroup == null) ? 0 : resourceGroup.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!(obj instanceof Resource))
			return false;
		
		Resource other = (Resource) obj;
		if (concreteChildren == null) {
			if (other.concreteChildren != null)
				return false;
		} else if (!concreteChildren.equals(other.concreteChildren))
			return false;
		
		if (consumptionUnitName == null) {
			if (other.consumptionUnitName != null)
				return false;
		} else if (!consumptionUnitName.equals(other.consumptionUnitName))
			return false;
		
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		
		if (dependantOnMe == null) {
			if (other.dependantOnMe != null)
				return false;
		} else if (!dependantOnMe.equals(other.dependantOnMe))
			return false;
		
		if (dependantOnThem == null) {
			if (other.dependantOnThem != null)
				return false;
		} else if (!dependantOnThem.equals(other.dependantOnThem))
			return false;
		
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		
		if (enforcedMaxQuota != other.enforcedMaxQuota)
			return false;
		
		if (enforcedMinQuota != other.enforcedMinQuota)
			return false;
		
		if (fulfillmentProfiles == null) {
			if (other.fulfillmentProfiles != null)
				return false;
		} else if (!fulfillmentProfiles.equals(other.fulfillmentProfiles))
			return false;
		
		if (isAbstract != other.isAbstract)
			return false;
		
		if (isConsumable != other.isConsumable)
			return false;
		
		if (isDiscoverable != other.isDiscoverable)
			return false;
		
		if (isExternallyConsumed != other.isExternallyConsumed)
			return false;
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		
		if (resourceGroup == null) {
			if (other.resourceGroup != null)
				return false;
		} else if (!resourceGroup.equals(other.resourceGroup))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "<Resource name=" + name + "/>";
	}

}
