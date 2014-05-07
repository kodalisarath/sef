package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycle;

public class AtomicProduct implements Serializable {
	private static final long serialVersionUID = 7193202158721069988L;

	private String name = null;
	private Resource resource = null;
	private AbstractQuotaCharacteristic quota = null;
	private AbstractTimeCharacteristic validity = null;
	private Map<String, Object> metas = null;
	private Map<SubscriptionLifeCycle, Boolean> suppressFulfilment = null;
	private boolean resetQuotaOnRenewal = false;
	
	public AtomicProduct(String name) {
		this.name = name;
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

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
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

	public Map<SubscriptionLifeCycle, Boolean> getSuppressFulfilment() {
		return suppressFulfilment;
	}

	public void setSuppressFulfilment(Map<SubscriptionLifeCycle, Boolean> suppressFulfilment) {
		this.suppressFulfilment = suppressFulfilment;
	}

	public boolean isResetQuotaOnRenewal() {
		return resetQuotaOnRenewal;
	}

	public void setResetQuotaOnRenewal(boolean resetQuotaOnRenewal) {
		this.resetQuotaOnRenewal = resetQuotaOnRenewal;
	}

	public String getName() {
		return name;
	}
	
}
