package com.ericsson.raso.sef.core.db.mapper;

import java.util.Collection;
import java.util.Map;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.db.model.Ticket;
import com.ericsson.raso.sef.core.db.model.TicketMeta;

public interface TicketMapper {
	
	void createTicket(Ticket ticket);
	
	void deleteTicket(String ticketId);
	
	void deleteTicketMeta(String ticketId);
	
	Ticket getTicket(String ticketId);
	
	void updateTicket(Ticket ticket);
	
	Collection<Meta> getTicketMetas(Map<String, Object> map);
	
	void insertTicketMeta(TicketMeta ticketMeta);

	void updateTicketMeta(TicketMeta ticketMeta);
	
	Collection<Ticket> getTicketByPurchaseId(String purchaseId );
	
}
