package com.ericsson.raso.sef.core.db.model.smart;


import java.util.Date;

import com.ericsson.raso.sef.core.db.model.CallingCircleRelation;

public class CallingCircle {

	private String owner;
	private String prodcatOffer;
	private String memberA;
	private String memberB;
	private CallingCircleRelation relationship;
	private String fafIndicator;
	private Date creationTime;
	private Date expiryTime;
	

	public CallingCircle() { }
	
	public CallingCircle(String owner, String prodcatOffer, String memberA, String memberB, CallingCircleRelation relationship,
			String fafIndicator) {
		super();
		this.owner = owner;
		this.prodcatOffer = prodcatOffer;
		this.memberA = memberA;
		this.memberB = memberB;
		this.relationship = relationship;
		this.fafIndicator = fafIndicator;
	}

	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getMemberA() {
		return memberA;
	}
	public void setMemberA(String memberA) {
		this.memberA = memberA;
	}
	public String getMemberB() {
		return memberB;
	}
	public void setMemberB(String memberB) {
		this.memberB = memberB;
	}
	public String getProdcatOffer() {
		return prodcatOffer;
	}
	public void setProdcatOffer(String prodcatOffer) {
		this.prodcatOffer = prodcatOffer;
	}
	public CallingCircleRelation getRelationship() {
		return relationship;
	}
	public void setRelationship(CallingCircleRelation relationship) {
		this.relationship = relationship;
	}
	public String getFafIndicator() {
		return fafIndicator;
	}
	public void setFafIndicator(String fafIndicator) {
		this.fafIndicator = fafIndicator;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}
	@Override
	public String toString() {
		return "CallingCircle [owner=" + owner + ", memberA=" + memberA + ", memberB=" + memberB + ", prodcatOffer=" + prodcatOffer
				+ ", relationship=" + relationship + ", creationTime=" + creationTime + ", expiryTime=" + expiryTime + "]";
	}
	
	

}
