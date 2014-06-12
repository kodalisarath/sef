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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (date ^ (date >>> 32));
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
		
		if (!(obj instanceof HardDateTime))
			return false;
		
		HardDateTime other = (HardDateTime) obj;
		if (date != other.date)
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "HardDateTime [date=" + date + "]";
	}
	

}
