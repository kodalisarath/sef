package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;

public class PurchaseHistory implements Serializable {
	private static final long serialVersionUID = -5283675964917191934L;

	
	private List<HistoryEvent> purchaseHistory = null;
	


	public void add(HistoryEvent historyEvent) {
		if (this.purchaseHistory == null)
			this.purchaseHistory = new ArrayList<HistoryEvent>();
		
		this.purchaseHistory.add(historyEvent);
		
	}


	public void add(SubscriptionLifeCycleEvent event, long timestamp, MonetaryUnit chargedAmount) {
		if (this.purchaseHistory == null)
			this.purchaseHistory = new ArrayList<HistoryEvent>();
		
		HistoryEvent historyEvent = new HistoryEvent(event, timestamp, chargedAmount);
		this.purchaseHistory.add(historyEvent);
	}
	
	


	public List<HistoryEvent> getSubscriptionHistory() {
		return purchaseHistory;
	}

	public HistoryEvent getLastEvent(SubscriptionLifeCycleEvent event) {
		HistoryEvent returnEvent = null;
		for (HistoryEvent historyEvent: this.purchaseHistory) {
			if (historyEvent.event == event)
				returnEvent = historyEvent;
		}
		return returnEvent;
	}
		
	public HistoryEvent getFirstEvent(SubscriptionLifeCycleEvent event) {
		for (HistoryEvent historyEvent: this.purchaseHistory) {
			if (historyEvent.event == event)
				return historyEvent;
		}
		return null;
	}
		



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((purchaseHistory == null) ? 0 : purchaseHistory.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;

		if (!(obj instanceof PurchaseHistory))
			return false;
		
		PurchaseHistory other = (PurchaseHistory) obj;
		if (purchaseHistory == null) {
			if (other.purchaseHistory != null)
				return false;
		} else if (!purchaseHistory.equals(other.purchaseHistory))
			return false;
		
		return true;
	}





	class HistoryEvent implements Serializable {
		private static final long serialVersionUID = -2449277144684948258L;

		private SubscriptionLifeCycleEvent event = null;
		private long timestamp = -1L;
		private MonetaryUnit chargedAmount = null;

		public HistoryEvent(SubscriptionLifeCycleEvent event, long timestamp, MonetaryUnit chargedAmount) {
			this.event = event;
			this.timestamp = timestamp;
			this.chargedAmount = chargedAmount;
		}

		
		public SubscriptionLifeCycleEvent getEvent() {
			return event;
		}


		public long getTimestamp() {
			return timestamp;
		}
		
		


		public MonetaryUnit getChargedAmount() {
			return chargedAmount;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((event == null) ? 0 : event.hashCode());
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
			
			if (event != other.event)
				return false;
			
			if (timestamp != other.timestamp)
				return false;
			
			return true;
		}

		private PurchaseHistory getOuterType() {
			return PurchaseHistory.this;
		}
		
		

	}


	

}
