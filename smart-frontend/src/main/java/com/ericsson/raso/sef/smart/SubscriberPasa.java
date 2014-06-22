package com.ericsson.raso.sef.smart;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriberPasa implements Serializable {
	private static final long	serialVersionUID	= 7366456574101630585L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberPasa.class);

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
		LOGGER.debug("Updating pasa...key: " + relevantKey + ", pasa: " + pasaID);
		Map<String, Integer> pasaCountToday = this.pasaReceivedCount.get(relevantKey);
		if (pasaCountToday == null) {
			pasaCountToday = new HashMap<String, Integer>();
		}

		Integer pasaCount = pasaCountToday.get(pasaID);
		LOGGER.debug("Updating pasa count...key: " + relevantKey + ", pasacount: " + (pasaCount+1) );
		if (pasaCount == null) 
			pasaCountToday.put(relevantKey, 1);
		else 
			pasaCountToday.put(relevantKey, ++pasaCount);

		Map<String, Integer> pasaAmountToday = this.pasaReceivedAmount.get(relevantKey);
		if (pasaAmountToday == null) {
			pasaAmountToday = new HashMap<String, Integer>();
		}

		Integer pasaAmount = pasaAmountToday.get(pasaID);
		LOGGER.debug("Updating pasa count...key: " + relevantKey + ", pasaamount: " + (pasaAmount + value));
		if (pasaAmount == null) 
			pasaAmountToday.put(relevantKey, 1);
		else 
			pasaAmountToday.put(relevantKey, (pasaAmount + value));
		LOGGER.debug("Updated pasa info... Will persist...");
		return true;
	}

	private void pruneObsoleteData() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String relevantKey = format.format(new Date());
		LOGGER.debug("relevant Key: " + relevantKey);
		
		//prune out old and obsolete data...
		for (String key: this.pasaReceivedCount.keySet()) {
			LOGGER.debug("Checking pasacount for obsolete key: " + key);
			if (!key.equals(relevantKey)) {
				LOGGER.debug("Pruning pasacount obsolete key: " + key);
				this.pasaReceivedCount.remove(key);
			}
		}

		for (String key: this.pasaReceivedAmount.keySet()) {
			LOGGER.debug("Checking pasaamount for obsolete key: " + key);
			if (!key.equals(relevantKey)) {
				LOGGER.debug("Pruning pasaamount obsolete key: " + key);
				this.pasaReceivedAmount.remove(key);
			}
		}
	}

	@Override
	public String toString() {
		return "SubscriberPasa [pasaReceivedCount=" + pasaReceivedCount + ", pasaReceivedAmount=" + pasaReceivedAmount + "]";
	}

	
}