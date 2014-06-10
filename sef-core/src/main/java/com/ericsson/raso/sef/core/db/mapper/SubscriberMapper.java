package com.ericsson.raso.sef.core.db.mapper;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.db.model.SmSequence;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.model.SubscriberAuditTrial;
import com.ericsson.raso.sef.core.db.model.SubscriberMeta;

public interface SubscriberMapper {
	int isSubscriberExists(String msisdn);
	
	void createSubscriber(Subscriber subscriber);
	
	void deleteSubscriber(String userId);
	
	Subscriber getSubscriber(String msisdn);
	
	Subscriber getSubscriberByUserId(String userId);
	
	void updateSubscriber(Subscriber subscriber);
	
	Collection<Meta> getSubscriberMetas(Map<String, Object> map);
	
	Collection<Meta> getAllSubscriberMetas(String userId);
	
	void insertSubscriberMeta(SubscriberMeta subscriberMeta);

	void updateSubscriberMeta(SubscriberMeta subscriberMeta);

	void insertSubscriberHistory(SubscriberAuditTrial subscriberHistory);
	
	Collection<SubscriberAuditTrial> getSubscriberHistory(Map<String, Object> map);
	
	SmSequence userSequence(String rand);
	
	void bulkInsertSubscriberMeta(List<Meta> list);
	
	void bulkInsertSubscriber(List<Subscriber> list);

}
