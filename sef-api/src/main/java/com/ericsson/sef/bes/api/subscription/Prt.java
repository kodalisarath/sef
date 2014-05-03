package com.ericsson.sef.bes.api.subscription;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prt")
public class Prt {
	private FulfillmentProfile fulfillmentProfile;

	public FulfillmentProfile getFulfillmentProfile() {
		return fulfillmentProfile;
	}

	public void setFulfillmentProfile(FulfillmentProfile fulfillmentProfile) {
		this.fulfillmentProfile = fulfillmentProfile;
	}

}
