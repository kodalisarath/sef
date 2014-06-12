package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.core.db.model.Subscriber;
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

	public boolean execute(Subscriber subscriber, SubscriptionLifeCycleEvent event) throws CatalogException {
		if (subscriber.getPaymentType() == null)
			throw new CatalogException("Payment/Subsciber Type is not defined in the Store. All further activities will fail!!");
		
		SubscriberType subscriberType = SubscriberType.valueOf(subscriber.getPaymentType());
		Map<SubscriptionLifeCycleEvent, Set<Action>> lifeCyclePolicies = this.policyChain.get(subscriberType);
		Set<Action> contextPolicies = lifeCyclePolicies.get(event);
		
		for (Action policy: contextPolicies) {
			if (!policy.execute())
				return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((policyChain == null) ? 0 : policyChain.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!(obj instanceof PolicyTree))
			return false;
		
		PolicyTree other = (PolicyTree) obj;
		if (policyChain == null) {
			if (other.policyChain != null)
				return false;
		} else if (!policyChain.equals(other.policyChain))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "PolicyTree [policyChain=" + policyChain + ", isEmpty()=" + isEmpty() + "]";
	}
	
	
	
}
