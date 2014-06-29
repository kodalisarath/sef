package com.ericsson.raso.sef.scheduler.test;

import java.util.Calendar;
import java.util.Date;

 
public class TestDateMain  {
	
	public static void main(String args[])
	{
		Date d = new Date(14364078933878L);
		
		System.out.println(d.toString());
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(1404081824765L);
		
		System.out.println(cal.getTime().toString());
	}
}