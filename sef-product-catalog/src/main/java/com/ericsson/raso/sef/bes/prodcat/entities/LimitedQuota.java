package com.ericsson.raso.sef.bes.prodcat.entities;

import com.ericsson.raso.sef.bes.prodcat.SubscriptionException;

public final class LimitedQuota extends AbstractQuotaCharacteristic {
	private static final long serialVersionUID = -2742643405964349074L;

	private long definedQuota = -1L;
	private long consumedQuota = -1L;

	public LimitedQuota() {
		super(Type.LIMITED);
	}

	@Override
	public long getDefinedQuota() {
		return this.definedQuota;
	}

	@Override
	public long getConsumedQuota() {
		return this.consumedQuota;
	}

	@Override
	public long getRemainingQuota() {
		return (this.definedQuota - this.consumedQuota);
	}

	@Override
	public long consume(long units, boolean flexible) throws SubscriptionException {
		if ((this.definedQuota - this.consumedQuota - units) > 0) {
			this.consumedQuota += units;
			return units;
		} else {
			long available = (this.definedQuota - this.consumedQuota);
			if (flexible) {
				this.consumedQuota = this.definedQuota;
				return (units - available);
			}
			throw new SubscriptionException("Available quota: " + available + " and request");
		}
	}

}
