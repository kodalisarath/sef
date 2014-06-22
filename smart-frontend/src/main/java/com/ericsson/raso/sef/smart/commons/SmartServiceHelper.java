package com.ericsson.raso.sef.smart.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.ericsson.raso.sef.smart.ErrorCode;
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
import com.ericsson.raso.sef.smart.processor.EntireReadUtil;
import com.ericsson.raso.sef.smart.processor.WalletUsage;
import com.ericsson.raso.sef.smart.processor.WalletUsageUtil;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public abstract class SmartServiceHelper {

	private static final Logger logger = LoggerFactory.getLogger(SmartServiceHelper.class);

	public static SubscriberInfo getAndRefreshSubscriber(String msisdn) throws SmException {

		logger.debug("Entering getAndRefreshSubscriber.....");
		String requestId = RequestContextLocalStore.get().getRequestId();
		logger.debug("Entering Request ID..... " + requestId);
		SubscriberInfo subscriberInfo = new SubscriberInfo();

		List<Meta> metas = new ArrayList<Meta>();
		Meta meta = new Meta();
		meta.setKey("READ_SUBSCRIBER");
		meta.setValue("ENTIRE_READ_SUBSCRIBER");
		metas.add(meta);
		logger.debug("Entering SmartServiceResolver.....");

		ISubscriberRequest subscriberRequest = SmartServiceResolver.getSubscriberRequest();
		String correlationId = subscriberRequest.readSubscriber(requestId, msisdn, metas);
		SubscriberResponseStore.put(correlationId, subscriberInfo);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.debug("Exception while sleep     :" + e.getMessage());
		}

		logger.debug("Awake from sleep.. going to check subscriber response in store with id: " + correlationId);

		subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(correlationId);

		if (subscriberInfo != null && subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() == 504)
			throw ExceptionUtil.toSmException(new ResponseCode(504, "Unknown Subscriber"));

		updateSubscriberState(subscriberInfo);

		return subscriberInfo;
	}

	public static void updateSubscriberState(SubscriberInfo subscriberInfo) {

		// To DO

	}

	public static SmartModel readSubscriberAccounts(String msisdn) throws SmException {
		logger.debug("Entering getAndRefreshSubscriber.....");
		String requestId = RequestContextLocalStore.get().getRequestId();
		logger.debug("Entering Request ID..... " + requestId);
		SubscriberInfo subscriberInfo = new SubscriberInfo();

		List<Meta> metas = new ArrayList<Meta>();
		Meta meta = new Meta();
		meta.setKey("READ_SUBSCRIBER");
		meta.setValue("ENTIRE_READ_SUBSCRIBER");
		metas.add(meta);
		logger.debug("Entering SmartServiceResolver.....");

		ISubscriberRequest subscriberRequest = SmartServiceResolver.getSubscriberRequest();
		String correlationId = subscriberRequest.readSubscriber(requestId, msisdn, metas);
		SubscriberResponseStore.put(correlationId, subscriberInfo);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.debug("Exception while sleep     :" + e.getMessage());
		}

		logger.debug("Awake from sleep.. going to check subscriber response in store with id: " + correlationId);

		subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(correlationId);

		Map<String, String> metaList = subscriberInfo.getMetas();

		return null;

	}

	private static SubscriberInfo readEntireSubscriberInfo(String requestId, String subscriberId, List<Meta> metas) {
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, subscriberId, metas);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
		}
		logger.info("Check if response received for read subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		
		if (subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() > 0)
			throw ExceptionUtil.toSmException(new ResponseCode(13423, "EntireRead Entity - Customer with primary key Keyname:PK,CustomerId: " + subscriberId + " does not exist"));
		return subscriberInfo;

	}


	public static EntireRead entireReadSubscriber(String msisdn) throws SmException {
		Date currentTime = new Date();
		EntireRead entireRead = new EntireRead();
		String requestId = RequestContextLocalStore.get().getRequestId();
		List<Meta> metaList = new ArrayList<Meta>();
		metaList.add(new Meta("READ_SUBSCRIBER", "ENTIRE_READ_SUBSCRIBER"));
		SubscriberInfo subscriberInfo = readEntireSubscriberInfo(requestId, msisdn, metaList);

		logger.debug("subscriberInfo returned is " + subscriberInfo);
		
		
		Subscriber subscriber = subscriberInfo.getSubscriber();
		logger.debug("subscriber object returned is " + subscriber);
		if (subscriber == null) {
			logger.error("Subscriber Information is empty -- Will treat as unknown subscriber.");
			throw ExceptionUtil.toSmException(new ResponseCode(13423, "EntireRead Entity - Customer with primary key Keyname:PK,CustomerId: " + subscriber.getMsisdn() + " does not exist"));

		}

		entireRead.setWelcomePack(EntireReadUtil.getWelcomePack(subscriber));
		entireRead.setCustomer(EntireReadUtil.getCustomer(subscriber, currentTime));
		entireRead.setRop(EntireReadUtil.getRop(subscriber, currentTime));
		entireRead.setRpps(EntireReadUtil.getRpp(subscriber, currentTime));

		return entireRead;

	}

}
