package com.ericsson.raso.sef.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.config.Range;
import com.ericsson.raso.sef.core.config.Value;

public class AtomicRouter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AtomicRouter.class);
	
	private boolean isAvailable = false;
	private ConcurrentHashMap<String, Value> values = new ConcurrentHashMap<String, Value>();
	private HashSet<Range> ranges = new HashSet<Range>();
	
	protected Value locateRoute(long searched) {
		for (Range range: this.ranges) {
			if (range.contains(searched)) {
				LOGGER.debug("Found the range for: " + searched);
				String valuePointer = range.getValue();
				LOGGER.debug("valuePointer: " + valuePointer);
				Value value = this.values.get(valuePointer);
				LOGGER.debug("value returned: " + value);
				
				return value;
			}
		}
		return null;
	}

	protected void loadRanges(ArrayList<Range> ranges) {
		for (Range range: ranges) {
			try {
				this.ranges.add(range);
				LOGGER.debug("Added Route Element: " + range);
			} catch (Exception e) {
				LOGGER.error("Failed to load range: " + range + " into Router. Cause: " + e.getMessage(), e);
				this.isAvailable = false;
				this.ranges.clear();
				this.values.clear();
				return;
			}
		}
		LOGGER.debug("Added all Route Element Ranges to the Router...");
	}
	
	
	protected void loadValues(ArrayList<Value> values) {
		for (Value value: values) {
			try {
				this.values.put(value.getId(), value);
				LOGGER.debug("Added Value Element: " + value);
			} catch (Exception e) {
				LOGGER.error("Failed to load value: " + value + " into Router. Cause: " + e.getMessage(), e);
				this.isAvailable = false;
				this.ranges.clear();
				this.values.clear();
				return;
			}
		}
		LOGGER.debug("Added all Route Element Values to the Router...");
	}
	
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	@Override
	public String toString() {
		return "Router [isAvailable=" + isAvailable + ", values=" + values + ", ranges=" + ranges + "]";
	} 
	
	
	
}
