package com.ericsson.raso.sef.bes.prodcat.entities;

public final class DigitalAsset extends AssetResource {
		private static final long serialVersionUID = 8621825533170200306L;

		public DigitalAsset(String name) {
			super(name);
		}

		@Override
		public String toString() {
			return "DigitalAsset [getDescription()=" + getDescription() + ", isAbstract()=" + isAbstract() + ", isConsumable()="
					+ isConsumable() + ", getConsumptionUnitName()=" + getConsumptionUnitName() + ", getEnforcedMinQuota()="
					+ getEnforcedMinQuota() + ", getEnforcedMaxQuota()=" + getEnforcedMaxQuota() + ", getCost()=" + getCost()
					+ ", getName()=" + getName() + ", getConcreteChildren()=" + getConcreteChildren() + ", getDependantOnMe()="
					+ getDependantOnMe() + ", getDependantOnOthers()=" + getDependantOnOthers() + ", getFulfillmentProfiles()="
					+ getFulfillmentProfiles() + ", isDiscoverable()=" + isDiscoverable() + ", isExternallyConsumed()="
					+ isExternallyConsumed() + ", getOwner()=" + getOwner() + ", getResourceGroup()=" + getResourceGroup()
					+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + "]";
		}


}
