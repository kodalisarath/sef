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
	
	private boolean isAvailable = false;
	private HashMap<String, RangeRouter> routers = new HashMap<String, RangeRouter>();
	private TreeMap<String, Value> values = new TreeMap<String, Value>();
	private TreeSet<Range> ranges = new TreeSet<Range>(); 
	
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
			this.init(section);
		}
		RangeRouter router = this.routers.get(section);
		if (router == null || !router.isAvailable) {
			LOGGER.error("Router Config Section: " + section + " is either missing or badly done.. Disabling this router!!");
			throw new IllegalStateException("Router Config Section: " + section + " is either missing or badly done.. Disabling this router!!");
		} else {
			return router.locateRoute(searched);
		}

	}
	
	private Value locateRoute(long searched) {
		for (Range range: this.ranges) {
			if (range.contains(searched)) {
				String valuePointer = range.getValue();
				return this.values.get(valuePointer);
			}
		}
		return null;
	}

	private synchronized void init(String section) {
		Section configSection = SefCoreServiceResolver.getConfigService().getSection(section);
		if (configSection == null)
			return;
		
		Router routerConfig = configSection.getRouter();
		ArrayList<Range> ranges = routerConfig.getRanges().getRange();
		ArrayList<Value> values = routerConfig.getValues().getValue();
		
		RangeRouter router = new RangeRouter();
		if (ranges != null || values != null) {
			this.isAvailable = this.validateRanges(ranges);
			router.loadRanges(router, ranges);
			router.loadValues(router, values);
		} else {
			LOGGER.error("configuration missing for Router in specified Section: '" + section + "'. Please check config.xml");
			router.isAvailable = false;
		}
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
	
	private void loadRanges(RangeRouter router, ArrayList<Range> ranges) {
		for (Range range: ranges) {
			try {
				router.ranges.add(range);
			} catch (Exception e) {
				LOGGER.error("Failed to load range: " + range + " into Router. Cause: " + e.getMessage(), e);
				router.isAvailable = false;
				router.ranges.clear();
				router.values.clear();
				return;
			}
		}
		LOGGER.debug("Added all Route Element Ranges to the Router...");
	}

	private void loadValues(RangeRouter router, ArrayList<Value> values) {
		for (Value value: values) {
			try {
				router.values.put(value.getId(), value);
			} catch (Exception e) {
				LOGGER.error("Failed to load value: " + value + " into Router. Cause: " + e.getMessage(), e);
				router.isAvailable = false;
				router.ranges.clear();
				router.values.clear();
				return;
			}
		}
		LOGGER.debug("Added all Route Element Values to the Router...");
	}
}
