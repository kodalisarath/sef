package com.ericsson.raso.sef.smart.commons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.Property;

public class WalletOfferMappingHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(WalletOfferMappingHelper.class);
	
	private static WalletOfferMappingHelper instance = null;
	
	private Map<String, WalletOfferMapping> walletMappings = new HashMap<String, WalletOfferMapping>();
	private Map<String, WalletOfferMapping> offerMappings = new HashMap<String, WalletOfferMapping>();
	
	
	public WalletOfferMappingHelper() {
		List<Property> properties = SefCoreServiceResolver.getConfigService().getSection("GLOBAL_walletMapping").getProperty();
		for (Property property: properties) { 
			this.offerMappings.put(property.getKey(), new WalletOfferMapping(property.getKey(), property.getValue()));
			this.walletMappings.put(property.getValue(), new WalletOfferMapping(property.getKey(), property.getValue()));
		}
		LOGGER.debug("Loaded Wallet Mapping: " + this.walletMappings.size() + ", Offer Mapping: " + this.offerMappings.size());
	}
	
	public static synchronized WalletOfferMappingHelper getInstance() {
		if (instance == null) {
			LOGGER.debug("Singleton created for WalletOfferMappingHelper");
			instance = new WalletOfferMappingHelper();
		}
		return instance;
	}
	
	public String getWallet(String offerID) {
		WalletOfferMapping mapping = this.offerMappings.get(offerID);
		LOGGER.debug("Check Wallet Mapping for offerID: " + offerID + ":=" + mapping);
		if (mapping != null)
			return mapping.getWalletName();
		return null;
	}

	public WalletOfferMapping getWalletMapping(String offerID) {
		WalletOfferMapping mapping = this.offerMappings.get(offerID);
		LOGGER.debug("Check Wallet Mapping for offerID: " + offerID + ":=" + mapping);
		return mapping;
	}



	public String getOffer(String walletName) {
		WalletOfferMapping mapping = this.walletMappings.get(walletName);
		LOGGER.debug("Check Wallet Mapping for walletName: " + walletName + ":=" + mapping);
		if (mapping != null)
			return mapping.getOfferID();
		return null;
	}

	public WalletOfferMapping getOfferMapping(String walletName) {
		WalletOfferMapping mapping = this.walletMappings.get(walletName);
		LOGGER.debug("Check Wallet Mapping for walletName: " + walletName + ":=" + mapping);
		return mapping;
	}


}
