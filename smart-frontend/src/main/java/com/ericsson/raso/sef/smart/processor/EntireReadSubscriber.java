package com.ericsson.raso.sef.smart.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.ExceptionUtil;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.SmartServiceHelper;
import com.ericsson.raso.sef.smart.commons.read.EntireRead;
import com.ericsson.raso.sef.smart.commons.read.Rpp;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberInfo;
import com.ericsson.raso.sef.smart.subscriber.response.SubscriberResponseStore;
import com.ericsson.raso.sef.smart.usecase.EntireReadRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ListParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StructParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;

public class EntireReadSubscriber implements Processor {
	private static final org.slf4j.Logger	logger	= LoggerFactory.getLogger(EntireReadSubscriber.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		EntireReadRequest request = (EntireReadRequest) exchange.getIn().getBody();

		logger.debug("Lets just get ILDB first, to handle Preactive");
		Subscriber subscriber = this.simpleRead(request.getCustomerId());
		
		logger.debug("contract state: " + subscriber.getContractState());
		if (subscriber.getContractState().equals(ContractState.PREACTIVE.getName())) {
			exchange.getOut().setBody(createPreactiveResponse(subscriber, request.isTransactional()));
			return;
		}
		
		
		
		
		logger.debug("Manila: About to Call entireReadSubscriber ");
		EntireRead entireRead = SmartServiceHelper.entireReadSubscriber(request.getCustomerId());

		if (entireRead == null) {
			logger.error("Unknon User.. Error Not Found");
			throw ExceptionUtil.toSmException(new ResponseCode(13423, "EntireRead Entity - Customer with primary key Keyname:PK,CustomerId: " + request.getCustomerId() + " does not exist"));
		}

		logger.debug("Manila: EntireRead Response is " + entireRead);
		exchange.getOut().setBody(createResponse(entireRead, request.isTransactional()));

	}

	
	private Subscriber simpleRead(String customerId) throws SmException {
		List<Meta> metaList = new ArrayList<Meta>();
		//metaList.add(new Meta("READ_SUBSCRIBER", "ENTIRE_READ_SUBSCRIBER"));
		ISubscriberRequest iSubscriberRequest = SmartServiceResolver.getSubscriberRequest();
		
		String requestId = iSubscriberRequest.readSubscriber(UniqueIdGenerator.generateId(), customerId, metaList);
		SubscriberResponseStore.put(requestId, new SubscriberInfo());
		ISemaphore semaphore = SefCoreServiceResolver.getCloudAwareCluster().getSemaphore(requestId);
		try {
			semaphore.init(0);
			semaphore.acquire();
		} catch (InterruptedException e) {
		}
		logger.info("Check if response received for simpleread subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		
		if (subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() > 0) {
			if (subscriberInfo.getStatus().getCode() == 504)
				throw ExceptionUtil.toSmException(ErrorCode.primaryKeyAlreadyExists1);
			else
				throw ExceptionUtil.toSmException(new ResponseCode(subscriberInfo.getStatus().getCode(), subscriberInfo.getStatus().getDescription()));
		}
		
		return subscriberInfo.getSubscriber();
	}

	private Object createPreactiveResponse(Subscriber subscriber, boolean isTransactional) {
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
		
		// Read Customer - read, bucket, version
		//// customer read
		Operation operation = new Operation();
		operation.setName("Read");
		operation.setModifier("Customer");
		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		parameterList.add(EntireReadUtil.shortParameter("billCycleId", 0));
		parameterList.add(EntireReadUtil.symbolicOrDateParameter("billCycleSwitch", "MAX_DATEANDTIME"));
		parameterList.add(EntireReadUtil.shortParameter("billCycleIdAfterSwitch", -1));
		parameterList.add(EntireReadUtil.intParameter("prefetchFilter", -1));
		parameterList.add(EntireReadUtil.enumerationValueParameter("category", "ONLINE"));
		operationResult.getOperation().add(operation);
		
		//// customer read bucket
		operation = new Operation();
		operation.setName("BucketRead");
		operation.setModifier("Customer");

		parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();

		parameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		parameterList.add(EntireReadUtil.enumerationValueParameter("bCategory", "ONLINE"));
		SimpleDateFormat metaStoreFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat nsnResponseFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
		String date = subscriber.getMetas().get("bInValidFrom");
		if (date == null)
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", "MAX_DATEANDTIME"));
		else
			try {
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", nsnResponseFormat.format(metaStoreFormat.parse(date))));
			} catch (ParseException e) {
				logger.error("Unparseable date(bInvalidFrom): " + date);
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", "MAX_DATEANDTIME"));
			}

		date = subscriber.getMetas().get("bValidFrom");
		if (date == null)
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", nsnResponseFormat.format(new Date())));
		else
			try {
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", nsnResponseFormat.format(metaStoreFormat.parse(subscriber.getMetas().get("bValidFrom")))));
			} catch (ParseException e) {
				logger.error("Unparseable date(bInvalidFrom): " + date);
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", nsnResponseFormat.format(new Date())));
			}
		parameterList.add(EntireReadUtil.intParameter("bSeriesId", 0));
		operationResult.getOperation().add(operation);
		
		//// customer read version
		operation = new Operation();
		operation.setName("VersionRead");
		operation.setModifier("Customer");

		parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();

		parameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		parameterList.add(EntireReadUtil.enumerationValueParameter("category", "ONLINE"));
		date = subscriber.getMetas().get("bValidFrom");
		if (date == null)
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", nsnResponseFormat.format(new Date())));
		else
			try {
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", nsnResponseFormat.format(metaStoreFormat.parse(subscriber.getMetas().get("bValidFrom")))));
			} catch (ParseException e) {
				logger.error("Unparseable date(bInvalidFrom): " + date);
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", nsnResponseFormat.format(new Date())));
			}
		date = subscriber.getMetas().get("bValidFrom");
		if (date == null)
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", nsnResponseFormat.format(new Date())));
		else
			try {
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", nsnResponseFormat.format(metaStoreFormat.parse(subscriber.getMetas().get("bValidFrom")))));
			} catch (ParseException e) {
				logger.error("Unparseable date(bInvalidFrom): " + date);
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", nsnResponseFormat.format(new Date())));
			}
		operationResult.getOperation().add(operation);
		
		
		
		
		// Read RopRead, RopBucket, RopVersion
		//// rop read
		operation = new Operation();
		operation.setName("Read");
		operation.setModifier("ROP");

		parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		parameterList.add(EntireReadUtil.intParameter("Key", 1));
		parameterList.add(EntireReadUtil.enumerationValueParameter("category", "ONLINE"));
		parameterList.add(EntireReadUtil.intParameter("prefetchFilter", -1));
		parameterList.add(EntireReadUtil.booleanParameter("AnnoFirstWarningPeriodSent", false));
		parameterList.add(EntireReadUtil.booleanParameter("AnnoSecondWarningPeriodSent", false));
		parameterList.add(EntireReadUtil.booleanParameter("IsBalanceClearanceOnOutpayment", true));
		if (subscriber.getMetas().get("IsCollectCallAllowed") != null)
			parameterList.add(EntireReadUtil.booleanParameter("IsCollectCallAllowed", Boolean.parseBoolean(subscriber.getMetas().get("IsCollectCallAllowed"))));
		parameterList.add(EntireReadUtil.booleanParameter("IsFirstCallPassed", false));
		parameterList.add(EntireReadUtil.booleanParameter("IsGPRSUsed", false));
		parameterList.add(EntireReadUtil.booleanParameter("IsLastRechargeInfoStored", false));
		parameterList.add(EntireReadUtil.booleanParameter("IsLastTransactionEnqUsed", false));
		parameterList.add(EntireReadUtil.booleanParameter("IsLocked", false));
		parameterList.add(EntireReadUtil.booleanParameter("IsOperatorCollectCallAllowed", false));
		parameterList.add(EntireReadUtil.booleanParameter("IsSmsAllowed", false));
		parameterList.add(EntireReadUtil.booleanParameter("IsUSCAllowed", false));
		// skipping active end date.. since the user is not activated yet
		ListParameter counterListParameter = EntireReadUtil.listParameter("ChargedMenuAccessCounter");
		parameterList.add(counterListParameter);
		for (int i=0; i<6; ++i) {
			IntElement intParameter = new IntElement();
			intParameter.setValue(0);
			counterListParameter.getElementOrBooleanElementOrByteElement().add(intParameter);
		}
		if (subscriber.getMetas().get("c_TaggingStatus") != null) 
			parameterList.add(EntireReadUtil.intParameter("c_TaggingStatus", Integer.parseInt(subscriber.getMetas().get("c_TaggingStatus"))));
		// skipping firstcall date, since the user is not yet active
		// skipping grace date, since the user is not yet active
		if (subscriber.getMetas().get("s_CRMTitle") != null) 
			parameterList.add(EntireReadUtil.stringParameter("s_CRMTitle", subscriber.getMetas().get("s_CRMTitle")));
		parameterList.add(EntireReadUtil.stringParameter("LastKnownPeriod", "PreActive"));
		if (subscriber.getMetas().get("PreActiveEndDate") != null) 
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("PreActiveEndDate", subscriber.getMetas().get("PreActiveEndDate")));
		operationResult.getOperation().add(operation);

		//// rop read bucket
		operation = new Operation();
		operation.setName("BucketRead");
		operation.setModifier("ROP");

		parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		parameterList.add(EntireReadUtil.intParameter("Key", 1));
		parameterList.add(EntireReadUtil.enumerationValueParameter("bCategory", "ONLINE"));
		parameterList.add(EntireReadUtil.intParameter("bSeriesId", 0));
		date = subscriber.getMetas().get("bInValidFrom");
		if (date == null)
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", "MAX_DATEANDTIME"));
		else
			try {
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", nsnResponseFormat.format(metaStoreFormat.parse(date))));
			} catch (ParseException e) {
				logger.error("Unparseable date(bInvalidFrom): " + date);
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", "MAX_DATEANDTIME"));
			}

		date = subscriber.getMetas().get("bValidFrom");
		if (date == null)
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", nsnResponseFormat.format(new Date())));
		else
			try {
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", nsnResponseFormat.format(metaStoreFormat.parse(subscriber.getMetas().get("bValidFrom")))));
			} catch (ParseException e) {
				logger.error("Unparseable date(bInvalidFrom): " + date);
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", nsnResponseFormat.format(new Date())));
			}
		operationResult.getOperation().add(operation);
	
		
		//// rop read version
		operation = new Operation();
		operation.setName("VersionRead");
		operation.setModifier("ROP");

		parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		parameterList.add(EntireReadUtil.intParameter("Key", 1));
		date = subscriber.getMetas().get("bValidFrom");
		if (date == null)
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", nsnResponseFormat.format(new Date())));
		else
			try {
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", nsnResponseFormat.format(metaStoreFormat.parse(subscriber.getMetas().get("bValidFrom")))));
			} catch (ParseException e) {
				logger.error("Unparseable date(bInvalidFrom): " + date);
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", nsnResponseFormat.format(new Date())));
			}
		date = subscriber.getMetas().get("bValidFrom");
		if (date == null)
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", nsnResponseFormat.format(new Date())));
		else
			try {
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", nsnResponseFormat.format(metaStoreFormat.parse(subscriber.getMetas().get("bValidFrom")))));
			} catch (ParseException e) {
				logger.error("Unparseable date(bInvalidFrom): " + date);
				parameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", nsnResponseFormat.format(new Date())));
			}
		
		parameterList.add(EntireReadUtil.stringParameter("s_OfferId", "TnT"));
		operationResult.getOperation().add(operation);
		
		// Read Rpp's - read, bucket, version
		//// rpp read
		operation = new Operation();
		operation.setName("Read");
		operation.setModifier("RPP_s_subLifeIncentive");

		operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();

		parameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		parameterList.add(EntireReadUtil.booleanParameter("s_CanBeSharedByMultipleRops", false));
		parameterList.add(EntireReadUtil.booleanParameter("s_InsertedViaBatch", false));
		parameterList.add(EntireReadUtil.booleanParameter("s_PreActive", true));
		parameterList.add(EntireReadUtil.enumerationValueParameter("category", "ONLINE"));
		parameterList.add(EntireReadUtil.intParameter("Key", 1));
		parameterList.add(EntireReadUtil.intParameter("prefetchFilter", -1));
		if (subscriber.getMetas().get("s_CRMTitle") != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_CRMTitle", subscriber.getMetas().get("s_CRMTitle")));
		}
		String packagge = subscriber.getMetas().get("Package");
		if (packagge == null)
			packagge = subscriber.getMetas().get("package");
		if (packagge == null)
			packagge = subscriber.getMetas().get("-");
		parameterList.add(EntireReadUtil.stringParameter("s_PackageId", packagge));
		parameterList.add(EntireReadUtil.intParameter("s_PeriodStartPoint", -1));
		operationResult.getOperation().add(operation);
		
		//// rpp read bucket
		operation = new Operation();
		operation.setName("BucketRead");
		operation.setModifier("RPP_s_subLifeIncentive");

		parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		parameterList.add(EntireReadUtil.booleanParameter("s_Active", true));
		parameterList.add(EntireReadUtil.booleanParameter("s_Valid", true));
		parameterList.add(EntireReadUtil.enumerationValueParameter("bCategory", "ONLINE"));
		parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", "MAX_DATEANDTIME"));
		parameterList.add(EntireReadUtil.intParameter("bSeriesId", 0));
		parameterList.add(EntireReadUtil.intParameter("Key", 1));
		parameterList.add(EntireReadUtil.intParameter("OfferProfileKey", 1));
		parameterList.add(EntireReadUtil.byteParameter("s_Error", (byte)0x00));
		parameterList.add(EntireReadUtil.intParameter("s_Info", 0));
		operationResult.getOperation().add(operation);
		
		//// rpp read version
		operation = new Operation();
		operation.setName("VersionRead");
		operation.setModifier("RPP_s_subLifeIncentive");

		parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		parameterList.add(EntireReadUtil.enumerationValueParameter("Category", "ONLINE"));
		parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", "MAX_DATEANDTIME"));
		parameterList.add(EntireReadUtil.intParameter("Key", 1));
		parameterList.add(EntireReadUtil.intParameter("OfferProfileKey", 1));
		operationResult.getOperation().add(operation);
		
		
		return responseData;
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
