package com.ericsson.raso.sef.notification.workflows;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallingCircleEdrProcessor {

	private Logger log = LoggerFactory.getLogger("callingCircle");

	public void printEdr(CallingCircleEdr circleEdr) {

		try {
			Printer printer = new Printer();
			
			printer.edrStr = new StringBuilder();
			printer.edrStr.append("Timestamp=" + new Date().toString() + ",");
			printer.edrStr.append("CallingParty=" + circleEdr.getCallingParty() + ",");
			printer.edrStr.append("CalledParty=" + circleEdr.getCalledParty() + ",");
			printer.edrStr.append("PromoName=" + circleEdr.getPromoName() + ",");

			if (circleEdr.getRelationship() != null) {
				printer.edrStr.append("Relationship=" + circleEdr.getRelationship() + ",");
			}

			if (circleEdr.getFaFIndicatorValue() != null) {
				printer.edrStr.append("FaFIndicator=" + circleEdr.getFaFIndicatorValue() + ",");
			}
			if (circleEdr.getExpiry() != null) {
				printer.edrStr.append("Expiry=" + circleEdr.getExpiry() + ",");
			}

			printer.edrStr.append("Status=" + circleEdr.getStatus());

			if (circleEdr.getReasonForInvalidCallAttempt() != null) {
				printer.edrStr.append(", ReasonForInvalidCallAttempt=" + circleEdr.getReasonForInvalidCallAttempt());
			}
			
			log.info(printer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class Printer {
		StringBuilder edrStr;
		@Override
		public String toString() {
			return edrStr!=null?edrStr.toString():"";
		}
	}
}