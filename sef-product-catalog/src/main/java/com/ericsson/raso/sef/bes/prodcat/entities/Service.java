package com.ericsson.raso.sef.bes.prodcat.entities;


public final class Service extends Resource {
	private static final long serialVersionUID = 4136268544827820112L;

	public Service(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Service [getDescription()=" + getDescription() + ", isAbstract()=" + isAbstract() + ", isConsumable()=" + isConsumable()
				+ ", getConsumptionUnitName()=" + getConsumptionUnitName() + ", getEnforcedMinQuota()=" + getEnforcedMinQuota()
				+ ", getEnforcedMaxQuota()=" + getEnforcedMaxQuota() + ", getCost()=" + getCost() + ", getName()=" + getName()
				+ ", getConcreteChildren()=" + getConcreteChildren() + ", getDependantOnMe()=" + getDependantOnMe()
				+ ", getDependantOnOthers()=" + getDependantOnOthers() + ", getFulfillmentProfiles()=" + getFulfillmentProfiles()
				+ ", isDiscoverable()=" + isDiscoverable() + ", isExternallyConsumed()=" + isExternallyConsumed() + ", getOwner()="
				+ getOwner() + ", getResourceGroup()=" + getResourceGroup() + "]";
	}

	
}
