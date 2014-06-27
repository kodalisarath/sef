package com.ericsson.raso.sef.core.db.mapper.smart;

import java.util.Collection;

import org.apache.ibatis.exceptions.PersistenceException;

import com.ericsson.raso.sef.core.db.model.smart.CallingCircle;
import com.ericsson.raso.sef.core.db.service.PersistenceError;

public interface CallingCircleMapper {
	
	void createCallingCircleAndMembership(CallingCircle callingCircle) throws PersistenceException;
	
	CallingCircle fetchCallingCircle(CallingCircle queryCallingCircle) throws PersistenceException;
	
	void deleteCallingCircle(CallingCircle deleteCallingCircle) throws PersistenceException;

	Collection<CallingCircle> fetchCallingCirclesWithMembershipFor(String subscriberId) throws PersistenceException;
	
	void deleteCallingCircleMembership(CallingCircle deleteCallingCircle) throws PersistenceException;
	
	int fetchCallingCircleMemberCountForOwner(String nbCorrelator, CallingCircle query) throws PersistenceException;
	
	int fetchCallingCircleMemberCountForMember(String nbCorrelator, CallingCircle query) throws PersistenceException;
	
	Collection<CallingCircle> fetchCallingCircleMembersForOwner(String nbCorrelator, CallingCircle query) throws PersistenceException;
	
	Collection<String> fetchAllCallingCircleMembers(String nbCorrelator, CallingCircle query) throws PersistenceError;

	Collection<String> fetchCallingCircleMembersOnly(String nbCorrelator, CallingCircle query) throws PersistenceError;

	
}
