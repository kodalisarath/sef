//package test.ericsson.prodcat;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//
//import com.ericsson.raso.sef.bes.prodcat.OfferManager;
//import com.ericsson.raso.sef.bes.prodcat.ServiceRegistry;
//import com.ericsson.raso.sef.bes.prodcat.entities.Offer;
//import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
//import com.ericsson.raso.sef.core.db.model.CurrencyCode;
//
//public class main {
//
//	public static void main(String[] args) throws Exception {
//		System.setProperty("path.offeringCatalog", "C:\\Users\\enolraf\\Documents\\Smart IN-IL\\IL\\New Build\\sef-core\\offeringCatalog.ccem");
//		System.setProperty("path.serviceRegistry", "C:\\Users\\enolraf\\Documents\\Smart IN-IL\\IL\\New Build\\sef-core\\serviceRegistry.ccem");
//		// TODO Auto-generated method stub
//		PromoHelper helper = new PromoHelper();
//		OfferManager.offerStoreLocation = null;
//		
//		
//		ArrayList<Resource> resources = new ArrayList<Resource>();
//		
//		resources.add(helper.createRefill("AllText10_Refill", "1017", 0, "1", CurrencyCode.PHP));
//		resources.add(helper.createDA("AllText10_DA", 54, "PHP"));
//		resources.add(helper.createTimerProfile("AllText10_TimerOffer_1054", 1054));
//		
//		// TODO Add these to service registry
//		ServiceRegistry serviceRegistry = new ServiceRegistry();
//		for (Integer i = 0; i < resources.size(); i++) {
//			serviceRegistry.createResource(resources.get(i));
//		}
//		
//		HashSet<String> planCodes=new HashSet<String>();
//		planCodes.add("J");
//		Offer offer = helper.createCommercialOffer("AllText10", "All Text 10", planCodes, resources);
//		
//		// TODO Add to offering catalog
//		
//		OfferManager offerManager = new OfferManager();
//		
//		offerManager.createOffer(offer);
//		
//	}
//
//}
