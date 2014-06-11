package com.ericsson.raso.sef.ne;

import java.util.List;

import com.ericsson.raso.ne.core.smpp.SmppMessage;
import com.ericsson.raso.sef.core.Meta;

public interface SmsParser {

	List<Meta> getMetas();

	SmppMessage getSms();

	boolean isValidSm();

	List<String> getMessages();

	String getMsisdn();
	
	String getEventId();

}
