package com.ericsson.sef.promo_creation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.raso.sef.bes.prodcat.entities.AbstractTimeCharacteristic;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.InfiniteTime;
import com.ericsson.raso.sef.bes.prodcat.entities.NoTermination;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.Price;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.entities.Service;
import com.ericsson.raso.sef.bes.prodcat.entities.State;
import com.ericsson.raso.sef.bes.prodcat.entities.UnlimitedQuota;
import com.ericsson.raso.sef.bes.prodcat.policies.AccumulateUnlimited;
import com.ericsson.raso.sef.bes.prodcat.policies.SwitchUnlimited;
import com.ericsson.raso.sef.core.db.model.CurrencyCode;
import com.ericsson.raso.sef.fulfillment.profiles.AddPeriodicAccountManagementDataProfile;
import com.ericsson.raso.sef.fulfillment.profiles.CreateSubscriberProfile;
import com.ericsson.raso.sef.fulfillment.profiles.DedicatedAccountProfile;
import com.ericsson.raso.sef.fulfillment.profiles.DeleteSubscriberProfile;
import com.ericsson.raso.sef.fulfillment.profiles.DnsUpdateProfile;
import com.ericsson.raso.sef.fulfillment.profiles.OfferProfile;
import com.ericsson.raso.sef.fulfillment.profiles.ProfileRegistry;
import com.ericsson.raso.sef.fulfillment.profiles.ReadBalancesProfile;
import com.ericsson.raso.sef.fulfillment.profiles.ReadSubscriberProfile;
import com.ericsson.raso.sef.fulfillment.profiles.RefillProfile;
import com.ericsson.raso.sef.fulfillment.profiles.UpdateServiceClassProfile;
import com.ericsson.raso.sef.fulfillment.profiles.UpdateSubscriberSegmentationProfile;

@SuppressWarnings("unused")
public class PromoHelper {
	
	ProfileRegistry profileRegistry = null;
	
	private static final Logger logger = LoggerFactory.getLogger(PromoHelper.class);
	
	public PromoHelper() {
		profileRegistry = new ProfileRegistry();
	}
	
	public Resource createRefill(String name, String refillProfileId, Integer refillType, String transactionAmount, CurrencyCode transactionCurrency, String[] abstractResources) throws Exception {
		
		Resource resource = new Service(name);
		resource.setDescription("Refill Profile");
		resource.setConsumable(true);
		resource.setDiscoverable(true);
		resource.setExternallyConsumed(true);
		resource.setConsumptionUnitName("PHP");
		resource.setCost(null); // TODO to be fixed later
		
		RefillProfile fulfillmentProfile = new RefillProfile(name);
		fulfillmentProfile.setRefillProfileId(refillProfileId);
		fulfillmentProfile.setRefillType(refillType);
		fulfillmentProfile.setTransactionAmount(transactionAmount);
		fulfillmentProfile.setTransactionCurrency(transactionCurrency);
		fulfillmentProfile.setAbstractResources(Arrays.asList(abstractResources));
		
		//System.out.println(fulfillmentProfile.getName());
		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
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
		profileRegistry.createProfile(fulfillmentProfile);
		
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
		return resource;
		
	}
	
	public Resource createDA(String name, Integer dedicatedAccountId, String transactionCurrency, String transactionAmount) throws Exception {
		
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
		profileRegistry.createProfile(fulfillmentProfile);
		
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
		return resource;
	}
	
	public Resource createTimerProfile(String name, Integer offerId) throws Exception {
		
		Resource resource = new Service(name);
		
		resource.setDescription("Timer Profile");
		resource.setConsumable(true);
		resource.setDiscoverable(true);
		resource.setExternallyConsumed(true);
		resource.setConsumptionUnitName("PHP");
		resource.setCost(null);
		
		OfferProfile fulfillmentProfile = new OfferProfile(name);
		
		fulfillmentProfile.setOfferID(offerId);
		profileRegistry.createProfile(fulfillmentProfile);
		
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
		return resource;
	}
	
	public Resource createDnsUpdateProfile(String name, String description) throws Exception
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
		
		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
		return resource;		
	}
	
	public Resource createCreateSubscriberProfile(String name, String description) throws Exception
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
		
		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
		return resource;		
	}
	
	public Resource createDeleteSuscriberProfile(String name, String description) throws Exception
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
		
		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
		return resource;		
	}	
	
	public Resource createDeleteDnsEntryProfile(String name, String description) throws Exception
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);
		
//		DeleteDns fulfillmentProfile = new DeleteSubscriberProfile(name);
//		
//		profileRegistry.createProfile(fulfillmentProfile);
//		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
		return resource;		
	}	
	
	public Resource createGetAccountDetailsProfile(String name, String description) throws Exception
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

	public Resource createGetBalanceAndDateProfile(String name, String description) throws Exception
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
	
	public Resource createUpdateSubscriberSegmentationProfile(String name, String description) throws Exception
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
		
		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
		return resource;
	}
	
	public Resource createUpdateServiceClassProfile(String name, String description) throws Exception
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

	public Resource createAddPeriodicAccountManagementProfile(String name, String description) throws Exception
	{
		Resource resource = new Service(name);
		resource.setDescription(description);
		resource.setAbstract(false);
		resource.setDiscoverable(false);
		resource.setExternallyConsumed(false);
		resource.setConsumable(false);
		resource.setEnforcedMaxQuota(-1L);
		resource.setEnforcedMinQuota(-1L);
		
		AddPeriodicAccountManagementDataProfile fulfillmentProfile = new AddPeriodicAccountManagementDataProfile(name);
		profileRegistry.createProfile(fulfillmentProfile);
		resource.addFulfillmentProfile(fulfillmentProfile.getName());
		
		return resource;
	}
	
	public Offer createCommercialOffer(String name, String description, String currencyCode, Integer price, Set<String> planCodes, ArrayList<Resource> resources) throws Exception {

		Offer commercialOffer = new Offer(name);
		commercialOffer.setDescription(description);
		commercialOffer.setAutoTermination(null);
		commercialOffer.setCommercial(false);
		commercialOffer.setPrice(new Price(currencyCode, price));
		commercialOffer.setRenewalPeriod(new InfiniteTime());
		
		// System.out.println("Adding Product: " + commercialOffer.getName());
		
		for (String planCode : planCodes) {
			commercialOffer.addExternalHandle(planCode);
		}
		
		
		for (int i = 0; i < resources.size(); i++) {
			
			String productName = commercialOffer.getName() + "_" + resources.get(i).getName();
			AtomicProduct createCommercialProduct = new AtomicProduct(productName);
			//createCommercialProduct.setCriteria(null);
			createCommercialProduct.setResetQuotaOnRenewal(false);
			// TODO createRefill.setMeta("TODO", forImplemenTation);
			createCommercialProduct.setResource(resources.get(i));
			//System.out.println("Adding commercial product: " + productName);
			
			commercialOffer.addProduct(createCommercialProduct);
		}
		
		// System.out.println("Adding Commercial Offer: " + commercialOffer.toString());
		// logger.info("CommercialOffer: " + commercialOffer.toString());
		return commercialOffer;
		
	}

	public Offer createNonCommercialOffer(String name, String description, Set<String> planCodes,  ArrayList<Resource> resources) throws Exception {

			Offer nonCommercialOffer = new Offer(name);
			nonCommercialOffer.setAutoTermination(new NoTermination());
			nonCommercialOffer.setRenewalPeriod(new InfiniteTime());
			nonCommercialOffer.setOfferState(State.PUBLISHED);
			nonCommercialOffer.setRecurrent(false);
			nonCommercialOffer.setTrialPeriod(new InfiniteTime());
			nonCommercialOffer.setCommercial(false);
			
			// System.out.println("Adding Non-Commercial Offer: " + nonCommercialOffer.toString());
			
			for (String planCode : planCodes) {
				nonCommercialOffer.addExternalHandle(planCode);
			}
			
			for (int i = 0; i < resources.size(); i++) {
				
				String productName = nonCommercialOffer.getName() + "_" + resources.get(i).getName();
				AtomicProduct createNonCommercialProduct = new AtomicProduct(productName);
				createNonCommercialProduct.setQuota(new UnlimitedQuota());
				createNonCommercialProduct.setResource(resources.get(i));
				// logger.info("Non CommercialOffer: " + nonCommercialOffer.toString());
				nonCommercialOffer.addProduct(createNonCommercialProduct);
			}
			
			return nonCommercialOffer;
	}
	
}
