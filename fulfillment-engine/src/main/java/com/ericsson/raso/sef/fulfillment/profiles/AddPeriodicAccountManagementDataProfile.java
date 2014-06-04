package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.List;
import java.util.Map;

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

	public void setPamIndicator(Integer pamIndicator) {
		// TODO Auto-generated method stub
		
	}

	public void setPamServiceId(Integer pamServiceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((pamInformationList == null) ? 0 : pamInformationList
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
		AddPeriodicAccountManagementDataProfile other = (AddPeriodicAccountManagementDataProfile) obj;
		if (pamInformationList == null) {
			if (other.pamInformationList != null)
				return false;
		} else if (!pamInformationList.equals(other.pamInformationList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AddPeriodicAccountManagementDataProfile [pamInformationList="
				+ pamInformationList + "]";
	}

	
}
