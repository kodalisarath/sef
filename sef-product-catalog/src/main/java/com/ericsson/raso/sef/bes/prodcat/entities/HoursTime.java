package com.ericsson.raso.sef.bes.prodcat.entities;

public class HoursTime extends AbstractTimeCharacteristic {
	private static final long serialVersionUID = -6593942723657880011L;

	private static final int HOURS2MILLIS = 3600000; // (60 mins * 60 secs * 1000 millis)

	private long numberOfHours = -1L;

	public HoursTime(byte hours) {
		super(Type.HOURS);

		if (hours > 24)
			throw new IllegalArgumentException("Please use DAYS for this case. The given hours: " + hours
					+ " is already more than 24 hours!!");

		this.numberOfHours = (hours * HOURS2MILLIS);
	}

	@Override
	public long getExpiryTimeInMillis() {
		if (this.getActivationTime() == DISCOVERY_MODE)
			return this.numberOfHours;
		else
			return (this.getActivationTime() + this.numberOfHours);
	}

}
