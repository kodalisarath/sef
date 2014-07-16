package com.ericsson.raso.sef.core.db.service.internal;

import java.util.Collection;

import com.ericsson.raso.sef.core.db.mapper.ScheduledRequestMapper;
import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestMeta;
import com.ericsson.raso.sef.core.db.service.ScheduleRequestService;

public class ScheduleRequestServiceImpl implements ScheduleRequestService {

	
	private ScheduledRequestMapper scheduledRequestMapper;
	
	public void setScheduledRequestMapper(ScheduledRequestMapper scheduledRequestMapper){
		this.scheduledRequestMapper=scheduledRequestMapper;
	}
	
	@Override
	public ScheduledRequest getScheduledRequestByJobId(String jobId) {
		return scheduledRequestMapper.getScheduledRequestByJobId(jobId);
	}

	@Override
	public Collection<ScheduledRequestMeta> getScheduledRequestMetas(long id) {
		return scheduledRequestMapper.getScheduledRequestMetas(id);
	}

	@Override
	public Collection<ScheduledRequest> findIdenticalRequests(
			ScheduledRequest request) {
		return scheduledRequestMapper.findIdenticalRequests(request);
	}

	@Override
	public void upadteScheduledRequestStatus(ScheduledRequest request) {
		scheduledRequestMapper.upadteScheduledRequestStatus(request);
	}

	/*@Override
	public ObsoleteCodeDbSequence scheduledRequestSequence(String rand) {
		return scheduledRequestMapper.scheduledRequestSequence(rand);
	}*/

	@Override
	public void insertScheduledRequest(ScheduledRequest request) {
		scheduledRequestMapper.insertScheduledRequest(request);
	}

	@Override
	public void insertScheduledRequestMeta(ScheduledRequestMeta meta) {
		scheduledRequestMapper.insertScheduledRequestMeta(meta);

	}

	@Override
	public String getJobId(String msisdn, String requestId) {
		return scheduledRequestMapper.getJobId(msisdn, requestId);
	}
	
	@Override
	public Collection<String> getJobIdByOfferId(String msisdn, String offerId,String lifeCycleEvent) {
		return scheduledRequestMapper.getJobIdByOfferId(msisdn, offerId,lifeCycleEvent);
	}
	
}
