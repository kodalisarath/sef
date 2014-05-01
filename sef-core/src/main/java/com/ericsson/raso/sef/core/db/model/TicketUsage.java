package com.ericsson.raso.sef.core.db.model;

import java.util.List;

import org.joda.time.DateTime;

public class TicketUsage {

	private String eventId;
	private String ticketId;
	private String descriptionEn;
	private DateTime created;
	private long consumedUnits;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}	

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public long getConsumedUnits() {
		return consumedUnits;
	}

	public void setConsumedUnits(long consumedUnits) {
		this.consumedUnits = consumedUnits;
	}

}
