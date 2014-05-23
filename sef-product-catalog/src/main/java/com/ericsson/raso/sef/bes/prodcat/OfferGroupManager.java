package com.ericsson.raso.sef.bes.prodcat;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.bes.prodcat.service.IOfferGroupManager;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.SecureSerializationHelper;

//TODO: this class is expected to be a singleton in Spring Framework...


/**
 * Offer Groups collect a logical set of offers. Each offer in a group is assigned a unique priority that determines if a
 * 'switch' request is an upgrade or downgrade. This priority overrides auto-calculated weightage that will be used to determine
 * upgrade/ downgrade. In the absence of this value, then the system auto-calculates the weightage using Characteristics - Quota,
 * Validity, SLA & Resource Priority.
 */

public class OfferGroupManager implements  IOfferGroupManager {
	
	private Map<String, Map<String, Byte>> offerGroups = new TreeMap<String, Map<String, Byte>>();
	private String offerGroupsStoreLocation = null;
	private SecureSerializationHelper ssh = null;
	

	private static IOfferGroupManager instance = null;

	public OfferGroupManager() {
		ssh = new SecureSerializationHelper();

		// TODO: fetch the store location from config, once the config services is ready....
		offerGroupsStoreLocation = getStoreLocation();

		if (ssh.fileExists(this.offerGroupsStoreLocation)) {
			try {
				this.offerGroups = (Map<String, Map<String, Byte>>) ssh.fetchFromFile(this.offerGroupsStoreLocation);
				if (this.offerGroups == null)
					this.offerGroups = new TreeMap<String, Map<String,Byte>>();
			} catch (FrameworkException e) {
				this.offerGroups = new TreeMap<String, Map<String,Byte>>();
			}
		}
	}

	
	private String getStoreLocation() {
		String offerStoreLocation = System.getenv("SEF_CATALOG_HOME");
		String filename = "offerGroupStore.ccm";
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
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferGroupManager#getOfferGroup(java.lang.String)
	 */
	@Override
	public Map<String, Byte> getOfferGroup(String offerGroup) throws CatalogException {
		if (offerGroup == null)
			throw new CatalogException("Given offerGroup was null");

		if (this.offerGroups == null)
			throw new CatalogException("Offer Group Manager not initialized -OR- No Groups defined yet!!");

		return this.offerGroups.get(offerGroup);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferGroupManager#setOfferGroup(java.lang.String, java.util.Map)
	 */
	@Override
	public void setOfferGroup(String offerGroup, Map<String, Byte> offers) throws CatalogException {
		if (offerGroup == null)
			throw new CatalogException("Given offerGroup was null");

		if (offers == null)
			throw new CatalogException("Given offers was null");

		if (this.offerGroups == null)
			this.offerGroups = new TreeMap<String, Map<String, Byte>>();

		this.offerGroups.put(offerGroup, offers);
		
		try {
			this.ssh.persistToFile(this.offerGroupsStoreLocation, (Serializable) this.offerGroups);
		} catch (FrameworkException e) {
			throw new CatalogException("Unable to save changes!!!", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferGroupManager#defineOfferGroupAndPriority(java.lang.String, java.lang.String, byte)
	 */
	@Override
	public void defineOfferGroupAndPriority(String groupName, String offerName, byte priority) throws CatalogException {
		if (groupName == null)
			throw new CatalogException("Given groupName was null");

		if (offerName == null)
			throw new CatalogException("Given offerName was null");

		Map<String, Byte> offerGroup = this.offerGroups.get(groupName);
		if (offerGroup == null)
			offerGroup = new TreeMap<String, Byte>();

		Byte currentPriority = offerGroup.get(offerName);
		if (currentPriority == null) {
			offerGroup.put(offerName, currentPriority);
		} else {
			if (currentPriority != priority && offerGroup.containsValue(priority))
				throw new CatalogException("Offer: " + offerName + " with Priority: " + priority
						+ " is conflicting with other offer of same priority!!");
			offerGroup.put(offerName, priority);
		}

		this.offerGroups.put(groupName, offerGroup);

		try {
			this.ssh.persistToFile(this.offerGroupsStoreLocation, (Serializable) this.offerGroups);
		} catch (FrameworkException e) {
			throw new CatalogException("Unable to save changes!!!", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferGroupManager#unsetOfferGroupAndPriority(java.lang.String, java.lang.String)
	 */
	@Override
	public void unsetOfferGroupAndPriority(String groupName, String offerName) throws CatalogException {
		if (groupName == null)
			throw new CatalogException("Given groupName was null");

		if (offerName == null)
			throw new CatalogException("Given offerName was null");

		if (this.offerGroups.isEmpty())
			return;

		Map<String, Byte> offerGroup = this.offerGroups.get(groupName);
		if (offerGroup == null)
			return;

		offerGroup.remove(offerName);
		if (offerGroup.isEmpty())
			this.offerGroups.remove(groupName);

		try {
			this.ssh.persistToFile(this.offerGroupsStoreLocation, (Serializable) this.offerGroups);
		} catch (FrameworkException e) {
			throw new CatalogException("Unable to save changes!!!", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.ericsson.raso.sef.bes.prodcat.IOfferGroupManager#getOfferPriority(java.lang.String, java.lang.String)
	 */
	@Override
	public Byte getOfferPriority(String groupName, String offerName) throws CatalogException {
		if (groupName == null)
			throw new CatalogException("Given groupName was null");

		if (offerName == null)
			throw new CatalogException("Given offerName was null");

		if (this.offerGroups == null)
			return null;

		Map<String, Byte> offerGroup = this.offerGroups.get(groupName);
		if (offerGroup == null)
			return null;

		return offerGroup.get(offerName);
	}

}
