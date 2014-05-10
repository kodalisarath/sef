package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;

import com.ericsson.raso.sef.bes.prodcat.SubscriptionException;

public abstract class AbstractQuotaCharacteristic implements Serializable {
	private static final long serialVersionUID = -3000726857332909230L;
	protected static final long DISCOVERY_MODE = -1L;

	private Type type = null;

	public AbstractQuotaCharacteristic(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	/**
	 * Returns the quota as defined in the product characteristic.
	 * 
	 * For unlimited quota, the returned value will be -1 signifying no limits
	 * 
	 * @return
	 */
	public abstract long getDefinedQuota();

	/**
	 * Returns the quota consumed from this atomic product.
	 * 
	 * For unlimited quota, the returned value will be -1 signifying no limits
	 * 
	 * @return
	 */
	public abstract long getConsumedQuota();

	/**
	 * Returns the quota remaining from this atomic product.
	 * 
	 * For unlimited quota, the returned value will be -1 signifying no limits
	 * 
	 * @return
	 */
	public abstract long getRemainingQuota();

	/**
	 * Allows a user to consume the quota provisioned. This method is usually an update to subscription status since most service
	 * consumption will not be intercepted by SEF BES.
	 * 
	 * If the defined Quota is less than requested Quota for consumption, then the default implementation is to reject with either an
	 * {@link SubscriptionException}. When <code>flexible</code> is set to true, then the implementation will consume all available remaining
	 * quota and will return the consumed value.
	 * 
	 * For unlimited quota, the return will be same as requested units, to avoid differential processing logic from the consumer.
	 * 
	 * @param units - amount requested to consume from this product's subscription
	 * @param flexible - allows the request to complete gracefully and functionally even when there is not enough quota remaining
	 * @return - the amount of units that was actually consumed when processing this request.
	 */
	public abstract long consume(long units, boolean flexible) throws SubscriptionException;

	enum Type {
		UNLIMITED, LIMITED;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!(obj instanceof AbstractQuotaCharacteristic))
			return false;
		
		AbstractQuotaCharacteristic other = (AbstractQuotaCharacteristic) obj;
		if (type != other.type)
			return false;
		return true;
	}
	
	
	
}
