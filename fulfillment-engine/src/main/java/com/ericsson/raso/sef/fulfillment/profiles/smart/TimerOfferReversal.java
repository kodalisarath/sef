package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.io.Serializable;

public class TimerOfferReversal implements Serializable {
	private static final long serialVersionUID = 7236937351035766353L;

	Integer offerID = null;
	Integer dedicatedAccountInformationID = null;
	Long millisToReverse = null;
	
	public TimerOfferReversal() { }
	
	public TimerOfferReversal(Integer offerID, Integer dedicatedAccountInformationID, Long hoursToReverse) {
		super();
		this.offerID = offerID;
		this.dedicatedAccountInformationID = dedicatedAccountInformationID;
		this.millisToReverse = ((hoursToReverse * 3600000L));
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
	
	public Long getMillisToReverse() {
		return this.millisToReverse;
	}

	public Long getHoursToReverse() {
		return (this.millisToReverse / 3600000L);
	}

	public void setHoursToReverse(Integer hoursToReverse) {
		this.millisToReverse = (hoursToReverse * 3600000L);
	}
	
	
	public Long getMinutesToReverse() {
		return (this.millisToReverse / 60000L);
	}

	public void setMinutessToReverse(long minutesToReverse) {
		this.millisToReverse = (minutesToReverse * 60000L);
	}
	
	
	

	@Override
	public String toString() {
		return "TimerOfferReversal [dedicatedAccountInformationID=" + dedicatedAccountInformationID + ", hoursToReverse=" + millisToReverse
				+ "]";
	}

	
	
}
