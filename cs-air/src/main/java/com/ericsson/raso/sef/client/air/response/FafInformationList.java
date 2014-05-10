package com.ericsson.raso.sef.client.air.response;

import java.util.Date;
import java.util.Map;

public class FafInformationList extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public FafInformationList(Map<String, Object> paramMap) {
		super(paramMap);
	}

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
		if (fafNumber == null) {
			fafNumber = getParam("fafNumber", String.class);
		}
		return fafNumber;
	}

	public String getOwner() {
		if (owner == null) {
			owner = getParam("owner", String.class);
		}
		return owner;
	}

	public Date getExpiryDate() {
		if (expiryDate == null) {
			expiryDate = getParam("expiryDate", Date.class);
		}
		return expiryDate;
	}

	public Date getExpiryDateRelative() {
		if (expiryDateRelative == null) {
			expiryDateRelative = getParam("expiryDateRelative", Date.class);
		}
		return expiryDateRelative;
	}

	public Integer getFafIndicator() {
		if (fafIndicator == null) {
			fafIndicator = getParam("fafIndicator", Integer.class);
		}
		return fafIndicator;
	}

	public Integer getOfferID() {
		if (offerID == null) {
			offerID = getParam("offerID", Integer.class);
		}
		return offerID;
	}

	public Date getStartDate() {
		if (startDate == null) {
			startDate = getParam("startDate", Date.class);
		}
		return startDate;
	}

	public Date getStartDateRelative() {
		if (startDateRelative == null) {
			startDateRelative = getParam("startDateRelative", Date.class);
		}
		return startDateRelative;
	}

	public Boolean getExactMatch() {
		if (exactMatch) {
			exactMatch = getParam("exactMatch", Boolean.class);
		}
		return exactMatch;
	}

}
