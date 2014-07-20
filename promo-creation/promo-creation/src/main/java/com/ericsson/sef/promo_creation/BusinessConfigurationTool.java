package com.ericsson.sef.promo_creation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.OfferManager;
import com.ericsson.raso.sef.bes.prodcat.SecureSerializationHelper;
import com.ericsson.raso.sef.bes.prodcat.ServiceRegistry;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.CommitUntilNDays;
import com.ericsson.raso.sef.bes.prodcat.entities.DaysTime;
import com.ericsson.raso.sef.bes.prodcat.entities.HoursTime;
import com.ericsson.raso.sef.bes.prodcat.entities.InfiniteTime;
import com.ericsson.raso.sef.bes.prodcat.entities.NoTermination;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.Price;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.entities.Service;
import com.ericsson.raso.sef.bes.prodcat.entities.State;
import com.ericsson.raso.sef.bes.prodcat.entities.TerminateAfterNDays;
import com.ericsson.raso.sef.bes.prodcat.entities.UnlimitedQuota;
import com.ericsson.raso.sef.bes.prodcat.entities.smart.SmartSimplePricingPolicy;
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
import com.ericsson.raso.sef.fulfillment.profiles.smart.CallingCircleProfile;
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
			resource = this.createCreateSubscriberProfile("CREATE_SUBSCRIBER", "Create a new Subscriber", 0, 0, 0, false, 0);
			handles = new ArrayList<String>();
			handles.add("CREATE_SUBSCRIBER");
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
			handles.add("MODIFY_SUBSCRIBER_TAGGING_SetSpecialFraudBit");
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
			bizConfig = this.getSimpleBcWorkflow("SUBSCRIBE_PACKAGE_ITEM", "WelcomePack - ServiceClass", handles, resource);
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
		
			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("AlkansyaLoad100", "AL01", 1, "10000", CurrencyCode.PHP, "1", "10000");
			handles = new ArrayList<String>();
			handles.add("WL3");
			bizConfig = this.getSimpleBcWorkflow("AlkansyaLoad100", "Alkansya Load Wallet Load 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("AlkansyaLoad15", "AL01", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("ALK01");
			bizConfig = this.getSimpleBcWorkflow("AlkansyaLoad15", "Alkansya Load Wallet Load 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("AlkansyaLoad150", "AL01", 1, "15000", CurrencyCode.PHP, "1", "15000");
			handles = new ArrayList<String>();
			handles.add("WL4");
			bizConfig = this.getSimpleBcWorkflow("AlkansyaLoad150", "Alkansya Load Wallet Load 150", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("AlkansyaLoad30", "AL01", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("WL1");
			bizConfig = this.getSimpleBcWorkflow("AlkansyaLoad30", "Alkansya Load Wallet Load 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("AlkansyaLoad300", "AL01", 1, "30000", CurrencyCode.PHP, "1", "30000");
			handles = new ArrayList<String>();
			handles.add("WL5");
			bizConfig = this.getSimpleBcWorkflow("AlkansyaLoad300", "Alkansya Load Wallet Load 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("AlkansyaLoad50", "AL01", 1, "5000", CurrencyCode.PHP, "1", "5000");
			handles = new ArrayList<String>();
			handles.add("WL2");
			bizConfig = this.getSimpleBcWorkflow("AlkansyaLoad50", "Alkansya Load Wallet Load 50", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("AlkansyaLoad500", "AL01", 1, "50000", CurrencyCode.PHP, "1", "50000");
			handles = new ArrayList<String>();
			handles.add("WL6");
			bizConfig = this.getSimpleBcWorkflow("AlkansyaLoad500", "Alkansya Load Wallet Load 500", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("Ipon", "AL01", 1, "1", CurrencyCode.PHP, "1", "1");
			handles = new ArrayList<String>();
			handles.add("FD1");
			bizConfig = this.getSimpleBcWorkflow("Ipon", "Convert Whole Uload Balance To AirTime", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("P5FirstPullAlkansyaMenu", "AL01", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("YTN11");
			bizConfig = this.getSimpleBcWorkflow("P5FirstPullAlkansyaMenu", "P5 Credit for First Pull of Alkansya menu", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Ask For Load...");
			resource = this.createRefill("AskforLoadP5", "ASL1", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("5");
			bizConfig = this.getSimpleBcWorkflow("AskforLoadP5", "Ask-for-Load P5", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, CTC Load...");
			resource = this.createRefill("TicketLoad10", "CTC1", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("10");
			bizConfig = this.getSimpleBcWorkflow("TicketLoad10", "TicketLoad 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, CTC Load...");
			resource = this.createRefill("TicketLoad15", "CTC1", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("15");
			bizConfig = this.getSimpleBcWorkflow("TicketLoad15", "TicketLoad 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, CTC Load...");
			resource = this.createRefill("TicketLoad30", "CTC1", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("30");
			bizConfig = this.getSimpleBcWorkflow("TicketLoad30", "TicketLoad 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, CTC Load...");
			resource = this.createRefill("CTCLoad100", "CTC1", 1, "10000", CurrencyCode.PHP, "1", "10000");
			handles = new ArrayList<String>();
			handles.add("100");
			bizConfig = this.getSimpleBcWorkflow("CTCLoad100", "CTC Load 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, CTC Load...");
			resource = this.createRefill("CTCLoad1000", "CTC1", 1, "100000", CurrencyCode.PHP, "1", "100000");
			handles = new ArrayList<String>();
			handles.add("1000");
			bizConfig = this.getSimpleBcWorkflow("CTCLoad1000", "CTC Load 1000", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, CTC Load...");
			resource = this.createRefill("CTCLoad300", "CTC1", 1, "30000", CurrencyCode.PHP, "1", "30000");
			handles = new ArrayList<String>();
			handles.add("300");
			bizConfig = this.getSimpleBcWorkflow("CTCLoad300", "CTC Load 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, CTC Load...");
			resource = this.createRefill("CTCLoad500", "CTC1", 1, "50000", CurrencyCode.PHP, "1", "50000");
			handles = new ArrayList<String>();
			handles.add("500");
			bizConfig = this.getSimpleBcWorkflow("CTCLoad500", "CTC Load 500", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Corporate Quick Load...");
			resource = this.createRefill("CorporateQuickLoad100", "CQ01", 1, "10000", CurrencyCode.PHP, "1", "10000");
			handles = new ArrayList<String>();
			handles.add("B1");
			bizConfig = this.getSimpleBcWorkflow("CorporateQuickLoad100", "Corporate Quick Load 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Corporate Quick Load...");
			resource = this.createRefill("CorporateQuickLoad1000", "CQ01", 1, "100000", CurrencyCode.PHP, "1", "100000");
			handles = new ArrayList<String>();
			handles.add("X8");
			bizConfig = this.getSimpleBcWorkflow("CorporateQuickLoad1000", "Corporate Quick Load 1000", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Corporate Quick Load...");
			resource = this.createRefill("CorporateQuickLoad200", "CQ01", 1, "20000", CurrencyCode.PHP, "1", "20000");
			handles = new ArrayList<String>();
			handles.add("XA");
			bizConfig = this.getSimpleBcWorkflow("CorporateQuickLoad200", "Corporate Quick Load 200", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Corporate Quick Load...");
			resource = this.createRefill("CorporateQuickLoad300", "CQ01", 1, "30000", CurrencyCode.PHP, "1", "30000");
			handles = new ArrayList<String>();
			handles.add("X6");
			bizConfig = this.getSimpleBcWorkflow("CorporateQuickLoad300", "Corporate Quick Load 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Corporate Quick Load...");
			resource = this.createRefill("CorporateQuickLoad500", "CQ01", 1, "50000", CurrencyCode.PHP, "1", "50000");
			handles = new ArrayList<String>();
			handles.add("X7");
			bizConfig = this.getSimpleBcWorkflow("CorporateQuickLoad500", "Corporate Quick Load 500", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("Economy30", "L001", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("A");
			handles.add("T1");
			handles.add("SM2");
			bizConfig = this.getSimpleBcWorkflow("Economy30", "Economy 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("Economy30SM", "L001", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("SM12");
			bizConfig = this.getSimpleBcWorkflow("Economy30SM", "Economy 30 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Emergency Load...");
			resource = this.createRefill("EmergencyLoadSMS", "EL01", 1, "400", CurrencyCode.PHP, "1", "400");
			handles = new ArrayList<String>();
			handles.add("PBB");
			bizConfig = this.getSimpleBcWorkflow("EmergencyLoadSMS", "Emergency Load SMS", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Emergency Load...");
			resource = this.createRefill("EmergencyLoadSMSAllNetSMS", "EL01", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("PBB3");
			bizConfig = this.getSimpleBcWorkflow("EmergencyLoadSMSAllNetSMS", "Emergency Load SMS - All Net SMS", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Emergency Load...");
			resource = this.createRefill("EmergencyLoadVoice", "EL01", 1, "650", CurrencyCode.PHP, "1", "650");
			handles = new ArrayList<String>();
			handles.add("CBBV1");
			bizConfig = this.getSimpleBcWorkflow("EmergencyLoadVoice", "Emergency Load Voice", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("Extra115", "L001", 1, "11500", CurrencyCode.PHP, "1", "11500");
			handles = new ArrayList<String>();
			handles.add("C");
			handles.add("T3");
			bizConfig = this.getSimpleBcWorkflow("Extra115", "Extra 115", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Pasaload Refill...");
			resource = this.createRefill("PasaloadP10", "PL01", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("10");
			handles.add("O3");
			handles.add("W3");
			bizConfig = this.getSimpleBcWorkflow("PasaloadP10", "Pasaload P10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Pasaload Refill...");
			resource = this.createRefill("PasaloadP100", "PL01", 1, "10000", CurrencyCode.PHP, "1", "10000");
			handles = new ArrayList<String>();
			handles.add("1H");
			handles.add("1O");
			handles.add("W8");
			bizConfig = this.getSimpleBcWorkflow("PasaloadP100", "Pasaload P100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Pasaload Refill...");
			resource = this.createRefill("PasaloadP15", "PL01", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("15");
			handles.add("O4");
			handles.add("W4");
			bizConfig = this.getSimpleBcWorkflow("PasaloadP15", "Pasaload P15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Pasaload Refill...");
			resource = this.createRefill("PasaloadP2", "PL01", 1, "200", CurrencyCode.PHP, "1", "200");
			handles = new ArrayList<String>();
			handles.add("2");
			handles.add("O1");
			handles.add("W1");
			bizConfig = this.getSimpleBcWorkflow("PasaloadP2", "Pasaload P2", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Pasaload Refill...");
			resource = this.createRefill("PasaloadP20", "PL01", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("20");
			handles.add("O5");
			handles.add("W5");
			bizConfig = this.getSimpleBcWorkflow("PasaloadP20", "Pasaload P20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Pasaload Refill...");
			resource = this.createRefill("PasaloadP200", "PL01", 1, "20000", CurrencyCode.PHP, "1", "20000");
			handles = new ArrayList<String>();
			handles.add("2H");
			handles.add("2O");
			handles.add("W9");
			bizConfig = this.getSimpleBcWorkflow("PasaloadP200", "Pasaload P200", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Pasaload Refill...");
			resource = this.createRefill("PasaloadP30", "PL01", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("30");
			handles.add("O7");
			handles.add("W6");
			bizConfig = this.getSimpleBcWorkflow("PasaloadP30", "Pasaload P30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Pasaload Refill...");
			resource = this.createRefill("PasaloadP35", "PL01", 1, "100", CurrencyCode.PHP, "1", "100");
			handles = new ArrayList<String>();
			handles.add("35T");
			handles.add("O8");
			handles.add("Y2");
			bizConfig = this.getSimpleBcWorkflow("PasaloadP35", "Pasaload P35", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Pasaload Refill...");
			resource = this.createRefill("PasaloadP5", "PL01", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("5");
			handles.add("O2");
			handles.add("W2");
			bizConfig = this.getSimpleBcWorkflow("PasaloadP5", "Pasaload P5", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Pasaload Refill...");
			resource = this.createRefill("PasaloadP60", "PL01", 1, "6000", CurrencyCode.PHP, "1", "6000");
			handles = new ArrayList<String>();
			handles.add("60");
			handles.add("O9");
			handles.add("W7");
			bizConfig = this.getSimpleBcWorkflow("PasaloadP60", "Pasaload P60", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliAlkansyaInstantTextP1", "AL01", 1, "100", CurrencyCode.PHP, "1", "100");
			handles = new ArrayList<String>();
			handles.add("ALK07");
			bizConfig = this.getSimpleBcWorkflow("PasariliAlkansyaInstantTextP1", "PasariliAlkansya InstantText P1", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliAlkansyaInstantTextP2", "AL01", 1, "200", CurrencyCode.PHP, "1", "200");
			handles = new ArrayList<String>();
			handles.add("ALK08");
			bizConfig = this.getSimpleBcWorkflow("PasariliAlkansyaInstantTextP2", "PasariliAlkansya InstantText P2", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliAlkansyaInstantTextP3", "AL01", 1, "300", CurrencyCode.PHP, "1", "300");
			handles = new ArrayList<String>();
			handles.add("ALK09");
			bizConfig = this.getSimpleBcWorkflow("PasariliAlkansyaInstantTextP3", "PasariliAlkansya InstantText P3", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliAlkansyaInstantTextP4", "AL01", 1, "400", CurrencyCode.PHP, "1", "400");
			handles = new ArrayList<String>();
			handles.add("ALK10");
			bizConfig = this.getSimpleBcWorkflow("PasariliAlkansyaInstantTextP4", "PasariliAlkansya InstantText P4", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliAlkansyaInstantTextP5", "AL01", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("ALK11");
			bizConfig = this.getSimpleBcWorkflow("PasariliAlkansyaInstantTextP5", "PasariliAlkansya InstantText P5", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliAlkansyaInstantTextP6", "AL01", 1, "600", CurrencyCode.PHP, "1", "600");
			handles = new ArrayList<String>();
			handles.add("ALK12");
			bizConfig = this.getSimpleBcWorkflow("PasariliAlkansyaInstantTextP6", "PasariliAlkansya InstantText P6", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliAlkansyaInstantTextP7", "AL01", 1, "700", CurrencyCode.PHP, "1", "700");
			handles = new ArrayList<String>();
			handles.add("ALK13");
			bizConfig = this.getSimpleBcWorkflow("PasariliAlkansyaInstantTextP7", "PasariliAlkansya InstantText P7", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliAlkansyaInstantTextP8", "AL01", 1, "800", CurrencyCode.PHP, "1", "800");
			handles = new ArrayList<String>();
			handles.add("ALK14");
			bizConfig = this.getSimpleBcWorkflow("PasariliAlkansyaInstantTextP8", "PasariliAlkansya InstantText P8", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliAlkansyaInstantTextP9", "AL01", 1, "900", CurrencyCode.PHP, "1", "900");
			handles = new ArrayList<String>();
			handles.add("ALK15");
			bizConfig = this.getSimpleBcWorkflow("PasariliAlkansyaInstantTextP9", "PasariliAlkansya InstantText P9", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliP3", "AL01", 1, "300", CurrencyCode.PHP, "1", "300");
			handles = new ArrayList<String>();
			handles.add("ALK06");
			bizConfig = this.getSimpleBcWorkflow("PasariliP3", "Pasarili P3", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliPatok10", "AL01", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("ALK18");
			bizConfig = this.getSimpleBcWorkflow("PasariliPatok10", "Pasarili Patok10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliUnliTxt10", "AL01", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("ALK17");
			bizConfig = this.getSimpleBcWorkflow("PasariliUnliTxt10", "Pasarili UnliTxt10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliUnliTxt5", "AL01", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("ALK16");
			bizConfig = this.getSimpleBcWorkflow("PasariliUnliTxt5", "Pasarili UnliTxt5", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliUpsize20", "AL01", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("T13");
			bizConfig = this.getSimpleBcWorkflow("PasariliUpsize20", "Pasarili Upsize20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Alkansya Load...");
			resource = this.createRefill("PasariliUpsize35", "AL01", 1, "3500", CurrencyCode.PHP, "1", "3500");
			handles = new ArrayList<String>();
			handles.add("T11");
			bizConfig = this.getSimpleBcWorkflow("PasariliUpsize35", "Pasarili Upsize35", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Presyong Pinoy Load...");
			resource = this.createRefill("PresyongPinoyLoad115", "PP01", 1, "11500", CurrencyCode.PHP, "1", "11500");
			handles = new ArrayList<String>();
			handles.add("BR002");
			bizConfig = this.getSimpleBcWorkflow("PresyongPinoyLoad115", "Presyong Pinoy Load 115", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Presyong Pinoy Load...");
			resource = this.createRefill("PresyongPinoyLoad30", "PP01", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("BR007");
			bizConfig = this.getSimpleBcWorkflow("PresyongPinoyLoad30", "Presyong Pinoy Load 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Presyong Pinoy Load...");
			resource = this.createRefill("PresyongPinoyLoad60", "PP01", 1, "6000", CurrencyCode.PHP, "1", "6000");
			handles = new ArrayList<String>();
			handles.add("BR008");
			bizConfig = this.getSimpleBcWorkflow("PresyongPinoyLoad60", "Presyong Pinoy Load 60", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Presyong Pinoy Load...");
			resource = this.createRefill("PresyongPinoyLoad100", "PP01", 1, "10000", CurrencyCode.PHP, "1", "10000");
			handles = new ArrayList<String>();
			handles.add("BR001");
			bizConfig = this.getSimpleBcWorkflow("PresyongPinoyLoad100", "Presyong Pinoy Load 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Presyong Pinoy Load...");
			resource = this.createRefill("PresyongPinoyLoad1000", "PP01", 1, "100000", CurrencyCode.PHP, "1", "100000");
			handles = new ArrayList<String>();
			handles.add("BR006");
			bizConfig = this.getSimpleBcWorkflow("PresyongPinoyLoad1000", "Presyong Pinoy Load 1000", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Presyong Pinoy Load...");
			resource = this.createRefill("PresyongPinoyLoad200", "PP01", 1, "20000", CurrencyCode.PHP, "1", "20000");
			handles = new ArrayList<String>();
			handles.add("BR003");
			bizConfig = this.getSimpleBcWorkflow("PresyongPinoyLoad200", "Presyong Pinoy Load 200", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Presyong Pinoy Load...");
			resource = this.createRefill("PresyongPinoyLoad300", "PP01", 1, "30000", CurrencyCode.PHP, "1", "30000");
			handles = new ArrayList<String>();
			handles.add("BR004");
			bizConfig = this.getSimpleBcWorkflow("PresyongPinoyLoad300", "Presyong Pinoy Load 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Presyong Pinoy Load...");
			resource = this.createRefill("PresyongPinoyLoad500", "PP01", 1, "50000", CurrencyCode.PHP, "1", "50000");
			handles = new ArrayList<String>();
			handles.add("BR005");
			bizConfig = this.getSimpleBcWorkflow("PresyongPinoyLoad500", "Presyong Pinoy Load 500", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("Regular60", "L001", 1, "6000", CurrencyCode.PHP, "1", "6000");
			handles = new ArrayList<String>();
			handles.add("B");
			handles.add("T2");
			bizConfig = this.getSimpleBcWorkflow("Regular60", "Regular 60", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("RegularLoadP10", "L001", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("TPW04");
			handles.add("Plan10");
			bizConfig = this.getSimpleBcWorkflow("RegularLoadP10", "Regular Load P10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad100", "L001", 1, "10000", CurrencyCode.PHP, "1", "10000");
			handles = new ArrayList<String>();
			handles.add("X2");
			handles.add("T20");
			handles.add("SM5");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad100", "SMARTLoad 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad100SM", "L001", 1, "10000", CurrencyCode.PHP, "1", "10000");
			handles = new ArrayList<String>();
			handles.add("SM14");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad100SM", "SMARTLoad 100 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad1000", "L001", 1, "100000", CurrencyCode.PHP, "1", "100000");
			handles = new ArrayList<String>();
			handles.add("F");
			handles.add("T6");
			handles.add("SM9");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad1000", "SMARTLoad 1000", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad1000SM", "L001", 1, "100000", CurrencyCode.PHP, "1", "100000");
			handles = new ArrayList<String>();
			handles.add("SM18");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad1000SM", "SMARTLoad 1000 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad15", "L001", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("U");
			handles.add("T17");
			handles.add("SM1");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad15", "SMARTLoad 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad15SM", "L001", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("SM11");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad15SM", "SMARTLoad 15 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad20", "L001", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("BD5");
			handles.add("T10");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad20", "SMARTLoad 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad200", "L001", 1, "20000", CurrencyCode.PHP, "1", "20000");
			handles = new ArrayList<String>();
			handles.add("G");
			handles.add("T7");
			handles.add("SM6");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad200", "SMARTLoad 200", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad200SM", "L001", 1, "20000", CurrencyCode.PHP, "1", "20000");
			handles = new ArrayList<String>();
			handles.add("SM15");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad200SM", "SMARTLoad 200 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad250", "L001", 1, "25000", CurrencyCode.PHP, "1", "25000");
			handles = new ArrayList<String>();
			handles.add("RA001");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad250", "SMARTLoad 250", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad300", "L001", 1, "30000", CurrencyCode.PHP, "1", "30000");
			handles = new ArrayList<String>();
			handles.add("D");
			handles.add("T4");
			handles.add("SM7");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad300", "SMARTLoad 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad300SM", "L001", 1, "30000", CurrencyCode.PHP, "1", "30000");
			handles = new ArrayList<String>();
			handles.add("SM16");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad300SM", "SMARTLoad 300 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad50", "L001", 1, "5000", CurrencyCode.PHP, "1", "5000");
			handles = new ArrayList<String>();
			handles.add("X1");
			handles.add("T19");
			handles.add("TKT01");
			handles.add("SM3");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad50", "SMARTLoad 50", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad50SM", "L001", 1, "5000", CurrencyCode.PHP, "1", "5000");
			handles = new ArrayList<String>();
			handles.add("SM13");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad50SM", "SMARTLoad 50 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad500", "L001", 1, "50000", CurrencyCode.PHP, "1", "50000");
			handles = new ArrayList<String>();
			handles.add("E");
			handles.add("T5");
			handles.add("SM8");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad500", "SMARTLoad 500", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Regular Refill...");
			resource = this.createRefill("SMARTLoad500SM", "L001", 1, "50000", CurrencyCode.PHP, "1", "50000");
			handles = new ArrayList<String>();
			handles.add("SM17");
			bizConfig = this.getSimpleBcWorkflow("SMARTLoad500SM", "SMARTLoad 500 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, TakaTak Refill...");
			resource = this.createRefill("Takatak12", "TK01", 1, "1200", CurrencyCode.PHP, "1", "1200");
			handles = new ArrayList<String>();
			handles.add("TK3");
			bizConfig = this.getSimpleBcWorkflow("Takatak12", "Takatak 12", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, TakaTak Refill...");
			resource = this.createRefill("Takatak3", "TK01", 1, "300", CurrencyCode.PHP, "1", "300");
			handles = new ArrayList<String>();
			handles.add("TK1");
			bizConfig = this.getSimpleBcWorkflow("Takatak3", "Takatak 3", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, TakaTak Refill...");
			resource = this.createRefill("Takatak6", "TK01", 1, "600", CurrencyCode.PHP, "1", "600");
			handles = new ArrayList<String>();
			handles.add("TK2");
			bizConfig = this.getSimpleBcWorkflow("Takatak6", "Takatak 6", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, VAS Refill...");
			resource = this.createRefill("EloadPromo5000", "VS01", 1, "500000", CurrencyCode.PHP, "1", "500000");
			handles = new ArrayList<String>();
			handles.add("ELP01");
			bizConfig = this.getSimpleBcWorkflow("EloadPromo5000", "Eload Promo 5000", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, VAS Refill...");
			resource = this.createRefill("EloadPromo10000", "VS01", 1, "1000000", CurrencyCode.PHP, "1", "1000000");
			handles = new ArrayList<String>();
			handles.add("ELP02");
			bizConfig = this.getSimpleBcWorkflow("EloadPromo10000", "Eload Promo 10000", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);



			System.out.println("Offer, Resource & Profile - Predefined, VAS Refill...");
			resource = this.createRefill("KPR01", "VS01", 1, "300", CurrencyCode.PHP, "1", "300");
			handles = new ArrayList<String>();
			handles.add("KPR01");
			bizConfig = this.getSimpleBcWorkflow("KPR01", "KaPartner Rewards 300 on-net SMS", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, VAS Refill...");
			resource = this.createRefill("DBP01", "VS01", 1, "100", CurrencyCode.PHP, "1", "100");
			handles = new ArrayList<String>();
			handles.add("DBP01");
			bizConfig = this.getSimpleBcWorkflow("DBP01", "DB Buliding and Profiling Program 20 Onnet SMS", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, VAS Refill...");
			resource = this.createRefill("UNI01", "VS01", 1, "2500", CurrencyCode.PHP, "1", "2500");
			handles = new ArrayList<String>();
			handles.add("UNI01");
			bizConfig = this.getSimpleBcWorkflow("UNI01", "Unilever Reward 25 On Net SMS", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, VAS Refill...");
			resource = this.createRefill("KPR11", "VS01", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("KPR11");
			bizConfig = this.getSimpleBcWorkflow("KPR11", "KaPartner Rewards Gaan All-In-One 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, VAS Refill...");
			resource = this.createRefill("KPR12", "VS01", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("KPR12");
			bizConfig = this.getSimpleBcWorkflow("KPR12", "KaPartner Rewards Gaan Text 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, VAS Refill...");
			resource = this.createRefill("IOSMSJapan", "VS01", 1, "200", CurrencyCode.PHP, "1", "200");
			handles = new ArrayList<String>();
			handles.add("I");
			bizConfig = this.getSimpleBcWorkflow("IOSMSJapan", "IOSMS Japan", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTxt2All15Regional", "1001", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("RB328");
			handles.add("PWIL3");
			bizConfig = this.getSimpleBcWorkflow("UnliTxt2All15Regional", "UnliTxt2All 15 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanUnliTxtPlus15", "1002", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("RB410");
			handles.add("GTX15");
			bizConfig = this.getSimpleBcWorkflow("GaanUnliTxtPlus15", "GaanUnliTxt Plus 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanUnliTxtPlus20", "1003", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("SB46");
			handles.add("GT20T");
			bizConfig = this.getSimpleBcWorkflow("GaanUnliTxtPlus20", "GaanUnliTxt Plus 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanUnliTxtPlus30", "1004", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("SB47");
			handles.add("GT30T");
			bizConfig = this.getSimpleBcWorkflow("GaanUnliTxtPlus30", "GaanUnliTxt Plus 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTalkPlus20", "1005", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("UCT17");
			handles.add("UP20");
			bizConfig = this.getSimpleBcWorkflow("UnliTalkPlus20", "UnliTalk Plus 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTalkPlus100", "1006", 1, "10000", CurrencyCode.PHP, "1", "10000");
			handles = new ArrayList<String>();
			handles.add("UCT4");
			handles.add("T22");
			bizConfig = this.getSimpleBcWorkflow("UnliTalkPlus100", "UnliTalk Plus 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTextExtra30", "1007", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("RB510");
			handles.add("PJST1");
			bizConfig = this.getSimpleBcWorkflow("UnliTextExtra30", "UnliTextExtra 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanTxt10", "1008", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("RB60");
			handles.add("Q");
			bizConfig = this.getSimpleBcWorkflow("GaanTxt10", "GaanTxt 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanTxt20", "1009", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("RB323");
			handles.add("X5");
			bizConfig = this.getSimpleBcWorkflow("GaanTxt20", "GaanTxt 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTxt10", "1010", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("UF57");
			bizConfig = this.getSimpleBcWorkflow("UnliTxt10", "UnliTxt 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTxt150", "1011", 1, "15000", CurrencyCode.PHP, "1", "15000");
			handles = new ArrayList<String>();
			handles.add("UF137");
			handles.add("PWIL1");
			bizConfig = this.getSimpleBcWorkflow("UnliTxt150", "UnliTxt 150", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTxt51hr", "1012", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("UF58");
			bizConfig = this.getSimpleBcWorkflow("UnliTxt51hr", "UnliTxt5 1 hr", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTxt2All20", "1013", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("RB511");
			handles.add("PJST2");
			bizConfig = this.getSimpleBcWorkflow("UnliTxt2All20", "UnliTxt2All 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTxt15Regional", "1014", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("UF166");
			handles.add("UPK05");
			bizConfig = this.getSimpleBcWorkflow("UnliTxt15Regional", "UnliTxt 15 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTxt5for12hrsRegional", "1015", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("UF165");
			handles.add("UPK04");
			bizConfig = this.getSimpleBcWorkflow("UnliTxt5for12hrsRegional", "UnliTxt 5 for 12 hrs Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTxt20Regional", "1016", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("UF235");
			handles.add("UNL20");
			bizConfig = this.getSimpleBcWorkflow("UnliTxt20Regional", "UnliTxt 20 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("AllText10", "1017", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("J");
			bizConfig = this.getSimpleBcWorkflow("AllText10", "All Text 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("PaTokOTex10", "1019", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("RB95");
			handles.add("FMX1");
			bizConfig = this.getSimpleBcWorkflow("PaTokOTex10", "PaTok-O-Tex 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("PaTokOTex15", "1020", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("RB460");
			handles.add("PTT15");
			bizConfig = this.getSimpleBcWorkflow("PaTokOTex15", "PaTok-O-Tex 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("PaTokOTex20", "1021", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("RB96");
			handles.add("FMX2");
			bizConfig = this.getSimpleBcWorkflow("PaTokOTex20", "PaTok-O-Tex 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("PaTokOTex30", "1022", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("RB97");
			handles.add("FMX3");
			bizConfig = this.getSimpleBcWorkflow("PaTokOTex30", "PaTok-O-Tex 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTxtAll_UnliTropaCall20", "1023", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("RB531");
			handles.add("CSW01");
			bizConfig = this.getSimpleBcWorkflow("UnliTxtAll_UnliTropaCall20", "UnliTxtAll_UnliTropaCall 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanUnliTxtPlus15Regional", "1024", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("RB537");
			handles.add("GAU15");
			bizConfig = this.getSimpleBcWorkflow("GaanUnliTxtPlus15Regional", "GaanUnliTxtPlus 15 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("SangkatuTex15", "1025", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("RB288");
			handles.add("SNGK1");
			bizConfig = this.getSimpleBcWorkflow("SangkatuTex15", "SangkatuTex 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("SangkatuTex30", "1026", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("RB289");
			handles.add("SNGK2");
			bizConfig = this.getSimpleBcWorkflow("SangkatuTex30", "SangkatuTex 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliCombo10Regional", "1027", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("UCT37");
			handles.add("TUAS3");
			bizConfig = this.getSimpleBcWorkflow("UnliCombo10Regional", "Unli Combo 10 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliSingkoRegional", "1028", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("UF170");
			handles.add("TUAS2");
			bizConfig = this.getSimpleBcWorkflow("UnliSingkoRegional", "Unli Singko Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("KaTokUNLISMSforMember", "KF25", 1, "0", CurrencyCode.PHP, "1", "0");
			handles = new ArrayList<String>();
			handles.add("GR007");
			bizConfig = this.getSimpleBcWorkflow("KaTokUNLISMSforMember", "KaTok-at-Tex25 KaTok-at-Tex35 UNLI SMS for Member", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BonggaTxt5", "1033", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("RB270");
			handles.add("TPW01");
			bizConfig = this.getSimpleBcWorkflow("BonggaTxt5", "BonggaTxt 5", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTxtPlus10", "1034", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("RB271");
			handles.add("TPW02");
			bizConfig = this.getSimpleBcWorkflow("UnliTxtPlus10", "UnliTxt Plus 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanAllInOne15", "1035", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("RB461");
			handles.add("GAO15");
			bizConfig = this.getSimpleBcWorkflow("GaanAllInOne15", "Gaan-All-In-One 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanAllInOne20", "1036", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("RB462");
			handles.add("GAO20");
			bizConfig = this.getSimpleBcWorkflow("GaanAllInOne20", "Gaan-All-In-One 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanAllInOne15Regional", "1037", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("RB526");
			handles.add("GAM15");
			bizConfig = this.getSimpleBcWorkflow("GaanAllInOne15Regional", "Gaan-All-In-One 15 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanAllInOne20Regional", "1038", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("RB527");
			handles.add("GAM20");
			bizConfig = this.getSimpleBcWorkflow("GaanAllInOne20Regional", "Gaan-All-In-One 20 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("SangkatuTok15", "1039", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("SB338");
			handles.add("SNGK4");
			bizConfig = this.getSimpleBcWorkflow("SangkatuTok15", "SangkatuTok 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("SangkatuTok30", "1040", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("SB339");
			handles.add("SNGK5");
			bizConfig = this.getSimpleBcWorkflow("SangkatuTok30", "SangkatuTok 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("Txt2All5", "1041", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("RB325");
			handles.add("OUT05");
			bizConfig = this.getSimpleBcWorkflow("Txt2All5", "Txt2All5", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TokTok15", "1042", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("SB603");
			bizConfig = this.getSimpleBcWorkflow("TokTok15", "TokTok 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TokTok10", "1043", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("SB602");
			bizConfig = this.getSimpleBcWorkflow("TokTok10", "TokTok 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("PantawidLoad1Bucket", "1045", 1, "100", CurrencyCode.PHP, "1", "100");
			handles = new ArrayList<String>();
			handles.add("RB501");
			bizConfig = this.getSimpleBcWorkflow("PantawidLoad1Bucket", "Pantawid Load 1 Bucket", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliTxt2AllPlus10", "1046", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("RB536");
			bizConfig = this.getSimpleBcWorkflow("UnliTxt2AllPlus10", "UnliTxt2All Plus 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliText2All300", "1047", 1, "20000", CurrencyCode.PHP, "1", "20000");
			handles = new ArrayList<String>();
			handles.add("RB339");
			bizConfig = this.getSimpleBcWorkflow("UnliText2All300", "UnliText2All 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("RegionalCombo10", "1052", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("RB549");
			handles.add("SHP10");
			bizConfig = this.getSimpleBcWorkflow("RegionalCombo10", "Regional Combo 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliAllText10Regional", "1053", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("RB550");
			handles.add("TXA10");
			bizConfig = this.getSimpleBcWorkflow("UnliAllText10Regional", "Unli All Text 10 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnlitextTrioCombo10", "1055", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("ZTN03");
			handles.add("YTN40");
			bizConfig = this.getSimpleBcWorkflow("UnlitextTrioCombo10", "Unlitext Trio Combo 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnlitextTrioCombo15", "1056", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("ZTN04");
			handles.add("YTN41");
			bizConfig = this.getSimpleBcWorkflow("UnlitextTrioCombo15", "Unlitext Trio Combo 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnlitextTrioCombo20", "1057", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("ZTN05");
			handles.add("YTN42");
			bizConfig = this.getSimpleBcWorkflow("UnlitextTrioCombo20", "Unlitext Trio Combo 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnlitextTrioCombo30", "1058", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("ZTN06");
			handles.add("YTN43");
			bizConfig = this.getSimpleBcWorkflow("UnlitextTrioCombo30", "Unlitext Trio Combo 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("EnhancedGA15Regional", "1059", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("ZTN07");
			handles.add("YTN44");
			bizConfig = this.getSimpleBcWorkflow("EnhancedGA15Regional", "Enhanced GA15 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("EnhancedGA20Regional", "1060", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("ZTN08");
			handles.add("YTN45");
			bizConfig = this.getSimpleBcWorkflow("EnhancedGA20Regional", "Enhanced GA20 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TokenGaanAllInOne55SMS", "1061", 1, "5500", CurrencyCode.PHP, "1", "5500");
			handles = new ArrayList<String>();
			handles.add("RB529");
			bizConfig = this.getSimpleBcWorkflow("TokenGaanAllInOne55SMS", "Token - Gaan All-In-One 55 - SMS", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TokenGaanAllInOne55Voice", "1062", 1, "5500", CurrencyCode.PHP, "1", "5500");
			handles = new ArrayList<String>();
			handles.add("RB530");
			bizConfig = this.getSimpleBcWorkflow("TokenGaanAllInOne55Voice", "Token - Gaan All-In-One 55 - Voice", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TokenGaanUnliTxtPlus55Voice", "1063", 1, "5500", CurrencyCode.PHP, "1", "5500");
			handles = new ArrayList<String>();
			handles.add("SB583");
			bizConfig = this.getSimpleBcWorkflow("TokenGaanUnliTxtPlus55Voice", "Token - Gaan UnliTxtPlus55 - Voice", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanAllInOne30", "1064", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("RB457");
			handles.add("GA30");
			handles.add("SM152");
			handles.add("PlanGAIO30");
			bizConfig = this.getSimpleBcWorkflow("GaanAllInOne30", "Gaan-All-In-One 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);



			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanAllInOne55", "1065", 1, "5500", CurrencyCode.PHP, "1", "5500");
			handles = new ArrayList<String>();
			handles.add("RB458");
			handles.add("GA55");
			handles.add("SM153");
			handles.add("PlanGAIO55");
			bizConfig = this.getSimpleBcWorkflow("GaanAllInOne55", "Gaan-All-In-One 55", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("GaanAllInOne99", "1066", 1, "9900", CurrencyCode.PHP, "1", "9900");
			handles = new ArrayList<String>();
			handles.add("RB459");
			handles.add("GA99");
			handles.add("SM154");
			handles.add("PlanGAIO99");
			bizConfig = this.getSimpleBcWorkflow("GaanAllInOne99", "Gaan-All-In-One 99", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("Trinet300", "1067", 1, "30000", CurrencyCode.PHP, "1", "30000");
			handles = new ArrayList<String>();
			handles.add("GR027");
			bizConfig = this.getSimpleBcWorkflow("Trinet300", "Trinet 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("EnhancedAllText20", "1018", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("K");
			handles.add("SM10");
			handles.add("PlanA20");
			bizConfig = this.getSimpleBcWorkflow("EnhancedAllText20", "Enhanced All Text 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("PantawidLoad1BucketEload", "1070", 1, "100", CurrencyCode.PHP, "1", "100");
			handles = new ArrayList<String>();
			handles.add("RPL01");
			bizConfig = this.getSimpleBcWorkflow("PantawidLoad1BucketEload", "Pantawid Load 1 Bucket Eload", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("ProjectMagneto_UNLITRIO20PLUS", "1071", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("ZTN13");
			handles.add("YTN46");
			bizConfig = this.getSimpleBcWorkflow("ProjectMagneto_UNLITRIO20PLUS", "Project Magneto_UNLITRIO 20 PLUS", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("ProjectSurprise_UnliTexttoAll10", "1072", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("ZTN14");
			bizConfig = this.getSimpleBcWorkflow("ProjectSurprise_UnliTexttoAll10", "Project Surprise_Unli Text to All 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("ProjectTrio_UnliCombo10", "1073", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("UCT37");
			handles.add("TUAS3");
			bizConfig = this.getSimpleBcWorkflow("ProjectTrio_UnliCombo10", "Project Trio_UnliCombo10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("ProjectTrio_UnliSingko", "1074", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("UF170");
			handles.add("TUAS2");
			bizConfig = this.getSimpleBcWorkflow("ProjectTrio_UnliSingko", "Project Trio_UnliSingko", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("ProjectTrio_Call_and_Text10", "1075", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("YBU48");
			bizConfig = this.getSimpleBcWorkflow("ProjectTrio_Call_and_Text10", "Project Trio_Call_and_Text 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("ProjectTrio_UnliTxt2All10", "1076", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("YBU49");
			bizConfig = this.getSimpleBcWorkflow("ProjectTrio_UnliTxt2All10", "Project Trio_UnliTxt2All10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("KamusText20", "3001", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("RB353");
			bizConfig = this.getSimpleBcWorkflow("KamusText20", "KamusText 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("FlexiCallTextAbroad30", "3002", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("RB354");
			bizConfig = this.getSimpleBcWorkflow("FlexiCallTextAbroad30", "Flexi Call Text Abroad 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("FlexiCallTextAbroad50", "3003", 1, "5000", CurrencyCode.PHP, "1", "5000");
			handles = new ArrayList<String>();
			handles.add("RB355");
			bizConfig = this.getSimpleBcWorkflow("FlexiCallTextAbroad50", "Flexi Call Text Abroad 50", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TalkALot300", "3004", 1, "30000", CurrencyCode.PHP, "1", "30000");
			handles = new ArrayList<String>();
			handles.add("RB352");
			bizConfig = this.getSimpleBcWorkflow("TalkALot300", "Talk-A-Lot 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TextALot35", "3005", 1, "3500", CurrencyCode.PHP, "1", "3500");
			handles = new ArrayList<String>();
			handles.add("RB350");
			bizConfig = this.getSimpleBcWorkflow("TextALot35", "Text-A-Lot 35", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TextALot60", "3006", 1, "6000", CurrencyCode.PHP, "1", "6000");
			handles = new ArrayList<String>();
			handles.add("RB351");
			bizConfig = this.getSimpleBcWorkflow("TextALot60", "Text-A-Lot 60", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TextTipid100", "3007", 1, "10000", CurrencyCode.PHP, "1", "10000");
			handles = new ArrayList<String>();
			handles.add("RB313");
			bizConfig = this.getSimpleBcWorkflow("TextTipid100", "TextTipid 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TextTipid150", "3008", 1, "15000", CurrencyCode.PHP, "1", "15000");
			handles = new ArrayList<String>();
			handles.add("RB314");
			bizConfig = this.getSimpleBcWorkflow("TextTipid150", "TextTipid 150", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TextTipid200", "3009", 1, "20000", CurrencyCode.PHP, "1", "20000");
			handles = new ArrayList<String>();
			handles.add("RB315");
			bizConfig = this.getSimpleBcWorkflow("TextTipid200", "TextTipid 200", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TextTipid300", "3010", 1, "30000", CurrencyCode.PHP, "1", "30000");
			handles = new ArrayList<String>();
			handles.add("RB316");
			bizConfig = this.getSimpleBcWorkflow("TextTipid300", "TextTipid 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliSurf500", "3011", 1, "50000", CurrencyCode.PHP, "1", "50000");
			handles = new ArrayList<String>();
			handles.add("SB374");
			handles.add("SB375");
			handles.add("SB376");
			handles.add("SB377");
			handles.add("SB378");
			handles.add("SB379");
			handles.add("SB380");
			handles.add("SB381");
			handles.add("SB382");
			handles.add("SB383");
			handles.add("SB384");
			handles.add("SB385");
			bizConfig = this.getSimpleBcWorkflow("UnliSurf500", "UnliSurf 500", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TalkTipid100", "3012", 1, "10000", CurrencyCode.PHP, "1", "10000");
			handles = new ArrayList<String>();
			handles.add("SB341");
			handles.add("SB345");
			bizConfig = this.getSimpleBcWorkflow("TalkTipid100", "TalkTipid 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TalkTipid300", "3013", 1, "30000", CurrencyCode.PHP, "1", "30000");
			handles = new ArrayList<String>();
			handles.add("SB342");
			handles.add("SB346");
			bizConfig = this.getSimpleBcWorkflow("TalkTipid300", "TalkTipid 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("IDDol20", "3014", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("RB541");
			bizConfig = this.getSimpleBcWorkflow("IDDol20", "IDDol 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("IDDol25", "3015", 1, "2500", CurrencyCode.PHP, "1", "2500");
			handles = new ArrayList<String>();
			handles.add("RB542");
			bizConfig = this.getSimpleBcWorkflow("IDDol25", "IDDol 25", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("IDDol40", "3016", 1, "4000", CurrencyCode.PHP, "1", "4000");
			handles = new ArrayList<String>();
			handles.add("RB543");
			bizConfig = this.getSimpleBcWorkflow("IDDol40", "IDDol 40", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("IDDol100", "3017", 1, "10000", CurrencyCode.PHP, "1", "10000");
			handles = new ArrayList<String>();
			handles.add("RB544");
			bizConfig = this.getSimpleBcWorkflow("IDDol100", "IDDol 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("IDDSale", "3018", 1, "0", CurrencyCode.PHP, "1", "0");
			handles = new ArrayList<String>();
			handles.add("ZBU16");
			bizConfig = this.getSimpleBcWorkflow("IDDSale", "IDD Sale", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("Unlisurf50", "5001", 1, "5000", CurrencyCode.PHP, "1", "5000");
			handles = new ArrayList<String>();
			handles.add("SB80");
			bizConfig = this.getSimpleBcWorkflow("Unlisurf50", "Unlisurf 50", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("TNTPaidSampler", "5005", 1, "100", CurrencyCode.PHP, "1", "100");
			handles = new ArrayList<String>();
			handles.add("SB606");
			bizConfig = this.getSimpleBcWorkflow("TNTPaidSampler", "TNT Paid Sampler", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("AlwaysOn10", "5006", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("SB397");
			bizConfig = this.getSimpleBcWorkflow("AlwaysOn10", "Always On 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("AlwaysOn20", "5007", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("SB521");
			bizConfig = this.getSimpleBcWorkflow("AlwaysOn20", "Always On 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("AlwaysOn30", "5008", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("SB522");
			bizConfig = this.getSimpleBcWorkflow("AlwaysOn30", "Always On 30 ", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("AlwaysOn99", "5009", 1, "9900", CurrencyCode.PHP, "1", "9900");
			handles = new ArrayList<String>();
			handles.add("SB497");
			bizConfig = this.getSimpleBcWorkflow("AlwaysOn99", "Always On 99", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("AlwaysOn199", "5010", 1, "19900", CurrencyCode.PHP, "1", "19900");
			handles = new ArrayList<String>();
			handles.add("SB580");
			bizConfig = this.getSimpleBcWorkflow("AlwaysOn199", "Always On 199", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("AlwaysOn299", "5011", 1, "29900", CurrencyCode.PHP, "1", "29900");
			handles = new ArrayList<String>();
			handles.add("SB581");
			bizConfig = this.getSimpleBcWorkflow("AlwaysOn299", "Always On 299 ", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("AlwaysOn499", "5012", 1, "49900", CurrencyCode.PHP, "1", "49900");
			handles = new ArrayList<String>();
			handles.add("SB525");
			bizConfig = this.getSimpleBcWorkflow("AlwaysOn499", "Always On 499 ", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("AlwaysOn995", "5013", 1, "99500", CurrencyCode.PHP, "1", "99500");
			handles = new ArrayList<String>();
			handles.add("SB404");
			bizConfig = this.getSimpleBcWorkflow("AlwaysOn995", "Always On 995 ", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("Facebook10Panther", "5014", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("GW698");
			bizConfig = this.getSimpleBcWorkflow("Facebook10Panther", "Facebook 10 Panther", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("Yahoo15Panther", "5015", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("GW719");
			bizConfig = this.getSimpleBcWorkflow("Yahoo15Panther", "Yahoo 15 Panther", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BlackberryDaily50", "5016", 1, "5000", CurrencyCode.PHP, "1", "5000");
			handles = new ArrayList<String>();
			handles.add("SB65");
			bizConfig = this.getSimpleBcWorkflow("BlackberryDaily50", "Blackberry Daily 50", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BlackberryWeekly300", "5017", 1, "30000", CurrencyCode.PHP, "1", "30000");
			handles = new ArrayList<String>();
			handles.add("SB66");
			bizConfig = this.getSimpleBcWorkflow("BlackberryWeekly300", "Blackberry Weekly 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BlackberryMonthly599", "5018", 1, "59900", CurrencyCode.PHP, "1", "59900");
			handles = new ArrayList<String>();
			handles.add("SB108");
			bizConfig = this.getSimpleBcWorkflow("BlackberryMonthly599", "Blackberry Monthly 599", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BlackberryLiteEmailDaily35", "5019", 1, "3500", CurrencyCode.PHP, "1", "3500");
			handles = new ArrayList<String>();
			handles.add("SB259");
			bizConfig = this.getSimpleBcWorkflow("BlackberryLiteEmailDaily35", "Blackberry Lite Email Daily 35", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BlackberryLiteEmailMonthly299", "5020", 1, "29900", CurrencyCode.PHP, "1", "29900");
			handles = new ArrayList<String>();
			handles.add("SB258");
			bizConfig = this.getSimpleBcWorkflow("BlackberryLiteEmailMonthly299", "Blackberry Lite Email Monthly 299", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BlackberrySocialDaily35", "5021", 1, "3500", CurrencyCode.PHP, "1", "3500");
			handles = new ArrayList<String>();
			handles.add("SB257");
			bizConfig = this.getSimpleBcWorkflow("BlackberrySocialDaily35", "Blackberry Social Daily 35", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BlackberrySocialMonthly299", "5022", 1, "29900", CurrencyCode.PHP, "1", "29900");
			handles = new ArrayList<String>();
			handles.add("SB256");
			bizConfig = this.getSimpleBcWorkflow("BlackberrySocialMonthly299", "Blackberry Social Monthly 299", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BlackberryMessengerMonthly99", "5024", 1, "9900", CurrencyCode.PHP, "1", "9900");
			handles = new ArrayList<String>();
			handles.add("SB560");
			bizConfig = this.getSimpleBcWorkflow("BlackberryMessengerMonthly99", "Blackberry Messenger Monthly 99", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BlackberryMonthly599Microwarehouse", "5025", 1, "59900", CurrencyCode.PHP, "1", "59900");
			handles = new ArrayList<String>();
			handles.add("BBP1");
			handles.add("BB1200F");
			bizConfig = this.getSimpleBcWorkflow("BlackberryMonthly599Microwarehouse", "Blackberry Monthly 599 Microwarehouse", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BlackberryLiteEmailMonthly299Microwarehouse", "5026", 1, "29900", CurrencyCode.PHP, "1", "29900");
			handles = new ArrayList<String>();
			handles.add("BBP3");
			handles.add("BB650L");
			bizConfig = this.getSimpleBcWorkflow("BlackberryLiteEmailMonthly299Microwarehouse", "Blackberry Lite Email Monthly 299 Microwarehouse", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BlackberrySocialMonthly299Microwarehouse", "5027", 1, "29900", CurrencyCode.PHP, "1", "29900");
			handles = new ArrayList<String>();
			handles.add("BBP2");
			handles.add("BBS650S");
			bizConfig = this.getSimpleBcWorkflow("BlackberrySocialMonthly299Microwarehouse", "Blackberry Social Monthly 299 Microwarehouse", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("SnapperDaily10", "5028", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("SB457");
			handles.add("SB460");
			bizConfig = this.getSimpleBcWorkflow("SnapperDaily10", "Snapper Daily 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("P1TNTMicrosachetFB", "5029", 1, "100", CurrencyCode.PHP, "1", "100");
			handles = new ArrayList<String>();
			handles.add("SB604");
			bizConfig = this.getSimpleBcWorkflow("P1TNTMicrosachetFB", "P1 TNT Microsachet FB", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("P5TNTMicrosachetFB", "5030", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("SB605");
			bizConfig = this.getSimpleBcWorkflow("P5TNTMicrosachetFB", "P5 TNT Microsachet FB", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("OperaSurf15", "5031", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("SB597");
			bizConfig = this.getSimpleBcWorkflow("OperaSurf15", "OperaSurf 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("OperaSurf60", "5032", 1, "6000", CurrencyCode.PHP, "1", "6000");
			handles = new ArrayList<String>();
			handles.add("SB598");
			bizConfig = this.getSimpleBcWorkflow("OperaSurf60", "OperaSurf 60", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("EmailPackage5", "5036", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("ZBU17");
			bizConfig = this.getSimpleBcWorkflow("EmailPackage5", "Email Package 5", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("EmailPackage10", "5037", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("ZBU18");
			bizConfig = this.getSimpleBcWorkflow("EmailPackage10", "Email Package 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("ChatPackage5", "5038", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("ZBU19");
			bizConfig = this.getSimpleBcWorkflow("ChatPackage5", "Chat Package 5", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("ChatPackage10", "5039", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("ZBU20");
			bizConfig = this.getSimpleBcWorkflow("ChatPackage10", "Chat Package 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("PhotoPackage10", "5040", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("ZBU21");
			bizConfig = this.getSimpleBcWorkflow("PhotoPackage10", "Photo Package 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("PhotoPackage20", "5041", 1, "2000", CurrencyCode.PHP, "1", "2000");
			handles = new ArrayList<String>();
			handles.add("ZBU22");
			bizConfig = this.getSimpleBcWorkflow("PhotoPackage20", "Photo Package 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("SocialPackage", "5042", 1, "0", CurrencyCode.PHP, "1", "0");
			handles = new ArrayList<String>();
			handles.add("YBU23");
			bizConfig = this.getSimpleBcWorkflow("SocialPackage", "Social Package", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliPackage15", "5043", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("ZBU24");
			bizConfig = this.getSimpleBcWorkflow("UnliPackage15", "Unli Package 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliPackage30", "5044", 1, "3000", CurrencyCode.PHP, "1", "3000");
			handles = new ArrayList<String>();
			handles.add("ZBU25");
			bizConfig = this.getSimpleBcWorkflow("UnliPackage30", "Unli Package 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("SpeedBoost", "5045", 1, "500", CurrencyCode.PHP, "1", "500");
			handles = new ArrayList<String>();
			handles.add("ZBU26");
			bizConfig = this.getSimpleBcWorkflow("SpeedBoost", "Speed Boost", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliOperaMini7Days", "5033", 1, "8000", CurrencyCode.PHP, "1", "8000");
			handles = new ArrayList<String>();
			handles.add("DA003");
			bizConfig = this.getSimpleBcWorkflow("UnliOperaMini7Days", "Unli Opera Mini 7 Days", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliOperaMini15Days", "5034", 1, "16000", CurrencyCode.PHP, "1", "16000");
			handles = new ArrayList<String>();
			handles.add("DA004");
			bizConfig = this.getSimpleBcWorkflow("UnliOperaMini15Days", "Unli Opera Mini 15 Days", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliOperaMini30Days", "5035", 1, "29900", CurrencyCode.PHP, "1", "29900");
			handles = new ArrayList<String>();
			handles.add("DA005");
			bizConfig = this.getSimpleBcWorkflow("UnliOperaMini30Days", "Unli Opera Mini 30 Days", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliNokiaXpress1Day", "5049", 1, "1500", CurrencyCode.PHP, "1", "1500");
			handles = new ArrayList<String>();
			handles.add("ZBU07");
			bizConfig = this.getSimpleBcWorkflow("UnliNokiaXpress1Day", "Unli Nokia Xpress 1 Day", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliNokiaXpress7Days", "5050", 1, "8000", CurrencyCode.PHP, "1", "8000");
			handles = new ArrayList<String>();
			handles.add("ZBU08");
			bizConfig = this.getSimpleBcWorkflow("UnliNokiaXpress7Days", "Unli Nokia Xpress 7 Days", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliNokiaXpress15Days", "5051", 1, "16000", CurrencyCode.PHP, "1", "16000");
			handles = new ArrayList<String>();
			handles.add("ZBU09");
			bizConfig = this.getSimpleBcWorkflow("UnliNokiaXpress15Days", "Unli Nokia Xpress 15 Days", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("UnliNokiaXpress30Days", "5052", 1, "29900", CurrencyCode.PHP, "1", "29900");
			handles = new ArrayList<String>();
			handles.add("ZBU10");
			bizConfig = this.getSimpleBcWorkflow("UnliNokiaXpress30Days", "Unli Nokia Xpress 30 Days", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("Unlisurf299", "5002", 1, "29900", CurrencyCode.PHP, "1", "29900");
			handles = new ArrayList<String>();
			handles.add("ZBU12");
			bizConfig = this.getSimpleBcWorkflow("Unlisurf299", "Unlisurf 299", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("Unlisurf999", "5003", 1, "99900", CurrencyCode.PHP, "1", "99900");
			handles = new ArrayList<String>();
			handles.add("ZBU13");
			bizConfig = this.getSimpleBcWorkflow("Unlisurf999", "Unlisurf 999", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("BBMP10", "5023", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("ZBU04");
			bizConfig = this.getSimpleBcWorkflow("BBMP10", "BBM P10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("MessagingUnliCHATALL299", "5056", 1, "29900", CurrencyCode.PHP, "1", "29900");
			handles = new ArrayList<String>();
			handles.add("ZBU11");
			bizConfig = this.getSimpleBcWorkflow("MessagingUnliCHATALL299", "Messaging Unli CHATALL299", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Promo Refill...");
			resource = this.createRefill("LoyalistaGaanText10BatchCrediting", "B001", 1, "1000", CurrencyCode.PHP, "1", "1000");
			handles = new ArrayList<String>();
			handles.add("GTX10");
			bizConfig = this.getSimpleBcWorkflow("LoyalistaGaanText10BatchCrediting", "Loyalista Gaan Text 10 Batch Crediting", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			//========================================== End of the Commercial Offers ============		

			//========================================== Start of the Offer Reversals ============		
			
/*			Commented the first sample Reversal BC, as this is covered in the new BC. This is left as a reference in case any issues are found in the reversal offers later
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
			serviceRegistry.createResource(resource);*/
	
			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			List<DedicatedAccountReversal> daReversals = new ArrayList<DedicatedAccountReversal>();

			DedicatedAccountReversal daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(241);
			daReversal.setAmountToReverse(10000);
			daReversals.add(daReversal);

			List<TimerOfferReversal> toReversals = new ArrayList<TimerOfferReversal>();

			TimerOfferReversal toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1241);
			toReversal.setDedicatedAccountInformationID(241);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevAlkansyaLoad100", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevWL3");
			
			bizConfig = this.getSimpleBcWorkflow("RevAlkansyaLoad100", "Alkansya Load Wallet Load 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);



			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(241);
			daReversal.setAmountToReverse(1500);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1241);
			toReversal.setDedicatedAccountInformationID(241);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevAlkansyaLoad15", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK01");
			
			bizConfig = this.getSimpleBcWorkflow("RevAlkansyaLoad15", "Alkansya Load Wallet Load 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(241);
			daReversal.setAmountToReverse(15000);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1241);
			toReversal.setDedicatedAccountInformationID(241);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevAlkansyaLoad150", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevWL4");
			
			bizConfig = this.getSimpleBcWorkflow("RevAlkansyaLoad150", "Alkansya Load Wallet Load 150", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(241);
			daReversal.setAmountToReverse(3000);
			daReversals.add(daReversal);

					
			toReversals = new ArrayList<TimerOfferReversal>();
			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1241);
			toReversal.setDedicatedAccountInformationID(241);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevAlkansyaLoad30", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevWL1");
			
			bizConfig = this.getSimpleBcWorkflow("RevAlkansyaLoad30", "Alkansya Load Wallet Load 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(241);
			daReversal.setAmountToReverse(30000);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();
			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1241);
			toReversal.setDedicatedAccountInformationID(241);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevAlkansyaLoad300", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevWL5");
			
			bizConfig = this.getSimpleBcWorkflow("RevAlkansyaLoad300", "Alkansya Load Wallet Load 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(241);
			daReversal.setAmountToReverse(5000);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1241);
			toReversal.setDedicatedAccountInformationID(241);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevAlkansyaLoad50", daReversals, toReversals);
			handles = new ArrayList<String>();
                        handles.add("RevWL2");
			
			bizConfig = this.getSimpleBcWorkflow("RevAlkansyaLoad50", "Alkansya Load Wallet Load 50", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(241);
			daReversal.setAmountToReverse(50000);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1241);
			toReversal.setDedicatedAccountInformationID(241);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevAlkansyaLoad500", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevWL6");
			
			bizConfig = this.getSimpleBcWorkflow("RevAlkansyaLoad500", "Alkansya Load Wallet Load 500", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Ask For Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(500);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			/*
		
			resource = this.createSmartReversalRefill("RevAskforLoadP5", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev5");
			
			bizConfig = this.getSimpleBcWorkflow("RevAskforLoadP5", "Ask-for-Load P5", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - CTC Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(1000);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevTicketLoad10", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev10");
			
			bizConfig = this.getSimpleBcWorkflow("RevTicketLoad10", "TicketLoad 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - CTC Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(1500);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevTicketLoad15", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev15");
			
			bizConfig = this.getSimpleBcWorkflow("RevTicketLoad15", "TicketLoad 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - CTC Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(361);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(361);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevTicketLoad30", daReversals, toReversals);
			handles = new ArrayList<String>();
                        handles.add("Rev30");
			
			bizConfig = this.getSimpleBcWorkflow("RevTicketLoad30", "TicketLoad 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - CTC Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(10000);
			daReversal.setHoursToReverse(720);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevCTCLoad100", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev100");
			
			bizConfig = this.getSimpleBcWorkflow("RevCTCLoad100", "CTC Load 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - CTC Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(100000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(25000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevCTCLoad1000", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev1000");
			
			bizConfig = this.getSimpleBcWorkflow("RevCTCLoad1000", "CTC Load 1000", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - CTC Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(30000);
			daReversal.setHoursToReverse(1800);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(3300);
			daReversal.setHoursToReverse(1800);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1800);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(1800);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevCTCLoad300", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev300");
			
			bizConfig = this.getSimpleBcWorkflow("RevCTCLoad300", "CTC Load 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - CTC Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(50000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(8300);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevCTCLoad500", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev500");
			
			bizConfig = this.getSimpleBcWorkflow("RevCTCLoad500", "CTC Load 500", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Corporate Quick Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(10000);
			daReversal.setHoursToReverse(720);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(1000);
			daReversal.setHoursToReverse(720);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevCorporateQuickLoad100", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevB1");
			
			bizConfig = this.getSimpleBcWorkflow("RevCorporateQuickLoad100", "Corporate Quick Load 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Corporate Quick Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(100000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(25000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevCorporateQuickLoad1000", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevX8");
			
			bizConfig = this.getSimpleBcWorkflow("RevCorporateQuickLoad1000", "Corporate Quick Load 1000", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Corporate Quick Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(20000);
			daReversal.setHoursToReverse(1440);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(1500);
			daReversal.setHoursToReverse(1440);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1440);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(1440);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevCorporateQuickLoad200", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevXA");
			
			bizConfig = this.getSimpleBcWorkflow("RevCorporateQuickLoad200", "Corporate Quick Load 200", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Corporate Quick Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(30000);
			daReversal.setHoursToReverse(1800);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(3300);
			daReversal.setHoursToReverse(1800);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1800);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(1800);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevCorporateQuickLoad300", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevX6");
			
			bizConfig = this.getSimpleBcWorkflow("RevCorporateQuickLoad300", "Corporate Quick Load 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Corporate Quick Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(50000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(8300);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevCorporateQuickLoad500", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevX7");
			
			bizConfig = this.getSimpleBcWorkflow("RevCorporateQuickLoad500", "Corporate Quick Load 500", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			resource = this.createSmartReversalRefill("RevEconomy30", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevA");
            handles.add("RevT1");
            handles.add("RevSM2");
			
			bizConfig = this.getSimpleBcWorkflow("RevEconomy30", "Economy 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(3200);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

				
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevEconomy30SM", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevSM12");
			
			bizConfig = this.getSimpleBcWorkflow("RevEconomy30SM", "Economy 30 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

*/
			System.out.println("Offer, Resource & Profile - Emergency Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(100);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(300);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevEmergencyLoadSMS", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevPBB");
			
			bizConfig = this.getSimpleBcWorkflow("RevEmergencyLoadSMS", "Emergency Load SMS", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Emergency Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(100);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(400);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevEmergencyLoadSMSAllNetSMS", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevPBB3");
			
			bizConfig = this.getSimpleBcWorkflow("RevEmergencyLoadSMSAllNetSMS", "Emergency Load SMS - All Net SMS", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Emergency Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(100);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(3);
			daReversal.setAmountToReverse(6000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

				
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1003);
			toReversal.setDedicatedAccountInformationID(3);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevEmergencyLoadVoice", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevCBBV1");
			
			bizConfig = this.getSimpleBcWorkflow("RevEmergencyLoadVoice", "Emergency Load Voice", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(11500);
			daReversal.setHoursToReverse(1080);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1080);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevExtra115", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevC");
            handles.add("RevT3");
			
			bizConfig = this.getSimpleBcWorkflow("RevExtra115", "Extra 115", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Pasaload Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(1000);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasaloadP10", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev10");
            handles.add("RevO3");
            handles.add("RevW3");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasaloadP10", "Pasaload P10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Pasaload Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(10000);
			daReversal.setHoursToReverse(720);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			resource = this.createSmartReversalRefill("RevPasaloadP100", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev1H");
            handles.add("Rev1O");
            handles.add("RevW8");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasaloadP100", "Pasaload P100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Pasaload Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(1500);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasaloadP15", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev15");
            handles.add("RevO4");
            handles.add("RevW4");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasaloadP15", "Pasaload P15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Pasaload Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(200);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasaloadP2", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev2");
            handles.add("RevO1");
            handles.add("RevW1");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasaloadP2", "Pasaload P2", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Pasaload Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(2000);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasaloadP20", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev20");
            handles.add("RevO5");
            handles.add("RevW5");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasaloadP20", "Pasaload P20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Pasaload Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(20000);
			daReversal.setHoursToReverse(1440);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1440);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasaloadP200", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev2H");
            handles.add("Rev2O");
            handles.add("RevW9");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasaloadP200", "Pasaload P200", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Pasaload Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasaloadP30", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev30");
            handles.add("RevO7");
            handles.add("RevW6");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasaloadP30", "Pasaload P30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Pasaload Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(100);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(10000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasaloadP35", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev35T");
            handles.add("RevO8");
            handles.add("RevY2");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasaloadP35", "Pasaload P35", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Pasaload Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(500);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasaloadP5", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev5");
            handles.add("RevO2");
            handles.add("RevW2");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasaloadP5", "Pasaload P5", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Pasaload Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(6000);
			daReversal.setHoursToReverse(720);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasaloadP60", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("Rev60");
            handles.add("RevO9");
            handles.add("RevW7");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasaloadP60", "Pasaload P60", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(1000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliAlkansyaInstantTextP1", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK07");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliAlkansyaInstantTextP1", "PasariliAlkansya InstantText P1", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(2000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliAlkansyaInstantTextP2", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK08");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliAlkansyaInstantTextP2", "PasariliAlkansya InstantText P2", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliAlkansyaInstantTextP3", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK09");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliAlkansyaInstantTextP3", "PasariliAlkansya InstantText P3", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(4000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliAlkansyaInstantTextP4", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK10");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliAlkansyaInstantTextP4", "PasariliAlkansya InstantText P4", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(5000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliAlkansyaInstantTextP5", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK11");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliAlkansyaInstantTextP5", "PasariliAlkansya InstantText P5", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(6000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliAlkansyaInstantTextP6", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK12");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliAlkansyaInstantTextP6", "PasariliAlkansya InstantText P6", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(7000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliAlkansyaInstantTextP7", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK13");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliAlkansyaInstantTextP7", "PasariliAlkansya InstantText P7", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(8000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliAlkansyaInstantTextP8", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK14");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliAlkansyaInstantTextP8", "PasariliAlkansya InstantText P8", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(9000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliAlkansyaInstantTextP9", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK15");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliAlkansyaInstantTextP9", "PasariliAlkansya InstantText P9", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(100);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP1", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK02");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP1", "Pasarili P1", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(150);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP1.50", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK03");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP1.50", "Pasarili P1.50", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(1000);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP10", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevGL10");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP10", "Pasarili P10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(10000);
			daReversal.setHoursToReverse(720);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP100", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevT20");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP100", "Pasarili P100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(11500);
			daReversal.setHoursToReverse(1080);
			daReversals.add(daReversal);

						
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1080);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP115", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevT3");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP115", "Pasarili P115", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(1500);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP15", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevT17");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP15", "Pasarili P15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(200);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP2", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK04");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP2", "Pasarili P2", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(250);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			resource = this.createSmartReversalRefill("RevPasariliP2.50", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK05");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP2.50", "Pasarili P2.50", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(20000);
			daReversal.setHoursToReverse(1440);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(1440);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1440);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(1440);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP200", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevT7");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP200", "Pasarili P200", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(300);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP3", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK06");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP3", "Pasarili P3", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP30", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevT1");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP30", "Pasarili P30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(30000);
			daReversal.setHoursToReverse(1800);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(3300);
			daReversal.setHoursToReverse(1800);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1800);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(1800);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP300", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevT4");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP300", "Pasarili P300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(50000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(8300);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP500", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevT5");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP500", "Pasarili P500", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(6000);
			daReversal.setHoursToReverse(720);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliP60", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevT2");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliP60", "Pasarili P60", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(101);
			daReversal.setAmountToReverse(2000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1101);
			toReversal.setDedicatedAccountInformationID(101);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliPatok10", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK18");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliPatok10", "Pasarili Patok10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliUnliTxt10", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK17");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliUnliTxt10", "Pasarili UnliTxt10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(1);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliUnliTxt5", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevALK16");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliUnliTxt5", "Pasarili UnliTxt5", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(1500);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliUpsize20", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevT13");
			
			bizConfig = this.getSimpleBcWorkflow("RefPasariliUpsize20", "Pasarili Upsize20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Alkansya Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPasariliUpsize35", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevT11");
			
			bizConfig = this.getSimpleBcWorkflow("RevPasariliUpsize35", "Pasarili Upsize35", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Presyong Pinoy Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(11500);
			daReversal.setHoursToReverse(1080);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1080);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPresyongPinoyLoad115", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevBR002");
			
			bizConfig = this.getSimpleBcWorkflow("RevPresyongPinoyLoad115", "Presyong Pinoy Load 115", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Presyong Pinoy Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPresyongPinoyLoad30", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevBR007");
			
			bizConfig = this.getSimpleBcWorkflow("RevPresyongPinoyLoad30", "Presyong Pinoy Load 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Presyong Pinoy Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(6000);
			daReversal.setHoursToReverse(720);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPresyongPinoyLoad60", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevBR008");
			
			bizConfig = this.getSimpleBcWorkflow("RevPresyongPinoyLoad60", "Presyong Pinoy Load 60", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Presyong Pinoy Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(10000);
			daReversal.setHoursToReverse(720);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPresyongPinoyLoad100", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevBR001");
			
			bizConfig = this.getSimpleBcWorkflow("RevPresyongPinoyLoad100", "Presyong Pinoy Load 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Presyong Pinoy Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(100000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(25000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPresyongPinoyLoad1000", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevBR006");
			
			bizConfig = this.getSimpleBcWorkflow("RevPresyongPinoyLoad1000", "Presyong Pinoy Load 1000", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Presyong Pinoy Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(20000);
			daReversal.setHoursToReverse(1440);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(1440);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1440);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(1440);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPresyongPinoyLoad200", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevBR003");
			
			bizConfig = this.getSimpleBcWorkflow("RevPresyongPinoyLoad200", "Presyong Pinoy Load 200", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Presyong Pinoy Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(30000);
			daReversal.setHoursToReverse(1800);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(3300);
			daReversal.setHoursToReverse(1800);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1800);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(1800);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPresyongPinoyLoad300", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevBR004");
			
			bizConfig = this.getSimpleBcWorkflow("RevPresyongPinoyLoad300", "Presyong Pinoy Load 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Presyong Pinoy Load Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(50000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(8300);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPresyongPinoyLoad500", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevBR005");
			
			bizConfig = this.getSimpleBcWorkflow("RevPresyongPinoyLoad500", "Presyong Pinoy Load 500", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(6000);
			daReversal.setHoursToReverse(720);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevRegular60", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevB");
            handles.add("RevT2");
			
			bizConfig = this.getSimpleBcWorkflow("RevRegular60", "Regular 60", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(1000);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevRegularLoadP10", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevTPW04");
            handles.add("RevPlan10");
			
			bizConfig = this.getSimpleBcWorkflow("RevRegularLoadP10", "Regular Load P10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(10000);
			daReversal.setHoursToReverse(720);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad100", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevX2");
            handles.add("RevT20");
            handles.add("RevSM5");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad100", "SMARTLoad 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(10500);
			daReversal.setHoursToReverse(720);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad100SM", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevSM14");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad100SM", "SMARTLoad 100 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(100000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(25000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad1000", daReversals, toReversals);
			handles = new ArrayList<String>();
                        handles.add("RevF");
                        handles.add("RevT6");
                        handles.add("RevSM9");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad1000", "SMARTLoad 1000", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(105000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(25000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad1000SM", daReversals, toReversals);
			handles = new ArrayList<String>();
                        handles.add("RevSM18");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad1000SM", "SMARTLoad 1000 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(1500);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad15", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevU");
            handles.add("RevT17");
            handles.add("RevSM1");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad15", "SMARTLoad 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(1600);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad15SM", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevSM11");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad15SM", "SMARTLoad 15 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(2000);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad20", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevBD5");
            handles.add("RevT10");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad20", "SMARTLoad 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(20000);
			daReversal.setHoursToReverse(1440);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(1440);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1440);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(1440);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad200", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevG");
            handles.add("RevT7");
            handles.add("RevSM6");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad200", "SMARTLoad 200", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(21000);
			daReversal.setHoursToReverse(1440);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(1440);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1440);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(1440);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad200SM", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevSM15");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad200SM", "SMARTLoad 200 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(25000);
			daReversal.setHoursToReverse(1440);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1440);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad250", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevRA001");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad250", "SMARTLoad 250", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(30000);
			daReversal.setHoursToReverse(1800);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(3300);
			daReversal.setHoursToReverse(1800);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1800);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(1800);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad300", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevD");
            handles.add("RevT4");
            handles.add("RevSM7");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad300", "SMARTLoad 300", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(31500);
			daReversal.setHoursToReverse(1800);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(3300);
			daReversal.setHoursToReverse(1800);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(1800);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(1800);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad300SM", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevSM16");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad300SM", "SMARTLoad 300 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(5000);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad50", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevX1");
            handles.add("RevT19");
            handles.add("RevTKT01");
            handles.add("RevSM3");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad50", "SMARTLoad 50", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(5300);
			daReversal.setHoursToReverse(360);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(360);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad50SM", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevSM13");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad50SM", "SMARTLoad 50 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(50000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(8300);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad500", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevE");
            handles.add("RevT5");
            handles.add("RevSM8");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad500", "SMARTLoad 500", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Regular Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(52500);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(8300);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevSMARTLoad500SM", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevSM17");
			
			bizConfig = this.getSimpleBcWorkflow("RevSMARTLoad500SM", "SMARTLoad 500 Smart Money", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - TakaTak Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(1200);
			daReversal.setMinutesToReverse(30);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setMinutessToReverse(30);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevTakatak12", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevTK3");
			
			bizConfig = this.getSimpleBcWorkflow("RevTakatak12", "Takatak 12", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - TakaTak Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(300);
			daReversal.setMinutesToReverse(10);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setMinutessToReverse(10);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevTakatak3", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevTK1");
			
			bizConfig = this.getSimpleBcWorkflow("RevTakatak3", "Takatak 3", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - TakaTak Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(600);
			daReversal.setMinutesToReverse(20);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setMinutessToReverse(20);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevTakatak6", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevTK2");
			
			bizConfig = this.getSimpleBcWorkflow("RevTakatak6", "Takatak 6", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - VAS Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(500000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevEloadPromo5000", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevELP01");
			
			bizConfig = this.getSimpleBcWorkflow("RevEloadPromo5000", "Eload Promo 5000", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - VAS Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(1000000);
			daReversal.setHoursToReverse(2880);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(2880);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevEloadPromo10000", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevELP02");
			
			bizConfig = this.getSimpleBcWorkflow("RevEloadPromo10000", "Eload Promo 10000", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - VAS Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(30000);
			daReversal.setHoursToReverse(1080);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(1080);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevKPR01", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevKPR01");
			
			bizConfig = this.getSimpleBcWorkflow("RevKPR01", "KaPartner Rewards 300 on-net SMS", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - VAS Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(2000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevDBP01", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevDBP01");
			
			bizConfig = this.getSimpleBcWorkflow("RevDBP01", "DB Buliding and Profiling Program 20 Onnet SMS", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - VAS Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(2500);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevUNI01", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevUNI01");
			
			bizConfig = this.getSimpleBcWorkflow("RevUNI01", "Unilever Reward 25 On Net SMS", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - VAS Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(15000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevKPR11", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevKPR11");
			
			bizConfig = this.getSimpleBcWorkflow("RevKPR11", "KaPartner Rewards Gaan All-In-One 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - VAS Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(10000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(1000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevKPR12", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevKPR12");
			
			bizConfig = this.getSimpleBcWorkflow("RevKPR12", "KaPartner Rewards Gaan Text 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - VAS Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(1);
			daReversal.setAmountToReverse(100);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(400);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1001);
			toReversal.setDedicatedAccountInformationID(1);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevIOSMSJapan", daReversals, toReversals);
			handles = new ArrayList<String>();
                        handles.add("RevI");
			
			bizConfig = this.getSimpleBcWorkflow("RevIOSMSJapan", "IOSMS Japan", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2103);
			toReversal.setHoursToReverse(48);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevUnliTxt2All15Regional", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevRB328");
            handles.add("RevPWIL3");
			
			bizConfig = this.getSimpleBcWorkflow("RevUnliTxt2All15Regional", "UnliTxt2All 15 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(3);
			daReversal.setAmountToReverse(90000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1003);
			toReversal.setDedicatedAccountInformationID(3);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevGaanUnliTxtPlus15", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevRB410");
            handles.add("RevGTX15");
			
			bizConfig = this.getSimpleBcWorkflow("RevGaanUnliTxtPlus15", "GaanUnliTxt Plus 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(3);
			daReversal.setAmountToReverse(120000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1003);
			toReversal.setDedicatedAccountInformationID(3);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevGaanUnliTxtPlus20", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevSB46");
            handles.add("RevGT20T");
			
			bizConfig = this.getSimpleBcWorkflow("RevGaanUnliTxtPlus20", "GaanUnliTxt Plus 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(3);
			daReversal.setAmountToReverse(180000);
			daReversal.setHoursToReverse(48);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(48);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1003);
			toReversal.setDedicatedAccountInformationID(3);
			toReversal.setHoursToReverse(48);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevGaanUnliTxtPlus30", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevSB47");
            handles.add("RevGT30T");
			
			bizConfig = this.getSimpleBcWorkflow("RevGaanUnliTxtPlus30", "GaanUnliTxt Plus 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2001);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevUnliTalkPlus20", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevUCT17");
            handles.add("RevUP20");
			
			bizConfig = this.getSimpleBcWorkflow("RevUnliTalkPlus20", "UnliTalk Plus 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2001);
			toReversal.setHoursToReverse(120);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(120);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevUnliTalkPlus100", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevUCT4");
            handles.add("RevT22");
			
			bizConfig = this.getSimpleBcWorkflow("RevUnliTalkPlus100", "UnliTalk Plus 100", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(3);
			daReversal.setAmountToReverse(90000);
			daReversal.setHoursToReverse(72);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1003);
			toReversal.setDedicatedAccountInformationID(3);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevUnliTextExtra30", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevRB510");
            handles.add("RevPJST1");
			
			bizConfig = this.getSimpleBcWorkflow("RevUnliTextExtra30", "UnliTextExtra 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(52);
			daReversal.setAmountToReverse(10000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(1000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1052);
			toReversal.setDedicatedAccountInformationID(52);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevGaanTxt10", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevRB60");
            handles.add("RevQ");
			
			bizConfig = this.getSimpleBcWorkflow("RevGaanTxt10", "GaanTxt 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(20000);
			daReversal.setHoursToReverse(48);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(48);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevGaanTxt20", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevRB323");
            handles.add("RevX5");
			
			bizConfig = this.getSimpleBcWorkflow("RevGaanTxt20", "GaanTxt 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevUnliTxt10", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevUF57");
			
			bizConfig = this.getSimpleBcWorkflow("RevUnliTxt10", "UnliTxt 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(720);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevUnliTxt150", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevUF137");
            handles.add("RevPWIL1");
			
			bizConfig = this.getSimpleBcWorkflow("RevUnliTxt150", "UnliTxt 150", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(1);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevUnliTxt51hr", daReversals, toReversals);
			handles = new ArrayList<String>();
                        handles.add("RevUF58");
			
			bizConfig = this.getSimpleBcWorkflow("RevUnliTxt51hr", "UnliTxt5 1 hr", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2103);
			toReversal.setHoursToReverse(48);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevUnliTxt2All20", daReversals, toReversals);
			handles = new ArrayList<String>();
                        handles.add("RevRB511");
                        handles.add("RevPJST2");
			
			bizConfig = this.getSimpleBcWorkflow("RevUnliTxt2All20", "UnliTxt2All 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(48);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevUnliTxt15Regional", daReversals, toReversals);
			handles = new ArrayList<String>();
                        handles.add("RevUF166");
                        handles.add("RevUPK05");
			
			bizConfig = this.getSimpleBcWorkflow("RevUnliTxt15Regional", "UnliTxt 15 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(12);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevUnliTxt5for12hrsRegional", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevUF165");
            handles.add("RevUPK04");
			
			bizConfig = this.getSimpleBcWorkflow("RevUnliTxt5for12hrsRegional", "UnliTxt 5 for 12 hrs Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(2101);
			toReversal.setHoursToReverse(72);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevUnliTxt20Regional", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevUF235");
            handles.add("RevUNL20");
			
			bizConfig = this.getSimpleBcWorkflow("RevUnliTxt20Regional", "UnliTxt 20 Regional", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(54);
			daReversal.setAmountToReverse(7500);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1054);
			toReversal.setDedicatedAccountInformationID(54);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevAllText10", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevJ");
			
			bizConfig = this.getSimpleBcWorkflow("RevAllText10", "All Text 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(101);
			daReversal.setAmountToReverse(2000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1101);
			toReversal.setDedicatedAccountInformationID(101);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPaTokOTex10", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevRB95");
            handles.add("RevFMX1");
			
			bizConfig = this.getSimpleBcWorkflow("RevPaTokOTex10", "PaTok-O-Tex 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Promo Refill Reversal...");
			daReversals = new ArrayList<DedicatedAccountReversal>();

			daReversal = new DedicatedAccountReversal();
			daReversal.setDedicatedAccountInformationID(101);
			daReversal.setAmountToReverse(3000);
			daReversal.setHoursToReverse(24);
			daReversals.add(daReversal);

			
			
			toReversals = new ArrayList<TimerOfferReversal>();

			toReversal = new TimerOfferReversal();
			toReversal.setOfferID(1101);
			toReversal.setDedicatedAccountInformationID(101);
			toReversal.setHoursToReverse(24);
			toReversals.add(toReversal);

			
			resource = this.createSmartReversalRefill("RevPaTokOTex15", daReversals, toReversals);
			handles = new ArrayList<String>();
            handles.add("RevRB460");
            handles.add("RevPTT15");
			
			bizConfig = this.getSimpleBcWorkflow("RevPaTokOTex15", "PaTok-O-Tex 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			//========================================== End of the Offer Reversals ============		
			
			//===========>>>> Flexi Refilll... 
			resource = this.createSmartFlexiRefillProfile("FlexiRefill");
			handles = new ArrayList<String>();
			handles.add("FlexiRefill");
			
			bizConfig = this.getSimpleBcWorkflow("FlexiRefill", "Flexi Refill", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
	
			//===================================== Balance Adjustment Starts ===========================
			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("OnPeakAccountID", 1, 1);
			handles = new ArrayList<String>();
			handles.add("OnPeakAccountID");
			bizConfig = this.getSimpleBcWorkflow("OnPeakAccountID", "On Peak Account", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
			
			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("RwdMocTri", 2, 1);
			handles = new ArrayList<String>();
			handles.add("RwdMocTri");
			bizConfig = this.getSimpleBcWorkflow("RwdMocTri", "RwdMocTri", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketMocOn", 3, 1);
			handles = new ArrayList<String>();
			handles.add("BucketMocOn");
			bizConfig = this.getSimpleBcWorkflow("BucketMocOn", "BucketMocOn", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketMocSun", 4, 1);
			handles = new ArrayList<String>();
			handles.add("BucketMocSun");
			bizConfig = this.getSimpleBcWorkflow("BucketMocSun", "BucketMocSun", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketMocTri", 5, 1);
			handles = new ArrayList<String>();
			handles.add("BucketMocTri");
			bizConfig = this.getSimpleBcWorkflow("BucketMocTri", "BucketMocTri", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketMocGbl", 6, 1);
			handles = new ArrayList<String>();
			handles.add("BucketMocGbl");
			bizConfig = this.getSimpleBcWorkflow("BucketMocGbl", "BucketMocGbl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketMocGblInt", 7, 1);
			handles = new ArrayList<String>();
			handles.add("BucketMocGblInt");
			bizConfig = this.getSimpleBcWorkflow("BucketMocGblInt", "BucketMocGblInt", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketMocInt", 8, 1);
			handles = new ArrayList<String>();
			handles.add("BucketMocInt");
			bizConfig = this.getSimpleBcWorkflow("BucketMocInt", "BucketMocInt", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketRoamMoc", 9, 1);
			handles = new ArrayList<String>();
			handles.add("BucketRoamMoc");
			bizConfig = this.getSimpleBcWorkflow("BucketRoamMoc", "BucketRoamMoc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketIntlS3Moc", 10, 1);
			handles = new ArrayList<String>();
			handles.add("BucketIntlS3Moc");
			bizConfig = this.getSimpleBcWorkflow("BucketIntlS3Moc", "BucketIntlS3Moc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("CallAllBucketMoc", 11, 1);
			handles = new ArrayList<String>();
			handles.add("CallAllBucketMoc");
			bizConfig = this.getSimpleBcWorkflow("CallAllBucketMoc", "CallAllBucketMoc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketTokMoc", 12, 1);
			handles = new ArrayList<String>();
			handles.add("BucketTokMoc");
			bizConfig = this.getSimpleBcWorkflow("BucketTokMoc", "BucketTokMoc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketArawMoc", 13, 1);
			handles = new ArrayList<String>();
			handles.add("BucketArawMoc");
			bizConfig = this.getSimpleBcWorkflow("BucketArawMoc", "BucketArawMoc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketTkTpdMoc", 14, 1);
			handles = new ArrayList<String>();
			handles.add("BucketTkTpdMoc");
			bizConfig = this.getSimpleBcWorkflow("BucketTkTpdMoc", "BucketTkTpdMoc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketMocTriCc", 15, 1);
			handles = new ArrayList<String>();
			handles.add("BucketMocTriCc");
			bizConfig = this.getSimpleBcWorkflow("BucketMocTriCc", "BucketMocTriCc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("RwdSmsGbl", 51, 1);
			handles = new ArrayList<String>();
			handles.add("RwdSmsGbl");
			bizConfig = this.getSimpleBcWorkflow("RwdSmsGbl", "RwdSmsGbl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketSmsOn", 52, 1);
			handles = new ArrayList<String>();
			handles.add("BucketSmsOn");
			bizConfig = this.getSimpleBcWorkflow("BucketSmsOn", "BucketSmsOn", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketSmsTri", 53, 1);
			handles = new ArrayList<String>();
			handles.add("BucketSmsTri");
			bizConfig = this.getSimpleBcWorkflow("BucketSmsTri", "BucketSmsTri", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketSmsGbl", 54, 1);
			handles = new ArrayList<String>();
			handles.add("BucketSmsGbl");
			bizConfig = this.getSimpleBcWorkflow("BucketSmsGbl", "BucketSmsGbl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketSmsInt", 55, 1);
			handles = new ArrayList<String>();
			handles.add("BucketSmsInt");
			bizConfig = this.getSimpleBcWorkflow("BucketSmsInt", "BucketSmsInt", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketSmsRoam", 56, 1);
			handles = new ArrayList<String>();
			handles.add("BucketSmsRoam");
			bizConfig = this.getSimpleBcWorkflow("BucketSmsRoam", "BucketSmsRoam", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketIntlS4Sms", 57, 1);
			handles = new ArrayList<String>();
			handles.add("BucketIntlS4Sms");
			bizConfig = this.getSimpleBcWorkflow("BucketIntlS4Sms", "BucketIntlS4Sms", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketTkTpdSms", 58, 1);
			handles = new ArrayList<String>();
			handles.add("BucketTkTpdSms");
			bizConfig = this.getSimpleBcWorkflow("BucketTkTpdSms", "BucketTkTpdSms", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketKamusTxtSms", 59, 1);
			handles = new ArrayList<String>();
			handles.add("BucketKamusTxtSms");
			bizConfig = this.getSimpleBcWorkflow("BucketKamusTxtSms", "BucketKamusTxtSms", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("DailyCapSms", 60, 1);
			handles = new ArrayList<String>();
			handles.add("DailyCapSms");
			bizConfig = this.getSimpleBcWorkflow("DailyCapSms", "DailyCapSms", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("AllTxtBcktSmsGbl", 61, 1);
			handles = new ArrayList<String>();
			handles.add("AllTxtBcktSmsGbl");
			bizConfig = this.getSimpleBcWorkflow("AllTxtBcktSmsGbl", "AllTxtBcktSmsGbl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("ConsWalletAcc", 101, 1);
			handles = new ArrayList<String>();
			handles.add("ConsWalletAcc");
			bizConfig = this.getSimpleBcWorkflow("ConsWalletAcc", "ConsWalletAcc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketIntlS1Cbo", 102, 1);
			handles = new ArrayList<String>();
			handles.add("BucketIntlS1Cbo");
			bizConfig = this.getSimpleBcWorkflow("BucketIntlS1Cbo", "BucketIntlS1Cbo", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketIntlS2Cbo", 103, 1);
			handles = new ArrayList<String>();
			handles.add("BucketIntlS2Cbo");
			bizConfig = this.getSimpleBcWorkflow("BucketIntlS2Cbo", "BucketIntlS2Cbo", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketIntlCombo", 104, 1);
			handles = new ArrayList<String>();
			handles.add("BucketIntlCombo");
			bizConfig = this.getSimpleBcWorkflow("BucketIntlCombo", "BucketIntlCombo", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("FlexiBckAcc", 105, 1);
			handles = new ArrayList<String>();
			handles.add("FlexiBckAcc");
			bizConfig = this.getSimpleBcWorkflow("FlexiBckAcc", "FlexiBckAcc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("DataVolFbc", 141, 6);
			handles = new ArrayList<String>();
			handles.add("DataVolFbc");
			bizConfig = this.getSimpleBcWorkflow("DataVolFbc", "DataVolFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("DataMinFbc", 142, 0);
			handles = new ArrayList<String>();
			handles.add("DataMinFbc");
			bizConfig = this.getSimpleBcWorkflow("DataMinFbc", "DataMinFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("DataArawFbc", 143, 0);
			handles = new ArrayList<String>();
			handles.add("DataArawFbc");
			bizConfig = this.getSimpleBcWorkflow("DataArawFbc", "DataArawFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("RwdVolDataFbc", 144, 6);
			handles = new ArrayList<String>();
			handles.add("RwdVolDataFbc");
			bizConfig = this.getSimpleBcWorkflow("RwdVolDataFbc", "RwdVolDataFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("ComDataFbc", 145, 6);
			handles = new ArrayList<String>();
			handles.add("ComDataFbc");
			bizConfig = this.getSimpleBcWorkflow("ComDataFbc", "ComDataFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("EmailDataFbc", 146, 6);
			handles = new ArrayList<String>();
			handles.add("EmailDataFbc");
			bizConfig = this.getSimpleBcWorkflow("EmailDataFbc", "EmailDataFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("GamesDataFbc", 147, 6);
			handles = new ArrayList<String>();
			handles.add("GamesDataFbc");
			bizConfig = this.getSimpleBcWorkflow("GamesDataFbc", "GamesDataFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("IMDataFbc", 148, 6);
			handles = new ArrayList<String>();
			handles.add("IMDataFbc");
			bizConfig = this.getSimpleBcWorkflow("IMDataFbc", "IMDataFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("AudioStreamFbc", 149, 6);
			handles = new ArrayList<String>();
			handles.add("AudioStreamFbc");
			bizConfig = this.getSimpleBcWorkflow("AudioStreamFbc", "AudioStreamFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("VideoDataFbc", 150, 1);
			handles = new ArrayList<String>();
			handles.add("VideoDataFbc");
			bizConfig = this.getSimpleBcWorkflow("VideoDataFbc", "VideoDataFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("SnsDataFbc", 151, 0);
			handles = new ArrayList<String>();
			handles.add("SnsDataFbc");
			bizConfig = this.getSimpleBcWorkflow("SnsDataFbc", "SnsDataFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("SnsData2Fbc", 152, 6);
			handles = new ArrayList<String>();
			handles.add("SnsData2Fbc");
			bizConfig = this.getSimpleBcWorkflow("SnsData2Fbc", "SnsData2Fbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("1030DataFbc", 153, 1);
			handles = new ArrayList<String>();
			handles.add("1030DataFbc");
			bizConfig = this.getSimpleBcWorkflow("1030DataFbc", "1030DataFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("1030AutoFbc", 154, 1);
			handles = new ArrayList<String>();
			handles.add("1030AutoFbc");
			bizConfig = this.getSimpleBcWorkflow("1030AutoFbc", "1030AutoFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BucketIM6VolFbc", 155, 6);
			handles = new ArrayList<String>();
			handles.add("BucketIM6VolFbc");
			bizConfig = this.getSimpleBcWorkflow("BucketIM6VolFbc", "BucketIM6VolFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnivWalletAcc", 241, 1);
			handles = new ArrayList<String>();
			handles.add("UnivWalletAcc");
			bizConfig = this.getSimpleBcWorkflow("UnivWalletAcc", "UnivWalletAcc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliMocOnCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliMocOnCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliMocOnCtl", "UnliMocOnCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliMocTriCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliMocTriCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliMocTriCtl", "UnliMocTriCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliMocGblCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliMocGblCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliMocGblCtl", "UnliMocGblCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("Katok10", 1, 1);
			handles = new ArrayList<String>();
			handles.add("Katok10");
			bizConfig = this.getSimpleBcWorkflow("Katok10", "Katok10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("Katok15", 1, 1);
			handles = new ArrayList<String>();
			handles.add("Katok15");
			bizConfig = this.getSimpleBcWorkflow("Katok15", "Katok15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("KatokAtTex25", 1, 1);
			handles = new ArrayList<String>();
			handles.add("KatokAtTex25");
			bizConfig = this.getSimpleBcWorkflow("KatokAtTex25", "KatokAtTex25", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("KatokAtTex35", 1, 1);
			handles = new ArrayList<String>();
			handles.add("KatokAtTex35");
			bizConfig = this.getSimpleBcWorkflow("KatokAtTex35", "KatokAtTex35", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("KatokSubGrace", 1, 1);
			handles = new ArrayList<String>();
			handles.add("KatokSubGrace");
			bizConfig = this.getSimpleBcWorkflow("KatokSubGrace", "KatokSubGrace", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliSmsOnCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliSmsOnCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliSmsOnCtl", "UnliSmsOnCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliSmsTriCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliSmsTriCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliSmsTriCtl", "UnliSmsTriCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliSmsGblCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliSmsGblCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliSmsGblCtl", "UnliSmsGblCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliAccessSmsCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliAccessSmsCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliAccessSmsCtl", "UnliAccessSmsCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("SnsDataFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("SnsDataFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("SnsDataFbcCtl", "SnsDataFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("IMDataFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("IMDataFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("IMDataFbcCtl", "IMDataFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("ComDataFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("ComDataFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("ComDataFbcCtl", "ComDataFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("EmailDataFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("EmailDataFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("EmailDataFbcCtl", "EmailDataFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("GamesDataFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("GamesDataFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("GamesDataFbcCtl", "GamesDataFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("AudioStreamFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("AudioStreamFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("AudioStreamFbcCtl", "AudioStreamFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("VideoDataFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("VideoDataFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("VideoDataFbcCtl", "VideoDataFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("LTEUnliSurfFbc", 1, 1);
			handles = new ArrayList<String>();
			handles.add("LTEUnliSurfFbc");
			bizConfig = this.getSimpleBcWorkflow("LTEUnliSurfFbc", "LTEUnliSurfFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliSurfFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliSurfFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliSurfFbcCtl", "UnliSurfFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliSurfFbcCtlRen", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliSurfFbcCtlRen");
			bizConfig = this.getSimpleBcWorkflow("UnliSurfFbcCtlRen", "UnliSurfFbcCtlRen", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliThroFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliThroFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliThroFbcCtl", "UnliThroFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BrwsThroFbc", 1, 1);
			handles = new ArrayList<String>();
			handles.add("BrwsThroFbc");
			bizConfig = this.getSimpleBcWorkflow("BrwsThroFbc", "BrwsThroFbc", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("BBDataFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("BBDataFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("BBDataFbcCtl", "BBDataFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("RoamFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("RoamFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("RoamFbcCtl", "RoamFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliKonekFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliKonekFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliKonekFbcCtl", "UnliKonekFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("SnsData2FbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("SnsData2FbcCtl");
			bizConfig = this.getSimpleBcWorkflow("SnsData2FbcCtl", "SnsData2FbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("IMData2FbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("IMData2FbcCtl");
			bizConfig = this.getSimpleBcWorkflow("IMData2FbcCtl", "IMData2FbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliNxbFbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliNxbFbcCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliNxbFbcCtl", "UnliNxbFbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliEmail1FbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliEmail1FbcCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliEmail1FbcCtl", "UnliEmail1FbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliIM2FbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliIM2FbcCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliIM2FbcCtl", "UnliIM2FbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliPhoto1FbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliPhoto1FbcCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliPhoto1FbcCtl", "UnliPhoto1FbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliSns1FbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliSns1FbcCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliSns1FbcCtl", "UnliSns1FbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliBoost1FbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliBoost1FbcCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliBoost1FbcCtl", "UnliBoost1FbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliIM1FbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliIM1FbcCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliIM1FbcCtl", "UnliIM1FbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliBrws1FbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliBrws1FbcCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliBrws1FbcCtl", "UnliBrws1FbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Balance Adjustment...");
			resource = this.createSmartBalanceAdjustmentProfile("UnliSns2FbcCtl", 1, 1);
			handles = new ArrayList<String>();
			handles.add("UnliSns2FbcCtl");
			bizConfig = this.getSimpleBcWorkflow("UnliSns2FbcCtl", "UnliSns2FbcCtl", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			//===================================== Balance Adjustment Ends ===========================
			
			//===================================== Calling Circle Begins ===========================
			
/*			System.out.println("Offer, Resource & Profile - Predefined, Calling Circle Refill...");
			resource = this.createRefill("KaTokatTex25", "KT25", 1, "2500", CurrencyCode.PHP, "1", "1");
			handles = new ArrayList<String>();
			handles.add("RB506");
			bizConfig = this.getSimpleBcWorkflow("KaTokatTex25", "KaTok-at-Tex 25", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);


			System.out.println("Offer, Resource & Profile - Predefined, Calling Circle...");
			resource = this.createRefill("KaTokatTex35", "KT35", 1, "3500", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("RB507");
			bizConfig = this.getSimpleBcWorkflow("KaTokatTex35", "KaTok-at-Tex 35", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);



			System.out.println("Offer, Resource & Profile - Predefined, Calling Circle Refill...");
			resource = this.createRefill("KaTOK15", "K15", 1, "1500", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("RB319");
			bizConfig = this.getSimpleBcWorkflow("KaTOK15", "KaTOK 15", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Calling Circle Refill...");
			resource = this.createRefill("KaTOK10", "K10", 1, "1000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("RB318");
			bizConfig = this.getSimpleBcWorkflow("KaTOK10", "KaTOK 10", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

*/

			resource = new Service("KaTOK10");
			resource.setDescription("KaTOK10 Calling Circle Profile");
			resource.setConsumable(true);
			resource.setDiscoverable(true);
			resource.setExternallyConsumed(true);
			resource.setConsumptionUnitName("PHP");

			CallingCircleProfile fulfillmentProfile = new CallingCircleProfile("KaTOK10");
			fulfillmentProfile.setRefillProfileId("K10");
			fulfillmentProfile.setRefillType(1);
			fulfillmentProfile.setTransactionAmount("0");
			fulfillmentProfile.setTransactionCurrency(CurrencyCode.PHP);
			fulfillmentProfile.setPurchaseAmount("100");
			fulfillmentProfile.setRenewalAmount("999900");
			
			fulfillmentProfile.setFafIndicatorSponsorMember("100");
			fulfillmentProfile.setFafIndicatorMemberSponsor("101");
			fulfillmentProfile.setFafIndicatorMemberMember("999");
			fulfillmentProfile.setFafAccumulatorId((201));
			fulfillmentProfile.setProdcatOffer("PK10");
			fulfillmentProfile.setMaxMembers(1);
			fulfillmentProfile.setAssociatedPromo("Katok10Promo");
			fulfillmentProfile.setWelcomeMessageEventId("cc-success-enrollment");
			fulfillmentProfile.setA_PartyMemberThresholdBreachMessageEventId("090111519011");
			
			fulfillmentProfile.setCallingCircleCsOfferID(2015);
			fulfillmentProfile.setFreebieType("OFFER");
			fulfillmentProfile.setFreebieOfferId(2019);
			fulfillmentProfile.setFreebieOfferType(2);
			fulfillmentProfile.setFreebieOfferValidity((long) (86400000));
			fulfillmentProfile.setFreebiePlanCode("PK10");
			fulfillmentProfile.setFreebieTransactionAmount("1");
			fulfillmentProfile.setFreebieTransactionCurrency(CurrencyCode.PHP);
			fulfillmentProfile.setFreebieRenewalAmount("1");
			fulfillmentProfile.setFreebiePurchaseAmount("1");
			
			resource.addFulfillmentProfile(fulfillmentProfile.getName());
			profileRegistry.createProfile(fulfillmentProfile);
			
			Offer templatedOffer = new Offer("KaTOK10");
			templatedOffer.setDescription("KaTOK10 Calling Circle Offer");
			
			templatedOffer.setAutoTermination(new NoTermination());
			templatedOffer.setRenewalPeriod(new InfiniteTime());
			templatedOffer.setOfferState(State.TESTING);
			templatedOffer.setOfferState(State.PUBLISHED);
			templatedOffer.setRecurrent(false);
			templatedOffer.setCommercial(false);
			templatedOffer.addExternalHandle("RB318");
			templatedOffer.addExternalHandle("K10");

			AtomicProduct product = new AtomicProduct("KaTOK10");
			product.setQuota(new UnlimitedQuota());
			product.setResource(resource);
			product.setValidity(new InfiniteTime());

			templatedOffer.addProduct(product); 
			offerManager.createOffer(templatedOffer);
			serviceRegistry.createResource(resource);

	

			resource = new Service("KaTOK15");
			resource.setDescription("KaTOK15 Calling Circle Profile");
			resource.setConsumable(true);
			resource.setDiscoverable(true);
			resource.setExternallyConsumed(true);
			resource.setConsumptionUnitName("PHP");

			fulfillmentProfile = new CallingCircleProfile("KaTOK15");
			fulfillmentProfile.setRefillProfileId("K15");
			fulfillmentProfile.setRefillType(1);
			fulfillmentProfile.setTransactionAmount("0");
			fulfillmentProfile.setTransactionCurrency(CurrencyCode.PHP);
			fulfillmentProfile.setPurchaseAmount("100");
			fulfillmentProfile.setRenewalAmount("999900");
			
			fulfillmentProfile.setFafIndicatorSponsorMember("200");
			fulfillmentProfile.setFafIndicatorMemberSponsor("201");
			fulfillmentProfile.setFafIndicatorMemberMember("202");
			fulfillmentProfile.setFafAccumulatorId((201));
			fulfillmentProfile.setProdcatOffer("K15");
			fulfillmentProfile.setMaxMembers(2);
			fulfillmentProfile.setAssociatedPromo("Katok15Promo");
			fulfillmentProfile.setWelcomeMessageEventId("cc-success-enrollment");
			fulfillmentProfile.setA_PartyMemberThresholdBreachMessageEventId("090111519011");
			
			fulfillmentProfile.setCallingCircleCsOfferID(2016);
			fulfillmentProfile.setFreebieType("OFFER");
			fulfillmentProfile.setFreebieOfferId(2019);
			fulfillmentProfile.setFreebieOfferType(2);
			fulfillmentProfile.setFreebieOfferValidity((long) (86400000));
			fulfillmentProfile.setFreebiePlanCode("PK15");
			fulfillmentProfile.setFreebieTransactionAmount("1");
			fulfillmentProfile.setFreebieTransactionCurrency(CurrencyCode.PHP);
			fulfillmentProfile.setFreebieRenewalAmount("1");
			fulfillmentProfile.setFreebiePurchaseAmount("1");
			
			resource.addFulfillmentProfile(fulfillmentProfile.getName());
			profileRegistry.createProfile(fulfillmentProfile);
			
			templatedOffer = new Offer("KaTOK15");
			templatedOffer.setDescription("KaTOK15 Calling Circle Offer");
			
			templatedOffer.setAutoTermination(new NoTermination());
			templatedOffer.setRenewalPeriod(new InfiniteTime());
			templatedOffer.setOfferState(State.TESTING);
			templatedOffer.setOfferState(State.PUBLISHED);
			templatedOffer.setRecurrent(false);
			templatedOffer.setCommercial(false);
			templatedOffer.addExternalHandle("RB319");
			templatedOffer.addExternalHandle("K15");

			product = new AtomicProduct("KaTOK15");
			product.setQuota(new UnlimitedQuota());
			product.setResource(resource);
			product.setValidity(new InfiniteTime());

			templatedOffer.addProduct(product); 
			offerManager.createOffer(templatedOffer);
			serviceRegistry.createResource(resource);

			
			resource = new Service("KaTOK25");
			resource.setDescription("KaTOK25 Calling Circle Profile");
			resource.setConsumable(true);
			resource.setDiscoverable(true);
			resource.setExternallyConsumed(true);
			resource.setConsumptionUnitName("PHP");

			fulfillmentProfile = new CallingCircleProfile("KaTOK25");
			fulfillmentProfile.setRefillProfileId("KT25");
			fulfillmentProfile.setRefillType(1);
			fulfillmentProfile.setTransactionAmount("0");
			fulfillmentProfile.setTransactionCurrency(CurrencyCode.PHP);
			fulfillmentProfile.setPurchaseAmount("100");
			fulfillmentProfile.setRenewalAmount("999900");
			
			fulfillmentProfile.setFafIndicatorSponsorMember("300");
			fulfillmentProfile.setFafIndicatorMemberSponsor("301");
			fulfillmentProfile.setFafIndicatorMemberMember("999");
			fulfillmentProfile.setFafAccumulatorId((201));
			fulfillmentProfile.setProdcatOffer("KT25");
			fulfillmentProfile.setMaxMembers(1);
			fulfillmentProfile.setAssociatedPromo("Katok25Promo");
			fulfillmentProfile.setWelcomeMessageEventId("cc-success-enrollment");
			fulfillmentProfile.setA_PartyMemberThresholdBreachMessageEventId("090111519011");
			
			fulfillmentProfile.setCallingCircleCsOfferID(2017);
			fulfillmentProfile.setFreebieType("REFILL");
			fulfillmentProfile.setFreebieRefillID("KF25");
			fulfillmentProfile.setFreebieRefillType(1);
			fulfillmentProfile.setFreebiePlanCode("GR007");
			fulfillmentProfile.setFreebieTransactionAmount("1");
			fulfillmentProfile.setFreebieTransactionCurrency(CurrencyCode.PHP);
			fulfillmentProfile.setFreebieRenewalAmount("1");
			fulfillmentProfile.setFreebiePurchaseAmount("1");
			
			resource.addFulfillmentProfile(fulfillmentProfile.getName());
			profileRegistry.createProfile(fulfillmentProfile);
			
			templatedOffer = new Offer("KaTOK25");
			templatedOffer.setDescription("KaTOK25 Calling Circle Offer");
			
			templatedOffer.setAutoTermination(new NoTermination());
			templatedOffer.setRenewalPeriod(new InfiniteTime());
			templatedOffer.setOfferState(State.TESTING);
			templatedOffer.setOfferState(State.PUBLISHED);
			templatedOffer.setRecurrent(false);
			templatedOffer.setCommercial(false);
			templatedOffer.addExternalHandle("RB506");
			templatedOffer.addExternalHandle("KT25");

			product = new AtomicProduct("KaTOK25");
			product.setQuota(new UnlimitedQuota());
			product.setResource(resource);
			product.setValidity(new InfiniteTime());

			templatedOffer.addProduct(product); 
			offerManager.createOffer(templatedOffer);
			serviceRegistry.createResource(resource);


			resource = new Service("KaTOK35");
			resource.setDescription("KaTOK35 Calling Circle Profile");
			resource.setConsumable(true);
			resource.setDiscoverable(true);
			resource.setExternallyConsumed(true);
			resource.setConsumptionUnitName("PHP");

			fulfillmentProfile = new CallingCircleProfile("KaTOK35");
			fulfillmentProfile.setRefillProfileId("KT35");
			fulfillmentProfile.setRefillType(1);
			fulfillmentProfile.setTransactionAmount("0");
			fulfillmentProfile.setTransactionCurrency(CurrencyCode.PHP);
			fulfillmentProfile.setPurchaseAmount("100");
			fulfillmentProfile.setRenewalAmount("999900");
			
			fulfillmentProfile.setFafIndicatorSponsorMember("400");
			fulfillmentProfile.setFafIndicatorMemberSponsor("401");
			fulfillmentProfile.setFafIndicatorMemberMember("402");
			fulfillmentProfile.setFafAccumulatorId((201));
			fulfillmentProfile.setProdcatOffer("KT35");
			fulfillmentProfile.setMaxMembers(2);
			fulfillmentProfile.setAssociatedPromo("Katok35Promo");
			fulfillmentProfile.setWelcomeMessageEventId("cc-success-enrollment");
			fulfillmentProfile.setA_PartyMemberThresholdBreachMessageEventId("090111519011");
			
			fulfillmentProfile.setCallingCircleCsOfferID(2018);
			fulfillmentProfile.setFreebieType("REFILL");
			fulfillmentProfile.setFreebieRefillID("KF25");
			fulfillmentProfile.setFreebieRefillType(1);
			fulfillmentProfile.setFreebiePlanCode("GR007");
			fulfillmentProfile.setFreebieTransactionAmount("1");
			fulfillmentProfile.setFreebieTransactionCurrency(CurrencyCode.PHP);
			fulfillmentProfile.setFreebieRenewalAmount("1");
			fulfillmentProfile.setFreebiePurchaseAmount("1");
			
			resource.addFulfillmentProfile(fulfillmentProfile.getName());
			profileRegistry.createProfile(fulfillmentProfile);
			
			templatedOffer = new Offer("KaTOK35");
			templatedOffer.setDescription("KaTOK35 Calling Circle Offer");
			
			templatedOffer.setAutoTermination(new NoTermination());
			templatedOffer.setRenewalPeriod(new InfiniteTime());
			templatedOffer.setOfferState(State.TESTING);
			templatedOffer.setOfferState(State.PUBLISHED);
			templatedOffer.setRecurrent(false);
			templatedOffer.setCommercial(false);
			templatedOffer.addExternalHandle("RB507");
			templatedOffer.addExternalHandle("KT35");

			product = new AtomicProduct("KaTOK35");
			product.setQuota(new UnlimitedQuota());
			product.setResource(resource);
			product.setValidity(new InfiniteTime());

			templatedOffer.addProduct(product); 
			offerManager.createOffer(templatedOffer);
			serviceRegistry.createResource(resource);

			
			//===================================== Calling Circle Ends ===========================
			
			
			//===================================== Araw Araw Begin===========================
			

/*			System.out.println("Offer, Resource & Profile - Predefined, Araw Araw Refill...");
			resource = this.createRefill("ArawArawText20", "1044", 1, "2000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("RB281");
			bizConfig = this.getSimpleBcWorkflow("ArawArawText20", "Araw-Araw Text 20", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Araw Araw Refill...");
			resource = this.createRefill("ArawArawLoad30", "1049", 1, "3000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("GR028");
			bizConfig = this.getSimpleBcWorkflow("ArawArawLoad30", "Araw-Araw Load 30", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Araw Araw Refill...");
			resource = this.createRefill("ArawArawLoad60", "1050", 1, "6000", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("GR029");
			bizConfig = this.getSimpleBcWorkflow("ArawArawLoad60", "Araw-Araw Load 60", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);

			System.out.println("Offer, Resource & Profile - Predefined, Araw Araw Refill...");
			resource = this.createRefill("iCHamp", "1051", 1, "0", CurrencyCode.PHP);
			handles = new ArrayList<String>();
			handles.add("CMP01");
			bizConfig = this.getSimpleBcWorkflow("iCHamp", "iCHamp", handles, resource);
			offerManager.createOffer(bizConfig);
			serviceRegistry.createResource(resource);
*/
		
			resource = new Service("iCHamp");
			resource.setDescription("Araw Araw iChamp Refill Profile");
			resource.setConsumable(true);
			resource.setDiscoverable(true);
			resource.setExternallyConsumed(true);
			resource.setConsumptionUnitName("PHP");

			RefillProfile fulfillmentProfile1 = new RefillProfile("iCHamp");
			fulfillmentProfile1.setRefillProfileId("1051");
			fulfillmentProfile1.setRefillType(1);
			fulfillmentProfile1.setTransactionAmount("0");
			fulfillmentProfile1.setTransactionCurrency(CurrencyCode.PHP);
			fulfillmentProfile1.setPurchaseAmount("100");
			fulfillmentProfile1.setRenewalAmount("999900");
			

			resource.addFulfillmentProfile(fulfillmentProfile1.getName());
			profileRegistry.createProfile(fulfillmentProfile1);
			
			templatedOffer = new Offer("iCHamp");
			templatedOffer.setDescription("iCHamp Araw Araw Offer");
			templatedOffer.setAutoTermination(new TerminateAfterNDays(7));
			templatedOffer.setMinimumCommitment(new CommitUntilNDays(7));
			templatedOffer.setRenewalPeriod(new DaysTime(1));
			templatedOffer.setOfferState(State.TESTING);
			templatedOffer.setOfferState(State.PUBLISHED);
			templatedOffer.setRecurrent(true);
			templatedOffer.setCommercial(false);
			templatedOffer.addExternalHandle("CMP01");

			product = new AtomicProduct("iCHamp");
			product.setQuota(new UnlimitedQuota());
			product.setResource(resource);
			product.setValidity(new DaysTime(1));

			templatedOffer.addProduct(product); 
			offerManager.createOffer(templatedOffer);
			serviceRegistry.createResource(resource);

			System.out.println("ArawArawText20 offer Refill...");
			resource = new Service("ArawArawText20");
			resource.setDescription("ArawArawText20 Refill Profile");
			resource.setConsumable(true);
			resource.setDiscoverable(true);
			resource.setExternallyConsumed(true);
			resource.setConsumptionUnitName("PHP");

			fulfillmentProfile1 = new RefillProfile("ArawArawText20");
			fulfillmentProfile1.setRefillProfileId("1044");
			fulfillmentProfile1.setRefillType(1);
			fulfillmentProfile1.setTransactionAmount("0");
			fulfillmentProfile1.setTransactionCurrency(CurrencyCode.PHP);
			fulfillmentProfile1.setPurchaseAmount("100");
			fulfillmentProfile1.setRenewalAmount("999900");
			

			resource.addFulfillmentProfile(fulfillmentProfile1.getName());
			profileRegistry.createProfile(fulfillmentProfile1);
			
			templatedOffer = new Offer("ArawArawText20");
			templatedOffer.setDescription("ArawArawText20 Offer");
			templatedOffer.setAutoTermination(new TerminateAfterNDays(5));
			templatedOffer.setMinimumCommitment(new CommitUntilNDays(5));
			templatedOffer.setRenewalPeriod(new DaysTime(1));
			templatedOffer.setOfferState(State.TESTING);
			templatedOffer.setOfferState(State.PUBLISHED);
			templatedOffer.setRecurrent(true);
			templatedOffer.setCommercial(false);
			templatedOffer.addExternalHandle("RB281");

			product = new AtomicProduct("ArawArawText20");
			product.setQuota(new UnlimitedQuota());
			product.setResource(resource);
			product.setValidity(new DaysTime(1));

			templatedOffer.addProduct(product); 
			offerManager.createOffer(templatedOffer);
			serviceRegistry.createResource(resource);
						
			resource = new Service("ArawArawLoad30");
			resource.setDescription("Araw Araw Load 30 Refill Profile");
			resource.setConsumable(true);
			resource.setDiscoverable(true);
			resource.setExternallyConsumed(true);
			resource.setConsumptionUnitName("PHP");

			
//			resource = new Service("ArawArawText20");
//			resource.setDescription("Araw Araw Load 20 Refill Profile");
//			resource.setConsumable(true);
//			resource.setDiscoverable(true);
//			resource.setExternallyConsumed(true);
//			resource.setConsumptionUnitName("PHP");
//
//			fulfillmentProfile = new RefillProfile("ArawArawText20");
//			fulfillmentProfile.setRefillProfileId("1044");
//			fulfillmentProfile.setRefillType(1);
//			fulfillmentProfile.setTransactionAmount("0");
//			fulfillmentProfile.setTransactionCurrency(CurrencyCode.PHP);
//			fulfillmentProfile.setPurchaseAmount("1");
//			fulfillmentProfile.setRenewalAmount("9999");
//			
//
//			resource.addFulfillmentProfile(fulfillmentProfile.getName());
//			profileRegistry.createProfile(fulfillmentProfile);
//			
//			templatedOffer = new Offer("ArawArawText20");
//			templatedOffer.setDescription("Araw Araw Load 20 Offer");
//			templatedOffer.setAutoTermination(new TerminateAfterNDays(5));
//			templatedOffer.setMinimumCommitment(new CommitUntilNDays(5));
//			templatedOffer.setRenewalPeriod(new DaysTime(1));
//			templatedOffer.setOfferState(State.TESTING);
//			templatedOffer.setOfferState(State.PUBLISHED);
//			templatedOffer.setRecurrent(true);
//			templatedOffer.setCommercial(false);
//			templatedOffer.addExternalHandle("RB281");
//
//			product = new AtomicProduct("ArawArawText20");
//			product.setQuota(new UnlimitedQuota());
//			product.setResource(resource);
//			product.setValidity(new DaysTime(1));
//
//			templatedOffer.addProduct(product); 
//			offerManager.createOffer(templatedOffer);
//			serviceRegistry.createResource(resource);
//						
//			resource = new Service("ArawArawLoad30");
//			resource.setDescription("Araw Araw Load 30 Refill Profile");
//			resource.setConsumable(true);
//			resource.setDiscoverable(true);
//			resource.setExternallyConsumed(true);
//			resource.setConsumptionUnitName("PHP");

			fulfillmentProfile1 = new RefillProfile("ArawArawLoad30");
			fulfillmentProfile1.setRefillProfileId("1049");
			fulfillmentProfile1.setRefillType(1);
			fulfillmentProfile1.setTransactionAmount("0");
			fulfillmentProfile1.setTransactionCurrency(CurrencyCode.PHP);
			fulfillmentProfile1.setPurchaseAmount("100");
			fulfillmentProfile1.setRenewalAmount("999900");
			
			resource.addFulfillmentProfile(fulfillmentProfile1.getName());
			profileRegistry.createProfile(fulfillmentProfile1);
			
			templatedOffer = new Offer("ArawArawLoad30");
			templatedOffer.setDescription("Araw Araw Load 30 Offer");
			templatedOffer.setAutoTermination(new TerminateAfterNDays(3));
			templatedOffer.setMinimumCommitment(new CommitUntilNDays(3));
			templatedOffer.setRenewalPeriod(new DaysTime(1));
			templatedOffer.setOfferState(State.TESTING);
			templatedOffer.setOfferState(State.PUBLISHED);
			templatedOffer.setRecurrent(true);
			templatedOffer.setCommercial(false);
			templatedOffer.addExternalHandle("GR028");

			product = new AtomicProduct("ArawArawLoad30");
			product.setQuota(new UnlimitedQuota());
			product.setResource(resource);
			product.setValidity(new DaysTime(1));

			templatedOffer.addProduct(product); 
			offerManager.createOffer(templatedOffer);
			serviceRegistry.createResource(resource);
						

			resource = new Service("ArawArawLoad60");
			resource.setDescription("Araw Araw Load 60 Refill Profile");
			resource.setConsumable(true);
			resource.setDiscoverable(true);
			resource.setExternallyConsumed(true);
			resource.setConsumptionUnitName("PHP");

			fulfillmentProfile1 = new RefillProfile("ArawArawLoad60");
			fulfillmentProfile1.setRefillProfileId("1050");
			fulfillmentProfile1.setRefillType(1);
			fulfillmentProfile1.setTransactionAmount("0");
			fulfillmentProfile1.setTransactionCurrency(CurrencyCode.PHP);
			fulfillmentProfile1.setPurchaseAmount("100");
			fulfillmentProfile1.setRenewalAmount("999900");
			
			resource.addFulfillmentProfile(fulfillmentProfile1.getName());
			profileRegistry.createProfile(fulfillmentProfile1);
			
			templatedOffer = new Offer("ArawArawLoad60");
			templatedOffer.setDescription("Araw Araw Load 60 Offer");
			templatedOffer.setAutoTermination(new TerminateAfterNDays(7));
			templatedOffer.setMinimumCommitment(new CommitUntilNDays(7));
			templatedOffer.setRenewalPeriod(new DaysTime(1));
			templatedOffer.setOfferState(State.TESTING);
			templatedOffer.setOfferState(State.PUBLISHED);
			templatedOffer.setRecurrent(true);
			templatedOffer.setCommercial(false);
			templatedOffer.addExternalHandle("GR029");

			product = new AtomicProduct("ArawArawLoad60");
			product.setQuota(new UnlimitedQuota());
			product.setResource(resource);
			product.setValidity(new DaysTime(1));

			templatedOffer.addProduct(product); 
			offerManager.createOffer(templatedOffer);
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
		Price basePrice = new Price("PHP", 100);
		basePrice.addRatingRule(new SmartSimplePricingPolicy("custInfoCharge"));
		templatedOffer.setPrice(basePrice);

		for (String handle: handles)
			templatedOffer.addExternalHandle(handle);

		AtomicProduct product = new AtomicProduct(name);
		product.setQuota(new UnlimitedQuota());
		product.setResource(resource);
		product.setValidity(new InfiniteTime());

		templatedOffer.addProduct(product);

		return templatedOffer;
	}	

//	public Offer getSimpleOffer(String name, String description, List<String> handles, Resource resource) throws CatalogException {
//		Offer templatedOffer = new Offer(name);
//		templatedOffer.setDescription(description);
//		templatedOffer.setAutoTermination(new NoTermination());
//		templatedOffer.setRenewalPeriod(new InfiniteTime());
//		templatedOffer.setOfferState(State.TESTING);
//		templatedOffer.setOfferState(State.PUBLISHED);
//		templatedOffer.setRecurrent(false);
//		templatedOffer.setTrialPeriod(new InfiniteTime());
//		templatedOffer.setCommercial(true);
//		templatedOffer.setPrice(new Price("PHP", 100));
//
//		for (String handle: handles)
//			templatedOffer.addExternalHandle(handle);
//
//		AtomicProduct product = new AtomicProduct(name);
//		product.setQuota(new UnlimitedQuota());
//		product.setResource(resource);
//		product.setValidity(new InfiniteTime());
//
//		templatedOffer.addProduct(product);
//
//		return templatedOffer;
//	}

	public Resource createRefill(String name, String refillProfileId, Integer refillType, String transactionAmount, CurrencyCode transactionCurrency, String renewalAmount, String purchaseAmount) throws CatalogException {

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
		fulfillmentProfile.setRenewalAmount(renewalAmount);
		fulfillmentProfile.setPurchaseAmount(purchaseAmount);
		

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
		fulfillmentProfile.setTransactionCurrency("PHP");
		
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
		resource.setDescription("Smart Flexible Refill Profile");
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
		for (int i=1; i<8; i++ ) {
			ServiceOffering soInfo = new ServiceOffering();
			soInfo.setServiceOfferingId(i);
			if (soId == 0)
				soInfo.setServiceOfferingActiveFlag(false);
			else
				soInfo.setServiceOfferingActiveFlag((soId==i));
			serviceOfferings.add(soInfo);
		}
		System.out.println("Service Offerings prepared: " + serviceOfferings);
		
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
