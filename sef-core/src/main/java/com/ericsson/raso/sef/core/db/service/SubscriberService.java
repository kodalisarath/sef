package com.ericsson.raso.sef.core.db.service;

import java.util.Collection;
import java.util.List;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.model.SubscriberAuditTrial;

public interface SubscriberService {

	void createSubscriber(Subscriber subscriber);
	
	void updateSubscriber(Subscriber subscriber);
	
	void deleteSubscriber(String userId);
	
	void setMetas(String userId, List<Meta> metas);
	
	Collection<Meta> getMetas(String userId, String... metaKeys);
	
	Collection<SubscriberAuditTrial> getSubscriberHistory(String userId, String... metaKeys);
	
	Subscriber getSubscriber(String msisdn, String... metaKeys);
	
	Subscriber getSubscriberByUserId(String userId);
	
	Subscriber getSubscriber(String msisdn);
	
}
