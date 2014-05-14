package com.ericsson.raso.sef.bes.prodcat.entities;

import com.ericsson.raso.sef.bes.prodcat.tasks.refactor.FulfillmentTask;
import com.ericsson.raso.sef.ruleengine.Rule;

public abstract class FulfillmentProfile<E> extends FulfillmentTask<E> {
	private static final long serialVersionUID = 8880950261612749964L;

	private String name = null;
	private Rule criteria = null;

	protected FulfillmentProfile(String name) {
		this.name = name;
	}


	public boolean isProfileSelectable() {
		return this.criteria.execute();
	}

	public Rule getCriteria() {
		return criteria;
	}

	public void setCriteria(Rule criteria) {
		this.criteria = criteria;
	}

	public String getName() {
		return name;
	}


	public FulfillmentProfile(String name, Rule criteria) {
		super();
		this.name = name;
		this.criteria = criteria;
	}
	
	

}
