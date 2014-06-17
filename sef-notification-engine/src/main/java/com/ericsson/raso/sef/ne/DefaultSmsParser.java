package com.ericsson.raso.sef.ne;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.ne.core.smpp.SmppMessage;
import com.ericsson.raso.sef.ne.notification.NotificationEvent;

public class DefaultSmsParser implements SmsParser {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private SmppMessage submitSm;

	private List<String> messages = new ArrayList<String>();
	private String msisdn;
	private boolean isValidSm;
	private String eventId = "0";
	private List<Meta> metas = new ArrayList<Meta>();

	public DefaultSmsParser(SmppMessage submitSm) {
		this.submitSm = submitSm;
		parse();
	}

	
	public String getEventId() {
		return eventId;
	}

	public List<Meta> getMetas() {
		return metas;
	}

	public SmppMessage getSms() {
		return submitSm;
	}

	public boolean isValidSm() {
		return isValidSm;
	}

	public List<String> getMessages() {
		return messages;
	}

	private void parse() {
		String body = submitSm.getMessageBody();
		log.info("Message Body:: " + body);
		msisdn = submitSm.getDestinationMsisdn();
		generateEventIdAndMetas(body);
	}

	public String getMsisdn() {
		return msisdn;
	}

	private void generateEventIdAndMetas(String body) {
		try {
			String[] data = body.trim().split(",");
			eventId = data[0];
			if(eventId.equalsIgnoreCase(NotificationEvent.DEFAULT_EVENTID)) {
				messages.add(body.replaceFirst("0,", ""));
			} else {
				for (int i = 1; i < data.length; i++) {
					if(data[i].isEmpty()) continue;
					String[] str = data[i].trim().split(":");
					Meta meta = new Meta(str[0].trim(), str[1].trim().replaceAll("\\{#}",", "));
					metas.add(meta);
				}
			}
			isValidSm = true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			isValidSm = false;
		}
	}

}
