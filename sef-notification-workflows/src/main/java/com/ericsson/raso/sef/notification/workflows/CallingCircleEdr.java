package com.ericsson.raso.sef.notification.workflows;

//

import java.util.Date;

import com.ericsson.raso.sef.core.db.model.CallingCircleRelation;

public class CallingCircleEdr {

	private String callingParty;
	private String calledParty;
	private String promoName;
	private CallingCircleRelation relationship;
	private Integer faFIndicatorValue;
	private Date expiry;
	private String status;
	private String reasonForInvalidCallAttempt;

	public CallingCircleEdr() {
	}

	public String getCallingParty() {
		return callingParty;
	}

	public void setCallingParty(String callingParty) {
		this.callingParty = callingParty;
	}

	public String getCalledParty() {
		return calledParty;
	}

	public void setCalledParty(String calledParty) {
		this.calledParty = calledParty;
	}

	public String getPromoName() {
		return promoName;
	}

	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}

	public CallingCircleRelation getRelationship() {
		return relationship;
	}

	public void setRelationship(CallingCircleRelation relationship) {
		this.relationship = relationship;
	}

	public Integer getFaFIndicatorValue() {
		return faFIndicatorValue;
	}

	public void setFaFIndicatorValue(Integer faFIndicatorValue) {
		this.faFIndicatorValue = faFIndicatorValue;
	}

	public Date  getExpiry() {
		return expiry;
	}

	public void setExpiry(Date  expiry) {
		this.expiry = expiry;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReasonForInvalidCallAttempt() {
		return reasonForInvalidCallAttempt;
	}

	public void setReasonForInvalidCallAttempt(String reasonForInvalidCallAttempt) {
		this.reasonForInvalidCallAttempt = reasonForInvalidCallAttempt;
	}
}
