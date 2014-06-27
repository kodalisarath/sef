package com.ericsson.raso.sef.notification.workflows.common;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.raso.sef.notification.workflows.NotificationContext;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public abstract class WorkflowEngineServiceHelper {

	private static final Logger logger = LoggerFactory.getLogger(WorkflowEngineServiceHelper.class);

	/**
	 * After discussing with Sean Kumar, Sathya, on 25/6/2014
	 * Workflows does not need to call Transaction Engine to query subscriber status.
	 * Workflow has to call DB to get subscriber info only
	 * @param msisdn
	 * @return
	 * @throws SmException
	 */
	public static Subscriber getSubscriber(String msisdn) throws SmException {

		logger.debug("Entering WorkflowEngineServiceHelper.....");
		//String requestId = RequestContextLocalStore.get().getRequestId();
//		String requestId = UniqueIdGenerator.generateId();
//		logger.debug("Generated WorkflowEngineServiceHelper Request ID..... " + requestId);
//		SubscriberInfo subscriberInfo = new SubscriberInfo();
//
//		List<Meta> metas = new ArrayList<Meta>();
//		Meta meta = new Meta();
//		meta.setKey("READ_SUBSCRIBER");
//		meta.setValue("PARTIAL_READ_SUBSCRIBER");
//		metas.add(meta);
//		logger.debug("Entering CGEngineServiceHelper.....");
//
//		ISubscriberRequest subscriberRequest = NotificationContext.getBean(ISubscriberRequest.class);
//		
//		String correlationId = subscriberRequest.readSubscriber(requestId, msisdn, metas);
//		SubscriberResponseStore.put(correlationId, subscriberInfo);
//
//		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
//		try {
//			semaphore.init(0);
//			semaphore.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			logger.debug("Exception while sleep     :" + e.getMessage());
//		}
//
//		logger.debug("Awake from sleep.. going to check subscriber response in store with id: " + correlationId);
//
//		subscriberInfo = (SubscriberInfo) SubscriberResponseStore.get(correlationId);
		SubscriberService subscriberService = SefCoreServiceResolver.getSusbcriberStore();
		Subscriber subscriber = null;
		try {
			subscriber = subscriberService.getSubscriber("pasaload:money",msisdn);
			return subscriber;
		} catch (PersistenceError e) {
			// TODO Auto-generated catch block
			logger.error("Exception capured getSubscriberInfo ", e);
			throw new SmException(e);
		}
	}


}
