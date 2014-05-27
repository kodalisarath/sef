package com.ericsson.raso.sef.bes.engine.transaction;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import com.ericsson.sef.bes.api.entities.Offer;
import com.ericsson.sef.bes.api.entities.Product;
public abstract class TransactionServiceHelper {
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
	
	private static Set<Product> translateProducts(List<AtomicProduct> allAtomicProducts) {
		Set<Product> products = new TreeSet<Product>();
		
		for (AtomicProduct source: allAtomicProducts) 
			products.add(TransactionServiceHelper.getApiEntity(source));
		
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
			AbstractQuotaCharacteristic quotalimited=new LimitedQuota();
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
	
	private static Set<AtomicProduct> translatetoAtomicProducts(List<Product> products) {
		Set<AtomicProduct> AtomicProducts = new TreeSet<AtomicProduct>();
		for (Product source: products) 
			
			AtomicProducts.add(TransactionServiceHelper.getApiEntity(source));
		
		return AtomicProducts;
	}
	

}