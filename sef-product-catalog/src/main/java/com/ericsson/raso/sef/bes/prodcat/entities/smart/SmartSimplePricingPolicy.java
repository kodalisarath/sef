package com.ericsson.raso.sef.bes.prodcat.entities.smart;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.prodcat.Constants;
import com.ericsson.raso.sef.bes.prodcat.entities.MonetaryUnit;
import com.ericsson.raso.sef.bes.prodcat.entities.PricingPolicy;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;

public class SmartSimplePricingPolicy extends PricingPolicy {
	private static final long serialVersionUID = -65196986520647498L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SmartSimplePricingPolicy.class);
	private MonetaryUnit cost = null;

	public SmartSimplePricingPolicy(String name) {
		super(name);
	}

	@Override
	public boolean execute() {
		LOGGER.debug("..Inside SmartSimplePricingPolicy...");
		Map<String, Object> context = RequestContextLocalStore.get().getInProcess();
		String channelName = (String) context.get("channelName");
		LOGGER.debug("ChannelName: "+channelName);
		if (channelName != null) {
			String rate = SefCoreServiceResolver.getConfigService().getValue("SMART_CustomerInfoChannel", channelName);
			LOGGER.debug("rate: "+rate);
			if (rate != null) {
				context.put(Constants.RATED_AMOUNT.name(), Long.parseLong(rate));
			}
		}
			
		return true;
	}

	public void setCost(MonetaryUnit cost) {
		this.cost = cost;
	}
		
	
}
