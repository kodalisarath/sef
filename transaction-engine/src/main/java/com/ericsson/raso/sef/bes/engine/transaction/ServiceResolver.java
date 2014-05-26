package com.ericsson.raso.sef.bes.engine.transaction;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ericsson.raso.sef.bes.engine.transaction.service.ISubscriberResponse;
import com.ericsson.raso.sef.bes.prodcat.service.IOfferCatalog;
import com.ericsson.raso.sef.bes.prodcat.service.IServiceRegistry;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.fulfillment.FulfillmentRequest;
import com.ericsson.sef.bes.api.subscription.ISubscriptionResponse;

public class ServiceResolver implements ApplicationContextAware {

	public static ApplicationContext context = null; 

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		ServiceResolver.context = context;
	}
	
	public static IOfferCatalog getOfferCatalog() {
		return context.getBean(IOfferCatalog.class);
	}

	public static IServiceRegistry getServiceRegistry() {
		return context.getBean(IServiceRegistry.class);
	}
	
	public static TransactionManager getTransactionManager() {
		return context.getBean(TransactionManager.class);
	}
	
	public static ISubscriberResponse getSubscriberResponseClient() {
		return context.getBean(ISubscriberResponse.class);
	}
	
	public static ISubscriptionResponse getSubscriptionResponseClient() {
		return context.getBean(ISubscriptionResponse.class);
	}
	
	public static FulfillmentRequest getFulfillmentRequestClient() {
		return context.getBean(FulfillmentRequest.class);
	}
	
	
	//Method  to parse com.ericsson.sef.bes.api.entities.Subscriber object values to 
		//com.ericsson.raso.sef.core.db.model.Subscriber
		//called from the subscriber processor classes
		public static com.ericsson.raso.sef.bes.engine.transaction.entities.Subscriber parseToCoreSubscriber(Subscriber subscriber){
			com.ericsson.raso.sef.bes.engine.transaction.entities.Subscriber subscriberModel=new com.ericsson.raso.sef.bes.engine.transaction.entities.Subscriber();
			subscriberModel.setUserId(subscriber.getUserId());
			subscriberModel.setCustomerId(subscriber.getCustomerId());
			subscriberModel.setContractId(subscriber.getContractId());
			subscriberModel.setMsisdn(subscriber.getMsisdn());
			subscriberModel.setPin(subscriber.getPin());
			subscriberModel.setEmail(subscriber.getEmail());
			subscriberModel.setImsi(subscriber.getImsi());
			subscriberModel.setImeiSv(subscriber.getImeiSv());
			subscriberModel.setPaymentType(subscriber.getPaymentType());
			subscriberModel.setPaymentResponsible(subscriber.getPaymentResponsible());
			subscriberModel.setPaymentParent(subscriber.getPaymentParent());
			subscriberModel.setBillCycleDay(subscriber.getBillCycleDay());
			subscriberModel.setContractState(subscriber.getContractState());
			subscriberModel.setDateOfBirth(subscriber.getDateOfBirth());
			subscriberModel.setGender(subscriber.getGender());
			subscriberModel.setPrefferedLanguage(subscriber.getPrefferedLanguage());
			subscriberModel.setRegistrationDate(subscriber.getRegistrationDate());
			subscriberModel.setActiveDate(subscriber.getActiveDate());
			subscriberModel.setRatePlan(subscriber.getRatePlan());
			subscriberModel.setCustomerSegment(subscriber.getCustomerSegment());
			subscriberModel.setCreated(subscriber.getCreated());
			subscriberModel.setLastModified(subscriber.getLastModified());
			subscriberModel.setMetas(subscriber.getMetas());
			return subscriberModel;
			
		}


}