package com.ericsson.raso.sef.core.db.service;

import java.util.List;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.model.SubscriberAuditTrial;

public interface SubscriberService {

	boolean createSubscriber(String nbCorrelator, Subscriber subscriber) throws PersistenceError;

	boolean updateSubscriber(String nbCorrelator, Subscriber subscriber) throws PersistenceError;
	
	boolean deleteSubscriber(String nbCorrelator, String userId) throws PersistenceError;
	
	boolean setMetas(String userId, List<Meta> metas) throws PersistenceError;
	
	List<Meta> getMetas(String userId, List<String> metaKeys) throws PersistenceError;
	
	List<SubscriberAuditTrial> getSubscriberHistory(String userId, List<String> metaKeys) throws PersistenceError;
	
	Subscriber getSubscriber(String msisdn, List<String> metaKeys) throws PersistenceError;
	
	Subscriber getSubscriberByUserId(String userId) throws PersistenceError;
	
	Subscriber getSubscriber(String msisdn) throws PersistenceError;

	
}
