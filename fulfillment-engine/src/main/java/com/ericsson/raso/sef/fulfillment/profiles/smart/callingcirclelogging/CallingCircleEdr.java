package com.ericsson.raso.sef.fulfillment.profiles.smart.callingcirclelogging;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.db.model.smart.CallingCircle;

public class CallingCircleEdr {
	private static final Logger edrCallingCircle = LoggerFactory.getLogger(CallingCircleEdr.class);

	
	public  static void generateEdr(String status, String prodcatOffer, long expiry, CallingCircle circle, String fafIndicator, String reason) {
		SimpleDateFormat tsFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		Date timestamp = new Date();
		SimpleDateFormat expiryFormat = new SimpleDateFormat("yyy MMM dd HH:mm:ss zzz yyyy");
		Date expiryDate =  (expiry==-1)?new Date():new Date(expiry);
		
		
		// for successful add
		///-- Timestamp=Fri Feb 21 13:07:33 PHT 2014,CallingParty=639465610684, CalledParty=639098107385, PromoName=K15, Relationship=SPONSER_MEMBER, FaFIndicator=200, Expiry=2014-04-01 21:59:25, Status=ADD
		// for successful delete
		///-- Timestamp=Fri Feb 21 13:07:33 PHT 2014,CallingParty=639465610684, CalledParty=639098107385, PromoName=K15, Relationship=SPONSER_MEMBER, FaFIndicator=200, Expiry=2014-04-01 21:59:25, Status=DELETE
		// for all failure
		///--Timestamp=Fri Feb 21 13:07:33 PHT 2014,CallingParty=639777000011, CalledParty=639098107385, PromoName=K15, Status=FAILED, ReasonForInvalidCallAttempt=SUBSCRIBER  639777000011 NOT FOUND
		///--Timestamp=Fri Feb 21 13:06:47 PHT 2014,CallingParty=639469649175,   CalledParty=639098107385, PromoName=K15, Status=FAILED, ReasonForInvalidCallAttempt=No valid purchase found for Katok15Promo for 639469649175
		edrCallingCircle.error("Timestamp=" + tsFormat.format(timestamp)
								+ ",CallingParty=" +  circle.getMemberA()       //this.subscriberId
								+ ",CalledParty=" + circle.getMemberB()
								+ ",PromoName=" + prodcatOffer
								+ ",Relationship=" + circle.getRelationship()
								+ ",FafIndicator=" + fafIndicator
								+ ",Expiry=" + expiryFormat.format(expiryDate)
								+ ",Status=" + status
								+ ((reason==null)?"":",ReasonForInvalidCallAttempt=" + reason));
		
		

	}
	

}
