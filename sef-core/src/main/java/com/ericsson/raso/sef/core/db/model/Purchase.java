package com.ericsson.raso.sef.core.db.model;

import org.joda.time.DateTime;

public class Purchase {

	private String eventId;// unique key mapped to commerce trial
	private String purchaseId; // unique key

	private String recurrentPurchaseId;
	private CurrencyCode currencyCode;
	private DateTime purchaseCreated;
	private PurchaseState purchaseState;
	private String chargedUserId;
	private PurchaseType purchaseType;
	private String productId;
	private String productDescription;
	private String price;
	private String vatPrice;
	private String vatCode;
	private boolean recurrence;
	private String recurrentProductId;
	private boolean gift;
	private DateTime expiryTime;

	public String getEventId() {
		return eventId;
	}

	public String getPurchaseId() {
		return purchaseId;
	}

	public String getRecurrentPurchaseId() {
		return recurrentPurchaseId;
	}

	public CurrencyCode getCurrencyCode() {
		return currencyCode;
	}

	public DateTime getPurchaseCreated() {
		return purchaseCreated;
	}

	public PurchaseState getPurchaseState() {
		return purchaseState;
	}

	public String getChargedUserId() {
		return chargedUserId;
	}

	public PurchaseType getPurchaseType() {
		return purchaseType;
	}

	public String getProductId() {
		return productId;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public String getPrice() {
		return price;
	}

	public String getVatPrice() {
		return vatPrice;
	}

	public String getVatCode() {
		return vatCode;
	}

	public boolean getRecurrence() {
		return recurrence;
	}

	public String getRecurrentProductId() {
		return recurrentProductId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}

	public void setRecurrentPurchaseId(String recurrentPurchaseId) {
		this.recurrentPurchaseId = recurrentPurchaseId;
	}

	public void setCurrencyCode(CurrencyCode currencyCode) {
		this.currencyCode = currencyCode;
	}

	public void setPurchaseCreated(DateTime purchaseCreated) {
		this.purchaseCreated = purchaseCreated;
	}

	public void setPurchaseState(PurchaseState purchaseState) {
		this.purchaseState = purchaseState;
	}

	public void setChargedUserId(String chargedUserId) {
		this.chargedUserId = chargedUserId;
	}

	public void setPurchaseType(PurchaseType purchaseType) {
		this.purchaseType = purchaseType;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setVatPrice(String vatPrice) {
		this.vatPrice = vatPrice;
	}

	public void setVatCode(String vatCode) {
		this.vatCode = vatCode;
	}

	public void setRecurrence(boolean recurrence) {
		this.recurrence = recurrence;
	}

	public void setRecurrentProductId(String recurrentProductId) {
		this.recurrentProductId = recurrentProductId;
	}

	public boolean isGift() {
		return gift;
	}

	public void setGift(boolean gift) {
		this.gift = gift;
	}

	public DateTime getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(DateTime expiryTime) {
		this.expiryTime = expiryTime;
	}
}
