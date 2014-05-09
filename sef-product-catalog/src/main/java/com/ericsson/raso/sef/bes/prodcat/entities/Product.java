package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;

import com.ericsson.raso.sef.ruleengine.Rule;

public abstract class Product implements Serializable {
	private static final long serialVersionUID = 2741629131590884038L;
	
	/**
	 * Unique Identifier to the Resource
	 */
	private String name = null;
	
	/**
	 * identifies who is the owner for this resource. From info security context, this concept will allow a stakeholder to access all of his
	 * own resources as well as resources for all stakeholders chained under the current level.
	 */
	private Owner owner = null;
	
	private Rule criteria = null;
	
	public Product(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	
	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Rule getCriteria() {
		return criteria;
	}

	public void setCriteria(Rule criteria) {
		this.criteria = criteria;
	}
	
		
}
