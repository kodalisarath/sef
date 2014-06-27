package com.ericsson.raso.sef.notification.workflows.callingcircle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.raso.sef.notification.workflows.CallingCircleEdrProcessor;
import com.ericsson.raso.sef.notification.workflows.NotificationContext;
import com.ericsson.raso.sef.notification.workflows.promo.Promo;
import com.ericsson.raso.sef.client.air.command.RefillCommand;
import com.ericsson.raso.sef.client.air.command.UpdateAccumulatorCommand;
import com.ericsson.raso.sef.client.air.command.UpdateFaFListCommand;
import com.ericsson.raso.sef.client.air.command.UpdateOfferCommand;
import com.ericsson.raso.sef.client.air.request.AccumulatorInformation;
import com.ericsson.raso.sef.client.air.request.FafInformation;
import com.ericsson.raso.sef.client.air.request.RefillRequest;
import com.ericsson.raso.sef.client.air.request.UpdateAccumulatorRequest;
import com.ericsson.raso.sef.client.air.request.UpdateFaFListRequest;
import com.ericsson.raso.sef.client.air.request.UpdateOfferRequest;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.mapper.CallingCircleMapper;
import com.ericsson.raso.sef.core.db.mapper.PurchaseMapper;
import com.ericsson.raso.sef.core.db.model.CallingCircle;
import com.ericsson.raso.sef.core.db.model.CallingCircleRelation;
import com.ericsson.raso.sef.core.db.model.Purchase;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.service.CallingCircleDBService;
import com.ericsson.sef.scheduler.command.ScheduleRemoveCallingCircleCmd;
import com.ericsson.sef.scheduler.common.TransactionEngineHelper;

public class SmCallingCircleService implements CallingCircleService {

	private static Logger log = LoggerFactory.getLogger(SmCallingCircleService.class);

	@Override
	@Transactional
	public Collection<CallingCircle> addCircle(String callingCircleId, String aparty, String bparty) throws SmException {
		
		
//		Promo promo = CallingCircleUtil.getCallingCirclePromo(callingCircleId);
//
//		String productId = promo.getAssociatedPromo();
//		Subscriber apartySubscriber = CallingCircleUtil.getSubscriberByMsisdn(aparty);
//		Subscriber bpartySubscriber = CallingCircleUtil.getSubscriberByMsisdn(bparty);
//		 
//		//CallingCircleDBService callingCircleService = SefCoreServiceResolver.getCallingCircleDBService();
//		
//		CallingCircleMapper callingCircleMapper = NotificationContext.getBean(CallingCircleMapper.class);
//		
//		Purchase purchase = getValidPurchase(apartySubscriber.getUserId(), productId);
//
//		if (purchase == null) {
//			log.error("No valid purchase found for " + productId + " for " + aparty);
//			throw new SmException(new ResponseCode(500, "No valid purchase found for " + productId + " for " + aparty));
//		}
//
//		CallingCircle smcircle = new CallingCircle();
//		smcircle.setAparty(apartySubscriber.getUserId());
//		smcircle.setBparty(bpartySubscriber.getUserId());
//		smcircle.setApartyMsisdn(apartySubscriber.getMsisdn());
//		smcircle.setBpartyMsisdn(bpartySubscriber.getMsisdn());
//		
//		smcircle.setiLProductId(productId);
//		smcircle.setRelationship(CallingCircleRelation.SPONSER_MEMBER);
//		 
//		Collection<CallingCircle> smCircles = callingCircleMapper.findIdenticalCircles(smcircle);
//		if (smCircles != null && smCircles.size() > 0) {
//			log.error("Calling circle with productID: " + productId + " already exist for Aparty: " + aparty + " and Bparty:" + bparty);
//			throw new SmException(new ResponseCode(500, "Calling circle with productID: " + productId + " already exist for Aparty: " + aparty
//					+ " and Bparty:" + bparty));
//		}
//
//		Collection<CallingCircle> callingCircles = null;
//
//		switch (promo.getType()) {
//		case ONE_WAY:
//			callingCircles = createOneWayCircle(productId, apartySubscriber, bpartySubscriber, purchase);
//			break;
//		case TWO_WAY:
//			callingCircles = createTwoWayCircle(productId, apartySubscriber, bpartySubscriber, purchase, promo);
//			break;
//		default:
//			break;
//		}
		return null;
	}

	@Override
	@Transactional
	public void removeCircle(long circleId) throws SmException {
		CallingCircleMapper callingCircleMapper = NotificationContext.getBean(CallingCircleMapper.class);
		CallingCircleEdrProcessor circleEdrProcessor = new CallingCircleEdrProcessor();
		CallingCircle callingCircle = new CallingCircle();
		callingCircle.setId(circleId);
		try {
			Collection<CallingCircle> identicalCircles = callingCircleMapper.findIdenticalCircles(callingCircle);
			if (identicalCircles.iterator().hasNext()) {
				callingCircle = identicalCircles.iterator().next();

				if (callingCircle != null) {
					Integer indicator = CallingCircleUtil.getIndicator(callingCircle.getiLProductId(), callingCircle.getRelationship());
					if (indicator != null) {
						Subscriber apartySubscriber = CallingCircleUtil.getSubscriberByUserId(callingCircle.getAparty());
						Subscriber bpartySubscriber = CallingCircleUtil.getSubscriberByUserId(callingCircle.getBparty());

						callingCircle.setApartyMsisdn(apartySubscriber.getMsisdn());
						callingCircle.setBpartyMsisdn(bpartySubscriber.getMsisdn());

						FafInformation faf = new FafInformation();
						faf.setFafIndicator(indicator);
						faf.setFafNumber(bpartySubscriber.getMsisdn());
						faf.setOwner("Subscriber");

						List<UpdateFaFListRequest> fafList = new ArrayList<UpdateFaFListRequest>();
						fafList.add(createupdateFnfRequest(apartySubscriber.getMsisdn(), faf, true));
						updateFnfIndicator(fafList);

						callingCircleMapper.removeCallingCircle(circleId);
						circleEdrProcessor.printEdr(CallingCircleUtil.getCallingCircleEdr(callingCircle, "DELETE"));
					}
				}
			}
		} catch (SmException e) {
			circleEdrProcessor.printEdr(CallingCircleUtil.getCallingCircleEdr(callingCircle.getApartyMsisdn(), callingCircle.getBpartyMsisdn(),
					callingCircle.getiLProductId(), "FAILED", e.getMessage()));
			throw e;
		}
	}

	private Collection<CallingCircle> createOneWayCircle(String productId, Subscriber apartySubscriber, Subscriber bpartySubscriber, Purchase purchase) throws SmException {
		CallingCircle callingCircle = createCaliingCircleObj(productId, apartySubscriber, bpartySubscriber, purchase, CallingCircleRelation.SPONSER_MEMBER);
		createCircles(callingCircle);

		Collection<CallingCircle> callingCircles = new ArrayList<CallingCircle>();
		callingCircles.add(callingCircle);
		return callingCircles;
	}

	private Purchase getValidPurchase(String userid, String productId) throws SmException {
		PurchaseMapper purchaseMapper = NotificationContext.getBean(PurchaseMapper.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userid);
		map.put("product_id", productId);

		Collection<Purchase> userPurchase = purchaseMapper.getUserPurchase(map);
		Purchase purchase = null;

		if (userPurchase != null && userPurchase.size() > 0) {
			Iterator<Purchase> iterator = userPurchase.iterator();
			purchase = iterator.next();
			for (Purchase userpurchase : userPurchase) {
				if (userpurchase.getExpiryTime().isAfter(purchase.getExpiryTime())) {
					purchase = userpurchase;
				}
			}
			return purchase;
		}
		return null;
	}

	private CallingCircle createCaliingCircleObj(String productId, Subscriber aparty, Subscriber bparty, Purchase purchase, CallingCircleRelation relation) {
		CallingCircle callingCircle = new CallingCircle();
		callingCircle.setAparty(aparty.getUserId());
		callingCircle.setBparty(bparty.getUserId());
		callingCircle.setCreationTime(new DateTime());
		callingCircle.setiLProductId(productId);
		callingCircle.setPurchaseReference(purchase.getPurchaseId());
		callingCircle.setExpiryTime(purchase.getExpiryTime());
		callingCircle.setRelationship(relation);
		callingCircle.setApartyMsisdn(aparty.getMsisdn());
		callingCircle.setBpartyMsisdn(bparty.getMsisdn());
		return callingCircle;
	}

	private Collection<CallingCircle> createTwoWayCircle(String productId,Subscriber apartySubscriber, Subscriber bpartySubscriber, Purchase purchase, Promo promo)
			throws SmException {
		CallingCircleMapper callingCircleMapper = NotificationContext.getBean(CallingCircleMapper.class);
		List<CallingCircle> callingCircles = new ArrayList<CallingCircle>();

		callingCircles.add(createCaliingCircleObj(productId, apartySubscriber, bpartySubscriber, purchase, CallingCircleRelation.SPONSER_MEMBER));
		callingCircles.add(createCaliingCircleObj(productId, bpartySubscriber, apartySubscriber, purchase, CallingCircleRelation.MEMBER_SPONSER));

		CallingCircle smcircle = new CallingCircle();
		smcircle.setAparty(apartySubscriber.getUserId());
		smcircle.setiLProductId(productId);
		smcircle.setRelationship(CallingCircleRelation.SPONSER_MEMBER);

		if(promo.getMmRelationShip()!=null && promo.getMmRelationShip()!=0){
			
			Collection<CallingCircle> smCircles = callingCircleMapper.findIdenticalCircles(smcircle);
	
			for (CallingCircle callingCircle : smCircles) {
				Collection<CallingCircle> mmCircles = callingCircleMapper.findAllMemberMemberCircles(callingCircle);
				if (mmCircles != null && mmCircles.size() > 0) {
					for (CallingCircle mmCircle : mmCircles) {
						Subscriber mem_apartysubscriber = CallingCircleUtil.getSubscriberByUserId(mmCircle.getAparty());
						Subscriber mem_bpartysubscriber = CallingCircleUtil.getSubscriberByUserId(mmCircle.getBparty());
						
						mmCircle.setPurchaseReference(purchase.getPurchaseId());
						mmCircle.setExpiryTime(purchase.getExpiryTime());
						
						mmCircle.setApartyMsisdn(mem_apartysubscriber.getMsisdn());
						mmCircle.setBpartyMsisdn(mem_bpartysubscriber.getMsisdn());
						
						callingCircles.add(mmCircle);
					}
				} else {

					Subscriber cpartysubscriber = CallingCircleUtil.getSubscriberByUserId(callingCircle.getBparty());
					callingCircles
							.add(createCaliingCircleObj(productId,cpartysubscriber, bpartySubscriber, purchase, CallingCircleRelation.MEMBER_MEMBER));
					callingCircles
							.add(createCaliingCircleObj(productId, bpartySubscriber, cpartysubscriber, purchase, CallingCircleRelation.MEMBER_MEMBER));
				}
			}
		}

		createCircles(callingCircles.toArray(new CallingCircle[callingCircles.size()]));
		updateAccumulator(apartySubscriber.getMsisdn());

		String ucipStr[] = promo.getUcip().split(",");
		if (ucipStr[0].startsWith(CallingCircleConstants.UPADTEOFFER)) {
			updateOffer(bpartySubscriber.getMsisdn(), ucipStr, purchase);
		} else {
			refill(bpartySubscriber.getMsisdn(), ucipStr);
		}

		return callingCircles;
	}

	private void createCircles(CallingCircle... callingCircles) throws SmException {
		CallingCircleMapper callingCircleMapper = NotificationContext.getBean(CallingCircleMapper.class);

		if (callingCircles == null || callingCircles.length == 0)
			return;

		List<UpdateFaFListRequest> fafList = new ArrayList<UpdateFaFListRequest>();
		for (CallingCircle callingCircle : callingCircles) {
			if (callingCircle.getId() == 0) {
				long id = callingCircleMapper.callingCircleSequence(UUID.randomUUID().toString()).getSeq();
				callingCircle.setId(id);
				callingCircleMapper.createCallingCircle(callingCircle);

				Integer indicator = CallingCircleUtil.getIndicator(callingCircles[0].getiLProductId(), callingCircle.getRelationship());

				if (indicator != null) {
					FafInformation faf = new FafInformation();
					faf.setFafIndicator(indicator);
					faf.setFafNumber(callingCircle.getBpartyMsisdn());
					faf.setOwner("Subscriber");

					fafList.add(createupdateFnfRequest(callingCircle.getApartyMsisdn(), faf, false));
				}
			} else {
				callingCircleMapper.updateCallingCircle(callingCircle);
			}
			ScheduleRemoveCallingCircleCmd scheduleRemoveCallingCircleCmd = new ScheduleRemoveCallingCircleCmd(callingCircle);
			scheduleRemoveCallingCircleCmd.execute();
		}
		updateFnfIndicator(fafList);
	}

	public UpdateFaFListRequest createupdateFnfRequest(String subscriber, FafInformation fafInformation, boolean isDel) throws SmException {

		UpdateFaFListRequest faFListRequest = new UpdateFaFListRequest();
		faFListRequest.setSubscriberNumber(subscriber);
		if (isDel) {
			faFListRequest.setFafAction("DELETE");
		} else {
			faFListRequest.setFafAction("ADD");
		}
		faFListRequest.setFafInformation(fafInformation);
		return faFListRequest;
	}

	public void updateFnfIndicator(List<UpdateFaFListRequest> fafRequest) throws SmException {
		if (fafRequest.size() > 0) {
			for (UpdateFaFListRequest request : fafRequest) {
				UpdateFaFListCommand faFListCommand = new UpdateFaFListCommand(request);
				faFListCommand.execute();
			}
		}
	}

	private void updateAccumulator(String msisdn) throws SmException {
		UpdateAccumulatorRequest accumulatorRequest = new UpdateAccumulatorRequest();

		AccumulatorInformation accumulatorInformation = new AccumulatorInformation();
		accumulatorInformation.setAccumulatorID(201);
		accumulatorInformation.setAccumulatorValueRelative(1);

		List<AccumulatorInformation> list = new ArrayList<AccumulatorInformation>();
		list.add(accumulatorInformation);
		accumulatorRequest.setSubscriberNumber(msisdn);
		accumulatorRequest.setAccumulatorUpdateInformation(list);

		UpdateAccumulatorCommand accumulatorCommand = new UpdateAccumulatorCommand(accumulatorRequest);
		accumulatorCommand.execute();
	}

	private void refill(String bparty, String[] ucipStr) throws SmException {
		RefillRequest refillRequest = new RefillRequest();

		String refillId = ucipStr[0].split(":")[1];
		String promoName = ucipStr[1].split(":")[1];

		refillRequest.setSubscriberNumber(bparty);
		refillRequest.setRefProfID(refillId);
		refillRequest.setRefType(1);
		refillRequest.setTransacAmount("1");
		refillRequest.setExternalData1(promoName);
		refillRequest.setTransacCurrency("PHP");

		refillRequest.setRefAccAfterFlag(false);
		refillRequest.setRefAccBeforeFlag(false);

		RefillCommand command = new RefillCommand(refillRequest);

		command.execute();
	}

	private void updateOffer(String bparty, String[] ucipStr, Purchase purchase) throws SmException {
		UpdateOfferRequest offerRequest = new UpdateOfferRequest();
		offerRequest.setSubscriberNumber(bparty);
		offerRequest.setOfferID(Integer.parseInt(ucipStr[0].split(":")[1]));
		offerRequest.setOfferType(2);
		offerRequest.setExpiryDateTime(new Date(purchase.getExpiryTime().getMillis()));
		UpdateOfferCommand updateOfferCommand = new UpdateOfferCommand(offerRequest);
		updateOfferCommand.execute();
	}
}
