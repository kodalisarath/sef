package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionServiceHelper;
import com.ericsson.raso.sef.bes.engine.transaction.entities.UpdateSubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.UpdateSubscriberResponse;
import com.ericsson.raso.sef.bes.engine.transaction.orchestration.AbstractStepResult;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscriber.ISubscriberResponse;

public class UpdateSubscriber extends AbstractTransaction{
	
	private static final long serialVersionUID = 7686721923498952231L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSubscriber.class);
	
	public UpdateSubscriber(String requestId,String subscriberId,Map<String,String> metas) {
		super(requestId, new UpdateSubscriberRequest(requestId, subscriberId,metas));
		this.setResponse(new UpdateSubscriberResponse(requestId,true));
		LOGGER.debug("FFE Sanity Check: " + subscriberId + ", " + metas);
	}

	@Override
	public Boolean execute() throws TransactionException {
		LOGGER.debug("Entering Update Subscriber...");
		com.ericsson.raso.sef.core.db.model.Subscriber subscriberEntity = null;
		
		try {
			//This enity must contains the subscriber and his  metas from the DB
			subscriberEntity = ((UpdateSubscriberRequest)this.getRequest()).persistableEntity();
			SubscriberService subscriberStore = SefCoreServiceResolver.getSusbcriberStore();
			if (subscriberStore == null) {
				LOGGER.error("Unable to access persistence tier service!!");
				this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), 
										new ResponseCode(1006, "Unable to access persistence tier service!! Check configuration (beans.xml)")));
				return false;
			}
			if(subscriberEntity == null) {
				LOGGER.error("Subscriber not found in database" );
				
				this.getResponse().setReturnFault(new TransactionException("tx-engine", new ResponseCode(504,"Subscriber not found")));
				sendResponse();
				return false;
			}
			
			else{
				if(ContractState.PREACTIVE.getName().equals(subscriberEntity.getContractState())){
					List<Meta> listMetas = ((UpdateSubscriberRequest)this.getRequest()).getRequestMetas();
					LOGGER.debug("Iterating the metas from then processor");
					for(Meta meta:listMetas){
						if(subscriberEntity.getMetas().contains(meta)){
							try {
								subscriberStore.updateMeta(this.getRequestId(),subscriberEntity.getMsisdn(),meta);
							} catch (PersistenceError e) {
								LOGGER.error("Error in the updatemeta at UpdateSubscriber",e);
							}
						}else{
							try {
								LOGGER.debug("Metas doesnot contain in the DB,creating now!!!!");
								subscriberStore.createMeta(this.getRequestId(), subscriberEntity.getMsisdn(),meta);
							} catch (PersistenceError e) {
								LOGGER.error("Error in the createmeta at UpdateSubscriber",e);
							}
						}
						
					}
					
				}else{
					
					this.getResponse().setReturnFault(new TransactionException("tx-engine", new ResponseCode(4020,"Invalid Operation State")));
					
				}
			}
			LOGGER.debug("Got Persistable Entity: Subscriber: " + subscriberEntity);
				
		} catch (FrameworkException e1) {
			LOGGER.error("Method:Update Subscriber framework Exception",e1);
			this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), 
											new ResponseCode(1006, "Unable to pack the workflow tasks for this use-case"), e1));
			sendResponse();
			return false;
		}
		
		
		/*if (subscriberStore == null) {
			LOGGER.error("Unable to access persistence tier service!!");
			this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), 
									new ResponseCode(1006, "Unable to access persistence tier service!! Check configuration (beans.xml)")));
			return false;
		}*/
		//It contains the metas we get from the processor
	
		
	/*	LOGGER.debug("About to persist Subscriber: " + subscriberEntity);
		try {
			subscriberStore.updateSubscriber(this.getRequestId(), subscriberEntity);
		} catch (PersistenceError e1) {
			String errorCode;
			LOGGER.debug("Persistence error while updating subscriber",e1);
			if(e1.getStatusCode().getCode() == 9000){
				errorCode="504";
			}else{
				errorCode="4020";
			}
			this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), new ResponseCode(Integer.valueOf(errorCode), "Saving metas failed!!"), e1));
			return false;
		}
		LOGGER.debug("Update Subscriber successfull!!");
		LOGGER.debug("calling setMetas ");
		try {
			subscriberStore.updateMetas(this.getRequestId(), subscriberEntity.getUserId(),
						TransactionServiceHelper.getSefCoreList(((UpdateSubscriberRequest)this.getRequest()).getMetas()));
			
		} catch (PersistenceError e) {
			LOGGER.debug("Persistence error while updating subscriber metas",e);
			this.getResponse().setReturnFault(new TransactionException(this.getRequestId(), new ResponseCode(1004, "Saving metas failed!!"), e));
			return false;
		}*/
		sendResponse();
		
		return true;
	}

	@Override
	public void sendResponse() {
		
		LOGGER.debug("Invoking update subscriber response ");
		TransactionStatus txnStatus=new TransactionStatus();
		boolean result = true;
		if (this.getResponse() != null) {
			if (this.getResponse().getAtomicStepResults() != null) {
				for (AbstractStepResult stepResult: this.getResponse().getAtomicStepResults().values()) {
					if (stepResult.getResultantFault() != null) {
						txnStatus.setComponent(stepResult.getResultantFault().getComponent());
						txnStatus.setCode(stepResult.getResultantFault().getStatusCode().getCode());
						txnStatus.setDescription(stepResult.getResultantFault().getStatusCode().getMessage());
						LOGGER.debug("UpdateSubscriber::=> Transaction Status: " + txnStatus);
						result = false;
						break;
					}
				}
			}
		}else
			result=false;
		
		if (result != false)
			result = true;
		LOGGER.debug("UpdateSubscriber::=> Functional Result: " + result);
		
		LOGGER.debug("Invoking update subscriber response!!");
		ISubscriberResponse subscriberClient = ServiceResolver.getSubscriberResponseClient();
		if (subscriberClient != null) {
			LOGGER.debug("Gonna Send send response");
			subscriberClient.updateSubscriber(this.getRequestId(), txnStatus, result);
			LOGGER.debug("update susbcriber response posted");
		} else {
			LOGGER.error("Unable to acquire client access to response interface. Request will time-out in the consumer side!!");
		}
		LOGGER.debug("update susbcriber response posted");
	}


}
