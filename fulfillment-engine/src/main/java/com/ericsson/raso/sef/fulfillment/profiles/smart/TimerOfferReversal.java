package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.io.Serializable;

public class TimerOfferReversal implements Serializable {
	private static final long serialVersionUID = 7236937351035766353L;

	Integer offerID = null;
	Integer dedicatedAccountInformationID = null;
	Long hoursToReverse = null;
	
	public TimerOfferReversal() { }
	
	public TimerOfferReversal(Integer offerID, Integer dedicatedAccountInformationID, Long hoursToReverse) {
		super();
		this.offerID = offerID;
		this.dedicatedAccountInformationID = dedicatedAccountInformationID;
		this.hoursToReverse = hoursToReverse;
	}


	public Integer getOfferID() {
		return offerID;
	}

	public void setOfferID(Integer offerID) {
		this.offerID = offerID;
	}

	public Integer getDedicatedAccountInformationID() {
		return dedicatedAccountInformationID;
	}

	public void setDedicatedAccountInformationID(Integer dedicatedAccountInformationID) {
		this.dedicatedAccountInformationID = dedicatedAccountInformationID;
	}

	public Long getHoursToReverse() {
		return (hoursToReverse / 3600000L) & Long.MAX_VALUE;
	}

	public void setHoursToReverse(Integer hoursToReverse) {
		this.hoursToReverse = (hoursToReverse * 3600000L) & Long.MAX_VALUE;
	}
	
	
	public Long getMinutesToReverse() {
		return (this.hoursToReverse / 60000L) & Long.MAX_VALUE;
	}

	public void setMinutessToReverse(long minutesToReverse) {
		this.hoursToReverse = (minutesToReverse * 60000L) & Long.MAX_VALUE;
	}
	
	
	

	@Override
	public String toString() {
		return "TimerOfferReversal [dedicatedAccountInformationID=" + dedicatedAccountInformationID + ", hoursToReverse=" + hoursToReverse
				+ "]";
	}

	
	
}
