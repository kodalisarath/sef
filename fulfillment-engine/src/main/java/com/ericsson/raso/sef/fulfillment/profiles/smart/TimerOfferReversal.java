package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.io.Serializable;

public class TimerOfferReversal implements Serializable {
	private static final long serialVersionUID = 7236937351035766353L;

	Integer dedicatedAccountInformationID = null;
	Integer hoursToReverse = null;
	
	public TimerOfferReversal() { }
	
	public TimerOfferReversal(Integer dedicatedAccountInformationID, Integer hoursToReverse) {
		super();
		this.dedicatedAccountInformationID = dedicatedAccountInformationID;
		this.hoursToReverse = hoursToReverse;
	}

	public Integer getDedicatedAccountInformationID() {
		return dedicatedAccountInformationID;
	}

	public void setDedicatedAccountInformationID(Integer dedicatedAccountInformationID) {
		this.dedicatedAccountInformationID = dedicatedAccountInformationID;
	}

	public Integer getHoursToReverse() {
		return hoursToReverse;
	}

	public void setHoursToReverse(Integer hoursToReverse) {
		this.hoursToReverse = hoursToReverse;
	}

	@Override
	public String toString() {
		return "TimerOfferReversal [dedicatedAccountInformationID=" + dedicatedAccountInformationID + ", hoursToReverse=" + hoursToReverse
				+ "]";
	}

	
	
}
