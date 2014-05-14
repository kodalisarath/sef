package com.ericsson.raso.sef.bes.prodcat.tasks;

import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;

public final class Fulfillment extends TransactionTask {
	private static final long serialVersionUID = 8908620047795202259L;

	private FulfillmentMode mode = null;

	private AtomicProduct atomicProduct = null;
	private String subscriberId = null;
	
	public Fulfillment(FulfillmentMode mode, AtomicProduct atomicProduct, String subscriberId) {
		super(Type.FULFILLMENT);
		this.mode = mode;
		this.atomicProduct = atomicProduct;
		this.subscriberId = subscriberId;
	}

	public FulfillmentMode getMode() {
		return mode;
	}

	public void setMode(FulfillmentMode mode) {
		this.mode = mode;
	}

	public AtomicProduct getAtomicProduct() {
		return atomicProduct;
	}

	public void setAtomicProduct(AtomicProduct atomicProduct) {
		this.atomicProduct = atomicProduct;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	
	

		
}
