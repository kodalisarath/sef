package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.dccapi.avp.CCRequestNumberAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCRequestTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCServiceSpecificUnitsAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCTotalOctetsAvp;
import com.ericsson.pps.diameter.dccapi.avp.GrantedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.MultipleServicesCreditControlAvp;
import com.ericsson.pps.diameter.dccapi.avp.RequestedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.SubscriptionIdAvp;
import com.ericsson.pps.diameter.dccapi.command.Cca;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.avp.AuthApplicationIdAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.pps.diameter.rfcapi.base.avp.OriginHostAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.OriginRealmAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.ResultCodeAvp;
import com.ericsson.pps.diameter.scapv2.avp.SubscriptionIdLocationAvp;
import com.ericsson.pps.diameter.scapv2.avp.TimeZoneAvp;
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.ResponseCode;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.nsn.avp.mms.AdressAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.mms.MMSInformationAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.mms.PsInformationAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.mms.ServiceInfoAvp;

public class MMSChargingProcessor implements Processor {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		log.debug(String.format("Enter MMSChargingProcessor.process. exchange is %s", exchange));
		log.debug("MMSChargingProcessor  Inside ProcessMethod");
		ChargingRequest request = (ChargingRequest) exchange.getIn().getBody();	
		
		Ccr sourceCcr = request.getSourceCcr();
		log.debug("MMSChargingProcessor  Inside sourceCcr "+sourceCcr);
		Ccr scapCcr = toScapCcr(sourceCcr,request);
		log.debug("MMSChargingProcessor  Inside scapCcr "+scapCcr);

		log.error("REQ.OUT: " + scapCcr);
		Cca cca = scapCcr.send();
		log.error("RES.IN: " + cca);

		log.debug("MMSChargingProcessor  Inside cca received is "+cca);
		ChargingInfo response = new ChargingInfo();
		response.setUniqueMessageId(request.getMessageId());
		response.setSessionId(request.getSessionId());

		List<Avp> resultAvps = toNsnAnswer(cca, response,request);
		response.setAvpList(resultAvps);
		log.debug("MMSChargingProcessor  Inside ProcessMethod response is "+resultAvps);
		exchange.getOut().setBody(response);
		log.debug("End MMSChargingProcessor.process");
	}

	protected Ccr toScapCcr(Ccr sourceCcr, ChargingRequest request) throws Exception {
		
		log.debug("MMSChargingProcessor  Inside toScapCcr method");
		log.debug(String.format("Enter MMSChargingProcessor.toScapCcr sourceCcr is %s, request is %s", sourceCcr, request));
		Ccr scapCcr = CgEngineContext.getChargingApi().createScapCcr(sourceCcr.getSessionId(), request.getHostId());

		scapCcr.setServiceContextId("SCAP_V.2.0@ericsson.com");
		scapCcr.setServiceIdentifier(7003); 
		scapCcr.setCCRequestType(sourceCcr.getCCRequestType());
		scapCcr.setCCRequestNumber(sourceCcr.getCCRequestNumber());
		scapCcr.setEventTimestamp(sourceCcr.getEventTimestamp());
		scapCcr.addAvp(new TimeZoneAvp((byte) 11, (byte) 0, (byte) 0));

		Avp subscriptionIdAvp = sourceCcr.getAvp(SubscriptionIdAvp.AVP_CODE);
		if (subscriptionIdAvp != null) {
			scapCcr.addAvp(subscriptionIdAvp);
		}

		if(sourceCcr.getRequestedAction()==1){
			log.error("Requested Action is not as per the specification, Requested Action:"+sourceCcr.getRequestedAction());
			throw new SmException(ResponseCode.DIAMETER_UNABLE_TO_COMPLY);
		}
		scapCcr.setRequestedAction(sourceCcr.getRequestedAction());

		//Check on multiple requested unit.
		if (sourceCcr.getAvp(ServiceInfoAvp.AVP_CODE)==null) { 
			log.error("Service Info not found in Diameter request");
			throw new SmException(ResponseCode.DIAMETER_MISSING_AV); 
		}

		ServiceInfoAvp serviceInfoAvp = new ServiceInfoAvp(sourceCcr.getAvp(ServiceInfoAvp.AVP_CODE));

		PsInformationAvp psInformation = serviceInfoAvp.getPsInformationAvp(); 
		if(psInformation!=null){
			scapCcr.addAvp(psInformation.get3GPPChargingId());
			SubscriptionIdLocationAvp subLocAvp = new SubscriptionIdLocationAvp(psInformation.get3GPPUserLocationInfo().getAsUTF8String());
			scapCcr.addAvp(subLocAvp);
		}

		MMSInformationAvp mmsInfoAvp = serviceInfoAvp.getMMSInformationAvp();
		if(mmsInfoAvp == null) {
			log.error("MMS Information AVP is not found in Diameter request");
			throw new SmException(ResponseCode.DIAMETER_MISSING_AV); 
		}

		AdressAvp oriAddressAvp = mmsInfoAvp.getOriginatorAddressAvp();
		AdressAvp recAddressAvp = mmsInfoAvp.getRecipientAddressAvp();

		if(oriAddressAvp == null || recAddressAvp == null) {
			log.error("MMS Information AVP is empty in Diameter request");
			throw new SmException(ResponseCode.DIAMETER_INVALID_AVP_VALUE); 
		}


		if(oriAddressAvp.getOriginAddressTypeAvp() !=null){
			scapCcr.addAvp(oriAddressAvp.getOriginAddressTypeAvp());
		}
		if(oriAddressAvp.getOriginAdressDataAvp()!=null){
			scapCcr.addAvp(oriAddressAvp.getOriginAdressDataAvp());
		}
		if(recAddressAvp.getRecipientAddressTypeAvp()!=null){
			scapCcr.addAvp(recAddressAvp.getRecipientAddressTypeAvp());
		}
		if(recAddressAvp.getRecipientAdressDataAvp()!=null){
			scapCcr.addAvp(recAddressAvp.getRecipientAdressDataAvp());
		}
		if(recAddressAvp.getRecipientAdressDomainNameAvp()!=null){
			scapCcr.addAvp(recAddressAvp.getRecipientAdressDomainNameAvp());
		}
		if(mmsInfoAvp.geMessageIdAvp()!=null){
			scapCcr.addAvp(mmsInfoAvp.geMessageIdAvp());
		}
		if(mmsInfoAvp.getMessageSizeAvp()!=null){
			RequestedServiceUnitAvp serviceUnitAvp = new RequestedServiceUnitAvp();
			CCTotalOctetsAvp avp = new CCTotalOctetsAvp(mmsInfoAvp.getMessageSizeAvp().getAsInt());
			serviceUnitAvp.addSubAvp(avp);
			scapCcr.addAvp(serviceUnitAvp);
		}
		log.debug("MMSChargingProcessor  toScapCcr method Ended "+scapCcr);
		return scapCcr;
	}

	protected List<Avp> toNsnAnswer(Cca cca, ChargingInfo response,ChargingRequest chargingRequest) throws AvpDataException {
	
		
		log.debug("MMSChargingProcessor  Inside toNsnAnswer method");
		log.debug(String.format("Enter MMSChargingProcessor.toScapCcr cca is %s, response is %s, chargingRequest is %s", cca, response, chargingRequest));
		List<Avp> answerAvp = new ArrayList<Avp>();
		answerAvp.add(new ResultCodeAvp(cca.getResultCode()));
		answerAvp.add(new OriginHostAvp(cca.getOriginHost()));
		answerAvp.add(new OriginRealmAvp(cca.getOriginRealm()));
		answerAvp.add(new AuthApplicationIdAvp(cca.getAuthApplicationId()));
		answerAvp.add(new CCRequestNumberAvp(cca.getCCRequestNumber()));
		answerAvp.add(new CCRequestTypeAvp(cca.getCCRequestType()));

		response.setResultCodeAvp(new ResultCodeAvp(cca.getResultCode()));


		if(cca.getAvp(GrantedServiceUnitAvp.AVP_CODE)!=null){
			GrantedServiceUnitAvp grantedSerUnitAvp = new GrantedServiceUnitAvp(
					cca.getAvp(GrantedServiceUnitAvp.AVP_CODE));
			if (grantedSerUnitAvp != null) {
				CCTotalOctetsAvp ccTotalOctetsAvp = new CCTotalOctetsAvp(grantedSerUnitAvp.getSubAvp(CCTotalOctetsAvp.AVP_CODE));
				if(ccTotalOctetsAvp!=null){
					MultipleServicesCreditControlAvp msccAvp = new MultipleServicesCreditControlAvp();
					GrantedServiceUnitAvp gsuAvp = new GrantedServiceUnitAvp();
					CCServiceSpecificUnitsAvp ccsuAvp = new CCServiceSpecificUnitsAvp();
					ccsuAvp.setData(ccTotalOctetsAvp.getAsLong());
					gsuAvp.addSubAvp(ccsuAvp);
					msccAvp.addSubAvp(gsuAvp);
					answerAvp.add(msccAvp);
				}
			}
		}
		
		log.debug("MMSChargingProcessor  Inside toNsnAnswer method answerAvp "+answerAvp);
		return answerAvp;
	}
}
