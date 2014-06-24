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
import com.ericsson.raso.sef.smart.usecase.VersionCreateOrWriteCustomerRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public class VersionCreateOrWriteCustomer implements Processor {

	private static final Logger logger = LoggerFactory
			.getLogger(VersionCreateOrWriteCustomer.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
			// get the method signature of web service [Standard]
			logger.debug("Getting exchange body....");
			VersionCreateOrWriteCustomerRequest versionCreateOrWriteCustomerRequest = (VersionCreateOrWriteCustomerRequest) exchange
					.getIn().getBody();
			logger.debug("Got it!");
			List<com.ericsson.sef.bes.api.entities.Meta> metas = new ArrayList<com.ericsson.sef.bes.api.entities.Meta>();
			metas.add(new com.ericsson.sef.bes.api.entities.Meta("CustomerId", versionCreateOrWriteCustomerRequest.getCustomerId()));
			metas.add(new com.ericsson.sef.bes.api.entities.Meta("category", versionCreateOrWriteCustomerRequest.getCategory()));
			if(versionCreateOrWriteCustomerRequest.getvValidFrom()!=null){
			metas.add(new com.ericsson.sef.bes.api.entities.Meta("vValidFrom", DateUtil.convertISOToSimpleDateFormat(versionCreateOrWriteCustomerRequest.getvValidFrom())));
			}
			if(versionCreateOrWriteCustomerRequest.getvInvalidFrom()!=null){
			metas.add(new com.ericsson.sef.bes.api.entities.Meta("vInvalidFrom", DateUtil.convertISOToSimpleDateFormat(versionCreateOrWriteCustomerRequest.getvInvalidFrom())));
		    }
			metas.add(new com.ericsson.sef.bes.api.entities.Meta("MessageId", String.valueOf(versionCreateOrWriteCustomerRequest.getMessageId())));
			String requestId = RequestContextLocalStore.get().getRequestId();

			// functional service logic to be ported here [Porting work]
			String customerId = versionCreateOrWriteCustomerRequest.getCustomerId();
			
			SubscriberInfo subscriberInfo = updateSubscriber(requestId,versionCreateOrWriteCustomerRequest.getCustomerId(), metas,Constants.VersionCreateOrWriteCustomer);
			//exchange.getOut().setBody(subscriberInfo);
		if (subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() >0) {
			
			throw ExceptionUtil.toSmException(new ResponseCode(subscriberInfo.getStatus().getCode(),subscriberInfo.getStatus().getDescription()));
			
		} 
	         DummyProcessor.response(exchange);
		
	}

	
	private SubscriberInfo updateSubscriber(String requestId,
			String customer_id, List<Meta> metas,String useCase) throws SmException {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver
				.getSubscriberRequest();
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		logger.debug("Requesting ");
		iSubscriberRequest.updateSubscriber(requestId, customer_id, metas,useCase);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
			logger.error("Error while calling acquire()");
		}
		semaphore.destroy();
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		
		return subscriberInfo;
	}
}
