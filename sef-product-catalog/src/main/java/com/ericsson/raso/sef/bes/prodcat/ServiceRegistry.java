package com.ericsson.raso.sef.bes.prodcat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.entities.Owner;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.service.IServiceRegistry;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.bes.prodcat.SecureSerializationHelper;


//TODO: please udate the code implemenation to also synchronize with Hazelcast...

public final class ServiceRegistry implements IServiceRegistry {
	
	private Map<String, Resource> resources = new TreeMap<String, Resource>();;
	private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

	private String serviceRegistryLocation = null;
	private SecureSerializationHelper ssh = null;

	public ServiceRegistry() {
		// TODO: fetch the store location from config, once the config services
		// is ready....

		serviceRegistryLocation = getStoreLocation();
		
		ssh = new SecureSerializationHelper();

		if (ssh.fileExists(this.serviceRegistryLocation)) {
			try {
				this.resources = (Map<String, Resource>) ssh.fetchFromFile(this.serviceRegistryLocation);
				logger.info("Successfully loaded resources in Service Registry, Total resources loaded: " + this.resources.size());
			} catch (FrameworkException e) {
				logger.error("Error loading service registry, Exception: " +  e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	private String getStoreLocation() {
		String offerStoreLocation = System.getenv("SEF_CATALOG_HOME");
		String filename = "serviceRegistry.ccm";
		String finalfile = "";
		String your_os = System.getProperty("os.name").toLowerCase();
		if(your_os.indexOf("win") >= 0){
			finalfile = offerStoreLocation + "\\" + filename;
		}else if(your_os.indexOf( "nix") >=0 || your_os.indexOf( "nux") >=0){
			finalfile = offerStoreLocation + "/" + filename;
		}else{
			finalfile = offerStoreLocation + "/" + filename;
		}
		logger.debug("ServiceRegisty Store location: " +  finalfile);
		return finalfile;
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IServiceRegistry#createResource(com.ericsson.raso.sef.bes.prodcat.entities.Resource)
	 */
	@Override
	public boolean createResource(Resource resource) throws CatalogException {
		if (resource == null)
			throw new CatalogException("Given Resource was null!!");

		if (!this.resources.containsValue(resource) && !this.resources.containsKey(resource.getName()))
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
			
			remove(resource.getName(), resource);
		} else
			return false;

		try {
			this.ssh.persistToFile(this.serviceRegistryLocation, (Serializable) this.resources);
			return true;
		} catch (FrameworkException e) {
			throw new CatalogException("Failed to persist changes!!", e);
		}

	}

	private boolean remove(String name, Resource resource) {
		
		if(this.resources.containsKey(name) && Objects.equals(this.resources.get(name), resource) ) {
			this.resources.remove(name);
		}
		return false;
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
