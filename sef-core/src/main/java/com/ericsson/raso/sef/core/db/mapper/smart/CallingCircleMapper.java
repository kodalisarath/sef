package com.ericsson.raso.sef.core.db.mapper.smart;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;

import com.ericsson.raso.sef.core.db.model.smart.CallingCircle;
import com.ericsson.raso.sef.core.db.service.PersistenceError;

public interface CallingCircleMapper {
	
	void createCallingCircleAndMembership(CallingCircle callingCircle) throws PersistenceException;
	
	CallingCircle fetchCallingCircle(@Param("owner") String owner, @Param("prodcatOffer") String prodcatOffer) throws PersistenceException;
	
	void deleteCallingCircle(@Param("owner") String owner, 
			@Param("prodcatOffer") String prodcatOffer) throws PersistenceException;

	Collection<CallingCircle> fetchCallingCirclesWithMembershipFor(String subscriberId) throws PersistenceException;
	
	void deleteCallingCircleMembership(@Param("owner") String owner, 
										@Param("prodcatOffer") String prodcatOffer) throws PersistenceException;
	
	int fetchCallingCircleMemberCountForOwner(String nbCorrelator, @Param("owner") String owner, 
																	@Param("prodcatOffer") String prodcatOffer, 
																	@Param("memberB") String memberB) throws PersistenceException;
	
	int fetchCallingCircleMemberCountForMember(String nbCorrelator, @Param("owner") String owner,
																	@Param("prodcatOffer") String prodcatOffer,
																	@Param("memberB") String memberB) throws PersistenceException;
	
	Collection<CallingCircle> fetchCallingCircleMembersForOwner(String nbCorrelator, CallingCircle query) throws PersistenceException;
	
	Collection<String> fetchAllCallingCircleMembers(String nbCorrelator, @Param("owner") String owner, 
				@Param("prodcatOffer") String prodcatOffer) throws PersistenceError;

	Collection<String> fetchCallingCircleMembersOnly(String nbCorrelator, @Param("owner") String owner, 
			@Param("prodcatOffer") String prodcatOffer) throws PersistenceError;

	
}
