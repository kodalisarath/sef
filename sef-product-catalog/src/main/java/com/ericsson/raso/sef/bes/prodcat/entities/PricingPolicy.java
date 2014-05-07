package com.ericsson.raso.sef.bes.prodcat.entities;

import com.ericsson.raso.sef.ruleengine.Policy;

public class PricingPolicy extends Policy {
	private static final long serialVersionUID = 6928999115192214745L;

	public PricingPolicy() {
	}

	public PricingPolicy(String name) {
		super(name);
	}
	
	@Override
	public boolean execute() {
		//TODO: after request context service is available, implement the following code...
		/*
		 * 1. fetch request context reference...
		 * 2. allow each transform unit to execute...
		 * 3. capture the output of tranform into new MonetaryUnit
		 */
		return super.execute();
	}


	

}
