package com.ericsson.raso.sef.fulfillment.processors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Holder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.entities.FulfillmentProfile;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.service.IServiceRegistry;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentServiceResolver;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Product;

public class UseCaseProcessor implements Processor {
	
	Logger logger = LoggerFactory.getLogger(UseCaseProcessor.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void process(Exchange arg0) throws Exception {
		IServiceRegistry serviceRegistry = FulfillmentServiceResolver.getServiceRegistry();
		Object[] objectArray=(Object[]) arg0.getIn().getBody(Object.class);
		String operationName = (String)objectArray[0];
	 	Holder<String> msisdn =(Holder<String>)objectArray[1];
	 	Holder<Product> product=(Holder<Product>) objectArray[2]; 
	 	Holder<List> metas =(Holder<List>) objectArray[3];
		Map<String, String> map = covertToMap(msisdn.value, metas.value);
		
		Resource resource = serviceRegistry.readResource(product.value.getResourceName());
		List<FulfillmentProfile> fulfillmentProfiles = resource.getFulfillmentProfiles();
		
		
		switch(operationName){
		case "fulfill":fulfill(fulfillmentProfiles,product.value, map);
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
	
	private Map<String, String> covertToMap(String msisdn, List metas) {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("msisdn", msisdn.toString());
		Iterator<Meta> iterator = metas.iterator();
		while(iterator.hasNext()) {
			Meta meta = iterator.next();
			map.put(meta.getKey(), meta.getValue());
		}
		return map;
	}
}
