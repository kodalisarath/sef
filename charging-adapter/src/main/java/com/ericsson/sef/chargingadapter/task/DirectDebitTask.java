package com.ericsson.sef.chargingadapter.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.csapi.schema.parlayx.common.v2_1.ChargingInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ericsson.pps.diameter.dccapi.avp.CCMoneyAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCRequestTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.CurrencyCodeAvp;
import com.ericsson.pps.diameter.dccapi.avp.RequestedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterInfoAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterValueAvp;
import com.ericsson.pps.diameter.dccapi.avp.SubscriptionIdAvp;
import com.ericsson.pps.diameter.dccapi.avp.SubscriptionIdDataAvp;
import com.ericsson.pps.diameter.dccapi.avp.SubscriptionIdTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.UnitValueAvp;
import com.ericsson.pps.diameter.dccapi.command.Cca;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.Time;
import com.ericsson.pps.diameter.scapv2.avp.TimeZoneAvp;
import com.ericsson.pps.diameter.scapv2.avp.TrafficCaseAvp;
import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.Task;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.lb.Member;
import com.ericsson.sef.chargingadapter.CaContext;
import com.ericsson.sef.diameter.DiameterErrorCode;

public class DirectDebitTask implements Task<Void> {
	
	private static Logger log = LoggerFactory.getLogger(DirectDebitTask.class);

	private String requestId;
	private String msisdn;
	private ChargingInformation chargingInformation;
	
	public DirectDebitTask(String requestId, String msisdn, ChargingInformation chargingInformation) {
		this.requestId = requestId;
		this.msisdn = msisdn;
		this.chargingInformation = chargingInformation;
	}

	@Override
	public Void execute() throws SmException, PersistenceError {
		log.error("...Processing DirectDebitTask....");
		Map<String, String> metas = toMap(chargingInformation.getDescription());
		/*Subscriber subscriber = CaContext.getSubscriberService().getSubscriber(requestId, msisdn);
		String site = subscriber.getMetaValue(Subscriber.subscriberSite);*/
		String site = "MANILA";
		Member route = CaContext.getChargingApi().getLoadBalancerPool().getMemberBySite(site);
		log.error("HostId: " + route.getHostId());
		Ccr ccr = CaContext.getChargingApi().createScapCcr(requestId, route.getHostId());
		ccr.setEventTimestamp(new Time(new Date(System.currentTimeMillis())));
		ccr.setCCRequestNumber(0);
		ccr.setCCRequestType(CCRequestTypeAvp.EVENT_REQUEST);
		ccr.setRequestedAction(0);
		
		SubscriptionIdAvp subscriptionIdAvp = new SubscriptionIdAvp();
		subscriptionIdAvp.addSubAvp(new SubscriptionIdTypeAvp(SubscriptionIdTypeAvp.END_USER_E164));
		subscriptionIdAvp.addSubAvp(new SubscriptionIdDataAvp(msisdn));
		ccr.addAvp(subscriptionIdAvp);

		ServiceParameterInfoAvp spinfo = new ServiceParameterInfoAvp();
		spinfo.addSubAvp(new ServiceParameterTypeAvp(203));
		log.error("Channel Name :" + metas.get(Constants.CHANNEL_NAME));
		spinfo.addSubAvp(new ServiceParameterValueAvp(metas.get(Constants.CHANNEL_NAME).getBytes()));
		//spinfo.addSubAvp(new ServiceParameterValueAvp("MOBILE".getBytes()));
		ccr.addAvp(spinfo);
		
		ccr.setServiceIdentifier(7002);
		
		ccr.addAvp(new TrafficCaseAvp(21));
		ccr.addAvp(new TimeZoneAvp((byte) 3, (byte) 0, (byte) 0));
		
		CCMoneyAvp moneyAvp = new CCMoneyAvp();
		moneyAvp.addSubAvp(new CurrencyCodeAvp(608));
		UnitValueAvp unitValueAvp = new UnitValueAvp();
		unitValueAvp.addExponent(0);
		unitValueAvp.addValueDigits(chargingInformation.getAmount().longValue());
		moneyAvp.addSubAvp(unitValueAvp);

		RequestedServiceUnitAvp requestedServiceUnitAvp = new RequestedServiceUnitAvp();
		requestedServiceUnitAvp.addSubAvp(moneyAvp);	
		ccr.addAvp(requestedServiceUnitAvp);
		
		ccr.setServiceIdentifier(7002);
		
		try {
			Cca cca = ccr.send();
			if (cca.getResultCode() != DiameterErrorCode.DIAMETER_SUCCESS.getCode()) {
				throw new SmException(new ResponseCode(cca.getResultCode().intValue(), "Direct debit failure"));
			}
			else if(cca.getResultCode()==2001){
				log.error("Success DirectDebit request");
			}
		} catch (SmException s) {
			throw s;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SmException(e);
		}
		return null;
	}
	
	private Map<String, String> toMap(String description) {
		String[] entries = StringUtils.delimitedListToStringArray(description, "|");
		Map<String, String> map = new HashMap<String, String>();
		for (String str : entries) {
			String[] entry =  StringUtils.delimitedListToStringArray(str, ";");
			if(entry.length == 2) {
				map.put(entry[0], entry[1]);
			}
		}
		return map;
	}
}
