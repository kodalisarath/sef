package com.ericsson.sef.promo_creation;

import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.OfferContainer;
import com.ericsson.raso.sef.bes.prodcat.SecureSerializationHelper;
import com.ericsson.raso.sef.bes.prodcat.ServiceRegistry;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.Product;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.entities.Service;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.fulfillment.profiles.FulfillmentProfile;
import org.apache.commons.codec.binary.Base64;

public class OfferVerification {

	public static void main(String[] args) {
		try {
			SecureSerializationHelper helper = new SecureSerializationHelper();
			Base64 encoder = new Base64();
			
			OfferContainer offerStore = (OfferContainer) helper.fetchFromFile("C:\\Temp\\offerStore.ccm");
			Map<String, Resource> serviceRegistry = (Map<String, Resource>) helper.fetchFromFile("C:\\Temp\\serviceRegistry.ccm");
			Map<String, FulfillmentProfile<?>> profileRegistry = (Map<String, FulfillmentProfile<?>>) helper.fetchFromFile("C:\\Temp\\profileRegistry.ccm");
			
			System.out.println("Encrypted form: " + new String (encoder.encode(helper.encrypt("639777180104"))));
			
System.out.println("\n1. Create Subscriber:\n" + offerStore.getOfferById("CREATE_SUBSCRIBER"));
System.out.println("\n2. Read Subscriber:\n" + offerStore.getOfferById("READ_SUBSCRIBER"));
System.out.println("\n3. Modify Tagging:\n" + offerStore.getOfferById("MODIFY_TAGGING"));
System.out.println("\n4. Delete Subscriber:\n" + offerStore.getOfferById("DELETE_TAGGING"));
System.out.println("\n5. Welcome Pack:\n" + offerStore.getOfferById("SUBSCRIBE_PACKAGE_ITEM_WelcomePackServiceClass"));
System.out.println("\n6. Modify Tagging Reset bit:\n" + offerStore.getOfferById("MODIFY_SUBSCRIBER_TAGGING_SetResetBit"));

		
			
			System.out.println("Verifying all offers...");
			
			for(String offerId: offerStore.getOffersById().keySet()) {
				Offer taggingOffer = offerStore.getOfferById(offerId);
				//Offer taggingOffer = offerStore.getOfferById("katok-AT25-TEX25");
				System.out.println("\nOffer name: " + taggingOffer.getName());
				System.out.println("Offer contents: " + taggingOffer);
				for (AtomicProduct product: taggingOffer.getAllAtomicProducts()) {
					System.out.println("Product name: " + product.getName());
					System.out.println("Product resource: " + product.getResource());
					System.out.println("Service Registry Instance: " + serviceRegistry.get(product.getResource().getName()));
					System.out.println("Service Registry Instance Type: " + serviceRegistry.get(product.getResource().getName()));

					Service resource = (Service) product.getResource();
					System.out.println("Resource name: " + resource.getName());

					for (String profileId: resource.getFulfillmentProfiles()) {
						System.out.println("Profile Id: " + profileId);
						FulfillmentProfile profile = profileRegistry.get(profileId);
						System.out.println("Profile Registry class: " + profile.getClass().getCanonicalName());
						System.out.println("Profile Registry contents: " + profile.toString());
					}
				}
			}
			
		} catch (FrameworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
