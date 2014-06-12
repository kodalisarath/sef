package com.ericsson.raso.sef.client.air.request;

public class OfferSelection extends NativeAirRequest {
	private static final long serialVersionUID = 8388331634098443476L;

	private int offerIDFirst;
	private int offerIDLast;

	public int getOfferIDFirst() {
		return offerIDFirst;
	}

	public void setOfferIDFirst(int offerIDFirst) {
		this.offerIDFirst = offerIDFirst;
		addParam("offerIDFirst", offerIDFirst);
	}

	public int getOfferIDLast() {
		return offerIDLast;
	}

	public void setOfferIDLast(int offerIDLast) {
		this.offerIDLast = offerIDLast;
		addParam("offerIDLast", offerIDLast);
	}
}
