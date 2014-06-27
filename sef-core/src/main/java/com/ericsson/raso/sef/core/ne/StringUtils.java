package com.ericsson.raso.sef.core.ne;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Meta;

public class StringUtils 
{

	private static Logger log = LoggerFactory.getLogger(StringUtils.class);
	public static String prepareMessage(String message, List<Meta> metas) {
		String msg = message;
		for (Meta meta : metas) {
			msg = msg.replace("{" +meta.getKey()+ "}", meta.getValue());
		}
		return msg;
	}
	
	public static void main(String[] args) {
		String template = "Dear subscriber, your {offerId} has been expired now. Please purchase {offerId} to avail UNLIMITED offer.";
		List<Meta> metas = new ArrayList<Meta>();
		metas.add(new Meta("offerId", "Yahoo"));
		
		log.debug(prepareMessage(template, metas));
 	}
}
