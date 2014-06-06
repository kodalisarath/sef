package com.ericsson.raso.sef.bes.prodcat.tasks;

import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;

public final class Fulfillment extends TransactionTask {
	private static final long serialVersionUID = 8908620047795202259L;

	private FulfillmentMode mode = null;

	private AtomicProduct atomicProduct = null;
	private String subscriberId = null;
	private Map<String, Object> additionalInputs = null;

	public Fulfillment(FulfillmentMode mode, AtomicProduct atomicProduct,
			String subscriberId, Map<String, Object> additionalInputs) {
		super(TaskType.FULFILLMENT);
		this.mode = mode;
		this.atomicProduct = atomicProduct;
		this.subscriberId = subscriberId;
		this.additionalInputs = additionalInputs;
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

	public Map<String, Object> getAdditionalInputs() {
		return additionalInputs;
	}

	public void setAdditionalInputs(Map<String, Object> additionalInputs) {
		this.additionalInputs = additionalInputs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((atomicProduct == null) ? 0 : atomicProduct.hashCode());
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result
				+ ((subscriberId == null) ? 0 : subscriberId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!super.equals(obj))
			return false;

		if (!(obj instanceof Fulfillment))
			return false;

		Fulfillment other = (Fulfillment) obj;
		if (atomicProduct == null) {
			if (other.atomicProduct != null)
				return false;
		} else if (!atomicProduct.equals(other.atomicProduct))
			return false;

		if (mode != other.mode)
			return false;

		if (subscriberId == null) {
			if (other.subscriberId != null)
				return false;
		} else if (!subscriberId.equals(other.subscriberId))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "Fulfillment [mode=" + mode + ", atomicProduct=" + atomicProduct + ", subscriberId=" + subscriberId + ", additionalInputs="
				+ additionalInputs + "]";
	}

}
