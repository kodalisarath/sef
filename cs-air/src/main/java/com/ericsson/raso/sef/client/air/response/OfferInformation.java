package com.ericsson.raso.sef.client.air.response;

import java.util.Date;
import java.util.Map;

public class OfferInformation extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public OfferInformation(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private Integer offerID;
	private Date startDate;
	private Date expiryDate;
	private Date startDateTime;
	private Date expiryDateTime;

	public Integer getOfferID() {
		if (offerID == null) {
			offerID = getParam("offerID", Integer.class);
		}
		return offerID;
	}

	public Date getStartDateTime() {
		if (startDateTime == null) {
			startDateTime = getParam("startDateTime", Date.class);
		}
		return startDateTime;
	}

	public Date getExpiryDateTime() {
		if (expiryDateTime == null) {
			expiryDateTime = getParam("expiryDateTime", Date.class);
		}
		return expiryDateTime;
	}

	public Date getStartDate() {
		if (startDate == null) {
			startDate = getParam("startDate", Date.class);
		}
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getExpiryDate() {
		if (expiryDate == null) {
			expiryDate = getParam("expiryDate", Date.class);
		}
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public void setExpiryDateTime(Date expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}

	@Override
	public String toString() {
		return "OfferInformation [offerID=" + offerID + ", startDate=" + startDate + ", expiryDate=" + expiryDate + ", startDateTime="
				+ startDateTime + ", expiryDateTime=" + expiryDateTime + "]";
	}
	
	
	
}
