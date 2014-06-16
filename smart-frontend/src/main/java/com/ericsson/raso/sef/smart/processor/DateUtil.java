package com.ericsson.raso.sef.smart.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;
public class DateUtil {

	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
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

				logger.error("Exception captured.@ convertISOToSimpleDateFormat..",e);
			}
		}
		return simpleDateStr;
	}

	public static final String convertISOToSimpleDateFormat(
			XMLGregorianCalendar date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			GregorianCalendar gc = date.toGregorianCalendar();
			return sdf.format(gc.getTime());

		} catch (Exception e) {
			logger.error("Exception captured at convertISOToSimpleDateFormat",e);
			return null;
		}
	}

	public static XMLGregorianCalendar convertDateToUTCtime(Date date) {
		try {
			GregorianCalendar gc = (GregorianCalendar) GregorianCalendar
					.getInstance();
			gc.setTime(date);
			gc.setTimeZone(TimeZone.getTimeZone("UTC"));
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

		} catch (Exception e) {
			logger.error("Exception captured at convertDateToUTCtime",e);
			return null;
		}
	}

	public static String convertDateToString(Date in, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(in);
	}

	public static Date convertStringToDate(String in, String pattern)
			throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.parse(in);
	}

	public static String convertDateToString(Date in) {
		//String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return dateFormat.format(in);
		
	}
	public static String formatStringDate(String in) {
		//String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return dateFormat.format(in);
	}

	public static String formatStringDate(String in, String pattern) {
		//String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(in);
	}

	public static Date convertStringToDate(String in)  {

	//	String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		try
		{
		return dateFormat.parse(in);
		}
		catch(ParseException p)
		{
			logger.error("Exception in convertStringToDate",p);
			return null;
		}
	}

	public static XMLGregorianCalendar convertDateToUTCtime(String dateStr) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			Date date = format.parse(dateStr);
			return convertDateToUTCtime(date);
		} catch (Exception e) {
			logger.error("Exception captured at convertDateToUTCtime",e);
			return null;
		}
	}

	public static String addDaysToDate(String dateStr, int days) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, days);
		String output = sdf.format(c.getTime());
		return output;
	}

}