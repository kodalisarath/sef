package com.ericsson.raso.sef.smart.commons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.Property;

public class WalletOfferMappingHelper {
	
	private static WalletOfferMappingHelper instance = null;
	
	private Map<String, WalletOfferMapping> walletMappings = new HashMap<String, WalletOfferMapping>();
	private Map<String, WalletOfferMapping> offerMappings = new HashMap<String, WalletOfferMapping>();
	
	
	public WalletOfferMappingHelper() {
		List<Property> properties = SefCoreServiceResolver.getConfigService().getSection("GLOBAL_walletMapping").getProperty();
		for (Property property: properties) { 
			this.offerMappings.put(property.getKey(), new WalletOfferMapping(property.getKey(), property.getValue()));
			this.offerMappings.put(property.getValue(), new WalletOfferMapping(property.getKey(), property.getValue()));
		}
	}
	
	public static synchronized WalletOfferMappingHelper getInstance() {
		if (instance == null) {
			instance = new WalletOfferMappingHelper();
		}
		return instance;
	}
	
	public String getWallet(String offerID) {
		WalletOfferMapping mapping = this.offerMappings.get(offerID);
		if (mapping != null)
			return mapping.getWalletName();
		return null;
	}

	public WalletOfferMapping getWalletMapping(String offerID) {
		WalletOfferMapping mapping = this.offerMappings.get(offerID);
		return mapping;
	}



	public String getOffer(String walletName) {
		WalletOfferMapping mapping = this.walletMappings.get(walletName);
		if (mapping != null)
			return mapping.getOfferID();
		return null;
	}

	public WalletOfferMapping getOfferMapping(String walletName) {
		WalletOfferMapping mapping = this.walletMappings.get(walletName);
		return mapping;
	}


}
