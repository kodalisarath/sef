package com.ericsson.sef.bes.api.subscription;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subscriptionDetail")
public class SubscriptionDetail {
	private String productId;
	private String description;
	private String purchaseId;
	private String purchaseType;
	private String price;
	private String latestReurrentId;
	private boolean recurrent;
	private Date expirationTime;
	private Date recurrentPurchaseDate;
	private List<Ticket> tickets;
	

	public String getProductId() {
		return productId;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public Boolean getRecurrent() {
		return recurrent;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public void setRecurrent(Boolean recurrent) {
		this.recurrent = recurrent;
	}

	public String getDescription() {
		return description;
	}

	public String getPurchaseId() {
		return purchaseId;
	}

	public String getPrice() {
		return price;
	}

	public String getLatestReurrentId() {
		return latestReurrentId;
	}

	public boolean isRecurrent() {
		return recurrent;
	}

	

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setLatestReurrentId(String latestReurrentId) {
		this.latestReurrentId = latestReurrentId;
	}

	public void setRecurrent(boolean recurrent) {
		this.recurrent = recurrent;
	}

	
	public List<Ticket> getTickets() {
		if(tickets == null) {
			tickets = new ArrayList<Ticket>();
		}
		return tickets;
	}
	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Date getRecurrentPurchaseDate() {
		return recurrentPurchaseDate;
	}

	public void setRecurrentPurchaseDate(Date recurrentPurchaseDate) {
		this.recurrentPurchaseDate = recurrentPurchaseDate;
	}


}
