package com.ericsson.raso.sef.smart.subscription.response;

import java.util.List;

import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class PurchaseResponse extends AbstractSubscriptionResponse {
	
	private TransactionStatus fault;
	private String subscriptionId;
	private List<Product> products;
	private List<Meta> billingMetas;
	
	public TransactionStatus getFault() {
		return fault;
	}
	public void setFault(TransactionStatus fault) {
		this.fault = fault;
	}
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public List<Meta> getBillingMetas() {
		return billingMetas;
	}
	public void setBillingMetas(List<Meta> billingMetas) {
		this.billingMetas = billingMetas;
	}
}
