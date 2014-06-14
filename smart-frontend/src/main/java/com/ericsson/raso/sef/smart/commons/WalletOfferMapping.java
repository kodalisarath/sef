package com.ericsson.raso.sef.smart.commons;

public final class WalletOfferMapping {
	private String offerID;
	private String walletName;
	
	
	public WalletOfferMapping(String offerID, String walletName) {
		super();
		this.offerID = offerID;
		this.walletName = walletName;
	}
	
	public String getOfferID() {
		return offerID;
	}
	
	public void setOfferID(String offerID) {
		this.offerID = offerID;
	}
	
	public String getWalletName() {
		return walletName;
	}
	
	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}
	
}
