package com.ericsson.raso.sef.smart.commons;

public class SmartConstants {
	
	private SmartConstants() {
		
	}
	
	
	//SOAP Interface constants - used in interface unmarshaling
	
	public static final String EVENT_CLASS = "EventClass";
	public static final String RESOURCE_ID = "resourceId";
	public static final String PRODUCT_ID = "productId";
	public static final String EXPIRY_DATE = "expiryDate";
	public static final String VALIDITY_LEFT = "validityLeft";
	public static final String EXPIRY_DAYS_OF_EXTENSION = "expiryDaysOfExtension";
	public static final String EXPIRY_POLICY = "expiryPolicy";
	public static final String CHARGED_USER_ID = "chargedUserId";
	public static final String PURCHASE_TYPE = "purchaseType";
	public static final String notificationMessage = "notificationMessage";
	public static final String GRACE_PERIOD = "gracePeriod";
	public static final String USECASE = "usecase";
	
	public static final String PREACTIVE_ENDDATE = "preActiveEndDate";
	public static final String RESPONSE_PREACTIVE_ENDDATE = "PreActiveEndDate";
	public static final String GRACE_ENDDATE = "graceEndDate";
	public static final String RESPONSE_GRACE_ENDDATE = "GraceEndDate";
	public static final String DATE_FORMAT = "dateFormat";
	public static final String SUBSCRIBER = "subscriber";
	public static final String PREACTIVE_PERIOD = "preActivePeriod";
	public static final String MILLISEC_MULTIPLIER = "milliSecMultiplier";
	public static final String CREDENTIALS = "userCredantials";
	
	public static final int AIRTIME_DA_ID = 1;
	public static final int AIRTIME_OFFER_ID = 1001;
	public static final int ALKANSYA_OFFER_ID = 1241;
	public static final int ALKANSYA_DA_ID = 241;
	public static final int GRACE_ACC_OFFER_ID = 1;
	public static final int GRACE_TIMER_OFFER_ID = 2;
	public static final int UNLI_DA_ID = 242;
	public static final int UNLI_OFFER_START_ID = 2001;
	
	public static final String OfferConversionFector = "walletConversionFactor";
	
	public static final long CS_LONGEST_EXPTIME = 2114351999000l;

}
