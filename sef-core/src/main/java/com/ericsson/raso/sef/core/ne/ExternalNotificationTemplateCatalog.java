package com.ericsson.raso.sef.core.ne;

import java.util.Collection;

import com.ericsson.raso.sef.core.SmException;

public interface ExternalNotificationTemplateCatalog {

	void reload() throws Exception;

	ExternalNotifcationEvent getEventById(String eventId);

	void storeEvent(ExternalNotifcationEvent event) throws SmException;

	Collection<ExternalNotifcationEvent> getAllEvents();

	void remove(String eventId) throws SmException;

}
