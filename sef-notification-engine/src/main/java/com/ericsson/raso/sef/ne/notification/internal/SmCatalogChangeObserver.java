package com.ericsson.raso.sef.ne.notification.internal;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.ne.CatalogChangeObserver;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;

public class SmCatalogChangeObserver implements CatalogChangeObserver, EntryListener<String, Date> {
	
	private static transient Logger log = LoggerFactory.getLogger(SmCatalogChangeObserver.class);

	private String PRODUCT_CATALOAG = "product.catalog.store";
	
	public static enum DsmKey {
		OFFER_STORE_KEY,PREMIUM_RESOURCE_KEY,EXTERNAL_RESOURCE_KEY,EXTERNAL_NOTIFICATION_KEY,AUTHORIZATION_KEY;
	}
	
	private boolean started = false;
	
//	private SmCache smCache;
//	
//	public SmCatalogChangeObserver(SmCache smCache) {
//		this.smCache = smCache;
//	}

	@Override
	public void notifyOfferChanges() {
//		Map<String, Date> map = OfferingCatalogContext.getBean(SmCache.class).getMap(PRODUCT_CATALOAG);
//		map.put(DsmKey.OFFER_STORE_KEY.name(), new Date());
	}
	
	@Override
	public void notifyPrtChanges() {
//		Map<String, Date> map = OfferingCatalogContext.getBean(SmCache.class).getMap(PRODUCT_CATALOAG);
//		map.put(DsmKey.PREMIUM_RESOURCE_KEY.name(), new Date());
//		log.info("Catalog Change Observer started successfully.");
	}
	
	@Override
	public void notifyExternalResourceChanges() {
//		Map<String, Date> map = OfferingCatalogContext.getBean(SmCache.class).getMap(PRODUCT_CATALOAG);
//		map.put(DsmKey.EXTERNAL_RESOURCE_KEY.name(), new Date());
	}
	
	@Override
	public void notifyExternalNotificationChanges() {
//		Map<String, Date> map = OfferingCatalogContext.getBean(SmCache.class).getMap(PRODUCT_CATALOAG);
//		map.put(DsmKey.EXTERNAL_NOTIFICATION_KEY.name(), new Date());
	}
	
	@Override
	public void notifyAuthorizationChanges() {
//		Map<String, Date> map = OfferingCatalogContext.getBean(SmCache.class).getMap(PRODUCT_CATALOAG); 
//		map.put(DsmKey.AUTHORIZATION_KEY.name(), new Date());
	}
	
	@Override
	public void entryAdded(EntryEvent<String, Date> event) {
		log.info("Entry added with Key: " + event.getKey());
	}

	@Override
	public void entryRemoved(EntryEvent<String, Date> event) {
		log.info("Entry removed with Key: " + event.getKey());
	}

	@Override
	public void entryUpdated(EntryEvent<String, Date> event) {
//		log.info("Entry updated with Key: " + event.getKey());
//	
//		DsmKey key = DsmKey.valueOf(event.getKey()); 
//		try {
//			switch (key) {
//			case OFFER_STORE_KEY:
//				OfferingCatalogContext.getOfferingCatalog().reload();
//				break;
//			case PREMIUM_RESOURCE_KEY:
//				OfferingCatalogContext.getPremiumResourceCatalog().reload();
//				break;
//			case EXTERNAL_RESOURCE_KEY:
//				OfferingCatalogContext.getExternalResourceCatalog().reload();
//				break;
//			case EXTERNAL_NOTIFICATION_KEY:
//				OfferingCatalogContext.getExternalNotificationTemplateCatalog().reload();
//				break;
//			case AUTHORIZATION_KEY:
//				OfferingCatalogContext.getAuthorizationCatalog().reload();
//			default:
//				break;
//			}
//		} catch (Exception e) {
//			log.error(e.getMessage(),e);
//		}
	}

	@Override
	public void entryEvicted(EntryEvent<String, Date> event) {
		//log.info("Entry evicted with Key: " + event.getKey());
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		
	}

	@Override
	public void start() {
//		IMap<String, Date> map = smCache.getIMap(PRODUCT_CATALOAG);
//		map.addEntryListener(this, true);
//		started = true;
	}

	@Override
	public void stop() {
		
	}

	@Override
	public boolean isRunning() {
		return started;
	}

	@Override
	public int getPhase() {
		return 1;
//		return smCache.getPhase() + 1;
	}
}
