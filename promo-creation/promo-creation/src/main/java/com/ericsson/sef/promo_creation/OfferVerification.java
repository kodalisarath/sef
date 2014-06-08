package com.ericsson.sef.promo_creation;

import java.util.Base64;

import com.ericsson.raso.sef.bes.prodcat.OfferContainer;
import com.ericsson.raso.sef.bes.prodcat.SecureSerializationHelper;
import com.ericsson.raso.sef.core.FrameworkException;

public class OfferVerification {

	public static void main(String[] args) {
		try {
			SecureSerializationHelper helper = new SecureSerializationHelper();
			OfferContainer offerStore = (OfferContainer) helper.fetchFromFile("/Users/esatnar/Documents/offerStore.ccm");
			Base64.Encoder encoder = Base64.getEncoder();
			
			System.out.println("Encrypted form: " + new String (encoder.encode(helper.encrypt("639777180104"))));
			
			System.out.println("\n1. Create Subscriber:\n" + offerStore.getOfferById("CREATE_SUBSCRIBER"));
			System.out.println("\n2. Read Subscriber:\n" + offerStore.getOfferById("READ_SUBSCRIBER"));
			
			
		} catch (FrameworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
