package com.ericsson.raso.sef.bes.prodcat;

import java.io.Serializable;

/**
 * Presents a governing structure for a lifecycle of Subscription. This enum is intended to enrich any event involved in product discovery,
 * purchase and its lifecycle.
 * 
 * Please note: this is not related to product's lifecycle, while both product and subscriptions have impacts to each other's life-cycle.
 * 
 * @author esatnar 
 */
public enum SubscriptionLifeCycleEvent implements Serializable {
	/**
	 * This state indicates a product in discovery mode; not yet attached to a subscriber profile.
	 */
	DISCOVERY,
	
	/**
	 * This state indicates a product is attached to a subscriber profile as a subscription. AN event on this state indicates workflows &
	 * triggers required for completing a new purchase, such as service order fulfillment.
	 */
	PURCHASE,
	
	/**
	 * This state indicates a product is attached to a subscriber profile and the subscription validity is closing and is triggered for
	 * pre-renewal actions - notifications, , etc.
	 */
	PRE_RENEWAL,
	
	/**
	 * This state indicates a product is attached to a subscriber profile and the subscription validity is closing and is triggered for
	 * renewal.
	 */
	RENEWAL,
	
	/**
	 * This state indicates a product is attached to a subscriber profile and the subscription validity is closing and is triggered for
	 * pre-expiry actions - notifications, , etc.
	 */
	PRE_EXPIRY,
	
	/**
	 * This state indicates a product is attached to a subscriber profile and the subscription validity is closing and is triggered for
	 * expiry (closure of all purchases, workflows such as provisioning & fulfillment, audit trail, update history, etc.
	 */
	EXPIRY,
	
	/**
	 * This state indicates a product is attached to a subscriber profile and the subscription is being requested for termination. All
	 * workflows are similar to <code>EXPIRY</code>. Typically, such events are useful to trigger external events such as voluntary churn
	 * handling.
	 */
	TERMINATE;

}
