package com.ericsson.raso.sef.core.db.service;

import java.util.Collection;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.model.SubscriberAuditTrial;
import com.ericsson.raso.sef.core.db.model.SubscriberMeta;

public interface SubscriberService {

	void createSubscriber(Subscriber subscriber);
	
	void updateSubscriber(Subscriber subscriber);
	
	void deleteSubscriber(String userId);
	
	void setMetas(String userId, Meta... metas);
	
	Collection<SubscriberMeta> getMetas(String userId, String... metaKeys);
	
	Collection<SubscriberAuditTrial> getSubscriberHistory(String userId, String... metaKeys);
	
	Subscriber getSubscriber(String msisdn, String... metaKeys);
	
	Subscriber getSubscriberByUserId(String userId);
	
	Subscriber getSubscriber(String msisdn);
	
}
