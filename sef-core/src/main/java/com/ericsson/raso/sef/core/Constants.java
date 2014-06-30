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
	
	//Diameter
	public static final String HEADER_PATTERN = "diameter-";
	public static final String PROTOCOTYPE = "protocolType";
	public static final String REALM = "realm";
	public static final String PRODUCTID = "productId";
	public static final String VENDORID = "vendorId";
	public static final String ACCOUNTID = "accountId";
	public static final String AUTHID = "authId";
	public static final String MESSAGETIMEOUT = "messageTimeout";
	public static final String FQDN = "fqdn";
	public static final String OWNTCPPORT = "ownTcpPort";
	public static final String POOLSIZE = "poolSize";
	public static final String EVENTQUEUESIZE = "eventQueueSize";
	public static final String SENDQUEUESIZE = "sendQueueSize";
	public static final String SENDMESSAGELIMIT = "sendMessageLimit";
	public static final String ETHINTERFACE="ethInterface";
	public static final String ENDPOINTID = "endpointId";
	public static final String EXCEPTION = "exception";
	public static final String operationName = "operationName";
	public static final String OWN_DIAMETER_URI = "ownDiameterURI";
	//Local Cache
	public static final String EVICTIONDURATION = "evictionDuration";
	public static final String EVICTIONTIMEUNIT = "evictionTimeUnit";
	public static final String MAXSIZE = "maxSize";
	public static final String EXCUTABLE_OFFER = "executableOffer";
	//Activation Schedular
	public static final String QUARTZ_INTERFACE = "sef-scheduler-interfaces";
	public static final String QUARTZ_SERVER = "sef-scheduler-server";
	public static final String QUARTZ_CLIENT = "sef-scheduler-client";
	public static final String PURCHASE_ID = "purchaseID";

	public static final String REQUEST_ID = "requestId";
	public static final String IL_CHANNEL = "ILCHANNEL";
	public static final String SUBSCRIPTION_LIFE_CYCLE_EVENT = "subscriptionLifeCycleEvent";
	public static final String USECASE = "usecase";

	public static final String TXN_ENGINE_SUBSCRIBER_ID = "TXN_ENGINE_SUBSCRIBER_ID";
	public static final String TXN_ENGINE_OFFER_ID = "TXN_ENGINE_OFFER_ID";
	
}
