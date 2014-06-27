package com.ericsson.raso.sef.ne.core.smpp.internal;

import java.util.Date;

import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.Session;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.ne.core.config.SourceSmppAddress;
import com.ericsson.raso.sef.ne.core.config.TargetSmppAddress;
import com.ericsson.raso.sef.ne.core.endpoint.SmppClientEndpoint;
import com.ericsson.raso.sef.ne.core.smpp.SmppClient;

public class JSmppClient implements SmppClient {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private SmppClientEndpoint configuration;

	private SMPPSession session;
	
	private SourceSmppAddress source;
	private TargetSmppAddress destination;
	
	
	public JSmppClient(SmppClientEndpoint configuration, SourceSmppAddress source, TargetSmppAddress destination) {
		this.configuration = configuration;
		this.source = source;
		this.destination = destination;
	}

	
	public JSmppClient() {
		
	}

	
	public void start() throws Exception {
		try {
			session = new SMPPSession();
			String host ="";
			int port = 0;
			String systemId ="";
			String password ="";
			session.connectAndBind(host,port,new BindParameter(BindType.BIND_TX, systemId,password, "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
	        session.setMessageReceiverListener(new MessageReceiverListener() {
	            public void onAcceptDeliverSm(DeliverSm deliverSm)
	                    throws ProcessRequestException {
	                    try {
	                        DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
	                        long id = Long.parseLong(delReceipt.getId()) & 0xffffffff;
	                        String messageId = Long.toString(id, 16).toUpperCase();
	                        log.info("Receiving delivery receipt for message '" + messageId + "' : " + delReceipt);
	                    } catch (InvalidDeliveryReceiptException e) {
	                    	log.error("Failed getting delivery receipt", e);
	                    }
	             }

				public DataSmResult onAcceptDataSm(DataSm paramDataSm,
						Session paramSession) throws ProcessRequestException {
					log.info(paramDataSm.toString());
					return null;
				}

				public void onAcceptAlertNotification(
						AlertNotification paramAlertNotification) {
					log.info(paramAlertNotification.toString());
					
				} 
	            });
			
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public void destroy() {
        session.unbindAndClose();
//		session.destroy();
	}

	public void sendMessage(String msisdn, String message, String senderAddr) throws Exception {
		
        OptionalParameter[] opts = new OptionalParameter[]{};
		String messageId = session.submitShortMessage("CMT", 
        					TypeOfNumber.valueOf(source.getTon()), NumberingPlanIndicator.valueOf(source.getNpi()), source.getAddress(),
        					TypeOfNumber.valueOf(destination.getTon()), NumberingPlanIndicator.valueOf(destination.getNpi()), msisdn,
        					new ESMClass(), (byte)0, (byte)1,  new AbsoluteTimeFormatter().format(new Date()), null,
        					new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), (byte)0, new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false),
        					(byte)0, message.getBytes(),opts);
		
		log.debug("Sent Message to SMSC;MessageID:"+messageId+";MessageText:"+message);
		
	}
}
