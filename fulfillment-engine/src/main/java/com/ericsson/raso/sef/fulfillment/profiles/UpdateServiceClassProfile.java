package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.List;
import java.util.Map;

import com.ericsson.sef.bes.api.entities.Product;

public class UpdateServiceClassProfile extends BlockingFulfillment<Product> {

	protected UpdateServiceClassProfile(String name) {
		super(name);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String serviceClassAction;

	public String getServiceClassAction() {
		return serviceClassAction;
	}

	public void setServiceClassAction(String serviceClassAction) {
		this.serviceClassAction = serviceClassAction;
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
