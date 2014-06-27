package com.ericsson.raso.sef.notification.workflows.callingcircle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.notification.workflows.CallingCircleEdr;
import com.ericsson.raso.sef.notification.workflows.ErrorCode;
import com.ericsson.raso.sef.notification.workflows.promo.CallingCircleType;
import com.ericsson.raso.sef.notification.workflows.promo.Promo;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.config.Section;
import com.ericsson.raso.sef.core.db.model.CallingCircle;
import com.ericsson.raso.sef.core.db.model.CallingCircleRelation;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.SubscriberService;

public class CallingCircleUtil {
	private static Logger log = LoggerFactory.getLogger(CallingCircleUtil.class);
	public static Promo getCallingCirclePromo(String productId) {
		IConfig config = SefCoreServiceResolver.getConfigService();
		 
		return buildPromo(config, String.format("callingCircleCfg-%s", productId));
	}
	
	private final static Promo buildPromo(IConfig config, String sectionName){
		/**
		 * Example config
		 * <section id="callingCircleCfg-K10"
					description="Caling Circle Configuration for K10">
					<property key="id" value="K10" />
					<property key="successEventId" value="100100362015" />
					<property key="associatedPromo" value="Katok10Promo" />
					<property key="openCloudRegistration" value="false" />
					<property key="ucip" value="UPADTEOFFER:2019,PCODE:PK10" />
					<property key="type" value="TWO_WAY" />
					<property key="smRelationShip" value="100" />
					<property key="msRelationShip" value="100" />
			</section>	
		 */
		Section section = config.getSection(sectionName);
		if (section == null)
			return null;

		Promo promo = new Promo();
		promo.setAssociatedPromo(config.getValue(sectionName, "associatedPromo"));
		promo.setId(config.getValue(sectionName, "associatedPromo"));
		promo.setSuccessEventId(config.getValue(sectionName, "successEventId"));
		
		if ( "TWO_WAY".equals(config.getValue(sectionName, "type"))){
			promo.setType(CallingCircleType.TWO_WAY);
		} else if ("ONE_WAY".equals(config.getValue(sectionName, "type"))){
			promo.setType(CallingCircleType.ONE_WAY);
		}
		
		promo.setUcip(config.getValue(sectionName, "ucip"));
		
		if ( config.getValue(sectionName, "mmRelatioShip")!= null)
			promo.setMmRelationShip(Integer.parseInt(config.getValue(sectionName, "mmRelatioShip")));
		if ( config.getValue(sectionName, "msRelatioShip")!= null)
			promo.setMsRelationShip(Integer.parseInt(config.getValue(sectionName, "msRelatioShip")));
		if ( config.getValue(sectionName, "openCloudRegistration")!= null)
			promo.setOpenCloudRegistration(Boolean.parseBoolean(config.getValue(sectionName, "openCloudRegistration")));
		if ( config.getValue(sectionName, "smRelatioShip")!= null)
			promo.setSmRelationShip(Integer.parseInt(config.getValue(sectionName, "smRelatioShip")));
		
		
		return promo;
	}
	
	public static Integer getIndicator(String productId, CallingCircleRelation relation) {
		Promo promo = getCallingCirclePromo(productId);
		Integer indicator = null;
		switch (relation) {
		case MEMBER_MEMBER:
			indicator = promo.getMmRelationShip();
			break;
		case MEMBER_SPONSER:
			indicator = promo.getMsRelationShip();
			break;
		case SPONSER_MEMBER:
			indicator = promo.getSmRelationShip();
			break;
		default:
			break;
		}
		return indicator;
	}
	
	public static  CallingCircleEdr getCallingCircleEdr(String aparty, String bparty, String promoName, String status, String reason) {
		CallingCircleEdr callingCircleEdr = new CallingCircleEdr();
		callingCircleEdr.setCalledParty(bparty);
		callingCircleEdr.setCallingParty(aparty);
		callingCircleEdr.setPromoName(promoName);
		callingCircleEdr.setReasonForInvalidCallAttempt(reason);
		callingCircleEdr.setStatus(status);
		return callingCircleEdr;

	}

	public static  CallingCircleEdr getCallingCircleEdr(CallingCircle callingCircle,String status) throws SmException {
		CallingCircleEdr callingCircleEdr = new CallingCircleEdr();
		callingCircleEdr.setCalledParty(callingCircle.getBpartyMsisdn());
		callingCircleEdr.setCallingParty(callingCircle.getApartyMsisdn());
		callingCircleEdr.setPromoName(callingCircle.getiLProductId());
		callingCircleEdr.setStatus(status);
		callingCircleEdr.setExpiry(callingCircle.getExpiryTime().toDate());
		callingCircleEdr.setRelationship(callingCircle.getRelationship());
		callingCircleEdr.setFaFIndicatorValue(CallingCircleUtil.getIndicator(callingCircle.getiLProductId(),callingCircle.getRelationship()));
		return callingCircleEdr;
	}
	
	public static Subscriber getSubscriberByMsisdn(String msisdn) throws SmException {
		
		SubscriberService subscriberService = SefCoreServiceResolver.getSusbcriberStore();
		//@TODO fill in nbCorrelator
		try {
			log.warn("Please verify the correct value for nbCorrelator value, current value is pasaload:money");
			Subscriber subscriber = subscriberService.getSubscriber("pasaload:money",msisdn);
			if(subscriber==null){
				throw new SmException(ErrorCode.SUBSCRIBER_NOT_FOUND);
			} 
			
			return subscriber;
		} catch (PersistenceError e) {
			log.error(e.getMessage(),e);
			throw new SmException(e);
		}
		 
	}
	
	public static Subscriber getSubscriberByUserId(String userid) throws SmException {
		SubscriberService subscriberService = SefCoreServiceResolver.getSusbcriberStore();

		try {
			//@TODO fill in nbCorrelator
			log.warn("Please verify the correct value for nbCorrelator value, current value is pasaload:money");
			Subscriber subscriber = subscriberService.getSubscriberByUserId("pasaload:money", userid);
			if(subscriber==null){
				throw new SmException(ErrorCode.SUBSCRIBER_NOT_FOUND);
			}
			
			return subscriber;
		} catch (PersistenceError e) {
			log.error(e.getMessage(),e);
			throw new SmException(e);
		}
		
	}
}
