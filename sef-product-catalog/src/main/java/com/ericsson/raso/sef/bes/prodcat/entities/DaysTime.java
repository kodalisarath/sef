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
		
		if (!(obj instanceof DaysTime))
			return false;
		
		DaysTime other = (DaysTime) obj;
		if (numberOfHours != other.numberOfHours)
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "<TimeCharacteristic name='Days' numberOfDays='" + ((int)(this.numberOfHours/24)) + "'/>";
	}
	
	

}
