package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vasclient.wsdl.VASClientSEI;

import com.ericsson.raso.sef.bes.prodcat.SubscriptionLifeCycleEvent;
import com.ericsson.raso.sef.client.air.command.GetAccountDetailsCommand;
import com.ericsson.raso.sef.client.air.command.RefillCommand;
import com.ericsson.raso.sef.client.air.command.UpdateAccumulatorCommand;
import com.ericsson.raso.sef.client.air.command.UpdateFaFListCommand;
import com.ericsson.raso.sef.client.air.command.UpdateOfferCommand;
import com.ericsson.raso.sef.client.air.request.AccumulatorInformation;
import com.ericsson.raso.sef.client.air.request.FafInformation;
import com.ericsson.raso.sef.client.air.request.GetAccountDetailsRequest;
import com.ericsson.raso.sef.client.air.request.RefillRequest;
import com.ericsson.raso.sef.client.air.request.UpdateAccumulatorRequest;
import com.ericsson.raso.sef.client.air.request.UpdateFaFListRequest;
import com.ericsson.raso.sef.client.air.request.UpdateOfferRequest;
import com.ericsson.raso.sef.client.air.response.AccountFlags;
import com.ericsson.raso.sef.client.air.response.GetAccountDetailsResponse;
import com.ericsson.raso.sef.client.air.response.OfferInformation;
import com.ericsson.raso.sef.client.air.response.ServiceOffering;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.core.db.model.CallingCircleRelation;
import com.ericsson.raso.sef.core.db.model.CurrencyCode;
import com.ericsson.raso.sef.core.db.model.smart.CallingCircle;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.smart.CallingCircleService;
import com.ericsson.raso.sef.core.smpp.SmppMessage;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentServiceResolver;
import com.ericsson.raso.sef.fulfillment.profiles.BlockingFulfillment;
import com.ericsson.raso.sef.fulfillment.profiles.PartialReadSubscriberProfile;
import com.ericsson.raso.sef.fulfillment.profiles.RefillProfile;
import com.ericsson.raso.sef.fulfillment.profiles.smart.callingcirclelogging.CallingCircleEdr;
import com.ericsson.sef.bes.api.entities.Product;

public final class CallingCircleProfile extends RefillProfile {
	private static final long serialVersionUID = 2321187981269420862L;
	private static final Logger logger = LoggerFactory.getLogger(CallingCircleProfile.class);
	
	private String fafIndicatorSponsorMember;
	private String fafIndicatorMemberSponsor;
	private String fafIndicatorMemberMember;

	private Integer fafAccumulatorId;

	private String subscriberId;
	private String prodcatOffer;
	private String memberB;
	private Integer maxMembers;
	private String associatedPromo;
	private String welcomeMessageEventId;
	private String A_PartyMemberThresholdBreachMessageEventId;
	private String NewMemberAddedEventId;
	private String B_PartyInvalidStateEventId;
	
	private Integer callingCircleCsOfferID;

	private String freebieType; // this must be either "OFFER" or "REFILL"
	private Integer freebieOfferId;
	private Integer freebieOfferType;
	private Long freebieOfferValidity;
	private String freebieRefillID;
	private String freebiePlanCode;
	private Integer freebieRefillType;
	private String freebieTransactionAmount;
	private CurrencyCode freebieTransactionCurrency;
	private String freebieRenewalAmount;
	private String freebiePurchaseAmount;


	private Map<String, String> memberBMetas = new HashMap<String, String>();
	private Map<String, String> memberAMetas = new HashMap<String, String>();
	private long callingCircleExpiry = -1;

	public CallingCircleProfile(String name) {
		super(name);
	}

	@Override
	public List<Product> fulfill(Product p, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();
		
		logger.debug("CallingCircle fulfill() invoked... Sanity Check: metas: " + map + "\n\n static config: " + this.toString());
		
		logger.debug("Check metas: " + map);
		if (map != null && !map.containsKey("CallingCircleWorkFlow")) {
			logger.debug("Event Source is NOT Notification Engine. Delegating to 'predefined' Refill.");
			return super.fulfill(p, map);
		}

		boolean breakFlow = false;

		// Step 1: Collect all variables needed here....
		this.subscriberId = map.get("SUBSCRIBER_ID");
		if (this.subscriberId == null)
			this.subscriberId = map.get("msisdn");
		
		this.prodcatOffer = map.get("prodcatOffer");
		if (this.prodcatOffer == null)
			this.prodcatOffer = map.get("productId");
		
		this.memberB = map.get("B-Party");
		if (this.memberB == null)
			this.memberB = map.get("BP");


		// Step 2: Get AccountDetails
		logger.debug("Attempting to fetch detals of A-Party: " + this.subscriberId);
		if (!this.readSubscriber(this.subscriberId, this.memberAMetas)) {
			logger.error("A Party was not found or other error fetching detals... MIGRATION WAAS FAULTY!!!");
			this.sendSorryMessage(NotificationMessageEvent.B_PartyUnknown.getEventName(), this.subscriberId);
			CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember, this.callingCircleExpiry);
			CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "Unable to check A-party details");

			breakFlow = true;
		} else 
			logger.debug("A-Party (" + this.subscriberId + ") getAccountDetails graceful!!");
		
		if (!breakFlow) {
			logger.debug("Going to check for A-Party Calling Circle Expiry...");
			 this.callingCircleExpiry = this.getCallingCircleExpiry(); 
			 if (this.callingCircleExpiry == -1) {
				 logger.error("A Party has not subscribed to the calling circle offer!!");
				 this.sendSorryMessage(NotificationMessageEvent.B_PartyUnknown.getEventName(), this.subscriberId);
				 CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember, this.callingCircleExpiry);
				 CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "Unable to check A-party details");

				 breakFlow = true;

			 }
		}
		
		logger.debug("Attempting to fetch detals of B-Party: " + this.memberB);
		if (!breakFlow && !this.readSubscriber(this.memberB, this.memberBMetas)) {
			logger.error("Probably the user was not found or other error fetching detals...");
			this.sendSorryMessage(NotificationMessageEvent.B_PartyUnknown.getEventName(), this.subscriberId);
			CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember, this.callingCircleExpiry);
			CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "Unable to check B-party details");

			breakFlow = true;
		} else 
			logger.debug("B-Party (" + this.memberB + ") getAccountDetails graceful!!");
		
		
		// Step 3: Check for B-Number status
		logger.debug("Attempting to check contract state of B-Party: " + this.memberB);
		if (!breakFlow && !this.checkAllowedContractState(this.memberB)) {
			logger.error("User not allowed to enter Calling Circle Membership");
			this.sendSorryMessage(this.B_PartyInvalidStateEventId, this.subscriberId);
			CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember, this.callingCircleExpiry);
			CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "B-party in invalid state");
			breakFlow = true;
		} else
			logger.debug("B-Party (" + this.memberB + ") is confirmed to be in valid state!!");
		
		
		// Step 4a: Send Sorry Messages - already done above

		// Step 5: B-NUmber is already in Active or Grace...
		/*
		 * 1. Check the DB for current members & verify breach
		 * 2.  Prepare UCIP requests for FAF Update
		 * 	- 1 UCIP Command per A number with all relationship types..
		 *  - 2 Fire the UCIP Commands & dont give a fuck about the responses.
		 *  - 3 Update Accumulator ID for A-number
		 *  - 4 Store in the DB for all relationships
		 */
		if (!breakFlow) {
			CallingCircleService ccService = SefCoreServiceResolver.getCallingCircleService();
			logger.debug("Attempting to check max member violation");
			try {
				CallingCircle query = new CallingCircle();
				query.setOwner(subscriberId);
				query.setMemberB(memberB);
				query.setProdcatOffer(prodcatOffer);
				int memberCount = ccService.fetchCallingCircleMemberCountForOwner(UniqueIdGenerator.generateId(), query);
				logger.debug("Existing member count: " + memberCount + ", max members: " + this.maxMembers);
				if (this.maxMembers <= memberCount) {
					logger.warn("Subscriber: " + this.subscriberId + " already has max members allowed for this group: " + this.prodcatOffer);
					CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember, this.callingCircleExpiry);
					CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "Already invited max members to Calling Circle");
					breakFlow = true;
				}
			} catch(PersistenceError e) {
				logger.debug("Cannot assert member threshold breach. Cannot proceed!!", e);
				this.sendSorryMessage(this.A_PartyMemberThresholdBreachMessageEventId, this.subscriberId);
				CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember, this.callingCircleExpiry);
				CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "Unable to assert max membership breach for A-Party");
				breakFlow = true;
			}

			logger.debug("Attempting to add members to the circle...	");
			try {
				if (!breakFlow && !this.addCallingCircleMembers()) {
					logger.warn("Transaction with AIR & DB seems to have failed!! Send Sorry and stop.");
					this.sendSorryMessage(NotificationMessageEvent.UpdateFafFailed.getEventName(), this.subscriberId);
					CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember, this.callingCircleExpiry);
					CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "Update CS-AIR/DB for Calling Circle Membership failed");
					breakFlow = true;
				} else 
					logger.debug("Completed adding/updating all members to this circle...");
			} catch (SmException e) {
				logger.debug("Cannot update FAF Info to CS-AIR/DB. Send Sorry and stop", e);
				this.sendSorryMessage(NotificationMessageEvent.UpdateFafFailed.getEventName(), this.subscriberId);
				CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember, this.callingCircleExpiry);
				CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "Update CS-AIR/DB for Calling Circle Membership failed");
				breakFlow = true;
			} catch (PersistenceError e) {
				logger.debug("Cannot update FAF Info to CS-AIR/DB. Send Sorry and stop", e);
				this.sendSorryMessage(NotificationMessageEvent.UpdateFafFailed.getEventName(), this.subscriberId);
				CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember, this.callingCircleExpiry);
				CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "Update CS-AIR/DB for Calling Circle Membership failed");
				breakFlow = true;
			}
		}



		// Step 6: Check for Freebies
		/*
		 * 1. for every prodcatOffer there will be a freebie plancode configured
		 * 2. a freebie can be (a) offer extension with life-cycle dates, (b) refill
		 * 3. send sms to all members of the calling circle
		 * 4. gnerate cdr
		 * 5. shoot emlpp command to sps
		 */
		try {
			if (!breakFlow && this.freebieType != null && this.freebieType.equals("OFFER")) {
				this.updateFreebieOffer(memberB);
			}

			if (!breakFlow && this.freebieType != null && this.freebieType.equals("REFILL")) {
				this.performFreebieRefill(memberB);
			}
			
			if (!breakFlow) {
				logger.debug("sending welcome message...");
				this.sendWelcomeMessage();

				logger.debug("sending emlpp update...");
				this.sendEmlppUpdate();
			}

		} catch (SmException e) {
			logger.debug("Cannot provision freebie in CS-AIR. Send Sorry and stop", e);
			//this.sendSorryMessage(NotificationMessageEvent.FreebieProvisioningFailed);
			//breakFlow = true;
		}

		if (breakFlow) {
			throw new FulfillmentException("ffe", new ResponseCode(999, "Workflow failed!! Check logs..."));
		}
		
		p.setMetas(map);
		returned.add(p);
		return returned;
	}

	private void sendEmlppUpdate() {
		try {
			int ser1 = (int) Math.floor(Math.random() * 900000000) + 1000000000;
			int ser2 = (int) Math.floor(Math.random() * 900000000) + 1000000000;
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String emlppString = ser1 + "" + ser2 + ":" + df.format(new Date()) + ":" + "GSFA:G_ULC1::" + this.memberB + "::38:::::::::::::::::::::::;";
			logger.info("EMLPP REQ: " + emlppString);
			VASClientSEI vasClient = FulfillmentServiceResolver.getVASClient();
			String emlResponse = vasClient.sendToSPS(emlppString);
			logger.info("EMLPP RES:" + emlResponse);
			     
		} catch (Exception e) {
			logger.error("Unable to update EMLPP. Cause: " + e.getMessage(), e);
		}
	}
	

	private long getCallingCircleExpiry() {
		for (String key : this.memberAMetas.keySet()) {
			logger.debug("FLEXI:: processing meta:" + key + "=" + this.memberAMetas.get(key));

			if (key.startsWith("READ_SUBSCRIBER_OFFER")) {
				logger.debug("FLEXI:: OFFER_ID...." + this.memberAMetas.get(key));
				String offerForm = this.memberAMetas.get(key);
				logger.debug("Check before split - offerForm: " + offerForm);
				String offerParts[] = offerForm.split(",");
				logger.debug("Offer Parts: " + offerParts.length);
				int i= 0; for (String part: offerParts) {
					logger.debug("OfferPart[" + i++ + "] :=" + part);
				}
				String offerId = offerParts[0];
				String start = offerParts[1];
				if (start.equals("null"))
					start = offerParts[2];
				String expiry = offerParts[3];
				if (expiry.equals("null"))
					expiry = offerParts[4];


				if (Integer.parseInt(offerId) == this.callingCircleCsOfferID.intValue()) {
					if (expiry == null) {
						logger.error("CS-AIR did not return expiry for this configured OfferID: " + offerId);
						return -1;
					}
					return Long.parseLong(expiry);
				} 
			}
		}
		logger.error("Possibly the BC is configured wrong. Cnfigured OfferId: " + this.callingCircleCsOfferID + " was not found in CS-AIR for A-Party!!");
		return -1;
	}

	



	private void updateFreebieOffer(String subscriberId) throws SmException {
		UpdateOfferRequest offerRequest = new UpdateOfferRequest();
		offerRequest.setSubscriberNumber(subscriberId);
		offerRequest.setOfferID(this.freebieOfferId);
		offerRequest.setOfferType(this.freebieOfferType);
		offerRequest.setExpiryDateTime(new Date(System.currentTimeMillis() + this.freebieOfferValidity));
		UpdateOfferCommand updateOfferCommand = new UpdateOfferCommand(offerRequest);
		updateOfferCommand.execute();

	}

	private void performFreebieRefill(String subscriberId) throws SmException {
		RefillRequest refillRequest = new RefillRequest();

		refillRequest.setSubscriberNumber(subscriberId);
		refillRequest.setRefProfID(this.freebieRefillID);
		refillRequest.setRefType(this.freebieRefillType);
		refillRequest.setTransacAmount(this.freebieTransactionAmount);
		refillRequest.setExternalData1(this.freebiePlanCode);
		refillRequest.setTransacCurrency(this.freebieTransactionCurrency.name());
		refillRequest.setRefAccAfterFlag(false);
		refillRequest.setRefAccBeforeFlag(false);

		RefillCommand command = new RefillCommand(refillRequest);
		command.execute();


	}



	private boolean addCallingCircleMembers() throws SmException, PersistenceError{
		/*
		 * 1. Check the DB for current members & verify breach
		 * 2.  Prepare UCIP requests for FAF Update
		 * 	- 1 UCIP Command per A number with all relationship types..
		 *  - 2 Fire the UCIP Commands & dont give a fuck about the responses.
		 *  - 3 Update Accumulator ID for A-number
		 *  - 4 Store in the DB for all relationships
		 */
		CallingCircleService ccService = SefCoreServiceResolver.getCallingCircleService();

		Collection<String> members = null;
		CallingCircle query = new CallingCircle();
		query.setOwner(subscriberId);
		query.setProdcatOffer(prodcatOffer);	
		
		try {
			members = ccService.fetchCallingCircleMembersOnly(UniqueIdGenerator.generateId(), query);
		} catch (PersistenceError e) {
			logger.debug("Cannot fetch member threshold breach. Cannot proceed!!", e);
			CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember);
			CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "Could not assert existing members in this Calling Circle");
			return false;
		}

		CallingCircle ccRelationship = new CallingCircle(this.subscriberId, prodcatOffer, subscriberId, memberB, CallingCircleRelation.SPONSER_MEMBER, fafIndicatorSponsorMember, this.callingCircleExpiry);
		logger.debug("Adding Member & relation: " + ccRelationship);
		this.updateFaf(subscriberId, ccRelationship, "ADD");
		logger.debug("AIR updated");
		ccService.createCallingCircleMemberMapping(UniqueIdGenerator.generateId(), ccRelationship);
		logger.debug("DB updated");
		CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, ccRelationship, this.fafIndicatorSponsorMember, null);
		logger.debug("EDR updated");
		
		ccRelationship = new CallingCircle(subscriberId, prodcatOffer, memberB, subscriberId,CallingCircleRelation.MEMBER_SPONSER, fafIndicatorMemberSponsor, this.callingCircleExpiry);
		logger.debug("Adding Member & relation: " + ccRelationship);
		this.updateFaf(memberB, ccRelationship, "ADD");
		logger.debug("AIR updated");
		ccService.createCallingCircleMemberMapping(UniqueIdGenerator.generateId(), ccRelationship);
		logger.debug("DB updated");
		CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, ccRelationship, this.fafIndicatorMemberSponsor, null);
		logger.debug("EDR updated");
		
		this.updateFafAccumulator(subscriberId, fafAccumulatorId, 1);


		for (String member: members) {
			ccRelationship = new CallingCircle(subscriberId, prodcatOffer, memberB, member, CallingCircleRelation.MEMBER_MEMBER, fafIndicatorMemberMember, this.callingCircleExpiry);
			logger.debug("Adding Member & relation: " + ccRelationship);
			this.updateFaf(memberB, ccRelationship, "ADD");
			logger.debug("AIR updated");
			ccService.createCallingCircleMemberMapping(UniqueIdGenerator.generateId(), ccRelationship);
			logger.debug("DB updated");
			CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, ccRelationship, this.fafIndicatorMemberMember, null);
			logger.debug("EDR updated");
			
			ccRelationship = new CallingCircle(subscriberId, prodcatOffer, member, memberB, CallingCircleRelation.MEMBER_MEMBER, fafIndicatorMemberMember, this.callingCircleExpiry);
			logger.debug("Adding Member & relation: " + ccRelationship);
			ccService.createCallingCircleMemberMapping(UniqueIdGenerator.generateId(), ccRelationship);
			logger.debug("DB updated");
			this.updateFaf(member, ccRelationship, "ADD");
			CallingCircleEdr.generateEdr("ADD", this.prodcatOffer, this.callingCircleExpiry, ccRelationship, this.fafIndicatorMemberMember, null);
			logger.debug("EDR updated");
			
		}


		this.updateFafAccumulator(subscriberId, fafAccumulatorId, 1);
		logger.debug("A-party CC Accumulator updated");
		

		return true;
	}

	private void updateFafAccumulator(String subscriberId, Integer accumulatorId, Integer memberCount) throws SmException {
		List<AccumulatorInformation> accumulatorUpdateInformation = new ArrayList<AccumulatorInformation>();
		AccumulatorInformation accumulatorUpdateInfo = new AccumulatorInformation();
		accumulatorUpdateInfo.setAccumulatorID(accumulatorId);
		
		if (memberCount >0 )
			accumulatorUpdateInfo.setAccumulatorValueRelative(1);
		else
			accumulatorUpdateInfo.setAccumulatorValueAbsolute(0);
			
		accumulatorUpdateInformation.add(accumulatorUpdateInfo);


		UpdateAccumulatorRequest request = new UpdateAccumulatorRequest();
		request.setSubscriberNumber(subscriberId);
		request.setSubscriberNumberNAI(1);
		request.setAccumulatorUpdateInformation(accumulatorUpdateInformation);

		UpdateAccumulatorCommand command = new UpdateAccumulatorCommand(request);
		command.execute();
	}

	private void updateFaf(String subscriberId, CallingCircle callingCircle, String fafAction) throws SmException {
		List<FafInformation> updateFafInfo = new ArrayList<FafInformation>();
		FafInformation fafInfo = new FafInformation();
		fafInfo.setOwner("Subscriber");
		fafInfo.setFafIndicator(Integer.parseInt(callingCircle.getFafIndicator()));
		fafInfo.setFafNumber(callingCircle.getMemberB());
		updateFafInfo.add(fafInfo);

		UpdateFaFListRequest request = new UpdateFaFListRequest();
		request.setFafInformationList(updateFafInfo);
		request.setFafAction(fafAction);
		request.setSubscriberNumber(subscriberId);
		request.setSubscriberNumberNAI(1);

		UpdateFaFListCommand command = new UpdateFaFListCommand(request);
		command.execute();


	}

	private void sendSorryMessage(String eventName, String subscriberId) {
		SmppMessage message = new SmppMessage();
		message.setDestinationMsisdn(subscriberId);
		message.setMessageBody(eventName + "," + "x:X");
		FulfillmentServiceResolver.getCamelContext().createProducerTemplate().sendBody("activemq:queue:notification", message);

	}

	private void sendWelcomeMessage() {

		CallingCircleService ccService = SefCoreServiceResolver.getCallingCircleService();

		Collection<String> members = null;
		try {
			CallingCircle query = new CallingCircle();
			query.setOwner(subscriberId);
			query.setProdcatOffer(prodcatOffer);	
			members = ccService.fetchAllCallingCircleMembers(UniqueIdGenerator.generateId(), query);
		} catch (PersistenceError e) {
			logger.error("Cannot fetch member welcome message. Cannot proceed!!", e);
			return;
		}

		String circleNumbers = "";
		for (String msisdn: members) {
			circleNumbers += (circleNumbers.isEmpty()?"":"#") + msisdn;
		}

		for (String member: members) {
			SmppMessage message = new SmppMessage();
			message.setDestinationMsisdn(member);
			message.setMessageBody(this.welcomeMessageEventId + ",circle-numbers:" + circleNumbers + ",promo:" + this.associatedPromo);
			FulfillmentServiceResolver.getCamelContext().createProducerTemplate().sendBody("activemq:queue:notification", message);
		}
	}


	private boolean checkAllowedContractState(String memberB2) {

		OfferInfo oInfo = null;
		long longestExpiry = 0; long secondLongestExpiry = 0;
		OfferInfo endurantOffer = null; boolean anyOfferFound = false;
		Map<String, OfferInfo> subscriberOffers = new HashMap<String, OfferInfo>();
		TreeSet<OfferInfo> sortedOffers = new TreeSet<OfferInfo>();
		for (String key : this.memberBMetas.keySet()) {
			logger.debug("FLEXI:: processing meta:" + key + "=" + this.memberBMetas.get(key));

			if (key.startsWith("READ_SUBSCRIBER_SERVICE_OFFERING")) {
				logger.debug("FLEXI:: SERVICE_OFFERING...." + this.memberBMetas.get(key));
				String soForm = this.memberBMetas.get(key);
				logger.debug("Check before split - offerForm: " + soForm);
				String soParts[] = soForm.split(",");
				logger.debug("SO Parts: " + soParts.length);
				int i= 0; for (String part: soParts) {
					logger.debug("soParts[" + i++ + "] :=" + part);
				}
				int soID = Integer.parseInt(soParts[0]);
				boolean soFlag = Boolean.parseBoolean(soParts[1]);

				SvcOffering so = new SvcOffering(soID, soFlag);
				if ( (so.soID >= 2 && so.soID <= 7) && so.isFlag()) {
					logger.debug("SO ID: " + soID + " is SET. User not allowed for this transaction");
					return false;
				}
			}

			if (key.startsWith("READ_SUBSCRIBER_OFFER")) {
				logger.debug("FLEXI:: OFFER_ID...." + this.memberBMetas.get(key));
				String offerForm = this.memberBMetas.get(key);
				logger.debug("Check before split - offerForm: " + offerForm);
				String offerParts[] = offerForm.split(",");
				logger.debug("Offer Parts: " + offerParts.length);
				int i= 0; for (String part: offerParts) {
					logger.debug("OfferPart[" + i++ + "] :=" + part);
				}
				String offerId = offerParts[0];
				String start = offerParts[1];
				if (start.equals("null"))
					start = offerParts[2];
				String expiry = offerParts[3];
				if (expiry.equals("null"))
					expiry = offerParts[4];

				String daID = SefCoreServiceResolver.getConfigService().getValue("Global_offerMapping", offerId);
				String walletName = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", offerId);

				/*
				 * As per Tanzeem the following offers must be ignored from considering life-cycle date impacts
				 */
				int offerID = Integer.parseInt(offerId);
				if (offerID == 2) {
					this.memberBMetas.put("inGrace", "true");
					logger.debug("FLEXI:: CUSTOMER IN GRACE!!!");
				}

				if (offerID == 4) {
					logger.debug("FLEXI:: CUSTOMER IN RECYCLE. REJECTING...");
					return false;
				}

				if (daID == null) {
					logger.debug("OfferID: " + offerId + " is not configure with a DA. Ignoring...");
					continue;
				}

				if (offerID == 1 || offerID == 2 || offerID == 4 || offerID == 1241 || (offerID >= 7000 && offerID <= 9999)) {
					logger.debug("FLEXI:: Offer listed in omission case. Ignoring...");
					continue;
				}

				oInfo = new OfferInfo(offerId, Long.parseLong(expiry), Long.parseLong(start), daID, walletName);
				subscriberOffers.put(offerId, oInfo);
				sortedOffers.add(oInfo);
				anyOfferFound = true;

				logger.debug("FLEXI:: OFFER_INFO: " + oInfo);


			}

			if (key.startsWith("READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG")) {
				if (this.memberBMetas.get(key).equals("false")) {
					logger.debug("Seems like subscriber is in PREACTIVE state. Not Allowed");
					this.memberBMetas.put("inPreactive", "true");
				}
			}
		}
		return true;
	}

	@Override
	public List<Product> prepare(Product p, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();

		p.setMetas(map);
		returned.add(p);
		return returned;
	}

	@Override
	public List<Product> query(Product p, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();

		p.setMetas(map);
		returned.add(p);
		return returned;
	}

	@Override
	public List<Product> revert(Product p, Map<String, String> map) throws FulfillmentException {
		List<Product> returned = new ArrayList<Product>();

		// Logic goes like this: 
		/*
		 * 1. query all members of the impacted calling circle which is getting expired here...
		 * 2. check if the members exist for the same callinc circle product in other groups
		 * 3. prepare faf update commmands for each member who is not present in other groups
		 * 4. fire faf update for owner of this calling circle
		 * 5. fire faf updates for members who are only present in this calling circle group
		 * 6. update/delete db with the expiry event (remove entries)
		 * 7. update accumulator id (reset to 0) for the owner of this calling circle group
		 * 8. generate cdr for deleted numbers - in sync with the DB, implying members who are not removed from DB must also not feature in the cdr
		 */
		
		boolean breakFlow = false;
		
		try {
			if (!this.removeCallingCircleMembers()) {
				logger.error("Failed to remove calling circle from CS-AIR.");
				breakFlow = true;
			}
		} catch(SmException e) {
			logger.error("Failed to remove calling circle from  CS-AIR. Cause: " + e.getMessage(), e);
			CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember, this.callingCircleExpiry);
			CallingCircleEdr.generateEdr("DELETE", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "Could not assert existing members in this Calling Circle");
			breakFlow = true;
		}
		
		
		try {
			if (!breakFlow && !this.deleteCallingCircle()) {
				logger.error("Failed to remove calling circle from DB.");
				breakFlow = true;
			}
		} catch(PersistenceError e) {
			logger.error("Failed to remove calling circle from DB. Cause: " + e.getMessage(), e);
			CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember, this.callingCircleExpiry);
			CallingCircleEdr.generateEdr("DELETE", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "Could not assert existing members in this Calling Circle");
			breakFlow = true;
		}
		
		
		
		
		p.setMetas(map);
		returned.add(p);
		return returned;
	}
	
	private boolean deleteCallingCircle() throws PersistenceError {
		CallingCircleService ccService = SefCoreServiceResolver.getCallingCircleService();
		
		CallingCircle deleted = new CallingCircle();
		deleted.setOwner(this.subscriberId);
		deleted.setProdcatOffer(this.prodcatOffer);
		ccService.deleteCallingCircle(UniqueIdGenerator.generateId(), deleted);
		return true;
	}
	
	private boolean removeCallingCircleMembers() throws SmException {
		CallingCircleService ccService = SefCoreServiceResolver.getCallingCircleService();
		
		// first get all members
		Collection<String> members = null;
		CallingCircle query = new CallingCircle();
		query.setOwner(subscriberId);
		query.setProdcatOffer(prodcatOffer);	
		
		try {
			members = ccService.fetchAllCallingCircleMembers(UniqueIdGenerator.generateId(), query);
		} catch (PersistenceError e) {
			logger.debug("Cannot fetch member threshold breach. Cannot proceed!!", e);
			CallingCircle edrEntry = new CallingCircle(this.subscriberId, this.prodcatOffer, this.subscriberId, this.memberB, CallingCircleRelation.SPONSER_MEMBER, this.fafIndicatorSponsorMember, this.callingCircleExpiry);
			CallingCircleEdr.generateEdr("DELETE", this.prodcatOffer, this.callingCircleExpiry, edrEntry, this.fafIndicatorSponsorMember, "Could not assert existing members in this Calling Circle");
			return false;
		}

		
		
		

		CallingCircle ccRelationship = null; 
		for (String member: members) {
			if (!this.subscriberId.equals(member)) {
				ccRelationship = new CallingCircle(subscriberId, prodcatOffer, subscriberId, memberB, CallingCircleRelation.SPONSER_MEMBER, fafIndicatorSponsorMember, this.callingCircleExpiry);
				this.updateFaf(subscriberId, ccRelationship, "DELETE");
				CallingCircleEdr.generateEdr("DELETE", this.prodcatOffer, this.callingCircleExpiry, ccRelationship, this.fafIndicatorSponsorMember, null);

				ccRelationship = new CallingCircle(subscriberId, prodcatOffer, memberB, subscriberId,CallingCircleRelation.MEMBER_SPONSER, fafIndicatorMemberSponsor, this.callingCircleExpiry);
				this.updateFaf(memberB, ccRelationship, "DELETE");
				CallingCircleEdr.generateEdr("DELETE", this.prodcatOffer, this.callingCircleExpiry, ccRelationship, this.fafIndicatorMemberSponsor, null);
			} else {
				for (String otherMember: members) {
					if (!otherMember.equals(this.subscriberId) && !otherMember.equals(member)) {
						query.setMemberB(memberB);
						try {
							int otherMemberships = ccService.fetchCallingCircleMemberCountForMember(UniqueIdGenerator.generateId(), query);
							
							if (otherMemberships == 0) {
								ccRelationship = new CallingCircle(subscriberId, prodcatOffer, memberB, member, CallingCircleRelation.MEMBER_MEMBER, fafIndicatorMemberMember, this.callingCircleExpiry);
								this.updateFaf(memberB, ccRelationship, "DELETE");
								CallingCircleEdr.generateEdr("DELETE", this.prodcatOffer, this.callingCircleExpiry, ccRelationship, this.fafIndicatorMemberMember, null);
						
								ccRelationship = new CallingCircle(subscriberId, prodcatOffer, member, memberB, CallingCircleRelation.MEMBER_MEMBER, fafIndicatorMemberMember, this.callingCircleExpiry);
								this.updateFaf(member, ccRelationship, "DELETE");
								CallingCircleEdr.generateEdr("DELETE", this.prodcatOffer, this.callingCircleExpiry, ccRelationship, this.fafIndicatorMemberMember, null);
							
							} else {
								ccRelationship = new CallingCircle(subscriberId, prodcatOffer, memberB, member, CallingCircleRelation.MEMBER_MEMBER, fafIndicatorMemberMember);
								CallingCircleEdr.generateEdr("DELETE", this.prodcatOffer, this.callingCircleExpiry, ccRelationship, this.fafIndicatorMemberMember, "B-Party member of other active circle");

								ccRelationship = new CallingCircle(subscriberId, prodcatOffer, member, memberB, CallingCircleRelation.MEMBER_MEMBER, fafIndicatorMemberMember);
								CallingCircleEdr.generateEdr("DELETE", this.prodcatOffer, this.callingCircleExpiry, ccRelationship, this.fafIndicatorMemberMember, "B-Party member of other active circle");
							}
						} catch (PersistenceError e) {
							logger.debug("Cannot fetch member threshold breach. Cannot proceed!!", e);
							ccRelationship = new CallingCircle(subscriberId, prodcatOffer, subscriberId, otherMember, CallingCircleRelation.SPONSER_MEMBER, fafIndicatorSponsorMember, this.callingCircleExpiry);
							CallingCircleEdr.generateEdr("DELETE", this.prodcatOffer, this.callingCircleExpiry, ccRelationship, this.fafIndicatorSponsorMember, "Could not assert existing members in this Calling Circle");
							return false;
						}
					}
				}
			}
		}
		
		this.updateFafAccumulator(this.subscriberId, this.fafAccumulatorId, 0);
		
		return true;
	}
	
	
	

	

	private boolean readSubscriber(String customerId, Map<String, String> metas) {
		try {
			memberBMetas.put("msisdn", customerId);
			memberBMetas.put("SUBSCRIBER_ID", customerId);
			memberBMetas.put("READ_SUBSCRIBER", "PARTIAL_READ_SUBSCRIBER");

			GetAccountDetailsRequest request = new GetAccountDetailsRequest();
			request.setSubscriberNumber(customerId);
			request.setSubscriberNumberNAI(1);
			
			GetAccountDetailsCommand command =  new GetAccountDetailsCommand(request);
			GetAccountDetailsResponse response = command.execute();
			
			this.processAccountDetailsResponse(metas, response);
			
		} catch(Exception e) {
			logger.error("Couldnt fetch subscriber info. Cause: " + e.getMessage(), e);
			if (e instanceof SmException) {
				if (((SmException) e).getStatusCode().getCode() == 102) {
					metas.put("unknownSubscriber", "true");
					return false;
				}
			}
		}

		return true;
	}
	
	private void processAccountDetailsResponse(Map<String, String> accountDetails, GetAccountDetailsResponse response) {
		// direct attributes...
		if (response.getActivationDate() != null)
			accountDetails.put(READ_SUBSCRIBER_ACTIVATION_DATE, "" + response.getActivationDate().getTime());

		if (response.getSupervisionExpiryDate() != null)
			accountDetails.put(READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE, "" + response.getSupervisionExpiryDate().getTime());

		if (response.getServiceFeeExpiryDate() != null)
			accountDetails.put(READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE, "" + response.getServiceFeeExpiryDate().getTime());

		logger.debug("Packed all date attributes...");

		// service offerings
		int index = 0;
		for (com.ericsson.raso.sef.client.air.response.ServiceOffering serviceOffering: response.getServiceOfferings()) {
			logger.debug("extracting service offering: " + serviceOffering);
			accountDetails.put(READ_SUBSCRIBER_SERVICE_OFFERING + "." + ++index, "" + serviceOffering.getServiceOfferingID() + "," + serviceOffering.isServiceOfferingActiveFlag());
		}
		logger.debug("Packed all service offerings...");

		// account flags
		AccountFlags accountFlags = response.getAccountFlags();
		Boolean flag = accountFlags.isActivationStatusFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG, "" + flag);

		flag = accountFlags.isNegativeBarringStatusFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG, "" + flag);

		flag = accountFlags.isSupervisionPeriodWarningActiveFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG, "" + flag);

		flag = accountFlags.isServiceFeePeriodWarningActiveFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG, "" + flag);

		flag = accountFlags.isSupervisionPeriodExpiryFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG, "" + flag);

		flag = accountFlags.isServiceFeePeriodExpiryFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG, "" + flag);

		flag = accountFlags.isTwoStepActivationFlag();
		if (flag != null)
			accountDetails.put(READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG, "" + flag);

		logger.debug("Packed all account flags...");

		if (response.getServiceOfferings() != null) {
			logger.debug("About to process ServiceOfferings. Size: " + response.getServiceOfferings().size());
			index = 0;
			for (ServiceOffering serviceOffering: response.getServiceOfferings()) {
				accountDetails.put(READ_SUBSCRIBER_SERVICE_OFFERING + "." + ++index, serviceOffering.getServiceOfferingID() + 
						"," + serviceOffering.isServiceOfferingActiveFlag());
			}
		}
		
		
		// offer info...
		index = 0;
		if (response.getOfferInformationList() != null) {
			logger.debug("About to process OfferInfoList. Size: " + response.getOfferInformationList().size());
			for (OfferInformation offerInformation: response.getOfferInformationList()) {
				accountDetails.put(READ_SUBSCRIBER_OFFER_INFO + "." + ++index, (offerInformation.getOfferID() + 
						"," + ((offerInformation.getStartDate()==null)?"null":offerInformation.getStartDate().getTime()) + 
						"," + ((offerInformation.getStartDateTime()==null)?"null":offerInformation.getStartDateTime().getTime()) + 
						"," + ((offerInformation.getExpiryDate()==null)?"null":offerInformation.getExpiryDate().getTime()) + 
						"," + ((offerInformation.getExpiryDateTime()==null)?"null":offerInformation.getExpiryDateTime().getTime()) ));
			}
		} else {
			logger.error("DID NOT RECEIVE Offer Info List from UCIP Response!!");
		}
		
		// service class
		//TODO: IGroup UCIP stack does not have this impl...
		
		logger.debug("Packed all offer info..." + accountDetails.toString());

	}
	
	
	public String getFafIndicatorSponsorMember() {
		return fafIndicatorSponsorMember;
	}

	public void setFafIndicatorSponsorMember(String fafIndicatorSponsorMember) {
		this.fafIndicatorSponsorMember = fafIndicatorSponsorMember;
	}

	public String getFafIndicatorMemberSponsor() {
		return fafIndicatorMemberSponsor;
	}

	public void setFafIndicatorMemberSponsor(String fafIndicatorMemberSponsor) {
		this.fafIndicatorMemberSponsor = fafIndicatorMemberSponsor;
	}

	public String getFafIndicatorMemberMember() {
		return fafIndicatorMemberMember;
	}

	public void setFafIndicatorMemberMember(String fafIndicatorMemberMember) {
		this.fafIndicatorMemberMember = fafIndicatorMemberMember;
	}

	public Integer getFafAccumulatorId() {
		return fafAccumulatorId;
	}

	public void setFafAccumulatorId(Integer fafAccumulatorId) {
		this.fafAccumulatorId = fafAccumulatorId;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getProdcatOffer() {
		return prodcatOffer;
	}

	public void setProdcatOffer(String prodcatOffer) {
		this.prodcatOffer = prodcatOffer;
	}

	public String getMemberB() {
		return memberB;
	}

	public void setMemberB(String memberB) {
		this.memberB = memberB;
	}

	public Integer getMaxMembers() {
		return maxMembers;
	}

	public void setMaxMembers(Integer maxMembers) {
		this.maxMembers = maxMembers;
	}

	public String getAssociatedPromo() {
		return associatedPromo;
	}

	public void setAssociatedPromo(String associatedPromo) {
		this.associatedPromo = associatedPromo;
	}

	public String getWelcomeMessageEventId() {
		return welcomeMessageEventId;
	}

	public void setWelcomeMessageEventId(String welcomeMessageEventId) {
		this.welcomeMessageEventId = welcomeMessageEventId;
	}

	public String getA_PartyMemberThresholdBreachMessageEventId() {
		return A_PartyMemberThresholdBreachMessageEventId;
	}

	public void setA_PartyMemberThresholdBreachMessageEventId(String a_PartyMemberThresholdBreachMessageEventId) {
		A_PartyMemberThresholdBreachMessageEventId = a_PartyMemberThresholdBreachMessageEventId;
	}

	public String getNewMemberAddedEventId() {
		return NewMemberAddedEventId;
	}

	public void setNewMemberAddedEventId(String newMemberAddedEventId) {
		NewMemberAddedEventId = newMemberAddedEventId;
	}

	public Integer getCallingCircleCsOfferID() {
		return callingCircleCsOfferID;
	}

	public void setCallingCircleCsOfferID(Integer callingCircleCsOfferID) {
		this.callingCircleCsOfferID = callingCircleCsOfferID;
	}

	public String getFreebieType() {
		return freebieType;
	}

	public void setFreebieType(String freebieType) {
		this.freebieType = freebieType;
	}

	public Integer getFreebieOfferId() {
		return freebieOfferId;
	}

	public void setFreebieOfferId(Integer freebieOfferId) {
		this.freebieOfferId = freebieOfferId;
	}

	public Integer getFreebieOfferType() {
		return freebieOfferType;
	}

	public void setFreebieOfferType(Integer freebieOfferType) {
		this.freebieOfferType = freebieOfferType;
	}

	public Long getFreebieOfferValidity() {
		return freebieOfferValidity;
	}

	public void setFreebieOfferValidity(Long freebieOfferValidity) {
		this.freebieOfferValidity = freebieOfferValidity;
	}

	public String getFreebieRefillID() {
		return freebieRefillID;
	}

	public void setFreebieRefillID(String freebieRefillID) {
		this.freebieRefillID = freebieRefillID;
	}

	public String getFreebiePlanCode() {
		return freebiePlanCode;
	}

	public void setFreebiePlanCode(String freebiePlanCode) {
		this.freebiePlanCode = freebiePlanCode;
	}

	public Integer getFreebieRefillType() {
		return freebieRefillType;
	}

	public void setFreebieRefillType(Integer freebieRefillType) {
		this.freebieRefillType = freebieRefillType;
	}

	public String getFreebieTransactionAmount() {
		return freebieTransactionAmount;
	}

	public void setFreebieTransactionAmount(String freebieTransactionAmount) {
		this.freebieTransactionAmount = freebieTransactionAmount;
	}

	public CurrencyCode getFreebieTransactionCurrency() {
		return freebieTransactionCurrency;
	}

	public void setFreebieTransactionCurrency(CurrencyCode freebieTransactionCurrency) {
		this.freebieTransactionCurrency = freebieTransactionCurrency;
	}

	public String getFreebieRenewalAmount() {
		return freebieRenewalAmount;
	}

	public void setFreebieRenewalAmount(String freebieRenewalAmount) {
		this.freebieRenewalAmount = freebieRenewalAmount;
	}

	public String getFreebiePurchaseAmount() {
		return freebiePurchaseAmount;
	}

	public void setFreebiePurchaseAmount(String freebiePurchaseAmount) {
		this.freebiePurchaseAmount = freebiePurchaseAmount;
	}

	public void setCallingCircleExpiry(long callingCircleExpiry) {
		this.callingCircleExpiry = callingCircleExpiry;
	}

	class SvcOffering {
		private int soID;
		private boolean flag;

		public SvcOffering(int soID, boolean flag) {
			this.soID = soID;
			this.flag = flag;
		}

		public int getSoID() {
			return soID;
		}

		public void setSoID(int soID) {
			this.soID = soID;
		}

		public boolean isFlag() {
			return flag;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}

		@Override
		public String toString() {
			return "ServiceOffering [soID=" + soID + ", flag=" + flag + "]";
		}
	}


	class OfferInfo implements Comparable<OfferInfo>{
		private String offerID;
		private long offerExpiry;
		private long offerStart;
		private String daID;
		private String walletName;

		public OfferInfo() {
		}

		public OfferInfo(String offerID, long offerExpiry, long offerStart, String daID, String walletName) {
			super();
			this.offerID = offerID;
			this.offerExpiry = offerExpiry;
			this.offerStart = offerStart;
			this.daID = daID;
			this.walletName = walletName;
		}

		@Override
		public String toString() {
			return "OfferInfo [offerID=" + offerID + ", offerExpiry=" + offerExpiry + ", daID=" + daID + ", walletName=" + walletName + "]";
		}

		@Override
		public int compareTo(OfferInfo o) {
			int expiry = (int) (this.offerExpiry - o.offerExpiry);
			if (expiry == 0)
				return this.offerID.compareTo(o.offerID);
			else
				return expiry;
		}

	}

	enum NotificationMessageEvent {
		B_PartyUnknown ("800120130000"),
		//B_PartyInvalidState ("800120120000"),
		//A_PartyMemberThresholdBreach (""), -- moved to BC since its unique to each CallingCircle Pack
		//NewMemberAdded (),-- moved to BC since its unique to each CallingCircle Pack
		UpdateFafFailed ("800120130000");
		//FreebieProvisioningFailed () -- this scenario is not handled in prev impl and hence no event ID registered in Notification Manager

		private String eventName;

		private NotificationMessageEvent(String eventName) {
			this.eventName = eventName;
		}

		public String getEventName() {
			return this.eventName;
		}
	}

	@Override
	public String toString() {
		return "CallingCircleProfile [fafIndicatorSponsorMember=" + fafIndicatorSponsorMember
				+ ", fafIndicatorMemberSponsor=" + fafIndicatorMemberSponsor + ", fafIndicatorMemberMember=" + fafIndicatorMemberMember
				+ ", fafAccumulatorId=" + fafAccumulatorId + ", subscriberId=" + subscriberId + ", prodcatOffer=" + prodcatOffer
				+ ", memberB=" + memberB + ", maxMembers=" + maxMembers + ", associatedPromo=" + associatedPromo
				+ ", welcomeMessageEventId=" + welcomeMessageEventId + ", A_PartyMemberThresholdBreachMessageEventId="
				+ A_PartyMemberThresholdBreachMessageEventId + ", NewMemberAddedEventId=" + NewMemberAddedEventId
				+ ", B_PartyInvalidStateEventId=" + B_PartyInvalidStateEventId + ", callingCircleCsOfferID=" + callingCircleCsOfferID
				+ ", freebieType=" + freebieType + ", freebieOfferId=" + freebieOfferId + ", freebieOfferType=" + freebieOfferType
				+ ", freebieOfferValidity=" + freebieOfferValidity + ", freebieRefillID=" + freebieRefillID + ", freebiePlanCode="
				+ freebiePlanCode + ", freebieRefillType=" + freebieRefillType + ", freebieTransactionAmount=" + freebieTransactionAmount
				+ ", freebieTransactionCurrency=" + freebieTransactionCurrency + ", freebieRenewalAmount=" + freebieRenewalAmount
				+ ", freebiePurchaseAmount=" + freebiePurchaseAmount + ", memberBMetas=" + memberBMetas + ", memberAMetas=" + memberAMetas
				+ ", callingCircleExpiry=" + callingCircleExpiry + ", [Refill Inheritance:=" + super.toString() + "]]";
	}

	private static final String READ_SUBSCRIBER_ACTIVATION_DATE = "READ_SUBSCRIBER_ACTIVATION_DATE";
	private static final String READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE = "READ_SUBSCRIBER_SUPERVISION_EXPIRY_DATE";
	private static final String READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE = "READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE";
	private static final String	READ_SUBSCRIBER_SERVICE_OFFERING	= "READ_SUBSCRIBER_SERVICE_OFFERING";
	private static final String READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG = "READ_SUBSCRIBER_ACTIVATION_STATUS_FLAG";
	private static final String READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG = "READ_SUBSCRIBER_NEGATIVE_BARRING_STATUS_FLAG";
	private static final String READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG = "READ_SUBSCRIBER_SUPERVISION_PERIOD_WARNING_ACTIVE_FLAG";
	private static final String READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG = "READ_SUBSCRIBER_SERVICE_FEE_PERIOD_WARNING_ACTIVE_FLAG";
	private static final String READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG = "READ_SUBSCRIBER_SUPERVISION_PERIOD_EXPIRY_FLAG";
	private static final String READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG = "READ_SUBSCRIBER_SERVICE_FEE_PERIOD_EXPIRY_FLAG";
	private static final String READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG = "READ_SUBSCRIBER_TWO_STEP_ACTIVATION_FLAG";
	private static final String	READ_SUBSCRIBER_OFFER_INFO	= "READ_SUBSCRIBER_OFFER_INFO";

	

}
