package com.ericsson.raso.sef.bes.prodcat.entities;

import java.util.List;
import java.util.Map;

import com.ericsson.raso.sef.ruleengine.Rule;

public final class Price extends MonetaryUnit {
	private static final long serialVersionUID = 8359880187271854753L;
	
	private MonetaryUnit cost = null;
	private List<Tax> taxes = null;
	private List<PricingPolicy> ratingRules = null;
	private Rule criteria = null;

	public Price(String iso4217CurrencyCode, long amount) {
		super(iso4217CurrencyCode, amount);
		this.cost = new Cost(iso4217CurrencyCode, amount);
	}
	
	public MonetaryUnit getSimpleAdviceOfCharge() {
		//TODO: implement the following logic...
		/*
		 * 1. invoke getPrintableAdviceOfCharge()
		 * 2. extract & return "final offer"
		 */
		
		return null;
	}
	
	public Map<String, MonetaryUnit> getPrintableAdviceOfCharge() {
		//TODO: implement the following logic...
		/*
		 * 1. process all policies over the base cost
		 * 2. calculate all taxes
		 * 3. sum up the entries to "final offer" entry
		 * 4. load up the cost elements building up to final price in the hashmap
		 */
				
		return null;
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

	public void setRatingRules(List<PricingPolicy> ratingRules) {
		this.ratingRules = ratingRules;
	}

	public Rule getCriteria() {
		return criteria;
	}

	public void setCriteria(Rule criteria) {
		this.criteria = criteria;
	}
	
	
	
	
}
