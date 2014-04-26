package com.ericsson.raso.sef.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

@Plugin(name = "RangeFilter", category = "Core", elementType = "filter", printObject = true)
public class RangeFilter extends AbstractFilter {

	private final Level lowerBound;
	private final Level upperBound;
	
	protected RangeFilter(Level minLevel, Level maxLevel, Result onMatch, Result onMismatch) {
		super(onMatch, onMismatch);
		this.lowerBound = minLevel;
		this.upperBound = maxLevel;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
		if (level.intLevel() < this.lowerBound.intLevel() || level.intLevel() > this.upperBound.intLevel())
			return onMismatch;
		return onMatch;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
		if (level.intLevel() < this.lowerBound.intLevel() || level.intLevel() > this.upperBound.intLevel())
			return onMismatch;
		return onMatch;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
		if (level.intLevel() < this.lowerBound.intLevel() || level.intLevel() > this.upperBound.intLevel())
			return onMismatch;
		return onMatch;
	}

	@Override
	public Result filter(LogEvent event) {
		if (event.getLevel().intLevel() < this.lowerBound.intLevel() || event.getLevel().intLevel() > this.upperBound.intLevel())
			return onMismatch;
		return onMatch;
	}
	
	/**
     * Create a ThresholdFilter.
     * @param loggerLevel The log Level.
     * @param match The action to take on a match.
     * @param mismatch The action to take on a mismatch.
     * @return The created ThresholdFilter.
     */
    @PluginFactory
    public static RangeFilter createFilter(@PluginAttribute("minLevel") String minLevel,
    											@PluginAttribute("maxLevel") String maxLevel,
    											@PluginAttribute("onMatch") String match,
    											@PluginAttribute("onMismatch") String mismatch) {
        Level low = (minLevel == null) ? Level.INFO : Level.toLevel(minLevel.toUpperCase());
        Level high = (maxLevel == null) ? Level.INFO : Level.toLevel(maxLevel.toUpperCase());
        Result onMatch = (match == null) ? Result.NEUTRAL : Result.valueOf(match.toUpperCase());
        Result onMismatch = mismatch == null ? Result.DENY : Result.valueOf(mismatch.toUpperCase());
 
        return new RangeFilter(low, high, onMatch, onMismatch);
    }

	@Override
	public String toString() {
		return "RangeFilter [lowerBound=" + lowerBound + ", upperBound=" + upperBound + ", onMatch=" + onMatch
				+ ", onMismatch=" + onMismatch + "]";
	}

	
}
