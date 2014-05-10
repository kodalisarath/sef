package com.ericsson.raso.sef.bes.prodcat;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.bes.prodcat.entities.Owner;
import com.ericsson.raso.sef.bes.prodcat.service.IOwnerManager;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.SecureSerializationHelper;

public class OwnerManager implements IOwnerManager {
	
	private String ownersStoreLocation = null;
	private SecureSerializationHelper ssh = null;
	
	private Map<String, Owner> owners = new TreeMap<String, Owner>();
	
	public OwnerManager() {
		ssh = new SecureSerializationHelper();

		// TODO: fetch the store location from, once the config services is ready....

		if (ssh.fileExists(this.ownersStoreLocation)) {
			try {
				this.owners = (Map<String, Owner>) ssh.fetchFromFile(this.ownersStoreLocation);
				if (this.owners == null)
					this.owners = new TreeMap<String, Owner>();
			} catch (FrameworkException e) {
				this.owners = new TreeMap<String, Owner>();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOwnerManager#createOwner(com.ericsson.raso.sef.bes.prodcat.entities.Owner)
	 */
	@Override
	public boolean createOwner(Owner owner) throws CatalogException {
		if (this.owners == null) 
			this.owners = new TreeMap<String, Owner>();
		
		if (this.owners.containsKey(owner.getName()))
			throw new CatalogException("Duplicate Owner [" + owner + "] cannot be created.");
		
		System.out.println("Adding Pemission: " + owner);
		this.owners.put(owner.getName(), owner);
		
			
		try {
			this.ssh.persistToFile(this.ownersStoreLocation, (Serializable) this.owners);
			return true;
		} catch (FrameworkException e) {
			throw new CatalogException("Could not save the changes!!!", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOwnerManager#readOwner(java.lang.String)
	 */
	@Override
	public Owner readOwner(String owner) throws CatalogException {
		if (owner == null)
			throw new CatalogException("Specified Owner was null");
		
		try {
			if (this.owners.containsKey(owner))
				return this.owners.get(owner);
			else
				throw new CatalogException("Specified Owner [" + owner + "] cannot be found.");
		} catch (IllegalArgumentException e) {
			throw new CatalogException("Specified Owner was not defined/ created yet");
		}
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOwnerManager#updateOwner(com.ericsson.raso.sef.bes.prodcat.entities.Owner)
	 */
	@Override
	public boolean updateOwner(Owner owner) throws CatalogException {
		if (this.owners == null) {
			throw new CatalogException("Specified Owner [" + owner + "] cannot be found to update.");
		}
		
		if (!this.owners.containsKey(owner.getName()))
			throw new CatalogException("Specified Owner [" + owner + "] cannot be found to update.");
		
		this.owners.put(owner.getName(), owner);

		try {
			this.ssh.persistToFile(this.ownersStoreLocation, (Serializable) this.owners);
			return true;
		} catch (FrameworkException e) {
			throw new CatalogException("Could not save the changes!!!", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOwnerManager#deleteOwner(com.ericsson.raso.sef.bes.prodcat.entities.Owner)
	 */
	@Override
	public boolean deleteOwner(Owner owner) throws CatalogException {
		if (this.owners == null) {
			throw new CatalogException("Specified Owner [" + owner + "] cannot be found to delete.");
		}
		
		
		boolean isNotFound = true;
		Iterator<Owner> owners = this.owners.values().iterator();
		while (owners.hasNext()) {
			Owner fromStore = owners.next();
			if (fromStore.contains(owner)) {
				isNotFound = false;
				fromStore.removeMember(owner);
			}
		}
		
		this.owners.remove(owner.getName());
		
		if (isNotFound)
			throw new CatalogException("Specified Owner [" + owner + "] cannot be found to delete.");
	
		try {
			this.ssh.persistToFile(this.ownersStoreLocation, (Serializable) this.owners);
			return true;
		} catch (FrameworkException e) {
			throw new CatalogException("Could not save the changes!!!", e);
		}
	}
	
	




	

}
