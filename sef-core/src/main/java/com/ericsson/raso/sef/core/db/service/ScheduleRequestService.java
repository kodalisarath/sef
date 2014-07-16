package com.ericsson.raso.sef.core.db.service;

import java.util.Collection;

import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestMeta;

public interface ScheduleRequestService {

	ScheduledRequest getScheduledRequestByJobId(String jobId);

	Collection<ScheduledRequestMeta> getScheduledRequestMetas(long id);

	Collection<ScheduledRequest> findIdenticalRequests(ScheduledRequest request);

	void upadteScheduledRequestStatus(ScheduledRequest request);

	//Long scheduledRequestSequence(String rand);

	void insertScheduledRequest(ScheduledRequest request);

	void insertScheduledRequestMeta(ScheduledRequestMeta meta);

	String getJobId(String msisdn, String requestId);
	
	Collection<String> getJobIdByOfferId(String msisdn, String offerId,String lifeCycleEvent);

}
