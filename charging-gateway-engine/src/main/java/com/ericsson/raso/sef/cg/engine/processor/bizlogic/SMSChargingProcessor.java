package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.rmi.transport.proxy.CGIHandler;

import com.ericsson.pps.diameter.dccapi.avp.CCRequestNumberAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCRequestTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.GrantedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.MultipleServicesCreditControlAvp;
import com.ericsson.pps.diameter.dccapi.avp.RequestedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.SubscriptionIdAvp;
import com.ericsson.pps.diameter.dccapi.avp.UsedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.ValidityTimeAvp;
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
import com.ericsson.raso.sef.core.cg.nsn.avp.sms.AdressAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.sms.MMSInformationAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.sms.SMSInformationAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.sms.ServiceInfoAvp;


public class SMSChargingProcessor implements Processor {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	//SefScapChargingApi sefScapChargingApi = null;
	@Override
	public void process(Exchange exchange) throws Exception {
		log.debug(String.format("Enter SMSChargingProcessor.process. exchange is %s", exchange));
		ChargingRequest request = (ChargingRequest) exchange.getIn().getBody();	
		
		Ccr sourceCcr = request.getSourceCcr();
		Ccr scapCcr = toScapCcr(sourceCcr, request);

		Cca cca = scapCcr.send();

		ChargingInfo response = new ChargingInfo();
		response.setUniqueMessageId(request.getMessageId());
		response.setSessionId(request.getSessionId());

		List<Avp> resultAvps = toNsnAnswer(cca, response, request);
		response.setAvpList(resultAvps);

		exchange.getOut().setBody(response);
		log.debug("End SMSChargingProcessor.process");
	}

	protected Ccr toScapCcr(Ccr sourceCcr, ChargingRequest request) throws Exception {
		log.debug(String.format("Enter SMSChargingProcessor.toScapCcr. sourceCcr is %s, request is %s", 
				sourceCcr, request));
	//	 sefScapChargingApi = new SefScapChargingApi();
		Ccr scapCcr = CgEngineContext.getChargingApi().createScapCcr(sourceCcr.getSessionId(), request.getHostId());

		scapCcr.setServiceContextId("SCAP_V.2.0@ericsson.com");
		scapCcr.setServiceIdentifier(7004);
		scapCcr.setCCRequestType(sourceCcr.getCCRequestType());
		scapCcr.setCCRequestNumber(sourceCcr.getCCRequestNumber());
		scapCcr.setEventTimestamp(sourceCcr.getEventTimestamp());
		scapCcr.addAvp(new TimeZoneAvp((byte) 11, (byte) 0, (byte) 0));

		Avp subscriptionIdAvp = sourceCcr.getAvp(SubscriptionIdAvp.AVP_CODE);
		if (subscriptionIdAvp != null) {
			scapCcr.addAvp(subscriptionIdAvp);
		}

		if (sourceCcr.getRequestedServiceUnit() != null) {
			log.error("Diameter Contradicting Avps:" + sourceCcr.getRequestedServiceUnit());
			throw new SmException(ResponseCode.DIAMETER_CONTRADICTING_AVPS);
		}
		Avp[] msccAvps = sourceCcr.getMultipleServicesCreditControlArray();
		if (msccAvps.length > 1) {
			log.error("Multiple values for multipleRequestUnits found; number:" + msccAvps.length);
			throw new SmException(ResponseCode.DIAMETER_CONTRADICTING_AVPS);
		}

		if (sourceCcr.getCCRequestType() == 1) {
			// Initial Request
			MultipleServicesCreditControlAvp msccAvp = new MultipleServicesCreditControlAvp(msccAvps[0]);
			RequestedServiceUnitAvp rsuAvp = new RequestedServiceUnitAvp(msccAvp.getSubAvp(437));
			scapCcr.addAvp(rsuAvp);

		} else if (sourceCcr.getCCRequestType() == 3) {
			// Termination Request
			MultipleServicesCreditControlAvp msccAvp = new MultipleServicesCreditControlAvp(msccAvps[0]);
			UsedServiceUnitAvp usuAvp = new UsedServiceUnitAvp(msccAvp.getSubAvp(446));
			scapCcr.addAvp(usuAvp);
		} else {
			log.error("Diameter CC Request type is invalid, RequestType:" + sourceCcr.getCCRequestType());
			throw new SmException(ResponseCode.DIAMETER_UNABLE_TO_COMPLY);
		}

		// Check on multiple requested unit.
		if (sourceCcr.getAvp(ServiceInfoAvp.AVP_CODE) == null) {
			log.error("Service Info not found in Diameter request");
			throw new SmException(ResponseCode.DIAMETER_MISSING_AV);
		}
		ServiceInfoAvp serviceInfoAvp = new ServiceInfoAvp(sourceCcr.getAvp(ServiceInfoAvp.AVP_CODE));
		SMSInformationAvp smsInformation = serviceInfoAvp.getSMSInformationAvp();
		if (smsInformation == null) {
			log.error("Service Information AVP is empty in Diameter request");
			throw new SmException(ResponseCode.DIAMETER_INVALID_AVP_VALUE);
		}

		String originatorSCCPAddress = smsInformation.getOriginatorSCCPAddress();

		if (originatorSCCPAddress != null) {
			SubscriptionIdLocationAvp subLocAvp = new SubscriptionIdLocationAvp(originatorSCCPAddress);
			scapCcr.addAvp(subLocAvp);
		}


		if (smsInformation.getSMSCAddress() != null) {
			scapCcr.addAvp(smsInformation.getSMSCAddress());
		}
		if (smsInformation.getSMMessageType() != null) {
			scapCcr.addAvp(smsInformation.getSMMessageType());
		}
		if (smsInformation.getOriginatorInterfaceType() != null) {
			scapCcr.addAvp(smsInformation.getOriginatorInterfaceType());
		}

		MMSInformationAvp mmsInfoAvp = serviceInfoAvp.getMMSInformationAvp();
		if (mmsInfoAvp == null) {
			log.error("MMS Information AVP is not found in Diameter request");
			throw new SmException(ResponseCode.DIAMETER_MISSING_AV);
		}
		AdressAvp oriAddressAvp = mmsInfoAvp.getOriginatorAddressAvp();
		AdressAvp recAddressAvp = mmsInfoAvp.getRecipientAddressAvp();

		if (oriAddressAvp == null || recAddressAvp == null) {
			log.error("MMS Information AVP is empty in Diameter request");
			throw new SmException(ResponseCode.DIAMETER_INVALID_AVP_VALUE);
		}

		if (oriAddressAvp.getOriginAddressTypeAvp() != null) {
			scapCcr.addAvp(oriAddressAvp.getOriginAddressTypeAvp());
		}
		if (oriAddressAvp.getOriginAdressDataAvp() != null) {
			scapCcr.addAvp(oriAddressAvp.getOriginAdressDataAvp());
		}
		if (recAddressAvp.getRecipientAddressTypeAvp() != null) {
			scapCcr.addAvp(recAddressAvp.getRecipientAddressTypeAvp());
		}
		if (recAddressAvp.getRecipientAdressDataAvp() != null) {
			scapCcr.addAvp(recAddressAvp.getRecipientAdressDataAvp());
		}

		if (mmsInfoAvp.geMessageIdAvp() != null) {
			scapCcr.addAvp(mmsInfoAvp.geMessageIdAvp());
		}
		log.debug("End SMSChargingProcessor.toScapCcr");
		return scapCcr;
	}

	protected List<Avp> toNsnAnswer(Cca cca, ChargingInfo response, ChargingRequest chargingRequest)
			throws AvpDataException {
		log.debug(String.format("Enter SMSChargingProcessor.toNsnAnswer. cca is %s, response is %s, chargingRequest is %s", 
				cca, response, chargingRequest));
		List<Avp> answerAvp = new ArrayList<Avp>();
		ResultCodeAvp resultCodeAvp = new ResultCodeAvp(cca.getResultCode());
		answerAvp.add(resultCodeAvp);
		answerAvp.add(new OriginHostAvp(cca.getOriginHost()));
		answerAvp.add(new OriginRealmAvp(cca.getOriginRealm()));
		answerAvp.add(new AuthApplicationIdAvp(cca.getAuthApplicationId()));
		answerAvp.add(new CCRequestNumberAvp(cca.getCCRequestNumber()));
		answerAvp.add(new CCRequestTypeAvp(cca.getCCRequestType()));
		response.setResultCodeAvp(resultCodeAvp);

		MultipleServicesCreditControlAvp msccAvp = new MultipleServicesCreditControlAvp();
		if(cca.getAvp(GrantedServiceUnitAvp.AVP_CODE) !=null){
			GrantedServiceUnitAvp grantedSerUnitAvp = new GrantedServiceUnitAvp(cca.getAvp(GrantedServiceUnitAvp.AVP_CODE));
			if (grantedSerUnitAvp != null) {
				msccAvp.addSubAvp(grantedSerUnitAvp);
			}
		}
		msccAvp.addSubAvp(resultCodeAvp);
		if(cca.getAvp(ValidityTimeAvp.AVP_CODE)!=null){
			ValidityTimeAvp validityTimeAvp = new ValidityTimeAvp(cca.getAvp(ValidityTimeAvp.AVP_CODE));
			if (validityTimeAvp != null) {
				msccAvp.addSubAvp(validityTimeAvp);
			}
		}
		answerAvp.add(msccAvp);
		log.debug("End SMSChargingProcessor.toNsnAnswer");
		return answerAvp;
	}
}
