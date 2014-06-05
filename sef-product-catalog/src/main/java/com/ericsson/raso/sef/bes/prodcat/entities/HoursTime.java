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
			return (this.numberOfHours * HOURS2MILLIS) + System.currentTimeMillis();
		else
			return (this.getActivationTime() + this.numberOfHours);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (numberOfHours ^ (numberOfHours >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!super.equals(obj))
			return false;
		
		if (!(obj instanceof HoursTime))
			return false;
		
		HoursTime other = (HoursTime) obj;
		if (numberOfHours != other.numberOfHours)
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "<TimeCharacteristic name='Hours' numberOfHours='" + this.numberOfHours + "'/>";
	}
	

}
