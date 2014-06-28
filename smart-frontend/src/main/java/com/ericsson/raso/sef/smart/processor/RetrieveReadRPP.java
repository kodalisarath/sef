package com.ericsson.raso.sef.smart.processor;

import java.util.Collection;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.smart.commons.SmartServiceHelper;
import com.ericsson.raso.sef.smart.commons.read.EntireRead;
import com.ericsson.raso.sef.smart.commons.read.Rpp;
import com.ericsson.raso.sef.smart.usecase.RetrieveReadRPPRequest;
import com.ericsson.raso.sef.watergate.FloodGate;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class RetrieveReadRPP implements Processor {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		RetrieveReadRPPRequest request = (RetrieveReadRPPRequest) exchange
				.getIn().getBody();

		/*
		 * refreshSubscriberState(request.getCustomerId());
		 * 
		 * SmartCommonService smartCommonService =
		 * SmartContext.getSmartCommonService();
		 * 
		 * EntireRead entireRead =
		 * smartCommonService.entireReadSubscriber(request.getCustomerId());
		 */

		SmartServiceHelper.getAndRefreshSubscriber(request.getCustomerId());
		EntireRead entireRead = SmartServiceHelper.entireReadSubscriber(request
				.getCustomerId());

		String edrIdentifier = (String)exchange.getIn().getHeader("EDR_IDENTIFIER"); 
		
		log.error("FloodGate acknowledging exgress...");
		FloodGate.getInstance().exgress();
		
		exchange.getOut().setBody(
				createResponse(entireRead, request.isTransactional()));
		
		exchange.getOut().setHeader("EDR_IDENTIFIER", edrIdentifier);
		

	}

	private CommandResponseData createResponse(EntireRead entireRead,
			boolean isTransactional) {
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();

		responseData.setCommandResult(result);
		OperationResult operationResult = new OperationResult();
		if (isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}

		if (entireRead.getRpps() != null) {
			Collection<Rpp> rpps = entireRead.getRpps();
			for (Rpp rpp : rpps) {
				operationResult.getOperation().add(
						EntireReadUtil.createRppRead(rpp.getRppRead()));
			}
		}

		return responseData;
	}

}
