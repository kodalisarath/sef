package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.ReadSubscriberMetaRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.ReadSubscriberMetaResponse;
import com.ericsson.raso.sef.bes.engine.transaction.entities.ReadSubscriberResponse;
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscriber.ISubscriberResponse;


public class ReadSubscriberMeta extends AbstractTransaction {
	private static final long	serialVersionUID	= 8130277491237379246L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadSubscriberMeta.class);
	
	private boolean isWorkflowEngaged = false;

	public ReadSubscriberMeta(String requestId, String subscriberId, Set<String> metas) {
		super(requestId, new ReadSubscriberMetaRequest(requestId, subscriberId, metas));
		this.setResponse(new ReadSubscriberMetaResponse(requestId));
	}

	@Override
	public Boolean execute() throws TransactionException {
		LOGGER.debug("Entering Read Subscriber Meta somehow....");
		
		if (((ReadSubscriberMetaRequest)this.getRequest()).getSubscriberId() == null) {
			LOGGER.error("Subscriber ID is not found. Cannot execute!!!");
			this.getResponse().setReturnFault(new TransactionException("txe", new ResponseCode(1000, "Subscriber ID was not found!!"), null));
			return false;
		}
		
		SubscriberService subscriberStore = SefCoreServiceResolver.getSusbcriberStore();
		if (subscriberStore == null) {
			this.getResponse().setReturnFault(new TransactionException("txe", new ResponseCode(1001, "Unable to secure DB Service for Subscriber!!!"), null));
			return false;
		}


		Collection<Meta> metas = null;
		try {
			metas = subscriberStore.getMetas(this.getRequestId(), ((ReadSubscriberMetaRequest)this.getRequest()).getSubscriberId(), 
															this.getMetaKeys(((ReadSubscriberMetaRequest)this.getRequest()).getMetaNames()));
		} catch (PersistenceError e) {
			this.getResponse().setReturnFault(new TransactionException("txe", new ResponseCode(1002, "Persistence failure with metas"), e));
			return false;
		}


		Set<com.ericsson.sef.bes.api.entities.Meta> result = this.getApiEntities(metas);
		((ReadSubscriberMetaResponse)this.getResponse()).setMetas(result);

		this.sendResponse();
		return true;
	}

	
	private List<String> getMetaKeys(Set<String> metaNames) {
		List<String> metaKeys = new ArrayList<String>();
		metaKeys.addAll(metaNames);
		return metaKeys;
	}

	private Set<com.ericsson.sef.bes.api.entities.Meta> getApiEntities(Collection<Meta> metas) {
		Set<com.ericsson.sef.bes.api.entities.Meta> result = new HashSet<com.ericsson.sef.bes.api.entities.Meta>();
		for (Meta nativeMeta: metas) {
			result.add(new com.ericsson.sef.bes.api.entities.Meta(nativeMeta.getKey(), nativeMeta.getValue()));
		}
		return result;
	}

	@Override
	public void sendResponse() {
		//TODO: implement this logic
		/*
		 * 1. when this method is called, it means that Orchestration Manager has executed all steps in the transaction. Either a response or
		 * exception is available.
		 * 
		 * 2. The response will most likely be results/ responses/ exceptions from atomic steps in the transaction. This must be packed into
		 * the response pojo structure pertinent to method signature of the response interface.
		 * 
		 * 3. once the response pojo entity is packed, the client for response interface must be invoked. the assumption is that response
		 * interface will notify the right JVM waiting for this response thru a Object.wait
		 */
		
		TransactionStatus txnStatus=null;
		com.ericsson.sef.bes.api.entities.Subscriber subscriber = ((ReadSubscriberResponse)this.getResponse()).getSubscriber();
			
		
		LOGGER.debug("Invoking read subscriber response!!");
		ISubscriberResponse subscriberClient = ServiceResolver.getSubscriberResponseClient();
		subscriberClient.readSubscriber(this.getRequestId(), 
				                    txnStatus, 
									subscriber);
		LOGGER.debug("read susbcriber response posted");

		
	}
	
	

}
