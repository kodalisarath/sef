package com.ericsson.raso.sef.core.db.service;

import java.util.Collection;
import java.util.List;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.model.SubscriberAuditTrial;

public interface SubscriberService {

	boolean isSubscriberExist(String subscriber) throws PersistenceError;
	
	boolean createSubscriber(String nbCorrelator, Subscriber subscriber) throws PersistenceError;

	boolean updateSubscriber(String nbCorrelator, Subscriber subscriber) throws PersistenceError;
	
	boolean deleteSubscriber(String nbCorrelator, Subscriber subscriber) throws PersistenceError;
	
	boolean fulldeleteSubscriber(String nbCorrelator, Subscriber subscriber) throws PersistenceError;
	
	//This below 2 methods were added as part of the refactoring 
	boolean createMeta(String nbCorrelator,String userId,Meta metas) throws PersistenceError;
	boolean updateMeta(String nbCorrelator,String userId,Meta metas) throws PersistenceError;
	List<SubscriberAuditTrial> getSubscriberHistory(String nbCorrelator, String userId, List<String> metaKeys) throws PersistenceError;
	
	Subscriber getSubscriber(String nbCorrelator, String msisdn, List<String> metaKeys) throws PersistenceError;
	
	Subscriber getSubscriberByUserId(String nbCorrelator, String userId) throws PersistenceError;
	
	Subscriber getSubscriber(String nbCorrelator, String msisdn) throws PersistenceError;

	boolean createMetas(String nbCorrelator, String userId, Collection<Meta> metas) throws PersistenceError;

	boolean updateMetas(String nbCorrelator, String userId, Collection<Meta> metas) throws PersistenceError;

	List<Meta> getMetas(String nbCorrelator, String userId, List<String> metaKeys) throws PersistenceError;
	
	
}
