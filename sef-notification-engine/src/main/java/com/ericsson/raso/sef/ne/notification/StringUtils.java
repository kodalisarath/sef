package com.ericsson.raso.sef.ne.notification;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.raso.sef.core.Meta;

public class StringUtils {
	
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
		
		System.out.println(prepareMessage(template, metas));
 	}
}
