package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.GetAccountDetailsCommand;
import com.ericsson.raso.sef.client.air.command.UpdateServiceClassCommand;
import com.ericsson.raso.sef.client.air.command.UpdateSubscriberSegmentationCmd;
import com.ericsson.raso.sef.client.air.request.GetAccountDetailsRequest;
import com.ericsson.raso.sef.client.air.request.ServiceOffering;
import com.ericsson.raso.sef.client.air.request.UpdateServiceClassRequest;
import com.ericsson.raso.sef.client.air.request.UpdateSubscriberSegmentRequest;
import com.ericsson.raso.sef.client.air.response.GetAccountDetailsResponse;
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
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
		
		
		// get account details
//		LOGGER.info("Preparing the transaction with get account details");
//		GetAccountDetailsRequest accountDetailsRequest = new GetAccountDetailsRequest();
//		accountDetailsRequest.setSubscriberNumber(msisdn);
//		accountDetailsRequest.setSubscriberNumberNAI(1);
//		
//		GetAccountDetailsCommand accountDetailsCommand = new GetAccountDetailsCommand(accountDetailsRequest);
//		GetAccountDetailsResponse accountDetailsResponse = null;
//		try {
//			accountDetailsResponse = accountDetailsCommand.execute();
//		} catch (SmException e) {
//			LOGGER.debug("Failed Get Account Details. Code: " + e.getStatusCode().getCode() + e.getStatusCode().getMessage());
//			throw new FulfillmentException(e.getComponent(), new ResponseCode(e.getStatusCode().getCode(), e.getMessage()));	
//		}
		
		// extract needed info
//	 	List<com.ericsson.raso.sef.client.air.response.ServiceOffering> existingServiceOfferings = accountDetailsResponse.getServiceOfferings();
	 	
		
	 	List<String> metaKeys = new ArrayList<String>();
	 	metaKeys.add("packaze");
	 	List<Meta> metas;
		try {
			SubscriberService subscriberService = SefCoreServiceResolver.getSusbcriberStore();
		 	metas = subscriberService.getMetas("dynamic-task", msisdn, metaKeys);
		} catch (PersistenceError e) {
			LOGGER.debug("Failed Get Account Details. Code: " + e.getStatusCode().getCode() + e.getStatusCode().getMessage());
			throw new FulfillmentException(e.getComponent(), new ResponseCode(e.getStatusCode().getCode(), e.getMessage()));	
		}
	 	String packag = metas.get(0).getValue();
	 	String pack = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_welcomePackMapping", packag);
	 	
		// service class
		LOGGER.debug("Invoking service class");
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
		
		// subscriber segmentation
		LOGGER.info("Going ahead wtih subscriber segmentation...");
		UpdateSubscriberSegmentRequest request = new UpdateSubscriberSegmentRequest();
		request.setSubscriberNumber(msisdn);
		request.setServiceOfferings(serviceOfferings);
		try {
			new UpdateSubscriberSegmentationCmd(request).execute();
		} catch (SmException e) {
			LOGGER.error("Failed SubscriberSegmentation. Code: " + e.getStatusCode().getCode() + e.getStatusCode().getMessage(), e);
			throw new FulfillmentException(e.getComponent(), new ResponseCode(e.getStatusCode().getCode(), e.getStatusCode().getMessage()));
		} 
		
		return products;
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
		// TODO Auto-generated method stub
		return null;
	}
}
