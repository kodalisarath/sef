package com.ericsson.raso.sef.ne.notification.internal;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;

import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.ne.CatalogChangeObserver;
import com.ericsson.raso.sef.core.ne.ExternalNotifcationEvent;
import com.ericsson.raso.sef.core.ne.ExternalNotificationTemplateCatalog;
import com.ericsson.raso.sef.ne.NotificationEngineServiceResolver;


public class SmExternalNotificationTemplateCatalog implements ExternalNotificationTemplateCatalog {

	private Map<String, ExternalNotifcationEvent> map = new HashMap<String, ExternalNotifcationEvent>();

	public SmExternalNotificationTemplateCatalog() throws Exception {
		map = deserialize();
	}

	@Override
	public void reload() throws Exception {
		map = deserialize();
	}
	
	@Override
	public ExternalNotifcationEvent getEventById(String eventId) {
		return map.get(eventId);
	}

	@Override
	public Collection<ExternalNotifcationEvent> getAllEvents() {
		return map.values();
	}

	@Override
	public void storeEvent(ExternalNotifcationEvent event) throws SmException {
		map.put(event.getEventId(), event);
		try {
			serialize();
		} catch (Exception e) {
			throw new SmException(e);
		}
	}

	private void serialize() throws Exception {
		String smHome = System.getenv("CONFIG_HOME");
		FileSystemResource resource = new FileSystemResource(smHome + File.separator + "external-notifications.ccem");
		if(!resource.exists()) {
			resource.getFile().createNewFile();
		}
		ObjectOutputStream stream = new ObjectOutputStream(resource.getOutputStream());
		stream.writeObject(map);
		stream.flush();
		stream.close();
		NotificationEngineServiceResolver.getBean(CatalogChangeObserver.class).notifyExternalNotificationChanges();
		//OfferingCatalogContext.getCatalogChangeObserver().notifyExternalNotificationChanges();
	}

	@SuppressWarnings("unchecked")
	private Map<String, ExternalNotifcationEvent> deserialize() throws Exception {
		String smHome = System.getenv("CONFIG_HOME");
		FileSystemResource resource = new FileSystemResource(smHome + File.separator + "external-notifications.ccem");
		if(resource.exists())  {
			ObjectInputStream stream = new ObjectInputStream(resource.getInputStream());
			Map<String, ExternalNotifcationEvent> externalNotifications = (Map<String, ExternalNotifcationEvent>) stream.readObject();
			stream.close();
			return externalNotifications;
		}
		return new HashMap<String, ExternalNotifcationEvent>();
	}

	@Override
	public void remove(String eventId) throws SmException {
		map.remove(eventId);
		try {
			serialize();
		} catch (Exception e) {
			throw new SmException(e);
		}
	}
}
