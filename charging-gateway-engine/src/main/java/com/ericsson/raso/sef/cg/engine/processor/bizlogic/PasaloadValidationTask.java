package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.Task;
import com.ericsson.raso.sef.smart.PasaServiceManager;

public class PasaloadValidationTask implements Task<Void> {

	private static Logger log = LoggerFactory.getLogger(PasaloadValidationTask.class);
	public static final int AIRTIME_DA_ID = 1;
	private String msisdn;
	private long amountOfUnits;
	private String handle;

	public PasaloadValidationTask(String msisdn, long amountOfUnits, String handle) {
		this.msisdn = msisdn;
		this.amountOfUnits = amountOfUnits;
		this.handle = handle;
	}

	@Override
	public Void execute() throws SmException {
		log.debug("Enter PasaloadValidationTask.execute :msisdn: "+msisdn  +" ,handle: "+handle +" ,amountofUnits: "+amountOfUnits);
		
		boolean isPasaSendAllowed = PasaServiceManager.getInstance().isPasaSendAllowed(msisdn,handle, amountOfUnits );
		log.debug("Enter PasaloadValidationTask.execute isPasaSendAllowed "+isPasaSendAllowed);
		if(!isPasaSendAllowed)
			throw new SmException(ResponseCode.SUBSCRIBER_INSUFFICIENT_BALANCE);
		
		/*GetBalanceAndDateRequest request = new GetBalanceAndDateRequest();
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
*/		return null;
	}
}
