package com.ericsson.sef.bes.api.subscription;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "discoverUserOffer")
public class Offers {
	List<UserOffer> userOffers;

	public List<UserOffer> getUserOffers() {
		if(userOffers == null) {
			userOffers = new ArrayList<UserOffer>();
		}
		return userOffers;
	}
	public void setUserOffers(List<UserOffer> userOffers) {
		this.userOffers = userOffers;
	}
}
