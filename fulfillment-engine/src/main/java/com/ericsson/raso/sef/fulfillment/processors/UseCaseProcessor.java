package com.ericsson.raso.sef.fulfillment.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Holder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.service.IServiceRegistry;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentServiceResolver;
import com.ericsson.raso.sef.fulfillment.profiles.FulfillmentProfile;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.entities.TransactionStatus;

public class UseCaseProcessor implements Processor {

	Logger logger = LoggerFactory.getLogger(UseCaseProcessor.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void process(Exchange arg0) throws Exception {		

		logger.debug("E/// Request: " + arg0.getIn().getBody().toString());
		Object[] objectArray= arg0.getIn().getBody(Object[].class);
		String operationName = (String) arg0.getIn().getHeader("operationName");
	 	String msisdn =(String)objectArray[1];
	 	Product product=(Product) objectArray[2]; 
	 	List<Meta> metas = (List<Meta>) objectArray[3];
		Map<String, String> map = covertToMap(msisdn, metas);
		//String correlationId = (String) arg0.getIn().getHeader("CORRELATIONID");
		String correlationId = (String) objectArray[0];
		
		logger.debug("E/// msisdn: " + msisdn);
		logger.debug("E/// operationName: " + operationName);
		
		IServiceRegistry serviceRegistry = FulfillmentServiceResolver.getServiceRegistry();
		Resource resource = serviceRegistry.readResource(product.getResourceName());
		logger.debug("E/// productResourceName " + product.getResourceName());
		List<String> fulfillmentProfileIds = resource.getFulfillmentProfiles();
		List<FulfillmentProfile> fulfillmentProfiles = getProfiles(fulfillmentProfileIds);
		
		switch(operationName){
		case "fulfill":
			logger.debug("Use case identified as fulfill!!");
			fulfill(correlationId, msisdn, fulfillmentProfiles,product, map);
		               break;
		case "reverse":reverse(fulfillmentProfiles,product, map);
		               break;
		case "query":query(fulfillmentProfiles,product, map);
		             break;
		case "prepare":
			logger.debug("Use case identified as prepare!!");
			prepare(correlationId, msisdn, fulfillmentProfiles,product, map);
		                break;
		case "cancel":cancel(fulfillmentProfiles,product, map);
		               break;
		default:  break;
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	private List<FulfillmentProfile> getProfiles(
			List<String> fulfillmentProfileIds) {
		logger.debug("Retrieving fulfillment profile for ProfileID: " + fulfillmentProfileIds);
		List<FulfillmentProfile> profiles = new ArrayList<FulfillmentProfile>();
		
		try {
			for(String profile: fulfillmentProfileIds) {
			
				FulfillmentProfile fulfillmentProfile =  FulfillmentServiceResolver.getProfileRegistry().readProfile(profile);
				profiles.add(fulfillmentProfile);
			}
		} catch (CatalogException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("Returning profiles, Total " + profiles.size());
		return profiles;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void fulfill(String correlationId, String msisdn, List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		TransactionStatus status = new TransactionStatus();
		
		List<Product> products = new ArrayList<Product>();
		logger.debug("E/// How many profiles we have: " + profiles.size() + " and correlationID is " + correlationId);
		try {
		for(FulfillmentProfile profile: profiles) {
			logger.debug("E/// Product name: " + product.getName() + " " + map.size());
			logger.debug("E/// Profile name: " + profile.getName() + " " + map.size());
			//products.addAll((profile.fulfill(product, map)));
			List<Product>prods = profile.fulfill(product, map);
			for(Product prod:  prods) {
				if(prod != null) {
					products.add(prod);
				}
			}
		}
		} catch (FulfillmentException e) {
			logger.error("Exception while fulfilment " +  e.getMessage() + "Exception: " + e);
			status.setComponent(e.getComponent());
			status.setCode(e.getStatusCode().getCode());
			status.setDescription(e.getStatusCode().getMessage());
		}
		
		List<Meta> metas = new ArrayList<Meta>();
		
		logger.info("Sending fulfillment Response now");
		
		for(Product prod: products) {
			if(prod!= null) {
				if(prod.getName() != null)
				logger.debug("Product:" +  prod.getName());
				if(prod.getResourceName()!= null)
					logger.debug("Resource:" +  prod.getResourceName());
				logger.debug("Quota consumed:" +  prod.getQuotaConsumed());
				logger.debug("Quote defined: " +  prod.getQuotaDefined());
				logger.debug("Validity: " +  prod.getValidity());
			}
			
		}
		
		sendFulfillResponse(correlationId, msisdn, status, products, metas);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void prepare(String correlationId, String msisdn, List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {
		
		TransactionStatus status = new TransactionStatus();
		
		List<Product> products = new ArrayList<Product>();
		try {
		for(FulfillmentProfile profile: profiles) {
			logger.debug("E/// Product name: " + product.getName() + " " + map.size());
			logger.debug("E/// Profile name: " + profile.getName() + " " + map.size());
			//products.addAll((profile.fulfill(product, map)));
			List<Product>prods = profile.prepare(product, map);
			for(Product prod:  prods) {
				if(prod != null) {
					products.add(prod);
				}
			}
		}
		} catch (FulfillmentException e) {
			logger.error("Exception while fulfilment " +  e.getMessage() + "Exception: " + e);
			status.setComponent(e.getComponent());
			status.setCode(e.getStatusCode().getCode());
			status.setDescription(e.getStatusCode().getMessage());
		}

		List<Meta> metas = new ArrayList<Meta>();
		
		//TODO: Post Response
		sendPrepareResponse(correlationId, msisdn, new TransactionStatus(), products, metas);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void reverse(List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		try{
		for(FulfillmentProfile profile: profiles) { 
			profile.revert(product, map);
		}
		} catch(FulfillmentException e) {
			
		}
		
		//TODO: Post response
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void query(List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		try {
		for(FulfillmentProfile profile: profiles) { 
			profile.query(product, map);
		}
		} catch(FulfillmentException e) {
			
		}
		// TODO: Post response
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void cancel(List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		try {
			
		for(FulfillmentProfile profile: profiles) { 
			profile.revert(product, map);
		}
		} catch(FulfillmentException e) {
			
		}
		//TODO: Post Response
	}
	
	private Map<String, String> covertToMap(String msisdn, List<Meta> metas) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("msisdn", msisdn.toString());
		if(metas != null) {
		for(Meta meta: metas){
			map.put(meta.getKey(), meta.getValue().toString());
		}
		}
		return map;
	}
	
	private void sendFulfillResponse(String correlationId, String msidn, TransactionStatus fault, List<Product> products, List<Meta> meta) {
		FulfillmentServiceResolver.getFulfillmentResponseClient().fulfill(correlationId, fault, products, meta);
	}
	
	private void sendPrepareResponse(String correlationId, String msisdn, TransactionStatus fault, List<Product> products, List<Meta> meta) {
		FulfillmentServiceResolver.getFulfillmentResponseClient().prepare(correlationId, fault, products, meta);
	}
}
