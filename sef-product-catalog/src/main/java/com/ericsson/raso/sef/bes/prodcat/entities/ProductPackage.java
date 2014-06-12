package com.ericsson.raso.sef.bes.prodcat.entities;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;


public final class ProductPackage extends Product {
	private static final long serialVersionUID = -3336740926345972063L;
	
	private Set<Product> products = null;
	
	public ProductPackage(String name) {
		super(name);
	}

	public void addProduct(Product product) throws CatalogException {
		if (product == null)
			throw new CatalogException("Given Product was null");
		
		if (this.products == null)
			this.products = new TreeSet<Product>();
		
		this.products.add(product);
	}
	
	public void removeProduct(Product product) throws CatalogException {
		if (product == null)
			throw new CatalogException("Given Product was null");
		
		if (this.products != null)
			this.products.add(product);
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Set<AtomicProduct> getAtomicProducts() {
		Set<AtomicProduct> atomicProducts = new TreeSet<AtomicProduct>();
		atomicProducts = this.fetchAtomicProducts(atomicProducts);
		return atomicProducts;
	}
	
	private Set<AtomicProduct> fetchAtomicProducts(Set<AtomicProduct> atomicProducts) {
		if (this.products == null)
			return atomicProducts;
		
		Iterator<Product> products = this.products.iterator();
		while (products.hasNext()) {
			Product product = products.next();
			if (product instanceof AtomicProduct)
				atomicProducts.add((AtomicProduct)product);
			
			if (product instanceof ProductPackage)
				return fetchAtomicProducts(atomicProducts);
		}
		return atomicProducts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((products == null) ? 0 : products.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!super.equals(obj))
			return false;
		
		if (!(obj instanceof ProductPackage))
			return false;
		
		ProductPackage other = (ProductPackage) obj;
		if (products == null) {
			if (other.products != null)
				return false;
		} else if (!products.equals(other.products))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "ProductPackage [products=" + products + ", getName()=" + getName() + ", getOwner()=" + getOwner() + ", getCriteria()="
				+ getCriteria() + "]";
	}

	
	

}
