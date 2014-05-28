package com.ericsson.raso.sef.bes.prodcat.tasks;

import com.ericsson.raso.sef.bes.prodcat.entities.MonetaryUnit;

public final class Charging extends TransactionTask {
	private static final long serialVersionUID = 5352309838014664602L;

	private ChargingMode mode = null;

	private MonetaryUnit charging = null;
	private String subscriberId = null;
	
	public Charging(ChargingMode mode, MonetaryUnit charging, String subscriberId) {
		super(TaskType.CHARGING);
		this.mode = mode;
		this.charging = charging;
		this.subscriberId = subscriberId;
	}
	
	

	public MonetaryUnit getCharging() {
		return charging;
	}

	public void setCharging(MonetaryUnit charging) {
		this.charging = charging;
	}

	public ChargingMode getMode() {
		return mode;
	}

	public void setMode(ChargingMode mode) {
		this.mode = mode;
	}



	public String getSubscriberId() {
		return subscriberId;
	}



	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	
	
}
