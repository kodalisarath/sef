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
			subscriberPasa.setPasaReceived(pasaLoadID, (value + consumedAmount));

			this.persistToFile(subscriberPasaFile, subscriberPasa);
			LOGGER.debug("Pasa setup for the user...");
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


	
}
