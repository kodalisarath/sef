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
import com.ericsson.raso.sef.watergate.FloodGate;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.ericsson.sef.bes.api.subscriber.ISubscriberRequest;
import com.hazelcast.core.ISemaphore;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.wsdl._1.TisException;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ErrorInfo;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.FaultMessage;
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
		
			String edrIdentifier = (String)exchange.getIn().getHeader("EDR_IDENTIFIER"); 
			exchange.getOut().setBody(createPreactiveResponse(subscriber, request.isTransactional()));
			exchange.getOut().setHeader("EDR_IDENTIFIER", edrIdentifier);
			return;
		}
		
		logger.debug("Manila: About to Call entireReadSubscriber ");
		EntireRead entireRead = SmartServiceHelper.entireReadSubscriber(request.getCustomerId());

		if (entireRead == null) {
			logger.error("Unknon User.. Error Not Found");
			throw ExceptionUtil.toSmException(new ResponseCode(13423, "EntireRead Entity - Customer with primary key Keyname:PK,CustomerId: " + request.getCustomerId() + " does not exist"));
		}

		logger.debug("Manila: EntireRead Response is " + entireRead);
		String edrIdentifier = (String)exchange.getIn().getHeader("EDR_IDENTIFIER"); 
		
		logger.error("FloodGate acknowledging exgress...");
		FloodGate.getInstance().exgress();
		
		
		exchange.getOut().setBody(createResponse(entireRead, request.isTransactional()));
		exchange.getOut().setHeader("EDR_IDENTIFIER", edrIdentifier);

	}

	
	private Subscriber simpleRead(String customerId) throws SmException, TisException {
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
		semaphore.destroy();
		logger.info("Check if response received for simpleread subscriber");
		SubscriberInfo subscriberInfo = (SubscriberInfo) SubscriberResponseStore.remove(requestId);
		
		if (subscriberInfo.getStatus() != null && subscriberInfo.getStatus().getCode() > 0) {
			if (subscriberInfo.getStatus().getCode() == 504) {
				//throw ExceptionUtil.toSmException(ErrorCode.primaryKeyAlreadyExists1);
				ErrorInfo errorInfo = new ErrorInfo();
				errorInfo.setCode(String.valueOf(ErrorCode.primaryKeyAlreadyExists1.getCode()));
				errorInfo.setText("EntireRead Entity - Customer with primary key Keyname:PK,CustomerId: " + customerId + " does not exist");
				FaultMessage message = new FaultMessage();
				message.getErrorInfo().add(errorInfo);
				TisException exception = new TisException("EntireRead Entity - Customer with primary key Keyname:PK,CustomerId: " + customerId + " does not exist", message);
				throw new TisException(errorInfo.getText(), message);
			} else {
				throw ExceptionUtil.toSmException(new ResponseCode(subscriberInfo.getStatus().getCode(), subscriberInfo.getStatus().getDescription()));
			}
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
		Operation readCustomerOperation = new Operation();
		readCustomerOperation.setName("Read");
		readCustomerOperation.setModifier("Customer");
		
		List<Object> readCustomerParameterList = readCustomerOperation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		readCustomerParameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		readCustomerParameterList.add(EntireReadUtil.shortParameter("billCycleId", 0));
		readCustomerParameterList.add(EntireReadUtil.symbolicOrDateParameter("billCycleSwitch", "MAX_DATEANDTIME"));
		readCustomerParameterList.add(EntireReadUtil.shortParameter("billCycleIdAfterSwitch", -1));
		readCustomerParameterList.add(EntireReadUtil.intParameter("prefetchFilter", -1));
		readCustomerParameterList.add(EntireReadUtil.enumerationValueParameter("category", "ONLINE"));
		operationResult.getOperation().add(readCustomerOperation);
		
		//// customer read bucket
		Operation readCustomerBucketOperation = new Operation();
		readCustomerBucketOperation.setName("BucketRead");
		readCustomerBucketOperation.setModifier("Customer");

		List<Object> readCustomerBucketParameterList = readCustomerBucketOperation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		
		readCustomerBucketParameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		readCustomerBucketParameterList.add(EntireReadUtil.enumerationValueParameter("bCategory", "ONLINE"));
		SimpleDateFormat metaStoreFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat nsnResponseFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
		
		String date = subscriber.getMetas().get("bInValidFrom");
		if (date == null)
			readCustomerBucketParameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", "MAX_DATEANDTIME"));
		else
			try {
				metaStoreFormat.parse(date);
				readCustomerBucketParameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", date));
			} catch (ParseException e) {
				logger.error("Unparseable date(bInvalidFrom): " + date);
				readCustomerBucketParameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", "MAX_DATEANDTIME"));
			}

		date = subscriber.getMetas().get("bValidFrom");
		if (date == null)
			readCustomerBucketParameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", metaStoreFormat.format(new Date())));
		else
			try {
				metaStoreFormat.parse(date);
				readCustomerBucketParameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", subscriber.getMetas().get("bValidFrom")));
			} catch (ParseException e) {
				logger.error("Unparseable date(bValidFrom): " + date);
				logger.debug("Setting bValidFrom to 'NOW'");
				readCustomerBucketParameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", "NOW"));
			}
		readCustomerBucketParameterList.add(EntireReadUtil.intParameter("bSeriesId", 0));
		operationResult.getOperation().add(readCustomerBucketOperation);
		
		//// customer read version
		Operation readCustomerVersionOperation = new Operation();
		readCustomerVersionOperation.setName("VersionRead");
		readCustomerVersionOperation.setModifier("Customer");

		List<Object> readCustomerVersionParameterList = readCustomerVersionOperation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		
		readCustomerVersionParameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		readCustomerVersionParameterList.add(EntireReadUtil.enumerationValueParameter("category", "ONLINE"));
		
	
		date = subscriber.getMetas().get("vValidFrom");
		logger.debug("Checking for vValidFrom: " + date);
		if (date == null)
			readCustomerVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", "NOW"));
		else {
			try {
				metaStoreFormat.parse(date);
				readCustomerVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", date));
			} catch(ParseException e) {
				logger.debug("Unparseable Date from DB: " + date);
				readCustomerVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", "NOW"));
			}			
		}
			
		date = subscriber.getMetas().get("vInvalidFrom");
		logger.debug("Checking for vInvalidFrom: " + date);
		if (date == null)
			readCustomerVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", "MAX_DATEANDTIME")); 
		else {
			try {
				metaStoreFormat.parse(date);
				readCustomerVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", date));
			} catch (ParseException e) {
				readCustomerVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", "MAX_DATEANDTIME")); 
			}
		}
		operationResult.getOperation().add(readCustomerVersionOperation);
		
		
		
		
		// Read RopRead, RopBucket, RopVersion
		//// rop read
		Operation readRopOperation = new Operation();
		readRopOperation.setName("Read");
		readRopOperation.setModifier("ROP");

		List<Object> readRopParameterList = readRopOperation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		readRopParameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		readRopParameterList.add(EntireReadUtil.intParameter("Key", 1));
		readRopParameterList.add(EntireReadUtil.enumerationValueParameter("category", "ONLINE"));
		readRopParameterList.add(EntireReadUtil.intParameter("prefetchFilter", -1));
		readRopParameterList.add(EntireReadUtil.booleanParameter("AnnoFirstWarningPeriodSent", false));
		readRopParameterList.add(EntireReadUtil.booleanParameter("AnnoSecondWarningPeriodSent", false));
		readRopParameterList.add(EntireReadUtil.booleanParameter("IsBalanceClearanceOnOutpayment", true));
		if (subscriber.getMetas().get("IsCollectCallAllowed") != null)
			readRopParameterList.add(EntireReadUtil.booleanParameter("IsCollectCallAllowed", Boolean.parseBoolean(subscriber.getMetas().get("IsCollectCallAllowed"))));
		readRopParameterList.add(EntireReadUtil.booleanParameter("IsFirstCallPassed", false));
		readRopParameterList.add(EntireReadUtil.booleanParameter("IsGPRSUsed", false));
		readRopParameterList.add(EntireReadUtil.booleanParameter("IsLastRechargeInfoStored", false));
		readRopParameterList.add(EntireReadUtil.booleanParameter("IsLastTransactionEnqUsed", false));
		readRopParameterList.add(EntireReadUtil.booleanParameter("IsLocked", Boolean.parseBoolean(subscriber.getMetas().get("IsLocked"))));
		readRopParameterList.add(EntireReadUtil.booleanParameter("IsOperatorCollectCallAllowed", false));
		readRopParameterList.add(EntireReadUtil.booleanParameter("IsSmsAllowed", false));
		readRopParameterList.add(EntireReadUtil.booleanParameter("IsUSCAllowed", false));
		// skipping active end date.. since the user is not activated yet
		ListParameter counterListParameter = EntireReadUtil.listParameter("ChargedMenuAccessCounter");
		readRopParameterList.add(counterListParameter);
		for (int i=0; i<6; ++i) {
			IntElement intParameter = new IntElement();
			intParameter.setValue(0);
			counterListParameter.getElementOrBooleanElementOrByteElement().add(intParameter);
		}
		if (subscriber.getMetas().get("Tagging") != null) 
			readRopParameterList.add(EntireReadUtil.intParameter("c_TaggingStatus", Integer.parseInt(subscriber.getMetas().get("Tagging"))));
		// skipping firstcall date, since the user is not yet active
		// skipping grace date, since the user is not yet active
		if (subscriber.getMetas().get("s_CRMTitle") != null) 
			readRopParameterList.add(EntireReadUtil.stringParameter("s_CRMTitle", subscriber.getMetas().get("s_CRMTitle")));
		readRopParameterList.add(EntireReadUtil.stringParameter("LastKnownPeriod", "PreActive"));
		
		logger.debug("Check PreActiveEndDate: " + subscriber.getMetas().get("PreActiveEndDate"));
		if (subscriber.getMetas().get("PreActiveEndDate") != null) 
			readRopParameterList.add(EntireReadUtil.symbolicOrDateParameter("PreActiveEndDate", subscriber.getMetas().get("PreActiveEndDate")));
		operationResult.getOperation().add(readRopOperation);

		//// rop read bucket
		Operation readRopBucketOperation = new Operation();
		readRopBucketOperation.setName("BucketRead");
		readRopBucketOperation.setModifier("ROP");

		List<Object> readRopBucketParameterList = readRopBucketOperation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		readRopBucketParameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		readRopBucketParameterList.add(EntireReadUtil.intParameter("Key", 1));
		readRopBucketParameterList.add(EntireReadUtil.enumerationValueParameter("bCategory", "ONLINE"));
		readRopBucketParameterList.add(EntireReadUtil.intParameter("bSeriesId", 0));
		date = subscriber.getMetas().get("bInValidFrom");
		if (date == null)
			readRopBucketParameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", "MAX_DATEANDTIME"));
		else
			try {
				readRopBucketParameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", nsnResponseFormat.format(metaStoreFormat.parse(date))));
			} catch (ParseException e) {
				logger.error("Unparseable date(bInvalidFrom): " + date);
				readRopBucketParameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", "MAX_DATEANDTIME"));
			}
		// skipping bValidFrom - since preactive users are not activated yet....
		operationResult.getOperation().add(readRopBucketOperation);
	
		
		//// rop read version
		Operation readRopVersionOperation = new Operation();
		readRopVersionOperation.setName("VersionRead");
		readRopVersionOperation.setModifier("ROP");

		List<Object> readRopVersionParameterList = readRopVersionOperation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		readRopVersionParameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		readRopVersionParameterList.add(EntireReadUtil.intParameter("Key", 1));
		
		
		date = subscriber.getMetas().get("vValidFrom");
		if (date == null)
			readRopVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", "NOW"));
		else
			try {
				metaStoreFormat.parse(date);
				readRopVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", date));
			} catch (ParseException e) {
				logger.error("Unparseable date(bInvalidFrom): " + date);
				readRopVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", "NOW"));
			}

		date = subscriber.getMetas().get("vInvalidFrom");
		if (date == null)
			readRopVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", "MAX_DATETIME"));
		else
			try {
				metaStoreFormat.parse(date);
				readRopVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", date));
			} catch (ParseException e) {
				logger.error("Unparseable date(bInvalidFrom): " + date);
				readRopVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", "MAX_DATETIME"));
			}

		
		readRopVersionParameterList.add(EntireReadUtil.stringParameter("s_OfferId", "TnT"));
		operationResult.getOperation().add(readRopVersionOperation);
		
		// Read Rpp's - read, bucket, version
		//// rpp read
		Operation readRppOperation = new Operation();
		readRppOperation.setName("Read");
		readRppOperation.setModifier("RPP_s_subLifeIncentive");

		List<Object> readRppParameterList = readRppOperation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		readRppParameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		readRppParameterList.add(EntireReadUtil.booleanParameter("s_CanBeSharedByMultipleRops", false));
		readRppParameterList.add(EntireReadUtil.booleanParameter("s_InsertedViaBatch", false));
		readRppParameterList.add(EntireReadUtil.booleanParameter("s_PreActive", true));
		readRppParameterList.add(EntireReadUtil.enumerationValueParameter("category", "ONLINE"));
		readRppParameterList.add(EntireReadUtil.intParameter("Key", 1));
		readRppParameterList.add(EntireReadUtil.intParameter("OfferProfileKey", 1));
		readRppParameterList.add(EntireReadUtil.intParameter("prefetchFilter", -1));
		if (subscriber.getMetas().get("s_CRMTitle") != null) {
			readRppParameterList.add(EntireReadUtil.stringParameter("s_CRMTitle", subscriber.getMetas().get("s_CRMTitle")));
		}
		String packagge = subscriber.getMetas().get("Package");
		if (packagge == null)
			packagge = subscriber.getMetas().get("package");
		if (packagge == null)
			packagge = subscriber.getMetas().get("-");
		readRppParameterList.add(EntireReadUtil.stringParameter("s_PackageId", packagge));
		readRppParameterList.add(EntireReadUtil.intParameter("s_PeriodStartPoint", -1));
		operationResult.getOperation().add(readRppOperation);
		
		//// rpp read bucket
		Operation readRppBucketOperation = new Operation();
		readRppBucketOperation.setName("BucketRead");
		readRppBucketOperation.setModifier("RPP_s_subLifeIncentive");

		List<Object> readRppBucketParameterList = readRppBucketOperation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		readRppBucketParameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		readRppBucketParameterList.add(EntireReadUtil.booleanParameter("s_Active", true));
		readRppBucketParameterList.add(EntireReadUtil.booleanParameter("s_Valid", true));
		readRppBucketParameterList.add(EntireReadUtil.enumerationValueParameter("bCategory", "ONLINE"));
		readRppBucketParameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", "MAX_DATEANDTIME"));
		readRppBucketParameterList.add(EntireReadUtil.intParameter("bSeriesId", 0));
		readRppBucketParameterList.add(EntireReadUtil.intParameter("Key", 1));
		readRppBucketParameterList.add(EntireReadUtil.intParameter("OfferProfileKey", 1));
		readRppBucketParameterList.add(EntireReadUtil.byteParameter("s_Error", (byte)0x00));
		readRppBucketParameterList.add(EntireReadUtil.intParameter("s_Info", 0));
		operationResult.getOperation().add(readRppBucketOperation);
		
		//// rpp read version
		Operation readRppVersionOperation = new Operation();
		readRppVersionOperation.setName("VersionRead");
		readRppVersionOperation.setModifier("RPP_s_subLifeIncentive");

		List<Object> readRppVersionParameterList = readRppVersionOperation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		readRppVersionParameterList = readRppVersionOperation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		readRppVersionParameterList.add(EntireReadUtil.stringParameter("CustomerId", subscriber.getMsisdn()));
		readRppVersionParameterList.add(EntireReadUtil.enumerationValueParameter("Category", "ONLINE"));
		readRppVersionParameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", "MAX_DATEANDTIME"));
		readRppVersionParameterList.add(EntireReadUtil.intParameter("Key", 1));
		readRppVersionParameterList.add(EntireReadUtil.intParameter("OfferProfileKey", 1));
		operationResult.getOperation().add(readRppVersionOperation);
		
		
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
			} else {
				logger.error("Entire Read is present but without Customer!!");
			}

			if (entireRead.getRop() != null) {
				operationResult.getOperation().add(EntireReadUtil.createRopRead(entireRead.getRop().getRopRead()));
				operationResult.getOperation().add(EntireReadUtil.createRopBucketRead(entireRead.getRop().getRopBucketRead()));
				operationResult.getOperation().add(EntireReadUtil.createRopVersionRead(entireRead.getRop().getRopVersionRead()));
			} else {
				logger.error("Entire Read is present but without Rop!!");
			}

			if (entireRead.getRpps() != null) {
				Collection<Rpp> rpps = entireRead.getRpps();

				for (Rpp rpp : rpps) {
					
					operationResult.getOperation().add(EntireReadUtil.createRppRead(rpp.getRppRead()));
					operationResult.getOperation().add(EntireReadUtil.createRppBucketRead(rpp.getRppBucketRead()));
					operationResult.getOperation().add(EntireReadUtil.createRppVersionRead(rpp.getRppVersionRead()));
					lastIndex = rpp.getRppVersionRead().getKey();
				}
			} else {
				logger.error("Entire Read is present but without Rpp's!!");
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
			} else {
				logger.error("Entire Read is present but without Welcome Pack!!");
			}
		}
		return responseData;
	}

}
