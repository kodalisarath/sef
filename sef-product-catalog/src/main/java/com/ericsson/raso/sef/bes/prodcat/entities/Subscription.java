package com.ericsson.raso.sef.bes.prodcat.entities;

import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleState;
import com.ericsson.raso.sef.bes.prodcat.entities.SubscriptionHistory.HistoryEvent;

/**
 * This class represents an instance of {@link Offer} that is attached to subscriber profile. This decoupling allows handling a few complex use-cases...
 * <br><li> Track the subcription's history in its lifecycle such as repetitive suspension, barring, reactivation, etc.
 * <br><li> Create flexibility on switching between related offers, exit offers, versions of the same product.
 * <br><li> Tracking user behaviour and produce enablers for analytics such as purchases, consumption, payment behaviour & credit risks/rating.
 * @author esatnar
 *
 */
public class Subscription extends Offer {
	private static final long serialVersionUID = 218826816576313417L;
	
	private String subscriber = null;
	private SubscriptionHistory subscriptionHistory = null;
	
	public Subscription(String name) {
		super(name);
	}
	
	public void addSubscriptionHistory(HistoryEvent historyEvent) {
		if (this.subscriptionHistory == null)
			this.subscriptionHistory = new SubscriptionHistory();
		this.subscriptionHistory.add(historyEvent);
	}

	public String getSubscriber() {
		return subscriber;
	}

	public SubscriptionHistory getSubscriptionHistory() {
		return subscriptionHistory;
	}
	
	public void setActivationRequestTimeNow() {
		subscriptionHistory.add(SubscriptionLifeCycleState.IN_ACTIVATION, System.currentTimeMillis()); 
	}
	
	public void setActivationRequestTimeFuture(long timestamp) {
		subscriptionHistory.add(SubscriptionLifeCycleState.IN_ACTIVATION, timestamp); 
	}
	
	public void setActivatedTimeNow() {
		subscriptionHistory.add(SubscriptionLifeCycleState.ACTIVE, System.currentTimeMillis()); 
	}
	
	public void setActivatedTimeFuture(long timestamp) {
		subscriptionHistory.add(SubscriptionLifeCycleState.ACTIVE, timestamp); 
	}
	
	public void setBarringSuspensionTimeNow() {
		subscriptionHistory.add(SubscriptionLifeCycleState.BARRING_SUSPENSION, System.currentTimeMillis()); 
	}
	
	public void setBarringSuspensionTimeFuture(long timestamp) {
		subscriptionHistory.add(SubscriptionLifeCycleState.BARRING_SUSPENSION, timestamp); 
	}
	
	public void setRenewalSuspensionTimeNow() {
		subscriptionHistory.add(SubscriptionLifeCycleState.RENEWAL_SUSPENSION, System.currentTimeMillis()); 
	}
	
	public void setRenewalSuspensionTimeFuture(long timestamp) {
		subscriptionHistory.add(SubscriptionLifeCycleState.RENEWAL_SUCCESS, timestamp); 
	}
	
	public void setRenewalSuccessTimeNow() {
		subscriptionHistory.add(SubscriptionLifeCycleState.RENEWAL_SUCCESS, System.currentTimeMillis()); 
	}
	
	public void setRenewalSuccessTimeFuture(long timestamp) {
		subscriptionHistory.add(SubscriptionLifeCycleState.RENEWAL_SUSPENSION, timestamp); 
	}
	
	public void setClosedTimeNow() {
		subscriptionHistory.add(SubscriptionLifeCycleState.CLOSED, System.currentTimeMillis()); 
	}
	
	public void setClosedTimeFuture(long timestamp) {
		subscriptionHistory.add(SubscriptionLifeCycleState.CLOSED, timestamp); 
	}
	
	
	

}
