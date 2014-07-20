package com.ericsson.raso.sef.bes.prodcat.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.Constants;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.ruleengine.Rule;

public final class Price extends MonetaryUnit {
	private static final long serialVersionUID = 8359880187271854753L;
	private static final Logger LOGGER = LoggerFactory.getLogger(Price.class);
	private MonetaryUnit cost = null;
	private List<Tax> taxes = new ArrayList<Tax>();
	private List<PricingPolicy> ratingRules = new ArrayList<PricingPolicy>();
	private Rule criteria = null;
	
	private Map<String, Object> context = null;

	public Price(String iso4217CurrencyCode, long amount) {
		super(iso4217CurrencyCode, amount);
		this.cost = new Cost(iso4217CurrencyCode, amount);
		
	}
	
	public MonetaryUnit getSimpleAdviceOfCharge() {
		/*
		 * 1. invoke getPrintableAdviceOfCharge()
		 * 2. extract & return "final offer"
		 */
		LOGGER.debug("Inside getSimpleAdviceOfCharge method");
		Map<String, MonetaryUnit> costs = this.getPrintableAdviceOfCharge();
		return costs.get(Constants.FINAL_OFFER.name());
	}
	
	public Map<String, MonetaryUnit> getPrintableAdviceOfCharge() {
		LOGGER.debug("Inside getPrintableAdviceOfCharge method");
		Map<String, MonetaryUnit> costElements = new ConcurrentHashMap<String, MonetaryUnit>();

		context = RequestContextLocalStore.get().getInProcess();

		/*
		 * 1. process all policies over the base cost
		 * 2. calculate all taxes
		 * 3. sum up the entries to "final offer" entry
		 * 4. load up the cost elements building up to final price in the hashma
		 */
		
		// Step 1
		LOGGER.debug("Processing Pricing Policies : " + this.ratingRules);
		if(this.ratingRules != null) {
			for (PricingPolicy rating: this.ratingRules) {
				LOGGER.debug("Executing pricing policy: " + rating);
				rating.setCost(this.cost);
				if (rating.execute()) {
					Long ratedAmount = (Long) context.get(Constants.RATED_AMOUNT.name());
					LOGGER.debug("PricePolicy True. Setting Amount: " + ratedAmount);
					costElements.put(rating.getName(), new Cost(this.getIso4217CurrencyCode(), ratedAmount));
					//this.setAmount(this.getAmount() + ratedAmount);
					this.setAmount(ratedAmount);
					LOGGER.debug("Price updated to: " + this.getAmount());
				}
			}
		}
		
		// Step 2
		
		long finalOffer = this.getAmount();
		LOGGER.debug("FinalOffer before taxes: "+ finalOffer);
		if (this.taxes != null) {
			LOGGER.debug("taxes are not null. Size: " + this.taxes.size());
			for (Tax tax: this.taxes) {
				LOGGER.debug("Working tax: " + tax);
				MonetaryUnit taxAmount = tax.calculateTax(this);
				costElements.put(tax.getName(), taxAmount);
				finalOffer += taxAmount.getAmount();
				LOGGER.debug("Price updated after tax: "+ finalOffer);
			}
		} else
			LOGGER.debug("taxes are null!!");
		LOGGER.debug("FinalOffer after all taxes: "+ finalOffer);

		// Step 3
		costElements.put(Constants.FINAL_OFFER.name(), new Cost(this.getIso4217CurrencyCode(), finalOffer));

				
		return costElements;
	}
	
	public Map<String, MonetaryUnit> getPenalty() {
		Map<String, MonetaryUnit> costElements = new ConcurrentHashMap<String, MonetaryUnit>();
		LOGGER.debug("Inside getPenalty method");
		for (PricingPolicy rating: this.ratingRules) {
			rating.setCost(this.cost);
			if (rating.execute()) {
				long ratedAmount = (long) context.get(Constants.RATED_AMOUNT.name());
				costElements.put(rating.getName(), 
						new Cost(this.getIso4217CurrencyCode(), ratedAmount));
				this.setAmount(this.getAmount() + ratedAmount);
			}
		}
		
		return costElements;
	}

	public MonetaryUnit getCost() {
		return cost;
	}

	public void setCost(MonetaryUnit cost) {
		this.cost = cost;
	}

	public List<Tax> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<Tax> taxes) {
		this.taxes = taxes;
	}

	public List<PricingPolicy> getRatingRules() {
		return ratingRules;
	}

	public void addRatingRule(PricingPolicy rating) {
		LOGGER.debug("PricingPolicy: "+rating.toString());
		if (this.ratingRules == null)
			this.ratingRules = new ArrayList<PricingPolicy>();
		this.ratingRules.add(rating);
	}
	
	public void removeRatingRule(PricingPolicy rating) {
		this.ratingRules.remove(rating);
	}
	
	
	public Rule getCriteria() {
		return criteria;
	}

	public void setCriteria(Rule criteria) {
		this.criteria = criteria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((criteria == null) ? 0 : criteria.hashCode());
		result = prime * result + ((ratingRules == null) ? 0 : ratingRules.hashCode());
		result = prime * result + ((taxes == null) ? 0 : taxes.hashCode());
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
		
		if (!(obj instanceof Price))
			return false;
		
		Price other = (Price) obj;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		
		if (criteria == null) {
			if (other.criteria != null)
				return false;
		} else if (!criteria.equals(other.criteria))
			return false;
		
		if (ratingRules == null) {
			if (other.ratingRules != null)
				return false;
		} else if (!ratingRules.equals(other.ratingRules))
			return false;
		
		if (taxes == null) {
			if (other.taxes != null)
				return false;
		} else if (!taxes.equals(other.taxes))
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "Price [cost=" + cost + ", taxes=" + taxes + ", ratingRules=" + ratingRules + ", criteria=" + criteria + ", context="
				+ context + ", getIso4217CurrencyCode()=" + getIso4217CurrencyCode() + ", getAmount()=" + getAmount() + "]";
	}


	
	
}
