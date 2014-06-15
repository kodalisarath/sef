package com.ericsson.raso.sef.bes.engine.transaction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.entities.AbstractQuotaCharacteristic;
import com.ericsson.raso.sef.bes.prodcat.entities.AbstractTimeCharacteristic;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.DigitalAsset;
import com.ericsson.raso.sef.bes.prodcat.entities.HardDateTime;
import com.ericsson.raso.sef.bes.prodcat.entities.LimitedQuota;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.entities.UnlimitedQuota;
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.sef.bes.api.entities.Offer;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.Subscriber;


public abstract class TransactionServiceHelper {
	
	private final static Logger logger = LoggerFactory.getLogger(TransactionServiceHelper.class);
	
	public static com.ericsson.raso.sef.core.db.model.Subscriber fetchSubscriberFromDb(String subscriberId) throws TransactionException {
		SubscriberService subscriberStore = SefCoreServiceResolver.getSusbcriberStore();
		logger.debug("Subscriber Store:getSubscriberStore"+subscriberStore);
		if (subscriberStore == null)
			throw new TransactionException("txe", new ResponseCode(11614, "Unable to fetch Subscriber Profile for further processing of the request!! Please check configuration"));
		try {
			return subscriberStore.getSubscriber("dynamic-task", subscriberId);
		} catch (PersistenceError e) {
			logger.error("Persistence Issue fetching subscriber: " + subscriberId + ", Cause: " + e.getMessage(), e);
			throw new TransactionException("txe", new ResponseCode(11614, "Unable to fetch Subscriber Profile - Error on DB"));
		}
		
	}
	
	
	public static Subscriber getApiEntity(com.ericsson.raso.sef.core.db.model.Subscriber subscriber) {
		Subscriber returned = new Subscriber();
		if(subscriber != null){
			if(subscriber.getBillCycleDay() != null){
				returned.setBillCycleDay(subscriber.getBillCycleDay());
			}
			if(subscriber.getContractId() != null){
				returned.setContractId(subscriber.getContractId());	
			}
			if(subscriber.getContractState() != null){
				returned.setContractState(subscriber.getContractState().getName());	
			}
			if(subscriber.getCustomerId() != null){
				returned.setCustomerId(subscriber.getCustomerId());
			}
			if(subscriber.getCustomerSegment() != null){
				returned.setCustomerSegment(subscriber.getCustomerSegment());
			}
			if(subscriber.getEmail() != null){
				returned.setEmail(subscriber.getEmail());
			}
			if(subscriber.getGender() != null){
				returned.setGender(subscriber.getGender());
			}
			if(subscriber.getImeiSv() != null){
				returned.setImeiSv(subscriber.getImeiSv());
			}
			if(subscriber.getImsi() != null){
				returned.setImsi(subscriber.getImsi());
			}
			if(subscriber.getMetas() != null){
				logger.debug("SANITY CHECK: subscriber.metas= " + subscriber.getMetas());
				returned.setMetas(getMetas(subscriber.getMetas())); //TODO: this will need refactoring API entity, since SOAP does not support Map<K,V>
			}
			if(subscriber.getMsisdn() != null){
				returned.setMsisdn(subscriber.getMsisdn());
			}
			if(subscriber.getPaymentParent() != null){
				returned.setPaymentParent(subscriber.getPaymentParent());
			}
			if(subscriber.getPaymentResponsible() != null){
				returned.setPaymentResponsible(subscriber.getPaymentResponsible());
			}
			if(subscriber.getPaymentType() != null){
				returned.setPaymentType(subscriber.getPaymentType());
			}
			if(subscriber.getPin() != null){
				returned.setPin(subscriber.getPin());
			}
			if(subscriber.getPrefferedLanguage() != null){
				returned.setPrefferedLanguage(subscriber.getPrefferedLanguage());
			}
			if(subscriber.getRatePlan() != null){
				returned.setRatePlan(subscriber.getRatePlan());
			}
			if(subscriber.getRegistrationDate() != null){
				returned.setRegistrationDate(subscriber.getRegistrationDate().getTime());
			}
			if(subscriber.getUserId() != null){
				returned.setUserId(subscriber.getUserId());
			}
		}
		
		return returned;
		
	}
	
	public static com.ericsson.raso.sef.core.db.model.Subscriber getPersistableEntity(Subscriber subscriber) {
		com.ericsson.raso.sef.core.db.model.Subscriber returned = new com.ericsson.raso.sef.core.db.model.Subscriber();
		if(subscriber != null){
			if(subscriber.getCustomerId() != null){
				returned.setAccountId(subscriber.getCustomerId());
				returned.setCustomerId(subscriber.getCustomerId());
			}
			if(subscriber.getContractId() != null){
				returned.setContractId(subscriber.getContractId());
			}
			if(subscriber.getCustomerSegment() != null){
				returned.setCustomerSegment(subscriber.getCustomerSegment());
			}
			if(subscriber.getContractState() != null){
				returned.setContractState(ContractState.valueOf(subscriber.getContractState()));
			}
			if(subscriber.getEmail() != null){
				returned.setEmail(subscriber.getEmail());
			}
			if(subscriber.getImeiSv() != null){
				returned.setImeiSv(subscriber.getImeiSv());
			}
			if(subscriber.getImsi() != null){
				returned.setImsi(subscriber.getImsi());
			}
			if(subscriber.getMetas() != null){
				returned.setMetas(getMetas(subscriber.getMetas()));
			}
			if(subscriber.getMsisdn() != null){
				returned.setMsisdn(subscriber.getMsisdn());
			}
			if(subscriber.getPin() != null){
				returned.setPin(subscriber.getPin());
			}
			if(subscriber.getPaymentType() != null){
				returned.setPaymentType(subscriber.getPaymentType());
			}
			if(subscriber.getPrefferedLanguage() != null){
				returned.setPrefferedLanguage(subscriber.getPrefferedLanguage());
			}
			if(subscriber.getRatePlan() != null){
				returned.setRatePlan(subscriber.getRatePlan());
			}
			if(subscriber.getRegistrationDate() != null){
				returned.setRegistrationDate(new Date(subscriber.getRegistrationDate()));
			}
			if(subscriber.getUserId() != null){
				returned.setUserId(subscriber.getUserId());	
			}
		}
	
		return returned;
	}
	
	//Change of collection to arraylist
	private static List<Meta> getMetas(Map<String,String> metaMap) {
		logger.debug("Called getMetas TransactionService helper with meta size"+metaMap.size());
		List<Meta> metas = new ArrayList<Meta>();
		if (metaMap != null) {
			for (String key : metaMap.keySet()) {
				Meta meta = new Meta();
				meta.setKey(key);
				meta.setValue(metaMap.get(key));
				metas.add(meta);
			}
		}
		return metas;
	}

	private static Map<String, String> getMetas(Collection<Meta> subscriberMetas) {
		Map<String,String> map = new HashMap<String, String>();
		for(Meta meta : subscriberMetas) {
            // for some reason the subscriberMetas collection sometimes has a mix of null and non-null meta objects, to avoid NPEs we just don't process the null ones.
            if(meta == null) continue;
            
			map.put(meta.getKey(), meta.getValue());
		}
		return map;
	}

	
	
	public static Offer getApiEntity(com.ericsson.raso.sef.bes.prodcat.entities.Offer other) {
		Offer returned = new Offer();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
		Date dateField = null;
		
		
		returned.setName(other.getName());
		returned.setDescription(other.getDescription());
		
		// unfortunately, format class in java sdk is not synchronized and have to take this hit in every thread...
		long date = other.getRenewalPeriod().getExpiryTimeInMillis();
		if (date == -1) {
			returned.setValidity("Never Ending");
		} else {
			dateField = new Date(date);
			returned.setValidity(formatter.format(dateField));
		}

		returned.setRecurrence(other.isRecurrent());
		
		if (other.getTrialPeriod() != null) {
			date = other.getTrialPeriod().getExpiryTimeInMillis();
			if (date == -1) {
				returned.setTrial("No Trial");
			} else {
				dateField = new Date(date);
				returned.setTrial(formatter.format(dateField));
			}
		}
		
		if (other.getMinimumCommitment() != null) {
			try {
				date = other.getMinimumCommitment().getCommitmentTime(System.currentTimeMillis(), other.getRenewalPeriod().getExpiryTimeInMillis());
				if (date == -1) {
					returned.setMinimumCommitment("No Commitment");
				} else {
					dateField = new Date(date);
					returned.setMinimumCommitment(formatter.format(dateField));
				}
			} catch (CatalogException e) {
				returned.setMinimumCommitment("No Commitment");
			}
		}
		
		if (other.getAutoTermination() != null) {
			try {
				date = other.getAutoTermination().getTerminationTime(System.currentTimeMillis(), other.getRenewalPeriod().getExpiryTimeInMillis());
				if (date == -1) {
					returned.setAutoTerminate("No Automatic Termination");
				} else {
					dateField = new Date(date);
					returned.setMinimumCommitment(formatter.format(dateField));
				}
			} catch (CatalogException e) {
				returned.setMinimumCommitment("No Automatic Termination");
			}
		}
		
		returned.setPrice(other.getPrice().getSimpleAdviceOfCharge().getAmount());
		returned.setCurrency(other.getPrice().getIso4217CurrencyCode());
		
		returned.setProducts(TransactionServiceHelper.translateProducts(other.getAllAtomicProducts()));

		
		return returned;
	}
	
	public static Set<Product> translateProducts(List<AtomicProduct> allAtomicProducts) {
		Set<Product> products = new TreeSet<Product>();
		
		for (AtomicProduct source: allAtomicProducts) 
			products.add(TransactionServiceHelper.getApiEntity(source));
		
		return products;
	}

	public static Set<Product> translateProducts(Set<AtomicProduct> allAtomicProducts) {
		Set<Product> products = new HashSet<Product>();
		
		if(allAtomicProducts!=null) {
			for (AtomicProduct source: allAtomicProducts) 
			products.add(TransactionServiceHelper.getApiEntity(source));
		}
		
		return products;
	}

	public static Offer getApiEntity(com.ericsson.raso.sef.bes.prodcat.entities.Subscription subscription) {
		Offer returned = new Offer();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
		Date dateField = null;
		
		returned.setSubscriptionId(subscription.getSubscriptionId());
		returned.setName(subscription.getName());
		returned.setDescription(subscription.getDescription());
		
		long date = subscription.getRenewalPeriod().getExpiryTimeInMillis();
		if (date == -1) {
			returned.setValidity("Never Ending");
		} else {
			dateField = new Date(date);
			returned.setValidity(formatter.format(dateField));
		}

		returned.setRecurrence(subscription.isRecurrent());
		
		if (subscription.getMinimumCommitment() != null) {
			try {
				date = subscription.getMinimumCommitment().getCommitmentTime(System.currentTimeMillis(), subscription.getRenewalPeriod().getExpiryTimeInMillis());
				if (date == -1) {
					returned.setMinimumCommitment("No Commitment");
				} else {
					dateField = new Date(date);
					returned.setMinimumCommitment(formatter.format(dateField));
				}
			} catch (CatalogException e) {
				returned.setMinimumCommitment("No Commitment");
			}
		}
		
		if (subscription.getAutoTermination() != null) {
			try {
				date = subscription.getAutoTermination().getTerminationTime(System.currentTimeMillis(), subscription.getRenewalPeriod().getExpiryTimeInMillis());
				if (date == -1) {
					returned.setAutoTerminate("No Automatic Termination");
				} else {
					dateField = new Date(date);
					returned.setAutoTerminate(formatter.format(dateField));
				}
			} catch (CatalogException e) {
				returned.setAutoTerminate("No Automatic Termination");
			}
		}
		
		
		returned.setProducts(TransactionServiceHelper.translateProducts(subscription.getAllAtomicProducts()));
		
		return returned;
	}
	
	public static Product getApiEntity(AtomicProduct other) {
		Product returned = new Product();
		
		returned.setName(other.getName());
		returned.setResourceName(other.getResource().getName());
		returned.setQuotaDefined(other.getQuota().getDefinedQuota());
		returned.setQuotaConsumed(other.getQuota().getConsumedQuota());
		returned.setValidity(other.getValidity().getExpiryTimeInMillis());
		returned.setMetas(getApiMap(other.getMetas()));
		
		return returned;
	}
	

	public static AtomicProduct getApiEntity(Product other) {
		AtomicProduct returned = new AtomicProduct(other.getName());
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(other.getValidity());
		AbstractTimeCharacteristic validTime = new HardDateTime(cal.getTime());
		returned.setValidity(validTime);
		Resource resource=new DigitalAsset(other.getResourceName());
		returned.setResource(resource);
		if(other.getQuotaConsumed() == -1){
			AbstractQuotaCharacteristic quotaUnlimited=new UnlimitedQuota();
			returned.setQuota(quotaUnlimited);
		}else{
			LimitedQuota quotalimited=new LimitedQuota();
			quotalimited.setConsumedQuota(other.getQuotaConsumed());
			quotalimited.setDefinedQuota(other.getQuotaDefined());
			returned.setQuota(quotalimited);
		}
		
		returned.setMetas(getNativeMap(other.getMetas()));
		return returned;
	}
	
	public static com.ericsson.raso.sef.bes.prodcat.entities.Offer getProdcatEntity(Offer other) {
		com.ericsson.raso.sef.bes.prodcat.entities.Offer returned = new com.ericsson.raso.sef.bes.prodcat.entities.Offer(other.getName());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
		Date dateField = null;
		
	
		return returned;
		
	}
	
	public static Set<AtomicProduct> translatetoAtomicProducts(List<Product> products) {
		Set<AtomicProduct> AtomicProducts = new TreeSet<AtomicProduct>();
		for (Product source: products) 
			
			AtomicProducts.add(TransactionServiceHelper.getApiEntity(source));
		
		return AtomicProducts;
	}
	
	public static Map<String, String> getApiMap(Map<String, Object> map) {
		Map<String, String> returned = new HashMap<String, String>();
		
		for (String key: map.keySet()) {
			returned.put(key, "" + map.get(key));
		}
		
		return returned;
	}
	
	public static Map<String, Object> getNativeMap(Map<String, String> map) {
		Map<String, Object> returned = new HashMap<String, Object>();
		if (map!=null) {
		
		for (String key: map.keySet()) {
			returned.put(key, map.get(key));
		}
		
		}
		return returned;
	}



	public static com.ericsson.sef.bes.api.entities.Subscriber enrichSubscriber(com.ericsson.sef.bes.api.entities.Subscriber subscriber,
			List<Product> products) {
		
		if(products == null)
			return subscriber;
		
		String value = null;
		for (Product product: products) {
			Map<String, String> metas = product.getMetas();
			// direct attributes...
			value = metas.get(Constants.READ_SUBSCRIBER_ACTIVATION_DATE.name());
			if (value != null)
				subscriber.setActiveDate(Long.parseLong(value));
			
			value = metas.get(Constants.READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE.name());
			if (value != null)
				subscriber.addMeta(Constants.READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE.name(), value);
			
			value = metas.get(Constants.READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE.name());
			if (value != null)
				subscriber.addMeta(Constants.READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE.name(), value);
			
			// account flags
			value = metas.get(Constants.READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG.name());
			logger.debug("Activation Status flag: " + value);
			if (value != null)
				subscriber.addMeta(Constants.READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG.name(), value);
			
			value = metas.get(Constants.READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG.name());
			if (value != null)
				subscriber.addMeta(Constants.READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG.name(), value);
			
			value = metas.get(Constants.READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG.name());
			if (value != null)
				subscriber.addMeta(Constants.READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG.name(), value);
			
			value = metas.get(Constants.READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG.name());
			if (value != null)
				subscriber.addMeta(Constants.READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG.name(), value);
			
			value = metas.get(Constants.READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG.name());
			if (value != null)
				subscriber.addMeta(Constants.READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG.name(), value);
			
			value = metas.get(Constants.READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG.name());
			if (value != null)
				subscriber.addMeta(Constants.READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG.name(), value);
			
			value = metas.get(Constants.READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG.name());
			if (value != null)
				subscriber.addMeta(Constants.READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG.name(), value);
			
			

			for (String key: metas.keySet()) {
					subscriber.addMeta(key, metas.get(key));
			}						
		}
		
		
		return subscriber;

	}

	
	public static Map<String, String> getMap(List<Meta> metas) {
		Map<String, String> map = new HashMap<String, String>();
		for(Meta meta: metas) {
			map.put(meta.getKey(), meta.getValue());
		}
		return map;
	}
	
	public static List<Meta> getSefCoreList(Map<String,String> metas) {
		logger.debug("getSefCoreList : called"+metas.size());
		List<Meta> metaList=new ArrayList<Meta>();
		for(String key: metas.keySet()) {
			Meta meta=new Meta();
			meta.setKey(key);
			meta.setValue(metas.get(key));
			metaList.add(meta);
		}
		logger.debug("returning the metaList"+metaList.size());
		return metaList;
	}
	
	public static List<com.ericsson.sef.bes.api.entities.Meta> getSefApiList(Map<String,String> metas) {
		List<com.ericsson.sef.bes.api.entities.Meta> metaList=new ArrayList<com.ericsson.sef.bes.api.entities.Meta>();
		if (metas != null) {
			for(String key: metas.keySet()) {
				com.ericsson.sef.bes.api.entities.Meta meta=new com.ericsson.sef.bes.api.entities.Meta();
				meta.setKey(key);
				meta.setValue(metas.get(key));
				metaList.add(meta);
			}
		}
		return metaList;
	}
	

	public static Map<String, String> getApiMap(
			List<com.ericsson.sef.bes.api.entities.Meta> metas) {
		Map<String, String> map = new HashMap<String, String>();
		if (metas != null) {
			for (com.ericsson.sef.bes.api.entities.Meta meta : metas) {
				map.put(meta.getKey(), meta.getValue());
			}
		}
		return map;
	}


	
	
}
