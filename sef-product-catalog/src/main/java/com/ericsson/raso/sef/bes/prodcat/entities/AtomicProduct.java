package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;

public final class AtomicProduct extends Product implements Serializable {
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

	
}
