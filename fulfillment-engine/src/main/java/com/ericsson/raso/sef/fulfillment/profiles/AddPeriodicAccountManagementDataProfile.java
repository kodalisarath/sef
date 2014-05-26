package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.entities.BlockingFulfillment;
import com.ericsson.sef.bes.api.entities.Product;

public class AddPeriodicAccountManagementDataProfile extends BlockingFulfillment<Product>{

	public AddPeriodicAccountManagementDataProfile(String name) {
		super(name);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PamInformationList pamInformationList;
	
	public PamInformationList getPamInformationList() {
		return pamInformationList;
	}

	public void setPamInformationList(PamInformationList pamInformationList) {
		this.pamInformationList = pamInformationList;
	}

	@Override
	public void fulfill(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void query(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revert(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	public void setPamIndicator(Integer pamIndicator) {
		// TODO Auto-generated method stub
		
	}

	public void setPamServiceId(Integer pamServiceId) {
		// TODO Auto-generated method stub
		
	}

	
	
}
