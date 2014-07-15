package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.UpdateServiceClassCommand;
import com.ericsson.raso.sef.client.air.request.ServiceOffering;
import com.ericsson.raso.sef.client.air.request.UpdateServiceClassRequest;
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.SubscriberService;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.fulfillment.profiles.BlockingFulfillment;
import com.ericsson.sef.bes.api.entities.Product;

public class UnsubscribePackageItemProfile extends BlockingFulfillment<Product> {
	private static final long serialVersionUID = 4724882357406161010L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UnsubscribePackageItemProfile.class);

	public UnsubscribePackageItemProfile(String name) {
		super(name);
	}

	// get account details
	// no params
	
	//service class
	private String serviceClassAction;

	//subscriber segment
	List<ServiceOffering> serviceOfferings = new ArrayList<ServiceOffering>();
	

	@Override
	public List<Product> fulfill(Product p, Map<String, String> map) throws FulfillmentException {
		
		LOGGER.info("Entering fullfill method of UnsubscribePackageItemProfile");
		List<Product> products = new ArrayList<Product>();
		if (map == null || map.isEmpty())
			throw new FulfillmentException("ffe", new ResponseCode(1001, "runtime parameters 'metas' missing in request!!"));
		
		String msisdn = map.get("msisdn");
		String defaultServiceClass = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "defaultServiceClass");
		
		
		// fetch meta from db 
	 	List<String> metaKeys = new ArrayList<String>();
	 	metaKeys.add("Package");
	 	List<Meta> metas;
		try {
			SubscriberService subscriberService = SefCoreServiceResolver.getSusbcriberStore();
		 	metas = subscriberService.getMetas(UniqueIdGenerator.generateId(), msisdn, metaKeys);
		} catch (PersistenceError e) {
			LOGGER.debug("Failed Get Account Details. Code: " + e.getStatusCode().getCode() + e.getStatusCode().getMessage());
			throw new FulfillmentException(e.getComponent(), new ResponseCode(e.getStatusCode().getCode(), e.getMessage()));	
		}
	 	
		String packag = this.getMetaValue(metas, "Package");
	 	if (packag == null) {
	 		LOGGER.warn("Unable to fetch welcome pack from user profile!!");
	 		throw new FulfillmentException("ffe", new ResponseCode(11614, "Unable to get existing package from user profile!!"));
	 	}
	 	String pack = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_welcomePackMapping", packag);
	 	
		// service class
		LOGGER.debug("Invoking service class - pack: " + pack + " & default: " + defaultServiceClass);
		UpdateServiceClassRequest scRequest = new UpdateServiceClassRequest();
		scRequest.setSubscriberNumber(msisdn);
		scRequest.setServiceClassAction(this.serviceClassAction);
		scRequest.setServiceClassNew(Integer.valueOf(defaultServiceClass));
		scRequest.setServiceClassCurrent(Integer.parseInt(pack));		
		UpdateServiceClassCommand cmd = new UpdateServiceClassCommand(scRequest);
		try {
			cmd.execute();
		} catch (SmException e) {
			LOGGER.debug("Failed Update Service Class. Code: " + e.getStatusCode().getCode() + e.getStatusCode().getMessage(), e);
			throw new FulfillmentException(e.getComponent(), new ResponseCode(e.getStatusCode().getCode(), e.getMessage()));	
		}
		
		
		p.setMetas(map);
		products.add(p);
		return products;
	}

	private String getMetaValue(List<Meta> metas, String metaName) {
		for (Meta meta: metas) {
			if (meta.getKey() != null && meta.getKey().equalsIgnoreCase(metaName))
				return meta.getValue();
		}
		return null;
	}
	
	@Override
	public List<Product> prepare(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> products = new ArrayList<Product>();
		return products;
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> products = new ArrayList<Product>();
		return products;
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> products = new ArrayList<Product>();
		return products;
	}

	
	public String getServiceClassAction() {
		return serviceClassAction;
	}

	public void setServiceClassAction(String serviceClassAction) {
		this.serviceClassAction = serviceClassAction;
	}

	public List<ServiceOffering> getServiceOfferings() {
		return serviceOfferings;
	}

	public void setServiceOfferings(List<ServiceOffering> serviceOfferings) {
		this.serviceOfferings = serviceOfferings;
	}

	@Override
	public String toString() {
		return "UnsubscribePackageItemProfile [serviceClassAction=" + serviceClassAction + ", serviceOfferings=" + serviceOfferings + "]";
	}
}
