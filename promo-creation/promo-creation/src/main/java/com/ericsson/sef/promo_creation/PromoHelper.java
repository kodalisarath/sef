package com.ericsson.sef.promo_creation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.InfiniteTime;
import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
import com.ericsson.raso.sef.bes.prodcat.entities.Price;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.entities.Service;
import com.ericsson.raso.sef.core.db.model.CurrencyCode;
import com.ericsson.raso.sef.fulfillment.profiles.AddPeriodicAccountManagementDataProfile;
import com.ericsson.raso.sef.fulfillment.profiles.DedicatedAccountProfile;
import com.ericsson.raso.sef.fulfillment.profiles.OfferProfile;
import com.ericsson.raso.sef.fulfillment.profiles.ProfileRegistry;
import com.ericsson.raso.sef.fulfillment.profiles.RefillProfile;

@SuppressWarnings("unused")
public class PromoHelper {
	
	ProfileRegistry profileRegistry = null;
	
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
		
		System.out.println(fulfillmentProfile.getName());
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

	
	public Offer createCommercialOffer(String name, String description, String currencyCode, Integer price, Set<String> planCodes, ArrayList<Resource> resources) throws Exception {

		Offer commercialOffer = new Offer(name);
		commercialOffer.setDescription(description);
		commercialOffer.setAutoTermination(null);
		commercialOffer.setCommercial(false);
		commercialOffer.setPrice(new Price(currencyCode, price));
		commercialOffer.setRenewalPeriod(new InfiniteTime());
		
		System.out.println("Adding Product: " + commercialOffer.getName());
		
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
			System.out.println("Adding commercial product: " + productName);
			
			commercialOffer.addProduct(createCommercialProduct);
		}
		
		return commercialOffer;
		
	}
	
}
