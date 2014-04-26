package com.ericsson.raso.sef.auth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple;
import com.ericsson.raso.sef.auth.permissions.Permission;
import com.ericsson.raso.sef.auth.permissions.Privilege;

import static com.ericsson.raso.sef.auth.permissions.AuthorizationPrinciple.*;

public class PrivilegeManager {
	
	Map<AuthorizationPrinciple, Privilege> privileges = new HashMap<AuthorizationPrinciple, Privilege>();
	
	public PrivilegeManager(String privilegesStoreLocation) {
		if (storeExists(privilegesStoreLocation)) {
			if (!loadFromStore(privilegesStoreLocation))
					loadDefaults();
			//TODO: Logger
		} else
			loadDefaults();
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
		this.privileges.put(DOMAIN_PARTNER_META, partnerAdmin);
		this.privileges.put(DOMAIN_PARTNER_LIFECYCLE, partnerAdmin);
		this.privileges.put(DOMAIN_PARTNER_SUBSCRIPTIONS_ALL, partnerAdmin);
		this.privileges.put(DOMAIN_PARTNER_SUBSCRIPTIONS_SELF, partnerAdmin);
		this.privileges.put(DOMAIN_PARTNER_PRODUCTS_ALL, partnerAdmin);
		this.privileges.put(DOMAIN_PARTNER_PRODUCTS_SELF, partnerAdmin);
		this.privileges.put(DOMAIN_PARTNER_SETTLEMENT_ALL, partnerAdmin);
		this.privileges.put(DOMAIN_PARTNER_SETTLEMENT_SELF, partnerAdmin);
		

		this.privileges.put(DOMAIN_USER_ALL, userAdmin);
		this.privileges.put(DOMAIN_USER_USER, user);
		this.privileges.put(DOMAIN_USER_GROUP, group);
		this.privileges.put(DOMAIN_USER_PRIVILEGES, permissions);
		
		this.privileges.put(SERVICE_CTXT_ALL, serviceCtxtAdmin);
		this.privileges.put(SERVICE_CTXT_INTERFACE, serviceCtxtInterface);
		this.privileges.put(SERVICE_CTXT_PRIMITIVE, serviceCtxtPrimitive);
		this.privileges.put(SERVICE_CTXT_PARAMETER, serviceCtxtParameter);
		
		
		
	}

	private boolean loadFromStore(String privilegesStoreLocation) {
		try {
			FileInputStream fisStore = new FileInputStream(privilegesStoreLocation);
			ObjectInputStream oisStore = new ObjectInputStream(fisStore);
			this.privileges = (HashMap<AuthorizationPrinciple, Privilege>) oisStore.readObject();
			
			oisStore.close();
			fisStore.close();
			oisStore = null;
			fisStore = null;
			
			return true;
		} catch (FileNotFoundException e) {
			// TODO Logger
			return false;
		} catch (IOException e) {
			// TODO Logger
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Logger
			return false;
		}
		
	}

	private boolean storeExists(String privilegesStoreLocation) {
		try {
			new FileInputStream(privilegesStoreLocation).close();;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

}
