package com.ericsson.raso.sef.bes.prodcat.db.mapper;

import com.ericsson.raso.sef.bes.prodcat.entities.PurchaseHistory;

public interface PurchaseHistoryMapper {

	void createPurchaseHIstory(PurchaseHistory purchaseHistory);

	void updatePurchaseHistory(PurchaseHistory purchaseHistory);

	void deletePurchaseHistory(String subsciptionId);

	PurchaseHistory getPurchaseHistory(String subscriptionId);

	String findLastSubscriptionLifeCycleEvent_in_Subscription_Purchase_History(
			String subscriptionId, String eventId);

	String findFirstSubscriptionLifeCycleEvent_in_Subscription_PurchaseHistory(
			String subscriptionId, String eventId);
	
	String findLastPurchaseState(String subscriptionId);
	
	
}
