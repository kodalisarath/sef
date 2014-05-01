package com.ericsson.raso.sef.core.db.mapper;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import com.ericsson.raso.sef.core.db.model.ScheduledRequest;
import com.ericsson.raso.sef.core.db.model.ScheduledRequestMeta;
import com.ericsson.raso.sef.core.db.model.SmSequence;

public interface ScheduledRequestMapper {
	
	ScheduledRequest getScheduledRequestByJobId(String jobId);
	
	Collection<ScheduledRequestMeta> getScheduledRequestMetas(long id);
	
	Collection<ScheduledRequest> findIdenticalRequests(ScheduledRequest request);
		
	void upadteScheduledRequestStatus(ScheduledRequest request);
	
	SmSequence scheduledRequestSequence(String rand);
	
	void insertScheduledRequest(ScheduledRequest request);
	
	void insertScheduledRequestMeta(ScheduledRequestMeta meta);
	
	String getJobId(@Param("msisdn") String msisdn,@Param("requestId") String requestId);
	

}
