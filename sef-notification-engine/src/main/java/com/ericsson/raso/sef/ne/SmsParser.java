package com.ericsson.raso.sef.ne;

import java.util.List;

import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.ne.core.smpp.SmppMessage;

public interface SmsParser {

	List<Meta> getMetas();

	SmppMessage getSms();

	boolean isValidSm();

	List<String> getMessages();

	String getMsisdn();
	
	String getEventId();

}
