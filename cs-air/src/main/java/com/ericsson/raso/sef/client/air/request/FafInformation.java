package com.ericsson.raso.sef.client.air.request;

import java.util.Date;

public class FafInformation extends NativeAirRequest {

	private String fafNumber;
	private String owner;
	private Date expiryDate;
	private Date expiryDateRelative;
	private Integer fafIndicator;
	private Integer offerID;
	private Date startDate;
	private Date startDateRelative;
	private Boolean exactMatch;

	public String getFafNumber() {
		return fafNumber;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public Date getExpiryDateRelative() {
		return expiryDateRelative;
	}

	public Integer getFafIndicator() {
		return fafIndicator;
	}

	public Integer getOfferID() {
		return offerID;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getStartDateRelative() {
		return startDateRelative;
	}

	public Boolean getExactMatch() {
		return exactMatch;
	}

	public void setFafNumber(String fafNumber) {
		this.fafNumber = fafNumber;
		addParam("fafNumber", fafNumber);

	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
		addParam("expiryDate", expiryDate);
	}

	public void setExpiryDateRelative(Date expiryDateRelative) {
		this.expiryDateRelative = expiryDateRelative;
		addParam("expiryDateRelative", expiryDateRelative);
	}

	public void setFafIndicator(Integer fafIndicator) {
		this.fafIndicator = fafIndicator;
		addParam("fafIndicator", fafIndicator);
	}

	public void setOfferID(Integer offerID) {
		this.offerID = offerID;
		addParam("offerID", offerID);
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		addParam("startDate", startDate);
	}

	public void setStartDateRelative(Date startDateRelative) {
		this.startDateRelative = startDateRelative;
		addParam("startDateRelative", startDateRelative);
	}

	public void setExactMatch(Boolean exactMatch) {
		this.exactMatch = exactMatch;
		addParam("exactMatch", exactMatch);
	}

	public String getOwner() {
		return owner;

	}

	public void setOwner(String owner) {
		this.owner = owner;
		addParam("owner", owner);
	}
}
