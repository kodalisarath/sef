package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.ruleengine.Action;

public final class PolicyTree implements Serializable {
	private static final long serialVersionUID = 8122763451218472345L;
	
	private Map<SubscriberType, Map<SubscriptionLifeCycleEvent, Set<Action>>> policyChain = null;
	
	public void setPolicy(SubscriberType subscriberType, SubscriptionLifeCycleEvent lifeCycle, Action action) {
		if (this.policyChain == null)
			this.policyChain = new TreeMap<SubscriberType, Map<SubscriptionLifeCycleEvent, Set<Action>>>();
		
		Map<SubscriptionLifeCycleEvent, Set<Action>> lifeCycleActions = this.policyChain.get(subscriberType);
		if (lifeCycleActions == null)
			lifeCycleActions = new TreeMap<SubscriptionLifeCycleEvent, Set<Action>>();
		
		Set<Action> actions = lifeCycleActions.get(lifeCycle);
		if (actions == null) 
			actions = new TreeSet<Action>();
		
		actions.add(action);
		lifeCycleActions.put(lifeCycle, actions);
		this.policyChain.put(subscriberType, lifeCycleActions);
	}
	
	public void setPolicies(SubscriberType subscriberType, SubscriptionLifeCycleEvent lifeCycle, Set<Action> actions) {
		if (this.policyChain == null)
			this.policyChain = new TreeMap<SubscriberType, Map<SubscriptionLifeCycleEvent, Set<Action>>>();
		
		Map<SubscriptionLifeCycleEvent, Set<Action>> lifeCycleActions = this.policyChain.get(subscriberType);
		if (lifeCycleActions == null)
			lifeCycleActions = new TreeMap<SubscriptionLifeCycleEvent, Set<Action>>();
		
		Set<Action> fromChain = lifeCycleActions.get(lifeCycle);
		if (fromChain == null) 
			fromChain = new TreeSet<Action>();
		
		fromChain.addAll(actions);
		lifeCycleActions.put(lifeCycle, fromChain);
		this.policyChain.put(subscriberType, lifeCycleActions);
	}
	
	public void removePolicy(SubscriberType subscriberType, SubscriptionLifeCycleEvent lifeCycle, Action action) {
		if (this.policyChain == null) 
			return;
		
		Map<SubscriptionLifeCycleEvent, Set<Action>> lifeCycleActions = this.policyChain.get(subscriberType);
		if (lifeCycleActions == null)
			return;
		
		Set<Action> actions = lifeCycleActions.get(lifeCycle);
		if (actions == null) 
			return;
		
		actions.remove(action);
		
		if (actions.isEmpty())
			lifeCycleActions.remove(lifeCycle);
		
		if (lifeCycleActions.isEmpty())
			this.policyChain.remove(subscriberType);
		
		if (this.policyChain.isEmpty())
			this.policyChain = null;
	}
	
	public void removePolicies(SubscriberType subscriberType, SubscriptionLifeCycleEvent lifeCycle, Action action) {
		if (this.policyChain == null) 
			return;
		
		Map<SubscriptionLifeCycleEvent, Set<Action>> lifeCycleActions = this.policyChain.get(subscriberType);
		if (lifeCycleActions == null)
			return;
		
		Set<Action> actions = lifeCycleActions.get(lifeCycle);
		if (actions == null) 
			return;
		
		lifeCycleActions.remove(lifeCycle);
		
		if (lifeCycleActions.isEmpty())
			this.policyChain.remove(subscriberType);
		
		if (this.policyChain.isEmpty())
			this.policyChain = null;
	}
	
	public Set<Action> getPolicies(SubscriberType subscriberType, SubscriptionLifeCycleEvent lifeCycle) {
		if (this.policyChain == null) 
			return null;
		
		Map<SubscriptionLifeCycleEvent, Set<Action>> lifeCycleActions = this.policyChain.get(subscriberType);
		if (lifeCycleActions == null)
			return null;
		
		Set<Action> actions = lifeCycleActions.get(lifeCycle);
		if (actions == null) 
			return null;
		
		return actions;
	}

	public boolean isEmpty() {
		if (this.policyChain == null || this.policyChain.isEmpty())
			return true;
		return false;
	}
	
	
}
