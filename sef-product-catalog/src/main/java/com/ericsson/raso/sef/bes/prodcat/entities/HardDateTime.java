package com.ericsson.raso.sef.bes.prodcat.entities;

import java.util.Date;

public class HardDateTime extends AbstractTimeCharacteristic {
	private static final long serialVersionUID = -3951716171852300741L;

	private long date = -1L;

	public HardDateTime(Date date) {
		super(Type.DATE);

		this.date = date.getTime();
	}

	@Override
	public long getExpiryTimeInMillis() {
		return this.date;
	}

}
