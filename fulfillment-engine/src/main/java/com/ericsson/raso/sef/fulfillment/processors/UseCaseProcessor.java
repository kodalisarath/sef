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
		
		
		Object[] objectArray=(Object[]) arg0.getIn().getBody(Object.class);
		String operationName = (String)objectArray[0];
	 	Holder<String> msisdn =(Holder<String>)objectArray[1];
	 	Holder<Product> product=(Holder<Product>) objectArray[2]; 
	 	Holder<List<Meta>> metas =(Holder<List<Meta>>) objectArray[3];
		Map<String, String> map = covertToMap(msisdn.value, metas.value);
		String correlationId = (String) arg0.getIn().getHeader("CORRELATIONID");
		
		IServiceRegistry serviceRegistry = FulfillmentServiceResolver.getServiceRegistry();
		Resource resource = serviceRegistry.readResource(product.value.getResourceName());
		List<String> fulfillmentProfileIds = resource.getFulfillmentProfiles();
		List<FulfillmentProfile> fulfillmentProfiles = getProfiles(fulfillmentProfileIds);
		
		switch(operationName){
		case "fulfill":fulfill(correlationId, msisdn.value, fulfillmentProfiles,product.value, map);
		               break;
		case "reverse":reverse(fulfillmentProfiles,product.value, map);
		               break;
		case "query":query(fulfillmentProfiles,product.value, map);
		             break;
		case "prepare":prepare(fulfillmentProfiles,product.value, map);
		                break;
		case "cancel":cancel(fulfillmentProfiles,product.value, map);
		               break;
		default:  break;
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	private List<FulfillmentProfile> getProfiles(
			List<String> fulfillmentProfileIds) {
		
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
		return profiles;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void fulfill(String correlationId, String msisdn, List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		List<Product> products = new ArrayList<Product>();
		for(FulfillmentProfile profile: profiles) { 
			products.addAll((profile.fulfill(product, map)));
		}
		
		//TODO: address this with graceful exception handling
		sendFulfillResponse(correlationId, msisdn, null, products, null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void prepare(List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		for(FulfillmentProfile profile: profiles) { 
			profile.prepare(product, map);
		}
		//TODO: Post Response
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void reverse(List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		for(FulfillmentProfile profile: profiles) { 
			profile.revert(product, map);
		}
		
		//TODO: Post response
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void query(List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		for(FulfillmentProfile profile: profiles) { 
			profile.query(product, map);
		}
		// TODO: Post response
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void cancel(List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		for(FulfillmentProfile profile: profiles) { 
			profile.revert(product, map);
		}
		//TODO: Post Response
	}
	
	private Map<String, String> covertToMap(String msisdn, List<Meta> metas) {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("msisdn", msisdn.toString());
		Iterator<Meta> iterator = metas.iterator();
		while(iterator.hasNext()) {
			Meta meta = iterator.next();
			map.put(meta.getKey(), meta.getValue());
		}
		return map;
	}
	
	private void sendFulfillResponse(String correlationId, String msidn, TransactionStatus fault, List<Product> products, List<Meta> meta) {
		FulfillmentServiceResolver.getFulfillmentResponseClient().fulfill(correlationId, fault, products, meta);
	}
}
