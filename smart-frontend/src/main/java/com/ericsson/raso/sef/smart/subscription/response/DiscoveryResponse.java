package com.ericsson.raso.sef.smart.subscription.response;

import com.ericsson.sef.bes.api.entities.Offer;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class DiscoveryResponse extends AbstractSubscriptionResponse {

	private static final long serialVersionUID = -3985512985259212610L;
	
	
	private TransactionStatus fault;
	private Offer offer;
	
	public TransactionStatus getFault() {
		return fault;
	}
	public void setFault(TransactionStatus fault) {
		this.fault = fault;
	}
	public Offer getOffer() {
		return offer;
	}
	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	
}
