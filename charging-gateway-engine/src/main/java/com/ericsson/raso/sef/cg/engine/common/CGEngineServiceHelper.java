package com.ericsson.raso.sef.cg.engine.common;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public abstract class CGEngineServiceHelper {

	private static final Logger logger = LoggerFactory.getLogger(CGEngineServiceHelper.class);

	public static SubscriberInfo getSubscriberInfo(String msisdn) throws SmException {

		logger.debug("Entering CGEngineServiceHelper.....");
		//String requestId = RequestContextLocalStore.get().getRequestId();
		String requestId = UniqueIdGenerator.generateId();
		logger.debug("Generated CGEngineServiceHelper Request ID..... " + requestId);
		SubscriberInfo subscriberInfo = new SubscriberInfo();

		List<Meta> metas = new ArrayList<Meta>();
		Meta meta = new Meta();
		meta.setKey("READ_SUBSCRIBER");
		meta.setValue("PARTIAL_READ_SUBSCRIBER");
		metas.add(meta);
		logger.debug("Entering CGEngineServiceHelper.....");

		ISubscriberRequest subscriberRequest = CgEngineContext.getBean(ISubscriberRequest.class);
		
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
		
		semaphore.destroy();

		logger.debug("Awake from sleep.. going to check subscriber response in store with id: " + correlationId);

		subscriberInfo = (SubscriberInfo) SubscriberResponseStore.get(correlationId);

		return subscriberInfo;
	}


}
