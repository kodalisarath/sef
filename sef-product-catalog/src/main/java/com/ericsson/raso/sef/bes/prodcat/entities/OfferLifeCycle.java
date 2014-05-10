package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public class OfferLifeCycle implements Serializable {
	private static final long serialVersionUID = -3684779285289029622L;

	private Map<State, Long> lifecycle = null;

	public OfferLifeCycle() {
		lifecycle = new TreeMap<State, Long>();
		lifecycle.put(State.IN_CREATION, null);
		lifecycle.put(State.TESTING, null);
		lifecycle.put(State.PUBLISHED, null);
		lifecycle.put(State.RETIRED, null);
	}

	public void setCreationTime(long timestamp) {
		this.lifecycle.put(State.IN_CREATION, timestamp);
	}

	public long getCreationTime() {
		return this.lifecycle.get(State.IN_CREATION);
	}

	public void setTestingTime(long timestamp) throws CatalogException {
		long creation = this.lifecycle.get(State.IN_CREATION);
		if (timestamp <= creation)
			throw new CatalogException("Testing state cannot have started before Creation!!");

		this.lifecycle.put(State.TESTING, timestamp);
	}

	public long getTestingTime() {
		return this.lifecycle.get(State.TESTING);
	}

	public void setPublishedTime(long timestamp) throws CatalogException {
		long creation = this.lifecycle.get(State.IN_CREATION);
		if (timestamp <= creation)
			throw new CatalogException("Published state cannot have started before Creation!!");

		long testing = this.lifecycle.get(State.TESTING);
		if (timestamp <= testing)
			throw new CatalogException("Published state cannot have started before Testing!!");

		this.lifecycle.put(State.PUBLISHED, timestamp);
	}

	public long getPublishedTime() {
		return this.lifecycle.get(State.PUBLISHED);
	}

	public void setRetiredTime(long timestamp) throws CatalogException {
		long creation = this.lifecycle.get(State.IN_CREATION);
		if (timestamp <= creation)
			throw new CatalogException("Retired state cannot have started before Creation!!");

		long testing = this.lifecycle.get(State.TESTING);
		if (timestamp <= testing)
			throw new CatalogException("Retired state cannot have started before Testing!!");

		long published = this.lifecycle.get(State.PUBLISHED);
		if (timestamp <= published)
			throw new CatalogException("Retired state cannot have started before Publishing!!");

		this.lifecycle.put(State.RETIRED, timestamp);
	}

	public long getRetiredTime() {
		return this.lifecycle.get(State.RETIRED);
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

	

}
