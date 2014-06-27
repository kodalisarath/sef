package com.ericsson.raso.sef.core.db.mapper;

import com.ericsson.raso.sef.core.db.model.BulkPurchase;
import com.ericsson.raso.sef.core.db.model.ObsoleteCodeDbSequence;

public interface BulkPurchaseMapper {
	
	void createBulkPurchase(BulkPurchase bulkPurchase);
	
	ObsoleteCodeDbSequence purchaseSequence(String rand);
	
	ObsoleteCodeDbSequence commerceTrailSequence(String rand);

}
