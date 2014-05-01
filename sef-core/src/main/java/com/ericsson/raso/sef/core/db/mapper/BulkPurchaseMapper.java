package com.ericsson.raso.sef.core.db.mapper;

import com.ericsson.raso.sef.core.db.model.BulkPurchase;
import com.ericsson.raso.sef.core.db.model.SmSequence;

public interface BulkPurchaseMapper {
	
	void createBulkPurchase(BulkPurchase bulkPurchase);
	
	SmSequence purchaseSequence(String rand);
	
	SmSequence commerceTrailSequence(String rand);

}
