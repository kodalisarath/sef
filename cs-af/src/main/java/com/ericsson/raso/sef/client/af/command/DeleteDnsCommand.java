package com.ericsson.raso.sef.client.af.command;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Name;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Update;

import com.ericsson.raso.sef.client.af.DnsServiceResolver;
import com.ericsson.raso.sef.client.af.internal.DnsAddress;
import com.ericsson.raso.sef.client.af.request.DeleteDnsRequest;
import com.ericsson.raso.sef.core.Command;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;


public class DeleteDnsCommand implements Command<Void> {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private DeleteDnsRequest request;

	public DeleteDnsCommand(DeleteDnsRequest request) {
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
			
			Name zone = Name.fromString(lastDigit + request.getZname());
			Update update = new Update(zone);
			update.delete(Name.fromString(updateMsisdn + request.getZname()), request.getDtype());

			String primaryAfAddress = SefCoreServiceResolver.getConfigService().getValue("af1", "address");
			Resolver primary = new SimpleResolver(primaryAfAddress);
			primary.setTCP(dns.isUseTcp());
			
			String secondaryAfAddress = SefCoreServiceResolver.getConfigService().getValue("af2", "address");
			Resolver secondary = new SimpleResolver(secondaryAfAddress);
			secondary.setTCP(dns.isUseTcp());
			
			try {		
				log.info("DNS Command to execute: "  + update.toString());
				primary.send(update);
			} catch (IOException e) {
				log.error("Primary AF DNS(" + primaryAfAddress + ") failed... will try secondary");
				secondary.send(update);
			}
			log.info("dns delete for msisdn: " + msisdn);
		} catch (Exception e) {
			log.error("Error while firing DNS command.", e);
			throw new SmException("cs-af", e);
		}
		return null;
	}
}
