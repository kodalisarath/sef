package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ericsson.raso.sef.client.air.request.ServiceOffering;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.sef.bes.api.entities.Product;

public class UpdateSubscriberSegmentationProfile extends BlockingFulfillment<Product>{

	private static final long serialVersionUID = -4012237785041809835L;
	
	List<ServiceOffering> serviceOfferings = new ArrayList<ServiceOffering>();
	
	public UpdateSubscriberSegmentationProfile(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Product> fulfill(Product e, Map<String, String> map)
			throws FulfillmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map)
			throws FulfillmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map)
			throws FulfillmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map)
			throws FulfillmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
