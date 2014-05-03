package com.ericsson.sef.bes.api.subscription;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "userOffer")
public class UserOffer {
	private String productId;
	private String description;
	private String price;
	private boolean recurrent;
	private List<OfferTicket> offerTickets;

	public String getProductId() {
		return productId;
	}

	public String getDescription() {
		return description;
	}

	public String getPrice() {
		return price;
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

	public void setPrice(String price) {
		this.price = price;
	}

	public void setRecurrent(boolean recurrent) {
		this.recurrent = recurrent;
	}

	public List<OfferTicket> getOfferTickets() {
		if (offerTickets == null) {
			offerTickets = new ArrayList<OfferTicket>();
		}
		return offerTickets;
	}

	public void setOfferTickets(List<OfferTicket> offerTickets) {
		this.offerTickets = offerTickets;
	}

}
