package com.ericsson.raso.sef.core;

public class Constants {

	
	//UCIP Specific - Constants used to pass ucip specific information to flow through from north bound interfaces
	
	public static final String TX_TYPE = "transcationType";
	public static final String TX_CODE = "transcationCode";
	public static final String TX_AMOUNT = "transactionAmount";
	public static final String CHANNEL_NAME = "channelName";
	public static final String EX_DATA1 = "externalData1";
	public static final String EX_DATA2 = "externalData2";
	public static final String EX_DATA3 = "externalData3";
	public static final String TRANSPARENT_META1 = "transparentMeta1";
	public static final String TRANSPARENT_META2 = "transparentMeta2";
	public static final String TRANSPARENT_META3 = "transparentMeta3";
	
	
	//Flattened out metas returned from back end for subscriber profile
	
	public static final String READ_SUBSCRIBER_ACTIVATION_DATE = "READ_SUBSCRIBER_ACTIVATION_DATE";
	public static final String READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE = "READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE";
	public static final String READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE = "READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE";
	public static final String READ_SUBSCRIBER_SERVICE_OFFERING_ID = "READ_SUBSCRIBER_SERVICE_OFFERING_ID";
	public static final String READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG = "READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG";
	public static final String READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG = "READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG";
	public static final String READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG = "READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG";
	public static final String READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG = "READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG";
	public static final String READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG = "READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG";
	public static final String READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG = "READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG";
	public static final String READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG = "READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG";
	public static final String READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG = "READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG";
	public static final String READ_SUBSCRIBER_OFFER_INFO_OFFER_ID = "READ_SUBSCRIBER_OFFER_ID";
	public static final String READ_SUBSCRIBER_OFFER_INFO_START_DATE = "READ_SUBSCRIBER_START_DATE";
	public static final String READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE = "READ_SUBSCRIBER_EXPIRY_DATE";
	public static final String READ_SUBSCRIBER_OFFER_INFO_START_DATE_TIME = "READ_SUBSCRIBER_START_DATE_TIME";
	public static final String READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME = "READ_SUBSCRIBER_EXPIRY_DATE_TIME";
}
