package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.TreeSet;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public class OfferLifeCycle implements Serializable {
	private static final long serialVersionUID = -3684779285289029622L;

	private TreeSet<HistoryEvent> lifecycle = new TreeSet<HistoryEvent>();;

	public void setCreationTime(long timestamp) {
		this.lifecycle.add(new HistoryEvent(State.IN_CREATION, timestamp));
	}

	public long getCreationTime() {
		for (HistoryEvent event : this.lifecycle) {
			if (event.state == State.IN_CREATION)
				return event.timestamp;
		}
		return -1;
	}

	public void setTestingTime(long timestamp) throws CatalogException {
		long creation = this.getCreationTime();
		if (timestamp <= creation)
			throw new CatalogException("Testing state cannot have started before Creation!!");

		this.lifecycle.add(new HistoryEvent(State.TESTING, timestamp));
	}

	public long getTestingTime() {
		for (HistoryEvent event : this.lifecycle) {
			if (event.state == State.TESTING)
				return event.timestamp;
		}
		return -1;
	}

	public void setPublishedTime(long timestamp) throws CatalogException {
		long creation = this.getCreationTime();
		if (timestamp <= creation)
			throw new CatalogException("Published state cannot have started before Creation!!");

		long testing = this.getTestingTime();
		long lastDisabled = this.getLastDisabledTime();
		if (timestamp <= testing || timestamp <= lastDisabled)
			throw new CatalogException("Published state cannot have started before Testing or last Disabled!!");

		this.lifecycle.add(new HistoryEvent(State.PUBLISHED, timestamp));
	}

	public long getPublishedTime() {
		for (HistoryEvent event : this.lifecycle) {
			if (event.state == State.PUBLISHED)
				return event.timestamp;
		}
		return -1;
	}

	public void setRetiredTime(long timestamp) throws CatalogException {
		long creation = this.getCreationTime();
		if (timestamp <= creation)
			throw new CatalogException("Retired state cannot have started before Creation!!");

		long testing = this.getTestingTime();
		if (timestamp <= testing)
			throw new CatalogException("Retired state cannot have started before Testing!!");

		this.lifecycle.add(new HistoryEvent(State.RETIRED, timestamp));
	}
	
	public long getLastDisabledTime() {
		long timestamp = -1;
		for (HistoryEvent event : this.lifecycle) {
			if (event.state == State.DISABLED)
				timestamp = event.timestamp;
		}
		return timestamp;
		
	}

	public void setDisabledTime(long timestamp) throws CatalogException {
		long testing = this.getTestingTime();
		long published = this.getPublishedTime();
		if (timestamp <= testing || timestamp <= published)
			throw new CatalogException("Disabled state cannot have started before Testing or Publishing!!");

		this.lifecycle.add(new HistoryEvent(State.DISABLED, timestamp));
	}

	
	public void promote(State newState, long timestamp) throws CatalogException {
		switch (newState) {
			case IN_CREATION:
				this.setCreationTime(timestamp);
				break;
			case TESTING:
				this.setTestingTime(timestamp);
				break;
			case DISABLED:
				this.setDisabledTime(timestamp);
				break;
			case PUBLISHED:
				this.setPublishedTime(timestamp);
				break;
			case RETIRED:
				this.setRetiredTime(timestamp);
				break;
			default:
				throw new CatalogException("Seems like a hack. UNknown constant in a pre-defined enum!!!");
		}
	}

	public long getRetiredTime() {
		for (HistoryEvent event : this.lifecycle) {
			if (event.state == State.RETIRED)
				return event.timestamp;
		}
		return -1;
	}

	public long getEarliestTime(State state) {
		for (HistoryEvent event : this.lifecycle) {
			if (event.state == state)
				return event.timestamp;
		}
		return -1;

	}

	public long getLastTime(State state) {
		long timestamp = -1;
		for (HistoryEvent event : this.lifecycle) {
			if (event.state == state)
				timestamp = event.timestamp;
		}
		return timestamp;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lifecycle == null) ? 0 : lifecycle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof OfferLifeCycle))
			return false;

		OfferLifeCycle other = (OfferLifeCycle) obj;
		if (lifecycle == null) {
			if (other.lifecycle != null)
				return false;
		} else if (!lifecycle.equals(other.lifecycle))
			return false;

		return true;
	}

	class HistoryEvent implements Serializable, Comparable<HistoryEvent> {
		private static final long serialVersionUID = -6905134376367890464L;

		private State state = null;
		private long timestamp = -1l;

		public HistoryEvent(State state, long timestamp) {
			super();
			this.state = state;
			this.timestamp = timestamp;
		}

		public State getState() {
			return state;
		}

		public void setState(State state) {
			this.state = state;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
		
		

		@Override
		public int compareTo(HistoryEvent other) {
			if (this.equals(other)) {
				return 0;
			}
			
			if (this.state == other.state) {
				return (int) ((this.timestamp - other.timestamp) & Integer.MAX_VALUE);
			} else {
				return (this.state.intValue() - other.state.intValue());
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((state == null) ? 0 : state.hashCode());
			result = prime * result + (int) (timestamp);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
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

		private OfferLifeCycle getOuterType() {
			return OfferLifeCycle.this;
		}
		
		

	}

}
