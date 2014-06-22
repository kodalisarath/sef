package com.ericsson.raso.sef.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.config.Config;
import com.ericsson.raso.sef.core.config.Range;
import com.ericsson.raso.sef.core.config.Router;
import com.ericsson.raso.sef.core.config.Section;
import com.ericsson.raso.sef.core.config.Value;

public class RangeRouter {
	private static final Logger LOGGER = LoggerFactory.getLogger(RangeRouter.class);
	
	private static RangeRouter instance = null;
	
	private HashMap<String, AtomicRouter> routers = new HashMap<String, AtomicRouter>();
	
	private RangeRouter() { }
	
	public static synchronized RangeRouter getInstance() {
		if (instance == null)
			instance = new RangeRouter();
		return instance;
	}
	
	public Value getRoute(String section, long searched) {
		if (section == null) {
			LOGGER.debug("Section was null!!");
			return null;
		}
		
		if (this.routers.get(section) == null) {
			LOGGER.debug("No routers found for section: " + section + ". Will try to initialize now...");
			this.init(section);
		}
		
		AtomicRouter router = this.routers.get(section);
		if (router == null || !router.isAvailable()) {
			LOGGER.error("Router Config Section: " + section + " is either missing or badly done.. Disabling this router!!");
			throw new IllegalStateException("Router Config Section: " + section + " is either missing or badly done.. Disabling this router!!");
		} else {
			LOGGER.debug("Found a router for section: " + section);
			return router.locateRoute(searched);
		}

	}
	
	
	private synchronized void init(String section) {
		Section configSection = SefCoreServiceResolver.getConfigService().getSection(section);
		if (configSection == null) {
			LOGGER.debug("Section requested: '" + section + "' was not found in the config!!");
			return;
		}
		
		Router routerConfig = configSection.getRouter();
		ArrayList<Range> ranges = routerConfig.getRanges().getRange();
		ArrayList<Value> values = routerConfig.getValues().getValue();
		LOGGER.debug("Located config for ranges:" + ranges + " & values: " + values);
		
		AtomicRouter router = new AtomicRouter();
		if (ranges != null && values != null) {
			boolean isAvailable = this.validateRanges(ranges);
			if (isAvailable) {
				router.loadRanges(ranges);
				router.loadValues(values);
				router.setAvailable(isAvailable);
			}
			
		} else {
			LOGGER.error("configuration missing/bad for Router in specified Section: '" + section + "'. Please check config.xml");
			router.setAvailable(false);
		}
		LOGGER.debug("Adding new router (" + router + ") to section: " + section);
		this.routers.put(section, router);
	}

	
	private boolean validateRanges(ArrayList<Range> ranges) {
		for (Range r1: ranges) {
			for (Range r2: ranges) {
				// CASE 1: Range 1 is superset of Range 2
				if ( (r1.getStart() < r2.getStart()) && (r1.getEnd() > r2.getEnd()) ) {
					LOGGER.error("BAD CONFIGURAION: Range1 superset of Range2. Check Range1: " + r1 + " and Range2" + r2);
					return false;
				}
				
				// CASE 2: Range 1 is intruding into Range 2
				if ( (r1.getStart() > r2.getStart()) && (r1.getStart() < r2.getEnd()) && (r1.getEnd() > r2.getEnd()) ) {
					LOGGER.error("BAD CONFIGURAION: Range1 intrudes into Range 2. Check Range1: " + r1 + " and Range2" + r2);
					return false;
				}
				
				// CASE 3: Range 1 is extending into Range 2
				if ( (r1.getStart() < r2.getStart()) && (r2.getStart() < r1.getEnd()) && (r1.getEnd() < r2.getEnd()) ) {
					LOGGER.error("BAD CONFIGURAION: Range1 extends into Range2. Check Range1: " + r1 + " and Range2" + r2);
					return false;
				}
				
				// CASE 4: Range 1 is subset Range 2
				if ( (r1.getStart() > r2.getStart()) && (r1.getEnd() < r2.getEnd()) ) {
					LOGGER.error("BAD CONFIGURAION: Range1 subset of Range2. Check Range1: " + r1 + " and Range2" + r2);
					return false;
				}
			}
		}
		
		LOGGER.debug("Ranges validated successfully...");
		return true;
	}


	@Override
	public String toString() {
		return "RangeRouter [routers=" + routers + "]";
	}
	
	

	

	
}
