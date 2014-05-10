package com.ericsson.raso.sef.client.air.request;

public class MessageCapabiity extends NativeAirRequest{

	private boolean promotionNotificationFlag;
	private boolean firstIVRCallSetFlag;
	private boolean accountActivationFlag;
	
	public boolean isPromotionNotificationFlag() {
		return promotionNotificationFlag;
	}
	public void setPromotionNotificationFlag(boolean promotionNotificationFlag) {
		this.promotionNotificationFlag = promotionNotificationFlag;
		addParam("promotionNotificationFlag",this.promotionNotificationFlag);
	}
	public boolean isFirstIVRCallSetFlag() {
		return firstIVRCallSetFlag;
	}
	public void setFirstIVRCallSetFlag(boolean firstIVRCallSetFlag) {
		this.firstIVRCallSetFlag = firstIVRCallSetFlag;
		addParam("firstIVRCallSetFlag",this.firstIVRCallSetFlag);
	}
	public boolean isAccountActivationFlag() {
		return accountActivationFlag;
	}
	public void setAccountActivationFlag(boolean accountActivationFlag) {
		this.accountActivationFlag = accountActivationFlag;
		addParam("accountActivationFlag",this.accountActivationFlag);
	}
	
}
