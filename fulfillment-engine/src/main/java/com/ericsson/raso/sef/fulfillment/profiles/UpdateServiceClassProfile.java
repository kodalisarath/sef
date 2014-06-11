package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.UpdateServiceClassCommand;
import com.ericsson.raso.sef.client.air.request.UpdateServiceClassRequest;
import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.sef.bes.api.entities.Product;

public class UpdateServiceClassProfile extends BlockingFulfillment<Product> {
	private static final long serialVersionUID = -3209549693082338094L;

	public UpdateServiceClassProfile(String name) {
		super(name);
	}

	private static final Logger logger = LoggerFactory.getLogger(UpdateServiceClassProfile.class);

	private String serviceClassAction;

	public String getServiceClassAction() {
		return serviceClassAction;
	}

	public void setServiceClassAction(String serviceClassAction) {
		this.serviceClassAction = serviceClassAction;
	}

	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		
		logger.info("Entering fullfill method of updateServiceClassProfile");
		if (map == null || map.isEmpty())
			throw new FulfillmentException("ffe", new ResponseCode(1001, "runtime parameters 'metas' missing in request!!"));
		
		String msisdn = map.get("msisdn");
		String packag = map.get("package");
		String pack = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_welcomePackMapping", packag);
		
		List<Product> products = new ArrayList<Product>();
		logger.debug("Invoking CS to update service class");
		UpdateServiceClassRequest request = new UpdateServiceClassRequest();
		request.setSubscriberNumber(msisdn);
		request.setServiceClassAction(this.serviceClassAction);
		request.setServiceClassNew(Integer.parseInt(pack));
		String defaultServiceClass = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "defaultServiceClass");
		request.setServiceClassCurrent(Integer.valueOf(defaultServiceClass));		
		Command<?> cmd = new UpdateServiceClassCommand(request);
		try {
			cmd.execute();
		} catch (SmException e1) {
			logger.debug("Exception in execution of ucip request. Code: " + e1.getStatusCode().getCode() + e1.getStatusCode().getMessage());
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getMessage()));	
		}
		return products;
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map) {
		List<Product> products = new ArrayList<Product>();
		return products;
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) {
		List<Product> products = new ArrayList<Product>();
		return products;
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) {
		List<Product> products = new ArrayList<Product>();
		return products;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((serviceClassAction == null) ? 0 : serviceClassAction
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UpdateServiceClassProfile other = (UpdateServiceClassProfile) obj;
		if (serviceClassAction == null) {
			if (other.serviceClassAction != null)
				return false;
		} else if (!serviceClassAction.equals(other.serviceClassAction))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UpdateServiceClassProfile [serviceClassAction="
				+ serviceClassAction + "]";
	}
	
}
