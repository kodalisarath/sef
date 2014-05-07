package com.ericsson.raso.sef.bes.prodcat.entities;

public class DaysTime extends AbstractTimeCharacteristic {
	private static final long serialVersionUID = 7360547973297183545L;

	private static final int DAYS2MILLIS = 86400000; // (24 hours * 60 mins * 60 secs * 1000 millis)

	private long numberOfHours = -1L;

	public DaysTime(int days) {
		super(Type.DAYS);

		this.numberOfHours = (days * DAYS2MILLIS);
	}

	@Override
	public long getExpiryTimeInMillis() {
		if (this.getActivationTime() == DISCOVERY_MODE)
			return this.numberOfHours;
		else
			return (this.getActivationTime() + this.numberOfHours);
	}

}
