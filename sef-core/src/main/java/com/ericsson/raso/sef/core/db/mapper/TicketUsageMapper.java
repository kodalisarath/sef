package com.ericsson.raso.sef.core.db.mapper;

import java.util.Collection;
import java.util.Map;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.db.model.TicketUsage;
import com.ericsson.raso.sef.core.db.model.TicketUsageMeta;

public interface TicketUsageMapper {
	
	void createTicketUsage(TicketUsage ticketUsage);
	
	void deleteTicketUsage(String ticketUsageId);
	
	void deleteTicketUsageMeta(String ticketUsageId);
	
	TicketUsage getTicketUsage(String ticketUsageId);
	
	void updateTicketUsage(TicketUsage ticketUsage);
	
	Collection<Meta> getTicketUsageMetas(Map<String, Object> map);
	
	void insertTicketUsageMeta(TicketUsageMeta ticketUsageMeta);

	void updateTicketUsageMeta(TicketUsageMeta ticketusageMeta);

}
