package com.ericsson.raso.sef.core.db.mapper;

import java.util.Collection;

import com.ericsson.raso.sef.core.db.model.CommerceTrail;
import com.ericsson.raso.sef.core.db.model.SmSequence;


public interface CommerceTrailMapper {
	
	void createCommerceTrail(CommerceTrail commerceTrail);
	
	void deleteCommerceTrail(String eventId);
	
	CommerceTrail getCommerceTrail(String eventId);
	
	Collection<CommerceTrail> getCommerceTrailByUserId(String userId);
	
	void updateCommerceTrail(CommerceTrail commerceTrail);
	
	SmSequence commerceTrailSequence(String rand);
	
}
