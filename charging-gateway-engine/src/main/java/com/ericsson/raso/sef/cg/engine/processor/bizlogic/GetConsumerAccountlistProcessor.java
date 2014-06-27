package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.dccapi.avp.CCRequestNumberAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCRequestTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.CurrencyCodeAvp;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.pps.diameter.rfcapi.base.avp.ExperimentalResultAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.ExperimentalResultCodeAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.ResultCodeAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.SessionIdAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.VendorIdAvp;
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.client.air.command.GetBalanceAndDateCommand;
import com.ericsson.raso.sef.client.air.request.GetBalanceAndDateRequest;
import com.ericsson.raso.sef.client.air.response.DedicatedAccountInformation;
import com.ericsson.raso.sef.client.air.response.GetBalanceAndDateResponse;
import com.ericsson.raso.sef.client.air.response.OfferInformation;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.diameter.DiameterErrorCode;
import com.ericsson.raso.sef.core.cg.nsn.avp.AccountAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.ConsumerAccountIdAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.CurrencyAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.PPIInformationAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.ServiceInfoAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.TransparentDataAvp;
import com.ericsson.raso.sef.core.config.IConfig;

public class GetConsumerAccountlistProcessor implements Processor {

	protected static Logger log = LoggerFactory.getLogger(GetConsumerAccountlistProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		log.debug(String.format("Enter GetConsumerAccountlistProcessor.porcess. exchange is %s", exchange));
		ChargingRequest request = (ChargingRequest) exchange.getIn().getBody();

		Ccr sourceCcr = request.getSourceCcr();

		ServiceInfoAvp serviceInfoAvp = new ServiceInfoAvp(sourceCcr.getAvp(ServiceInfoAvp.AVP_CODE));
		PPIInformationAvp ppi = serviceInfoAvp.getPpiInformationAvp();

		List<ConsumerAccountIdAvp> accountIdAvps = ppi.getConsumerAccountIdAvps();

		GetBalanceAndDateRequest getBalRequest = new GetBalanceAndDateRequest();
		getBalRequest.setSubscriberNumber(request.getMsisdn());

		GetBalanceAndDateCommand command = new GetBalanceAndDateCommand(getBalRequest);
		GetBalanceAndDateResponse getBalResponse = command.execute();

		ChargingInfo response = new ChargingInfo();
		response.setUniqueMessageId(request.getMessageId());
		response.setSessionId(request.getSessionId());
		response.setAvpList(toNsnAnswer(sourceCcr, getBalResponse, accountIdAvps,request.getMsisdn()));
		ResultCodeAvp resultCodeAvp = new ResultCodeAvp(DiameterErrorCode.DIAMETER_SUCCESS.getCode());
		response.setResultCodeAvp(resultCodeAvp);
		
		exchange.getOut().setBody(response);
		log.debug("End GetConsumerAccountlistProcessor");
	}

	@Handler
	public ChargingInfo process(@Body ChargingRequest request) throws Exception {
		log.debug(String.format("Enter GetConsumerAccountlistProcessor.porcess. request is %s", request));
		Ccr sourceCcr = request.getSourceCcr();

		ServiceInfoAvp serviceInfoAvp = new ServiceInfoAvp(sourceCcr.getAvp(ServiceInfoAvp.AVP_CODE));
		PPIInformationAvp ppi = serviceInfoAvp.getPpiInformationAvp();

		List<ConsumerAccountIdAvp> accountIdAvps = ppi.getConsumerAccountIdAvps();


		GetBalanceAndDateRequest getBalRequest = new GetBalanceAndDateRequest();
		getBalRequest.setSubscriberNumber(request.getMsisdn());

		GetBalanceAndDateCommand command = new GetBalanceAndDateCommand(getBalRequest);
		GetBalanceAndDateResponse getBalResponse = command.execute();

		ChargingInfo response = new ChargingInfo();
		response.setUniqueMessageId(request.getMessageId());
		response.setSessionId(request.getSessionId());
		response.setAvpList(toNsnAnswer(sourceCcr, getBalResponse, accountIdAvps,request.getMsisdn()));
		ResultCodeAvp resultCodeAvp = new ResultCodeAvp(DiameterErrorCode.DIAMETER_SUCCESS.getCode());
		response.setResultCodeAvp(resultCodeAvp);
		log.debug("End GetConsumerAccountlistProcessor");
		return response;
	}

	private List<Avp> toNsnAnswer(Ccr ccr, GetBalanceAndDateResponse balresponse, List<ConsumerAccountIdAvp> list,String msisdn) {
		log.debug(String.format("Enter GetConsumerAccountlistProcessor.toNsnAnswer. ccr is %s, balresponse is %s " +
				"list is %s, msisdn is %s", 
				ccr, balresponse, list, msisdn));
		final String WALLET_MAPPING = "GLOBAL_walletMapping";
		final String OFFER_MAPPING = "Global_offerMapping";


		IConfig config = SefCoreServiceResolver.getConfigService();
		
		
		//Properties offerWalletMapping = config.properties(IConfig.GLOBAL_COMPONENT, WALLET_MAPPING);
	//	Properties offerDaMapping = config.properties(IConfig.GLOBAL_COMPONENT, OFFER_MAPPING);

		List<Avp> answerAvp = new ArrayList<Avp>();
		try {
			answerAvp.add(new CCRequestNumberAvp(0));
			answerAvp.add(new CCRequestTypeAvp(ccr.getCCRequestType()));
			answerAvp.add(new SessionIdAvp(ccr.getSessionId()));

			ExperimentalResultAvp experimentalResultAvp = new ExperimentalResultAvp();
			experimentalResultAvp.addSubAvp(new VendorIdAvp(28458));
			experimentalResultAvp.addSubAvp(new ExperimentalResultCodeAvp(1));
			answerAvp.add(experimentalResultAvp);

			answerAvp.add(new ResultCodeAvp(DiameterErrorCode.DIAMETER_SUCCESS.getCode()));
			PPIInformationAvp ppiInformationAvp = new PPIInformationAvp();

			Map<Integer, DedicatedAccountInformation> DaMap = toDaMap(balresponse.getDedicatedAccountInformation());

			List<OfferInformation> offerInformationList = balresponse.getOfferInformationList();
			boolean isaccountNotAdded= true;

			for (OfferInformation offerInformation : offerInformationList) {

				Integer offerId = offerInformation.getOfferID();
				String walletName = config.getValue(WALLET_MAPPING, offerId+"");
				//String walletName = offerWalletMapping.getProperty();
				if (walletName == null) {
					log.warn("No wallet is configured for OFFER ID: " + offerId);
					continue;
				}

				//String daID = offerDaMapping.getProperty(offerId.toString());
				String daID = config.getValue(OFFER_MAPPING, offerId+"");
				if (daID == null) {
					log.error("DA was not found for the offer: " + offerId);
					continue;
				}

				DedicatedAccountInformation accountInformation = DaMap.get(Integer.valueOf(daID));
				if (accountInformation != null) {
					Long daid = Long.valueOf(accountInformation.getDedicatedAccountID());
					if (isExistCaId(daid,list)) {

						AccountAvp account = new AccountAvp();
						account.addAccountId(daid);
						account.addAccountOwnerId(msisdn);
						long currBalance = Long.valueOf(accountInformation.getDedicatedAccountValue1());

						long confec= getConversionFector(""+offerId);
						currBalance = currBalance/confec;

						account.addAccountCurrentBalance(currBalance);
						account.addAccountExpiryDate(offerInformation.getExpiryDateTime().getTime());
						account.addAccountType(accountInformation.getDedicatedAccountUnitType());
						account.addAccountApproved(1);
						account.addAccountCurrentAuthorizedAmount(0l);
						account.addAccountLastBalanceModDate(1846300719786091975l);
						CurrencyAvp currencyAvp = ppiInformationAvp.getCurrencyAvp();
						if (currencyAvp != null) {
							account.addSubAvp(new CurrencyCodeAvp(608));
						}
						ppiInformationAvp.addSubAvp(account);
						isaccountNotAdded=false;
					}
				}
			}
			if(isaccountNotAdded)
			{
				ppiInformationAvp.addSubAvp(new TransparentDataAvp());
			}

			ServiceInfoAvp serviceInfoAvp = new ServiceInfoAvp();
			serviceInfoAvp.addPPiInformationAvp(ppiInformationAvp);
			answerAvp.add(serviceInfoAvp);

		} catch (AvpDataException e) {
			log.error("error during iterating AVP", e);
			answerAvp.clear();
			answerAvp.add(new ResultCodeAvp(DiameterErrorCode.DIAMETER_UNABLE_TO_COMPLY.getCode()));
		}
		log.debug("End toNsnAnswer");
		return answerAvp;
	}

	private Map<Integer, DedicatedAccountInformation> toDaMap(List<DedicatedAccountInformation> list) {
		log.debug(String.format("Enter GetConsumerAccountlistProcessor.toDaMap. list is %s", list));
		Map<Integer, DedicatedAccountInformation> map = new LinkedHashMap<Integer, DedicatedAccountInformation>();
		for (DedicatedAccountInformation da : list) {
			map.put(da.getDedicatedAccountID(), da);
		}
		log.debug("GetConsumerAccountlistProcessor.toDaMap");
		return map;
	}

	private boolean isExistCaId(Long id, List<ConsumerAccountIdAvp> list) {
		log.debug(String.format("Enter GetConsumerAccountlistProcessor.isExistCaId. id is %s, list is %s", id, list));
		for (ConsumerAccountIdAvp accountIdAvp : list) {
			try {
				if (id == accountIdAvp.getAsLong())
					return true;
			} catch (AvpDataException e) {
				continue;
			}
		}
		log.debug("GetConsumerAccountlistProcessor.isExistCaId");
		return false;
	}

	private static long getConversionFector(String offerId) {
		log.debug(String.format("Enter GetConsumerAccountlistProcessor.getConversionFector. offerId is %s", offerId));

		long conFec = 1;
		// IConfig config = SmCoreContext.getConfig();
		// Properties walletMapping =
		// config.properties(IConfig.GLOBAL_COMPONENT, "walletMapping");

		String walletName = SefCoreServiceResolver.getConfigService().getValue(
				"GLOBAL_walletMapping", offerId.trim());
		// String walletName = name;
		String conversionFactor = SefCoreServiceResolver.getConfigService()
				.getValue("GLOBAL_walletConversionFactor", walletName);

		if (conversionFactor != null) {
			conFec = Long.parseLong(conversionFactor);
		}
		/*
		 * Properties offerConvMapping =
		 * config.properties(IConfig.GLOBAL_COMPONENT,
		 * SmConstants.OfferConversionFector); //String walletName =
		 * walletMapping.getProperty(offerId.trim()); if(walletName!=null){
		 * String conv = offerConvMapping.getProperty(walletName);
		 * if(conv!=null){ conFec= Long.parseLong(conv); } }
		 */
		log.debug("GetConsumerAccountlistProcessor.getConversionFector");
		return conFec;
	}
	
}
