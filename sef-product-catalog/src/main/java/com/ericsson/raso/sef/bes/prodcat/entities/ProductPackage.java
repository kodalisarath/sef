package com.ericsson.raso.sef.bes.prodcat.entities;

import java.util.Set;
import java.util.TreeSet;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;


public final class ProductPackage extends Product {
	private static final long serialVersionUID = -3336740926345972063L;
	
	private Set<AtomicProduct> products = null;
	
	public ProductPackage(String name) {
		super(name);
	}

	public void addProduct(AtomicProduct product) throws CatalogException {
		if (product == null)
			throw new CatalogException("Given Product was null");
		
		if (this.products == null)
			this.products = new TreeSet<AtomicProduct>();
		
		this.products.add(product);
	}
	
	public void removeProduct(AtomicProduct product) throws CatalogException {
		if (product == null)
			throw new CatalogException("Given Product was null");
		
		if (this.products != null)
			this.products.add(product);
	}

	public Set<AtomicProduct> getProducts() {
		return products;
	}

	public void setProducts(Set<AtomicProduct> products) {
		this.products = products;
	}
	
	

}
