package com.ericsson.raso.sef.bes.prodcat;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.bes.prodcat.service.IResourceGroupManager;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.SecureSerializationHelper;

//TODO: this class is expected to be a singleton in Spring Framework...

/**
 * Resource Groups collect a logical set of resources. Each resource in a group is assigned a unique priority that determines if a
 * 'switch' request is an upgrade or downgrade. 
 * 
 */

public class ResourceGroupManager implements IResourceGroupManager {
	
	private Map<String, Map<String, Byte>> resourceGroups = new TreeMap<String, Map<String, Byte>>();
	private String resourceGroupsStoreLocation = null;
	private SecureSerializationHelper ssh = null;
	

	private static IResourceGroupManager instance = null;

	public ResourceGroupManager() {
		ssh = new SecureSerializationHelper();

		// TODO: fetch the store location from, once the config services is ready....
		resourceGroupsStoreLocation = getStoreLocation();

		if (ssh.fileExists(this.resourceGroupsStoreLocation)) {
			try {
				this.resourceGroups = (Map<String, Map<String, Byte>>) ssh.fetchFromFile(this.resourceGroupsStoreLocation);
				if (this.resourceGroups == null)
					this.resourceGroups = new TreeMap<String, Map<String,Byte>>();
			} catch (FrameworkException e) {
				this.resourceGroups = new TreeMap<String, Map<String,Byte>>();
			}
		}
	}
	
	
	private String getStoreLocation() {
		String offerStoreLocation = System.getenv("SEF_CATALOG_HOME");
		String filename = "resourceGroupStore.ccm";
		String finalfile = "";
		String your_os = System.getProperty("os.name").toLowerCase();
		if(your_os.indexOf("win") >= 0){
			finalfile = offerStoreLocation + "\\" + filename;
		}else if(your_os.indexOf( "nix") >=0 || your_os.indexOf( "nux") >=0){
			finalfile = offerStoreLocation + "/" + filename;
		}else{
			finalfile = offerStoreLocation + "{others}" + filename;
		}
		
		return finalfile;
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IResourceGroupManager#getResourceGroup(java.lang.String)
	 */
	@Override
	public Map<String, Byte> getResourceGroup(String resourceGroup) throws CatalogException {
		if (resourceGroup == null)
			throw new CatalogException("Given resourceGroup was null");

		if (this.resourceGroups == null)
			throw new CatalogException("Resource Group Manager not initialized -OR- No Groups defined yet!!");

		return this.resourceGroups.get(resourceGroup);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IResourceGroupManager#setResourceGroup(java.lang.String, java.util.Map)
	 */
	@Override
	public void setResourceGroup(String resourceGroup, Map<String, Byte> resources) throws CatalogException {
		if (resourceGroup == null)
			throw new CatalogException("Given resourceGroup was null");

		if (resources == null)
			throw new CatalogException("Given resources was null");

		if (this.resourceGroups == null)
			this.resourceGroups = new TreeMap<String, Map<String, Byte>>();

		this.resourceGroups.put(resourceGroup, resources);
		
		try {
			this.ssh.persistToFile(this.resourceGroupsStoreLocation, (Serializable) this.resourceGroups);
		} catch (FrameworkException e) {
			throw new CatalogException("Unable to save changes!!!", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IResourceGroupManager#defineResourceGroupAndPriority(java.lang.String, java.lang.String, byte)
	 */
	@Override
	public void defineResourceGroupAndPriority(String groupName, String resourceName, byte priority) throws CatalogException {
		if (groupName == null)
			throw new CatalogException("Given groupName was null");

		if (resourceName == null)
			throw new CatalogException("Given resourceName was null");

		Map<String, Byte> resourceGroup = this.resourceGroups.get(groupName);
		if (resourceGroup == null)
			resourceGroup = new TreeMap<String, Byte>();

		Byte currentPriority = resourceGroup.get(resourceName);
		if (currentPriority == null) {
			resourceGroup.put(resourceName, currentPriority);
		} else {
			if (currentPriority != priority && resourceGroup.containsValue(priority))
				throw new CatalogException("Resource: " + resourceName + " with Priority: " + priority
						+ " is conflicting with other offer of same priority!!");
			resourceGroup.put(resourceName, priority);
		}

		this.resourceGroups.put(groupName, resourceGroup);

		try {
			this.ssh.persistToFile(this.resourceGroupsStoreLocation, (Serializable) this.resourceGroups);
		} catch (FrameworkException e) {
			throw new CatalogException("Unable to save changes!!!", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IResourceGroupManager#unsetResourceGroupAndPriority(java.lang.String, java.lang.String)
	 */
	@Override
	public void unsetResourceGroupAndPriority(String groupName, String resourceName) throws CatalogException {
		if (groupName == null)
			throw new CatalogException("Given groupName was null");

		if (resourceName == null)
			throw new CatalogException("Given resourceName was null");

		if (this.resourceGroups.isEmpty())
			return;

		Map<String, Byte> resourceGroup = this.resourceGroups.get(groupName);
		if (resourceGroup == null)
			return;

		resourceGroup.remove(resourceName);
		if (resourceGroup.isEmpty())
			this.resourceGroups.remove(groupName);

		try {
			this.ssh.persistToFile(this.resourceGroupsStoreLocation, (Serializable) this.resourceGroups);
		} catch (FrameworkException e) {
			throw new CatalogException("Unable to save changes!!!", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IResourceGroupManager#getResourcePriority(java.lang.String, java.lang.String)
	 */
	@Override
	public Byte getResourcePriority(String groupName, String resourceName) throws CatalogException {
		if (groupName == null)
			throw new CatalogException("Given groupName was null");

		if (resourceName == null)
			throw new CatalogException("Given resourceName was null");

		if (this.resourceGroups == null)
			return null;

		Map<String, Byte> resourceGroup = this.resourceGroups.get(groupName);
		if (resourceGroup == null)
			return null;

		return resourceGroup.get(resourceName);
	}

}
