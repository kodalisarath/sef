package com.ericsson.raso.sef.smart.subscription.response;

import java.util.Set;

import com.ericsson.sef.bes.api.entities.Offer;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class SubscriptionEventResponse extends AbstractSubscriptionResponse {

	private static final long serialVersionUID = -3985512985259212610L;
	
	
	private TransactionStatus fault;
	private Boolean result;
	private Offer subscription;
	private Offer offer;
	private Set<Offer> offers;
	private long monetaryAmount;
	private String currency;
	

	public TransactionStatus getFault() {
		return fault;
	}
	public void setFault(TransactionStatus fault) {
		this.fault = fault;
	}	
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}	
	public Offer getSubscription() {
		return subscription;
	}
	public void setSubscription(Offer subscription) {
		this.subscription = subscription;
	}
	public Offer getOffer() {
		return this.offer;
	}
	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	public Set<Offer> getOffers() {
		return offers;
	}
	public void setOffers(Set<Offer> offers) {
		this.offers = offers;
	}
	public long getMonetaryAmount() {
		return monetaryAmount;
	}
	public void setMonetaryAmount(long monetaryAmount) {
		this.monetaryAmount = monetaryAmount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@Override
	public String toString() {
		return "SubscriptionEventResponse [fault=" + fault + ", result=" + result + ", subscription=" + subscription + ", offer=" + offer
				+ ", offers=" + offers + ", monetaryAmount=" + monetaryAmount + ", currency=" + currency + "]";
	}
	
	
	
	
}
