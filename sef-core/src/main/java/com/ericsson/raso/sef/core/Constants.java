package com.ericsson.raso.sef.core;

public class Constants {

	// UCIP Specific - Constants used to pass ucip specific information to flow
	// through from north bound interfaces

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
	// TO:DO remove after fixing an issue with Hazlecast
	public static final String SMFE_TXE_CORRELLATOR = "CorrelationStore";
	
	
	//Constants to deferrentiate use cases
	
	public static final String ModifyCustomerGrace = "ModifyCustomerGrace";
	public static final String ModifyCustomerPreActive = "ModifyCustomerPreActive";
	public static final String CreateOrWriteROP = "CreateOrWriteROP";
	public static final String VersionCreateOrWriteRop = "VersionCreateOrWriteRop";
	public static final String BucketCreateOrWriteRop = "BucketCreateOrWriteRop";
	public static final String BalanceAdjustment = "BalanceAdjustment";
	public static final String ModifyTagging = "ModifyTagging";
	public static final String RetrieveDelete = "RetrieveDelete";
	public static final String EntireDelete = "EntireDelete";
	public static final String VersionCreateOrWriteCustomer = "VersionCreateOrWriteCustomer";
	public static final String CreateOrWriteCustomer = "CreateOrWriteCustomer";
	public static final String CreateOrWriteServiceAccessKey = "CreateOrWriteServiceAccessKey";
	public static final String SubscribePackageItem = "SubscribePackageItem";
	public static final String UnSubscribePackageItem = "UnSubscribePackageItem";

	// Flattened out metas returned from back end for subscriber profile

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

	public static final String READ_BALANCES_DEDICATED_ACCOUNT_ID = "READ_BALANCES_DEDICATED_ACCOUNT_ID";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_VALUE_1 = "READ_BALANCES_DEDICATED_ACCOUNT_VALUE_1";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_VALUE_2 = "READ_BALANCES_DEDICATED_ACCOUNT_VALUE_2";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_EXPIRY_DATE = "READ_BALANCES_DEDICATED_ACCOUNT_EXPIRY_DATE";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_START_DATE = "READ_BALANCES_DEDICATED_ACCOUNT_START_DATE";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_PAM_SERVICE_ID = "READ_BALANCES_DEDICATED_ACCOUNT_PAM_SERVICE_ID";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_OFFER_ID = "READ_BALANCES_DEDICATED_ACCOUNT_OFFER_ID";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_PRODUCT_ID = "READ_BALANCES_DEDICATED_ACCOUNT_PRODUCT_ID";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_REAL_MONEY_FLAG = "READ_BALANCES_DEDICATED_ACCOUNT_REAL_MONEY_FLAG";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_DATE = "READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_DATE";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_VALUE_1 = "READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_VALUE_1";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_VALUE_2 = "READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_VALUE_2";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_DATE = "READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_DATE";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_VALUE_1 = "READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_VALUE_1";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_VALUE_2 = "READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_VALUE_1";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_ = "READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_2 = "READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_2";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_START_DATE = "READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_START_DATE";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_EXPIRY_DATE = "READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_EXPIRY_DATE";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_ACTIVE_VALUE_1 = "READ_BALANCES_DEDICATED_ACCOUNT_ACTIVE_VALUE_1";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_ACTIVE_VALUE_2 = "READ_BALANCES_DEDICATED_ACCOUNT_ACTIVE_VALUE_2";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_UNIT_TYPE = "READ_BALANCES_DEDICATED_ACCOUNT_UNIT_TYPE";
	public static final String READ_BALANCES_DEDICATED_ACCOUNT_COMPOSITE_DA_FLAG = "READ_BALANCES_DEDICATED_ACCOUNT_COMPOSITE_DA_FLAG";
	public static final String READ_BALANCES_OFFER_INFO_OFFER_ID = "READ_BALANCES_OFFER_INFO_OFFER_ID";
	public static final String READ_BALANCES_OFFER_INFO_START_DATE = "READ_BALANCES_OFFER_INFO_START_DATE";
	public static final String READ_BALANCES_OFFER_INFO_EXPIRY_DATE = "READ_BALANCES_OFFER_INFO_EXPIRY_DATE";
	public static final String READ_BALANCES_OFFER_INFO_START_DATE_TIME = "READ_BALANCES_OFFER_INFO_START_DATE_TIME";
	public static final String READ_BALANCES_OFFER_INFO_EXPIRY_DATE_TIME = "READ_BALANCES_OFFER_INFO_EXPIRY_DATE_TIME";
	public static final String READ_SUBSCRIBER_OFFER_INFO = "READ_SUBSCRIBER_OFFER_INFO";
}
