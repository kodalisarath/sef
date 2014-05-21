package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.Subscription;


public class Offer {
	
	private String subscriptionId = null;
	private String name = null;
	private String description = null;
	private String validity = null;
	private boolean recurrence = false;
	private String trial = null;
	private String minimumCommitment = null;
	private String autoTerminate = null;
	private long price = -1;
	private String currency = null;
	private Set<Product> products = null;
	
	public Offer(Subscription subscription) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
		Date dateField = null;
		
		this.subscriptionId = subscription.getSubscriptionId();
		this.name = subscription.getName();
		this.description = subscription.getDescription();
		
		long date = subscription.getRenewalPeriod().getExpiryTimeInMillis();
		if (date == -1) {
			this.validity = "Never Ending";
		} else {
			dateField = new Date(date);
			this.validity = formatter.format(dateField);
		}

		this.recurrence = subscription.isRecurrent();
		
		if (this.minimumCommitment != null) {
			try {
				date = subscription.getMinimumCommitment().getCommitmentTime(System.currentTimeMillis(), subscription.getRenewalPeriod().getExpiryTimeInMillis());
				if (date == -1) {
					this.minimumCommitment = "No Commitment";
				} else {
					dateField = new Date(date);
					this.minimumCommitment = formatter.format(dateField);
				}
			} catch (CatalogException e) {
				this.minimumCommitment = "No Commitment";
			}
		}
		
		if (this.autoTerminate != null) {
			try {
				date = subscription.getAutoTermination().getTerminationTime(System.currentTimeMillis(), subscription.getRenewalPeriod().getExpiryTimeInMillis());
				if (date == -1) {
					this.autoTerminate = "No Automatic Termination";
				} else {
					dateField = new Date(date);
					this.minimumCommitment = formatter.format(dateField);
				}
			} catch (CatalogException e) {
				this.minimumCommitment = "No Automatic Termination";
			}
		}
		
		
		this.products = this.translateProducts(subscription.getAllAtomicProducts());

		
		
	}
	
	public Offer(com.ericsson.raso.sef.bes.prodcat.entities.Offer other) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
		Date dateField = null;
		
		
		this.name = other.getName();
		this.description = other.getDescription();
		
		// unfortunately, format class in java sdk is not synchronized and have to take this hit in every thread...
		long date = other.getRenewalPeriod().getExpiryTimeInMillis();
		if (date == -1) {
			this.validity = "Never Ending";
		} else {
			dateField = new Date(date);
			this.validity = formatter.format(dateField);
		}

		this.recurrence = other.isRecurrent();
		
		if (this.trial != null) {
			date = other.getTrialPeriod().getExpiryTimeInMillis();
			if (date == -1) {
				this.trial = "No Trial";
			} else {
				dateField = new Date(date);
				this.trial = formatter.format(dateField);
			}
		}
		
		if (this.minimumCommitment != null) {
			try {
				date = other.getMinimumCommitment().getCommitmentTime(System.currentTimeMillis(), other.getRenewalPeriod().getExpiryTimeInMillis());
				if (date == -1) {
					this.minimumCommitment = "No Commitment";
				} else {
					dateField = new Date(date);
					this.minimumCommitment = formatter.format(dateField);
				}
			} catch (CatalogException e) {
				this.minimumCommitment = "No Commitment";
			}
		}
		
		if (this.autoTerminate != null) {
			try {
				date = other.getAutoTermination().getTerminationTime(System.currentTimeMillis(), other.getRenewalPeriod().getExpiryTimeInMillis());
				if (date == -1) {
					this.autoTerminate = "No Automatic Termination";
				} else {
					dateField = new Date(date);
					this.minimumCommitment = formatter.format(dateField);
				}
			} catch (CatalogException e) {
				this.minimumCommitment = "No Automatic Termination";
			}
		}
		
		this.price = other.getPrice().getSimpleAdviceOfCharge().getAmount();
		this.currency = other.getPrice().getIso4217CurrencyCode();
		
		this.products = this.translateProducts(other.getAllAtomicProducts());
		
		
	}

	private Set<Product> translateProducts(List<AtomicProduct> allAtomicProducts) {
		Set<Product> products = new TreeSet<Product>();
		
		for (AtomicProduct source: allAtomicProducts) 
			products.add(new Product(source));
		
		return products;
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
