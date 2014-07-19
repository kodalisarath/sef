package com.ericsson.raso.sef.watergate;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;

public class FloodGate {
	private static final Logger logger = LoggerFactory.getLogger(FloodGate.class);
	private static FloodGate instance = null;
	
	private int highWaterMark = 0;
	private AtomicInteger gatedThreshold = null;
	
	private FloodGate() {
		String configured  = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "wgHwm");
		if (configured == null) {
			logger.error("FloodGate watermark is not configured... Please contact Ericsson!!");
			this.highWaterMark = 150;
		}
		
		try {
			this.highWaterMark = Integer.parseInt(configured);
		} catch (Exception e) {
			logger.error("FloodGate watermark is not configured properly... Please contact Ericsson!!");
			this.highWaterMark = 150;
		}
		
		this.gatedThreshold = new AtomicInteger(0);
	}
	
	public static synchronized FloodGate getInstance() {
		if (instance == null) {
			instance = new FloodGate();
		}
		return instance;
	}
	
	public boolean isAllowed() {
		logger.debug("Ingress time: Current watermark: {}", this.gatedThreshold);
		if (this.gatedThreshold.intValue() < this.highWaterMark) {
			this.gatedThreshold.incrementAndGet();
			return true;
		}
		logger.debug("Ingress: REJECT WATEMARK BREACH: {}", this.gatedThreshold);
		return false;
	}
	
	public void exgress() {
		logger.debug("Exgress time: Current watermark: {}", this.gatedThreshold);
		if (this.gatedThreshold.intValue() > 0)
		this.gatedThreshold.decrementAndGet();
	}

}
