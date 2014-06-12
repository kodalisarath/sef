package com.ericsson.raso.sef.bes.prodcat.entities;



public final class PhysicalAsset extends AssetResource {
		private static final long serialVersionUID = -1493873269032558907L;

		public PhysicalAsset(String name) {
			super(name);
		}

		@Override
		public String toString() {
			return "PhysicalAsset [getDescription()=" + getDescription() + ", isAbstract()=" + isAbstract() + ", isConsumable()="
					+ isConsumable() + ", getConsumptionUnitName()=" + getConsumptionUnitName() + ", getEnforcedMinQuota()="
					+ getEnforcedMinQuota() + ", getEnforcedMaxQuota()=" + getEnforcedMaxQuota() + ", getCost()=" + getCost()
					+ ", getName()=" + getName() + ", getConcreteChildren()=" + getConcreteChildren() + ", getDependantOnMe()="
					+ getDependantOnMe() + ", getDependantOnOthers()=" + getDependantOnOthers() + ", getFulfillmentProfiles()="
					+ getFulfillmentProfiles() + ", isDiscoverable()=" + isDiscoverable() + ", isExternallyConsumed()="
					+ isExternallyConsumed() + ", getOwner()=" + getOwner() + ", getResourceGroup()=" + getResourceGroup() + "]";
		}


		
	
}
