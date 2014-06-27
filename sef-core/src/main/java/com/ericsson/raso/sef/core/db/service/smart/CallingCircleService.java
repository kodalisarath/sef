package com.ericsson.raso.sef.core.db.service.smart;

import java.util.Collection;

import org.apache.ibatis.exceptions.PersistenceException;

import com.ericsson.raso.sef.core.db.model.smart.CallingCircle;
import com.ericsson.raso.sef.core.db.service.PersistenceError;

public interface CallingCircleService {

	
	void createCallingCircleMemberMapping(String nbCorrelator, CallingCircle circle) throws PersistenceError;
	
	int fetchCallingCircleMemberCountForOwner(String nbCorrelator, CallingCircle query) throws PersistenceError;
	
	int fetchCallingCircleMemberCountForMember(String nbCorrelator, CallingCircle query) throws PersistenceError;
	
	CallingCircle fetchCallingCircleMemberMapping(String nbCorrelator, CallingCircle query) throws PersistenceError;

	void deleteCallingCircle(String nbCorrelator, CallingCircle query) throws PersistenceError;

	void deleteCallingCircleMemberMapping(String nbCorrelator, CallingCircle delete) throws PersistenceError;

	Collection<CallingCircle> fetchCallingCircleMembersForOwner(String nbCorrelator, CallingCircle query) throws PersistenceError;

	Collection<String> fetchAllCallingCircleMembers(String nbCorrelator, CallingCircle query) throws PersistenceError;

	Collection<String> fetchCallingCircleMembersOnly(String nbCorrelator, CallingCircle query) throws PersistenceError;

	
}
