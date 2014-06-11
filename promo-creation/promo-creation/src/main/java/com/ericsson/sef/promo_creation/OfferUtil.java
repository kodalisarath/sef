package com.ericsson.sef.promo_creation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.OfferManager;
import com.ericsson.raso.sef.bes.prodcat.ServiceRegistry;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.core.db.model.CurrencyCode;

public class OfferUtil {

	private static final Logger logger = LoggerFactory.getLogger(OfferUtil.class);

	private OfferManager offerManager = null;

	ArrayList<Resource> allResources = new ArrayList<Resource>();

	public OfferUtil() throws Exception {

		offerManager = new OfferManager();

	};

	public void addResource(ArrayList<Resource> list, Resource resource) {
		list.add(resource);
		allResources.add(resource);
	}

	public void createAllProductCatalog() throws Exception {
		Map<String, Resource> lookupResourcesByName = new HashMap<String, Resource>();
		PromoHelper helper = new PromoHelper();

		// ----------------------------------------------------------
		// --- creating all the resources
		// ----------------------------------------------------------
		lookupResourcesByName.put("UnliAllText10RegionalDedicatedAccount_54", helper.createDA ("UnliAllText10RegionalDedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("UnliAllText10RegionalRefill_1053", helper.createRefill("UnliAllText10RegionalRefill_1053", "1053", 1, "1", CurrencyCode.PHP, new String [] {"1054"}));
		lookupResourcesByName.put("TimerOffer_1054", helper.createTimerProfile("TimerOffer_1054", 1054));
		
		lookupResourcesByName.put("AllText10DedicatedAccount_54", helper.createDA("AllText10DedicatedAccount_54", 54, "1", "PHP"));
		lookupResourcesByName.put("AllText10Refill_1017", helper.createRefill("AllText10Refill_1017", "1017", 1, "1", CurrencyCode.PHP, new String[] { "1054" }));
		lookupResourcesByName.put("AllText20Refill_1018", helper.createRefill("AllText20Refill_1018", "1018", 1, "1", CurrencyCode.PHP, new String[] { "2103" }));
		lookupResourcesByName.put("TimerOffer_1005", helper.createTimerProfile("TimerOffer_1005", 1005));
		lookupResourcesByName.put("TimerOffer_1141", helper.createTimerProfile("TimerOffer_1141", 1141));

		lookupResourcesByName.put("PasaloadP2DedicatedAccount_1", helper.createDA ("PasaloadP2DedicatedAccount_1", 1, "200", "PHP"));
		lookupResourcesByName.put("PasaloadP2Refill_PL01", helper.createRefill("PasaloadP2Refill_PL01", "PL01", 1, "200", CurrencyCode.PHP, new String [] {"1001"}));
		lookupResourcesByName.put("TimerOffer_1001", helper.createTimerProfile("TimerOffer_1001", 1001));
		
		lookupResourcesByName.put("FlexiCallandTextAbroad30DedicatedAccount_102", helper.createDA ("FlexiCallandTextAbroad30DedicatedAccount_102", 102, "1", "PHP"));
		lookupResourcesByName.put("FlexiCallandTextAbroad30Refill_3002", helper.createRefill("FlexiCallandTextAbroad30Refill_3002", "3002", 1, "1", CurrencyCode.PHP, new String [] {"1102"}));
		lookupResourcesByName.put("TimerOffer_1102", helper.createTimerProfile("TimerOffer_1102", 1102));

		
		// Non Commercial Offer

		lookupResourcesByName.put("CreateSubscriber", helper .createCreateSubscriberProfile("CreateSubscriber", "Create a new Subscriber"));

		// ----------------------------------------------------------
		// --- add resources to all registry
		// ----------------------------------------------------------
		ServiceRegistry serviceRegistry = new ServiceRegistry();
		for (Map.Entry<String, Resource> entry : lookupResourcesByName.entrySet()) {
			Resource resource = entry.getValue();
			serviceRegistry.createResource(resource);
		}

		Resource accountBlockingBit = helper.createSmartServiceOfferingProfile("SetAccountActivationBlockingBit", "Tag Subsriber AccountActivation", 1, true);
		Resource recycleBit = helper.createSmartServiceOfferingProfile("SeRecycleBit", "Tag Subsriber Recycle", 2, true);
		Resource barGeneralBit = helper.createSmartServiceOfferingProfile("SetBarGeneralBit", "Tag Subsriber BarGeneral", 3, true);
		Resource barOtherBit = helper.createSmartServiceOfferingProfile("SetBarOtherBit", "Tag Subsriber BarOther", 4, true);
		Resource barIrmBit = helper.createSmartServiceOfferingProfile("SetBarIRMBit", "Tag Subsriber BarIRM", 5, true);
		Resource specialFraudBit = helper.createSmartServiceOfferingProfile("SetSpecialFraudBit", "Tag Subsriber SpecialFraud", 6, true);
		Resource forcedDeleteBit = helper.createSmartServiceOfferingProfile("SetForcedDeleteBit", "Tag Subsriber ForcedDelete", 7, true);
		Resource resetBit = helper.createSmartServiceOfferingProfile("SetResetBit", "Tag Subsriber Reset", 0, false);
		Resource entireRead = helper.createSmartEntireReadProfile("ReadSubscriber", "Entire Read Subscriber");
		Resource entireDelete = helper.createSmartEntireDeleteProfile("DeleteSubscriber", "Delete Subscriber");
		Resource subscriberPackageItem = helper .createUpdateServiceClassProfile("SubscribePackageItem", "Subscriber Package Item");
		Resource modifyTagging = helper .createUpdateSubscriberSegmentationProfile("ModifyTagging", "Modify Tagging");
		Resource unsubscriberPackageItem = helper .createUpdateServiceClassProfile("UnsubscribePackageItem", "Unsubscriber Package Item");
		Resource balanceAdjustment = helper.createUpdateServiceClassProfile("BalanceAdjustment", "Balance Adjustment");


		serviceRegistry.createResource(accountBlockingBit);
		serviceRegistry.createResource(recycleBit);
		serviceRegistry.createResource(barGeneralBit);
		serviceRegistry.createResource(barOtherBit);
		serviceRegistry.createResource(barIrmBit);
		serviceRegistry.createResource(specialFraudBit);
		serviceRegistry.createResource(forcedDeleteBit);
		serviceRegistry.createResource(resetBit);
		serviceRegistry.createResource(entireDelete);
		serviceRegistry.createResource(subscriberPackageItem);
		serviceRegistry.createResource(modifyTagging);
		serviceRegistry.createResource(unsubscriberPackageItem);
		serviceRegistry.createResource(balanceAdjustment);

		// ----------------------------------------------------------
		// --- creating all the offer products
		// ----------------------------------------------------------
		ArrayList<Resource> offerResources;
		Offer offer;
		HashSet<String> planCodes = new HashSet<String>();
		// ----------------------------------------------------------
		// Offer.name=UnliAllText10Regional
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB550");
		planCodes.add("TXA10");
		offerResources.add(lookupResourcesByName.get("UnliAllText10RegionalRefill_1053"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("UnliAllText10RegionalDedicatedAccount_54"));
		offer = helper.createCommercialOffer("UnliAllText10Regional", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=AllText10
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("J");
		offerResources.add(lookupResourcesByName.get("AllText10Refill_1017"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1054"));
		offerResources.add(lookupResourcesByName.get("AllText10DedicatedAccount_54"));
		offer = helper.createCommercialOffer("AllText10", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=PasaloadP2
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("2");
		planCodes.add("O1");
		planCodes.add("W1");
		offerResources.add(lookupResourcesByName.get("PasaloadP2Refill_PL01"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1001"));
		offerResources.add(lookupResourcesByName.get("PasaloadP2DedicatedAccount_1"));
		offer = helper.createCommercialOffer("PasaloadP2", "", "PHP", 200, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=FlexiCallandTextAbroad30
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("RB354");
		offerResources.add(lookupResourcesByName.get("FlexiCallandTextAbroad30Refill_3002"));
		offerResources.add(lookupResourcesByName.get("TimerOffer_1102"));
		offerResources.add(lookupResourcesByName.get("FlexiCallandTextAbroad30DedicatedAccount_102"));
		offer = helper.createCommercialOffer("FlexiCallandTextAbroad30", "", "PHP", 1, planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=CREATE_SUBSCRIBER
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("CREATE_SUBSCRIBER");

		Resource dnsRes = lookupResourcesByName.get("CreateSubscriber");
		// Resource acipRes =
		// lookupResourcesByName.get("InstallSubscriberACIP_Cmd");
		offerResources.add(dnsRes);

		offer = helper.createNonCommercialOffer("CREATE_SUBSCRIBER", "Create Subscriber", planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offers for Modify Tagging....
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("accountBlockingBit");
		offerResources.add(accountBlockingBit);
		offer = helper.createNonCommercialOffer("accountBlockingBit", "Service Offering Account Blocking", planCodes, offerResources);
		offerManager.createOffer(offer);

		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("recycleBit");
		offerResources.add(recycleBit);
		offer = helper.createNonCommercialOffer("recycleBit", "Service Offering Recycle", planCodes, offerResources);
		offerManager.createOffer(offer);

		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("barGeneralBit");
		offerResources.add(barGeneralBit);
		offer = helper.createNonCommercialOffer("barGeneralBit", "Service Offering Bar General", planCodes, offerResources);
		offerManager.createOffer(offer);

		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("barIrmBit");
		offerResources.add(barIrmBit);
		offer = helper.createNonCommercialOffer("barIrmBit", "Service Offering Bar Other", planCodes, offerResources);
		offerManager.createOffer(offer);

		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("barOtherBit");
		offerResources.add(barOtherBit);
		offer = helper.createNonCommercialOffer("barOtherBit", "Service Offering Bar Other", planCodes, offerResources);
		offerManager.createOffer(offer);

		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("specialFraudBit");
		offerResources.add(specialFraudBit);
		offer = helper.createNonCommercialOffer("specialFraudBit", "Service Offering Special Fraud", planCodes, offerResources);
		offerManager.createOffer(offer);

		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("forcedDeleteBit");
		offerResources.add(forcedDeleteBit);
		offer = helper.createNonCommercialOffer("forcedDeleteBit", "Service Offering Special Fraud", planCodes, offerResources);
		offerManager.createOffer(offer);

		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("resetBit");
		offerResources.add(resetBit);
		offer = helper.createNonCommercialOffer("resetBit", "Service Offering ResetBit", planCodes, offerResources);
		offerManager.createOffer(offer);

		// // ----------------------------------------------------------
		// // Offer.name=READ_SUBSCRIBER
		// // ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("READ_SUBSCRIBER");
		offerResources.add(entireRead);
		offer = helper.createNonCommercialOffer("READ_SUBSCRIBER", "Read Subscriber", planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=DELETE_SUBSCRIBER
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("DELETE_SUBSCRIBER");
		offerResources.add(entireDelete);
		offer = helper.createNonCommercialOffer("DELETE_SUBSCRIBER", "Delete Subscriber", planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=SUBSCRIBE_PACKAGE_ITEM
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("SUBSCRIBE_PACKAGE_ITEM");
		offerResources.add(subscriberPackageItem);
		offer = helper.createNonCommercialOffer("SUBSCRIBE_PACKAGE_ITEM", "Subscribe Package Item", planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=MODIFY_TAGGING
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("MODIFY_TAGGING");
		offerResources.add(subscriberPackageItem);
		offer = helper.createNonCommercialOffer("MODIFY_TAGGING", "Modify Tagging", planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UNSUBSCRIBE_PACKAGE_ITEM
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("UNSUBSCRIBE_PACKAGE_ITEM");
		offerResources.add(subscriberPackageItem);
		offer = helper.createNonCommercialOffer("UNSUBSCRIBE_PACKAGE_ITEM", "Unsubscribe Package Item", planCodes, offerResources);
		offerManager.createOffer(offer);
		// ----------------------------------------------------------
		// Offer.name=UNSUBSCRIBE_PACKAGE_ITEM
		// ----------------------------------------------------------
		offerResources = new ArrayList<Resource>();
		planCodes = new HashSet<String>();
		planCodes.add("BALANCE_ADJUSTMENT");
		offerResources.add(subscriberPackageItem);
		offer = helper.createNonCommercialOffer("BALANCE_ADJUSTMENT", "Balance Adjustment", planCodes, offerResources);
		offerManager.createOffer(offer);
	}

	public static void main(String[] args) throws Exception {
		OfferUtil offerUtil = new OfferUtil();

		System.out.println("Creating offer, profile, service catalog ...");
		offerUtil.createAllProductCatalog();

		System.out.println("Done catalog creation");

		// OfferVerification offerVerify = new OfferVerification();
		//
		// offerVerify.main(new String[] {"a", "b"});

	}
}
