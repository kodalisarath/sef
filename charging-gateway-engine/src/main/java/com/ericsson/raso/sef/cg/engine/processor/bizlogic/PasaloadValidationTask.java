package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.GetBalanceAndDateCommand;
import com.ericsson.raso.sef.client.air.request.GetBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.response.DedicatedAccountInformation;
import com.ericsson.raso.sef.client.air.response.GetBalanceAndDateResponse;
import com.ericsson.raso.sef.core.DateUtil;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.Task;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.model.SubscriberAuditTrial;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.SubscriberService;

public class PasaloadValidationTask implements Task<Void> {

	private static Logger log = LoggerFactory.getLogger(PasaloadValidationTask.class);
	public static final int AIRTIME_DA_ID = 1;
	private String msisdn;
	private long amountOfUnits;

	public PasaloadValidationTask(String msisdn, long amountOfUnits) {
		this.msisdn = msisdn;
		this.amountOfUnits = amountOfUnits;
	}

	@Override
	public Void execute() throws SmException {
		log.debug("Enter PasaloadValidationTask.execute");
		GetBalanceAndDateRequest request = new GetBalanceAndDateRequest();
		request.setSubscriberNumber(msisdn);

		GetBalanceAndDateResponse response = new GetBalanceAndDateCommand(request).execute();

		List<DedicatedAccountInformation> daList = response.getDedicatedAccountInformation();

		long balance = 0l;
		for (DedicatedAccountInformation da : daList) {
			if (da.getDedicatedAccountID().intValue() == AIRTIME_DA_ID) {
				balance = Double.valueOf(da.getDedicatedAccountValue1()).longValue();
			}
		}

		//SubscriberService subscriberService = CgEngineContext.getSubscriberService();
		
		SubscriberService subscriberService = SefCoreServiceResolver.getSusbcriberStore();
		Subscriber subscriber;
		try {
			subscriber = subscriberService.getSubscriber("pasaload:money",msisdn);
		} catch (PersistenceError e) {
			// TODO Auto-generated catch block
			log.error("Exception capured PasaloadValidationTask :execute getSubscriber ", e);
			throw new SmException(e);
		}


		long pasaBalance = 0l;
		
		
		Collection<SubscriberAuditTrial> histories;
		try {
			histories = subscriberService.getSubscriberHistory("pasaload:money",subscriber.getUserId(),null);
		} catch (PersistenceError e) {
			// TODO Auto-generated catch block
			log.error("Exception capured PasaloadValidationTask :execute getSubscriberHistory ", e);
			throw new SmException(e);
		}
		for (SubscriberAuditTrial subscriberAuditTrial : histories) {
			if (DateUtil.isSameDay(subscriberAuditTrial.getEventTimestamp(), new Date())){
				pasaBalance += Double.valueOf(subscriberAuditTrial.getAttributeNewValue()).longValue();
			}
		}
		
		if(balance - pasaBalance < amountOfUnits) {
			log.error("Pasaload:: Cannot transfer the amount recieved during the day.");
			throw new SmException(com.ericsson.raso.sef.cg.engine.ResponseCode.SUBSCRIBER_INSUFFICIENT_BALANCE);
		}
		log.debug("End PasaloadValidationTask.execute");
		return null;
	}
}
