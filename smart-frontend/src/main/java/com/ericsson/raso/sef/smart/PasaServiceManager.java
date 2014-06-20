package com.ericsson.raso.sef.smart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.smart.subscription.response.DiscoveryResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.sef.bes.api.entities.Offer;
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

	public boolean isPasaReceiveAllowed(String subscriberId, String pasaLoadID) {
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

		}
		LOGGER.info("Check if response received...");
		discoveryResponse = (DiscoveryResponse) RequestCorrelationStore.remove(requestCorrelator);
		if (discoveryResponse != null && discoveryResponse.getFault() != null && discoveryResponse.getFault().getCode() > 0) {
			LOGGER.error("Failed fetching PasaName from handle: " + pasaLoadID + ", " + discoveryResponse.getFault());
			return false;
		}

		try {
			String offerId = discoveryResponse.getOffer().getName();
			LOGGER.debug("Getting PASA: " + offerId);
			int allowedCount = Integer.parseInt(SefCoreServiceResolver.getConfigService().getValue("SMART_pasaLoad", offerId));
			int consumedCount = subscriberPasa.getPasaCount(offerId);
			if ((allowedCount != -1) && (consumedCount >= allowedCount)) {
				LOGGER.debug("User already exhausted pasa receive restriction");
				return false;
			}
			
			LOGGER.debug("User can avail the pasa. Proceed to refill...");
			return true;

		} catch (Exception e) {
			LOGGER.error("Bad Configuration for Pasa(" + pasaLoadID + "). Cannot afford revenue exposures!!!");
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

		}
		LOGGER.info("Check if response received...");
		discoveryResponse = (DiscoveryResponse) RequestCorrelationStore.remove(requestCorrelator);
		if (discoveryResponse != null && discoveryResponse.getFault() != null && discoveryResponse.getFault().getCode() > 0) {
			LOGGER.error("Failed fetching PasaName from handle: " + pasaLoadID + ", " + discoveryResponse.getFault());
			return false;
		}

		try {
			String offerId = discoveryResponse.getOffer().getName();
			LOGGER.debug("Setting PASA: " + offerId);
			int consumedAmount = subscriberPasa.getPasaAmount(offerId);
			subscriberPasa.setPasaReceived(pasaLoadID, (value + consumedAmount));

			this.persistToFile(subscriberPasaFile, subscriberPasa);

			return true;

		} catch (Exception e) {
			LOGGER.error("Bad Configuration for Pasa(" + pasaLoadID + "). Cannot afford revenue exposures!!!");
			return false;
		}
	}


	private SubscriberPasa fetchFromFile(String subscriberPasaFile) {
		try {
			FileInputStream fisPasa = new FileInputStream(subscriberPasaFile);
			ObjectInputStream oisPasa = new ObjectInputStream(fisPasa);
			SubscriberPasa pasa = (SubscriberPasa) oisPasa.readObject();

			oisPasa.close();
			fisPasa.close();

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

			oosPasa.writeObject(pasa);

			oosPasa.close();
			fosPasa.close();

			return true;

		} catch (FileNotFoundException e) {
			LOGGER.debug("Seems like subscriber has no pasa info... URI: " + subscriberPasaFile);
		} catch (IOException e) {
			LOGGER.debug("Error loading file: " + subscriberPasaFile, e);
		}
		return false;
	}


	class SubscriberPasa implements Serializable {
		private static final long	serialVersionUID	= 7366456574101630585L;

		private Map<String, Map<String, Integer>> pasaReceivedCount = new HashMap<String, Map<String,Integer>>();
		private Map<String, Map<String, Integer>> pasaReceivedAmount = new HashMap<String, Map<String,Integer>>();


		public int getPasaCount(String pasaID) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String relevantKey = format.format(new Date());

			this.pruneObsoleteData();

			// now get to do what you came here for...
			Map<String, Integer> pasaToday = this.pasaReceivedCount.get(relevantKey);
			if (pasaToday == null) {
				pasaToday = new HashMap<String, Integer>();
			}

			Integer pasaCount = pasaToday.get(pasaID);
			if (pasaCount == null) 
				return 0;
			else 
				return pasaCount;
		}

		public int getPasaAmount(String pasaID) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String relevantKey = format.format(new Date());

			this.pruneObsoleteData();

			// now get to do what you came here for...
			Map<String, Integer> pasaToday = this.pasaReceivedAmount.get(relevantKey);
			if (pasaToday == null) {
				pasaToday = new HashMap<String, Integer>();
			}

			Integer pasaAmount = pasaToday.get(pasaID);
			if (pasaAmount == null) 
				return 0;
			else 
				return pasaAmount;
		}

		public boolean setPasaReceived(String pasaID, Integer value) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String relevantKey = format.format(new Date());

			this.pruneObsoleteData();

			// now get to do what you came here for...
			LOGGER.debug("Updating pasa count...");
			Map<String, Integer> pasaCountToday = this.pasaReceivedCount.get(relevantKey);
			if (pasaCountToday == null) {
				pasaCountToday = new HashMap<String, Integer>();
			}

			Integer pasaCount = pasaCountToday.get(pasaID);
			if (pasaCount == null) 
				pasaCountToday.put(relevantKey, 1);
			else 
				pasaCountToday.put(relevantKey, pasaCount + 1);

			LOGGER.debug("Updating pasa amount...");
			Map<String, Integer> pasaAmountToday = this.pasaReceivedAmount.get(relevantKey);
			if (pasaAmountToday == null) {
				pasaAmountToday = new HashMap<String, Integer>();
			}

			Integer pasaAmount = pasaAmountToday.get(pasaID);
			if (pasaAmount == null) 
				pasaAmountToday.put(relevantKey, 1);
			else 
				pasaAmountToday.put(relevantKey, (pasaAmount + value));

			return true;
		}

		private void pruneObsoleteData() {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String relevantKey = format.format(new Date());

			//prune out old and obsolete data...
			for (String key: this.pasaReceivedCount.keySet()) {
				if (!key.equals(relevantKey)) {
					this.pasaReceivedCount.remove(key);
				}
			}

			for (String key: this.pasaReceivedAmount.keySet()) {
				if (!key.equals(relevantKey)) {
					this.pasaReceivedAmount.remove(key);
				}
			}



		}
	}
}
