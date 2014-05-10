package com.ericsson.raso.sef.bes.prodcat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.bes.prodcat.entities.Owner;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.service.IServiceRegistry;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.SecureSerializationHelper;


//TODO: please udate the code implemenation to also synchronize with Hazelcast...

public final class ServiceRegistry implements IServiceRegistry {
	
	private Map<String, Resource> resources = new TreeMap<String, Resource>();;

	private String serviceRegistryLocation = null;
	private SecureSerializationHelper ssh = null;

	public ServiceRegistry() {
		// TODO: fetch the store location from config, once the config services
		// is ready....

		ssh = new SecureSerializationHelper();

		if (ssh.fileExists(this.serviceRegistryLocation)) {
			try {
				this.resources = (Map<String, Resource>) ssh.fetchFromFile(this.serviceRegistryLocation);
			} catch (FrameworkException e) {
				// TODO: LOgger on this error...
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IServiceRegistry#createResource(com.ericsson.raso.sef.bes.prodcat.entities.Resource)
	 */
	@Override
	public boolean createResource(Resource resource) throws CatalogException {
		if (resource == null)
			throw new CatalogException("Given Resource was null!!");

		if (!this.resources.containsValue(resource))
			this.resources.put(resource.getName(), resource);
		else 
			throw new CatalogException("Given Resource (" + resource + ") already exists!!");

		try {
			this.ssh.persistToFile(this.serviceRegistryLocation, (Serializable) this.resources);
			return true;
		} catch (FrameworkException e) {
			throw new CatalogException("Failed to persist changes!!", e);
		}

	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IServiceRegistry#updateResource(com.ericsson.raso.sef.bes.prodcat.entities.Resource)
	 */
	@Override
	public boolean updateResource(Resource resource) throws CatalogException {
		if (resource == null)
			throw new CatalogException("Given Resource was null!!");

		if (this.resources.isEmpty())
			return false;

		if (this.resources.containsValue(resource))
			this.resources.put(resource.getName(), resource);
		else
			return false;

		try {
			this.ssh.persistToFile(this.serviceRegistryLocation, (Serializable) this.resources);
			return true;
		} catch (FrameworkException e) {
			throw new CatalogException("Failed to persist changes!!", e);
		}

	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IServiceRegistry#deleteResource(com.ericsson.raso.sef.bes.prodcat.entities.Resource)
	 */
	@Override
	public boolean deleteResource(Resource resource) throws CatalogException {
		if (resource == null)
			throw new CatalogException("Given Resource was null!!");

		if (this.resources.isEmpty())
			return false;

		
		if (this.resources.containsValue(resource)) {
			if (resource.getDependantOnMe() != null)
				throw new CatalogException("Resource (" + resource + ") has dependants: " + resource.getDependantOnMe());
			
			if (resource.getDependantOnOthers() != null) 
				for (Resource other: resource.getDependantOnOthers()) 
					other.removeDependantOnMe(resource);			
			
			this.resources.remove(resource.getName(), resource);
		} else
			return false;

		try {
			this.ssh.persistToFile(this.serviceRegistryLocation, (Serializable) this.resources);
			return true;
		} catch (FrameworkException e) {
			throw new CatalogException("Failed to persist changes!!", e);
		}

	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IServiceRegistry#readResource(java.lang.String)
	 */
	@Override
	public Resource readResource(String resource) throws CatalogException {
		if (resource == null)
			throw new CatalogException("Given Resource was null!!");

		if (this.resources.isEmpty())
			return null;

		return this.resources.get(resource);
	}

	@Override
	public List<Resource> getAllResources() throws CatalogException {
		Collection<Resource> allResources = this.resources.values();
		if (allResources.isEmpty())
			return new ArrayList<Resource>();
		return Arrays.asList((Resource[]) allResources.toArray());
	}

	@Override
	public List<Resource> getAllResourcesFor(Owner owner) throws CatalogException {
		List<Resource> returnValues = new ArrayList<Resource>();
		
		Collection<Resource> allResources = this.resources.values();
		if (allResources.isEmpty())
			return returnValues;
		
		Iterator<Resource> scanResources = allResources.iterator();
		while (scanResources.hasNext()) {
			Resource resource = scanResources.next();
			Owner resourceOwner = resource.getOwner();
			if (resourceOwner != null && resourceOwner.equals(owner)){
				returnValues.add(resource);
			}
		}
		
		return returnValues;
		
	}

	@Override
	public List<Resource> getAllResourcesFor(boolean isDiscoverable) throws CatalogException {
		List<Resource> returnValues = new ArrayList<Resource>();
		
		Collection<Resource> allResources = this.resources.values();
		if (allResources.isEmpty())
			return returnValues;
		
		Iterator<Resource> scanResources = allResources.iterator();
		while (scanResources.hasNext()) {
			Resource resource = scanResources.next();
			if (resource.isDiscoverable()){
				returnValues.add(resource);
			}
		}
		
		return returnValues;
	}

	@Override
	public List<Resource> getAllResourcesFor(String resourceGroup) throws CatalogException {
	List<Resource> returnValues = new ArrayList<Resource>();
		
		Collection<Resource> allResources = this.resources.values();
		if (allResources.isEmpty())
			return returnValues;
		
		Iterator<Resource> scanResources = allResources.iterator();
		while (scanResources.hasNext()) {
			Resource resource = scanResources.next();
			if (resource.getResourceGroup() != null && resource.getResourceGroup().equalsIgnoreCase(resourceGroup)){
				returnValues.add(resource);
			}
		}
		
		return returnValues;
	}

		

}
