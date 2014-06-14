package com.ericsson.raso.sef.smart.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.BucketCreateOrWriteRopRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public class BucketCreateOrWriteRop implements Processor {
	private static final Logger logger = LoggerFactory
			.getLogger(CreateOrWriteCustomerProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		BucketCreateOrWriteRopRequest request = (BucketCreateOrWriteRopRequest) exchange
				.getIn().getBody();
		List<Meta> metas = new ArrayList<Meta>();
		metas.add(new Meta("category", request.getCategory()));
		metas.add(new Meta("key", String.valueOf(request.getKey())));
		metas.add(new Meta("bValidFrom", request.getbValidFrom()));
		metas.add(new Meta("bInvalidFrom", request.getbInvalidFrom()));
		metas.add(new Meta("OnPeakAccountID_FU", request
				.getOnPeakAccountID_FU()));
		metas.add(new Meta("messageId", String.valueOf(request.getMessageId())));
		String requestId = RequestContextLocalStore.get().getRequestId();
		SubscriberInfo subscriberInfo=updateSubscriber(requestId, request.getCustomerId(), metas,Constants.BucketCreateOrWriteRop);
		exchange.getOut().setBody(subscriberInfo);
		if (subscriberInfo.getStatus() != null) {
			
		throw ExceptionUtil.toSmException(new ResponseCode(subscriberInfo.getStatus().getCode(),subscriberInfo.getStatus().getDescription()));
			
		}
		DummyProcessor.response(exchange);
	}

	private SubscriberInfo updateSubscriber(String requestId,
			String customer_id, List<Meta> metas,String useCase) throws SmException {
		logger.info("Invoking create subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver
				.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.updateSubscriber(requestId, customer_id, metas,useCase);

		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);

		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			logger.error("Error while acquire()", this.getClass().getName(), e);
		}

		logger.info("Check if response received for create subscriber");

		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		if (subscriberInfo != null) {

			try {
				if (subscriberInfo.getStatus().getCode() > 0) {
					ResponseCode resonseCode = new ResponseCode(subscriberInfo
							.getStatus().getCode(), subscriberInfo.getStatus()
							.getDescription());
					throw new SmException(resonseCode);
				}
			} catch (Exception e) {
				logger.error("subscriberInfo fields are null");
				throw null;
			}

		}

		return subscriberInfo;
	}

}
