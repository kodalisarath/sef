package com.ericsson.raso.sef.core.db.model;

import com.ericsson.raso.sef.core.Meta;

public class TicketUsageMeta extends Meta{
	private String ticketUsageId;

	public String getTicketUsageId() {
		return ticketUsageId;
	}

	public void setTicketUsageId(String ticketUsageId) {
		this.ticketUsageId = ticketUsageId;
	}

}