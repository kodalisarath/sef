package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.List;
import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.entities.BlockingFulfillment;
import com.ericsson.sef.bes.api.entities.Product;

public class AddPeriodicAccountManagementDataProfile extends BlockingFulfillment<Product>{

	protected AddPeriodicAccountManagementDataProfile(String name) {
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
	public List<Product> fulfill(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
	
}
