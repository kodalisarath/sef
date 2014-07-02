package com.ericsson.raso.sef.smart.processor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.ericsson.raso.sef.watergate.FloodGate;
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
		metas.add(new Meta("CustomerId", request.getCustomerId()));
		metas.add(new Meta("category", request.getCategory()));
		metas.add(new Meta("Key", String.valueOf(request.getKey())));
		
		
		String validFrom = request.getbValidFrom();
		if (validFrom != null) { 
			if (validFrom.equals("NOW")) {
				validFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
				metas.add(new Meta("bValidFrom", validFrom));
			} else {
				metas.add(new Meta("bValidFrom", DateUtil.convertISOToSimpleDateFormat(validFrom)));
			}
		}
		
		validFrom = request.getbInvalidFrom();
		if (validFrom != null) { 
			if (validFrom.equals("MAX_DATEANDTIME")) {
				logger.error("Cannot process infinity (MAX_DATEANDTIME) for bucket create or write rop: " + validFrom);
			} else {
				metas.add(new Meta("bInvalidFrom", DateUtil.convertISOToSimpleDateFormat(validFrom)));
			}
		}
		
		metas.add(new Meta("OnPeakAccountID_FU", request.getOnPeakAccountID_FU()));
		metas.add(new Meta("MessageId", String.valueOf(request.getMessageId())));
		String requestId = RequestContextLocalStore.get().getRequestId();
		SubscriberInfo subscriberInfo=updateSubscriber(requestId, request.getCustomerId(), metas,Constants.BucketCreateOrWriteRop);
		//exchange.getOut().setBody(subscriberInfo);
		if (subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() >0) {
			
		throw ExceptionUtil.toSmException(new ResponseCode(subscriberInfo.getStatus().getCode(),subscriberInfo.getStatus().getDescription()));
			
		}
		
		logger.error("FloodGate acknowledging exgress...");
		FloodGate.getInstance().exgress();
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
		semaphore.destroy();
		
		logger.info("Check if response received for create bucket rop");

		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		return subscriberInfo;
	}

}
