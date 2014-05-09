package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleState;

public class SubscriptionHistory implements Serializable {
	private static final long serialVersionUID = -5924208797026756224L;

	private List<HistoryEvent> subscriptionHistory = null;
	


	public void add(HistoryEvent historyEvent) {
		if (this.subscriptionHistory == null)
			this.subscriptionHistory = new ArrayList<HistoryEvent>();
		
		this.subscriptionHistory.add(historyEvent);
		
	}


	public void add(SubscriptionLifeCycleState state, long timestamp) {
		if (this.subscriptionHistory == null)
			this.subscriptionHistory = new ArrayList<HistoryEvent>();
		
		HistoryEvent historyEvent = new HistoryEvent(state, timestamp);
		this.subscriptionHistory.add(historyEvent);
	}
	
	


	public List<HistoryEvent> getSubscriptionHistory() {
		return subscriptionHistory;
	}

	
	public long getActivationTime() throws CatalogException {
		Iterator<HistoryEvent> scanHistory = this.subscriptionHistory.iterator();
		while (scanHistory.hasNext()) {
			HistoryEvent event = scanHistory.next();
			if (event.state == SubscriptionLifeCycleState.ACTIVE)
				return event.timestamp;
		}
		throw new CatalogException("This subscription does not seem to have been successfully activated!!");
	}
	
	public long getLastRenewalTime() throws CatalogException {
		long renewalTime = 0;
		Iterator<HistoryEvent> scanHistory = this.subscriptionHistory.iterator();
		while (scanHistory.hasNext()) {
			HistoryEvent event = scanHistory.next();
			if (event.state == SubscriptionLifeCycleState.RENEWAL_SUCCESS)
				renewalTime = event.timestamp;
		}
		
		return renewalTime;
	}
	
	public long getRenewalCount() throws CatalogException {
		long renewalCount = 0;
		Iterator<HistoryEvent> scanHistory = this.subscriptionHistory.iterator();
		while (scanHistory.hasNext()) {
			HistoryEvent event = scanHistory.next();
			if (event.state == SubscriptionLifeCycleState.RENEWAL_SUCCESS)
				renewalCount++;
		}
		
		return renewalCount;
	}
	
	



	class HistoryEvent {

		private SubscriptionLifeCycleState state = null;
		private long timestamp = -1L;

		public HistoryEvent(SubscriptionLifeCycleState state, long timestamp) {
			this.state = state;
			this.timestamp = timestamp;
		}

		public SubscriptionLifeCycleState getState() {
			return state;
		}

		public long getTimestamp() {
			return timestamp;
		}

	}


	

}
