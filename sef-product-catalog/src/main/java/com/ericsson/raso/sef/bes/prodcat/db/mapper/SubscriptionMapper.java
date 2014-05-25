package com.ericsson.raso.sef.bes.prodcat.db.mapper;

import java.util.List;

import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.Subscription;
import com.ericsson.raso.sef.bes.prodcat.entities.SubscriptionHistory;

public interface SubscriptionMapper {

	List<Subscription> getUserSubscriptions(String subscriberId);

	Subscription getSubscription(String subscriptionId, String offerId);

	SubscriptionHistory getSubscriptionHistory(String subscriptionId);

	void createSubscription(Subscription subscription);

	void updateSubscription(Subscription subscription);

	void deleteSubscription(String subscriptionId);

	void deleteSubscriptions(String subscriberId, String offerId);

	List<Subscription> getAllActiveSubscriptions(String resource);

	int getActiveSubscriptionsCount(String resource);

	boolean isEverSubscribedToOffer(String OfferId);

	boolean isEverSubscribedToResource(String resource);

	int getRenewalsCountToActiveSubscription_ForGivenOffer(
			String subscriptionId, String offerId);

	int getRenewalsCountToActiveSubscription_ForGivenResource(
			String subscriptionId, String resource);

	int getRenewalFailedCount_ForSubscriber(String subscriptionId);

	int getRenewalSuspensionsCount_On_Subscriber_AcrossAll_Active_ClosedSubscrptions(
			String subscriberId);

	int getRenewalSuspensionsCount_On_Subscriber_inGivenPeriod(
			String subscriberId);

	int getBarringSuspensionsCount_On_Subscriber(String subscriberId);

	int getBarringSuspensionsCount_On_Subscriber_inGivenPeriod(
			String subscriberId);

	int getBarredReactivatedCount_On_Subscriber(String subscriberId);

	int getBarredReactivatedCount_On_Subscriber_inGivenPeriod(
			String subscriberId);

	int getGraceActiveTransitionsCount_On_Subscriber(String subscriberId);

	int getGraceActiveTransitionsCount_On_Subscriber_inGivenPeriod(
			String subscriberId);

	String findLastSubscriptionState(String subscriptionId);

	String findLastSubscription_LifeCycleState(String subscriptionId);

	String findFirstSubscriptionLifeCycleState(String subscriptionId);

}
