package com.ericsson.sef.bes.api.subscription;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "offerTicket")
public class OfferTicket {
	private String description;
	private List<Prt> prt;
	private String quota;
	private Date validity;
	private Date expiryDate;

	public String getDescription() {
		return description;
	}

	public List<Prt> getPrt() {
		if(prt == null){
			prt = new ArrayList<Prt>();
		}
		return prt;
	}

	public String getQuota() {
		return quota;
	}

	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrt(List<Prt> prt) {
		this.prt = prt;
	}

	public void setQuota(String quota) {
		this.quota = quota;
	}

}
