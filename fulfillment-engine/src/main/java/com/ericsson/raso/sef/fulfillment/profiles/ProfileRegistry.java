package com.ericsson.raso.sef.fulfillment.profiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.SecureSerializationHelper;

public class ProfileRegistry implements IProfileRegistry {

	
	private Map<String, FulfillmentProfile<?, Map<String, String>>> profiles = new TreeMap<String, FulfillmentProfile<?, Map<String, String>>>();;

	private String profileRegistryLocation = null;
	private SecureSerializationHelper ssh = null;

	public ProfileRegistry() {
		
		profileRegistryLocation = getStoreLocation();
		
		ssh = new SecureSerializationHelper();

		if (ssh.fileExists(this.profileRegistryLocation)) {
			try {
				this.profiles = (Map<String, FulfillmentProfile<?, Map<String, String>>>) ssh.fetchFromFile(this.profileRegistryLocation);
			} catch (FrameworkException e) {
				// TODO: LOgger on this error...
			}
		}
	}
	
	private String getStoreLocation() {
		String profileStoreLoc = System.getenv("SEF_CATALOG_HOME");
		String filename = "profileRegistry.ccm";
		String finalfile = "";
		String your_os = System.getProperty("os.name").toLowerCase();
		if(your_os.indexOf("win") >= 0){
			finalfile = profileStoreLoc + "\\" + filename;
		}else if(your_os.indexOf( "nix") >=0 || your_os.indexOf( "nux") >=0){
			finalfile = profileStoreLoc + "/" + filename;
		}else{
			finalfile = profileStoreLoc + "{others}" + filename;
		}
		
		return finalfile;
	}
	
	@Override
	public boolean createProfile(
			FulfillmentProfile<?, Map<String, String>> profile)
			throws CatalogException {
		if (profile == null)
			throw new CatalogException("Given Resource was null!!");

		if (!this.profiles.containsValue(profile))
			this.profiles.put(profile.getName(), profile);
		else 
			throw new CatalogException("Given Profile (" + profile + ") already exists!!");

		try {
			this.ssh.persistToFile(this.profileRegistryLocation, (Serializable) this.profiles);
			return true;
		} catch (FrameworkException e) {
			throw new CatalogException("Failed to persist changes!!", e);
		}
		
	}

	@Override
	public boolean updateProfile(
			FulfillmentProfile<?, Map<String, String>> profile)
			throws CatalogException {
		if (profile == null)
			throw new CatalogException("Given profile was null!!");

		if (this.profiles.isEmpty())
			return false;

		if (this.profiles.containsValue(profile))
			this.profiles.put(profile.getName(), profile);
		else
			return false;

		try {
			this.ssh.persistToFile(this.profileRegistryLocation, (Serializable) this.profiles);
			return true;
		} catch (FrameworkException e) {
			throw new CatalogException("Failed to persist changes!!", e);
		}

	}

	@Override
	public boolean deleteProfile(
			FulfillmentProfile<?, Map<String, String>> profile)
			throws CatalogException {
		if (profile == null)
			throw new CatalogException("Given profile was null!!");

		if (this.profiles.isEmpty())
			return false;

		
		if (this.profiles.containsValue(profile)) {
			
			remove(profile.getName(), profile);
		} else
			return false;

		try {
			this.ssh.persistToFile(this.profileRegistryLocation, (Serializable) this.profiles);
			return true;
		} catch (FrameworkException e) {
			throw new CatalogException("Failed to persist changes!!", e);
		}

	}

	private boolean remove(String name, FulfillmentProfile<?, Map<String, String>> profile) {
		
		if(this.profiles.containsKey(name) && Objects.equals(this.profiles.get(name), profile) ) {
			this.profiles.remove(name);
		}
		return false;
	}
	
	
	

	@Override
	public List<FulfillmentProfile<?, Map<String, String>>> getAllProfiles()
			throws CatalogException {
		Collection<FulfillmentProfile<?, Map<String, String>>> allResources = this.profiles.values();
		if (allResources.isEmpty())
			return new ArrayList<FulfillmentProfile<?, Map<String, String>>>();
		return Arrays.asList((FulfillmentProfile<?, Map<String, String>>[]) allResources.toArray());
	}

	@Override
	public FulfillmentProfile<?, Map<String, String>> readProfile(String profile)
			throws CatalogException {
		if (profile == null)
			throw new CatalogException("Given profile was null!!");

		if (this.profiles.isEmpty())
			return null;

		return this.profiles.get(profile);
	}

}
