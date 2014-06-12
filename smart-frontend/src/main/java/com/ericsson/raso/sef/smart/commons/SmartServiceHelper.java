package com.ericsson.raso.sef.smart.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.read.Customer;
import com.ericsson.raso.sef.smart.commons.read.CustomerBucketRead;
import com.ericsson.raso.sef.smart.commons.read.CustomerRead;
import com.ericsson.raso.sef.smart.commons.read.CustomerVersionRead;
import com.ericsson.raso.sef.smart.commons.read.EntireRead;
import com.ericsson.raso.sef.smart.commons.read.Rop;
import com.ericsson.raso.sef.smart.commons.read.RopBucketRead;
import com.ericsson.raso.sef.smart.commons.read.RopRead;
import com.ericsson.raso.sef.smart.commons.read.RopVersionRead;
import com.ericsson.raso.sef.smart.commons.read.Rpp;
import com.ericsson.raso.sef.smart.commons.read.RppBucketRead;
import com.ericsson.raso.sef.smart.commons.read.RppRead;
import com.ericsson.raso.sef.smart.commons.read.RppVersionRead;
import com.ericsson.raso.sef.smart.commons.read.Tag;
import com.ericsson.raso.sef.smart.commons.read.WelcomePack;
import com.ericsson.raso.sef.smart.commons.read.WelcomePackBucketRead;
import com.ericsson.raso.sef.smart.commons.read.WelcomePackRead;
import com.ericsson.raso.sef.smart.commons.read.WelcomePackVersionRead;
import com.ericsson.raso.sef.smart.processor.DateUtil;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public abstract class SmartServiceHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(SmartServiceHelper.class);

	public static SubscriberInfo getAndRefreshSubscriber(String msisdn)
			throws SmException {

		logger.debug("Entering getAndRefreshSubscriber.....");
		String requestId = RequestContextLocalStore.get().getRequestId();
		logger.debug("Entering Request ID..... " + requestId);
		SubscriberInfo subscriberInfo = new SubscriberInfo();

		List<Meta> metas = new ArrayList<Meta>();
		Meta meta = new Meta();
		meta.setKey("READ_SUBSCRIBER");
		meta.setValue("READ_SUBSCRIBER");
		metas.add(meta);
		logger.debug("Entering SmartServiceResolver.....");

		ISubscriberRequest subscriberRequest = SmartServiceResolver
				.getSubscriberRequest();
		String correlationId = subscriberRequest.readSubscriber(requestId,
				msisdn, metas);
		SubscriberResponseStore.put(correlationId, subscriberInfo);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.debug("Exception while sleep     :" + e.getMessage());
		}

		logger.debug("Awake from sleep.. going to check subscriber response in store with id: "
				+ correlationId);

		subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.get(correlationId);

		if (subscriberInfo != null && subscriberInfo.getStatus() != null
				&& subscriberInfo.getStatus().getCode() == 504)
			throw ExceptionUtil.toSmException(new ResponseCode(504,
					"Unknown Subscriber"));

		updateSubscriberState(subscriberInfo);

		return subscriberInfo;
	}

	public static void updateSubscriberState(SubscriberInfo subscriberInfo) {

		// To DO

	}

	public static SmartModel readSubscriberAccounts(String msisdn)
			throws SmException {
		logger.debug("Entering getAndRefreshSubscriber.....");
		String requestId = RequestContextLocalStore.get().getRequestId();
		logger.debug("Entering Request ID..... " + requestId);
		SubscriberInfo subscriberInfo = new SubscriberInfo();

		List<Meta> metas = new ArrayList<Meta>();
		Meta meta = new Meta();
		meta.setKey("READ_SUBSCRIBER");
		meta.setValue("READ_SUBSCRIBER");
		metas.add(meta);
		logger.debug("Entering SmartServiceResolver.....");

		ISubscriberRequest subscriberRequest = SmartServiceResolver
				.getSubscriberRequest();
		String correlationId = subscriberRequest.readSubscriber(requestId,
				msisdn, metas);
		SubscriberResponseStore.put(correlationId, subscriberInfo);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.debug("Exception while sleep     :" + e.getMessage());
		}

		logger.debug("Awake from sleep.. going to check subscriber response in store with id: "
				+ correlationId);

		subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.get(correlationId);

		Map<String, String> metaList = subscriberInfo.getMetas();

		return null;

	}

	private static SubscriberInfo readEntireSubscriberInfo(String requestId,
			String subscriberId, List<Meta> metas) {
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver
				.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, subscriberId, metas);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
		}
		logger.info("Check if response received for read subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		return subscriberInfo;

	}

	private static void populateAccountDetails(Subscriber subscriberInfo) {

		if (subscriberInfo != null
				&& subscriberInfo.getAccountDetailsResponse() != null) {
			accountDetailsResponse = subscriberInfo.getAccountDetailsResponse();
		} else {
			GetAccountDetailsRequest accountDetailsRequest = new GetAccountDetailsRequest();
			accountDetailsRequest.setSubscriberNumber(customerId);
			accountDetailsResponse = new GetAccountDetailsCommand(
					accountDetailsRequest).execute();
		}

	}

	private static void populateUsAndUt() {
		GetUsageThresholdsAndCountersRequest countersRequest = new GetUsageThresholdsAndCountersRequest();
		countersRequest.setSubscriberNumber(customerId);
		GetUsageThresholdsAndCountersResponse usageRes = new GetUsageThresholdsAndCountersCmd(
				countersRequest).execute();

		List<UsageCounterUsageThresholdInformation> ucUtList = usageRes
				.getUsageCounterUsageThresholdInformation();
		ucUtMap = new HashMap<Integer, UsageCounterUsageThresholdInformation>();
		for (UsageCounterUsageThresholdInformation usageCounterUsageThresholdInformation : ucUtList) {
			ucUtMap.put(
					usageCounterUsageThresholdInformation.getUsageCounterID(),
					usageCounterUsageThresholdInformation);
		}

	}

	private static void populateDaAndOffers() {
		GetBalanceAndDateRequest gbdReq = new GetBalanceAndDateRequest();
		gbdReq.setSubscriberNumber(customerId);
		GetBalanceAndDateResponse gbdRes = new GetBalanceAndDateCommand(gbdReq)
				.execute();
		List<DedicatedAccountInformation> daList = gbdRes
				.getDedicatedAccountInformation();

		daMap = new HashMap<Integer, DedicatedAccountInformation>();
		for (DedicatedAccountInformation da : daList) {
			daMap.put(da.getDedicatedAccountID(), da);
		}

		List<OfferInformation> offerList = gbdRes.getOfferInformationList();
		offerMap = new HashMap<Integer, OfferInformation>();
		for (OfferInformation offerInformation : offerList) {
			offerMap.put(offerInformation.getOfferID(), offerInformation);
		}
	}

	public static WelcomePack getWelcomePack(Subscriber subscriber) {
		WelcomePack pack = new WelcomePack();
		pack.setRead(createRead(subscriber));
		pack.setBucketRead(createBucketRead(subscriber));
		pack.setVersionRead(createVersionRead(subscriber));
		return pack;
	}

	private static WelcomePackRead createRead(Subscriber subscriber) {
		WelcomePackRead read = new WelcomePackRead();
		read.setCategory("ONLINE");
		read.setCustomerId(subscriber.getCustomerId());
		read.setPrefetchFilter(-1);
		read.setsCrmTitle("-");
		read.setsCanBeSharedByMultipleRops(false);
		read.setsInsertedViaBatch(false);
		read.setOfferProfileKey(1);
		if (subscriber.getMetas() != null
				&& subscriber.getMetas().containsKey("package")) {
			String welcomePack = subscriber.getMetas().get("package");

			read.setsPackageId(welcomePack);
		}
		read.setsPreActive(ContractState.apiValue(
				ContractState.PREACTIVE.name()).equals(
				subscriber.getContractState()));
		read.setsActivationStartTime(subscriber.getActiveDate());
		read.setsPeriodStartPoint(-1);
		return read;
	}

	private static WelcomePackVersionRead createVersionRead(
			Subscriber subscriber) {
		WelcomePackVersionRead read = new WelcomePackVersionRead();
		read.setCategory("ONLINE");
		read.setCustomerId(subscriber.getCustomerId());
		read.setOfferProfileKey(1);
		IConfig config = SefCoreServiceResolver.getConfigService();
		read.setvValidFrom(DateUtil.convertDateToString(
				new Date(subscriber.getActiveDate()),
				config.getValue("GLOBAL", SmartConstants.DATE_FORMAT)));
		read.setvInvalidFrom(SmartConstants.MAX_DATETIME);
		return read;
	}

	private static WelcomePackBucketRead createBucketRead(Subscriber subscriber) {
		WelcomePackBucketRead read = new WelcomePackBucketRead();
		read.setbCategory("ONLINE");
		read.setbSeriesId(0);
		read.setOfferProfileKey(1);
		read.setsActive(true);
		read.setsError((byte) 0);
		read.setsInfo(0);
		read.setsValid(true);
		read.setCustomerId(subscriber.getCustomerId());
		IConfig config = SefCoreServiceResolver.getConfigService();
		read.setbValidFrom(DateUtil.convertDateToString(
				new Date(subscriber.getActiveDate()),
				config.getValue("GLOBAL", SmartConstants.DATE_FORMAT)));
		read.setbInvalidFrom(SmartConstants.MAX_DATETIME);

		return read;
	}

	public static EntireRead entireReadSubscriber(String msisdn)
			throws SmException {
		Date currentTime = new Date();
		EntireRead entireRead = new EntireRead();
		String requestId = RequestContextLocalStore.get().getRequestId();
		List<Meta> metaList = new ArrayList<Meta>();
		metaList.add(new Meta("READ_SUBSCRIBER", "ENTIRE_READ_SUBSCRIBER"));
		SubscriberInfo subscriberInfo = readEntireSubscriberInfo(requestId,
				msisdn, metaList);

		Subscriber subscriber = subscriberInfo.getSubscriber();

		if (subscriber == null)
			return null;

		entireRead.setWelcomePack(getWelcomePack(subscriber));
		entireRead.setCustomer(getCustomer(subscriber, currentTime));
		entireRead.setRop(getRop(subscriber, currentTime));
		entireRead.setRpps(null);

		return entireRead;

	}

	private static Customer getCustomer(Subscriber subcriber, Date currentTime) {
		Customer customer = new Customer();
		customer.setCustomerRead(createCustomerRead(subcriber));
		customer.setCustomerBucketRead(createCustomerBucketRead(subcriber,
				currentTime));
		customer.setCustomerVersionRead(createCustomerVersionRead(subcriber,
				currentTime));
		return customer;
	}

	private static CustomerRead createCustomerRead(Subscriber subcriber) {
		CustomerRead customerRead = new CustomerRead();
		customerRead.setCustomerId(subcriber.getCustomerId());
		customerRead.setBillCycleId(0);
		customerRead.setBillCycleIdAfterSwitch(-1);
		customerRead.setBillCycleSwitch(SmartConstants.MAX_DATETIME);
		customerRead.setCategory("ONLINE");
		customerRead.setPrefetchFilter(-1);
		return customerRead;
	}

	private static CustomerVersionRead createCustomerVersionRead(
			Subscriber subcriber, Date currentTime) {
		CustomerVersionRead versionRead = new CustomerVersionRead();
		versionRead.setCustomerId(subcriber.getCustomerId());
		versionRead.setCategory("ONLINE");
		versionRead.setvInvalidFrom(SmartConstants.MAX_DATETIME);
		IConfig config = SefCoreServiceResolver.getConfigService();
		versionRead.setvValidFrom(DateUtil.convertDateToString(currentTime,
				config.getValue("GLOBAL", SmartConstants.DATE_FORMAT)));
		return versionRead;
	}

	private static CustomerBucketRead createCustomerBucketRead(
			Subscriber subscriber, Date currentTime) {
		CustomerBucketRead bucketRead = new CustomerBucketRead();
		bucketRead.setCustomerId(subscriber.getCustomerId());
		bucketRead.setbCategory("ONLINE");
		bucketRead.setbSeriesId(0);
		IConfig config = SefCoreServiceResolver.getConfigService();
		bucketRead.setbValidFrom(DateUtil.convertDateToString(currentTime,
				config.getValue("GLOBAL", SmartConstants.DATE_FORMAT)));
		bucketRead.setbInvalidFrom(SmartConstants.MAX_DATETIME);
		return bucketRead;
	}

	private static Rop getRop(Subscriber subscriber, Date currentDateTime) {
		Rop rop = new Rop();
		rop.setRopRead(createRopRead(subscriber));
		rop.setRopBucketRead(createRopBucketRead(subscriber));
		rop.setRopVersionRead(createRopVersionRead(subscriber, currentDateTime));
		return rop;
	}

	private static RopRead createRopRead(Subscriber subscriber) {
		RopRead ropRead = new RopRead();
		ropRead.setCustomerId(subscriber.getCustomerId());
		ropRead.setKey(1);
		ropRead.setCategory("ONLINE");
		ropRead.setPrefetchFilter(-1);
		if (subscriber.getMetas() != null) {
			if (subscriber.getMetas().containsKey(
					Constants.READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE)) {
				ropRead.setActiveEndDate(subscriber.getMetas().get(
						Constants.READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE));
			}
		}

		ropRead.setAnnoFirstWarningPeriodSent(false);
		ropRead.setAnnoSecondWarningPeriodSent(false);

		for (int i = 1; i <= 6; i++) {
			ropRead.getChargedMenuAccessCounter().add(0);
		}

		Tag tag = getSmartTagging(subscriber);
		if (tag != null) {
			ropRead.setcTaggingStatus(tag.getSmartId());
		}

		ropRead.setCustomerId(subscriber.getCustomerId());

		IConfig config = SefCoreServiceResolver.getConfigService();
		ropRead.setFirstCallDate(DateUtil.convertDateToString(new Date(
				subscriber.getActiveDate()), config.getValue("GLOBAL",
				SmartConstants.DATE_FORMAT)));

		// if (accountDetailsResponse != null
		// && accountDetailsResponse.getActivationDate() != null) {
		// ropRead.setFirstCallDate(accountDetailsResponse.getActivationDate()
		// .toString());
		// }
		//
		/*
		 * OfferInformation graceOffer = offerMap.get(Integer.valueOf(1)); if
		 * (graceOffer != null && graceOffer.getExpiryDate() != null) {
		 * ropRead.setGraceEndDate(graceOffer.getExpiryDate().toString()); }
		 */

		ropRead.setGraceEndDate(getGraceEndDate(subscriber));
		ropRead.setIsBalanceClearanceOnOutpayment(true);

		String isCFMOC = subscriber.getMetas().get("IsCFMOC");
		if (isCFMOC != null) {
			ropRead.setIsCFMOC(Integer.valueOf(isCFMOC));
		}

		String IsCollectCallAllowed = subscriber.getMetas().get(
				"IsCollectCallAllowed");
		if (IsCollectCallAllowed != null) {
			ropRead.setIsCollectCallAllowed(Boolean
					.valueOf(IsCollectCallAllowed));
		}

		String IsFirstCallPassed = subscriber.getMetas().get(
				"IsFirstCallPassed");

		if (IsFirstCallPassed != null) {
			ropRead.setIsFirstCallPassed(Boolean.valueOf(IsFirstCallPassed));
		}

		String IsGPRSUsed = subscriber.getMetas().get("IsGPRSUsed");

		if (IsGPRSUsed != null) {
			ropRead.setIsGPRSUsed(Boolean.valueOf(IsGPRSUsed));
		}

		String IsLastRechargeInfoStored = subscriber.getMetas().get(
				"IsLastRechargeInfoStored");
		if (IsLastRechargeInfoStored != null) {
			ropRead.setIsLastRechargeInfoStored(Boolean
					.valueOf(IsLastRechargeInfoStored));
		}

		String IsLastTransactionEnqUsed = subscriber.getMetas().get(
				"IsLastTransactionEnqUsed");
		if (IsLastTransactionEnqUsed != null) {
			ropRead.setIsLastTransactionEnqUsed(Boolean
					.valueOf(IsLastTransactionEnqUsed));
		}

		String IsOperatorCollectCallAllowed = subscriber.getMetas().get(
				"IsOperatorCollectCallAllowed");
		if (IsOperatorCollectCallAllowed != null) {
			ropRead.setIsOperatorCollectCallAllowed(Boolean
					.valueOf(IsOperatorCollectCallAllowed));
		}

		String IsSmsAllowed = subscriber.getMetas().get("IsSmsAllowed");
		if (IsSmsAllowed != null) {
			ropRead.setIsSmsAllowed(Boolean.valueOf(IsSmsAllowed));
		}

		String PreActiveEndDate = subscriber.getMetas().get("preActiveEndDate");
		if (PreActiveEndDate != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			try {
				ropRead.setPreActiveEndDate(dateFormat.parse(PreActiveEndDate)
						.toString());
			} catch (ParseException e) {
			}
		}

		ropRead.setLastKnownPeriod(subscriber.getContractState());

		ropRead.setS_CRMTitle("-");
		if (tag != null && tag.isSmartTag()) {
			ropRead.setIsLocked(true);
		} else {
			ropRead.setIsLocked(false);
		}

		return ropRead;
	}

	private static RopBucketRead createRopBucketRead(Subscriber subscriber) {
		RopBucketRead read = new RopBucketRead();
		read.setCustomerId(subscriber.getCustomerId());
		read.setbCategory("ONLINE");
		read.setbInvalidFrom(SmartConstants.MAX_DATETIME);
		/*
		 * if (accountDetailsResponse != null &&
		 * accountDetailsResponse.getActivationDate() != null) {config
		 * read.setbValidFrom(accountDetailsResponse.getActivationDate()
		 * .toString()); }
		 */
		IConfig config = SefCoreServiceResolver.getConfigService();
		read.setbValidFrom(DateUtil.convertDateToString(
				new Date(subscriber.getActiveDate()),
				config.getValue("GLOBAL", SmartConstants.DATE_FORMAT)));

		/*
		 * DedicatedAccountInformation da =
		 * daMap.get(SmConstants.AIRTIME_DA_ID); if (da != null) {
		 * read.setOnPeakFuBalance(Long.valueOf(da
		 * .getDedicatedAccountActiveValue1())); }
		 */
		read.setOnPeakFuBalance(Long.parseLong(getDedicatedAccount(subscriber)));
		read.setbSeriesId(0);
		return read;
	}

	private static RopVersionRead createRopVersionRead(Subscriber subscriber,
			Date currentTime) {
		RopVersionRead read = new RopVersionRead();
		read.setCustomerId(subscriber.getCustomerId());
		read.setCategory("ONLINE");
		read.setKey(1);

		/*
		 * OfferInformation offer = offerMap.get(SmConstants.AIRTIME_OFFER_ID);
		 * if (offer != null) {
		 * read.setOnPeakAccountExpiryDate(offer.getExpiryDateTime()
		 * .toString()); }
		 */

		read.setOnPeakAccountExpiryDate(getOfferExpiryDateTime(subscriber));

		read.setsOfferId("TnT");
		IConfig config = SefCoreServiceResolver.getConfigService();
		read.setvValidFrom(DateUtil.convertDateToString(
				new Date(subscriber.getActiveDate()),
				config.getValue("GLOBAL", SmartConstants.DATE_FORMAT)));
		read.setvInvalidFrom(SmartConstants.MAX_DATETIME);

		return read;
	}

	private static Tag getSmartTagging(Subscriber subscriber) {

		Map<String, String> metaMap = subscriber.getMetas();
		Set<String> keySet = metaMap.keySet();
		String key = null;

		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			key = i.next();
			String value = null;
			if (key.startsWith(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID)) {
				String temp = "";
				if (key.length() > Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID
						.length()) {
					temp = key.substring(
							Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID
									.length() + 1, key.length());
				}
				if (metaMap
						.containsKey(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG
								+ temp)) {
					value = metaMap
							.get(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG
									+ temp);
					if (Boolean.valueOf(value)) {
						Tag tag = Tag.getTagById(Integer.parseInt(metaMap
								.get(key)));
						if (tag != null && tag.isSmartTag()) {
							return tag;
						}
					}
				}
			}
		}
		return null;
	}

	private static String getGraceEndDate(Subscriber subscriber) {

		Map<String, String> metaMap = subscriber.getMetas();
		Set<String> keySet = metaMap.keySet();
		String key = null;

		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			key = i.next();
			String value = null;

			if (key.startsWith(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID)) {

				value = metaMap.get(key);

				if ("1".equals(value)) {
					String temp = "";
					if (key.length() > Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID
							.length()) {
						temp = key.substring(
								Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID
										.length() + 1, key.length());
					}

					if (metaMap
							.containsKey(Constants.READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE
									+ temp)) {
						value = metaMap
								.get(Constants.READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE
										+ temp);

						return value;
					}
				}
			}
		}
		return null;
	}

	private static String getDedicatedAccount(Subscriber subscriber) {

		Map<String, String> metaMap = subscriber.getMetas();
		Set<String> keySet = metaMap.keySet();
		String key = null;

		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			key = i.next();
			String value = null;

			if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_ID)) {

				value = metaMap.get(key);

				if ("1".equals(value)) {
					String temp = "";
					if (key.length() > Constants.READ_BALANCES_DEDICATED_ACCOUNT_ID
							.length()) {
						temp = key.substring(
								Constants.READ_BALANCES_DEDICATED_ACCOUNT_ID
										.length() + 1, key.length());
					}

					if (metaMap
							.containsKey(Constants.READ_BALANCES_DEDICATED_ACCOUNT_VALUE_1
									+ temp)) {
						value = metaMap
								.get(Constants.READ_BALANCES_DEDICATED_ACCOUNT_VALUE_1
										+ temp);

						return value;
					}
				}
			}
		}
		return null;
	}

	private static String getOfferExpiryDateTime(Subscriber subscriber) {
		Map<String, String> metaMap = subscriber.getMetas();
		Set<String> keySet = metaMap.keySet();
		String key = null;
		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			key = i.next();
			String value = null;
			if (key.startsWith(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID)) {
				value = metaMap.get(key);
				if ("1001".equals(value)) {
					String temp = "";
					if (key.length() > Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID
							.length()) {
						temp = key.substring(
								Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID
										.length() + 1, key.length());
					}
					if (metaMap
							.containsKey(Constants.READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME
									+ temp)) {
						value = metaMap
								.get(Constants.READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME
										+ temp);
						return value;
					}
				}
			}
		}
		return null;
	}

	private static String getOfferExpiryDateTime(Subscriber subscriber,
			String key) {
		Map<String, String> metaMap = subscriber.getMetas();
		String value = null;
		if (key.startsWith(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID)) {
			value = metaMap.get(key);
			String temp = "";
			if (key.length() > Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID
					.length()) 
			{
				temp = key.substring(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID.length() + 1, key.length());
			}
			if (metaMap
					.containsKey(Constants.READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME
							+ temp)) {
				value = metaMap
						.get(Constants.READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME
								+ temp);
				return value;
			}
		}
		return null;
	}
	
	
	private static String getOfferStartDateTime(Subscriber subscriber,
			String key) {
		Map<String, String> metaMap = subscriber.getMetas();
		String value = null;
		if (key.startsWith(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID)) {
			value = metaMap.get(key);
			String temp = "";
			if (key.length() > Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID
					.length()) 
			{
				temp = key.substring(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID.length() + 1, key.length());
			}
			if (metaMap
					.containsKey(Constants.READ_SUBSCRIBER_OFFER_INFO_START_DATE_TIME
							+ temp)) {
				value = metaMap
						.get(Constants.READ_SUBSCRIBER_OFFER_INFO_START_DATE_TIME
								+ temp);
				return value;
			}
		}
		return null;
	}

	private static Collection<Rpp> getRpp(Subscriber subscriber) {
		List<Rpp> rpps = new ArrayList<Rpp>();
		// Collection<Integer> offerIds = offerMap.keySet();
		int index = 1;
		Map<String, String> metaMap = subscriber.getMetas();
		Set<String> keySet = metaMap.keySet();
		String key = null;
		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			key = i.next();
			String value = null;
			if (key.startsWith(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID)) {
				value = metaMap.get(key);
				if (!"1".equals(value)) {
					Rpp rpp = createRpp(subscriber, key, index++);
					if (rpp != null) {
						rpps.add(rpp);
					}
				}
			}
		}
		return rpps;
	}

	private static Rpp createRpp(Subscriber subscriber, String key, int index) {
		// String wallet =
		// offerWalletMapping.getProperty(String.valueOf(offerInformation.getOfferID()));

		String offerId = subscriber.getMetas().get(key);
		String walletName = SefCoreServiceResolver.getConfigService().getValue(
				"GLOBAL_walletMapping", offerId.trim());

		if (walletName != null) {
			Rpp rpp = new Rpp();

			RppRead rppRead = createRppRead(subscriber, key, offerId);
			rppRead.setKey(index);
			rpp.setRppRead(rppRead);

			RppBucketRead bucketRead = createRppBucketRead(subscriber, key,
					offerId);
			bucketRead.setKey(index);
			rpp.setRppBucketRead(bucketRead);

			RppVersionRead versionRead = createRppVersionRead(subscriber, key,
					offerId);
			versionRead.setKey(index);
			rpp.setRppVersionRead(versionRead);

			return rpp;
		}
		return null;
	}

	private static RppRead createRppRead(Subscriber subscriber, String key,
			String offerId) {
		// String wallet =
		// offerWalletMapping.getProperty(String.valueOf(offerId));
		String walletName = SefCoreServiceResolver.getConfigService().getValue(
				"GLOBAL_walletMapping", offerId.trim());

		RppRead read = new RppRead();
		read.setCustomerId(subscriber.getCustomerId());
		read.setCategory("ONLINE");
		read.setcUnliResetRechargeValidity(0);
		read.setOfferProfileKey(1);
		read.setPrefetchFilter(-1);
		/*if (offerInformation.getExpiryDateTime() != null) {
			read.setsActivationEndTime(offerInformation.getExpiryDateTime()
					.toString());
		}*/
		
		IConfig config = SefCoreServiceResolver.getConfigService();
		
		String offerExpiryDateString = getOfferExpiryDateTime(subscriber, key);
		read.setsActivationEndTime(offerExpiryDateString);
		
	/*	if (offerInformation.getStartDateTime() == null
				|| offerInformation.getStartDateTime().before(new Date(0))) {
			read.setsActivationStartTime(new Date(0).toString());
		} else {
			read.setsActivationStartTime(offerInformation.getStartDateTime()
					.toString());
		}*/
		read.setsActivationStartTime(getOfferStartDateTime(subscriber, key));
		
		read.setsCanBeSharedByMultipleRops(false);
		read.setsCRMTitle("-");
		read.setsInsertedViaBatch(false);
		read.setsPackageId(walletName);
		Date offerExpiryDate = DateUtil.convertStringToDate(offerExpiryDateString, config.getValue("GLOBAL", SmartConstants.DATE_FORMAT)) ;
		
		if (walletName.equalsIgnoreCase("1030AutoFbc")
				&& offerExpiryDate != null) {
			read.setC_TokenBasedExpiredDate(offerExpiryDate.getTime());
		}

		read.setsPeriodStartPoint(-1);
		if (ContractState.PREACTIVE.name().equals(ContractState.apiValue(subscriber.getContractState()))) {
			read.setsPreActive(true);
		} else {
			read.setsPreActive(false);
		}

		Long balance = 0l; 
		
		/***********TBD once the property isssue is fixed. Cannot port the usageTimingWindows property as it does not comply to our DTD********/
		//Need to uncommen the below once the schema is corrected for the property files.
		
		/*WalletUsage usage = WalletUsageUtil.getWalletUsage(wallet);
		
		if (usage != null) {
			UsageCounterUsageThresholdInformation uc = ucUtMap.get(Integer
					.valueOf(usage.getUc()));
			if (uc != null
					&& usage.getUc() == uc.getUsageCounterID().intValue()) {
				List<UsageThresholdInformation> tList = uc
						.getUsageThresholdInformation();
				for (UsageThresholdInformation ut : tList) {
					if (ut.getUsageThresholdID().intValue() == usage.getUt()) {
						if (uc.getUsageCounterValue() != null
								&& ut.getUsageThresholdValue() != null) {
							balance = Long.valueOf(ut.getUsageThresholdValue())
									- Long.valueOf(uc.getUsageCounterValue());
						}
					}
				}
			}
		}*/

		read.setcIACCreditLimitValidity(balance);
		return read;
	}

	private static RppBucketRead createRppBucketRead(Subscriber subscriber, String key,String offerId) {
		//String wallet = offerWalletMapping.getProperty(String.valueOf(offerId)));

		String walletName = SefCoreServiceResolver.getConfigService().getValue(
				"GLOBAL_walletMapping", offerId.trim());
		
		RppBucketRead read = new RppBucketRead();
		read.setCustomerId(subscriber.getCustomerId());
		read.setOfferProfileKey(1);

	/*	if(offer.getStartDateTime() == null || offer.getStartDateTime().before(new Date(0))) {
			read.setbValidFrom(new Date(0).toString());
		} else {
			read.setbValidFrom(offer.getStartDateTime().toString());
		}*/
		
		
		read.setbValidFrom(getOfferStartDateTime(subscriber, key));

		String offerExpiryDateString = getOfferExpiryDateTime(subscriber, key);
		
		IConfig config = SefCoreServiceResolver.getConfigService();
		
		Date offerExpiryDate = DateUtil.convertStringToDate(offerExpiryDateString, config.getValue("GLOBAL", SmartConstants.DATE_FORMAT));
		if(offerExpiryDateString != null) {
			read.setbInvalidFrom(offerExpiryDateString);
			read.setsNextPeriodAct(offerExpiryDate );
			read.setsExpireDate(offerExpiryDateString);
		}

		read.setbSeriesId(0);
		read.setbCategory("ONLINE");

		
		if(offerExpiryDate != null && offerExpiryDate.after(new Date())) {
			read.setsActive(true);
		} else {
			read.setsActive(false);
		}

		read.setsError((byte)0);
		read.setsInfo(0);
		read.setsValid(true);


		//String daId = offerDaMapping.getProperty(String.valueOf(offer.getOfferID()));
		String daId = SefCoreServiceResolver.getConfigService().getValue(
				"Global_offerMapping", offerId.trim());
		
		


		
		if(daId != null && offer.getOfferID().intValue() <= 2000) {
			Long balance = 0l;
			DedicatedAccountInformation da = daMap.get(Integer.valueOf(daId));
			if(da != null) {
				balance = Double.valueOf(da.getDedicatedAccountValue1()).longValue();
				long confec= CommonUtil.getConversionFector(""+offer.getOfferID());
				balance = balance/confec;
			}
			read.setsPeriodicBonusBalance(balance);
		} else {
			WalletUsage usage = WalletUsageUtil.getWalletUsage(wallet);
			Long balance = 0l;
			if(usage != null) {
				UsageCounterUsageThresholdInformation uc = ucUtMap.get(Integer.valueOf(usage.getUc()));
				if(uc != null && usage.getUc() == uc.getUsageCounterID().intValue()) {
					List<UsageThresholdInformation> tList = uc.getUsageThresholdInformation();
					for (UsageThresholdInformation ut : tList) {
						if(ut.getUsageThresholdID().intValue() == usage.getUt()) {
							if(uc.getUsageCounterValue() != null && ut.getUsageThresholdValue() != null) {
								balance = Long.valueOf(ut.getUsageThresholdValue()) - Long.valueOf(uc.getUsageCounterValue());
							}
						}
					}
				}
			}
			read.setsPeriodicBonusBalance(balance);
		}

		return read;
	}

	private static RppVersionRead createRppVersionRead(Subscriber subscriber,
			String key, String offerId) {
		RppVersionRead read = new RppVersionRead();
		read.setCategory("ONLINE");
		read.setCustomerId(subscriber.getCustomerId());
		read.setOfferProfileKey(1);
		read.setsPeriodicBonusCreditLimit(0L);

		if (offer.getStartDateTime() == null
				|| offer.getStartDateTime().before(new Date(0))) {
			read.setvValidFrom(new Date(0).toString());
		} else {
			read.setvValidFrom(offer.getStartDateTime().toString());
		}

		if (offer.getExpiryDateTime() != null) {
			read.setsPeriodicBonusExpiryDate(offer.getExpiryDateTime()
					.toString());
			read.setvInvalidFrom(offer.getExpiryDateTime().toString());
		} else {
			read.setvInvalidFrom(MAX_DATETIME);
		}

		if (offer.getStartDateTime() != null) {
			read.setvValidFrom(offer.getStartDateTime().toString());
		} else {
			read.setvValidFrom(NOW);
		}

		return read;
	}

}
