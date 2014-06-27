package com.ericsson.raso.sef.core.db.mapper;

import java.util.Collection;
import java.util.Map;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.db.model.Purchase;
import com.ericsson.raso.sef.core.db.model.PurchaseMeta;
import com.ericsson.raso.sef.core.db.model.ObsoleteCodeDbSequence;

public interface PurchaseMapper {
	
	void createPurchase(Purchase purchase);
	
	void deletePurchase(String purchaseId);
	
	Collection<Purchase> getPurchase(String purchaseId);
	
	Collection<Purchase> getUserPurchase(Map<String, Object> map);
	
	Purchase getPurchaseByEventId(String eventId);
	
	Collection<Meta> getPurchaseMetas(Map<String, Object> map);
	
	void insertPurchaseMeta(PurchaseMeta purchaseMeta);
	
	void deletePurchaseMeta(String purchaseId);
	
	ObsoleteCodeDbSequence purchaseSequence(String rand);

}
