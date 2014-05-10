package com.ericsson.raso.sef.client.air.request;

import java.util.Date;

import com.ericsson.raso.sef.plugin.xmlrpc.XmlRpcInt;

public class UpdateOfferRequest extends AbstractAirRequest {

	public UpdateOfferRequest() {
		super("UpdateOffer");
	}

	private Integer offerID;
	private Integer offerType;
	private Date startDateTime;
	private Date expiryDateTime;
	private Date expiryDate;
	private Integer expiryDateRelative;
	private XmlRpcInt expiryDateTimeRelative;

	public Integer getOfferID() {
		return offerID;
	}

	public void setOfferID(Integer offerID) {
		this.offerID = offerID;
		addParam("offerID", offerID);
		
	}

	public Integer getOfferType() {
		return offerType;
	}

	public void setOfferType(Integer offerType) {
		this.offerType = offerType;
		addParam("offerType", offerType);		
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
		addParam("startDateTime", startDateTime);		
	}

	public Date getExpiryDateTime() {
		return expiryDateTime;
	}

	public void setExpiryDateTime(Date expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
		addParam("expiryDateTime", expiryDateTime);
	}

	public Integer getExpiryDateRelative() {
		return expiryDateRelative;
	}

	public void setExpiryDateRelative(Integer expiryDateRelative) {
		this.expiryDateRelative = expiryDateRelative;
		addParam("expiryDateRelative", expiryDateRelative);
	}

	public XmlRpcInt getExpiryDateTimeRelative() {
		return expiryDateTimeRelative;
	}

	public void setExpiryDateTimeRelative(XmlRpcInt expiryDateTimeRelative) {
		this.expiryDateTimeRelative = expiryDateTimeRelative;
		addParam("expiryDateTimeRelative", expiryDateTimeRelative);
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
		addParam("expiryDate", expiryDate);
	}
}
