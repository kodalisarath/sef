package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;

public final class AtomicProduct extends Product implements Serializable, Comparable<AtomicProduct> {
	private static final long serialVersionUID = 7193202158721069988L;

	private Resource resource = null;
	private AbstractQuotaCharacteristic quota = null;
	private AbstractTimeCharacteristic validity = null;
	private Map<String, Object> metas = null;
	private Map<SubscriptionLifeCycleEvent, Boolean> suppressFulfilment = null;
	private boolean resetQuotaOnRenewal = false;
	
	public AtomicProduct(String name) {
		super(name);
	}
	
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setMeta(String metaName, Object value) {
		if (this.metas == null)
			this.metas = new TreeMap<String, Object>();
		
		this.metas.put(metaName, value);
	}
	
	public void removeMeta(String metaName) {
		if (this.metas != null)
			this.metas.remove(metaName);
	}
	
	public Object getMeta(String metaName) {
		if (this.metas != null)
			return this.metas.get(metaName);
		return null;
	}
	
	public Map<String, Object> getMetas() {
		return this.metas;
	}

	public AbstractQuotaCharacteristic getQuota() {
		return quota;
	}

	public void setQuota(AbstractQuotaCharacteristic quota) {
		this.quota = quota;
	}

	public AbstractTimeCharacteristic getValidity() {
		return validity;
	}

	public void setValidity(AbstractTimeCharacteristic validity) {
		this.validity = validity;
	}

	public Map<SubscriptionLifeCycleEvent, Boolean> getSuppressFulfilment() {
		return suppressFulfilment;
	}

	public void setSuppressFulfilment(Map<SubscriptionLifeCycleEvent, Boolean> suppressFulfilment) {
		this.suppressFulfilment = suppressFulfilment;
	}

	public boolean isResetQuotaOnRenewal() {
		return resetQuotaOnRenewal;
	}

	public void setResetQuotaOnRenewal(boolean resetQuotaOnRenewal) {
		this.resetQuotaOnRenewal = resetQuotaOnRenewal;
	}

	public boolean isSame(AtomicProduct other) {
		
		if (other == null)
			return false;
		
		if (this.resource == null)
			return false;
		
		if (other.resource == null)
			return false;
		
		if (!this.resource.equals(other.resource))
			return false;
		
		if (!this.quota.equals(other.quota))
			return false;
		
		if (!this.validity.equals(other.validity))
			return false;
		
		
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((quota == null) ? 0 : quota.hashCode());
		result = prime * result + (resetQuotaOnRenewal ? 1231 : 1237);
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + ((suppressFulfilment == null) ? 0 : suppressFulfilment.hashCode());
		result = prime * result + ((validity == null) ? 0 : validity.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!super.equals(obj))
			return false;
		
		if (!(obj instanceof AtomicProduct))
			return false;
		
		AtomicProduct other = (AtomicProduct) obj;
		if (quota == null) {
			if (other.quota != null)
				return false;
		} else if (!quota.equals(other.quota))
			return false;
		
		if (resetQuotaOnRenewal != other.resetQuotaOnRenewal)
			return false;
		
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		
		if (suppressFulfilment == null) {
			if (other.suppressFulfilment != null)
				return false;
		} else if (!suppressFulfilment.equals(other.suppressFulfilment))
			return false;
		
		if (validity == null) {
			if (other.validity != null)
				return false;
		} else if (!validity.equals(other.validity))
			return false;
		
		return true;
	}

	@Override
	public int compareTo(AtomicProduct o) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	
}
