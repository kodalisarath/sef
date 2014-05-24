package com.ericsson.raso.sef.fulfillment.processors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.raso.sef.bes.prodcat.entities.FulfillmentProfile;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.service.IServiceRegistry;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentServiceResolver;

public class UseCaseProcessor implements Processor {

	@SuppressWarnings("rawtypes")
	@Override
	public void process(Exchange arg0) throws Exception {
		IServiceRegistry serviceRegistry = FulfillmentServiceResolver.getServiceRegistry();
		String operationName = (String) arg0.getIn().getHeader("operationName");
		String msisdn = (String) arg0.getIn().getBody(String.class);
		Product product = (Product) arg0.getIn().getBody(Product.class);
		List<Meta> metas = (List<Meta>) arg0.getIn().getBody(List.class);
		
		Map<String, String> map = covertToMap(msisdn, metas);
		
		Resource resource = serviceRegistry.readResource(product.getResourceName());
		List<FulfillmentProfile> fulfillmentProfiles = resource.getFulfillmentProfiles();
		
		
		if(operationName.equalsIgnoreCase("fulfill")) {
			fulfill(fulfillmentProfiles,product, map);
		} else if (operationName.equalsIgnoreCase("reverse")){
			reverse(fulfillmentProfiles,product, map);
		} else if(operationName.equalsIgnoreCase("query")) {
			query(fulfillmentProfiles,product, map);
		} else if(operationName.equalsIgnoreCase("prepare")) {
			prepare(fulfillmentProfiles,product, map);
		} else if(operationName.equalsIgnoreCase("cancel")) {
			reverse(fulfillmentProfiles,product, map);
		} else return;
	}
	
	
	@SuppressWarnings("rawtypes")
	private void fulfill(List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		for(FulfillmentProfile profile: profiles) { 
			profile.fulfill(product, map);
		}
	}
	
	private void prepare(List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		for(FulfillmentProfile profile: profiles) { 
			profile.prepare(product, map);
		}
	}
	
	private void reverse(List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		for(FulfillmentProfile profile: profiles) { 
			profile.revert(product, map);
		}
	}
	
	private void query(List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		for(FulfillmentProfile profile: profiles) { 
			profile.query(product, map);
		}
	}
	
	private void cancel(List<FulfillmentProfile> profiles, Product product, Map<String, String> map) {

		for(FulfillmentProfile profile: profiles) { 
			profile.revert(product, map);
		}
	}
	
	private Map<String, String> covertToMap(String msisdn, List<Meta> metas) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("msisdn", msisdn);
		Iterator<Meta> iterator = metas.iterator();
		while(iterator.hasNext()) {
			Meta meta = iterator.next();
			map.put(meta.getKey(), meta.getValue());
		}
		return map;
	}
}
