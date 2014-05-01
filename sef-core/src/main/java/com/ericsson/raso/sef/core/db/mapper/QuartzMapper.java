package com.ericsson.raso.sef.core.db.mapper;

import java.util.List;

import com.ericsson.raso.sef.core.db.model.JobDetail;

public interface QuartzMapper {
	
	void bulkInsertJobDetails(List<JobDetail> jobDetails);
	
}
