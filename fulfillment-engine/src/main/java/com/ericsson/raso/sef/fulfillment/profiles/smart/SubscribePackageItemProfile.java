package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.AddPeriodicAccountManagementDataCmd;
import com.ericsson.raso.sef.client.air.command.UpdateServiceClassCommand;
import com.ericsson.raso.sef.client.air.request.AddPeriodicAccountManagementDataReq;
import com.ericsson.raso.sef.client.air.request.PamInformation;
import com.ericsson.raso.sef.client.air.request.UpdateServiceClassRequest;
import com.ericsson.raso.sef.client.air.response.AddPeriodicAccountManagementDataRes;
import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.fulfillment.profiles.BlockingFulfillment;
import com.ericsson.raso.sef.fulfillment.profiles.PamInformationList;
import com.ericsson.sef.bes.api.entities.Product;

public class SubscribePackageItemProfile extends BlockingFulfillment<Product>{
	private static final long serialVersionUID = 8598152491371180904L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SubscribePackageItemProfile.class);
	
	// service class
	private String serviceClassAction;

	// add pam data
	private List<PamInformation> pamInformationList;

		
	public SubscribePackageItemProfile(String name) {
		super(name);
	}

	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();
		
		LOGGER.debug("Starting to execute SubscribePackage...");
		if (map == null || map.isEmpty())
			throw new FulfillmentException("ffe", new ResponseCode(1001, "runtime parameters 'metas' missing in request!!"));
		
		String msisdn = map.get("msisdn");
		String packag = map.get("Package");
		String pack = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_welcomePackMapping", packag);
	
		
		// Service Class...
		LOGGER.debug("Invoking CS to update service class");
		
		UpdateServiceClassRequest request = new UpdateServiceClassRequest();
		request.setSubscriberNumber(msisdn);
		LOGGER.debug("msisdn is " + msisdn);
		request.setServiceClassAction(this.serviceClassAction);
		LOGGER.debug("service class action is " + this.serviceClassAction);
		request.setServiceClassNew(Integer.parseInt(pack));
		LOGGER.debug("Pack is " + Integer.parseInt(pack));
		String defaultServiceClass = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "defaultServiceClass");
		LOGGER.debug("Default SC is " + defaultServiceClass);
		request.setServiceClassCurrent(Integer.valueOf(defaultServiceClass));		
		LOGGER.debug("Current SC is " + Integer.valueOf(defaultServiceClass));
		UpdateServiceClassCommand cmd = new UpdateServiceClassCommand(request);
		try {
			cmd.execute();
			LOGGER.debug("Service Class updated...");
		} catch (SmException e1) {
			LOGGER.debug("Exception Service Class. Code: " + e1.getStatusCode().getCode() + e1.getStatusCode().getMessage());
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getMessage()));	
		}
		
		// Add PAM Data...
		LOGGER.debug("PAM Data...");
		AddPeriodicAccountManagementDataReq pamRequest = new AddPeriodicAccountManagementDataReq();
		pamRequest.setPamInformationList(this.pamInformationList);
		pamRequest.setSubscriberNumber(msisdn);
		AddPeriodicAccountManagementDataCmd pamCommand = new AddPeriodicAccountManagementDataCmd(pamRequest);
		try {
			AddPeriodicAccountManagementDataRes pamResponse = pamCommand.execute();
			LOGGER.debug("Service Class updated...");
		} catch (SmException e1) {
			int airFault = e1.getStatusCode().getCode(); 
			if ( airFault == 190) {
				LOGGER.debug("Can ignore fault: " + airFault + ". childish...");
			} else {
				LOGGER.debug("Exception Service Class. Code: " + e1.getStatusCode().getCode() + e1.getStatusCode().getMessage());
				throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getMessage()));
			}
		}
		
		
		e.setMetas(map);
		returned.add(e);
		
		return returned;
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();
		return returned;
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();
		return returned;
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();
		return returned;
	}
	
	public String getServiceClassAction() {
		return serviceClassAction;
	}

	public void setServiceClassAction(String serviceClassAction) {
		this.serviceClassAction = serviceClassAction;
	}
	
	


	
	public List<PamInformation> getPamInformationList() {
		return pamInformationList;
	}

	public void setPamInformationList(List<PamInformation> pamInformationList) {
		this.pamInformationList = pamInformationList;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
