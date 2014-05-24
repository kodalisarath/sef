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
			String msisdn = request.getMsisdn();
			String lastDigit = msisdn.substring(msisdn.length() - 1);
			String restMsisdn = msisdn.substring(0, msisdn.length() - 1);
			String updateMsisdn = restMsisdn + '.' + lastDigit;
			String rData = request.getSdpId() + request.getRdata();
			
			Name zone = Name.fromString(lastDigit + request.getZname());
			Update update = new Update(zone);
			update.add(Name.fromString(updateMsisdn + request.getZname()), request.getDtype(),  request.getTtl(), rData);

			Resolver res = new SimpleResolver(dns.getIp());
			res.setTCP(dns.isUseTcp());
			
			log.info("DNS Add entry: "  + update.toString());
			res.send(update);
			log.info("dns updated for msisdn: " + msisdn);
		} catch (Exception e) {
			log.error("Error while firing DNS command.", e);
			throw new SmException("cs-af", e);
		}
		return null;
	}
}
