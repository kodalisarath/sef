//package test.ericsson.prodcat;
//
//import java.util.ArrayList;
//import java.util.Set;
//
//import com.ericsson.raso.sef.bes.prodcat.CatalogException;
//import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
//import com.ericsson.raso.sef.bes.prodcat.entities.InfiniteTime;
//import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
//import com.ericsson.raso.sef.bes.prodcat.entities.OfferLifeCycle;
//import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
//import com.ericsson.raso.sef.bes.prodcat.entities.Service;
//import com.ericsson.raso.sef.core.db.model.CurrencyCode;
//
//
//import com.ericsson.raso.sef.fulfillment.profiles.DedicatedAccountProfile;
//import com.ericsson.raso.sef.fulfillment.profiles.OfferProfile;
//import com.ericsson.raso.sef.fulfillment.profiles.RefillProfile;
//
//public class PromoHelper {
//	public Resource createRefill(String name, String refillProfileId, Integer refillType, String transactionAmount, CurrencyCode transactionCurrency) throws CatalogException {
//		
//		Resource resource = new Service(name);
//		resource.setDescription("Refill Profile");
//		resource.setConsumable(true);
//		resource.setDiscoverable(true);
//		resource.setExternallyConsumed(true);
//		resource.setConsumptionUnitName("PHP");
//		resource.setCost(null); // TODO to be fixed later
//		
//		RefillProfile fulfillmentProfile = new RefillProfile(name);
//		fulfillmentProfile.setRefillProfileId(refillProfileId);
//		fulfillmentProfile.setRefillType(refillType);
//		fulfillmentProfile.setTransactionAmount(transactionAmount);
//		fulfillmentProfile.setTransactionCurrency(transactionCurrency);
//		
//		resource.addFulfillmentProfile(fulfillmentProfile);
//		
//		return resource;
//	}
//	
//	
//	public Resource createDA(String name, Integer dedicatedAccountId, String transactionCurrency) throws Exception {
//		
//		Resource resource = new Service(name);
//		
//		resource.setDescription("Dedicated Account");
//		resource.setConsumable(true);
//		resource.setDiscoverable(true);
//		resource.setExternallyConsumed(true);
//		resource.setConsumptionUnitName("PHP");
//		resource.setCost(null);
//		
//		DedicatedAccountProfile fulfillmentProfile = new DedicatedAccountProfile(name);
//		fulfillmentProfile.setDedicatedAccountID(dedicatedAccountId);
//		fulfillmentProfile.setTransactionCurrency(transactionCurrency);
//		
//		resource.addFulfillmentProfile(fulfillmentProfile);
//		
//		return resource;
//	}
//	
//	public Resource createTimerProfile(String name, Integer offerId) throws Exception {
//		
//		Resource resource = new Service(name);
//		
//		resource.setDescription("Timer Prolfile");
//		resource.setConsumable(true);
//		resource.setDiscoverable(true);
//		resource.setExternallyConsumed(true);
//		resource.setConsumptionUnitName("PHP");
//		resource.setCost(null);
//		
//		OfferProfile fulfillmentProfile = new OfferProfile(name);
//		
//		fulfillmentProfile.setOfferID(offerId);
//		
//		resource.addFulfillmentProfile(fulfillmentProfile);
//		
//		return resource;
//	}
//	
//	public Offer createCommercialOffer(String name, String description, Set<String> planCodes, ArrayList<Resource> resources) throws Exception {
//
//		Offer commercialOffer = new Offer(name);
//		commercialOffer.setDescription(description);
//		commercialOffer.setAutoTermination(null);
//		commercialOffer.setPrice(null);
//		commercialOffer.setCommercial(true);
//		commercialOffer.setRenewalPeriod(new InfiniteTime());
//		
//		for (String planCode : planCodes) {
//			commercialOffer.addExternalHandle(planCode);
//		}
//		
//		
//		for (int i = 0; i < resources.size(); i++) {
//			
//			String productName = "Commercial.Product" + i;
//			AtomicProduct createCommercialProduct = new AtomicProduct(productName);
//			createCommercialProduct.setCriteria(null);
//			createCommercialProduct.setResetQuotaOnRenewal(false);
//			// TODO createRefill.setMeta("TODO", forImplemenTation);
//			createCommercialProduct.setResource(resources.get(i));
//			commercialOffer.addProduct(createCommercialProduct);
//		}
//		
//		return commercialOffer;
//		
//	}
//}
//
