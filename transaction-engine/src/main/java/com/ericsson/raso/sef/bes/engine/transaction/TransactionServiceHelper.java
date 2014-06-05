package com.ericsson.raso.sef.bes.engine.transaction;
import java.text.DateFormat;
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
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.sef.bes.api.entities.Offer;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.Subscriber;


public abstract class TransactionServiceHelper {
	
	
	public static Subscriber getApiEntity(com.ericsson.raso.sef.core.db.model.Subscriber subscriber) {
		Subscriber returned = new Subscriber();
		
		
		returned.setBillCycleDay(subscriber.getBillCycleDay());
		returned.setContractId(subscriber.getContractId());
		returned.setContractState(subscriber.getContractState().getName());
		returned.setCustomerId(subscriber.getCustomerId());
		returned.setCustomerSegment(subscriber.getCustomerSegment());
		returned.setEmail(subscriber.getEmail());
		returned.setGender(subscriber.getGender());
		returned.setImeiSv(subscriber.getImeiSv());
		returned.setImsi(subscriber.getImsi());
		returned.setMetas(getMetas(subscriber.getMetas())); //TODO: this will need refactoring API entity, since SOAP does not support Map<K,V>
		returned.setMsisdn(subscriber.getMsisdn());
		returned.setPaymentParent(subscriber.getPaymentParent());
		returned.setPaymentResponsible(subscriber.getPaymentResponsible());
		returned.setPaymentType(subscriber.getPaymentType());
		returned.setPin(subscriber.getPin());
		returned.setPrefferedLanguage(subscriber.getPrefferedLanguage());
		returned.setRatePlan(subscriber.getRatePlan());
		returned.setRegistrationDate(subscriber.getRegistrationDate().getTime());
		returned.setUserId(subscriber.getUserId());
		
		return returned;
		
	}
	
	public static com.ericsson.raso.sef.core.db.model.Subscriber getPersistableEntity(Subscriber subscriber) {
		com.ericsson.raso.sef.core.db.model.Subscriber returned = new com.ericsson.raso.sef.core.db.model.Subscriber();
		
		returned.setAccountId(subscriber.getCustomerId());
		returned.setCustomerId(subscriber.getCustomerId());
		returned.setContractId(subscriber.getContractId());
		returned.setContractState(ContractState.valueOf(subscriber.getContractState()));
		returned.setCustomerSegment(subscriber.getCustomerSegment());
		returned.setEmail(subscriber.getEmail());
		returned.setImeiSv(subscriber.getImeiSv());
		returned.setImsi(subscriber.getImsi());
		returned.setMetas(getMetas(subscriber.getMetas()));
		returned.setMsisdn(subscriber.getMsisdn());
		returned.setPin(subscriber.getPin());
		returned.setPaymentType(subscriber.getPaymentType());
		returned.setPrefferedLanguage(subscriber.getPrefferedLanguage());
		returned.setRatePlan(subscriber.getRatePlan());
		returned.setRegistrationDate(new Date(subscriber.getRegistrationDate()));
		returned.setUserId(subscriber.getUserId());
		
		
		return returned;
	}
	
	
	private static Collection<Meta> getMetas(Map<String,String> metaMap) {
		Collection<Meta> metas = new ArrayList<Meta>();
		if (metaMap != null) {
			for (String key : metaMap.keySet()) {
				Meta meta = new Meta();
				meta.setKey(key);
				meta.setValue(metaMap.get(key));
			}
		}
		return metas;
	}

	private static Map<String, String> getMetas(Collection<Meta> subscriberMetas) {
		Map<String,String> map = new HashMap<String, String>();
		for(Meta meta: subscriberMetas) {
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
		
		for (String key: map.keySet()) {
			returned.put(key, map.get(key));
		}
		
		return returned;
	}



	public static com.ericsson.sef.bes.api.entities.Subscriber enrichSubscriber(com.ericsson.sef.bes.api.entities.Subscriber subscriber,
			List<Product> products) {

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
				// service offerings
				if (key.startsWith(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ACTIVE_FLAG.name()))
					subscriber.addMeta(key, metas.get(key));
				
				// account details offer info...
				if (key.startsWith(Constants.READ_SUBSCRIBER_OFFER_INFO_OFFER_ID.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_SUBSCRIBER_OFFER_INFO_START_DATE.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_SUBSCRIBER_OFFER_INFO_START_DATE_TIME.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME.name()))
					subscriber.addMeta(key, metas.get(key));
				
				// dedicated accounts...
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_ID.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_VALUE_1.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_VALUE_2.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_EXPIRY_DATE.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_START_DATE.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_PAM_SERVICE_ID.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_OFFER_ID.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_PRODUCT_ID.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_REAL_MONEY_FLAG.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_DATE.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_VALUE_1.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_EXPIRY_VALUE_2.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_DATE.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_VALUE_1.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_CLOSEST_ACCESSIBLE_VALUE_2.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_1.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_VALUE_2.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_START_DATE.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_SUB_DA_EXPIRY_DATE.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_ACTIVE_VALUE_1.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_ACTIVE_VALUE_2.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_UNIT_TYPE.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_COMPOSITE_DA_FLAG.name()))
					subscriber.addMeta(key, metas.get(key));
				
				// balance & date offer info...
				if (key.startsWith(Constants.READ_BALANCES_OFFER_INFO_OFFER_ID.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_OFFER_INFO_START_DATE.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_OFFER_INFO_START_DATE_TIME.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_OFFER_INFO_EXPIRY_DATE.name()))
					subscriber.addMeta(key, metas.get(key));
				
				if (key.startsWith(Constants.READ_BALANCES_OFFER_INFO_EXPIRY_DATE_TIME.name()))
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

	public static Map<String, String> getApiMap(List<com.ericsson.sef.bes.api.entities.Meta> metas) {
		Map<String, String> map = new HashMap<String, String>();
		for(com.ericsson.sef.bes.api.entities.Meta meta: metas) {
			map.put(meta.getKey(), meta.getValue());
		}
		return map;
	}


	
	
}
