package com.ericsson.sef.bes.api.entities;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="offer")
public class Offer {
	
	private String			subscriptionId		= null;
	private String			name				= null;
	private String			description			= null;
	private String			validity			= null;
	private boolean			recurrence			= false;
	private String			trial				= null;
	private String			minimumCommitment	= null;
	private String			autoTerminate		= null;
	private long			price				= -1;
	private String			currency			= null;
	private Set<Product>	products			= null;
	
	
	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getValidity() {
		return validity;
	}
	
	public void setValidity(String validity) {
		this.validity = validity;
	}
	
	public boolean isRecurrence() {
		return recurrence;
	}
	
	public void setRecurrence(boolean recurrence) {
		this.recurrence = recurrence;
	}
	
	public String getTrial() {
		return trial;
	}
	
	public void setTrial(String trial) {
		this.trial = trial;
	}
	
	public String getMinimumCommitment() {
		return minimumCommitment;
	}
	
	public void setMinimumCommitment(String minimumCommitment) {
		this.minimumCommitment = minimumCommitment;
	}
	
	public String getAutoTerminate() {
		return autoTerminate;
	}
	
	public void setAutoTerminate(String autoTerminate) {
		this.autoTerminate = autoTerminate;
	}
	
	public long getPrice() {
		return price;
	}
	
	public void setPrice(long price) {
		this.price = price;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public Set<Product> getProducts() {
		return products;
	}
	
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	

}
