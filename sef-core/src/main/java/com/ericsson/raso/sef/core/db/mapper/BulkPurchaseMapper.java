package com.ericsson.raso.sef.core.db.mapper;

import com.ericsson.raso.sef.core.db.model.BulkPurchase;

public interface BulkPurchaseMapper {
	
	void createBulkPurchase(BulkPurchase bulkPurchase);
	
	Long purchaseSequence(String rand);
	
	Long commerceTrailSequence(String rand);

}
