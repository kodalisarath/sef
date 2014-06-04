package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.util.Map;


public final class HandleLifeCycleRequest extends AbstractRequest {
	private static final long	serialVersionUID	= -921884001442299482L;
	
	private String subscriberId = null;
	private Map<String,String>	metas = null;

	public HandleLifeCycleRequest(String requestCorrelator, String subscriberId, Map<String,String> metas) {
		super(requestCorrelator);
		this.subscriberId = subscriberId;
		this.setMetas(metas);
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	
	public com.ericsson.raso.sef.core.db.model.Subscriber persistableEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String,String> getMetas() {
		return metas;
	}

	public void setMetas(Map<String,String> metas) {
		this.metas = metas;
	}

}
