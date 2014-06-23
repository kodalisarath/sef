package com.ericsson.raso.sef.bes.prodcat.entities.smart;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.Constants;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.bes.prodcat.entities.Cost;
import com.ericsson.raso.sef.bes.prodcat.entities.MonetaryUnit;
import com.ericsson.raso.sef.bes.prodcat.entities.PricingPolicy;
import com.ericsson.raso.sef.core.RequestContextLocalStore;

public class SmartLIfeCyclePricingPolicy extends PricingPolicy {
	private static final long serialVersionUID = -65196986520647498L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SmartLIfeCyclePricingPolicy.class);
	private MonetaryUnit purchaseCost = null;
	private MonetaryUnit renewalCost = null;

	public SmartLIfeCyclePricingPolicy(String name, Cost purchase, Cost renewal) {
		super(name);
		this.purchaseCost = purchase;
		this.renewalCost = renewal;
	}

	@Override
	public boolean execute() {
		LOGGER.debug("..Inside SmartSimplePricingPolicy...");
		Map<String, Object> context = RequestContextLocalStore.get().getInProcess();
		SubscriptionLifeCycleEvent event = (SubscriptionLifeCycleEvent) context.get("event");
		LOGGER.debug("event: "+event);
		if (event != null) {
			if (event == SubscriptionLifeCycleEvent.PURCHASE) { 
				context.put(Constants.RATED_AMOUNT.name(), this.purchaseCost.getAmount());
				LOGGER.debug("purchase rate: " + this.purchaseCost);
			} else if (event == SubscriptionLifeCycleEvent.RENEWAL) {
				context.put(Constants.RATED_AMOUNT.name(), this.renewalCost.getAmount());
				LOGGER.debug("renewal rate: " + this.renewalCost);
			}
		} else {
			context.put(Constants.RATED_AMOUNT.name(), 100);
		}
			
		return true;
	}

	public void setCost(MonetaryUnit cost) {
		this.purchaseCost = cost;
	}
		
	
}
