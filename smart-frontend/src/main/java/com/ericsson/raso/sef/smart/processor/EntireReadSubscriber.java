package com.ericsson.raso.sef.smart.processor;

import java.util.Collection;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.commons.SmartServiceHelper;
import com.ericsson.raso.sef.smart.commons.read.EntireRead;
import com.ericsson.raso.sef.smart.commons.read.Rpp;
import com.ericsson.raso.sef.smart.usecase.EntireReadRequest;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class EntireReadSubscriber implements Processor {
	private static final org.slf4j.Logger	logger	= LoggerFactory.getLogger(EntireReadSubscriber.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		EntireReadRequest request = (EntireReadRequest) exchange.getIn().getBody();
		/*
		 * logger.debug("Manila: About to Call getAndRefreshSubscriber ");
		 * SmartServiceHelper.getAndRefreshSubscriber(request.getCustomerId());
		 * logger.debug("Manila: getAndRefreshSubscriber Call completed");
		 */logger.debug("Manila: About to Call entireReadSubscriber ");
		EntireRead entireRead = SmartServiceHelper.entireReadSubscriber(request.getCustomerId());

		if (entireRead == null) {
			logger.error("Unknon User.. Error Not Found");
			throw ExceptionUtil.toSmException(new ResponseCode(13423, "EntireRead Entity - Customer with primary key Keyname:PK,CustomerId: " + request.getCustomerId() + " does not exist"));
		}

		logger.debug("Manila: EntireRead Response is " + entireRead);
		exchange.getOut().setBody(createResponse(entireRead, request.isTransactional()));

	}

	private CommandResponseData createResponse(EntireRead entireRead, boolean isTransactional) {
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

		int lastIndex = 0;
		if (entireRead != null) {
			if (entireRead.getCustomer() != null) {
				operationResult.getOperation().add(EntireReadUtil.createCustomerRead(entireRead.getCustomer().getCustomerRead()));
				operationResult.getOperation().add(EntireReadUtil.createCustomerBucketRead(entireRead.getCustomer().getCustomerBucketRead()));
				operationResult.getOperation().add(EntireReadUtil.createCustomerVersionRead(entireRead.getCustomer().getCustomerVersionRead()));
			}

			if (entireRead.getRop() != null) {
				operationResult.getOperation().add(EntireReadUtil.createRopRead(entireRead.getRop().getRopRead()));
				operationResult.getOperation().add(EntireReadUtil.createRopBucketRead(entireRead.getRop().getRopBucketRead()));
				operationResult.getOperation().add(EntireReadUtil.createRopVersionRead(entireRead.getRop().getRopVersionRead()));
			}

			if (entireRead.getRpps() != null) {
				Collection<Rpp> rpps = entireRead.getRpps();

				for (Rpp rpp : rpps) {
					
					operationResult.getOperation().add(EntireReadUtil.createRppRead(rpp.getRppRead()));
					operationResult.getOperation().add(EntireReadUtil.createRppBucketRead(rpp.getRppBucketRead()));
					operationResult.getOperation().add(EntireReadUtil.createRppVersionRead(rpp.getRppVersionRead()));
					lastIndex = rpp.getRppVersionRead().getKey();
				}
			}

			logger.debug("WelcomePack: " + entireRead.getWelcomePack() + ", Package: " + entireRead.getWelcomePack().getRead().getsPackageId());
			if (entireRead.getWelcomePack() != null && entireRead.getWelcomePack().getRead().getsPackageId() != null) {
				logger.debug("inside welcome pack....");
				entireRead.getWelcomePack().getRead().setKey(lastIndex + 1);
				entireRead.getWelcomePack().getVersionRead().setKey(lastIndex + 1);
				entireRead.getWelcomePack().getBucketRead().setKey(lastIndex + 1);

				operationResult.getOperation().add(EntireReadUtil.createWelcomePackRead(entireRead.getWelcomePack().getRead()));
				operationResult.getOperation().add(EntireReadUtil.createWelcomePackBucketRead(entireRead.getWelcomePack().getBucketRead()));
				operationResult.getOperation().add(EntireReadUtil.createWelcomePackVersionRead(entireRead.getWelcomePack().getVersionRead()));
			}
		}
		return responseData;
	}

}
