package com.ericsson.raso.sef.notification.workflows.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.Task;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.notification.workflows.NotificationContext;
import com.ericsson.raso.sef.notification.workflows.callingcircle.CallingCircleUtil;
import com.ericsson.raso.sef.notification.workflows.common.WorkflowEngineServiceHelper;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.ericsson.sef.scheduler.command.AbortRecycleCommand;
/*import com.ericsson.sm.api.subscriber.ContractState;
import com.ericsson.sm.api.subscriber.SubscriberManagement;
import com.ericsson.sm.core.SmException;
import com.ericsson.sm.core.Task;
import com.ericsson.sm.scheduler.command.AbortRecycleCommand;
*/

public class ActivateSubscriberTask implements Task<Void> {

	private static Logger log = LoggerFactory.getLogger(ActivateSubscriberTask.class);
	
	private String msisdn;
	
	public ActivateSubscriberTask(String msisdn) {
		this.msisdn = msisdn;
	}

	@Override
	public Void execute() throws SmException {
		log.debug("Executing ActivateSubscriberTask");

		//Subscriber subscriber = CallingCircleUtil.getSubscriberByMsisdn(msisdn);
		//log.debug("subscriber returned is " +subscriber);
		//Subscriber subscriber = subscriberInfo.getSubscriber();
		 
		//SubscriberManagement sm = NotificationContext.getBean(SubscriberManagement.class);
		try {
			//TODO How to change contract state?
			//sm.changeContractState(msisdn, ContractState.ACTIVE, null);
			//AbortRecycleCommand command = new AbortRecycleCommand(sm.getSubscriberProfile(msisdn, null));

			AbortRecycleCommand command = new AbortRecycleCommand(msisdn);
			command.execute();
		}  catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new SmException(e);
		}
		log.debug("Executing ActivateSubscriberTask completed");
		return null;
	}
	
//	private  SubscriberInfo readEntireSubscriberInfo(String subscriberId) 
//	{
//		String requestId = RequestContextLocalStore.get().getRequestId();
//		List<Meta> metaList = new ArrayList<Meta>();
//		metaList.add(new Meta("READ_SUBSCRIBER", "ENTIRE_READ_SUBSCRIBER"));
//		ISubscriberRequest iSubscriberRequest = ServiceResolver.getISubscriberRequestClient();
//		SubscriberInfo subInfo = new SubscriberInfo();
//		SubscriberResponseStore.put(requestId, subInfo);
//		iSubscriberRequest.readSubscriber(requestId, subscriberId, metaList);
//		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
//				.getSemaphore(requestId);
//		try {
//			semaphore.init(0);
//			semaphore.acquire();
//		} catch (InterruptedException e) {
//		}
//		log.info("Check if response received for read subscriber");
//		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
//				.remove(requestId);
//		return subscriberInfo;
//
//	}

}
