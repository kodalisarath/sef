package com.ericsson.raso.sef.auth;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;
import com.ericsson.raso.sef.auth.permissions.Permission;
import com.ericsson.raso.sef.auth.permissions.Privilege;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.SecureSerializationHelper;

import static com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple.*;

public class PrivilegeManager {
	
	private String privilegesStoreLocation = null;
	private Map<AuthorizationPrinciple, Privilege> privileges = null;
	
	private SecureSerializationHelper ssh = null;
	
	
	public PrivilegeManager(String storeLocation) {
		ssh = new SecureSerializationHelper();
		
		this.privilegesStoreLocation = storeLocation;
		if (ssh.fileExists(storeLocation)) {
			try {
				this.privileges = (Map<AuthorizationPrinciple, Privilege>) ssh.fetchFromFile(storeLocation);
				if (this.privileges == null)
					loadDefaults();
			} catch (FrameworkException e) {
				loadDefaults();
			}
		}			
	}
	
	public PrivilegeManager() {
		//TODO: fetch the store location from config, once the config services is ready....
		
		if (ssh.fileExists(this.privilegesStoreLocation)) {
			try {
				this.privileges = (Map<AuthorizationPrinciple, Privilege>) ssh.fetchFromFile(this.privilegesStoreLocation);
				if (this.privileges == null)
					loadDefaults();
			} catch (FrameworkException e) {
				loadDefaults();
			}
		}			
	}
	
	public boolean createPermission(Privilege privilege) throws AuthAdminException {
		if (this.privileges == null) 
			this.privileges = new HashMap<AuthorizationPrinciple, Privilege>();
		
		if (this.privileges.containsKey(privilege.getName()))
			throw new AuthAdminException("Duplicate Privilege [" + privilege + "] cannot be created.");
		
		System.out.println("Adding Pemission: " + privilege);
		this.privileges.put(privilege.getName(), privilege);
		
			
		try {
			this.ssh.persistToFile(this.privilegesStoreLocation, (Serializable) this.privileges);
			return true;
		} catch (FrameworkException e) {
			return false;
		}
	}
	
	public Privilege readPermission(AuthorizationPrinciple principle) throws AuthAdminException {
		if (principle == null)
			throw new AuthAdminException("Specified Privilege was null");
			
		if (this.privileges.containsKey(principle)) {
			return this.privileges.get(principle);
		}
		
		throw new AuthAdminException("Specified Privilege [" + principle + "] cannot be found.");
	}
	
	public Privilege readPermission(String principle) throws AuthAdminException {
		if (principle == null)
			throw new AuthAdminException("Specified Privilege was null");
		
		try {
			AuthorizationPrinciple key = AuthorizationPrinciple.valueOf(principle);
			if (this.privileges.containsKey(key))
				return this.privileges.get(key);
			else
				throw new AuthAdminException("Specified Privilege [" + principle + "] cannot be found.");
		} catch (IllegalArgumentException e) {
			throw new AuthAdminException("Specified Privilege was not defined/ created yet");
		}
		
			
	}
	
	public boolean updatePermission (Privilege privilege) throws AuthAdminException {
		if (this.privileges == null) {
			throw new AuthAdminException("Specified Privilege [" + privilege + "] cannot be found to update.");
		}
		
		if (!this.privileges.containsKey(privilege.getName()))
			throw new AuthAdminException("Specified Privilege [" + privilege + "] cannot be found to update.");
		
		this.privileges.put(privilege.getName(), privilege);

		try {
			this.ssh.persistToFile(this.privilegesStoreLocation, (Serializable) this.privileges);
			return true;
		} catch (FrameworkException e) {
			return false;
		}
	}
	
	public boolean deletePermission (Privilege privilege) throws AuthAdminException {
		if (this.privileges == null) {
			throw new AuthAdminException("Specified Privilege [" + privilege + "] cannot be found to delete.");
		}
		
		
		boolean isNotFound = true;
		Iterator<Privilege> privileges = this.privileges.values().iterator();
		while (privileges.hasNext()) {
			Privilege fromStore = privileges.next();
			if (fromStore.contains(privilege)) {
				isNotFound = false;
				fromStore.removeImplied(privilege);
			}
		}
		
		this.privileges.remove(privilege.getName());
		
		if (isNotFound)
			throw new AuthAdminException("Specified Privilege [" + privilege + "] cannot be found to delete.");
	
		try {
			this.ssh.persistToFile(this.privilegesStoreLocation, (Serializable) this.privileges);
			return true;
		} catch (FrameworkException e) {
			return false;
		}
	}
	
	
	private void loadDefaults() {
		
		// Entity Access Permissions - Broad Use-Case Classification to support ENTITY & INGRESS Controls)
		Permission entityAdmin = new Permission(ACCESS_ALL);
		Permission readOnly = new Permission(ACCESS_READ_ONLY);
		Permission create = new Permission(ACCESS_CREATE_NEW);
		Permission update = new Permission(ACCESS_UPDATE);
		Permission delete = new Permission(ACCESS_DELETE);
		
		create.addImplied(readOnly);
		update.addImplied(readOnly);
		delete.addImplied(readOnly);
		entityAdmin.addImplied(create);
		entityAdmin.addImplied(readOnly);
		entityAdmin.addImplied(update);
		entityAdmin.addImplied(delete);
		
		
		
		// Entity Domain Permissions (ENTITY Domain Control - Granular Info Security)
		Permission domainAdmin = new Permission(DOMAIN_ALL);
		Permission subscriberAdmin = new Permission(DOMAIN_SUBSCRIBER_ALL);
		Permission subscriptionAdmin = new Permission(DOMAIN_SUBSCRIPTION_ALL);
		Permission productAdmin = new Permission(DOMAIN_PRODUCT_ALL);
		Permission serviceAdmin = new Permission(DOMAIN_SERVICE_ALL);
		Permission partnerAdmin = new Permission(DOMAIN_PARTNER_ALL);
		Permission userAdmin = new Permission(DOMAIN_USER_ALL);
		
		domainAdmin.addImplied(subscriberAdmin);
		domainAdmin.addImplied(subscriptionAdmin);
		domainAdmin.addImplied(productAdmin);
		domainAdmin.addImplied(serviceAdmin);
		domainAdmin.addImplied(partnerAdmin);
		domainAdmin.addImplied(userAdmin);
		
		
		/// Subscriber Domain Controls
		Permission subscriberMeta = new Permission(DOMAIN_SUBSCRIBER_META, "META_LIST");
		Permission subscriberWorkflow = new Permission(DOMAIN_SUBSCRIBER_WORKFLOW, "ACTION_LIST");
		Permission subscriberLifeCycle = new Permission(DOMAIN_SUBSCRIBER_LIFE_CYCLE, "EVENT_LIST");
		Permission subscriberMvnoAll = new Permission(DOMAIN_SUBSCRIBER_MVNO_ALL);
		Permission subscriberMvnoOwn = new Permission(DOMAIN_SUBSCRIBER_MVNO_OWN, "MVNO_LIST");
		Permission subscriberMarketAll = new Permission(DOMAIN_SUBSCRIBER_MARKET_ALL);
		Permission subscriberMarketOwn = new Permission(DOMAIN_SUBSCRIBER_MARKET_OWN, "MARKET_LIST");
		
		subscriberAdmin.addImplied(subscriberMeta);
		subscriberAdmin.addImplied(subscriberWorkflow);
		subscriberAdmin.addImplied(subscriberLifeCycle);
		subscriberAdmin.addImplied(subscriberMvnoAll);
		subscriberAdmin.addImplied(subscriberMvnoOwn);
		subscriberAdmin.addImplied(subscriberMarketAll);
		subscriberAdmin.addImplied(subscriberMarketOwn);
		
		
		/// Subscription Domain Controls
		Permission subscriptionMeta = new Permission(DOMAIN_SUBSCRIPTION_META, "META_LIST");
		Permission subscriptionLifeCycle = new Permission(DOMAIN_SUBSCRIPTION_LIFECYCLE, "EVENT_LIST");
		
		subscriptionAdmin.addImplied(subscriptionMeta);
		subscriptionAdmin.addImplied(subscriptionLifeCycle);
		
		
		/// Product Domain Controls
		Permission productBusinessAll = new Permission(DOMAIN_PRODUCT_BUSINESS_ALL);
		Permission productBusinessOwn = new Permission(DOMAIN_PRODUCT_BUSINESS_OWN, "PRT_LIST");
		Permission productTechnicalAll = new Permission(DOMAIN_PRODUCT_BUSINESS_ALL);
		Permission productTechnicalOwn = new Permission(DOMAIN_PRODUCT_BUSINESS_OWN, "PRT_LIST");
		Permission productPriceAll = new Permission(DOMAIN_PRODUCT_BUSINESS_ALL);
		Permission productPriceOwn = new Permission(DOMAIN_PRODUCT_BUSINESS_OWN, "MVNO_LIST");
		
		productAdmin.addImplied(productBusinessAll);
		productAdmin.addImplied(productBusinessOwn);
		productAdmin.addImplied(productTechnicalAll);
		productAdmin.addImplied(productTechnicalOwn);
		productAdmin.addImplied(productPriceAll);
		productAdmin.addImplied(productPriceOwn);
		
		
		/// Service Registry Domain Controls
		Permission serviceFulfillment = new Permission(DOMAIN_SERVICE_FULFILLMENT);
		Permission serviceCostAll = new Permission(DOMAIN_SERVICE_COST_ALL);
		Permission serviceCostOwn = new Permission(DOMAIN_SERVICE_COST_OWN, "MARKET_LIST");
		Permission serviceBusinessAll = new Permission(DOMAIN_SERVICE_BUSINESS_ALL);
		Permission serviceBusinessOwn = new Permission(DOMAIN_SERVICE_BUSINESS_OWN, "MARKET_LIST");
		Permission serviceTechnicalAll = new Permission(DOMAIN_SERVICE_TECHNICAL_ALL);
		Permission serviceTechnicalOwn = new Permission(DOMAIN_SERVICE_TECHNICAL_OWN, "MARKET_LIST");
		
		serviceAdmin.addImplied(serviceFulfillment);
		serviceAdmin.addImplied(serviceCostAll);
		serviceAdmin.addImplied(serviceCostOwn);
		serviceAdmin.addImplied(serviceBusinessAll);
		serviceAdmin.addImplied(serviceBusinessOwn);
		serviceAdmin.addImplied(serviceTechnicalAll);
		serviceAdmin.addImplied(serviceTechnicalOwn);
		
		
		/// Partner Domain Controls
		Permission partnerMeta = new Permission(DOMAIN_PARTNER_META, "META_LIST");
		Permission partnerLifeCycle = new Permission(DOMAIN_PARTNER_LIFECYCLE, "EVENT_LIST");
		Permission partnerSubscriptionsAll = new Permission(DOMAIN_PARTNER_SUBSCRIPTIONS_ALL);
		Permission partnerSubscriptionsSelf = new Permission(DOMAIN_PARTNER_SUBSCRIPTIONS_SELF, "PRT_LIST");
		Permission partnerProductsAll = new Permission(DOMAIN_PARTNER_PRODUCTS_ALL);
		Permission partnerProductsSelf = new Permission(DOMAIN_PARTNER_PRODUCTS_SELF, "PRT_LIST");
		Permission partnerSettelementAll = new Permission(DOMAIN_PARTNER_SETTLEMENT_ALL);
		Permission partnerSettlementSelf = new Permission(DOMAIN_PARTNER_SETTLEMENT_SELF, "PRT_LIST");
		
		partnerAdmin.addImplied(partnerMeta);
		partnerAdmin.addImplied(partnerLifeCycle);
		partnerAdmin.addImplied(partnerSubscriptionsAll);
		partnerAdmin.addImplied(partnerSubscriptionsSelf);
		partnerAdmin.addImplied(partnerProductsAll);
		partnerAdmin.addImplied(partnerProductsSelf);
		partnerAdmin.addImplied(partnerSettelementAll);
		partnerAdmin.addImplied(partnerSettlementSelf);
		
		/// User Domain Controls
		Permission user = new Permission(DOMAIN_USER_USER);
		Permission group = new Permission(DOMAIN_USER_GROUP);
		Permission permissions = new Permission(DOMAIN_USER_PRIVILEGES);
		
		userAdmin.addImplied(user);
		userAdmin.addImplied(group);
		userAdmin.addImplied(permissions);
		
		
		
		// Service Context Permissions (INGRESS Control - to fail fast)
		Permission serviceCtxtAdmin = new Permission(SERVICE_CTXT_ALL);
		Permission serviceCtxtInterface = new Permission(SERVICE_CTXT_INTERFACE);
		Permission serviceCtxtPrimitive = new Permission(SERVICE_CTXT_PRIMITIVE);
		Permission serviceCtxtParameter = new Permission(SERVICE_CTXT_PARAMETER);
		
		serviceCtxtAdmin.addImplied(serviceCtxtInterface);
		serviceCtxtAdmin.addImplied(serviceCtxtPrimitive);
		serviceCtxtAdmin.addImplied(serviceCtxtParameter);
		
				
		// Load up the store with defaults....
		if (this.privileges == null)
			this.privileges = new HashMap<AuthorizationPrinciple, Privilege>();
		
		this.privileges.put(ACCESS_READ_ONLY, readOnly);
		this.privileges.put(ACCESS_CREATE_NEW, create);
		this.privileges.put(ACCESS_UPDATE, update);
		this.privileges.put(ACCESS_DELETE, delete);
		this.privileges.put(ACCESS_ALL, entityAdmin);
		

		this.privileges.put(DOMAIN_ALL, domainAdmin);
		this.privileges.put(DOMAIN_SUBSCRIBER_ALL, subscriberAdmin);
		this.privileges.put(DOMAIN_SUBSCRIPTION_ALL, subscriptionAdmin);
		this.privileges.put(DOMAIN_PRODUCT_ALL, productAdmin);
		this.privileges.put(DOMAIN_SERVICE_ALL, serviceAdmin);
		this.privileges.put(DOMAIN_PARTNER_ALL, partnerAdmin);
		this.privileges.put(DOMAIN_USER_ALL, userAdmin);
		
		this.privileges.put(DOMAIN_SUBSCRIBER_ALL, subscriberAdmin);
		this.privileges.put(DOMAIN_SUBSCRIBER_META, subscriberMeta);
		this.privileges.put(DOMAIN_SUBSCRIBER_WORKFLOW, subscriberWorkflow);
		this.privileges.put(DOMAIN_SUBSCRIBER_LIFE_CYCLE, subscriberLifeCycle);
		this.privileges.put(DOMAIN_SUBSCRIBER_MVNO_ALL, subscriberMvnoAll);
		this.privileges.put(DOMAIN_SUBSCRIBER_MVNO_OWN, subscriberMvnoOwn);
		this.privileges.put(DOMAIN_SUBSCRIBER_MARKET_ALL, subscriberMarketAll);
		this.privileges.put(DOMAIN_SUBSCRIBER_MARKET_OWN, subscriberMarketOwn);
		
		this.privileges.put(DOMAIN_SUBSCRIPTION_ALL, subscriptionAdmin);
		this.privileges.put(DOMAIN_SUBSCRIPTION_META, subscriptionMeta);
		this.privileges.put(DOMAIN_SUBSCRIPTION_LIFECYCLE, subscriptionLifeCycle);
		
		this.privileges.put(DOMAIN_PRODUCT_ALL, productAdmin);
		this.privileges.put(DOMAIN_PRODUCT_BUSINESS_ALL, productBusinessAll);
		this.privileges.put(DOMAIN_PRODUCT_BUSINESS_OWN, productBusinessOwn);
		this.privileges.put(DOMAIN_PRODUCT_TECHNICAL_ALL, productTechnicalAll);
		this.privileges.put(DOMAIN_PRODUCT_TECHNICAL_OWN, productTechnicalOwn);
		this.privileges.put(DOMAIN_PRODUCT_PRICE_ALL, productPriceAll);
		this.privileges.put(DOMAIN_PRODUCT_PRICE_OWN, productPriceOwn);
		
		this.privileges.put(DOMAIN_SERVICE_ALL, serviceAdmin);
		this.privileges.put(DOMAIN_SERVICE_FULFILLMENT, serviceFulfillment);
		this.privileges.put(DOMAIN_SERVICE_COST_ALL, serviceCostAll);
		this.privileges.put(DOMAIN_SERVICE_COST_OWN, serviceCostOwn);
		this.privileges.put(DOMAIN_SERVICE_BUSINESS_ALL, serviceBusinessAll);
		this.privileges.put(DOMAIN_SERVICE_BUSINESS_OWN, serviceBusinessOwn);
		this.privileges.put(DOMAIN_SERVICE_TECHNICAL_ALL, serviceTechnicalAll);
		this.privileges.put(DOMAIN_SERVICE_TECHNICAL_OWN, serviceTechnicalOwn);
		

		this.privileges.put(DOMAIN_PARTNER_ALL, partnerAdmin);
		this.privileges.put(DOMAIN_PARTNER_META, partnerMeta);
		this.privileges.put(DOMAIN_PARTNER_LIFECYCLE, partnerLifeCycle);
		this.privileges.put(DOMAIN_PARTNER_SUBSCRIPTIONS_ALL, partnerSubscriptionsAll);
		this.privileges.put(DOMAIN_PARTNER_SUBSCRIPTIONS_SELF, partnerSubscriptionsSelf);
		this.privileges.put(DOMAIN_PARTNER_PRODUCTS_ALL, partnerProductsAll);
		this.privileges.put(DOMAIN_PARTNER_PRODUCTS_SELF, partnerProductsSelf);
		this.privileges.put(DOMAIN_PARTNER_SETTLEMENT_ALL, partnerSettelementAll);
		this.privileges.put(DOMAIN_PARTNER_SETTLEMENT_SELF, partnerSettlementSelf);
		

		this.privileges.put(DOMAIN_USER_ALL, userAdmin);
		this.privileges.put(DOMAIN_USER_USER, user);
		this.privileges.put(DOMAIN_USER_GROUP, group);
		this.privileges.put(DOMAIN_USER_PRIVILEGES, permissions);
		
		this.privileges.put(SERVICE_CTXT_ALL, serviceCtxtAdmin);
		this.privileges.put(SERVICE_CTXT_INTERFACE, serviceCtxtInterface);
		this.privileges.put(SERVICE_CTXT_PRIMITIVE, serviceCtxtPrimitive);
		this.privileges.put(SERVICE_CTXT_PARAMETER, serviceCtxtParameter);
		
		// persist the entities
		try {
			this.ssh.persistToFile(this.privilegesStoreLocation, (Serializable) this.privileges);
			return;
		} catch (FrameworkException e) {
			return;
		}
		
	}

	
	@Override
	public String toString() {
		String privilegeDump = "<PrivilegeStore>";
		
		Iterator<Privilege> privileges = this.privileges.values().iterator();
		while (privileges.hasNext()) {
			Privilege fromStore = privileges.next();
			privilegeDump += fromStore.toString();
		}
		privilegeDump += "</PrivilegeStore>";
		
		return privilegeDump;
	}

}
