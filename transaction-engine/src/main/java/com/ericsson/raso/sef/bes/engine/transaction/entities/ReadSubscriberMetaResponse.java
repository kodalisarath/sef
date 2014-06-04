package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.util.Set;

import com.ericsson.sef.bes.api.entities.Meta;


public final class ReadSubscriberMetaResponse extends AbstractResponse {
	private static final long	serialVersionUID	= 1953619690977342269L;
	
	private Set<Meta> metas = null;

	
	public ReadSubscriberMetaResponse(String requestCorrelator) {
		super(requestCorrelator);
	}


	public Set<Meta> getMetas() {
		return metas;
	}


	public void setMetas(Set<Meta> metas) {
		this.metas = metas;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((metas == null) ? 0 : metas.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReadSubscriberMetaResponse other = (ReadSubscriberMetaResponse) obj;
		if (metas == null) {
			if (other.metas != null)
				return false;
		} else if (!metas.equals(other.metas))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "<ReadSubscriberMetaResponse metas='" + metas + "' />";
	}


	
}
