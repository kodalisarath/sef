package com.ericsson.raso.sef.client.af.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Name;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Update;

import com.ericsson.raso.sef.client.af.DnsServiceResolver;
import com.ericsson.raso.sef.client.af.internal.DnsAddress;
import com.ericsson.raso.sef.client.af.request.AddDnsRequest;
import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.SmException;

public class AddDnsCommand implements Command<Void> {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private AddDnsRequest request;

	public AddDnsCommand(AddDnsRequest request) {
		this.request = request;
	}

	@Override
	public Void execute() throws SmException {
		try {
			DnsAddress dns = DnsServiceResolver.getAccountFinderRoute().getDns(request.getMsisdn(), request.getSiteId());
			log.debug("Found the dnsClient to execute the command: " + dns);
			
			log.debug("Checking the DNS Request: " + request);
			String msisdn = request.getMsisdn();
			String lastDigit = msisdn.substring(msisdn.length() - 1);
			String restMsisdn = msisdn.substring(0, msisdn.length() - 1);
			String updateMsisdn = restMsisdn + '.' + lastDigit;
			String rData = "PIDCSDP01" + request.getRdata();
			
			Name zone = Name.fromString(lastDigit + request.getZname());
			Update update = new Update(zone);
			update.add(Name.fromString(updateMsisdn + request.getZname()), request.getDtype(),  request.getTtl(), rData);
			
			log.debug("Preparing the DNS Client to issue command over the network...");
			//TODO: Super crime if you this ip post 6th June
			Resolver res = new SimpleResolver("10.245.139.132");
			res.setTCP(dns.isUseTcp());
			
			log.info("DNS Command to execute: "  + update.toString());
			res.send(update);
			log.info("DNS provisioned wih user: " + msisdn);
		} catch (Exception e) {
			log.error("Error while firing DNS command.", e);
			throw new SmException("cs-af", e);
		}
		return null;
	}
}
