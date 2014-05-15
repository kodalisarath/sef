package com.ericsson.sef.bes.api.entities;

public final class Product {
	
	private String name = null;
	private String resourceName = null;
	private int quotaDefined = -1;
	private int quotaConsumed = -1;
	private String validity = null;
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getResourceName() {
		return resourceName;
	}
	
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public int getQuotaDefined() {
		return quotaDefined;
	}
	
	public void setQuotaDefined(int quotaDefined) {
		this.quotaDefined = quotaDefined;
	}
	
	public int getQuotaConsumed() {
		return quotaConsumed;
	}
	
	public void setQuotaConsumed(int quotaConsumed) {
		this.quotaConsumed = quotaConsumed;
	}
	
	public String getValidity() {
		return validity;
	}
	
	public void setValidity(String validity) {
		this.validity = validity;
	}
	
	

}
