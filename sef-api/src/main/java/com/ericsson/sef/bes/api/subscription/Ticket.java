package com.ericsson.sef.bes.api.subscription;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="ticket")
public class Ticket {
	private String ticketType;
	private String prt;
	private String limit;
	private long count;
	private Date expirationTime;
	private Date nextRenewalTime;
	private boolean usageUnlimited;
	public boolean isUsageUnlimited() {
		return usageUnlimited;
	}
	public void setUsageUnlimited(boolean usageUnlimited) {
		this.usageUnlimited = usageUnlimited;
	}
	public String getTicketType() {
		return ticketType;
	}
	public String getPrt() {
		return prt;
	}
	public String getLimit() {
		return limit;
	}
	public long getCount() {
		return count;
	}
	
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public void setPrt(String prt) {
		this.prt = prt;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public Date getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}
	public Date getNextRenewalTime() {
		return nextRenewalTime;
	}
	public void setNextRenewalTime(Date nextRenewalTime) {
		this.nextRenewalTime = nextRenewalTime;
	}
}
