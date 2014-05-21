package com.ericsson.raso.sef.bes.engine.transaction.entities;

import java.util.Map;


public final class GetAdviceOfChargeRequest extends AbstractRequest {
	private static final long serialVersionUID = -5742652213191533447L;
	
	private String offerId = null;
	private String susbcriberId = null;
	private Map<String, String> metas = null;

	public GetAdviceOfChargeRequest(String requestCorrelator, String offerId, String subscriberId, Map<String, String> metas) {
		super(requestCorrelator);
		this.offerId = offerId;
		this.susbcriberId = subscriberId;
		this.metas = metas;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String getSusbcriberId() {
		return susbcriberId;
	}

	public void setSusbcriberId(String susbcriberId) {
		this.susbcriberId = susbcriberId;
	}

	public Map<String, String> getMetas() {
		return metas;
	}

	public void setMetas(Map<String, String> metas) {
		this.metas = metas;
	}

	
			
}
