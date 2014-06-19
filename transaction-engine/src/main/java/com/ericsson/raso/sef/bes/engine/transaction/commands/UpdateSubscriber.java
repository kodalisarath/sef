package com.ericsson.raso.sef.bes.engine.transaction.commands;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.UpdateSubscriberRequest;
import com.ericsson.raso.sef.bes.engine.transaction.entities.UpdateSubscriberResponse;
import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.sef.bes.api.entities.TransactionStatus;
import com.ericsson.sef.bes.api.subscriber.ISubscriberResponse;

public class UpdateSubscriber extends AbstractTransaction {
	static final String PRE_ACTIVE = "PRE_ACTIVE";

	private static final long serialVersionUID = 7686721923498952231L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UpdateSubscriber.class);

	public UpdateSubscriber(String requestId, String subscriberId,Map<String, String> metas,String useCase) {
		super(requestId, new UpdateSubscriberRequest(requestId, subscriberId,metas,useCase));
		this.setResponse(new UpdateSubscriberResponse(requestId, true));
		LOGGER.debug("FFE Sanity Check: " + subscriberId + ", " + metas);
	}

	@Override
	public Boolean execute() throws TransactionException {
		LOGGER.debug("Entering Update Subscriber...");
		com.ericsson.raso.sef.core.db.model.Subscriber subscriberEntity = null;
		SubscriberService subscriberStore = SefCoreServiceResolver.getSusbcriberStore();

		try {

			if (subscriberStore == null) {
				LOGGER.error("Unable to access persistence tier service!!");
				this.getResponse().setReturnFault(new TransactionException(this.getRequestId(),new ResponseCode(1006,"Unable to access persistence tier service!! Check configuration (beans.xml)")));
				sendResponse();
			}
			List<Meta> listMetas = ((UpdateSubscriberRequest) this.getRequest()).getRequestMetas();
			String useCaseProcess = ((UpdateSubscriberRequest) this.getRequest()).getRequestUseCase();
			//Differentiated use cases for updating  subscriber and metas
			switch(useCaseProcess){

			case Constants.CreateOrWriteROP: 
			case Constants.CreateOrWriteServiceAccessKey:
			case Constants.VersionCreateOrWriteRop:
			case Constants.BucketCreateOrWriteRop:
			case Constants.VersionCreateOrWriteCustomer:

				LOGGER.debug("called createorwrite case in transaction manager");
				// This entity must contains the subscriber and his meta from the DB
				
				LOGGER.debug("Getting the request: " + this.getRequest());
				subscriberEntity = ((UpdateSubscriberRequest) this.getRequest()).persistableEntity();


				if (subscriberEntity == null) {
					LOGGER.error("Subscriber not found in database");
					this.getResponse().setReturnFault(new TransactionException("tx-engine", new ResponseCode(504, "Subscriber not found")));
					sendResponse();
				} else {
					LOGGER.debug("Printing Subscriber entity "+subscriberEntity.toString());
					LOGGER.debug("Subscriber exists and checking for the preactive state");
					LOGGER.debug("checking if this equals working fine ");
					if (ContractState.PREACTIVE.getName().equals(subscriberEntity.getContractState().getName())) {
						LOGGER.debug("It is PRE_ACTIVE state");
						for (Meta meta : listMetas) {
							LOGGER.debug("Printing the metas in the loop "+meta.getKey()+" "+meta.getValue()+""+subscriberEntity.getMsisdn());
							if (subscriberEntity.getMetas().contains(meta)) {
								try {																																									
									subscriberStore.updateMeta(this.getRequestId(),
											subscriberEntity.getMsisdn(), meta);
								} catch (PersistenceError e) {
									LOGGER.error("Error in the updatemeta at UpdateSubscriber",e);
								}
							} else {
								try {
									LOGGER.debug("Metas doesnot contain in the DB,creating now!!!!");
									subscriberStore.createMeta(this.getRequestId(),
											subscriberEntity.getMsisdn(), meta);
								} catch (PersistenceError e) {
									LOGGER.error("Error in the createmeta at UpdateSubscriber",e);
								}
							}

						}

					} else {
						this.getResponse().setReturnFault(
								new TransactionException("tx-engine",new ResponseCode(4020,"Invalid Operation State")));
						sendResponse();
					}
				}
				break;
				
			case Constants.RetrieveDelete:
				LOGGER.debug("Invoked RetrieveDelete Case");
				// This entity must contains the subscriber and his meta from the DB
				
				LOGGER.debug("Getting the request: " + this.getRequest());
				subscriberEntity = ((UpdateSubscriberRequest) this.getRequest()).persistableEntity();


				if (subscriberEntity == null) {
					LOGGER.error("Subscriber not found in database");
					this.getResponse().setReturnFault(new TransactionException("tx-engine", new ResponseCode(504, "Subscriber not found")));
					sendResponse();
				}
						subscriberStore.deleteSubscriber(this.getRequestId(),subscriberEntity);
				break;
				
			case Constants.ModifyCustomerPreActive:	
			case Constants.ModifyCustomerGrace:
				subscriberEntity = ((UpdateSubscriberRequest) this.getRequest()).persistableEntity();
				LOGGER.debug("Invoked ModifyCustomer Case");
				for(Meta meta:listMetas){
                  LOGGER.debug("In the for loop ");
					if(meta.getKey().equalsIgnoreCase("PreActiveEndDate")){
						LOGGER.debug("SK SK HERE" + this.getRequestId() + " " + ((UpdateSubscriberRequest) this.getRequest()).getSubscriberId() + " " + meta );
						subscriberStore.updateMeta(this.getRequestId(),((UpdateSubscriberRequest) this.getRequest()).getSubscriberId(), meta);
						
					}
					else if(meta.getKey().equalsIgnoreCase("GraceEndDate")){
						LOGGER.debug("SK SK HERE" + this.getRequestId() + " " + ((UpdateSubscriberRequest) this.getRequest()).getSubscriberId() + " " + meta );
						subscriberStore.updateMeta(this.getRequestId(),((UpdateSubscriberRequest) this.getRequest()).getSubscriberId(), meta);
						
					}
					else if(meta.getKey().equalsIgnoreCase("MessageId")){
						LOGGER.debug("SK SK HERE" + this.getRequestId() + " " + ((UpdateSubscriberRequest) this.getRequest()).getSubscriberId() + " " + meta );
						subscriberStore.updateMeta(this.getRequestId(),((UpdateSubscriberRequest) this.getRequest()).getSubscriberId(), meta);
						
					}
					else if(meta.getKey().equalsIgnoreCase("EventInfo")){
						LOGGER.debug("SK SK HERE" + this.getRequestId() + " " + ((UpdateSubscriberRequest) this.getRequest()).getSubscriberId() + " " + meta );
						subscriberStore.updateMeta(this.getRequestId(),((UpdateSubscriberRequest) this.getRequest()).getSubscriberId(), meta);
						
					}
					else if(meta.getKey().equalsIgnoreCase("AccessKey")){
						LOGGER.debug("SK SK HERE" + this.getRequestId() + " " + ((UpdateSubscriberRequest) this.getRequest()).getSubscriberId() + " " + meta );
						subscriberStore.updateMeta(this.getRequestId(),((UpdateSubscriberRequest) this.getRequest()).getSubscriberId(), meta);
						
					}
					else
					{
						LOGGER.debug("META PREACTIVENDDATE IS" + this.getRequestId() + " " + ((UpdateSubscriberRequest) this.getRequest()).getSubscriberId() + " " + meta );
						subscriberStore.createMeta(this.getRequestId(),((UpdateSubscriberRequest) this.getRequest()).getSubscriberId(), meta);
					}
					
					
				}
				break;
			case Constants.SubscribePackageItem:
				subscriberEntity = ((UpdateSubscriberRequest) this.getRequest()).persistableEntity();
				LOGGER.debug("Invoked SubscribePackageItem ");
				for(Meta meta:listMetas){
					subscriberStore.updateMeta(this.getRequestId(),((UpdateSubscriberRequest) this.getRequest()).getSubscriberId(), meta);

				}
				break;

			case Constants.UnSubscribePackageItem:
				subscriberEntity = ((UpdateSubscriberRequest) this.getRequest()).persistableEntity();
				LOGGER.debug("Invoked SubscribePackageItem ");
				for(Meta meta:listMetas){
					subscriberStore.updateMeta(this.getRequestId(),((UpdateSubscriberRequest) this.getRequest()).getSubscriberId(), meta);
				}
				break;

			case Constants.ModifyTagging:
				//subscriberEntity = ((UpdateSubscriberRequest) this.getRequest()).persistableEntity();
				LOGGER.debug("Invoked ModifyTagging Case");
				// This entity must contains the subscriber and his meta from the DB for modify tagging
				subscriberEntity = ((UpdateSubscriberRequest) this.getRequest()).persistableEntity();
				if (subscriberEntity == null) {
					LOGGER.error("Subscriber not found in database");
					this.getResponse().setReturnFault(new TransactionException("tx-engine", new ResponseCode(504, "Subscriber not found")));
					sendResponse();
					return true;
				} else {
					listMetas = ((UpdateSubscriberRequest) this.getRequest()).getRequestMetas();
					LOGGER.debug("Update Subscriber Metas are :", listMetas);
					if(!listMetas.isEmpty()){
						if(ContractState.apiValue("PREACTIVE").toString().equals(subscriberEntity.getContractState().toString()) 
								|| ContractState.apiValue("ACTIVE").toString().equalsIgnoreCase(subscriberEntity.getContractState().toString()) 
								|| ContractState.apiValue("GRACE").toString().equalsIgnoreCase(subscriberEntity.getContractState().toString())){
							LOGGER.debug("Subscriber Contract State for Tagging is :", subscriberEntity.getContractState().toString());
							// if subscriber state is active,preActive,grace then tagging required
							// get segmentation code for Subscriber based on tag value
							LOGGER.debug("Subscriber Contract State for Tagging is :", subscriberEntity.getContractState().toString());
							LOGGER.debug("Check for the metas from processors :"+ listMetas.size());
							String modifyTaggingCodeKey = null;
							String modifyTaggingCodeValue = null;
							String validTagKey = null;
							String validTagValue = null;
							boolean tagCode = Boolean.FALSE;
							for (Meta metas : listMetas) {
								modifyTaggingCodeKey = metas.getKey();
								modifyTaggingCodeValue = metas.getValue();
								if(modifyTaggingCodeValue != null){
									if(modifyTaggingCodeValue.equalsIgnoreCase("invalidBit")){
										tagCode = Boolean.TRUE;
									}
								}
							}
							if(tagCode){
								if(ContractState.apiValue("GRACE").toString().equalsIgnoreCase(subscriberEntity.getContractState().toString())){
									this.getResponse().setReturnFault(
											new TransactionException("tx-engine", new ResponseCode(
													504, "Invalid Operation State GRACE")));
								}else if(ContractState.apiValue("ACTIVE").toString().equalsIgnoreCase(subscriberEntity.getContractState().toString())){
									this.getResponse().setReturnFault(
											new TransactionException("tx-engine", new ResponseCode(
													4020, "Invalid Operation State ACTIVE")));
								}else{
									this.getResponse().setReturnFault(
											new TransactionException("tx-engine", new ResponseCode(
													4020, "Invalid Operation State PRE-ACTIVE")));
								}
								sendResponse();
								return false;
							}else{
								this.sendResponse();
								return true;
							}
						}

						// for Subscriber Recycle state and Unknown Subscriber State tagging not required
						else{
							if(ContractState.apiValue("RECYCLED").toString().equalsIgnoreCase(subscriberEntity.getContractState().toString())){
								LOGGER.error("Subscriber State is Recycled");
								this.getResponse().setReturnFault(
										new TransactionException("tx-engine", new ResponseCode(
												504, "Invalid Operation State RECYCLED")));
								sendResponse();
								return false;
							}
						}
					}
				}

				break;
			}

			LOGGER.debug("Got Persistable Entity: Subscriber: "
					+ subscriberEntity);

		} catch (FrameworkException e1) {
			LOGGER.error("Method:Update Subscriber framework Exception", e1);
			this.getResponse()
			.setReturnFault(
					new TransactionException(
							this.getRequestId(),
							new ResponseCode(1006,
									"Unable to pack the workflow tasks for this use-case"),
									e1));
			sendResponse();
			return false;
		}

		/*
		 * if (subscriberStore == null) {
		 * LOGGER.error("Unable to access persistence tier service!!");
		 * this.getResponse().setReturnFault(new
		 * TransactionException(this.getRequestId(), new ResponseCode(1006,
		 * "Unable to access persistence tier service!! Check configuration (beans.xml)"
		 * ))); return false; }
		 */
		// It contains the metas we get from the processor

		/*
		 * LOGGER.debug("About to persist Subscriber: " + subscriberEntity); try
		 * { subscriberStore.updateSubscriber(this.getRequestId(),
		 * subscriberEntity); } catch (PersistenceError e1) { String errorCode;
		 * LOGGER.debug("Persistence error while updating subscriber",e1);
		 * if(e1.getStatusCode().getCode() == 9000){ errorCode="504"; }else{
		 * errorCode="4020"; } this.getResponse().setReturnFault(new
		 * TransactionException(this.getRequestId(), new
		 * ResponseCode(Integer.valueOf(errorCode), "Saving metas failed!!"),
		 * e1)); return false; }
		 * LOGGER.debug("Update Subscriber successfull!!");
		 * LOGGER.debug("calling setMetas "); try {
		 * subscriberStore.updateMetas(this.getRequestId(),
		 * subscriberEntity.getUserId(),
		 * TransactionServiceHelper.getSefCoreList(
		 * ((UpdateSubscriberRequest)this.getRequest()).getMetas()));
		 * 
		 * } catch (PersistenceError e) {
		 * LOGGER.debug("Persistence error while updating subscriber metas",e);
		 * this.getResponse().setReturnFault(new
		 * TransactionException(this.getRequestId(), new ResponseCode(1004,
		 * "Saving metas failed!!"), e)); return false; }
		 */
		sendResponse();

		return true;
	}

	@Override
	public void sendResponse() {

		LOGGER.debug("Invoking update subscriber response ");
		TransactionStatus txnStatus = new TransactionStatus();
		boolean result = true;
		if (this.getResponse() != null) {
			if (this.getResponse() != null && this.getResponse().getReturnFault() != null) {
				TransactionException fault = this.getResponse().getReturnFault();
				if (fault != null) {
					txnStatus.setCode(fault.getStatusCode().getCode());
					txnStatus.setDescription(fault.getStatusCode().getMessage());
					txnStatus.setComponent(fault.getComponent());
				}
			}
		} else
			result = false;
		//What are you doing here?? not required
		/*if (this.getResponse() != null) {
			if (this.getResponse().getAtomicStepResults() != null) {
				for (AbstractStepResult stepResult : this.getResponse()
						.getAtomicStepResults().values()) {
					if (stepResult.getResultantFault() != null) {
						txnStatus.setComponent(stepResult.getResultantFault()
								.getComponent());
						txnStatus.setCode(stepResult.getResultantFault()
								.getStatusCode().getCode());
						txnStatus.setDescription(stepResult.getResultantFault()
								.getStatusCode().getMessage());
						LOGGER.debug("UpdateSubscriber::=> Transaction Status: "
								+ txnStatus);
						result = false;
						break;
					}
				}
			}
		}*/

		if (result != false)
			result = true;
		LOGGER.debug("UpdateSubscriber::=> Functional Result: " + result);

		LOGGER.debug("Invoking update subscriber response!!");
		ISubscriberResponse subscriberClient = ServiceResolver
				.getSubscriberResponseClient();
		if (subscriberClient != null) {
			LOGGER.debug("Gonna Send send response");
			subscriberClient.updateSubscriber(this.getRequestId(), txnStatus,
					result);
			LOGGER.debug("update susbcriber response posted");
		} else {
			LOGGER.error("Unable to acquire client access to response interface. Request will time-out in the consumer side!!");
		}
		LOGGER.debug("update susbcriber response posted");
	}

}
