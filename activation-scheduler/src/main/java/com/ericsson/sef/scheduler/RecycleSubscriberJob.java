package com.ericsson.sef.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.commons.read.Tag;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
//import com.ericsson.sm.api.subscriber.ContractState;
//import com.ericsson.sm.api.subscriber.Meta;
//import /com.ericsson.sm.api.subscriber.Subscriber;
//import com.ericsson.sm.api.subscriber.SubscriberManagement;
//import com.ericsson.sm.api.subscriber.WSException;
//import com.ericsson.sm.core.Tag;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;

public class RecycleSubscriberJob implements Job {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String msisdn = (String) context.getJobDetail().getJobDataMap().get(SchedulerContext.MSISDN);
		logger.info("RecycleSubscriberJob executing job id : "+ context.getJobDetail().getKey().getName()
				+ " Job Details  : " + context.getJobDetail().getJobDataMap().toString());
		/*SubscriberManagement management = SchedulerContext.getSubscriberManagement();
		try {
			Subscriber subscriber = management.getSubscriberProfile(msisdn, null);
			
			List<Meta> metas = new ArrayList<Meta>();
			metas.add(new Meta("federation-profile", "modifyTagging"));
			metas.add(new Meta("tagging", String.valueOf(Tag.RECYCLE.getSmartId()))); 
			if(subscriber.getContractState() == String.valueOf(ContractState.PREACTIVE)) {
				management.changeContractState(msisdn, ContractState.RECYCLED, metas);
			}
		} catch (WSException e) {
			log.error("Error while excuting the preactive to recycle job for msisdn: " + msisdn);
			throw new JobExecutionException(e);
		}*/
		
		Subscriber subscriber = null;
		try{
			String requestId = RequestContextLocalStore.get().getRequestId();
			List<com.ericsson.sef.bes.api.entities.Meta> metaList = new ArrayList<com.ericsson.sef.bes.api.entities.Meta>();
			metaList.add(new com.ericsson.sef.bes.api.entities.Meta("READ_SUBSCRIBER", "ENTIRE_READ_SUBSCRIBER"));
			SubscriberInfo subscriberInfo = readEntireSubscriberInfo(requestId,
					msisdn, metaList);

			logger.debug("subscriberInfo returned is " +subscriberInfo);
			subscriber = subscriberInfo.getSubscriber();
			if(subscriber == null){
				throw new RuntimeException("User with userID: " + msisdn + " does not exist anymore: ");
			}
			List<Meta> metas = new ArrayList<Meta>();
			metas.add(new Meta("federation-profile", "modifyTagging"));
			metas.add(new Meta("tagging", String.valueOf(Tag.RECYCLE.getSmartId()))); 
			if(subscriber.getContractState() == String.valueOf(ContractState.PREACTIVE)) {
				//To Do make a call to Update Subscriber with required parameters and Usecase.
			}
			}catch(Exception e1){
				throw new RuntimeException(e1);
			}
		
	}
	
	private  SubscriberInfo readEntireSubscriberInfo(String requestId,
			String subscriberId, List<com.ericsson.sef.bes.api.entities.Meta> metas) {
		ISubscriberRequest iSubscriberRequest = SchedulerContext.getBean(ISubscriberRequest.class);
		SubscriberInfo subInfo = new SubscriberInfo();
		SubscriberResponseStore.put(requestId, subInfo);
		iSubscriberRequest.readSubscriber(requestId, subscriberId, metas);
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster()
				.getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
		}
		logger.info("Check if response received for read subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		return subscriberInfo;

	}
	
	private SubscriberInfo updateSubscriber(String requestId,
			String customer_id, List<Meta> metas,String useCase) throws SmException {
		logger.info("Invoking update subscriber on tx-engine subscriber interface");
		ISubscriberRequest iSubscriberRequest = SchedulerContext.getBean(ISubscriberRequest.class);
				 
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
		logger.info("Check if response received for update subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore
				.remove(requestId);
		
		return subscriberInfo;
	}
}
