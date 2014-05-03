package com.ericsson.sef.bes.api.subscription;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscription")
public class Subscription {
	private List<SubscriptionDetail> subscriptionDetails;
	
	public List<SubscriptionDetail> getSubscriptionDetails() {
		if(subscriptionDetails == null) {
			subscriptionDetails = new ArrayList<SubscriptionDetail>();
		}
		return subscriptionDetails;
	}
	public void setSubscriptionDetails(List<SubscriptionDetail> subscriptionDetails) {
		this.subscriptionDetails = subscriptionDetails;
	}
}
