	package com.ericsson.sef.promo_creation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.OfferManager;
import com.ericsson.raso.sef.bes.prodcat.SecureSerializationHelper;
import com.ericsson.raso.sef.bes.prodcat.ServiceRegistry;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.InfiniteTime;
import com.ericsson.raso.sef.bes.prodcat.entities.NoTermination;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.Price;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.entities.Service;
import com.ericsson.raso.sef.bes.prodcat.entities.State;
import com.ericsson.raso.sef.bes.prodcat.entities.UnlimitedQuota;
import com.ericsson.raso.sef.client.air.request.OfferSelection;
import com.ericsson.raso.sef.client.air.request.PamInformation;
import com.ericsson.raso.sef.client.air.request.ServiceOffering;
import com.ericsson.raso.sef.core.db.model.CurrencyCode;
import com.ericsson.raso.sef.fulfillment.profiles.AddPeriodicAccountManagementDataProfile;
import com.ericsson.raso.sef.fulfillment.profiles.BalanceAdjustmentProfile;
import com.ericsson.raso.sef.fulfillment.profiles.DedicatedAccountProfile;
import com.ericsson.raso.sef.fulfillment.profiles.DnsUpdateProfile;
import com.ericsson.raso.sef.fulfillment.profiles.EntireReadSubscriberProfile;
import com.ericsson.raso.sef.fulfillment.profiles.OfferProfile;
import com.ericsson.raso.sef.fulfillment.profiles.PamInformationList;
import com.ericsson.raso.sef.fulfillment.profiles.PartialReadSubscriberProfile;
import com.ericsson.raso.sef.fulfillment.profiles.ProfileRegistry;
import com.ericsson.raso.sef.fulfillment.profiles.ReadBalancesProfile;
import com.ericsson.raso.sef.fulfillment.profiles.ReadSubscriberProfile;
import com.ericsson.raso.sef.fulfillment.profiles.RefillProfile;
import com.ericsson.raso.sef.fulfillment.profiles.UpdateServiceClassProfile;
import com.ericsson.raso.sef.fulfillment.profiles.UpdateSubscriberSegmentationProfile;
import com.ericsson.raso.sef.fulfillment.profiles.smart.CreateSubscriberProfile;
import com.ericsson.raso.sef.fulfillment.profiles.smart.DedicatedAccountReversal;
import com.ericsson.raso.sef.fulfillment.profiles.smart.DeleteSubscriberProfile;
import com.ericsson.raso.sef.fulfillment.profiles.smart.FlexiRechargeProfile;
import com.ericsson.raso.sef.fulfillment.profiles.smart.ModifyCustomerGraceProfile;
import com.ericsson.raso.sef.fulfillment.profiles.smart.ReversalProfile;
import com.ericsson.raso.sef.fulfillment.profiles.smart.SubscribePackageItemProfile;
import com.ericsson.raso.sef.fulfillment.profiles.smart.TimerOfferReversal;
import com.ericsson.raso.sef.fulfillment.profiles.smart.UnsubscribePackageItemProfile;

public class BusinessConfigurationTool {

	private SecureSerializationHelper serializationHelper = null;
	private OfferManager offerManager = null;
	private ServiceRegistry serviceRegistry = null;
	private ProfileRegistry profileRegistry = null;


	public BusinessConfigurationTool() {
		serializationHelper = new SecureSerializationHelper();
		offerManager = new OfferManager();
		serviceRegistry = new ServiceRegistry();
		profileRegistry = new ProfileRegistry();
	}

	public static void main(String[] args) {
		BusinessConfigurationTool bcTool = new BusinessConfigurationTool();
		bcTool.createSmartSpecificBusinessConfiguration();

	}

	public void createSmartSpecificBusinessConfiguration() {

		Resource resource = null;
		Offer bizConfig = null;
		List<String> handles = null;
		try {
			
			// Workflows....
			System.out.println("CREATING WORKFLOWS NOW.....\n======================.....\n\n");
			
			System.out.println("Offer, Resource & Profile - ENTIRE_READ_SUBSCRIBER...");
			resource = this.createSmartEntireReadProfile("ENTIRE_READ_SUBSCRIBER", "Entire Read Subscriber");
			handles = new ArrayList<String>();
			handles.add("READ_SUBSCRIBER");
			bizConfig = this.getSimpleBcWorkflow("ENTIRE_READ_SUBSCRIBER", "Entire Read SmartTnt Subsriber", handles, resource);
			offerManager.createOffer(bizConfig);
			try {
				serviceRegistry.createResource(resource);
			} catch (CatalogException e) {
				serviceRegistry.updateResource(resource);
			}
			
			System.out.println("Offer, Resource & Profile - CUSTOMER_INFO_CHARGE...");
			resource = this.createSmartEntireReadProfile("CUSTOMER_INFO_CHARGE", "Entire Read Subscriber");
			handles = new ArrayList<String>();
			handles.add("CUSTOMER_INFO_CHARGE");
			bizConfig = this.getSimpleOffer("CUSTOMER_INFO_CHARGE", "Entire Read SmartTnt Subsriber", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - PARTIAL_READ_SUBSCRIBER...");
			resource = this.createSmartPartialReadProfile("PARTIAL_READ_SUBSCRIBER", "Partial Read Subscriber");
			handles = new ArrayList<String>();
			handles.add("PARTIAL_READ_SUBSCRIBER");
			bizConfig = this.getSimpleBcWorkflow("PARTIAL_READ_SUBSCRIBER", "Entire Read SmartTnt Subsriber", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - READ_BALANCES...");
			resource = this.createSmartReadBalancesProfile("READ_BALANCES", "Read Subscriber Balances");
			handles = new ArrayList<String>();
			handles.add("READ_BALANCES");
			bizConfig = this.getSimpleBcWorkflow("READ_BALANCES", "Read Balances SmartTnt Subsriber", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - CREATE_SUSBCRIBER...");
			resource = this.createCreateSubscriberProfile("CREATE_SUSBCRIBER", "Create a new Subscriber", 0, 0, 0, false, 0);
			handles = new ArrayList<String>();
			handles.add("CREATE_SUSBCRIBER");
			bizConfig = this.getSimpleBcWorkflow("CREATE_SUBSCRIBER", "Create SmartTnt Subsriber", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - DELETE_SUBSCRIBER...");
			resource = this.createDeleteSuscriberProfile("DELETE_SUBSCRIBER", "Delete Subscriber", 0, 0, 0, false, 0);
			handles = new ArrayList<String>();
			handles.add("DELETE_SUBSCRIBER");
			bizConfig = this.getSimpleBcWorkflow("DELETE_SUBSCRIBER", "Delete SmartTnt Subsriber", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - SetAccountActivationBlockingBit...");
			resource =  this.createSmartTaggingProfile("SetAccountActivationBlockingBit", "Tag Subsriber AccountActivation", 1, true);
			handles = new ArrayList<String>();
			handles.add("MODIFY_SUBSCRIBER_TAGGING_AccountActivationBlockingBit");
			bizConfig = this.getSimpleBcWorkflow("MODIFY_SUBSCRIBER_TAGGING_AccountActivationBlockingBit", "SetAccountActivationBlockingBit", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - SetRecycleBit...");
			resource = this.createSmartTaggingProfile("SetRecycleBit", "Tag Subsriber Recycle", 2, true);
			handles = new ArrayList<String>();
			handles.add("MODIFY_SUBSCRIBER_TAGGING_SetRecycleBit");
			bizConfig = this.getSimpleBcWorkflow("MODIFY_SUBSCRIBER_TAGGING_SetRecycleBit", "SetRecycleBit", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - SetBarGeneralBit...");
			resource = this.createSmartTaggingProfile("SetBarGeneralBit", "Tag Subsriber BarGeneral", 3, true);
			handles = new ArrayList<String>();
			handles.add("MODIFY_SUBSCRIBER_TAGGING_SetBarGeneralBit");
			bizConfig = this.getSimpleBcWorkflow("MODIFY_SUBSCRIBER_TAGGING_SetBarGeneralBit", "SetBarGeneralBit", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - SetBarOtherBit...");
			resource = this.createSmartTaggingProfile("SetBarOtherBit", "Tag Subsriber BarOther", 4, true);
			handles = new ArrayList<String>();
			handles.add("MODIFY_SUBSCRIBER_TAGGING_SetBarOtherBit");
			bizConfig = this.getSimpleBcWorkflow("MODIFY_SUBSCRIBER_TAGGING_SetBarOtherBit", "SetBarOtherBit", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - SetBarIRMBit...");
			resource = this.createSmartTaggingProfile("SetBarIRMBit", "Tag Subsriber BarIRM", 5, true);
			handles = new ArrayList<String>();
			handles.add("MODIFY_SUBSCRIBER_TAGGING_SetBarIRMBit");
			bizConfig = this.getSimpleBcWorkflow("MODIFY_SUBSCRIBER_TAGGING_SetBarIRMBit", "SetBarIRMBit", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - SetSpecialFraudBit...");
			resource = this.createSmartTaggingProfile("SetSpecialFraudBit", "Tag Subsriber SpecialFraud", 6, true);
			handles = new ArrayList<String>();
			handles.add("MODIFY_SUBSCRIBER_TAGGING_SetBarIRMBit");
			bizConfig = this.getSimpleBcWorkflow("MODIFY_SUBSCRIBER_TAGGING_SetSpecialFraudBit", "SetSpecialFraudBit", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - SetForcedDeleteBit...");
			resource = this.createSmartTaggingProfile("SetForcedDeleteBit", "Tag Subsriber ForcedDelete", 7	, true);
			handles = new ArrayList<String>();
			handles.add("MODIFY_SUBSCRIBER_TAGGING_SetForcedDeleteBit");
			bizConfig = this.getSimpleBcWorkflow("MODIFY_SUBSCRIBER_TAGGING_SetForcedDeleteBit", "SetForcedDeleteBit", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - SetResetBit...");
			resource = this.createSmartTaggingProfile("SetResetBit", "Tag Subsriber Reset", 0, false);
			handles = new ArrayList<String>();
			handles.add("MODIFY_SUBSCRIBER_TAGGING_SetResetBit");
			bizConfig = this.getSimpleBcWorkflow("MODIFY_SUBSCRIBER_TAGGING_SetResetBit", "SetResetBit", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - InitialSC - WelcomePackServiceClass...");
			resource = this.createSmartWelcomePackProfile("InitialSC-WelcomePackServiceClass", "Tag Subsriber Reset");
			handles = new ArrayList<String>();
			handles.add("SUBSCRIBE_PACKAGE_ITEM_WelcomePackServiceClass");
			bizConfig = this.getSimpleBcWorkflow("SUBSCRIBE_PACKAGE_ITEM_WelcomePackServiceClass", "WelcomePack - ServiceClass", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - Subscribe WelcomePackServiceClass...");
			resource = this.createSmartWelcomePackProfile("SubscribePackageItem", "Welcome Pack Service Class");
			handles = new ArrayList<String>();
			handles.add("SUBSCRIBE_PACKAGE_ITEM");
			bizConfig = this.getSimpleBcWorkflow("SubscribePackageItem", "WelcomePack - ServiceClass", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - ModifyCustomerPreactive...");
			resource =  this.createSmartModifyCustomerPreactiveProfile("ModifyCustomerPreactive", "Tag Subsriber AccountActivation");
			handles = new ArrayList<String>();
			handles.add("ModifyCustomerPreactive");
			bizConfig = this.getSimpleBcWorkflow("ModifyCustomerPreactive", "ModifyCustomerPreactive", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - ModifyCustomerGrace...");
			resource =  this.createSmartModifyCustomerGraceProfile("ModifyCustomerGrace", "Tag Subsriber Grace");
			handles = new ArrayList<String>();
			handles.add("ModifyCustomerGrace");
			bizConfig = this.getSimpleBcWorkflow("ModifyCustomerGrace", "ModifyCustomerGrace", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - UnsubscribePackageItem...");
			resource =  this.createSmartUnscribePackageItemProfile("UnsubscribePackageItem", "UnsubsribePackageItem");
			handles = new ArrayList<String>();
			handles.add("UnsubscribePackageItem");
			bizConfig = this.getSimpleBcWorkflow("UnsubscribePackageItem", "UnsubscribePackageItem", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			
			
			System.out.println("CREATING COMMERCIAL OFFERS NOW.....\n======================.....\n\n");
			
			System.out.println("Offer, Resource & Profile - Predefined Refill...");
			resource = this.createRefill("Economy 30", "L001", 1, "3000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("A");
			handles.add("T1");
			handles.add("SM2");
			handles.add("SM12");
			bizConfig = this.getSimpleBcWorkflow("Economy30", "Economy 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - Unli Refill...");
			resource = this.createRefill("UnliText2All", "1001", 1, "1", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("RB238");
			handles.add("RB328");
			handles.add("PWIL3");
			bizConfig = this.getSimpleBcWorkflow("UnliText2All", "TXT2ALL (Regional)", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - Pasaload Refill...");
			resource = this.createRefill("PasaloadP2", "PL01", 1, "200", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("2");
			handles.add("O1");
			handles.add("W1");
			handles.add("PasaloadP2");
			bizConfig = this.getSimpleBcWorkflow("Pasaload2", "Pasaload 2PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - Pasaload Refill...");
			resource = this.createRefill("PasaloadP5", "PL01", 1, "500", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("5");
			handles.add("O2");
			handles.add("W2");
			handles.add("PasaloadP5");
			bizConfig = this.getSimpleBcWorkflow("Pasaload5", "Pasaload 5PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - Pasaload Refill...");
			resource = this.createRefill("PasaloadP10", "PL01", 1, "1000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("10");
			handles.add("O3");
			handles.add("W3");
			handles.add("PasaloadP10");
			bizConfig = this.getSimpleBcWorkflow("Pasaload10", "Pasaload 10PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Pasaload Refill...");
			resource = this.createRefill("PasaloadP15", "PL01", 1, "1500", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("15");
			handles.add("O4");
			handles.add("W4");
			handles.add("PasaloadP15");
			bizConfig = this.getSimpleBcWorkflow("Pasaload15", "Pasaload 15PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Pasaload Refill...");
			resource = this.createRefill("PasaloadP20", "PL01", 1, "2000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("20");
			handles.add("O5");
			handles.add("W5");
			handles.add("PasaloadP20");
			bizConfig = this.getSimpleBcWorkflow("Pasaload20", "Pasaload 20PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Pasaload Refill...");
			resource = this.createRefill("PasaloadP30", "PL01", 1, "3000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("30");
			handles.add("O7");
			handles.add("W6");
			handles.add("PasaloadP30");
			bizConfig = this.getSimpleBcWorkflow("Pasaload30", "Pasaload 30PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Pasaload Refill...");
			resource = this.createRefill("PasaloadP35", "PL01", 1, "3500", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("35T");
			handles.add("O8");
			handles.add("Y2");
			handles.add("PasaloadP35");
			bizConfig = this.getSimpleBcWorkflow("Pasaload35", "Pasaload 35PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Pasaload Refill...");
			resource = this.createRefill("PasaloadP60", "PL01", 1, "6000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("60");
			handles.add("O9");
			handles.add("W7");
			handles.add("PasaloadP60");
			bizConfig = this.getSimpleBcWorkflow("Pasaload60", "Pasaload 60PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Pasaload Refill...");
			resource = this.createRefill("PasaloadP100", "PL01", 1, "10000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("1H");
			handles.add("1O");
			handles.add("W8");
			handles.add("PasaloadP100");
			bizConfig = this.getSimpleBcWorkflow("Pasaload100", "Pasaload 100PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Pasaload Refill...");
			resource = this.createRefill("PasaloadP200", "PL01", 1, "20000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("2H");
			handles.add("2O");
			handles.add("W9");
			handles.add("PasaloadP200");
			bizConfig = this.getSimpleBcWorkflow("Pasaload200", "Pasaload 200PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Egame Pasaload Refill...");
			resource = this.createRefill("EgamePasaloadP2", "EGP1", 1, "200", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("2");
			handles.add("EgamePasaloadP2");
			bizConfig = this.getSimpleBcWorkflow("EgamePasaloadP2", "Egame Pasaload 2PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Egame Pasaload Refill...");
			resource = this.createRefill("EgamePasaloadP5", "EGP1", 1, "500", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("5");
			handles.add("EgamePasaloadP5");
			bizConfig = this.getSimpleBcWorkflow("EgamePasaloadP5", "Egame Pasaload 5PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Egame Pasaload Refill...");
			resource = this.createRefill("EgamePasaloadP10", "EGP1", 1, "1000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("10");
			handles.add("EgamePasaloadP10");
			bizConfig = this.getSimpleBcWorkflow("EgamePasaloadP10", "Egame Pasaload 10PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Egame Pasaload Refill...");
			resource = this.createRefill("EgamePasaloadP15", "EGP1", 1, "1500", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("15");
			handles.add("EgamePasaloadP15");
			bizConfig = this.getSimpleBcWorkflow("EgamePasaloadP15", "Egame Pasaload 15PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Egame Pasaload Refill...");
			resource = this.createRefill("EgamePasaloadP20", "EGP1", 1, "2000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("E6");
			handles.add("EgamePasaloadP20");
			bizConfig = this.getSimpleBcWorkflow("EgamePasaloadP20", "Egame Pasaload 20PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Egame Pasaload Refill...");
			resource = this.createRefill("EgamePasaloadP30", "EGP1", 1, "3000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("E7");
			handles.add("EgamePasaloadP30");
			bizConfig = this.getSimpleBcWorkflow("EgamePasaloadP30", "Egame Pasaload 30PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Egame Pasaload Refill...");
			resource = this.createRefill("EgamePasaloadP60", "EGP1", 1, "6000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("E8");
			handles.add("EgamePasaloadP60");
			bizConfig = this.getSimpleBcWorkflow("EgamePasaloadP60", "Egame Pasaload 60PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Egame Pasaload Refill...");
			resource = this.createRefill("EgamePasaloadP100", "EGP1", 1, "10000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("1H");
			handles.add("EgamePasaloadP100");
			bizConfig = this.getSimpleBcWorkflow("EgamePasaloadP100", "Egame Pasaload 100PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Egame Pasaload Refill...");
			resource = this.createRefill("EgamePasaloadP200", "EGP1", 1, "20000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("2H");
			handles.add("EgamePasaloadP200");
			bizConfig = this.getSimpleBcWorkflow("EgamePasaloadP200", "Egame Pasaload 200PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Hellow Card Pasaload Refill...");
			resource = this.createRefill("HellowPasaloadP10", "HLC1", 1, "1000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("10");
			handles.add("HellowPasaloadP10");
			bizConfig = this.getSimpleBcWorkflow("HellowPasaloadP10", "Hellow Card Pasaload 10PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Hellow Card Pasaload Refill...");
			resource = this.createRefill("HellowPasaloadP15", "HLC1", 1, "1500", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("15");
			handles.add("HellowPasaloadP15");
			bizConfig = this.getSimpleBcWorkflow("HellowPasaloadP15", "Hellow Card Pasaload 15PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Hellow Card Pasaload Refill...");
			resource = this.createRefill("HellowPasaloadP20", "HLC1", 1, "2000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("20");
			handles.add("HellowPasaloadP20");
			bizConfig = this.getSimpleBcWorkflow("HellowPasaloadP20", "Hellow Card Pasaload 20PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Hellow Card Pasaload Refill...");
			resource = this.createRefill("HellowPasaloadP30", "HLC1", 1, "3000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("30");
			handles.add("HellowPasaloadP30");
			bizConfig = this.getSimpleBcWorkflow("HellowPasaloadP30", "Hellow Card Pasaload 30PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Hellow Card Pasaload Refill...");
			resource = this.createRefill("HellowPasaloadP60", "HLC1", 1, "6000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("60");
			handles.add("HellowPasaloadP60");
			bizConfig = this.getSimpleBcWorkflow("HellowPasaloadP60", "Hellow Card Pasaload 60PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Hellow Card Pasaload Refill...");
			resource = this.createRefill("HellowPasaloadP100", "HLC1", 1, "10000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("1H");
			handles.add("HellowPasaloadP100");
			bizConfig = this.getSimpleBcWorkflow("HellowPasaloadP100", "Hellow Card Pasaload 100PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Hellow Card Pasaload Refill...");
			resource = this.createRefill("HellowPasaloadP200", "HLC1", 1, "20000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("2H");
			handles.add("HellowPasaloadP200");
			bizConfig = this.getSimpleBcWorkflow("HellowPasaloadP200", "Hellow Card Pasaload 200PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - Ask 4 Load Refill...");
			resource = this.createRefill("Ask4LoadP5", "ASL1", 1, "500", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("5");
			handles.add("Ask4LoadP5");
			bizConfig = this.getSimpleBcWorkflow("Ask4LoadP5", "Ask4Load 5PHP", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			
			System.out.println("Offer, Resource & Profile - Araw Araw Refill...");
			resource = this.createRefill("ArawAraw", "PL01", 1, "500", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("5");
			handles.add("O2");
			handles.add("W2");
			bizConfig = this.getSimpleBcWorkflow("ArawAraw", "Araw Araw", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Resource & Profile - Calling Circle Refill...");
			resource = this.createRefill("CallingCircle", "KT25", 1, "2500", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("RB506");
			handles.add("W2");
			bizConfig = this.getSimpleBcWorkflow("katok-AT25-TEX25", "2 way Calling Circle", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			//==========================================		
			
			System.out.println("Offer, Resource & Profile - Recharge Reversal...");
			List<DedicatedAccountReversal> daReversals = new ArrayList<DedicatedAccountReversal>();
			DedicatedAccountReversal daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);
			
			
			List<TimerOfferReversal> toReversals = new ArrayList<TimerOfferReversal>();
			TimerOfferReversal toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);
			
			resource = this.createSmartReversalRefill("Rev Economy 30", daReversals, toReversals);
			handles = new ArrayList<String>();
			handles.add("RevA");
			handles.add("RevT1");
			handles.add("RevSM2");
			handles.add("RevSM12");
			
			bizConfig = this.getSimpleBcWorkflow("Reversal Economy (30)", "Reversal Economy (30)", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
	
			
			//===========>>>> Flexi Refilll... 
			resource = this.createSmartFlexiRefillProfile("FlexiRefill");
			handles = new ArrayList<String>();
			handles.add("FlexiRefill");
			
			bizConfig = this.getSimpleBcWorkflow("FlexiRefill", "Flexi Refill", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
	
			//=====================================
			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BalanceAdjustment", 1, 1);
			handles = new ArrayList<String>();
			handles.add("OnPeakAccountID");
			bizConfig = this.getSimpleBcWorkflow("OnPeakAccountID", "On Peak Account", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
	
			
			
			
						
			

		//TODO: insert new bizconfig here...
			System.out.println("Offer, Resources & Profiles - DONE!!\n");
			
			
			
			
			
		} catch (CatalogException e) {
			System.out.println("Failed creating BizConfig. Cause: " + e.getMessage());
			e.printStackTrace();
		}

	}


	public Offer getSimpleBcWorkflow(String name, String description, List<String> handles, Resource resource) throws CatalogException {
		Offer templatedOffer = new Offer(name);
		templatedOffer.setDescription(description);
		templatedOffer.setAutoTermination(new NoTermination());
		templatedOffer.setRenewalPeriod(new InfiniteTime());
		templatedOffer.setOfferState(State.TESTING);
		templatedOffer.setOfferState(State.PUBLISHED);
		templatedOffer.setRecurrent(false);
		templatedOffer.setTrialPeriod(new InfiniteTime());
		templatedOffer.setCommercial(false);

		for (String handle: handles)
			templatedOffer.addExternalHandle(handle);

		AtomicProduct product = new AtomicProduct(name);
		product.setQuota(new UnlimitedQuota());
		product.setResource(resource);
		product.setValidity(new InfiniteTime());

		templatedOffer.addProduct(product);

		return templatedOffer;
	}
	
	public Offer getSimpleOffer(String name, String description, List<String> handles, Resource resource) throws CatalogException {
		Offer templatedOffer = new Offer(name);
		templatedOffer.setDescription(description);
		templatedOffer.setAutoTermination(new NoTermination());
		templatedOffer.setRenewalPeriod(new InfiniteTime());
		templatedOffer.setOfferState(State.TESTING);
		templatedOffer.setOfferState(State.PUBLISHED);
		templatedOffer.setRecurrent(false);
		templatedOffer.setTrialPeriod(new InfiniteTime());
		templatedOffer.setCommercial(true);
		templatedOffer.setPrice(new Price("PHP", 100));

		for (String handle: handles)
			templatedOffer.addExternalHandle(handle);

		AtomicProduct product = new AtomicProduct(name);
		product.setQuota(new UnlimitedQuota());
		product.setResource(resource);
		product.setValidity(new InfiniteTime());

		templatedOffer.addProduct(product);

		return templatedOffer;
	}

	public Resource createRefill(String name, String refillProfileId, Integer refillType, String transactionAmount, CurrencyCode transactionCurrency) throws CatalogException {

		Resource resource = new Service(name);
		resource.setDescription("Refill Profile");
		resource.setConsumable(true);
		resource.setDiscoverable(true);
		resource.setExternallyConsumed(true);
		resource.setConsumptionUnitName("PHP");

		RefillProfile fulfillmentProfile = new RefillProfile(name);
		fulfillmentProfile.setRefillProfileId(refillProfileId);
		fulfillmentProfile.setRefillType(refillType);
		fulfillmentProfile.setTransactionAmount(transactionAmount);
		fulfillmentProfile.setTransactionCurrency(transactionCurrency);
		

		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		profileRegistry.createProfile(fulfillmentProfile);

		return resource;
	}

	public Resource createPAM(String name, Integer pamServiceId, Integer pamIndicator) throws CatalogException {

		Resource resource = new Service(name);
		resource.setDescription("PAM");
		resource.setConsumable(true);
		resource.setDiscoverable(true);
		resource.setExternallyConsumed(true);
		resource.setConsumptionUnitName("PHP");
		resource.setCost(null);

		AddPeriodicAccountManagementDataProfile fulfillmentProfile = new AddPeriodicAccountManagementDataProfile(name);
		fulfillmentProfile.setPamIndicator(pamIndicator);
		fulfillmentProfile.setPamServiceId(pamServiceId);

		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		profileRegistry.createProfile(fulfillmentProfile);

		return resource;		
	}

	public Resource createDA(String name, Integer dedicatedAccountId, String transactionCurrency, String transactionAmount) throws CatalogException {

		Resource resource = new Service(name);
		resource.setDescription("Dedicated Account");
		resource.setConsumable(true);
		resource.setDiscoverable(true);
		resource.setExternallyConsumed(true);
		resource.setConsumptionUnitName("PHP");
		resource.setCost(null);

		DedicatedAccountProfile fulfillmentProfile = new DedicatedAccountProfile(name);
		fulfillmentProfile.setDedicatedAccountID(dedicatedAccountId);
		fulfillmentProfile.setTransactionAmount(transactionAmount);
		fulfillmentProfile.setTransactionCurrency(transactionCurrency);

		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		profileRegistry.createProfile(fulfillmentProfile);

		return resource;
	}

	public Resource createTimerProfile(String name, Integer offerId) throws CatalogException {

		Resource resource = new Service(name);
		resource.setDescription("Timer Profile");
		resource.setConsumable(true);
		resource.setDiscoverable(true);
		resource.setExternallyConsumed(true);
		resource.setConsumptionUnitName("PHP");
		resource.setCost(null);

		OfferProfile fulfillmentProfile = new OfferProfile(name);
		fulfillmentProfile.setOfferID(offerId);

		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		profileRegistry.createProfile(fulfillmentProfile);

		return resource;
	}

	public Resource createDnsUpdateProfile(String name, String description) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		DnsUpdateProfile fulfillmentProfile = new DnsUpdateProfile(name);
		fulfillmentProfile.setZname(".msisdn.sub.cs.");
		fulfillmentProfile.setRdata(".cs.");
		fulfillmentProfile.setDtype(5);
		fulfillmentProfile.setDclass(1);
		fulfillmentProfile.setTtl(100);

		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		profileRegistry.createProfile(fulfillmentProfile);

		return resource;		
	}

	public Resource createCreateSubscriberProfile(String name, String description, int promotionPlanId, 
			int serviceClassNew, int languageIDNew,	boolean temporaryBlockedFlag, int ussdEndOfCallNotificationID) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		CreateSubscriberProfile fulfillmentProfile = new CreateSubscriberProfile(name);
		fulfillmentProfile.setZname(".msisdn.sub.cs.");
		fulfillmentProfile.setRdata(".cs.");
		fulfillmentProfile.setDtype(5);
		fulfillmentProfile.setDclass(1);
		fulfillmentProfile.setTtl(100);
		fulfillmentProfile.setMessageCapabilityFlag(true);
		fulfillmentProfile.setPromotionPlanId(null);
		fulfillmentProfile.setServiceClassNew(null);
		fulfillmentProfile.setLanguageIDNew(null);
		fulfillmentProfile.setTemporaryBlockedFlag(null);
		fulfillmentProfile.setUssdEndOfCallNotificationID(null);
		
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		profileRegistry.createProfile(fulfillmentProfile);

		return resource;		
	}

	public Resource createDeleteSuscriberProfile(String name, String description, int promotionPlanId, 
			int serviceClassNew, int languageIDNew,	boolean temporaryBlockedFlag, int ussdEndOfCallNotificationID) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		DeleteSubscriberProfile fulfillmentProfile = new DeleteSubscriberProfile(name);
		fulfillmentProfile.setZname(".msisdn.sub.cs.");
		fulfillmentProfile.setRdata(".cs.");
		fulfillmentProfile.setDtype(5);
		fulfillmentProfile.setDclass(1);
		fulfillmentProfile.setDclass(100);
		fulfillmentProfile.setInsert(false);
		fulfillmentProfile.setMessageCapabilityFlag(true);
		fulfillmentProfile.setPromotionPlanId(null);
		fulfillmentProfile.setServiceClassNew(null);
		fulfillmentProfile.setLanguageIDNew(null);
		fulfillmentProfile.setTemporaryBlockedFlag(null);
		fulfillmentProfile.setUssdEndOfCallNotificationID(null);
				
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		profileRegistry.createProfile(fulfillmentProfile);

		return resource;		
	}	

	public Resource createDeleteDnsEntryProfile(String name, String description, String zName, String rData, int dType, int dClass, int ttl, boolean insertMode) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		DnsUpdateProfile fulfillmentProfile = new DnsUpdateProfile(name);
		fulfillmentProfile.setZname(zName);
		fulfillmentProfile.setRdata(rData);
		fulfillmentProfile.setDtype(dType);
		fulfillmentProfile.setDclass(dClass);
		fulfillmentProfile.setDclass(ttl);
		fulfillmentProfile.setInsert(insertMode);

		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		profileRegistry.createProfile(fulfillmentProfile);

		return resource;		
	}	
	
	public Resource createUpdateServiceClassProfile(String name, String description) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);
		
		UpdateServiceClassProfile fulfillmentProfile = new UpdateServiceClassProfile(name);
		
		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
		return resource;
	}
	
	public Resource createSmartReversalRefill(String name, List<DedicatedAccountReversal> daReversals, List<TimerOfferReversal> toReversals) throws CatalogException {

		Resource resource = new Service(name);
		resource.setDescription("SMART Reversal Profile");
		resource.setConsumable(true);
		resource.setDiscoverable(true);
		resource.setExternallyConsumed(true);
		resource.setConsumptionUnitName("PHP");

		ReversalProfile fulfillmentProfile = new ReversalProfile(name);
		fulfillmentProfile.setDaReversals(daReversals);
		fulfillmentProfile.setToReversals(toReversals);
		

		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		profileRegistry.createProfile(fulfillmentProfile);

		return resource;
	}

	public Resource createSmartBalanceAdjustmentProfile(String name, int dedicatedAccountID, int dedicatedAccountUnitType) throws CatalogException {

		Resource resource = new Service(name);
		resource.setDescription("Balance Adjustment Profile");
		resource.setConsumable(true);
		resource.setDiscoverable(true);
		resource.setExternallyConsumed(true);
		resource.setConsumptionUnitName("PHP");

		BalanceAdjustmentProfile fulfillmentProfile = new BalanceAdjustmentProfile(name);
		fulfillmentProfile.setDedicatedAccountID(dedicatedAccountID);
		fulfillmentProfile.setDedicatedAccountUnitType(dedicatedAccountUnitType);
		fulfillmentProfile.setTransactionCurrency("PHP");
		

		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		profileRegistry.createProfile(fulfillmentProfile);

		return resource;
	}

	
	public Resource createSmartFlexiRefillProfile(String name) throws CatalogException {

		Resource resource = new Service(name);
		resource.setDescription("Balance Adjustment Profile");
		resource.setConsumable(true);
		resource.setDiscoverable(true);
		resource.setExternallyConsumed(true);
		resource.setConsumptionUnitName("PHP");

		FlexiRechargeProfile fulfillmentProfile = new FlexiRechargeProfile(name);
		

		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		profileRegistry.createProfile(fulfillmentProfile);

		return resource;
	}

	

	public Resource createSmartInitialSCWelcomePackProfile(String name, String description) throws Exception
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);
		
		SubscribePackageItemProfile fulfillmentProfile = new SubscribePackageItemProfile(name);
		fulfillmentProfile.setServiceClassAction("SetOriginal");
		
		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
		return resource;
	}

	public Resource createSmartWelcomePackProfile(String name, String description) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);
		
		SubscribePackageItemProfile fulfillmentProfile = new SubscribePackageItemProfile(name);
		fulfillmentProfile.setServiceClassAction("SetOriginal");
		
		List<PamInformation> pamList = new ArrayList<PamInformation>();
		PamInformation pamInfo =  new PamInformation();
		pamInfo.setPamServiceID(1);
		pamInfo.setPamClassID(1);
		pamInfo.setScheduleID(1);
		pamList.add(pamInfo);
		fulfillmentProfile.setPamInformationList(pamList);;

		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
		return resource;
	}


	public Resource createSmartTaggingProfile(String name, String description, int soId, boolean activeFlag) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		UpdateSubscriberSegmentationProfile fulfillmentProfile = new UpdateSubscriberSegmentationProfile(name);

		List<ServiceOffering> serviceOfferings = new ArrayList<ServiceOffering>();
		for (int i=0; i<8; i++ ) {
			ServiceOffering soInfo = new ServiceOffering();
			soInfo.setServiceOfferingId(soId);
			if (soId == 0)
				soInfo.setServiceOfferingActiveFlag(false);
			else
				soInfo.setServiceOfferingActiveFlag((soId==i));
			serviceOfferings.add(soInfo);
		}

		fulfillmentProfile.setServiceOfferings(serviceOfferings);


		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());

		return resource;		
	}
	
	public Resource createSmartModifyCustomerPreactiveProfile(String name, String description) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		UpdateSubscriberSegmentationProfile fulfillmentProfile = new UpdateSubscriberSegmentationProfile(name);

		int soId = 1;
		List<ServiceOffering> serviceOfferings = new ArrayList<ServiceOffering>();
		for (int i=0; i<8; i++ ) {
			ServiceOffering soInfo = new ServiceOffering();
			soInfo.setServiceOfferingId(i);
			soInfo.setServiceOfferingActiveFlag(i == soId);
			serviceOfferings.add(soInfo);
		}

		fulfillmentProfile.setServiceOfferings(serviceOfferings);


		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());

		return resource;		
	}

	public Resource createSmartModifyCustomerGraceProfile(String name, String description) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		ModifyCustomerGraceProfile fulfillmentProfile = new ModifyCustomerGraceProfile(name);
		fulfillmentProfile.setOfferRequestedTypeFlag("10100000");
		OfferSelection offerSelection = new OfferSelection();
		offerSelection.setOfferIDFirst(1);
		offerSelection.setOfferIDLast(10);
		fulfillmentProfile.setOfferSelection(new OfferSelection[]{offerSelection});
		fulfillmentProfile.setRefillProfileId("EXTG");
		fulfillmentProfile.setRefillType(1);
		fulfillmentProfile.setTransactionAmount("0");
		fulfillmentProfile.setTransactionCurrency(CurrencyCode.PHP);


		

		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());

		return resource;		
	}

	public Resource createSmartUnscribePackageItemProfile(String name, String description) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		UnsubscribePackageItemProfile fulfillmentProfile = new UnsubscribePackageItemProfile(name);
		fulfillmentProfile.setServiceClassAction("SetOriginal");
		
		List<ServiceOffering> offerings = new ArrayList<ServiceOffering>();
		int soId = 1;
		List<ServiceOffering> serviceOfferings = new ArrayList<ServiceOffering>();
		for (int i=0; i<8; i++ ) {
			ServiceOffering soInfo = new ServiceOffering();
			soInfo.setServiceOfferingId(i);
			soInfo.setServiceOfferingActiveFlag(i == soId);
			serviceOfferings.add(soInfo);
		}
		fulfillmentProfile.setServiceOfferings(offerings);


		

		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());

		return resource;		
	}

	
	public Resource createSmartEntireReadProfile(String name, String description) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		EntireReadSubscriberProfile fulfillmentProfile = new EntireReadSubscriberProfile(name);

		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());

		return resource;		
	}

	public Resource createSmartCusomerInfoChargeProfile(String name, String description) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		EntireReadSubscriberProfile fulfillmentProfile = new EntireReadSubscriberProfile(name);

		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());

		return resource;		
	}

	public Resource createSmartPartialReadProfile(String name, String description) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		PartialReadSubscriberProfile fulfillmentProfile = new PartialReadSubscriberProfile(name);

		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());

		return resource;		
	}

	public Resource createSmartReadBalancesProfile(String name, String description) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		ReadBalancesProfile fulfillmentProfile = new ReadBalancesProfile(name);

		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());

		return resource;		
	}

	public Resource createSmartReadProfile(String name, String description) throws CatalogException
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		ReadSubscriberProfile fulfillmentProfile = new ReadSubscriberProfile(name);

		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());

		return resource;		
	}

	
	public Resource createSmartEntireDeleteProfile(String name, String description) throws CatalogException{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);

		DeleteSubscriberProfile fulfillmentProfile = new DeleteSubscriberProfile(name);

		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());

		return resource;		
	}


			
}
