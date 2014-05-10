package com.ericsson.raso.sef.bes.prodcat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Presents a state machine for a lifecycle of Subscription. This enum is intended to enrich any event involved in product discovery,
 * purchase and its lifecycle.
 * 
 * Please note: this is not related to product's lifecycle, while both product and subscriptions have impacts to each other's life-cycle.
 * 
 * @author esatnar
 */
public enum SubscriptionLifeCycleState {
	/**
	 * This state indicates a subscription is in a transient state representing a new purchase/ order is in progress.
	 */
	IN_ACTIVATION,

	/**
	 * This state indicates a subscription is in a stable active state. the subscriber can avail/ consume the relevant service/ resource in
	 * this state.
	 */
	ACTIVE,

	/**
	 * This state indicates a subscription is in a transient state representing a renewal is in progress.
	 */
	IN_RENEWAL,

	/**
	 * This state indicates a subscription is in a transient state representing a renewal is successful. This state is required to capture
	 * auto-termination, minimum commitment and other complex rules based on subscriber behavior.
	 */
	RENEWAL_SUCCESS,

	/**
	 * This state indicates a subscription is in a stable state representing a renewal is pending in lieu of system or subscriber
	 * insufficient funds. Until the renewal is successful, the user cannot avail/ consume this subscription access. Once the renewal
	 * profile has run out of attempts unsuccessfully, the subscription is moved to TERMINATED. If renewal was successful within the renewal
	 * profile, the subscription is reverted back to ACTIVE state.
	 */
	RENEWAL_SUSPENSION,

	/**
	 * This state indicates a subscription is in a stable state representing a administrative barring for various reasons such as abusive
	 * behavior, law enforcement, bad payment behavior, fraudulent behavior, credit risks, etc.
	 */
	BARRING_SUSPENSION,

	/**
	 * This state indicates a subscription is in a transient state representing a subscriber requested termination is in progress.
	 */
	IN_TERMINATION,

	/**
	 * This state indicates a subscription is in a transient state representing a expiry workflow is in progress.
	 */
	IN_EXPIRY,

	/**
	 * This state indicates a subscription is in a stable closed state. the subscription is closed and archived to history in subscriber
	 * profile and cannot be revived.
	 */
	CLOSED;

	public List<SubscriptionLifeCycleState> nextAllowedStates() {
		List<SubscriptionLifeCycleState> nextStates = null;
		SubscriptionLifeCycleState[] states = null;

		switch (this) {
			case IN_ACTIVATION:
				states = new SubscriptionLifeCycleState[] { ACTIVE, CLOSED };
				break;
			case ACTIVE:
				states = new SubscriptionLifeCycleState[] { IN_RENEWAL, IN_TERMINATION, IN_EXPIRY, BARRING_SUSPENSION };
				break;
			case IN_RENEWAL:
				states = new SubscriptionLifeCycleState[] { RENEWAL_SUCCESS, RENEWAL_SUSPENSION, CLOSED };
				break;
			case RENEWAL_SUCCESS:
				states = new SubscriptionLifeCycleState[] { ACTIVE };
				break;
			case IN_TERMINATION:
				states = new SubscriptionLifeCycleState[] { CLOSED };
				break;
			case IN_EXPIRY:
				states = new SubscriptionLifeCycleState[] { CLOSED };
				break;
			case BARRING_SUSPENSION:
				states = new SubscriptionLifeCycleState[] { ACTIVE, CLOSED };
				break;
			case RENEWAL_SUSPENSION:
				states = new SubscriptionLifeCycleState[] { RENEWAL_SUCCESS, CLOSED };
				break;
			case CLOSED:
				states = new SubscriptionLifeCycleState[] {};
				break;

		}

		return Arrays.asList(states);
	}

}
