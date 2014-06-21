package com.ericsson.sef.bes.api.entities;

import java.io.Serializable;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="offer")
public class Offer implements Serializable {
	private static final long	serialVersionUID	= -7648416263412619160L;

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

	@Override
	public String toString() {
		return "Offer [subscriptionId=" + subscriptionId + ", name=" + name + ", description=" + description + ", validity=" + validity
				+ ", recurrence=" + recurrence + ", trial=" + trial + ", minimumCommitment=" + minimumCommitment + ", autoTerminate="
				+ autoTerminate + ", price=" + price + ", currency=" + currency + ", products=" + products + "]";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((autoTerminate == null) ? 0 : autoTerminate.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((minimumCommitment == null) ? 0 : minimumCommitment.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (price ^ (price >>> 32));
		result = prime * result + ((products == null) ? 0 : products.hashCode());
		result = prime * result + (recurrence ? 1231 : 1237);
		result = prime * result + ((subscriptionId == null) ? 0 : subscriptionId.hashCode());
		result = prime * result + ((trial == null) ? 0 : trial.hashCode());
		result = prime * result + ((validity == null) ? 0 : validity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Offer other = (Offer) obj;
		if (autoTerminate == null) {
			if (other.autoTerminate != null)
				return false;
		} else if (!autoTerminate.equals(other.autoTerminate))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
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
		if (price != other.price)
			return false;
		if (products == null) {
			if (other.products != null)
				return false;
		} else if (!products.equals(other.products))
			return false;
		if (recurrence != other.recurrence)
			return false;
		if (subscriptionId == null) {
			if (other.subscriptionId != null)
				return false;
		} else if (!subscriptionId.equals(other.subscriptionId))
			return false;
		if (trial == null) {
			if (other.trial != null)
				return false;
		} else if (!trial.equals(other.trial))
			return false;
		if (validity == null) {
			if (other.validity != null)
				return false;
		} else if (!validity.equals(other.validity))
			return false;
		return true;
	}
	
	

}
