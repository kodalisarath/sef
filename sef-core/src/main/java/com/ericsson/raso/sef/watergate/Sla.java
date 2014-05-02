package com.ericsson.raso.sef.watergate;

import java.io.Serializable;

public class Sla implements Serializable {
	
	private static final long serialVersionUID = -4718087276305672083L;
		private int capacity;
		private TimeUnit slaUnit;
		
		public int getCapacity() {
			return capacity;
		}
		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}
		public TimeUnit getSlaUnit() {
			return slaUnit;
		}
		public void setSlaUnit(TimeUnit slaUnit) {
			this.slaUnit = slaUnit;
		}
}
