package com.ericsson.raso.sef.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

public class DateUtil {
	
	private static final String DEFAULT_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
	private static final String EDR_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	public static XMLGregorianCalendar convertDateToUTCtime(Date date) throws DatatypeConfigurationException {
		GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
		gc.setTime(date);
		gc.setTimeZone(TimeZone.getTimeZone("UTC"));
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
	}
	
	public static Date toDate(String dateStr) throws ParseException {
		return toDate(dateStr, DEFAULT_DATE_FORMAT);
	}
	
	public static Date toDate(String dateStr, String dateFormat) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.parse(dateStr);
	}
	
	
	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}

	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

	public static boolean isToday(DateTime d1) {
		if (d1 == null)
			return false;
		return d1.withTimeAtStartOfDay().isEqual(new DateTime().withTimeAtStartOfDay());
	}
	
	public static String toEdrFormat(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(EDR_DATE_FORMAT);
		return format.format(date);
	}
	
	public static void main(String[] args) {
		System.out.println(toEdrFormat(new Date()));
	}
}
