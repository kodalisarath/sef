package com.ericsson.raso.sef.core.db.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.db.model.ObsoleteCodeDbSequence;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.model.SubscriberAuditTrial;
import com.ericsson.raso.sef.core.db.model.SubscriberMeta;

public interface SubscriberMapper {
	Integer isSubscriberExists(String msisdn) throws PersistenceException;
	String getContractState(String userId) throws PersistenceException;
	void createSubscriber(Subscriber subscriber) throws PersistenceException;
	void changeContractStatus(Subscriber subscriber) throws PersistenceException;
	void deleteSubscriber(String userId) throws PersistenceException;
	
	Subscriber getSubscriber(String msisdn) throws PersistenceException;
	
	Subscriber getSubscriberByUserId(String userId) throws PersistenceException;
	
	void updateSubscriber(Subscriber subscriber) throws PersistenceException;
	
	Collection<SubscriberMeta> getSubscriberMetas(Map<String, Object> map) throws PersistenceException;
	
	Collection<SubscriberMeta> getAllSubscriberMetas(String userId) throws PersistenceException;
	
	void insertSubscriberMeta(SubscriberMeta subscriberMeta) throws PersistenceException;

	void updateSubscriberMeta(SubscriberMeta subscriberMeta) throws PersistenceException;

	Collection<SubscriberMeta> getSubscriberMetas(String msisdn) throws PersistenceException;
	
	boolean deleteSubscriberMetas(String userId) throws PersistenceException;

	void commit();
	
	void insertSubscriberHistory(SubscriberAuditTrial subscriberHistory) throws PersistenceException;
	Collection<SubscriberAuditTrial> getSubscriberHistory(Map<String, Object> map) throws PersistenceException;
	ObsoleteCodeDbSequence userSequence(String rand) throws PersistenceException;
	void bulkInsertSubscriberMeta(List<Meta> list) throws PersistenceException;
	void bulkInsertSubscriber(List<Subscriber> list) throws PersistenceException;
	
}
