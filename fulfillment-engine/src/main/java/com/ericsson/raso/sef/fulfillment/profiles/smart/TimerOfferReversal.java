package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.io.Serializable;

public class TimerOfferReversal implements Serializable {
	private static final long serialVersionUID = 7236937351035766353L;

	Integer offerID = null;
	Integer dedicatedAccountInformationID = null;
	Integer hoursToReverse = null;
	
	public TimerOfferReversal() { }
	
	public TimerOfferReversal(Integer offerID, Integer dedicatedAccountInformationID, Integer hoursToReverse) {
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

	public Integer getHoursToReverse() {
		return hoursToReverse / 3600000;
	}

	public void setHoursToReverse(Integer hoursToReverse) {
		this.hoursToReverse = hoursToReverse * 3600000;
	}
	
	
	public Integer getMinutesToReverse() {
		return hoursToReverse / 60000;
	}

	public void setMinutessToReverse(Integer minutesToReverse) {
		this.hoursToReverse = minutesToReverse * 60000;
	}
	
	
	

	@Override
	public String toString() {
		return "TimerOfferReversal [dedicatedAccountInformationID=" + dedicatedAccountInformationID + ", hoursToReverse=" + hoursToReverse
				+ "]";
	}

	
	
}
