package com.ericsson.raso.sef.bes.prodcat.entities;

public final class UnlimitedQuota extends AbstractQuotaCharacteristic {
		private static final long serialVersionUID = -2742643405964349074L;
	private static final long UNLIMITED = -1L;
	
	public UnlimitedQuota() {
		super(Type.UNLIMITED);
	}

@Override
	public long getDefinedQuota() {
		return UNLIMITED;
	}

	@Override
	public long getConsumedQuota() {
		return UNLIMITED;
	}

	@Override
	public long getRemainingQuota() {
		return UNLIMITED;
	}

	@Override
	public long consume(long units, boolean flexible) {
		return units;
	}
	
	
	

}
