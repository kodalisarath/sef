package com.ericsson.raso.sef.core.db.service;

import java.util.Collection;

import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestMeta;
import com.ericsson.raso.sef.core.db.model.ObsoleteCodeDbSequence;

public interface ScheduleRequestService {

	ScheduledRequest getScheduledRequestByJobId(String jobId);

	Collection<ScheduledRequestMeta> getScheduledRequestMetas(long id);

	Collection<ScheduledRequest> findIdenticalRequests(ScheduledRequest request);

	void upadteScheduledRequestStatus(ScheduledRequest request);

	ObsoleteCodeDbSequence scheduledRequestSequence(String rand);

	void insertScheduledRequest(ScheduledRequest request);

	void insertScheduledRequestMeta(ScheduledRequestMeta meta);

	String getJobId(String msisdn, String requestId);
	
	Collection<ScheduledRequest> getJobIdByOfferId(String msisdn, String offerId,String lifeCycleEvent);

}
