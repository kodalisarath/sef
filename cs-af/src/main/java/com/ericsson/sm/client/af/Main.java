package com.ericsson.sm.client.af;

import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Update;

public class Main {
	
	public static void main(String[] args) {
		String msisdn = "12245667986";
		String lastDigit = msisdn.substring(msisdn.length() - 1);
		System.out.println(lastDigit);
		String restMsisdn = msisdn.substring(0, msisdn.length() - 1);
		System.out.println(restMsisdn);
		String finalMsisdn = restMsisdn + '.' + lastDigit;
		System.out.println(finalMsisdn);
		
		
	}
	
	public static void main2(String[] args) throws Exception{
		Name zone = Name.fromString("lookup");
		Update update = new Update(zone);
		Record record = Record.newRecord(Name.fromString("msisdn"), 1, 1, 1000, "xyz".getBytes().length, "xyz".getBytes());
		update.add(record);
		
		Resolver res = new SimpleResolver("10.0.0.1");
		res.setTCP(false);
		res.send(update);
	}
	
	public static void main1(String[] args) throws Exception{
		Name zone = Name.fromString("lookup");
		Update update = new Update(zone);
		Record record = Record.newRecord(Name.fromString("msisdn"), 1, 1, 0);
		update.delete(record);
		
		Resolver res = new SimpleResolver("10.0.0.1");
		res.setTCP(false);
		res.send(update);
	}
}
