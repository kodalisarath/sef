package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.io.Serializable;

public class DedicatedAccountReversal implements Serializable {
	private static final long serialVersionUID = 7236937351035766353L;

	Integer dedicatedAccountInformationID = null;
	Integer hoursToReverse = null;
	Integer amountToReverse = null;
	
	public DedicatedAccountReversal() { }
	
	public DedicatedAccountReversal(Integer dedicatedAccountInformationID, Integer hoursToReverse, Integer amountToReverse) {
		super();
		this.dedicatedAccountInformationID = dedicatedAccountInformationID;
		this.hoursToReverse = hoursToReverse;
		this.amountToReverse = amountToReverse;
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

	public void setMinutesToReverse(Integer hoursToReverse) {
		this.hoursToReverse = hoursToReverse * 60000;
	}
	
	public Integer getAmountToReverse() {
		return amountToReverse;
	}

	public void setAmountToReverse(Integer amountToReverse) {
		this.amountToReverse = amountToReverse;
	}

	@Override
	public String toString() {
		return "DedicatedAccountReversal [dedicatedAccountInformationID=" + dedicatedAccountInformationID + ", hoursToReverse="
				+ hoursToReverse + ", amountToReverse=" + amountToReverse + "]";
	}
	
	
	
	
}
