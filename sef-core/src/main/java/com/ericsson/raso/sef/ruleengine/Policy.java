package com.ericsson.raso.sef.ruleengine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Policy implements Action, Serializable {
	
	private static final long serialVersionUID = -1780481144582884377L;
	
	private String name = null;
	private Rule rule = null;
	private List<TransformUnit> transforms = new ArrayList<TransformUnit>();

	public Policy() { }
	
	public Policy(String name) {
		this.name = name;
	}
	
	public void addTransformAction(TransformUnit transform) {
		this.transforms.add(transform);
	}
	
	public void removeTransformAction(TransformUnit transform) {
		this.transforms.remove(transform);
	}
	
	

	public String getName() {
		return name;
	}

	@Override
	public boolean execute() {
		if (this.rule.execute()) {
			for (TransformUnit transform: this.transforms) {
				try {
					transform.execute();
				} catch (TransformFailedException e) {
					// TODO logger on the error...
					return false;
				}
			}
		}

		return true;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public List<TransformUnit> getTransforms() {
		return transforms;
	}

	public void setTransforms(List<TransformUnit> transforms) {
		this.transforms = transforms;
	}
	
	

	
}
