package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.UpdateSubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.UpdateSubscriberResponse;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscriber.ISubscriberResponse;

public class UpdateSubscriber extends AbstractTransaction{
	
	private static final long serialVersionUID = 7686721923498952231L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSubscriber.class);
	
	public UpdateSubscriber(String requestId,String subscriberId,Map<String,String> metas) {
		super(requestId, new UpdateSubscriberRequest(requestId, subscriberId,metas));
		this.setResponse(new UpdateSubscriberResponse(requestId,true));
	}

	@Override
	public Boolean execute() throws TransactionException {
		LOGGER.debug("Entering Update Subscriber...");
		com.ericsson.raso.sef.core.db.model.Subscriber subscriberEntity = null;
		
		try {
			subscriberEntity = ((UpdateSubscriberRequest)this.getRequest()).persistableEntity();
			
			if(subscriberEntity == null) {
				LOGGER.error("Subscriber not found in database" );
				
				this.getResponse().setReturnFault(new TransactionException("tx-engine", new ResponseCode(102,"Subscriber not found")));
				sendResponse();
				return false;
			}
			LOGGER.debug("Got Persistable Entity: Subscriber...");
				
		} catch (FrameworkException e1) {
			this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), "Unable to pack the workflow tasks for this use-case", e1));
			sendResponse();
			return false;
		}
		
		SubscriberService subscriberStore = SefCoreServiceResolver.getSusbcriberStore();
		if (subscriberStore == null) {
			LOGGER.error("Unable to access persistence tier service!!");
			this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), "Unable to access persistence tier service!! Check configuration (beans.xml)"));
			return false;
		}
		subscriberStore.updateSubscriber(subscriberEntity);
		LOGGER.debug("Update Subscriber successfull!!");
		
		sendResponse();
		
		return true;
	}

	@Override
	public void sendResponse() {
		
		TransactionStatus txnStatus=null;
		
		TransactionException fault = this.getResponse().getReturnFault();
				txnStatus = new TransactionStatus();
				txnStatus.setCode(fault.getStatusCode().getCode());
				txnStatus.setDescription(fault.getMessage());
				txnStatus.setComponent(fault.getComponent());
	
		boolean result = ((UpdateSubscriberResponse)this.getResponse()).getResult();
		LOGGER.debug("Invoking update subscriber response!!");
		ISubscriberResponse subscriberClient=ServiceResolver.getSubscriberResponseClient();
		subscriberClient.updateSubscriber(this.getRequestId(),txnStatus,result);
		LOGGER.debug("update susbcriber response posted");
		
	}


}
