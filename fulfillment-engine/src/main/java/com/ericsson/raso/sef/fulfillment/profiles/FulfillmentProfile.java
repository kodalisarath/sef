package com.ericsson.raso.sef.fulfillment.profiles;

import java.io.Serializable;
import java.util.List;

import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.ruleengine.Rule;

public abstract class FulfillmentProfile<E> implements Serializable {
	private static final long serialVersionUID = 8880950261612749964L;

	private String name = null;
	private Rule criteria = null;
	private List<String> abstractResources = null;

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
	
	
	
	public List<String> getAbstractResources() {
		return abstractResources;
	}


	public void setAbstractResources(List<String> abstractResources) {
		this.abstractResources = abstractResources;
	}


	public void setName(String name) {
		this.name = name;
	}


	public abstract List<E> fulfill(E e, java.util.Map<String, String> map) throws FulfillmentException;
	public abstract List<E> prepare(E e, java.util.Map<String, String> map) throws FulfillmentException;
	public abstract List<E> query(E e, java.util.Map<String, String> map) throws FulfillmentException;
	public abstract List<E> revert(E e, java.util.Map<String, String> map) throws FulfillmentException;
	
}
