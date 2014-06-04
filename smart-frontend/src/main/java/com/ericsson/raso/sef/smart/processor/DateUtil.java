package com.ericsson.raso.sef.smart.processor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;

public class DateUtil {
	
	private static final String DEFAULT_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

	public static String convertISOToSimpleDateFormat(String dateStr) {
		SimpleDateFormat formatter, simpleDateFormatter;
		String simpleDateStr = "";
		formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		IConfig config = SefCoreServiceResolver.getConfigService();
		String simpleDateFromat = config.getValue("GLOBAL", "dateFormat");
		Date date = null;
		try {
			date = formatter.parse(dateStr.substring(0, 24));
			simpleDateFormatter = new SimpleDateFormat(simpleDateFromat);
			simpleDateStr = simpleDateFormatter.format(date);
		} catch (Exception ps) {
			try {
				date = formatter.parse(dateStr.substring(0, 23));
			} catch (Exception e) {
			}
			try {
				simpleDateFormatter = new SimpleDateFormat(simpleDateFromat);
				simpleDateStr = simpleDateFormatter.format(date);
			} catch (Exception e) {
				
			}
		}
		return simpleDateStr;
	}
	
	public static final String convertISOToSimpleDateFormat(XMLGregorianCalendar date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			GregorianCalendar gc = date.toGregorianCalendar();
			return sdf.format(gc.getTime());
			
		} catch (Exception e) {
			return null;
		}
	}

	public static XMLGregorianCalendar convertDateToUTCtime(Date date) {
		try {
			GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
			gc.setTime(date);
			gc.setTimeZone(TimeZone.getTimeZone("UTC"));
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

		} catch (Exception e) {
			return null;
		}
	}
	
	public static String convertDateToString(Date in, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(in);
	}
	
	public static XMLGregorianCalendar convertDateToUTCtime(String dateStr) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			Date date = format.parse(dateStr);
			return convertDateToUTCtime(date);
		} catch (Exception e) {
			return null;
		}
	}

}
