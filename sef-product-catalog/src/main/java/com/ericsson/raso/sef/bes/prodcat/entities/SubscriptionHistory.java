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
	
	



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subscriptionHistory == null) ? 0 : subscriptionHistory.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;

		if (!(obj instanceof SubscriptionHistory))
			return false;
		
		SubscriptionHistory other = (SubscriptionHistory) obj;
		if (subscriptionHistory == null) {
			if (other.subscriptionHistory != null)
				return false;
		} else if (!subscriptionHistory.equals(other.subscriptionHistory))
			return false;
		
		return true;
	}





	class HistoryEvent implements Serializable {
		private static final long serialVersionUID = -2449277144684948258L;

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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((state == null) ? 0 : state.hashCode());
			result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			
			if (this == obj)
				return true;

			if (!(obj instanceof HistoryEvent))
				return false;
			
			HistoryEvent other = (HistoryEvent) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			
			if (state != other.state)
				return false;
			
			if (timestamp != other.timestamp)
				return false;
			
			return true;
		}

		private SubscriptionHistory getOuterType() {
			return SubscriptionHistory.this;
		}

		@Override
		public String toString() {
			return "HistoryEvent [state=" + state + ", timestamp=" + timestamp + "]";
		}
		
		

	}





	@Override
	public String toString() {
		return "SubscriptionHistory [subscriptionHistory=" + subscriptionHistory + "]";
	}


	

}
