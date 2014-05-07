package com.ericsson.raso.sef.bes.prodcat.entities;

public final class InfiniteTime extends AbstractTimeCharacteristic {
	private static final long serialVersionUID = -3594492993588441352L;
	
	public InfiniteTime() {
		super(Type.INFINITE);
	}

	@Override
	public long getExpiryTimeInMillis() {
		return -1L;
	}


}
