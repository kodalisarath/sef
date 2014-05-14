package com.ericsson.raso.sef.bes.prodcat.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.bes.prodcat.Constants;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.ruleengine.Rule;

public final class Price extends MonetaryUnit {
	private static final long serialVersionUID = 8359880187271854753L;
	
	private MonetaryUnit cost = null;
	private List<Tax> taxes = null;
	private List<PricingPolicy> ratingRules = null;
	private Rule criteria = null;
	
	private Map<String, Object> context = null;

	public Price(String iso4217CurrencyCode, long amount) {
		super(iso4217CurrencyCode, amount);
		this.cost = new Cost(iso4217CurrencyCode, amount);
		
		context = RequestContextLocalStore.get().getInProcess();
	}
	
	public MonetaryUnit getSimpleAdviceOfCharge() {
		/*
		 * 1. invoke getPrintableAdviceOfCharge()
		 * 2. extract & return "final offer"
		 */
		Map<String, MonetaryUnit> costs = this.getPrintableAdviceOfCharge();
		return costs.get(Constants.FINAL_OFFER.name());
	}
	
	public Map<String, MonetaryUnit> getPrintableAdviceOfCharge() {
		Map<String, MonetaryUnit> costElements = new TreeMap<String, MonetaryUnit>();
		
		/*
		 * 1. process all policies over the base cost
		 * 2. calculate all taxes
		 * 3. sum up the entries to "final offer" entry
		 * 4. load up the cost elements building up to final price in the hashmap
		 */
		
		// Step 1
		for (PricingPolicy rating: this.ratingRules) {
			rating.setCost(this.cost);
			if (rating.execute()) {
				long ratedAmount = (long) context.get(Constants.RATED_AMOUNT.name());
				costElements.put(rating.getName(), 
						new Cost(this.getIso4217CurrencyCode(), ratedAmount));
				this.setAmount(this.getAmount() + ratedAmount);
			}
		}
		
		// Step 2
		long finalOffer = this.getAmount();
		for (Tax tax: this.taxes) {
			MonetaryUnit taxAmount = tax.calculateTax(this);
			costElements.put(tax.getName(), taxAmount);
			finalOffer += taxAmount.getAmount();
		}
		
		// Step 3
		costElements.put(Constants.FINAL_OFFER.name(), 
				new Cost(this.getIso4217CurrencyCode(), finalOffer));

				
		return costElements;
	}
	
	public Map<String, MonetaryUnit> getPenalty() {
		Map<String, MonetaryUnit> costElements = new TreeMap<String, MonetaryUnit>();
		
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
	
	
	
	
}
