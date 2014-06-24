package com.ericsson.raso.sef.smart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.subscription.response.DiscoveryResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.sef.bes.api.entities.Meta;
import com.hazelcast.core.ISemaphore;

public class PasaServiceManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(PasaServiceManager.class);


	private static PasaServiceManager instance = null;

	private String pasaServiceStoreLocation = null;

	private PasaServiceManager() { 
		pasaServiceStoreLocation = System.getenv("SMART_PASA_STORE_URI");
	}


	public static synchronized PasaServiceManager getInstance() {
		if (instance == null)
			instance = new PasaServiceManager();
		return instance;
	}

	public boolean isPasaSendAllowed(String subscriberId, String pasaLoadID, Integer value) {
		String subscriberPasaFile = this.pasaServiceStoreLocation;

		LOGGER.debug("Entering get pasa function...");

		if (this.pasaServiceStoreLocation == null) {
			LOGGER.debug("SMART_PASA_STORE_URI is not configured. Cannot process request");
			throw new IllegalStateException("SMART_PASA_STORE_URI is not configured. Cannot process request");
		}
		LOGGER.debug("Seems like pasa is configured...");


		if (subscriberPasaFile.endsWith("/"))
			subscriberPasaFile += subscriberId.substring(subscriberId.length() - 2) + "/" + subscriberId + ".pasa";
		else
			subscriberPasaFile += "/" + subscriberId.substring(subscriberId.length() - 2) + "/" + subscriberId + ".pasa";
		LOGGER.debug("Subscribe Pasa File: " + subscriberPasaFile);

		SubscriberPasa subscriberPasa = this.fetchFromFile(subscriberPasaFile);
		if (subscriberPasa == null) {
			LOGGER.debug("seems like user has no pasa before... Allowing the transaction...");
			return true;
		}

		// Getting the offer instance with handles
		LOGGER.debug("Fetching the internal offerId & all handles from prodcat...");
		DiscoveryResponse discoveryResponse = new DiscoveryResponse();
		String requestCorrelator = SmartServiceResolver.getSubscriptionRequest().discoverOfferByFederatedId(UniqueIdGenerator.generateId(), pasaLoadID, subscriberId);
		RequestCorrelationStore.put(requestCorrelator, discoveryResponse);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			LOGGER.debug("Apparently interrupted in smeaphore... Mostly the request will fail!!");
		}
		semaphore.destroy();
		LOGGER.info("Check if response received...");
		discoveryResponse = (DiscoveryResponse) RequestCorrelationStore.remove(requestCorrelator);
		if (discoveryResponse != null && discoveryResponse.getFault() != null && discoveryResponse.getFault().getCode() > 0) {
			LOGGER.error("Failed fetching PasaName from handle: " + pasaLoadID + ", " + discoveryResponse.getFault());
			return false;
		}


		// Getting the balance and date from UCIP, and sum up the final pasa scope balance (sum of all wallets)
		LOGGER.debug("Fetching the subscriber balances...");
		List<Meta> metas = new ArrayList<Meta>();
		metas.add(new Meta("READ_SUBSCRIBER", "READ_BALANCES"));
		metas.add(new Meta("SUBSCRIBER_ID", subscriberId));
		metas.add(new Meta("msisdn", subscriberId));

		SubscriberInfo subscriberInfo = new SubscriberInfo();
		String requestId = SmartServiceResolver.getSubscriberRequest().readSubscriber(UniqueIdGenerator.generateId(), subscriberId, metas);
		SubscriberResponseStore.put(requestId, subscriberInfo);
		semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			LOGGER.debug("Apparently interrupted in smeaphore... Mostly the request will fail!!");
		}
		semaphore.destroy();

		LOGGER.info("Check if response received...");
		subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestCorrelator);
		if (subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() > 0){
			LOGGER.error("failed to get subscriber balances, cannot process further");
			return false;
		}
	
		

		// Business logic here....

		try {
			// identify the actual offer for pasa handle
			String offerId = discoveryResponse.getOffer().getName();
			LOGGER.debug("Getting PASA: " + offerId + " for handle: " + pasaLoadID);

			String pasaOffer = SefCoreServiceResolver.getConfigService().getValue("SMART_pasaLoad", offerId);
			if (pasaOffer == null) {
				LOGGER.debug("Cannot find restriction mapping in config for pasa: " + offerId);
				return false;
			} 
			int allowedCount = Integer.parseInt(pasaOffer);
			LOGGER.debug("Allowed PASA from config: " + allowedCount);

			// get relevant susbciber balance
			int subscriberBalance = this.getRelevantPasaBalance(subscriberInfo.getSubscriber().getMetas());

			// check what the user recived today
			int consumedAmount = subscriberPasa.getPasaAmount(offerId);
			LOGGER.debug("PASA in user account for today: " + consumedAmount);
			
			// check if real balance is available for sending....
			if (subscriberBalance - consumedAmount >= value) {
				LOGGER.debug("User has adequate balance!! Subscriber Balance: " + subscriberBalance + ", pasaToday: " + consumedAmount + ", Available Balance: " + (subscriberBalance - consumedAmount) + ", Required Value: " + value);
				return true;
			} else {
				LOGGER.debug("User DOES NOT have adequate balance!! Subscriber Balance: " + subscriberBalance + ", pasaToday: " + consumedAmount + ", Available Balance: " + (subscriberBalance - consumedAmount) + ", Required Value: " + value);
				return false;
			}
			
	
		} catch (Exception e) {
			LOGGER.error("Bad Configuration for Pasa(" + pasaLoadID + "). Cannot afford revenue exposures!!!", e);
			this.persistToFile(subscriberPasaFile, subscriberPasa);
			return false;
		}

	}

	
	public boolean isPasaReceiveAllowed(String subscriberId, String pasaLoadID) {
		String subscriberPasaFile = this.pasaServiceStoreLocation;

		LOGGER.debug("Entering get pasa function...");

		if (this.pasaServiceStoreLocation == null) {
			LOGGER.debug("SMART_PASA_STORE_URI is not configured. Cannot process request");
			throw new IllegalStateException("SMART_PASA_STORE_URI is not configured. Cannot process request");
		}
		LOGGER.debug("Seems like pasa is configured...");


		if (subscriberPasaFile.endsWith("/"))
			subscriberPasaFile += subscriberId.substring(subscriberId.length() - 2) + "/" + subscriberId + ".pasa";
		else
			subscriberPasaFile += "/" + subscriberId.substring(subscriberId.length() - 2) + "/" + subscriberId + ".pasa";
		LOGGER.debug("Subscribe Pasa File: " + subscriberPasaFile);

		SubscriberPasa subscriberPasa = this.fetchFromFile(subscriberPasaFile);
		if (subscriberPasa == null) {
			LOGGER.debug("seems like user has no pasa before... Allowing the transaction...");
			return true;
		}

		LOGGER.debug("Fetching the internal offerId & all handles from prodcat...");
		DiscoveryResponse discoveryResponse = new DiscoveryResponse();
		String requestCorrelator = SmartServiceResolver.getSubscriptionRequest().discoverOfferByFederatedId(UniqueIdGenerator.generateId(), pasaLoadID, subscriberId);
		RequestCorrelationStore.put(requestCorrelator, discoveryResponse);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			LOGGER.debug("Apparently interrupted in smeaphore... Mostly the request will fail!!");
		}
		semaphore.destroy();
		LOGGER.info("Check if response received...");
		discoveryResponse = (DiscoveryResponse) RequestCorrelationStore.remove(requestCorrelator);
		if (discoveryResponse != null && discoveryResponse.getFault() != null && discoveryResponse.getFault().getCode() > 0) {
			LOGGER.error("Failed fetching PasaName from handle: " + pasaLoadID + ", " + discoveryResponse.getFault());
			return false;
		}

		try {
			String offerId = discoveryResponse.getOffer().getName();
			LOGGER.debug("Getting PASA: " + offerId + " for handle: " + pasaLoadID);

			String pasaOffer = SefCoreServiceResolver.getConfigService().getValue("SMART_pasaLoad", offerId);
			if (pasaOffer == null) {
				LOGGER.debug("Cannot find restriction mapping in config for pasa: " + offerId);
				return false;
			} 

			int allowedCount = Integer.parseInt(pasaOffer);
			LOGGER.debug("Allowed PASA from config: " + allowedCount);

			int consumedCount = subscriberPasa.getPasaCount(offerId);
			LOGGER.debug("PASA in user account for today: " + consumedCount);
			if ((allowedCount != -1) && (consumedCount >= allowedCount)) {
				LOGGER.debug("User already exhausted pasa receive restriction");
				return false;
			}

			LOGGER.debug("User can avail the pasa. Proceed to refill...");
			return true;

		} catch (Exception e) {
			LOGGER.error("Bad Configuration for Pasa(" + pasaLoadID + "). Cannot afford revenue exposures!!!", e);
			this.persistToFile(subscriberPasaFile, subscriberPasa);
			return false;
		}
	}

	public boolean setPasaReceived(String subscriberId, String pasaLoadID, Integer value) {
		String subscriberPasaFile = this.pasaServiceStoreLocation;

		LOGGER.debug("Entering save pasa function...");

		if (this.pasaServiceStoreLocation == null) {
			LOGGER.debug("SMART_PASA_STORE_URI is not configured. Cannot process request");
			throw new IllegalStateException("SMART_PASA_STORE_URI is not configured. Cannot process request");
		}	
		LOGGER.debug("Seems like pasa is configured...");

		if (subscriberPasaFile.endsWith("/"))
			subscriberPasaFile += subscriberId.substring(subscriberId.length() - 2) + "/" + subscriberId + ".pasa";
		else
			subscriberPasaFile += "/" + subscriberId.substring(subscriberId.length() - 2) + "/" + subscriberId + ".pasa";
		LOGGER.debug("Subscribe Pasa File: " + subscriberPasaFile);

		SubscriberPasa subscriberPasa = this.fetchFromFile(subscriberPasaFile);
		if (subscriberPasa == null) {
			LOGGER.debug("User has not previous pasa. Creating one noew...");
			subscriberPasa = new SubscriberPasa();
		}

		LOGGER.debug("Fetching the internal offerId & all handles from prodcat...");
		DiscoveryResponse discoveryResponse = new DiscoveryResponse();
		String requestCorrelator = SmartServiceResolver.getSubscriptionRequest().discoverOfferByFederatedId(UniqueIdGenerator.generateId(), pasaLoadID, subscriberId);
		RequestCorrelationStore.put(requestCorrelator, discoveryResponse);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestCorrelator);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			LOGGER.debug("Apparently interrupted in smeaphore... Mostly the request will fail!!");
		}
		semaphore.destroy();
		LOGGER.info("Check if response received...");
		discoveryResponse = (DiscoveryResponse) RequestCorrelationStore.remove(requestCorrelator);
		if (discoveryResponse != null && discoveryResponse.getFault() != null && discoveryResponse.getFault().getCode() > 0) {
			LOGGER.error("Failed fetching PasaName from handle: " + pasaLoadID + ", " + discoveryResponse.getFault());
			return false;
		}

		try {
			String offerId = discoveryResponse.getOffer().getName();
			LOGGER.debug("Getting PASA: " + offerId + " for handle: " + pasaLoadID);

			int consumedAmount = subscriberPasa.getPasaAmount(offerId);
			LOGGER.debug("User consumption for today: " + consumedAmount + ", storing consumption for this event: " + value);
			subscriberPasa.setPasaReceived(offerId, value);

			this.persistToFile(subscriberPasaFile, subscriberPasa);
			LOGGER.debug("Pasa setup for the user...");
			return true;

		} catch (Exception e) {
			LOGGER.error("Bad Configuration for Pasa(" + pasaLoadID + "). Cannot afford revenue exposures!!!", e);
			return false;
		}
	}

	private Integer getRelevantPasaBalance(Map<String, String> metas) {
		Map<Integer, DAInfo> daList = new HashMap<Integer, DAInfo>();
		Map<Integer, OfferInfo> offerList = new HashMap<Integer, OfferInfo>();
		for (String key : metas.keySet()) {

			if (key.startsWith("DA")) {
				LOGGER.debug("CIC:: meta: " + key + " = " + metas.get(key));
				String daForm = metas.get(key);
				LOGGER.debug("Check before split: " + daForm);
				String daPart[] = daForm.split(":+:");
				LOGGER.debug("DA Parts: " + daPart.length);
				int i=0; for (String part: daPart) {
					LOGGER.debug("daPart[" + i++ +"]" + part);
				}

				// first part is main DA...
				LOGGER.debug("Main DA: " + daPart[0]);
				String mainDaElements[] = daPart[0].split(",");
				int daID = Integer.parseInt(mainDaElements[0]);
				String daVal1 = mainDaElements[1];
				LOGGER.debug("daId: "+daID+", daVal1: "+daVal1);
				String daVal2 = mainDaElements[2];
				Long startDate =("null".equals(mainDaElements[3]))?null: Long.parseLong(mainDaElements[3]);
				Long expiryDate = (("null").equals(mainDaElements[4]))?null:Long.parseLong(mainDaElements[4]);
				Integer pamServiceID = (("null").equals(mainDaElements[5]))?null:Integer.parseInt(mainDaElements[5]);
				Integer offerID = (("null").equals(mainDaElements[6]))?null:Integer.parseInt(mainDaElements[6]);
				Integer productID = (("null").equals(mainDaElements[7]))?null:Integer.parseInt(mainDaElements[7]);
				Boolean isRealMoney = (("null").equals(mainDaElements[8]))?null:Boolean.parseBoolean(mainDaElements[8]);
				Long closestExpiryDate = (("null").equals(mainDaElements[9]))?null:Long.parseLong(mainDaElements[9]);
				String closestExpiryValue1 = (("null").equals(mainDaElements[10]))?null:mainDaElements[10];
				String closestExpiryValue2 = (("null").equals(mainDaElements[11]))?null:mainDaElements[11];
				Long closestAccessibleDate = (("null".equals(mainDaElements[12])))?null:Long.parseLong(mainDaElements[12]);
				String closestAccessibleValue1 = (("null").equals(mainDaElements[13]))?null:mainDaElements[13];
				String closestAccessibleValue2 = (("null").equals(mainDaElements[14]))?null:mainDaElements[14];
				String daActiveValue1 = (("null").equals(mainDaElements[15]))?null:mainDaElements[15];
				String daActiveValue2 = (("null").equals(mainDaElements[16]))?null:mainDaElements[16];
				Integer daUnitType = (("null").equals(mainDaElements[17]))?null:Integer.parseInt(mainDaElements[17]);
				Boolean isCompositeDAFlag = (("null").equals(mainDaElements[18]))?null:Boolean.parseBoolean(mainDaElements[18]);

				DAInfo daInfo = new DAInfo(daID, daVal1, daVal2, startDate, expiryDate, pamServiceID, offerID, productID, isRealMoney, 
						closestExpiryDate, closestExpiryValue1, closestExpiryValue2, closestAccessibleDate, closestAccessibleValue1, 
						closestAccessibleValue2, daActiveValue1, daActiveValue2, daUnitType, isCompositeDAFlag);

				// second part is subDA list...
				if (daPart.length > 1) {
					LOGGER.debug("All SubDA list: " + daPart[1]);
					String subDAs[] = daPart[1].split("|||");
					LOGGER.debug("Number of SubDA: " + subDAs.length);
					for (String subDAForm: subDAs) {
						String subDA[] = subDAForm.split(",");
						String subDAValue1 = subDA[0];
						String subDAValue2 = subDA[1];
						Long subDAStartDate = Long.parseLong(((subDA[2].equals("null"))?null:subDA[2]));
						Long subDAExpiryDate = Long.parseLong(((subDA[3].equals("null"))?null:subDA[3]));

						SubDAInfo subDAInfo = new SubDAInfo(subDAValue1, subDAValue2, subDAStartDate, subDAExpiryDate);
						daInfo.addSubDA(subDAInfo);
						LOGGER.debug("Added SubDA: " + subDAInfo);
					}
				} else {
					LOGGER.debug("Sub DAs does not seem to available to extract...");
				}
				daList.put(daID, daInfo);
				LOGGER.debug("Packed DA: " + daInfo);
			} // end of if for DA handling 
			if (key.startsWith("OFFER_INFO")) {
				LOGGER.debug("FLEXI:: OFFER_ID...." + metas.get(key));
				String offerForm = metas.get(key);
				LOGGER.debug("Check before split - offerForm: " + offerForm);
				String offerParts[] = offerForm.split(",");
				LOGGER.debug("Offer Parts: " + offerParts.length);
				int i= 0; for (String part: offerParts) {
					LOGGER.debug("OfferPart[" + i++ + "] :=" + part);
				}
				String offerId = offerParts[0];
				String start = offerParts[1];
				if (start.equals("null"))
					start = offerParts[2];
				String expiry = offerParts[3];
				if (expiry.equals("null"))
					expiry = offerParts[4];

				String daID = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", offerId);
				String walletName = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", offerId);

				int offerID = Integer.parseInt(offerId);
				if (offerID == 1 || offerID == 2 || offerID == 4 || offerID == 1241 || (offerID >= 7000 && offerID <= 9999)) {
					LOGGER.debug("FLEXI:: Offer listed in omission case. Ignoring...");
					continue;
				}

				if (daID == null) {
					LOGGER.debug("OfferID: " + offerId + " is not configure with a DA. Ignoring...");
					continue;
				}
				OfferInfo oInfo = new OfferInfo(offerID, Long.parseLong(expiry), Long.parseLong(start), daID, walletName);
				offerList.put(offerID, oInfo);

			}
		} // end of for loop... 
		LOGGER.debug("Size of offerList "+offerList.size()+" offerList.keyset: "+offerList.keySet());
		LOGGER.debug("Size of daList "+daList.size()+" daList.keyset: "+daList.keySet());
		
		
		// TODO: as per navneet, only DA:1 is checked... potentially can fail when CS BC is defined without coordinating with IL.
		String daValue = daList.get(1).daVal1;
		if (daValue == null)
			return 0;
		else 
			return Integer.parseInt(daValue);
	}


	
	private SubscriberPasa fetchFromFile(String subscriberPasaFile) {
		try {
			FileInputStream fisPasa = new FileInputStream(subscriberPasaFile);
			ObjectInputStream oisPasa = new ObjectInputStream(fisPasa);
			SubscriberPasa pasa = (SubscriberPasa) oisPasa.readObject();

			LOGGER.debug("Pasa Content fetched: " + pasa);

			oisPasa.close();
			fisPasa.close();

			LOGGER.debug("Fetched Pasa file...");
			return pasa;

		} catch (FileNotFoundException e) {
			LOGGER.debug("Seems like subscriber has no pasa info... URI: " + subscriberPasaFile);
		} catch (IOException e) {
			LOGGER.debug("Error loading file: " + subscriberPasaFile, e);
		} catch (ClassNotFoundException e) {
			LOGGER.debug("Build issue!! Template missing for: " + e.getMessage(), e);
		}

		return null;
	}

	private boolean persistToFile(String subscriberPasaFile, SubscriberPasa pasa) {
		try {
			FileOutputStream fosPasa = new FileOutputStream(subscriberPasaFile);
			ObjectOutputStream oosPasa = new ObjectOutputStream(fosPasa);

			LOGGER.debug("Pasa Content to Save: " + pasa);
			oosPasa.writeObject(pasa);

			oosPasa.close();
			fosPasa.close();

			LOGGER.debug("Saved the pasa file: " + subscriberPasaFile);
			return true;

		} catch (FileNotFoundException e) {
			LOGGER.debug("Seems like subscriber has no pasa info... URI: " + subscriberPasaFile);
		} catch (IOException e) {
			LOGGER.debug("Error loading file: " + subscriberPasaFile, e);
		}
		return false;
	}





	class SubDAInfo {
		String subDAValue1;
		String subDAValue2;
		Long subDAStartDate;
		Long subDAExpiryDate;

		public SubDAInfo(String subDAValue1, String subDAValue2, Long subDAStartDate, Long subDAExpiryDate) {
			this.subDAValue1 = subDAValue1;
			this.subDAValue2 = subDAValue2;
			this.subDAStartDate = subDAStartDate;
			this.subDAExpiryDate = subDAExpiryDate;
		}

		@Override
		public String toString() {
			return "SubDAInfo [subDAValue1=" + subDAValue1 + ", subDAValue2=" + subDAValue2 + ", subDAStartDate=" + subDAStartDate
					+ ", subDAExpiryDate=" + subDAExpiryDate + "]";
		}

	}

	class DAInfo {
		Integer daID;
		String daVal1;
		String daVal2;
		Long startDate;
		Long expiryDate;
		Integer pamServiceID;
		Integer offerID;
		Integer productID;
		Boolean isRealMoney;
		Long closestExpiryDate;
		String closestExpiryValue1;
		String closestExpiryValue2;
		Long closestAccessibleDate;
		String closestAccessibleValue1;
		String closestAccessibleValue2;
		String daActiveValue1;
		String daActiveValue2;
		Integer daUnitType;
		Boolean isComposite;
		List<SubDAInfo> subDAList;


		public DAInfo(Integer daID, String daVal1, String daVal2, Long startDate, Long expiryDate, Integer pamServiceID, Integer offerID,
				Integer productID, Boolean isRealMoney, Long closestExpiryDate, String closestExpiryValue1, String closestExpiryValue2,
				Long closestAccessibleDate, String closestAccessibleValue1, String closestAccessibleValue2, String daActiveValue1,
				String daActiveValue2, Integer daUnitType, Boolean isComposite) {
			this.daID = daID;
			this.daVal1 = daVal1;
			this.daVal2 = daVal2;
			this.startDate = startDate;
			this.expiryDate = expiryDate;
			this.pamServiceID = pamServiceID;
			this.offerID = offerID;
			this.productID = productID;
			this.isRealMoney = isRealMoney;
			this.closestExpiryDate = closestExpiryDate;
			this.closestExpiryValue1 = closestExpiryValue1;
			this.closestExpiryValue2 = closestExpiryValue2;
			this.closestAccessibleDate = closestAccessibleDate;
			this.closestAccessibleValue1 = closestAccessibleValue1;
			this.closestAccessibleValue2 = closestAccessibleValue2;
			this.daActiveValue1 = daActiveValue1;
			this.daActiveValue2 = daActiveValue2;
			this.daUnitType = daUnitType;
			this.isComposite = isComposite;
		}

		public void addSubDA(SubDAInfo subDA) {
			if (this.subDAList == null)
				this.subDAList = new ArrayList<SubDAInfo>();
			this.subDAList.add(subDA);
		}

		@Override
		public String toString() {
			return "DAInfo [daID=" + daID + ", daVal1=" + daVal1 + ", daVal2=" + daVal2 + ", startDate=" + startDate + ", expiryDate="
					+ expiryDate + ", pamServiceID=" + pamServiceID + ", offerID=" + offerID + ", productID=" + productID
					+ ", isRealMoney=" + isRealMoney + ", closestExpiryDate=" + closestExpiryDate + ", closestExpiryValue1="
					+ closestExpiryValue1 + ", closestExpiryValue2=" + closestExpiryValue2 + ", closestAccessibleDate="
					+ closestAccessibleDate + ", closestAccessibleValue1=" + closestAccessibleValue1 + ", closestAccessibleValue2="
					+ closestAccessibleValue2 + ", daActiveValue1=" + daActiveValue1 + ", daActiveValue2=" + daActiveValue2
					+ ", daUnitType=" + daUnitType + ", isComposite=" + isComposite + "]";
		}
	}

	class OfferInfo {
		private Integer offerID;
		private long offerExpiry;
		private long offerStart;
		private String daID;
		private String walletName;

		public OfferInfo() {}

		public OfferInfo(Integer offerID, long offerExpiry, long offerStart, String daID, String walletName) {
			super();
			this.offerID = offerID;
			this.offerExpiry = offerExpiry;
			this.offerStart = offerStart;
			this.daID = daID;
			this.walletName = walletName;
		}


		@Override
		public String toString() {
			return "OfferInfo [offerID=" + offerID + ", offerExpiry=" + offerExpiry + ", daID=" + daID + ", walletName=" + walletName + "]";
		}


	}


}
