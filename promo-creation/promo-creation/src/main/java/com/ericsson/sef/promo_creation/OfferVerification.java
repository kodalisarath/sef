package com.ericsson.sef.promo_creation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

	public static void main(String[] args) throws IOException, NoSuchFieldException, SecurityException {
		
		BufferedWriter bw = null;
		File outfile = new File("C:\\Temp\\offerVerification.txt");
		
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		
		FileWriter fw = new FileWriter(outfile);
		
		bw = new BufferedWriter(fw);
		try {
			SecureSerializationHelper helper = new SecureSerializationHelper();
			Base64 encoder = new Base64();
			
			OfferContainer offerStore = (OfferContainer) helper.fetchFromFile("C:\\Temp\\offerStore.ccm");
			Map<String, Resource> serviceRegistry = (Map<String, Resource>) helper.fetchFromFile("C:\\Temp\\serviceRegistry.ccm");
			Map<String, FulfillmentProfile<?>> profileRegistry = (Map<String, FulfillmentProfile<?>>) helper.fetchFromFile("C:\\Temp\\profileRegistry.ccm");
			
//			System.out.println("Encrypted form: " + new String (encoder.encode(helper.encrypt("639777180104"))));
//			
//			Offer offer = offerStore.getOfferById("Economy30SM");
//			System.out.println("\n1. Economy 30 SM:\n" + offer);
//			bw.write("\n1. Economy 30 SM:\n" + offer);
//				for (AtomicProduct product: offer.getAllAtomicProducts()) {
//					System.out.println("\t\tProduct name: " + product.getName());
//					bw.write("\t\tProduct name: " + product.getName());
//					System.out.println("\t\tProduct resource: " + product.getResource());
//					bw.write("\t\tProduct resource: " + product.getResource());
//					System.out.println("\t\tService Registry Instance: " + serviceRegistry.get(product.getResource().getName()));
//					System.out.println("\t\tService Registry Instance Type: " + serviceRegistry.get(product.getResource().getName()));
//		
//					Service resource = (Service) product.getResource();
//					System.out.println("\t\tResource name: " + resource.getName());
//		
//					for (String profileId: resource.getFulfillmentProfiles()) {
//						System.out.println("\t\t\tProfile Id: " + profileId);
//						FulfillmentProfile profile = profileRegistry.get(profileId);
//						System.out.println("\t\t\tProfile Registry class: " + profile.getClass().getCanonicalName());
//						System.out.println("\t\t\tProfile Registry contents: " + profile.toString());
//					}
//				}
//				
//				offer = offerStore.getOfferById("Economy30");
//			System.out.println("\n2. Economy 30:\n" + offer);
//				for (AtomicProduct product: offer.getAllAtomicProducts()) {
//					System.out.println("\t\tProduct name: " + product.getName());
//					System.out.println("\t\tProduct resource: " + product.getResource());
//					System.out.println("\t\tService Registry Instance: " + serviceRegistry.get(product.getResource().getName()));
//					System.out.println("\t\tService Registry Instance Type: " + serviceRegistry.get(product.getResource().getName()));
//		
//					Service resource = (Service) product.getResource();
//					System.out.println("\t\tResource name: " + resource.getName());
//		
//					for (String profileId: resource.getFulfillmentProfiles()) {
//						System.out.println("\t\t\tProfile Id: " + profileId);
//						FulfillmentProfile profile = profileRegistry.get(profileId);
//						System.out.println("\t\t\tProfile Registry class: " + profile.getClass().getCanonicalName());
//						System.out.println("\t\t\tProfile Registry contents: " + profile.toString());
//					}
//				}
				
//				offer = offerStore.getOfferById("Reversal Economy (30)");
//			System.out.println("\n3. Economy 30 Reversal:\n" + offer);
//				for (AtomicProduct product: offer.getAllAtomicProducts()) {
//					System.out.println("\t\tProduct name: " + product.getName());
//					System.out.println("\t\tProduct resource: " + product.getResource());
//					System.out.println("\t\tService Registry Instance: " + serviceRegistry.get(product.getResource().getName()));
//					System.out.println("\t\tService Registry Instance Type: " + serviceRegistry.get(product.getResource().getName()));
//		
//					Service resource = (Service) product.getResource();
//					System.out.println("\t\tResource name: " + resource.getName());
//		
//					for (String profileId: resource.getFulfillmentProfiles()) {
//						System.out.println("\t\t\tProfile Id: " + profileId);
//						FulfillmentProfile profile = profileRegistry.get(profileId);
//						System.out.println("\t\t\tProfile Registry class: " + profile.getClass().getCanonicalName());
//						System.out.println("\t\t\tProfile Registry contents: " + profile.toString());
//					}
//				}
//				
//				offer = offerStore.getOfferById("CUSTOMER_INFO_CHARGE");
//			System.out.println("\n4. Delete Subscriber:\n" + offer);
//				for (AtomicProduct product: offer.getAllAtomicProducts()) {
//					System.out.println("\t\tProduct name: " + product.getName());
//					System.out.println("\t\tProduct resource: " + product.getResource());
//					System.out.println("\t\tService Registry Instance: " + serviceRegistry.get(product.getResource().getName()));
//					System.out.println("\t\tService Registry Instance Type: " + serviceRegistry.get(product.getResource().getName()));
//		
//					Service resource = (Service) product.getResource();
//					System.out.println("\t\tResource name: " + resource.getName());
//		
//					for (String profileId: resource.getFulfillmentProfiles()) {
//						System.out.println("\t\t\tProfile Id: " + profileId);
//						FulfillmentProfile profile = profileRegistry.get(profileId);
//						System.out.println("\t\t\tProfile Registry class: " + profile.getClass().getCanonicalName());
//						System.out.println("\t\t\tProfile Registry contents: " + profile.toString());
//					}
//				}				
//			System.out.println("\n5. Welcome Pack:\n" + offerStore.getOfferById("SUBSCRIBE_PACKAGE_ITEM_WelcomePackServiceClass"));
//			System.out.println("\n6. Modify Tagging Reset bit:\n" + offerStore.getOfferById("MODIFY_SUBSCRIBER_TAGGING_SetResetBit"));

			System.out.println("Verifying all offers...");
			bw.write("Verifying all offers...");
			
			for(String offerId: offerStore.getOffersById().keySet()) {
				Offer taggingOffer = offerStore.getOfferById(offerId);
				//Offer taggingOffer = offerStore.getOfferById("katok-AT25-TEX25");
				System.out.println("\nOffer name: " + taggingOffer.getName());
				bw.write("\nOffer name: " + taggingOffer.getName());
				System.out.println("\tOffer contents: " + taggingOffer);
				bw.write("\n\tOffer contents: " + taggingOffer);
				for (AtomicProduct product: taggingOffer.getAllAtomicProducts()) {
					System.out.println("\t\tProduct name: " + product.getName());
					bw.write("\n\t\tProduct name: " + product.getName());
					System.out.println("\t\tProduct resource: " + product.getResource());
					bw.write("\n\t\tProduct resource: " + product.getResource());
					System.out.println("\t\tService Registry Instance: " + serviceRegistry.get(product.getResource().getName()));
					bw.write("\n\t\tService Registry Instance: " + serviceRegistry.get(product.getResource().getName()));
					System.out.println("\t\tService Registry Instance Type: " + serviceRegistry.get(product.getResource().getName()));
					bw.write("\n\t\tService Registry Instance Type: " + serviceRegistry.get(product.getResource().getName()));

					Service resource = (Service) product.getResource();
					System.out.println("\t\tResource name: " + resource.getName());
					bw.write("\n\t\tResource name: " + resource.getName());

					for (String profileId: resource.getFulfillmentProfiles()) {
						System.out.println("\t\t\tProfile Id: " + profileId);
						bw.write("\n\t\t\tProfile Id: " + profileId);
						FulfillmentProfile profile = profileRegistry.get(profileId);
						System.out.println("\t\t\tProfile Registry class: " + profile.getClass().getCanonicalName());
						bw.write("\n\t\t\tProfile Registry class: " + profile.getClass().getCanonicalName());
						System.out.println("\t\t\tProfile Registry contents: " + profile.toString());
						bw.write("\n\t\t\tProfile Registry contents: " + profile.toString());
					}
				}
			}
			
		} catch (FrameworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bw.write(e + "\n");
		}
		
		bw.close();
	}

}
