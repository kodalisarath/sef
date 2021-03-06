package com.ericsson.raso.sef.watergate;
import java.io.Serializable;

import com.hazelcast.core.IAtomicLong;

public class ConcurrencyCounter implements Serializable {
	
		private static final long serialVersionUID = 1438803197757624322L;
		
		private IAtomicLong currentConcurrency;
		private long allowedConcurrency;
		private int validityPeriodinMillis;
		private long createdTimeinMillis;
		
		public ConcurrencyCounter(long allowedConcurrency, int validityPeriod, long createdTime, IAtomicLong concurrency) {
			this.allowedConcurrency = allowedConcurrency;
			this.validityPeriodinMillis = validityPeriod;
			this.createdTimeinMillis = createdTime;
			this.currentConcurrency = concurrency;
		}

		public boolean isStillActive() {
			System.out.println("Validity"+ validityPeriodinMillis);
			System.out.println("Elapsed:" + (System.currentTimeMillis() - createdTimeinMillis));
			if(validityPeriodinMillis < (System.currentTimeMillis() - createdTimeinMillis)) 
				return true;
			return false;
		}
		
		public boolean hasLimit() {
			if(currentConcurrency.incrementAndGet() < allowedConcurrency)
				return true;
			return false;
		}
		
		public void reset() {
			currentConcurrency.destroy();
		}
}
